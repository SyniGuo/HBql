package org.apache.hadoop.hbase.hbql.query.expr.nullcomp;

import org.apache.hadoop.hbase.hbql.query.expr.NotValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.BooleanValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.GenericValue;

public abstract class GenericNullCompare extends NotValue<GenericNullCompare> implements BooleanValue {

    protected GenericNullCompare(final Type type, final boolean not, final GenericValue arg0) {
        super(type, not, arg0);
    }

    public String asString() {
        return this.getArg(0).asString() + " IS" + notAsString() + " NULL";
    }
}