package com.nearinfinity.hbase.dsl;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ResultColumn implements Column {
    
    private byte[] qualifier;
    private byte[] value;
    private HBase<?, ?> hBase;


    public ResultColumn(HBase<?, ?> hBase, byte[] qualifier, byte[] value) {
        this.hBase = hBase;
        this.qualifier = qualifier;
        this.value = value;
    }

    @Override
    public String qualifier() {
        return Bytes.toString(qualifier);
    }

    @Override
    public <U> U value(Class<U> c) {
        return hBase.fromBytes(value, c);
    }

}
