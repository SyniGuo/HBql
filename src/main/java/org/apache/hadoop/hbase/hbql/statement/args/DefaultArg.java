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

package org.apache.hadoop.hbase.hbql.statement.args;

import org.apache.expreval.expr.MultipleExpressionContext;
import org.apache.expreval.expr.TypeSignature;
import org.apache.expreval.expr.node.GenericValue;
import org.apache.hadoop.hbase.hbql.client.HBqlException;

import java.io.Serializable;

public class DefaultArg extends MultipleExpressionContext implements Serializable {

    // We have to make value transient because Object is not serializable for hbqlfilter
    // We will compute it again on the server after reset is called
    private transient Object value = null;
    private volatile boolean computed = false;

    public DefaultArg(final Class<? extends GenericValue> exprType, final GenericValue expr) throws HBqlException {
        super(new TypeSignature(null, exprType), expr);

        // This will force the type checking to happen
        this.getValue();
    }

    public void reset() {
        this.computed = false;
    }

    public Object getValue() {

        if (!computed) {
            synchronized (this) {
                if (!computed) {
                    // Type checking happens in this call, so we force it above in the constructor
                    try {
                        this.value = this.evaluateConstant(0, false, null);
                    }
                    catch (HBqlException e) {
                        e.printStackTrace();
                        this.value = null;
                    }
                    this.computed = true;
                }
            }
        }

        return this.value;
    }

    public String asString() {
        return this.getGenericValue(0).asString();
    }

    public boolean useResultData() {
        return false;
    }
}