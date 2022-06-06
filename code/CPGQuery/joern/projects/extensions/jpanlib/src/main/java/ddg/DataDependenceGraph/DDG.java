package ddg.DataDependenceGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import taint.TaintMap;


public class DDG
{

	private Set<DefUseRelation> defUseEdges = new HashSet<DefUseRelation>();

	
	
	public Set<DefUseRelation> getDefUseEdges()
	{
		return defUseEdges;
	}

	public void add(Object srcId, Object dstId, String symbol)
	{
		DefUseRelation statementPair = new DefUseRelation(srcId, dstId, symbol);
		defUseEdges.add(statementPair);
	};
	
	//NAVEX 
			private Map<Object, TaintMap> defUseTaint = new HashMap<Object, TaintMap>();
			
			public Map<Object, TaintMap> getDefUseTaint() {
				return defUseTaint;
			}

			public void addTaint(DefUseRelation ddgEdge, TaintMap tm) {
				
				Map<Object, TaintMap> statementPair = new HashMap<Object, TaintMap>();
				statementPair.put(ddgEdge, tm);
				defUseTaint.putAll(statementPair);
				
			}
	
	

	/**
	 * Compares the DDG with another DDG and returns a DDGDifference object
	 * telling us which edges need to be added/removed to transform one DDG into
	 * the other.
	 * 
	 * @param other
	 * @return
	 */
	public DDGDifference difference(DDG other)
	{
		DDGDifference retval = new DDGDifference();
		List<DefUseRelation> thisEdges = new LinkedList<DefUseRelation>(
				this.getDefUseEdges());

		Set<DefUseRelation> otherEdges = new HashSet<DefUseRelation>(
				other.getDefUseEdges());

		while (thisEdges.size() > 0)
		{
			DefUseRelation elem = thisEdges.remove(0);
			if (otherEdges.contains(elem))
				otherEdges.remove(elem);
			else
				retval.addRelToRemove(elem);
		}

		for (DefUseRelation elem : otherEdges)
			retval.addRelToAdd(elem);

		return retval;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for( DefUseRelation ddgEdge : this.getDefUseEdges())
			sb.append( ddgEdge).append( "\n");
		

		return sb.toString();
	}

	

}
