package dbAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author Abeer Alhuzali
 *
 */
public class QueryInfo {
	
	int qType; //select =1 insert=2 update = 3 delete= 4  create=5
	public int getqType() {
		return qType;
	}

	public void setqType(int qType) {
		this.qType = qType;
	}
	List<String> tNames; //t name
	public List<String> getTNames() {
		return tNames;
	}

	public void setTNames(List<String> tn) {
		this.tNames = tn;
	}

	public QueryInfo(int qType, List<String> tableList, List<String> colNames) {
		this.qType=qType;
		this.tNames=tableList;
		this.colNames=colNames;
		
	}
	public QueryInfo() {
		this.qType=0;
		this.tNames=new ArrayList<String>();
		this.colNames=new ArrayList <String>();
		this.qVars=new ArrayList<String>();
		this.mapQvarsQuetsList=new ArrayList<String>();
		this.allColDef=new ArrayList<ColDef >();  //this is used only for create query

	}
	
	public QueryInfo (QueryInfo q) {
		this(q.qType, q.tNames, q.colNames);
		this.qVars=q.qVars;
		this.mapQvarsQuetsList=q.mapQvarsQuetsList;
		this.allColDef=q.allColDef;
		this.colName= q.colName;
		this.itemName= q.itemName;
		this.qColumnsVarMap= q.qColumnsVarMap;
		this.where= q.where;
		

	}
	public QueryInfo(QueryInfo qInfo, ArrayList<String> qv) {
		this(qInfo);
		this.qVars= qv;
	}
	List<String> colNames; //t name
	String colName; //t name

	public List<String> getColNames() {
		return colNames;
	}

	public void setColNames(List<String> colList) {
       this.colNames=colList;		
	}
	//this function has the list of cols in one string e.g Select A,B,C
	public void setColNames(String colList) {
	       this.colName=colList;		
		}
	public String getColName() {
	       return this.colName;		
		}

public ArrayList<ColDef> allColDef; 
	
	public ArrayList<ColDef> getAllColDef() {
		return allColDef;
	}

	public void setAllColDef(ArrayList<ColDef> allColDef) {
		this.allColDef = allColDef;
	}	
	
	String where;
	public String getWhere() {
		return where;
	}

	public void setWhere(String whereList) {
		this.where=whereList;
		
	}
	
	String itemName;
	public String getItemName() {
		return itemName;
	}

	public void setItemNames(String itemList) {
		this.itemName=itemList;		
	}
	//this contains a mapping between colname -- var in the where clause
	Map<String, String> qColumnsVarMap;
	
	public Map<String, String> getqColumnsVarMap() {
		return qColumnsVarMap;
	}

	private void setqColumnsVarMap(Map<String, String> map) {
		if (this.qColumnsVarMap == null)
			this.qColumnsVarMap = new HashMap<String, String>();
        
		this.qColumnsVarMap.putAll( map);		
	}
	
	@Override
	public String toString() {
		return "QueryInfo [Type=" + qType + ", table Name(s)=" + tNames
				+ ", colNames=" + colNames + ", colName=" + colName
				+ ", itemName=" + itemName + 
				", QueryVars=" + this.qVars + "]";
	}

	public ArrayList<String> qVars;
	
	public ArrayList<String> mapQvarsQuetsList;


	
	public ArrayList<String> getMapQvarsQuetsList() {
		return this.mapQvarsQuetsList;
	}

	public void setMapQvarsQuetsList(ArrayList<String> mapQvarsQuetsList) {
		this.mapQvarsQuetsList = mapQvarsQuetsList;
	}

	public ArrayList<String> getqVars() {
		return this.qVars;
	}

	public void setInsertVars(String itemList) {
		String qIndicator;
		if(itemList.startsWith("(") && itemList.endsWith(")")){
			itemList=itemList.replace("(", "").replace(")", "");
		}
		for(String var: itemList.split(",")){
			qIndicator="no";
			if (var!=null){

				var=var.trim();
				if(var.startsWith("(") || var.endsWith(")")){
					var=var.replace("(", "").replace(")", "");
				}
				else if(var.startsWith("\\") || var.endsWith("\\")){
					var=var.replace("\\", "");
				}
				if (var.contains("ZZ"))
					{var=var.replace("ZZ", "$");
					 if((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\"")))
						 qIndicator="yes";
					 var=var.replaceAll("'", "").replaceAll("\"", "");
					}
				
				if (var.contains("XX"))
					var=var.replace("XX", "[").replace("'", "");
				if (var.contains("YY"))
					var=var.replace("YY", "]").replace("'", "");
				if( var.length()>=2 ){
				 //System.out.println("var is "+var);	
				  if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) && var.charAt(1) =='_' ){
					  qIndicator="yes";
					 var=var.replace("'", "").replace("'", "").replace("\"", "");
				  }
				}
				this.qVars.add(var);
				this.mapQvarsQuetsList.add(qIndicator);
	}
			
  }
		System.out.println("item vars  "+this.qVars);
		System.out.println("query indicarot vars  "+this.mapQvarsQuetsList);
	
 }

	public void setUpdateVars(String whereList) {
		String var; String qIndicator;
		for(String var1: whereList.split(",")){
			qIndicator="no";
			if (var1!=null){
			  var1=var1.trim();
				
			  if(var1.split("=").length == 2){ 
				var=(var1.split("="))[1]; //the var after = 
				var=var.trim();
				if(var.startsWith("(") || var.endsWith(")")){
						var=var.replace("(", "").replace(")", "");
					}
				if(var.startsWith("\\") || var.endsWith("\\")){
					var=var.replace("\\", "").replace("\\", "");
				}
				if (var.contains("ZZ"))
					{var=var.replace("ZZ", "$");
					if((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\"")))
						 qIndicator="yes";
					 var=var.replaceAll("'", "").replaceAll("\"", "");
					}
				if (var.contains("XX"))
					var=var.replace("XX", "[").replace("'", "");
				if (var.contains("YY"))
					var=var.replace("YY", "]").replace("'", "");
				if( var.length()>=2 ){
				 if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\"")))  ){
					qIndicator="yes";
					var=var.replace("'", "").replace("'", "").replace("\"", "");
				 }
				 
				}
				this.qVars.add(var);
				this.mapQvarsQuetsList.add(qIndicator);

	           }
			  else if(var1.split("=").length > 2){
				  String where=null;
				  if (var1.contains(" where ") ){
					  where=var1.substring((var1.indexOf("where")) +5).trim();
					  var1=var1.substring(0, var1.indexOf("where"));
				  }
				  else if ( var1.contains(" Where ")){
					  where=var1.substring((var1.indexOf("Where")) +5).trim();
					  var1=var1.substring(0, var1.indexOf("Where"));
				  }
				   else if( var1.contains(" WHERE ")){
					   where=var1.substring((var1.indexOf("WHERE")) +5).trim();
					   var1=var1.substring(0, var1.indexOf("WHERE"));
					  }
						 //var=(var1.split("="))[1];
				  if(where != null && var1!=null){
					      var1=var1.trim();
					      where=where.trim();
					      if(where.startsWith("(") || where.endsWith(")")){
								where=where.replace("(", "").replace(")", "");
							}
					      
						  if(var1.split("=").length == 2){ 
							var=(var1.split("="))[1]; //the var after = 
							var=var.trim();
							if (var.contains("ZZ"))
							  { var=var.replace("ZZ", "$");
								if((var.startsWith("'") && var.endsWith("'") ) || (var.startsWith("\"") && var.endsWith("\"")))
								  qIndicator="yes";
							    var=var.replaceAll("'", "").replaceAll("\"", "");
							}
							if(var.startsWith("\\") || var.endsWith("\\")){
								var=var.replace("\\", "").replace("\\", "");
							}
							if (var.contains("XX"))
								var=var.replace("XX", "[").replace("'", "");
							if (var.contains("YY"))
								var=var.replace("YY", "]").replace("'", "");
							if( var.length()>=2 ){
							 if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) && var.charAt(1) =='_' )
							 {qIndicator="yes";
								var=var.replace("'", "").replace("'", "").replace("\"", "");
							 }
							}
							this.qVars.add(var);
							this.mapQvarsQuetsList.add(qIndicator);

				           }
						  setDelSelVars( where);
					   }
				}
			  else if(var1.split("=").length < 2){
				        var=var1;
				        if(var.startsWith("(") || var.endsWith(")")){
							var=var.replace("(", "").replace(")", "");
						}
						var=var.trim();
						if (var.contains("ZZ")){
							var=var.replace("ZZ", "$");
							if((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\"")))
								  qIndicator="yes";
							    var=var.replaceAll("'", "").replaceAll("\"", "");	
						}
						if(var.startsWith("\\") || var.endsWith("\\")){
							var=var.replace("\\", "").replace("\\", "");
						}
						if (var.contains("XX"))
							var=var.replace("XX", "[").replace("'", "");
						if (var.contains("YY"))
							var=var.replace("YY", "]").replace("'", "");
						if( var.length()>=2 ){
						 if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) && var.charAt(1) =='_' ){
							 qIndicator="yes";
							var=var.replace("'", "").replace("'", "").replace("\"", "");
						 }
						}
						this.qVars.add(var);
						this.mapQvarsQuetsList.add(qIndicator);

			          
			  }
		}		
  }
		System.out.println("item vars  "+this.qVars);
		System.out.println("indicaotr   "+this.mapQvarsQuetsList);	

	}

	public void setDelSelVars(String whereList) {
		String temp=",", qIndicator;
        if (whereList.contains(" AND "))
        	temp="AND";
        else if(whereList.contains(" and "))
        	temp="and";
        
    String ret=splitWhereList(whereList, temp);
        if(ret.contains(" OR ") || ret.contains(" or ")){
        	splitWhereList(ret, "OR");
        }
		System.out.println("item vars  "+this.qVars);
		System.out.println("indicaotr   "+this.mapQvarsQuetsList);	

	}

	private String splitWhereList(String whereList, String temp) {
		String qIndicator, ret="";
		  for(String var1: whereList.split(temp)){
			  ret=var1;
			  System.out.println("temp is "+var1);
			  qIndicator="no";
			if (var1!=null){
				var1=var1.trim();
				 String t="";
				  if(var1.contains("<>"))
					  t="<>";
				  else if (var1.contains("!="))
					  t="!=";
				  else if (var1.contains("IN"))
					  t="IN";
				  else if (var1.contains("like") || var1.contains("LIKE") || var1.contains("Like"))
					  t="LIKE";
			  //for(int i=0; i < var1.split("=").length; i++)
			  if(var1.split("=").length == 2){
				String var=(var1.split("="))[1]; //the var after = 
				String var0 = (var1.split("=")) [0].trim();
				var=var.trim();
				if (var.contains("ZZ")){
					var=var.replace("ZZ", "$");
					if((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\"")))
						  qIndicator="yes";
					    var=var.replaceAll("'", "").replaceAll("\"", "");
					}
				if(var.startsWith("\\") || var.endsWith("\\")){
					var=var.replace("\\", "").replace("\\", "");
				}
				if (var.contains("XX"))
					var=var.replace("XX", "[").replace("'", "");
				if (var.contains("YY"))
					var=var.replace("YY", "]").replace("'", "");
				if (var.contains("(") || var.contains(")") )
					var=var.replace("(", "").replace(")", "");
				if( var.length()>=2 ){
				   if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) ){
					 qIndicator="yes";  
					var=var.replace("'", "").replace("\"", "");
				   }
				   else if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) && var.charAt(1) =='_' ){
						 qIndicator="yes";  
							var=var.replace("'", "").replace("\"", "");
						   }
				}
				this.qVars.add(var);
				Map<String , String > map = new HashMap<String, String>();
				map.put(var0, var);
				this.setqColumnsVarMap(map);
				this.mapQvarsQuetsList.add(qIndicator);

	      }
			 
			  else if(var1.split("<>").length == 2 || var1.split("!=").length == 2){
				  String var=(var1.split(t))[1]; //the var after = 
				  String var0 = (var1.split(t))[0];
					var=var.trim();
					if (var.contains("ZZ")){
						var=var.replace("ZZ", "$");
						if((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\"")))
							  qIndicator="yes";
						    var=var.replaceAll("'", "").replaceAll("\"", "");
				       }
					if(var.startsWith("\\") || var.endsWith("\\")){
						var=var.replace("\\", "").replace("\\", "");
					}
					if (var.contains("XX"))
						var=var.replace("XX", "[").replace("'", "");
					if (var.contains("YY"))
						var=var.replace("YY", "]").replace("'", "");
					if (var.contains("(") || var.contains(")") )
						var=var.replace("(", "").replace(")", "");
					if( var.length()>=2 ){
				    	if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) ){
				    	qIndicator="yes";	
						  var=var.replace("'", "").replace("'", "").replace("\"", "");
				    	}
				    	else if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) && var.charAt(1) =='_' ){
							 qIndicator="yes";  
								var=var.replace("'", "").replace("\"", "");
							   }
					}
					this.qVars.add(var);
					Map<String , String > map = new HashMap<String, String>();
					map.put(var0, var);
					this.setqColumnsVarMap(map);
					this.mapQvarsQuetsList.add(qIndicator);

			  }
			  
			  else if(var1.split("like").length == 2 || var1.split("Like").length == 2 || var1.split("LIKE").length == 2){
				  String var=(var1.split(t))[1]; //the var after = 
				  String var0 =(var1.split(t))[0];
					var=var.trim();
					if (var.contains("ZZ")){
						var=var.replace("ZZ", "$");
						if((var.startsWith("'") && var.endsWith("'") ) || (var.startsWith("\"") && var.endsWith("\"")))
							  qIndicator="yes";
						    var=var.replaceAll("'", "").replaceAll("\"", "");
						}
					if(var.startsWith("\\") || var.endsWith("\\")){
						var=var.replace("\\", "").replace("\\", "");
					}
					if (var.contains("XX"))
						var=var.replace("XX", "[").replace("'", "");
					if (var.contains("YY"))
						var=var.replace("YY", "]").replace("'", "");
					if (var.contains("(") || var.contains(")") )
						var=var.replace("(", "").replace(")", "");
					if (var.contains("%"))
						var=var.replace("%", "").replace("'", "");
					if( var.length()>=2 ){
				    	if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\"")))  ){
				    		qIndicator="yes";
						    var=var.replace("'", "").replace("'", "").replace("\"", "");
				    	}
				    	else if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) && var.charAt(1) =='_' ){
							 qIndicator="yes";  
								var=var.replace("'", "").replace("\"", "");
							   }
					}
					this.qVars.add(var);
					Map<String , String > map = new HashMap<String, String>();
					map.put(var0, var);
					this.setqColumnsVarMap(map);
					this.mapQvarsQuetsList.add(qIndicator);

			  }	
			  
			  
			  else if(var1.split("IN").length == 2 ){
				  String var=(var1.split("IN"))[1]; //the var after = 
				  String var0 =(var1.split(t))[0];

					var=var.trim();
					if (var.contains("ZZ")){
						var=var.replace("ZZ", "$");
						if((var.startsWith("'")|| var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\"")))
							  qIndicator="yes";
						    var=var.replaceAll("'", "").replaceAll("\"", "");
						}
					if(var.startsWith("\\") || var.endsWith("\\")){
						var=var.replace("\\", "").replace("\\", "");
					}
					if (var.contains("XX"))
						var=var.replace("XX", "[").replace("'", "");
					if (var.contains("YY"))
						var=var.replace("YY", "]").replace("'", "");
					if (var.contains("%"))
						var=var.replace("%", "").replace("'", "");
					if (var.contains("(") || var.contains(")") )
						var=var.replace("(", "").replace(")", "");
					if( var.length()>=2 ){
				    	if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) ){
				    		qIndicator="yes";
						    var=var.replace("'", "").replace("'", "").replace("\"", "");
				    	}
				    	else if (((var.startsWith("'") || var.endsWith("'") ) || (var.startsWith("\"") || var.endsWith("\""))) && var.charAt(1) =='_' ){
							 qIndicator="yes";  
								var=var.replace("'", "").replace("\"", "");
							   }
					}
					this.qVars.add(var);
					Map<String , String > map = new HashMap<String, String>();
					map.put(var0, var);
					this.setqColumnsVarMap(map);
					this.mapQvarsQuetsList.add(qIndicator);

			  }	
			  
	}		
  }
	return ret;	
	}

	

	

	
	
}
