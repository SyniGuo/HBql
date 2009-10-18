package org.apache.hadoop.hbase.hbql.query.expr.value.var;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.client.ResultMissingColumnException;
import org.apache.hadoop.hbase.hbql.query.expr.node.NumberValue;
import org.apache.hadoop.hbase.hbql.query.schema.ColumnAttrib;

public class ShortColumn extends GenericColumn<NumberValue> implements NumberValue {

    public ShortColumn(ColumnAttrib attrib) {
        super(attrib);
    }

    public Short getValue(final Object object) throws HBqlException, ResultMissingColumnException {
        if (this.getExprContext().useHBaseResult())
            return (Short)this.getColumnAttrib().getValueFromBytes((Result)object);
        else
            return (Short)this.getColumnAttrib().getCurrentValue(object);
    }
}