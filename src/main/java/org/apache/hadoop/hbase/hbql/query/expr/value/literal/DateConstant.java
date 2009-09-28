package org.apache.hadoop.hbase.hbql.query.expr.value.literal;

import org.apache.hadoop.hbase.hbql.client.TypeException;
import org.apache.hadoop.hbase.hbql.query.expr.node.DateValue;
import org.apache.hadoop.hbase.hbql.query.expr.node.GenericValue;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 25, 2009
 * Time: 6:58:31 PM
 */
public class DateConstant extends GenericLiteral implements DateValue {

    private static long now = System.currentTimeMillis();

    public enum Type {
        NOW(true, 0),
        MINDATE(false, 0),
        MAXDATE(false, Long.MAX_VALUE);

        final boolean adjusted;
        final long value;

        Type(final boolean adjusted, final long value) {
            this.adjusted = adjusted;
            this.value = value;
        }

        public boolean isAdjustment() {
            return this.adjusted;
        }

        public long getValue() {
            return this.value;
        }
    }

    private final Long dateval;

    public DateConstant(final Date dateval) {
        this.dateval = dateval.getTime();
    }

    public DateConstant(final Long val) {
        this.dateval = val;
    }

    public DateConstant(final Type type) {
        if (type.isAdjustment())
            this.dateval = getNow() + type.getValue();
        else
            this.dateval = type.getValue();
    }

    private static long getNow() {
        return now;
    }

    public static void resetNow() {
        now = System.currentTimeMillis();
    }

    @Override
    public Long getValue(final Object object) {
        return this.dateval;
    }

    @Override
    public Class<? extends GenericValue> validateTypes(final GenericValue parentExpr,
                                                       final boolean allowsCollections) throws TypeException {
        return DateValue.class;
    }

    @Override
    public String asString() {
        return "\"" + String.format("%ta %tb %td %tT %tZ %tY", new Date(this.dateval)) + "\"";
    }
}