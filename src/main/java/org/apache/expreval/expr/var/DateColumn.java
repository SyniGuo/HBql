package org.apache.expreval.expr.var;

import org.apache.expreval.expr.node.DateValue;
import org.apache.expreval.schema.ColumnAttrib;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.contrib.hbql.client.HBqlException;
import org.apache.hadoop.hbase.contrib.hbql.client.ResultMissingColumnException;

import java.util.Date;

public class DateColumn extends GenericColumn<DateValue> implements DateValue {

    public DateColumn(final ColumnAttrib attrib) {
        super(attrib);
    }

    public Long getValue(final Object object) throws HBqlException, ResultMissingColumnException {

        final Date val;

        if (this.getExprContext().useHBaseResult())
            val = (Date)this.getColumnAttrib().getValueFromBytes((Result)object);
        else
            val = (Date)this.getColumnAttrib().getCurrentValue(object);

        return val.getTime();
    }
}