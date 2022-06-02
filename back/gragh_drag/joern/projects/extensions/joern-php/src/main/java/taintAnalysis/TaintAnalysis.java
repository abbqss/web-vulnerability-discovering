
package taintAnalysis;

import java.util.HashSet;

import ast.ASTNode;
import ast.expressions.ArgumentList;
import ast.expressions.ArrayIndexing;
import ast.expressions.AssignmentExpression;
import ast.expressions.BinaryExpression;
import ast.expressions.BinaryOperationExpression;
import ast.expressions.CallExpressionBase;
import ast.expressions.CastExpression;
import ast.expressions.Constant;
import ast.expressions.DoubleExpression;
import ast.expressions.Expression;
import ast.expressions.Identifier;
import ast.expressions.IntegerExpression;
import ast.expressions.NewExpression;
import ast.expressions.PostDecOperationExpression;
import ast.expressions.PostIncOperationExpression;
import ast.expressions.PreDecOperationExpression;
import ast.expressions.PreIncOperationExpression;
import ast.expressions.PropertyExpression;
import ast.expressions.StringExpression;
import ast.expressions.Variable;
import ast.php.expressions.MethodCallExpression;
import ast.php.expressions.StaticCallExpression;
import cfg.nodes.ASTNodeContainer;
import ddg.DataDependenceGraph.DDG;
import ddg.DataDependenceGraph.DefUseRelation;
import udg.php.useDefAnalysis.environments.AssignmentEnvironment;
import udg.php.useDefAnalysis.environments.AssignmentWithOpEnvironment;
import taint.TaintMap;

/**
 * @author Abeer Alhuzali
 * Enhancing CPG by adding  sanitization tags
 */
public class TaintAnalysis {

	public void startTaintAnalysis(DDG ddg) {

		System.err.println("Starting taint analysis to decide the sanitization status of variables.. ");
		for( DefUseRelation ddgEdge : ddg.getDefUseEdges()){
			HashSet<TaintMap> tm = null;
			tm = analyzeTaint(ddgEdge.src, ddgEdge.symbol);
			if (tm != null )
			{    for (TaintMap t :tm){
				t.setType("src");
				ddg.addTaint(ddgEdge, t);
			}
			}
			tm = null;
			tm=  analyzeTaint(ddgEdge.dst, ddgEdge.symbol);
			if (tm != null )
			{   for (TaintMap t :tm){ 
				t.setType("dst");
				ddg.addTaint(ddgEdge, t);
			}
			}

		}

	}

	private HashSet<TaintMap> analyzeTaint(Object node, String symbol) {
		HashSet<TaintMap> tm = null;
		if (node instanceof ASTNodeContainer){
			tm = processTaint(((ASTNodeContainer)node).getASTNode(), symbol);
		}
		else if (node instanceof ASTNode){
			tm = processTaint(((ASTNode)node), symbol);
		}
		return tm;
	}

	private HashSet<TaintMap> processTaint(ASTNode astNode, String symbol) {
		HashSet<TaintMap> tm =null;

		switch(astNode.getTypeAsString()){
		case "AssignmentExpression":
		case "AssignmentByRefExpression":
			tm = assignmentTaint(astNode, symbol);
			break;

		case "BinaryOperationExpression":
			tm = binaryOperationTaint(astNode, symbol);
			break;

		case "CastExpression":
			tm = CastExpressionTaint(astNode, symbol);
			break;


		}
		return tm;

	}

	private HashSet<TaintMap> CastExpressionTaint(ASTNode astNode, String symbol) {
		HashSet<TaintMap> tmList =  new HashSet<TaintMap>();
		CastExpression cast = (CastExpression)astNode;
		if ( cast.getFlags() != "FLAG_TYPE_ARRAY" ||
				cast.getFlags() != "FLAG_TYPE_STRING" ||
				cast.getFlags() != "FLAG_TYPE_OBJECT" ){

			TaintMap tm= new TaintMap("san-all", null, null, symbol);
			tmList.add(tm);
		}
		return tmList;
	}

	private HashSet<TaintMap> binaryOperationTaint(ASTNode astNode, String symbol) {
		HashSet<TaintMap> tmList =  new HashSet<TaintMap>();
		BinaryOperationExpression bo = (BinaryOperationExpression)astNode;
		Expression left = bo.getLeft();
		Expression right = bo.getRight();

		switch (bo.getFlags()){
		case "BINARY_IS_SMALLER": case "BINARY_IS_SMALLER_OR_EQUAL":
		case "BINARY_IS_GREATER": case "BINARY_ADD":
		case "BINARY_SUB":case "BINARY_MUL" :
		case "BINARY_DIV": case "BINARY_MOD": case "BINARY_POW":
			if (left instanceof Variable ){
				if ( ((Variable)left).getNameExpression().getEscapedCodeStr().equals(symbol) ){

					TaintMap tm= new TaintMap("san-all", null, null, symbol);
					tmList.add(tm);
				}
			}
			if ( right instanceof Variable){
				if ( ((Variable)right).getNameExpression().getEscapedCodeStr().equals(symbol)){
					TaintMap tm= new TaintMap("san-all", null, null, symbol);
					tmList.add(tm);
				}
			}
			break;

		case "BINARY_IS_IDENTICAL": case "BINARY_IS_EQUAL":
			if (left instanceof Variable && (right instanceof Constant || right instanceof DoubleExpression
					|| right instanceof IntegerExpression || right instanceof StringExpression)){
				if ( ((Variable)left).getNameExpression().getEscapedCodeStr() != null ){
					if ( ((Variable)left).getNameExpression().getEscapedCodeStr().equals(symbol) ){

						TaintMap tm= new TaintMap("san-all", null, null, symbol);
						tmList.add(tm);
					}
				}
			}
			break;
		}


		return tmList;
	}

	private HashSet<TaintMap> assignmentTaint(ASTNode astNode, String symbol) {
		AssignmentExpression assignNode = (AssignmentExpression) astNode;
		assignNode.getLeft();
		TaintMap tm= null;
		HashSet<TaintMap> tmList =  new HashSet<TaintMap>();
		Expression right = assignNode.getRight();
		if (right instanceof CallExpressionBase && ! (right instanceof NewExpression)
				&& !(right instanceof MethodCallExpression) 
				&& !(right instanceof StaticCallExpression)){

			CallExpressionTaint(right, symbol, "", tmList);
		}
		else if (right instanceof Constant || right instanceof DoubleExpression
				|| right instanceof IntegerExpression || right instanceof StringExpression
				|| right instanceof  PreIncOperationExpression
				|| right instanceof PreDecOperationExpression
				|| right instanceof PostIncOperationExpression
				|| right instanceof PostDecOperationExpression){

			tm= new TaintMap("san-all", null, null, symbol);

			tmList.add(tm);
		}

		return tmList;
	}

	private void CallExpressionTaint(ASTNode node, String symbol, String san, HashSet<TaintMap> tmList) {
		CallExpressionBase call = ((CallExpressionBase)node);
		String funcName;
		if (call.getTargetFunc() instanceof Identifier)
			funcName = ((Identifier)call.getTargetFunc()).getNameChild().getEscapedCodeStr();
		else 
			return ;
		ArgumentList args = call.getArgumentList();


		if (Sanitization.find(Sanitization.F_SANITIZATION_STRING, funcName))
			san +=  "san-string:";
		else if (Sanitization.find(Sanitization.F_SANITIZATION_SQL, funcName))
			san += "san-sql:";
		else if (Sanitization.find(Sanitization.F_SANITIZATION_SQL_SPECIAL, funcName)){
			if (call.getArgumentList().size() >= 2
					&& call.getArgumentList().getArgument(1).getEscapedCodeStr() != null)
				if( call.getArgumentList().getArgument(1).getEscapedCodeStr().equals("ENT_QUOTES"))
					san+="san-sql:";
		}


		else if (Sanitization.find(Sanitization.F_SANITIZATION_XSS, funcName))
			san += "san-xss:";
		else if(Sanitization.find(Sanitization.F_SANITIZATION_SYSTEM, funcName))
			san+= "san-system:";
		else if(Sanitization.find(Sanitization.F_SANITIZATION_FILE, funcName))
			san+= "san-file:";

		for(Expression arg :args){
			CallExpressionTaintHelper(arg, symbol, funcName, san,  tmList);
		}

	}

	private void CallExpressionTaintHelper(Expression arg, String symbol, String funcName, String san , HashSet<TaintMap> tmList) {
		if (arg instanceof Variable && 
				((Variable)arg).getNameExpression().getEscapedCodeStr()!= null){
			if (((Variable)arg).getNameExpression().getEscapedCodeStr().equals(symbol)){

				TaintMap tm= new TaintMap(san, null, null, symbol);

				tmList.add(tm);

			}
		}
		else if (arg instanceof ArrayIndexing ){

			Expression temp = ((ArrayIndexing)arg).getArrayExpression();
			if (temp instanceof Variable 
					&& ( ((Variable)temp).getNameExpression().getEscapedCodeStr().equals("_GET") 
							|| ((Variable)temp).getNameExpression().getEscapedCodeStr().equals("_POST") 
							|| ((Variable)temp).getNameExpression().getEscapedCodeStr().equals("_REQUEST")
							|| ((Variable)temp).getNameExpression().getEscapedCodeStr().equals("_SESSION") )) {

				TaintMap tm= new TaintMap(san, null, null, symbol);

				tmList.add(tm);
			}
		}
		else if (arg instanceof CallExpressionBase){
			CallExpressionTaint(arg, symbol, san , tmList);
		}


	}






}
