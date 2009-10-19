package org.apache.hadoop.hbase.hbql.query.expr.value.function;

import org.apache.hadoop.hbase.hbql.client.HBqlException;
import org.apache.hadoop.hbase.hbql.client.InternalErrorException;
import org.apache.hadoop.hbase.hbql.client.ResultMissingColumnException;
import org.apache.hadoop.hbase.hbql.client.TypeException;
import org.apache.hadoop.hbase.hbql.query.expr.GenericExpr;
import org.apache.hadoop.hbase.hbql.query.expr.TypeSignature;
import org.apache.hadoop.hbase.hbql.query.expr.node.BooleanValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.DateValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.DoubleValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.FloatValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.GenericValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.IntegerValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.LongValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.ShortValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.StringValue;

public abstract class Function extends GenericExpr {

    public static enum Type {

        // Return Date
        DATE(new TypeSignature(DateValue.class, StringValue.class, StringValue.class)),
        INTERVAL(new TypeSignature(DateValue.class, LongValue.class)),
        DATELITERAL(new TypeSignature(DateValue.class)),

        // Return String
        TRIM(new TypeSignature(StringValue.class, StringValue.class)),
        LOWER(new TypeSignature(StringValue.class, StringValue.class)),
        UPPER(new TypeSignature(StringValue.class, StringValue.class)),
        CONCAT(new TypeSignature(StringValue.class, StringValue.class, StringValue.class)),
        REPLACE(new TypeSignature(StringValue.class, StringValue.class, StringValue.class, StringValue.class)),
        SUBSTRING(new TypeSignature(StringValue.class, StringValue.class, IntegerValue.class, IntegerValue.class)),
        ZEROPAD(new TypeSignature(StringValue.class, LongValue.class, IntegerValue.class)),

        // Return Number
        LENGTH(new TypeSignature(IntegerValue.class, StringValue.class)),
        INDEXOF(new TypeSignature(IntegerValue.class, StringValue.class, StringValue.class)),

        SHORT(new TypeSignature(ShortValue.class, StringValue.class)),
        INTEGER(new TypeSignature(IntegerValue.class, StringValue.class)),
        LONG(new TypeSignature(LongValue.class, StringValue.class)),
        FLOAT(new TypeSignature(FloatValue.class, StringValue.class)),
        DOUBLE(new TypeSignature(DoubleValue.class, StringValue.class)),

        DEFINEDINROW(new TypeSignature(BooleanValue.class, GenericValue.class)),
        EVAL(new TypeSignature(BooleanValue.class, StringValue.class));

        private final TypeSignature typeSignature;

        Type(final TypeSignature typeSignature) {
            this.typeSignature = typeSignature;
        }

        private TypeSignature getTypeSignature() {
            return typeSignature;
        }
    }

    private final Type functionType;

    public Function(final Type functionType, final GenericValue... exprs) {
        super(null, exprs);
        this.functionType = functionType;
    }

    protected Type getFunctionType() {
        return this.functionType;
    }

    protected TypeSignature getTypeSignature() {
        return this.getFunctionType().getTypeSignature();
    }

    protected void checkForNull(final String... vals) throws HBqlException {
        for (final Object val : vals) {
            if (val == null)
                throw new HBqlException("Null value in " + this.asString());
        }
    }

    public Class<? extends GenericValue> validateTypes(final GenericValue parentExpr,
                                                       final boolean allowsCollections) throws TypeException {

        int i = 0;
        if (this.getArgList().size() != this.getTypeSignature().getArgCount())
            throw new TypeException("Incorrect number of arguments in function " + this.getFunctionType().name()
                                    + " in " + this.asString());

        for (final Class<? extends GenericValue> clazz : this.getTypeSignature().getArgs()) {
            final Class<? extends GenericValue> type = this.getArg(i).validateTypes(this, false);
            try {
                this.validateParentClass(clazz, type);
            }
            catch (TypeException e) {
                // Catch the exception and improve message
                throw new TypeException("Invalid type " + type.getSimpleName() + " for arg " + i + " in function "
                                        + this.getFunctionName() + " in "
                                        + this.asString() + ".  Expecting type " + clazz.getSimpleName() + ".");
            }
            i++;
        }

        return this.getTypeSignature().getReturnType();
    }

    protected String getFunctionName() {
        return this.getFunctionType().name();
    }

    public GenericValue getOptimizedValue() throws HBqlException {
        this.optimizeArgs();
        if (!this.isAConstant())
            return this;
        else
            try {
                return this.getFunctionType().getTypeSignature().newLiteral(this.getValue(null));
            }
            catch (ResultMissingColumnException e) {
                throw new InternalErrorException();
            }
    }

    public String asString() {
        return this.getFunctionType().name() + super.asString();
    }
}