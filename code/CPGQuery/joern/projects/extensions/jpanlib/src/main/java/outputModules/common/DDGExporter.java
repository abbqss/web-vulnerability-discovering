package outputModules.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import databaseNodes.EdgeKeys;
import ddg.DataDependenceGraph.DDG;
import ddg.DataDependenceGraph.DefUseRelation;
import taint.TaintMap;

public abstract class DDGExporter
{
	public void addDDGToDatabase(DDG ddg)
	{

		Map<String, Object> properties = new HashMap<String, Object>();
		Set<DefUseRelation> defUseEdges = ddg.getDefUseEdges();

		if (defUseEdges == null)
			return;

		for (DefUseRelation defUseRel : defUseEdges)
		{
			properties.put( EdgeKeys.VAR, defUseRel.symbol);
			addDDGEdge(defUseRel, properties);
		}
		properties = new HashMap<String, Object>();
     for( Entry<Object, TaintMap> map : ddg.getDefUseTaint().entrySet())	{
			
			// should always be instances of ASTNode
    	 //NAVEX
			if( map.getKey() instanceof DefUseRelation ) {
				DefUseRelation r = (DefUseRelation)map.getKey();
				if (map.getValue().getType().equalsIgnoreCase("src"))
			         properties.put( EdgeKeys.TAINT_SRC, map.getValue());
				else 
					 properties.put( EdgeKeys.TAINT_DST, map.getValue());
				
				addDDGEdge(r, properties);
			}
		}
	}

	protected abstract void addDDGEdge(DefUseRelation defUseRel, Map<String, Object> properties);
}
