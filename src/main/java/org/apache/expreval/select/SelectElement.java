package org.apache.expreval.select;

import org.apache.expreval.schema.ColumnAttrib;
import org.apache.expreval.schema.HBaseSchema;
import org.apache.expreval.statement.SelectStatement;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.contrib.hbql.client.HBqlException;
import org.apache.hadoop.hbase.contrib.hbql.client.HConnection;

import java.util.List;

public interface SelectElement {

    void validate(HBaseSchema schema, HConnection connection) throws HBqlException;

    List<ColumnAttrib> getAttribsUsedInExpr();

    void assignAsNamesForExpressions(SelectStatement selectStatement);

    void assignValues(Object newobj,
                      int maxVerions,
                      Result result) throws HBqlException;

    int setParameter(String name, Object val) throws HBqlException;

    String getAsName();

    String getElementName();

    boolean hasAsName();

    boolean isAFamilySelect();

    String asString();
}