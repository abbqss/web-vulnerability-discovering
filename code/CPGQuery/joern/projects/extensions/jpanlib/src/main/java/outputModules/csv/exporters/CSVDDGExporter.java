package outputModules.csv.exporters;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ast.ASTNode;
import databaseNodes.EdgeKeys;
import databaseNodes.EdgeTypes;
import ddg.DataDependenceGraph.DDG;
import ddg.DataDependenceGraph.DefUseRelation;
import outputModules.common.DDGExporter;
import outputModules.common.Writer;
import taint.TaintMap;

public class CSVDDGExporter extends DDGExporter
{

	@Override
	protected void addDDGEdge(DefUseRelation defUseRel, Map<String, Object> properties)
	{
		long srcId = Writer.getIdForObject(defUseRel.src);
		long dstId = Writer.getIdForObject(defUseRel.dst);
		Writer.addEdge(srcId, dstId, properties, EdgeTypes.REACHES);
	}
	
	

	/**
	 * Simple method that takes a DDG and writes out the edges.
	 */
	public void writeDDGEdges(DDG ddg) {
	
		
		
		for( DefUseRelation ddgEdge : ddg.getDefUseEdges())	{
			Map<String, Object> properties = new HashMap<String, Object>();
			// should always be instances of ASTNode
			if( ddgEdge.src instanceof ASTNode && ddgEdge.dst instanceof ASTNode) {
				
				Writer.setIdForObject(ddgEdge.src, ((ASTNode)ddgEdge.src).getNodeId());
				Writer.setIdForObject(ddgEdge.dst, ((ASTNode)ddgEdge.dst).getNodeId());
				properties.put( EdgeKeys.VAR, ddgEdge.symbol);
				
				for( Entry<Object, TaintMap> map : ddg.getDefUseTaint().entrySet())	{
					
					// should always be instances of ASTNode
					//NAVEX
					if( map.getKey() instanceof DefUseRelation) {
						DefUseRelation r = (DefUseRelation)map.getKey();
						if (r.src.equals(ddgEdge.src) && r.dst.equals(ddgEdge.dst)  ){
							if (map.getValue().getType().equalsIgnoreCase("src"))
								properties.put( EdgeKeys.TAINT_SRC, map.getValue().getSanitization());
							else if (map.getValue().getType().equalsIgnoreCase("dst")) 
								properties.put( EdgeKeys.TAINT_DST, map.getValue().getSanitization());
							}
					   }
				}
				addDDGEdge( ddgEdge, properties);
			}
		}
		
		//Abeer
		/*
		properties = new HashMap<String, Object>();
		for( Entry<Object, TaintMap> map : ddg.getDefUseTaint().entrySet())	{
			
			// should always be instances of ASTNode
			if( map.getKey() instanceof DefUseRelation ) {
				DefUseRelation r = (DefUseRelation)map.getKey();
				if (map.getValue().getType().equalsIgnoreCase("src"))
			         properties.put( EdgeKeys.TAINT_SRC, map.getValue().getSanitization());
				else 
					 properties.put( EdgeKeys.TAINT_DST, map.getValue().getSanitization());
				
				addDDGEdge(r, properties);
			}
		}
		*/
		// clean up
		Writer.reset();
	}
}
