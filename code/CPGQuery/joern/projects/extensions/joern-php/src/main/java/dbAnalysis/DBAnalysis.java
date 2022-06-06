package dbAnalysis;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
//import net.sf.jsqlparser.parser.TokenMgrException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

/**
 * @author Abeer Alhuzali
 *
 */

public class DBAnalysis {
	
	String appName;
	
	public DBAnalysis() {
		allColDefinSchema=new ArrayList<ColDef>();
		appName=null;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public ArrayList<ColDef> getAllColDefinSchema() {
		return allColDefinSchema;
	}
	public void setAllColDefinSchema(ArrayList<ColDef> allColDefinSchema) {
		this.allColDefinSchema = allColDefinSchema;
	}

	public ArrayList<ColDef> allColDefinSchema; 
	
	//For testing and generating the parsed schema in csv formate	
	public static void main (String args[]) throws IOException{
		
		
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("\nPlease enter of the folder that has the schema files (e.g., /home/user/schema): ");
//		String schemaFolder = scanner.next().trim();


 		writeToFile("schema.csv","appName\ttableName\tcolName\tcolType\tcolConstraints\targumentsList",true);

		String app;
		
		String schemaFolder = "D:\\joern-navex\\out\\artifacts\\joern_navex_projects_extensions_joern_php_main_jar\\schema";
		File folder = new File(schemaFolder);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	 /*
		    	  * the assumption here is that the name of the schema file matches 
		    	  * exactally the name of the application.
		    	  * for example: the schema file of testApp is testApp.sql (case sensitive)
		    	  * This is very important in subsequant analysis steps.
		    	  */
		         app= listOfFiles[i].getAbsolutePath();
		         DBAnalysis db = new DBAnalysis();

		 		try {
		 			db.analyzeDBSchema(app);
		 		   
		 		} catch (SQLException | JSQLParserException le1) {
		 			// TODO Auto-generated catch block

		 		}
		 		
		 		String appName = app.substring(app.lastIndexOf("/")+1, app.lastIndexOf(".sql"));
		 		db.setAppName(appName);
		 		for (ColDef cd : db.getAllColDefinSchema()){
		 			writeToFile("schema.csv",db.getAppName()+"\t"+cd.toString2().replace("`", ""),true);	
		 		}
		      } 
		    }
		    
		 

	}
	public void analyzeDBSchema( String dbFile) throws SQLException, JSQLParserException, IOException {
       
		String schema = dbSchemaFileProcessing(dbFile);
		if(schema != null)
			paeseSchema(schema);
       
	}
	private  void paeseSchema(String schema) throws JSQLParserException {
		ArrayList<ColDef> cols=new ArrayList<ColDef>();
		
		 Statements parseStatements= CCJSqlParserUtil.parseStatements(schema);
		 for (Statement st  : parseStatements.getStatements()){
		    if (st instanceof CreateTable){
		    	  CreateTable ct =(CreateTable)(st);
		    	  cols.addAll(QueryProcessing.QueryMainC(ct.toString()));
		      } 
		 }
		
		setAllColDefinSchema(cols);
	}

	private String dbSchemaFileProcessing(String dbFile) throws IOException {
		 BufferedReader bis = DBAnalysis.getInStream(dbFile);
		 String ret="", str=null;
	        {
	        	str= bis.readLine();
	            while (str !=null){
                ret+=str.trim();
               		   str=bis.readLine();
               	  
                }
	            try{
	                bis.close();
	            }
	            catch(Exception e)
	            {
	               System.out.println(" Exception while closing teh file " + dbFile + e.getStackTrace());
	            }
	        }
	       
	        return ret;
	}
	
	 public static BufferedReader getInStream(String fileN) 
	    {   
	        File file = new File(fileN);
	        FileInputStream fis = null;
	        BufferedReader br = null;
	    
	        try {
	          fis = new FileInputStream(file);
	          br = new BufferedReader(new InputStreamReader(fis));
	        }   
	        catch(Exception e)
	        {   
	            throw new Error("reading file " + file + " Exception + " + e.getMessage());
	        }   
	        return br; 
	    }
	 
	 
	 
	 public static void writeToFile(String file, String spec,  boolean append) {
			
			File yourFile= new File(file);
		     if(!yourFile.exists() &&  
		    		  yourFile.getParentFile() != null 
		    		  && !yourFile.getParentFile().exists()) {
		   		    try {
		   		    	yourFile.getParentFile().mkdirs();
		   		    	yourFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		   		} 
		    	//true is to allow for appending
		    	
		    	try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(yourFile, append)))) 
		    	{
		    	    out.println(spec);

		    	}catch (IOException e) {
		    		e.printStackTrace();
		    	}
			
		}

}




	


