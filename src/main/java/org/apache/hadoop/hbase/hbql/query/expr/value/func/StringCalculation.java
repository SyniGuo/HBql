package org.apache.hadoop.hbase.hbql.query.expr.value.func;

import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.client.TypeException;
import org.apache.hadoop.hbase.hbql.query.expr.node.GenericValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.StringValue;
import org.apache.hadoop.hbase.hbql.query.expr.value.literal.StringLiteral;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Sep 7, 2009
 * Time: 9:51:01 PM
 */
public class StringCalculation extends GenericCalculation {

    public StringCalculation(final GenericValue expr1, final Operator operator, final GenericValue expr2) {
        super(expr1, operator, expr2);
    }

    @Override
    public Class<? extends GenericValue> validateTypes(final GenericValue parentExpr,
                                                       final boolean allowsCollections) throws TypeException {
        return this.validateType(StringValue.class);
    }

    @Override
    public GenericValue getOptimizedValue() throws HBqlException {

        this.setExpr1(this.getExpr1().getOptimizedValue());
        this.setExpr2(this.getExpr2().getOptimizedValue());

        return this.isAConstant() ? new StringLiteral(this.getValue(null)) : this;
    }

    @Override
    public String getValue(final Object object) throws HBqlException {

        final String val1 = (String)this.getExpr1().getValue(object);
        final String val2 = (String)this.getExpr2().getValue(object);

        switch (this.getOperator()) {
            case PLUS:
                return val1 + val2;
            default:
                throw new HBqlException("Invalid operator: " + this.getOperator());
        }
    }
}