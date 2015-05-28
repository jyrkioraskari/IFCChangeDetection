package fi.ni.nodenamer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.nodenamer.datastructure.Path;

public class RealChangeDetection {
	public Map<Node,Node> nodemap1=new HashMap<Node,Node>();
	public Map<Node,Node> nodemap2=new HashMap<Node,Node>();
	
	public void checkGuidBijections(Set<Node> nodes1, Set<Node> nodes2) {

		for (Node n1 : nodes1) {			
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			if (n1.getNodeType() == Node.IRINODE) {
				for (Node n2 : nodes2) {
					if (n2.getNodeType() == Node.LITERAL)
						continue;
					if (n2.getNodeType() == Node.IRINODE)
						if (n1.getURI().equals(n2.getURI())) {
							n1.getAa().setGuidBijection(true);
							n2.getAa().setGuidBijection(true);
							nodemap1.put(n1,n2);
							nodemap2.put(n2,n1);
						}
				}
			}

		}
		for (Node n1 : nodes1) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			if (n1.getNodeType() == Node.IRINODE) {
				if (!n1.getAa().isGuidBijection())
					n1.getAa().setChangeSetGuid(true);
			}
		}
		for (Node n2 : nodes2) {
			if (n2.getNodeType() == Node.LITERAL)
				continue;
			if (n2.getNodeType() == Node.IRINODE) {
				if (!n2.getAa().isGuidBijection())
					n2.getAa().setChangeSetGuid(true);
			}
		}

		for (Node n1 : nodes1) {
			if (n1.getAa().isChangeSetGuid()) {

				for (Connection e : n1.getEdges_out()) {
					if(setTheReachedNodes(n1,e.pointedNode(), 15))
					{
						for (Node bun : reached_nodes) {
							if(bun.getNodeType()==Node.BLANKNODE)
							  bun.getAa().setDerivedChange(true);
						}
					}

				}
			}

		}
		for (Node n2 : nodes2) {
			if (n2.getAa().isChangeSetGuid()) {
				for (Connection e : n2.getEdges_out()) {
					if(setTheReachedNodes(n2,e.pointedNode(), 15))
					{
						for (Node bun : reached_nodes) {
							if(bun.getNodeType()==Node.BLANKNODE)
							  bun.getAa().setDerivedChange(true);
						}
					}

				}

			}

		}
	}


	
	
	static Set<Node> reached_nodes = new HashSet<Node>();
	private static boolean setTheReachedNodes(Node origin,Node node, int maxpath) {

		reached_nodes.clear();
		reached_nodes.add(origin);
		reached_nodes.add(node);
		Queue<Path> q = new LinkedList<Path>();
		Path p0 = new Path(node);

		q.add(p0);
		while (!q.isEmpty()) {
			Path p1 = q.poll();
            
			if (p1.getSteps_taken() > maxpath) {
				System.out.println("max");
				return false;
			}

			if(handleLinks(q, p1, p1.getLast_node().getEdges_in()))
				return false;
			if(handleLinks(q, p1, p1.getLast_node().getEdges_out()))
				return false;
			
		}
		return true;
	}


	private static boolean handleLinks(Queue<Path> q, Path p1, List<Connection> edges) {
		
		for (Connection e : edges) {
			Node u = e.pointedNode();
			System.out.println("-- "+u.getRDFClass_name()+" "+e.getProperty());
			if(reached_nodes.contains(u))
				continue;	
			if(e.getProperty().equals("relatingMaterial"))
				continue;
			if(e.getProperty().equals("relatedElements"))
				continue;
			if(e.getProperty().equals("placementRelTo"))
			  continue;
			if(e.getProperty().equals("parentContext"))
				continue;
			
			if(u.getAa().isGuidBijection())
			{
				if(u.getRDFClass_name().equals("rdf:nil"))
					continue;
				System.out.println("guid class: "+u.getRDFClass_name()+" "+e.getProperty());
				return true;
			}
			Path p2 = new Path(p1, e);
			q.add(p2);
			reached_nodes.add(u);
		}
		return false;
	}


}
