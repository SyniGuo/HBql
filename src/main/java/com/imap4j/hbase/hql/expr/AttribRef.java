package com.imap4j.hbase.hql.expr;

import com.imap4j.hbase.hql.ClassSchema;
import com.imap4j.hbase.hql.FieldAttrib;
import com.imap4j.hbase.hql.HPersistException;
import com.imap4j.hbase.hql.HPersistable;

/**
 * Created by IntelliJ IDEA.
 * User: pambrose
 * Date: Aug 25, 2009
 * Time: 6:58:31 PM
 */
public class AttribRef implements ValueExpr {

    private final String attribName;
    private final ExprType type;

    public AttribRef(final ExprType type, final String attribName) {
        this.type = type;
        this.attribName = attribName;
    }

    @Override
    public Object getValue(final ClassSchema classSchema, final HPersistable recordObj) throws HPersistException {

        final FieldAttrib fieldAttrib = classSchema.getFieldAttribByField(this.attribName);

        switch (this.type) {

            case IntegerType:
            case NumberType: {
                return (Number)fieldAttrib.getValue(recordObj);
            }

            case StringType: {
                return (String)fieldAttrib.getValue(recordObj);
            }

            default:
                throw new HPersistException("Unknown type in AttribRef.getValue() - " + type);

        }

    }

}