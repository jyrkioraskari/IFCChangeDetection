package fi.ni.nodenamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import fi.ni.TestParams;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.util.StringChecksum;

public class BottomUpNamer {
	Queue<Node> potential = new LinkedList<Node>();

	
	public String sign(Node node) {
		List<String> l_class = new ArrayList<String>();
		List<String> l_out = new ArrayList<String>();

		l_class.add("type" + node.getRDFClass_name());

		// LITERAALIT
		List<Connection> cons_lit = node.getEdges_literals();
		for (Connection c : cons_lit) {
			l_out.add(c.getProperty() + c.pointedNode().getLexicalValue());
		}

		Collections.sort(l_class);
    	Collections.sort(l_out);

		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String s : l_class) {
			if (first) {
				sb.append(s);
				first = false;
			} else
				sb.append("*" + s);

		}
		first = true;
		for (String s : l_out) {
			if (first) {
				sb.append(s);
			} else
				sb.append("*" + s);

		}
		return sb.toString();
	}
    
	

	public void setBottomUpChecksums(Boolean useFullLiterals, Set<Node> nodes, TestParams params) {

		for (Node n : nodes) {
			if (n.getRDFClass_name().equals("rdf:nil")) {
				n.getAa().setBottomUp_chksum("NIL",n);
				
				n.getAa().setBottomUp_islet(true);
				n.getAa().setBottomUp_islet_recnoiput(true);
				n.getAa().setHasBottomUp_cksum(true);
			}			
			
			if (n.getEdges_out().size() == 0) {
				if(useFullLiterals)
					n.getAa().setBottomUp_chksum(sign(n),n);
				else
					n.getAa().setBottomUp_chksum(n.getLiteral_chksum(),n);
				
			    n.getAa().setBottomUp_islet(true);
				n.getAa().setBottomUp_islet_recnoiput(true);
				n.getAa().setHasBottomUp_cksum(true);
				for (Connection c : n.getEdges_in()) {
					potential.add(c.pointedNode());
				}
			}
			// Null
			if (n.getEdges_out().size() == 1) {
				for (Connection co : n.getEdges_out()) {
					if (co.pointedNode().getRDFClass_name().equals("rdf:nil"))
					{
						if(useFullLiterals)
							n.getAa().setBottomUp_chksum(sign(n),n);
						else
						    n.getAa().setBottomUp_chksum(n.getLiteral_chksum(),n);
						
					    n.getAa().setBottomUp_islet(true);
						n.getAa().setBottomUp_islet_recnoiput(true);
						n.getAa().setHasBottomUp_cksum(true);
						for (Connection c : n.getEdges_in()) {
							potential.add(c.pointedNode());
						}
					}
				}		
			}

		}
		


		Queue<Node> t = new LinkedList<Node>();
		for (int i = 0; i < 100; i++) {  
			t.clear();
			for (Node n : potential) {
                if(isDummy(n))
                	continue;
				boolean all = true;
				boolean all_islet = true;
				for (Connection c : n.getEdges_out()) {
					try
					{
					if (!c.pointedNode().getAa().hasBottomUp_cksum())
						all = false;
					if (!c.pointedNode().getAa().isBottomUp_islet_recnoiput())
						all_islet = false;
					}
					catch(Exception e)
					{
						System.out.println("pointed node: "+c.pointedNode());
						System.exit(1);
					}
				}
				if (all_islet) {					
					if(n.getEdges_in().size()==1)
						   n.getAa().setBottomUp_islet_recnoiput(true);
					n.getAa().setBottomUp_islet(true);
				}

				if (all) {					
					List<String> s = new ArrayList<String>();
					Set<Node> bottomUPNodes = new HashSet<Node>(); 
					for (Connection c : n.getEdges_out()) {
						s.add(c.pointedNode().getAa().getBottomUp_chksum());
						bottomUPNodes.addAll(c.pointedNode().getAa().getBottomUPNodes());
					}
					Collections.sort(s);
					StringChecksum sc = new StringChecksum(params.isUseHash());
					if(useFullLiterals)
					 sc.update(sign(n));
					else
					 sc.update(n.getLiteral_chksum());
					bottomUPNodes.add(n);
					for (String st : s)
						sc.update(st);
					n.getAa().setBottomUp_chksum(sc.getChecksumValue(),bottomUPNodes);
					n.getAa().setHasBottomUp_cksum(true);
					for (Connection c : n.getEdges_in()) {
						if (!c.pointedNode().getAa().hasBottomUp_cksum())
							t.add(c.pointedNode());
					}
				}

			}
			potential.clear();
			potential.addAll(t);
		}

	

	}

	// Remove those with loose ends
	public boolean isDummy(Node n)
	{
			for (Connection e : n.getEdges_out()) {

				if (e.pointedNode() == null) {
					return true;
				}

			}
			for (Connection e : n.getEdges_in()) {

				if (e.pointedNode() == null) {
					return true;				}

			}
		
	return false;
	}
	
}
