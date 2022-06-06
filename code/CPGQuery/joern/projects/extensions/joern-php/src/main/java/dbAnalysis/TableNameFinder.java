package dbAnalysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
//import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
//import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
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
import net.sf.jsqlparser.statement.update.Update;



/**
 * 
 * code from jsqlParser home page
 * @modified by Abeer Alhuzali 
 */
public class TableNameFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor {
	private List<String> tables;
	private List<String> items;
	private String table;


	public List<String> getTableList(Select select) {
		tables = new ArrayList<String>();
		select.getSelectBody().accept(this);
		PlainSelect ps = (PlainSelect) select.getSelectBody();
		String tb=ps.getFromItem().toString();
		if(tb!=null)
			tables.add(tb);
		else 
			tables.add("");
		return tables;
	}
	public String getWhereList(Select select) {
		PlainSelect plainSelectStatement = (PlainSelect) select.getSelectBody();
		String items = "";
		Expression exp = plainSelectStatement.getWhere();
		if ((exp!=null)){
			items+=""+exp+"";
		}
		return items;
	}
	public List<String> getTableList(Insert insert) {
		tables = new ArrayList<String>();
		insert.getTable().accept(this);
		tables.add(insert.getTable().getName());
		return tables;
	}
	public String getItemList(Insert insert) {
		String items="";// = new ArrayList<String>();
		ItemsList list=insert.getItemsList();
		if(insert.isUseValues() && list!=null) 
		  items += ""+list+"";
		
		return items;
	}
	public List<String> getTableList(Update update) {
		tables = new ArrayList<String>();
	
		
		if (update.getTables() != null) {
			for (Iterator t = update.getTables().iterator(); t.hasNext();) {
				Table tb = (Table) t.next();
				tb.accept(this);
				tables.add(tb.getName());
			}
		}
		return tables;
	}
	public String getWhereList(Update update) {
		String items = "";
		Expression exp = update.getWhere();
		
		
		if ((exp!=null)){
			items+=""+exp+"";
		}
		
			return items;
	}
	public String getExpList(Update update) {
		String items = "";
		List exp = update.getExpressions();
		
		if ((exp.size()>0)){
			items=PlainSelect.getStringList(exp, true, true);
		}
		
			return items;
	}
	public List<String> getTableList(Delete delete) {
		tables = new ArrayList<String>();
		delete.getTable().accept(this);
		tables.add(delete.getTable().getName());
		return tables;
	}
	public String getWhereList(Delete delete) {
		String items = "";
		Expression exp = delete.getWhere();
		if ((exp!=null)){
			items+=""+exp+"";
		}
		
			return items;
	}
	//Create
   public ArrayList<ColDef> getCreateInfo(CreateTable createStatement) {
		

		ArrayList<ColDef> allColDef= new ArrayList<ColDef> ();
		
		createStatement.getTable().accept(this);

		
		for (ColumnDefinition cd : createStatement.getColumnDefinitions()){
			 ColDef col=new ColDef();
			 ArrayList<String> colSpec= new ArrayList<String> ();
			 
              col.setTableName(createStatement.getTable().getName());
              // to capture int(11)
			  col.setColType( cd.getColDataType().getDataType()+cd.getColDataType().getArgumentsStringList());
              //TODO: we dont need this line anymore
			  col.setArgumentsList(cd.getColDataType().getArgumentsStringList());
              col.setColName(cd.getColumnName());
             if(cd.getColumnSpecStrings()!=null)
			    colSpec.addAll( cd.getColumnSpecStrings());
			 col.setColConstraints(colSpec);
			 System.out.println("CREATE query is : "+col.toString());
			 allColDef.add(col);	
		}
		return allColDef;
	}
	
	public void visit(PlainSelect plainSelect) {
		if (plainSelect.getFromItem() != null)
			plainSelect.getFromItem().accept(this); // to allow for a select query without from clause
		
		if (plainSelect.getJoins() != null) {
			for (Iterator joinsIt = plainSelect.getJoins().iterator(); joinsIt.hasNext();) {
				Join join = (Join) joinsIt.next();
				join.getRightItem().accept(this);
			}
		}
		if (plainSelect.getWhere() != null)
			plainSelect.getWhere().accept(this);

	}


   @Override
	public void visit(SubSelect subSelect) {
		subSelect.getSelectBody().accept(this);
	}
   @Override
	public void visit(Addition addition) {
		visitBinaryExpression(addition);
	}
   @Override
	public void visit(AndExpression andExpression) {
		visitBinaryExpression(andExpression);
	}

   @Override
   public void visit(Between between) {
		between.getLeftExpression().accept(this);
		between.getBetweenExpressionStart().accept(this);
		between.getBetweenExpressionEnd().accept(this);
	}
   @Override
	public void visit(Column tableColumn) {
	}
   @Override
	public void visit(Division division) {
		visitBinaryExpression(division);
	}
   @Override
	public void visit(DoubleValue doubleValue) {
	}
   @Override
	public void visit(EqualsTo equalsTo) {
		visitBinaryExpression(equalsTo);
	}
   @Override
	public void visit(Function function) {
	}
   @Override
	public void visit(GreaterThan greaterThan) {
		visitBinaryExpression(greaterThan);
	}
   @Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		visitBinaryExpression(greaterThanEquals);
	}
  
  
   @Override
	public void visit(IsNullExpression isNullExpression) {
	}
   @Override
	public void visit(JdbcParameter jdbcParameter) {
	}
   @Override
	public void visit(LikeExpression likeExpression) {
		visitBinaryExpression(likeExpression);
	}
   @Override
	public void visit(ExistsExpression existsExpression) {
		existsExpression.getRightExpression().accept(this);
	}
   @Override
	public void visit(LongValue longValue) {
	}
   @Override
	public void visit(MinorThan minorThan) {
		visitBinaryExpression(minorThan);
	}

   @Override
   public void visit(MinorThanEquals minorThanEquals) {
		visitBinaryExpression(minorThanEquals);
	}
	  @Override
	public void visit(Multiplication multiplication) {
		visitBinaryExpression(multiplication);
	}
	  @Override
	public void visit(NotEqualsTo notEqualsTo) {
		visitBinaryExpression(notEqualsTo);
	}
	  @Override
	public void visit(NullValue nullValue) {
	}
	  @Override
	public void visit(OrExpression orExpression) {
		visitBinaryExpression(orExpression);
	}
	  @Override
	public void visit(Parenthesis parenthesis) {
		parenthesis.getExpression().accept(this);
	}

	  @Override
	  public void visit(StringValue stringValue) {
	}
	  @Override
	public void visit(Subtraction subtraction) {
		visitBinaryExpression(subtraction);
	}
	 
	public void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}
	  @Override
	public void visit(ExpressionList expressionList) {
		for (Iterator iter = expressionList.getExpressions().iterator(); iter.hasNext();) {
			Expression expression = (Expression) iter.next();
			expression.accept(this);
		}

	}
	  @Override
	public void visit(DateValue dateValue) {
	}
	  @Override
	public void visit(TimestampValue timestampValue) {
	}
	  @Override
	public void visit(TimeValue timeValue) {
	}
	  @Override
	public void visit(CaseExpression caseExpression) {
	}
	  @Override
	public void visit(WhenClause whenClause) {
	}

	  @Override
	public void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		subjoin.getJoin().getRightItem().accept(this);
	}
	@Override
	public void visit(Concat arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Matches arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(BitwiseAnd arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(BitwiseOr arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(BitwiseXor arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(MultiExpressionList arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(SignedExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(JdbcNamedParameter arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(InExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(AllComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(AnyComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(CastExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Modulo arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(AnalyticExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WithinGroupExpression wgexpr) {

	}

	/*@Override
	public void visit(WithinGroupExpression arg0) {
		// TODO Auto-generated method stub
		
	}*/
	@Override
	public void visit(ExtractExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(IntervalExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(OracleHierarchicalExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(RegExpMatchOperator arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(JsonExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(RegExpMySQLOperator arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(UserVariable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(NumericBind arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(KeepExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(MySQLGroupConcat arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(RowConstructor arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Table arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(LateralSubSelect arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(ValuesList arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(SetOperationList arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(WithItem arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(HexValue arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(JsonOperator arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(OracleHint arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(TimeKeyExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(DateTimeLiteralExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(NotExpression arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(TableFunction arg0) {
		// TODO Auto-generated method stub
		
	}

}


