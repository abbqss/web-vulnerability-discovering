package dbAnalysis;


/**
 * @author Abeer Alhuzali
 *
 */
public class QueryVar {
	
	String var;   
	public QueryVar(String var, String userVar, boolean consFlag) {

		this.consFlag = consFlag;
		this.uVar = userVar;
	    this.var=var; //the var data type
	}
	
	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}
	String uVar;
	public String getuVar() {
		return uVar;
	}
	public void setuVar(String uVar) {
		this.uVar = uVar;
	}
	boolean consFlag;
	public boolean isConsFlag() {
		return consFlag;
	}
	public void setConsFlag(boolean consFlag) {
		this.consFlag = consFlag;
	}
	

}
