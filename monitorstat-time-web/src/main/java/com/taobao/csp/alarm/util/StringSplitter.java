/**
 * High-Speed Service Framework (HSF)
 *
 * www.taobao.com
 *  (C) ÌÔ±¦(ÖÐ¹ú) 2003-2011
 */
package com.taobao.csp.alarm.util;

/**
 * Split string content with the following format into several string slices.
 * <p>
 * 
 * <pre>
 * rule1@&lt;rule&gt;&lt;/rule&gt;
 * rule2@&lt;rule&gt;&lt;/rule&gt;
 * rule3@&lt;rule&gt;&lt;/rule&gt;
 * </pre>
 * 
 * header: <tt>rule1@</tt>, <tt>rule2@</tt>...<br />
 * content: <tt>&lt;rule&gt;&lt;/rule&gt;</tt>
 * 
 * @author yijiang
 * @since 1.4.9
 */
public final class StringSplitter {

    private String rawRule = null;

    private String[] headers = null;

    public StringSplitter(String rawRule) {
        this(rawRule, new String[0]);
    }

    public StringSplitter(String rawRule, String... headers) {
        this.rawRule = rawRule;
        this.headers = headers;
    }

    /**
     * Get the rule, specified by the rule header.
     * 
     * @param header
     *            rule header
     * @return rule content with header
     */
    public String get(final String header) {
        if (rawRule == null || rawRule.length() == 0 || header == null || header.length() == 0) {
            return null;
        }

        int beginIndex = rawRule.indexOf(header);
        if (beginIndex < 0) {
            return null;
        }

        if (headers == null || headers.length == 0) {
            return rawRule.substring(beginIndex);
        }

        // find the end index of the rule
        int endIndex = rawRule.length();
        int indexOfRuleBody = beginIndex + header.length();
        for (String h : headers) {
            int position = rawRule.indexOf(h);
            if (position >= indexOfRuleBody && position < endIndex) {
                endIndex = position;
            }
        }

        // return content rule with header
        return rawRule.substring(beginIndex, endIndex);
    }

    /**
     * Check whether the raw rule has a header.
     * 
     * @param header
     *            rule header
     * @return <tt>ture</tt>/<tt>false</tt>
     */
    public boolean has(final String header) {
        if (rawRule == null || rawRule.length() == 0 || header == null || header.length() == 0) {
            return false;
        }

        return rawRule.contains(header);
    }
}
