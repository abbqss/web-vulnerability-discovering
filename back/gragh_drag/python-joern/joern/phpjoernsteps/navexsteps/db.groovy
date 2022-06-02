//#!/usr/local/bin/groovy
@Grab('commons-net:commons-net:3.3')
@Grab('com.github.jsqlparser:jsqlparser:1.1')

//Author Abeer Alhuzali

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

//import net.sf.jsqlparser.parser.TokenMgrException;
import net.sf.jsqlparser.parser.TokenMgrError;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;

import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table.*;
import net.sf.jsqlparser.schema.Table;


import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.UnionOp;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;

/**
 * @author Abeer*
 */

def db_parsing(String sql, Vertex node) {
    String schema = node.toDir().db.next();


    Statements parseStatements = null;

    sql = sql.replace("'", "").replace('"', '');


    try {
        parseStatements = CCJSqlParserUtil.parseStatements(sql);
    } catch (e) { //JSQLParserException  | TokenMgrException e

        if (e.class != JSQLParserException || e.class != TokenMgrError)
            e.printStackTrace();
    }


    List<String> tableList = null;
    String colList = null, itemList = null;
    if (parseStatements != null) {
        for (Statement statement : parseStatements.getStatements()) {
            if (statement instanceof Insert) {
                Insert insertStatement = (Insert) statement;
                tableList = getTableList(insertStatement);
                colList = PlainSelect.getStringList((insertStatement.getColumns()), true, true);
                itemList = getItemList(insertStatement); //the vars
                //return itemList;
            } else if (statement instanceof Update) {
                Update updateStatement = (Update) statement;
                tableList = getTableList(updateStatement);
                List tempCol = updateStatement.getColumns();
                colList = PlainSelect.getStringList(tempCol, true, true);

                itemList = getExpList(updateStatement);

            }

        }
    }

    //*******Match the query arguments with the schema constraints*************
    if (itemList != null && colList != null && tableList != null && schema != null) {

        List vars = itemList.replace("(", "").replace(")", "").tokenize(",");
        List cols = colList.replace("(", "").replace(")", "").tokenize(",");
        schema = schema.replace("'", "").replace('"', '');


        //1- process schema
        //[[u'[[test_,  _t1_,  _col1_,  _datetimenull_,  _[NOT,  NULL]_,  _ _,  _], , [test_,  _t2_,  _col1_,  _varchar[20]_,  _[NOT,  NULL]_,  _ _,  _]]'], [u'[[test_,  _t1_,  _col1_,  _datetimenull_,  _[NOT,  NULL]_,  _ _,  _], , [test_,  _t2_,  _col1_,  _varchar[20]_,  _[NOT,  NULL]_,  _ _,  _]]'], [u'[[test_,  _t1_,  _col1_,  _datetimenull_,  _[NOT,  NULL]_,  _ _,  _], , [test_,  _t2_,  _col1_,  _varchar[20]_,  _[NOT,  NULL]_,  _ _,  _]]'], [u'[[test_,  _t1_,  _col1_,  _datetimenull_,  _[NOT,  NULL]_,  _ _,  _], , [test_,  _t2_,  _col1_,  _varchar[20]_,  _[NOT,  NULL]_,  _ _,  _]]'], [u'[[test_,  _t1_,  _col1_,  _datetimenull_,  _[NOT,  NULL]_,  _ _,  _], , [test_,  _t2_,  _col1_,  _varchar[20]_,  _[NOT,  NULL]_,  _ _,  _]]']]
        //maps var in query and data type of its col
        queryMap = [:]
        String test = ""
        tuples = schema.split('], , ');

        for (tuple in tuples) {
            def (app, tName, colName, colType, constraints, last) = tuple.tokenize('_, _'); //'_, _'
            //2- start the matching

            for (t in tableList) {

                if (t.equals(tName) || t.contains(tName)) {   //match col list

                    for (int i = 0; i < cols.size(); i++) {

                        if (cols.get(i).equals(colName) || cols.get(i).contains(colName)) {//cols.get(i).equals(colName)
                            queryMap.put(vars.get(i), colType + "__" + constraints);

                            break;
                        }
                    }
                }
            }

        }
        return queryMap;
    }

}


List<String> getTableList(Insert insert) {
    List tables = new ArrayList<String>();

    tables.add(insert.getTable().getName());
    return tables;
}

String getItemList(Insert insert) {
    String items = "";
    ItemsList list = insert.getItemsList();
    if (insert.isUseValues() && list != null)
        items += "" + list + "";

    return items;
}

List<String> getTableList(Update update) {
    List tables = new ArrayList<String>();
    if (update.getTables() != null) {
        for (Iterator t = update.getTables().iterator(); t.hasNext();) {
            Table tb = (Table) t.next();
            //tb.accept(this);
            tables.add(tb.getName());
        }
    }
    return tables;
}

String getExpList(Update update) {
    String items = "";
    List exp = update.getExpressions();
    if ((exp.size() > 0)) {
        items = PlainSelect.getStringList(exp, true, true);
    }

    return items;
}


	


	


