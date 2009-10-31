package org.apache.hadoop.hbase.contrib.hbql.statement.args;

import org.apache.expreval.client.HBqlException;
import org.apache.expreval.client.TypeException;
import org.apache.expreval.expr.node.GenericValue;
import org.apache.expreval.util.Lists;
import org.apache.hadoop.hbase.contrib.hbql.client.Query;
import org.apache.hadoop.hbase.contrib.hbql.client.Record;
import org.apache.hadoop.hbase.contrib.hbql.client.Results;
import org.apache.hadoop.hbase.contrib.hbql.statement.SelectStatement;
import org.apache.hadoop.hbase.contrib.hbql.statement.select.SelectElement;
import org.apache.hadoop.hbase.contrib.hbql.statement.select.SingleExpressionContext;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class InsertSelectValues extends InsertValueSource {

    private final SelectStatement selectStatement;
    private Iterator<Record> resultsIterator = null;
    private Record currentRecord = null;


    public InsertSelectValues(final SelectStatement selectStatement) {
        this.selectStatement = selectStatement;
    }

    public SelectStatement getSelectStatement() {
        return this.selectStatement;
    }

    public int setParameter(final String name, final Object val) throws HBqlException {
        return this.getSelectStatement().setParameter(name, val);
    }

    public void validate() throws HBqlException {

        for (final SelectElement element : this.getSelectStatement().getSelectElementList()) {
            if (element.isAFamilySelect())
                throw new TypeException("Family select items are not valid in INSERT statement");
        }

        this.getSelectStatement().validate(this.getInsertStatement().getConnection());
    }

    private Iterator<Record> getResultsIterator() {
        return this.resultsIterator;
    }

    private void setResultsIterator(final Iterator<Record> resultsIterator) {
        this.resultsIterator = resultsIterator;
    }

    private Record getCurrentRecord() {
        return this.currentRecord;
    }

    private void setCurrentRecord(final Record currentRecord) {
        this.currentRecord = currentRecord;
    }

    public void execute() throws HBqlException, IOException {
        final Query<Record> query = this.getInsertStatement().getConnection().newQuery(this.getSelectStatement());
        final Results<Record> results = query.getResults();
        this.setResultsIterator(results.iterator());
    }

    public List<Class<? extends GenericValue>> getValuesTypeList() throws HBqlException {
        final List<Class<? extends GenericValue>> typeList = Lists.newArrayList();
        for (final SelectElement element : this.getSelectStatement().getSelectElementList()) {
            if (element instanceof SingleExpressionContext) {
                final SingleExpressionContext expr = (SingleExpressionContext)element;
                final Class<? extends GenericValue> type = expr.getExpressionType();
                typeList.add(type);
            }
        }
        return typeList;
    }

    public void reset() {

    }

    public String asString() {
        return this.getSelectStatement().asString();
    }

    public Object getValue(final int i) throws HBqlException {
        final SelectElement element = this.getSelectStatement().getSelectElementList().get(i);
        final String name = element.getElementName();
        return this.getCurrentRecord().getCurrentValue(name);
    }

    public boolean isDefaultValue(final int i) throws HBqlException {
        return false;
    }

    public boolean hasValues() {
        if (this.getResultsIterator().hasNext()) {
            this.setCurrentRecord(this.getResultsIterator().next());
            return true;
        }
        else {
            return false;
        }
    }
}