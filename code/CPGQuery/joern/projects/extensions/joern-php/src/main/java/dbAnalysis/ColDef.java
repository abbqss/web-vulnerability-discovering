package dbAnalysis;


import java.util.ArrayList;
import java.util.List;


/**
 * @author Abeer Alhuzali
 *
 */
public class ColDef {
	String colName; 
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public ArrayList<String> getColConstraints() {
		return colConstraints;
	}

	public void setColConstraints(ArrayList<String> colConstraints) {
		this.colConstraints = colConstraints;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	String colType; 
	ArrayList<String> colConstraints;
	String tableName;
	
	List<String> argumentsList; // for int(11) : it stores 11
	public List<String> getArgumentsList() {
		return argumentsList;
	}

	public void setArgumentsList(List<String> argumentsStringList) {
		this.argumentsList = argumentsStringList;		
	}
	public ColDef() {
		this.colConstraints = new ArrayList<String>();
		this.argumentsList = new ArrayList<String> ();
	}
	public ColDef(String colName, String colType,
			ArrayList<String> colConstraints, String tableName) {
		
		this.colName = colName;
		this.colType = colType;
		this.colConstraints = colConstraints;
		this.tableName = tableName;
	}

	public ColDef(String col, String tableName2) {
		this.tableName=tableName2;
		col=col.trim();
		if (col.startsWith("(") ){
			int i=col.indexOf("(");
			col=col.substring(i+1).trim();
		}

		
		this.colName=col.substring(0,col.indexOf(" ")).trim();	
		
		col=col.substring(this.colName.length()+1).trim();
		if (col.contains("("))
			  this.colType=col.substring(0,col.indexOf("(")).trim();
		else 
		    this.colType=col.substring(0,col.indexOf(" ")).trim();
		col=col.substring(colType.length()).trim();
	
		ArrayList<String> cCons=new ArrayList<String>();
		int size=col.length();
		for (int i=0; i <= size;)
		{   if (col.indexOf(" ")!=-1){ //end of line
			String temp=col.substring(0, col.indexOf(" ")).trim();
			
			if (temp.equalsIgnoreCase("NOT")  ) {
				temp+= " NULL";
			}
			else if(temp.equalsIgnoreCase("PRIMARY")){
				temp+= " KEY";
			}
			col=col.substring(temp.length()).trim();
			cCons.add(temp );
			i=temp.length();
		 }	
		else { if (col.length()!=0 && !(col.endsWith(",") ))
			     {cCons.add(col) ;
			     }
			  break;
		    }
		}
		this.colConstraints=cCons;


	}


	@Override
	public String toString() {
		return "ColDef [colName=" + colName + ", colType=" + colType
				+ ", colConstraints=" + colConstraints + ", tableName="
				+ tableName + ", argumentsList=" + argumentsList + "]";
	}

	public String toString2() {
		String str=tableName + "\t" +colName + "\t";
		if (colType != null)
			str += colType+ "\t";
		else 
			str += " \t";
		if(colConstraints != null)
			 str+= colConstraints + "\t";
		else 
			str += " \t";
		if(argumentsList != null)
			str+=argumentsList;
		else 
			str += " ";
		return str;
	}
	

}
