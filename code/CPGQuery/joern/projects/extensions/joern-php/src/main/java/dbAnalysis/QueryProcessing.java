package dbAnalysis;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;



/**
 * @author Abeer Alhuzali
 *
 */
public class QueryProcessing {
     public static QueryInfo QueryMain( String sql) throws JSQLParserException{
    
    	 QueryInfo qi=new QueryInfo();
    	 CCJSqlParserManager pm = new  CCJSqlParserManager();
	
    	 Statement statement = pm.parse(new StringReader(sql));
    			 if (statement instanceof Select) {
    			 	Select selectStatement = (Select) statement;
    			 	TableNameFinder tablesNamesFinder = new TableNameFinder();
    			 	List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
    			 	PlainSelect plainSelectStatement = (PlainSelect) selectStatement.getSelectBody();
    			 	String colList=PlainSelect.getStringList(plainSelectStatement.getSelectItems());
    			    
     			 	String whereList = tablesNamesFinder.getWhereList(selectStatement);
     			 	

    			 
    			 	for (String iter :tableList) {
    			 		System.out.println("table name is "+iter);
    			 	}
    			 	System.out.println("where list  "+whereList);
    			 	
    				qi.setqType(1);
    				qi.setTNames(tableList);
    				qi.setItemNames(whereList);
    				qi.setWhere(whereList);
   				    qi.setDelSelVars(whereList);
   				    qi.setColNames(colList);

    				
    			 }
    			 else if (statement instanceof Insert) {
    				 Insert insertStatement = (Insert) statement;
     			 	TableNameFinder tablesNamesFinder = new TableNameFinder();
     			 	List<String> tableList = tablesNamesFinder.getTableList(insertStatement);
     			 	String colList=PlainSelect.getStringList ((insertStatement.getColumns()), true , true);
     			 	String itemList = tablesNamesFinder.getItemList(insertStatement);

     			 	
     			 	for (String iter : tableList) {
     			 		System.out.println("table name in insert "+iter);
     			 	}
     			 	
    			 		System.out.println("col list  "+colList);
    			 		System.out.println("item list  "+itemList);
    			 		
    			 	
     			 	qi.setqType(2);
    				qi.setTNames(tableList);
    				qi.setColNames(colList);
    				qi.setItemNames(itemList);
    				qi.setInsertVars(itemList);
    				
     			 }
    			 else if (statement instanceof Update) {
    				Update updateStatement = (Update) statement;
      			 	TableNameFinder tablesNamesFinder = new TableNameFinder();
      			    List<String> tableList = tablesNamesFinder.getTableList(updateStatement);
      			    List tempCol=updateStatement.getColumns();
     			 	String colList=PlainSelect.getStringList (tempCol, true , true);
   			 	    String whereList = tablesNamesFinder.getWhereList(updateStatement);
   			 	    String expList = tablesNamesFinder.getExpList(updateStatement);

      			 	for (String iter : tableList) {
      			 		System.out.println("table name is update "+iter);
      			 	}
      			 	System.out.println("col list  "+colList);
			 		System.out.println("where list  "+whereList); //where {x=y}
					System.out.println("exp in getExpList "+expList); // set x={ y}

      			 	qi.setqType(3);
    				qi.setTNames(tableList);
    				qi.setColNames(colList);
    				qi.setItemNames(expList);
    				qi.setWhere(whereList);
   				    
    				qi.setUpdateVars(expList); //where
    				qi.setUpdateVars(whereList); //set nnn=nnn
      			 }
    			 else if (statement instanceof Delete) {
    				 Delete deleteStatement = (Delete) statement;
       			 	TableNameFinder tablesNamesFinder = new TableNameFinder();
       			 	List<String> tableList = tablesNamesFinder.getTableList(deleteStatement);
    			 	String whereList = tablesNamesFinder.getWhereList(deleteStatement);
       			 	for (String iter : tableList) {
       			 	System.out.println("table name in delete "+iter);
       			 	}
			 		System.out.println("where list  "+whereList);

       			  qi.setqType(4);
 				  qi.setTNames(tableList);
 				  qi.setItemNames(whereList);
 				  qi.setDelSelVars(whereList);
       			 }
    			 
    			 
    			 return qi;
       			 
     }
     
     public static ArrayList<ColDef>  QueryMainC ( String sql ) throws JSQLParserException{
    	    
    	 QueryInfo qi=new QueryInfo();
    	 CCJSqlParserManager pm = new  CCJSqlParserManager();
    	 ArrayList<ColDef> allColDef=new  ArrayList<ColDef> ();
    	 Statement statement = pm.parse(new StringReader(sql));
                if (statement instanceof CreateTable) {
    				CreateTable createStatement = (CreateTable) statement;
       			 	TableNameFinder tablesNamesFinder = new TableNameFinder();
       			     allColDef= tablesNamesFinder.getCreateInfo(createStatement);
       			          			  
       			   qi.setqType(5);
       			  qi.setAllColDef(allColDef);
       			 }
    			 
    			 return allColDef;
       			 
     }
     
	public QueryProcessing() {
		
		
	}

}
