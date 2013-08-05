/*
 * Copyright 2007 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 *
 */
package com.taobao.csp.hadoop.job.gclog.gcparser;

import java.util.EnumMap;

public class GCDataStore extends GCStats
{
	GCDataStore(EnumMap<GCMetric, Boolean> enabled_map, int cpu_count,
		boolean has_time_zero)
	{
		super(enabled_map, cpu_count, has_time_zero);

		Class<GCMetric> c = GCMetric.class;
		_data_map = new EnumMap<GCMetric, Double>(c);
		_time_map = new EnumMap<GCMetric, Double>(c);
		for (GCMetric metric:  GCMetric.values())
		{
			_data_map.put(metric, null);
			_time_map.put(metric, null);
		}
	}

	public void add(GCMetric metric, double val)
	{
		super.add(metric, val);
		_data_map.put(metric, val);
	}

	public void add(GCMetric metric, String s)
	{
		Double val = Double.parseDouble(s);
		add(metric, val);
	}

	public Double data(GCMetric metric)
	{
		return _data_map.get(metric);
	}

	public Double time(GCMetric metric)
	{
		return _time_map.get(metric);
	}

	protected String filename(GCMetric metric, String prefix, String suffix)
	{
		StringBuilder filename = new StringBuilder();
		if (prefix != null) filename.append(prefix);
		filename.append(metric);
		if (suffix != null) filename.append(suffix);
		return filename.toString();
	}

	private EnumMap<GCMetric, Double> _data_map;
	private EnumMap<GCMetric, Double> _time_map;
}
