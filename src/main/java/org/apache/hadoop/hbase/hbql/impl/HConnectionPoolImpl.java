/*
 * Copyright (c) 2010.  The Apache Software Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hbase.hbql.impl;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.client.HConnection;
import org.apache.hadoop.hbase.hbql.client.HConnectionPool;

public class HConnectionPoolImpl extends ElementPool<HConnectionImpl> implements HConnectionPool {

    private final HBaseConfiguration hbaseConfiguration;
    private final int maxReferencesPerTable;

    public HConnectionPoolImpl(final int initPoolSize,
                               final int maxPoolSize,
                               final String poolName,
                               final HBaseConfiguration hbaseConfiguration,
                               final int maxPoolReferencesPerTablePerConnection) throws HBqlException {
        super(poolName, maxPoolSize);

        this.hbaseConfiguration = (hbaseConfiguration == null) ? new HBaseConfiguration() : hbaseConfiguration;
        this.maxReferencesPerTable = maxPoolReferencesPerTablePerConnection;

        for (int i = 0; i < initPoolSize; i++)
            this.addElementToPool();
    }

    public HBaseConfiguration getHBaseConfiguration() {
        return this.hbaseConfiguration;
    }

    public int getMaxReferencesPerTable() {
        return this.maxReferencesPerTable;
    }

    protected HConnectionImpl newElement() throws HBqlException {
        return new HConnectionImpl(this.getHBaseConfiguration(), this, this.getMaxReferencesPerTable());
    }

    public HConnection takeConnection() throws HBqlException {
        return this.take();
    }
}