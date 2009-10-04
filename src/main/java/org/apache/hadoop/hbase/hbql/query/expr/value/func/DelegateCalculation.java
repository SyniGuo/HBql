package org.apache.hadoop.hbase.hbql.query.expr.value.func;

import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.client.TypeException;
import org.apache.hadoop.hbase.hbql.query.expr.node.DateValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.GenericValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.NumberValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.StringValue;
import org.apache.hadoop.hbase.hbql.query.util.HUtil;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 25, 2009
 * Time: 6:58:31 PM
 */
public class DelegateCalculation extends GenericCalculation {

    private GenericCalculation typedExpr = null;

    public DelegateCalculation(final GenericValue arg0, final Operator operator, final GenericValue arg1) {
        super(null, arg0, operator, arg1);
    }

    private GenericCalculation getTypedExpr() {
        return typedExpr;
    }

    private void setTypedExpr(final GenericCalculation typedExpr) {
        this.typedExpr = typedExpr;
    }

    @Override
    public Class<? extends GenericValue> validateTypes(final GenericValue parentExpr,
                                                       final boolean allowsCollections) throws TypeException {

        final Class<? extends GenericValue> type1 = this.getArg(0).validateTypes(this, false);
        final Class<? extends GenericValue> type2 = this.getArg(1).validateTypes(this, false);

        if (HUtil.isParentClass(StringValue.class, type1, type2))
            this.setTypedExpr(new StringCalculation(this.getArg(0), this.getOperator(), this.getArg(1)));
        else if (HUtil.isParentClass(NumberValue.class, type1, type2))
            this.setTypedExpr(new NumberCalculation(this.getArg(0), this.getOperator(), this.getArg(1)));
        else if (HUtil.isParentClass(DateValue.class, type1, type2))
            this.setTypedExpr(new DateCalculation(this.getArg(0), this.getOperator(), this.getArg(1)));
        else
            this.throwInvalidTypeException(type1, type2);

        return this.getTypedExpr().validateTypes(parentExpr, false);
    }

    @Override
    public GenericValue getOptimizedValue() throws HBqlException {
        this.optimizeArgs();
        return !this.isAConstant() ? this : this.getTypedExpr().getOptimizedValue();
    }

    @Override
    public Object getValue(final Object object) throws HBqlException {
        return this.getTypedExpr().getValue(object);
    }
}