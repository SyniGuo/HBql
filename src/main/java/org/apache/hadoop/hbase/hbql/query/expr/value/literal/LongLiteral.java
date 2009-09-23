package org.apache.hadoop.hbase.hbql.query.expr.value.literal;

import org.apache.hadoop.hbase.hbql.client.HPersistException;
import org.apache.hadoop.hbase.hbql.query.expr.node.NumberValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.ValueExpr;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 25, 2009
 * Time: 6:58:31 PM
 */
public class LongLiteral extends GenericLiteral<NumberValue> implements NumberValue {

    private final Long value;

    public LongLiteral(final Long value) {
        this.value = value;
    }

    @Override
    public Long getValue(final Object object) {
        return this.value;
    }

    @Override
    public Class<? extends ValueExpr> validateType() throws HPersistException {
        return NumberValue.class;
    }

}