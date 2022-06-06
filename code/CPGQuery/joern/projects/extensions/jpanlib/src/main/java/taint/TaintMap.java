/**
 * 
 */
package taint;

import ast.ASTNode;
import ast.expressions.Expression;

/**
 * @author abeer
 *
 */
public class TaintMap {
 
	private String sanitization;
     private String encoding; 
     private String type; //belogns to source of dest nodes
     String symbol;
     ASTNode node;
 
	
	/**
	 * @param sanitization
	 * @param encoding
	 * @param type
	 * @param symbol
	 * @param node
	 */
	public TaintMap(String sanitization, String encoding, String type, String symbol) {

		this.sanitization = sanitization;
		this.encoding = encoding;
		this.type = type;
		this.symbol = symbol;
		
	}
	public TaintMap() {
		// TODO Auto-generated constructor stub
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public ASTNode getNode() {
		return node;
	}
	public void setNode(ASTNode node) {
		this.node = node;
	}
	
	public String getSanitization() {
		return sanitization;
	}
	public void setSanitization(String sanitization) {
		this.sanitization = sanitization;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
    @Override
	public String toString() {
    	if (this.sanitization != null && this.encoding != null &&
    			this.type != null && this.symbol != null ){
		return "TaintMap [sanitization=" + sanitization + ", encoding=" + encoding + ", type=" + type + ", symbol="
				+ symbol + ", node=" + node.getNodeId() + "]";
    	}
    	return "";
	}
    
	/*public void copy(ASTNode dest) {
		TaintMap tm= dest.getTaintMap();
		if (dest.getTaintMap() == null)
			{tm = new TaintMap();
			  
			}
		tm.setSanitization(this.getSanitization());
		tm.setEncoding(this.getEncoding());
		tm.setNode(this.getNode());
		tm.setType(this.getType());
		tm.setSymbol(this.getSymbol());
		dest.setTaintMap(tm);
		
	}*/
     
     
}
