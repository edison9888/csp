package com.taobao.csp.hadoop.biz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.hadoop.util.LocalUtil;


public class UrlPatternMatcherImpl implements UrlPatternMatcher {

	private static final Logger logger = Logger.getLogger(UrlPatternMatcherImpl.class);

	private static final String HOST_PATTERN_CATALOG = "host";
	private static final String SUFFIX_PATTERN_CATALOG = "suffix";
	private static final String FULL_PATTERN_CATALOG = "full";

	private static final int MAX_HOST_LENGTH = 64;

	private Map<String, List<Pattern>> hostPatternMap = Collections.emptyMap();
	private List<Pattern> fullPatterns = Collections.emptyList();
	private List<SuffixPattern> suffixPatterns = Collections.emptyList();

	public UrlPatternMatcherImpl fromProperties(String fileName) {
		try {
			final Map<String, List<Pattern>> hostPatternMap = new LinkedHashMap<String, List<Pattern>>(128);
			final List<SuffixPattern> suffixPatterns = new ArrayList<SuffixPattern>(32);
			final List<Pattern> fullPatterns = new ArrayList<Pattern>(32);
			
			InputStream is = UrlPatternMatcherImpl.class.getClassLoader().getResourceAsStream(fileName);
			if (is != null) {
				try {
					InputStreamReader inputStreamReader = new InputStreamReader(is, "GBK");
					BufferedReader bufferedReader =  new BufferedReader(inputStreamReader);
					
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						
						if (StringUtils.isNotBlank(line) && line.trim().charAt(0) != '#') {
							int index = line.indexOf('!');
							if (index != -1 && index + 1 < line.length()) {
								String catalog = line.substring(0, index);
								String pattern = line.substring(index + 1);
								if (HOST_PATTERN_CATALOG.equals(catalog)) {
									addToMultiMap(hostPatternMap, LocalUtil.getDomainFromUrl(pattern), Pattern.compile(pattern));
									continue;
								} else if (SUFFIX_PATTERN_CATALOG.equals(catalog)) {
									if (pattern.charAt(0) == '/') {
										suffixPatterns.add(new SuffixPattern("http://[\\S]+" + pattern, pattern));
									} else {
										suffixPatterns.add(new SuffixPattern("http://[\\S]+/" + pattern, pattern));
									}
									continue;
								} else if (FULL_PATTERN_CATALOG.equals(catalog)) {
									fullPatterns.add(Pattern.compile(pattern));
									continue;
								}
							}
							logger.warn("unknown URL pattern: " + line);
						}
					}

					this.hostPatternMap = hostPatternMap;
					this.suffixPatterns = suffixPatterns;
					this.fullPatterns = fullPatterns;
				} finally {
					LocalUtil.closeStream(is);
				}
			} else {
				logger.error("can not find file resource from ClassLoader: " + fileName);
			}
		} catch (Exception e) {
			logger.error("fail to build patterns from " + fileName, e);
		}
		
		return this;
	}

	/**
	 * 1 remove www.
	 * 2 remove parameters
	 * 3 remove last '/'
	 */
	private String cleanUrl(String url) {
		StringBuilder appender = new StringBuilder(url.length());

		int start = url.indexOf("://");
		if (start == -1) {
			start = 0;
		} else {
			start += 3;
			appender.append(url, 0, start);
			if (url.indexOf("www.", start) == start) {
				start += 4;
			}
		}

		boolean removeSlash = true;
		for (int i = start; i < url.length(); i++) {
			char c = url.charAt(i);
			if (c == '/') {
				if (!removeSlash) {
					removeSlash = true;
					appender.append(c);
				}
			} else if (c == '&' || c == '#' || c == '?') {
				// parameter appears, actually the url is cleaned before this
				break;
			} else {
				removeSlash = false;
				if (c != '|' && c >= 32 && c < 127) {
					appender.append(c);
				}
			}
		}

		// remove last '/'
		int length = appender.length();
		if (length > 1 && appender.charAt(length - 1) == '/') {
			appender.deleteCharAt(length - 1);
		}
		return appender.toString();
	}

	private String replaceHost(String url, String replacement) {
		int start = url.indexOf("://");
		if (start == -1) {
			start = 0;
		} else {
			start += 3;
		}
		int end = url.indexOf('/', start);
		if (end == -1) {
			end = url.length();
		}
		return url.substring(0, start) + replacement + url.substring(end);
	}

	public String getMatchedUrl(String url) {
		if (url.startsWith("http://")) {
			url = cleanUrl(url);
			String host = LocalUtil.getDomainFromUrl(url);

			if (host.length() > MAX_HOST_LENGTH) {
				host = LocalUtil.truncateWithEllipsis(host, MAX_HOST_LENGTH);
				url = replaceHost(url, host);
			}

			String ret = null;

			List<Pattern> patterns = hostPatternMap.get(host);
			if (patterns != null) {
				// host-based
				ret = matchPatterns(url, patterns);
			}

			if (ret == null) {
				// suffix
				ret = matchSuffixs(url, suffixPatterns);
			}

			if (ret == null) {
				// fullPatterns
				ret = matchPatterns(url, fullPatterns);
			}

			return ret == null ? url : ret;
		}
		
		return url;
	}

	private String matchPatterns(String url, List<Pattern> patterns) {
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(url);
			if (matcher.matches()) {
				if (matcher.groupCount() > 0) {
					String original = pattern.pattern();
					StringBuilder appender = new StringBuilder(url.length());
					int begin = 0;
					for (int i = 1; i <= matcher.groupCount(); i++) {
						int gStart = original.indexOf('(', begin);
						int gEnd = original.indexOf(')', gStart + 1);
						if (gStart != -1 && gEnd != -1) {
							appender.append(original, begin, gStart)
									.append(matcher.group(i));
							begin = gEnd + 1;
						} else {
							break;
						}
					}
					appender.append(original, begin, original.length());
					return appender.toString();
				} else {
					return pattern.pattern();
				}
			}
		}
		return null;
	}

	private String matchSuffixs(String url, List<SuffixPattern> patterns) {
		for (SuffixPattern pattern : patterns) {
			if (pattern.matches(url)) {
				return pattern.pattern();
			}
		}
		return null;
	}

	private static class SuffixPattern {
		final String pattern;
		final String suffix;
		SuffixPattern(String pattern, String suffix) {
			this.pattern = pattern;
			this.suffix = suffix;
		}
		boolean matches(String str) {
			return str.endsWith(suffix);
		}
		String pattern() {
			return pattern;
		}
//		String suffix() {
//			return suffix;
//		}
	}
	
	private void addToMultiMap(final Map<String, List<Pattern>> map, String domain, Pattern pattern) {
		List<Pattern> list = map.get(domain);
		
		if (list == null) {
			list = new ArrayList<Pattern>();
			map.put(domain, list);
		}
		
		list.add(pattern);
	}
}
