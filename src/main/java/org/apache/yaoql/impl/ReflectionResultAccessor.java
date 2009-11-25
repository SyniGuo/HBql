/*
 * Copyright (c) 2009.  The Apache Software Foundation
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

package org.apache.yaoql.impl;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.mapping.ColumnAttrib;
import org.apache.hadoop.hbase.hbql.mapping.ResultAccessor;
import org.apache.hadoop.hbase.hbql.statement.NonStatement;
import org.apache.hadoop.hbase.hbql.statement.StatementContext;
import org.apache.hadoop.hbase.hbql.statement.select.SelectElement;

import java.util.List;

public class ReflectionResultAccessor extends ResultAccessor {

    public ReflectionResultAccessor(final Object object) {
        super(new NonStatement(ReflectionMapping.getReflectionMapping(object), null));
    }

    public Object newObject(final StatementContext statementContext,
                            final List<SelectElement> selectElementList,
                            final int maxVersions,
                            final Result result) throws HBqlException {
        return null;
    }

    public ColumnAttrib getColumnAttribByName(final String name) throws HBqlException {
        return this.getMapping().getAttribByVariableName(name);
    }

    public ColumnAttrib getColumnAttribByQualifiedName(final String familyName, final String columnName) throws HBqlException {
        return null;
    }
}