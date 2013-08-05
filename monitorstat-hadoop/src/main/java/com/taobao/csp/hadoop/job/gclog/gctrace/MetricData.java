package com.taobao.csp.hadoop.job.gclog.gctrace;

import java.util.ArrayList;
public class MetricData {

        private String name;
        private ArrayList<Double> times;
        private ArrayList<Double> data;
        private int index;
        private int length;

        public boolean hasMore() {
            return index < length;
        }

        public String getName() {
            return name;
        }

        public double getTime() {
            assert hasMore();
            return times.get(index);
        }

        public double getData() {
            assert hasMore();
            return data.get(index);
        }

        public void moveToNext() {
            assert hasMore();
            ++index;
        }

        public MetricData(String name,
                ArrayList<Double> times,
                ArrayList<Double> data) {
            assert times.size() == data.size();

            this.name = name;
            this.times = times;
            this.data = data;
            this.index = 0;
            this.length = times.size();
        }
    }
