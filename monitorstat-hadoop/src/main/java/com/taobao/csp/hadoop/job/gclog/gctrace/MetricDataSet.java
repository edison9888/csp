package com.taobao.csp.hadoop.job.gclog.gctrace;

import java.util.ArrayList;

public class MetricDataSet extends ArrayList<MetricData> {

	private MetricData last;

	public void addMetricData(MetricData data) {
		add(data);
	}

	public boolean hasMore() {
		for (MetricData data : this) {
			if (data.hasMore()) {
				return true;
			}
		}
		return false;
	}

	public void moveToNext() {
		assert last == getEarliest();
		last.moveToNext();
	}

	public MetricData getEarliest() {
		assert hasMore();

		MetricData ret = null;

		for (MetricData data : this) {
			if (data.hasMore()) {
				if (ret == null) {
					ret = data;
				} else {
					if (data.getTime() < ret.getTime()) {
						ret = data;
					}
				}
			}
		}
		assert ret != null;

		last = ret;
		return ret;
	}
}