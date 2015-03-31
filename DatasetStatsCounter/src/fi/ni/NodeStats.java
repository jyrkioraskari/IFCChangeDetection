package fi.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.vo.DatasetStatsVO;

public class NodeStats {
	Set<Node> nodes = new HashSet<Node>();

	public NodeStats() {
	}
	
	public void setInternalGraph(Set<Node> nodes) {
		this.nodes=nodes;
	}

	
	public void createInternalGraph(String directory, String filename,String datatype,DatasetStatsVO dsvo) {
		RDFHandler ifch = new RDFHandler();
		OntModel model=ifch.handleRDF(directory, filename, datatype,dsvo);
		InternalModel im=new InternalModel();
		im.handle(model, nodes);
	
	}

	public void analyze(DatasetStatsVO dsvo) {
		for (Node bn : nodes) {
			if (bn.getRDFClass_name().contains("IfcPropertySet"))
				bn.setNodeType(Node.BLANKNODE);
		}
		makeUnique(dsvo);

	}

	public void makeUnique(DatasetStatsVO dsvo) {
		Map<String, Integer> class_inx = new HashMap<String, Integer>();
		Map<String, Node> lchecksums = new HashMap<String, Node>();
		for (Node bn : nodes) {
			bn.setCollided(false);
		}
		for (Node bn : nodes) {
			Node ex = lchecksums.put(bn.getURI(), bn);
			if (bn.getNodeType() == Node.LITERAL)
				continue;

			if (ex != null) {
				ex.setCollided(true);
				bn.setCollided(true);
			}
		}
		long collidecount = 0;
		for (Node bn : nodes) {
			if (bn.isCollided()) {
				collidecount++;
				Integer count = class_inx.get(bn.getURI());
				if (count == null)
					count = 0;
				class_inx.put(bn.getURI(), count + 1);
				if (count != 0)
					bn.setURI(bn.getURI() + ".#" + count);
			}
		}
		dsvo.setUnique_nodes((nodes.size() - collidecount));
		dsvo.setNodes(nodes.size());
		System.out.println("Uniques : " + (nodes.size() - collidecount) + " /"
				+ nodes.size());
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	Map<Node, Set<Triple>> links = new HashMap<Node, Set<Triple>>();

	public Set<Set<Triple>> deduceMSGs() {
		Set<Set<Triple>> ret = new HashSet<Set<Triple>>();
		LinkedList<Triple> triplelist = new LinkedList<Triple>();
		for (Node n : nodes) {
			if (n.getNodeType() == Node.LITERAL)
				continue;
			triplelist.addAll(associatedTriples(n));
		}
		System.out.println("triplelist size: "+triplelist.size());
		for (Triple t : triplelist) {
			Set<Triple> s_set = links.get(t.s);
			if (s_set == null) {
				s_set = new HashSet<Triple>();
				links.put(t.s, s_set);
			}

			s_set.add(t);

			Set<Triple> o_set = links.get(t.o);
			if (o_set == null) {
				o_set = new HashSet<Triple>();
				links.put(t.o, o_set);
			}

			o_set.add(t);
		}
		LinkedList<Triple> arguments = new LinkedList<Triple>();
		Set<Triple> current;
		while (!triplelist.isEmpty()) {
			arguments.add(triplelist.removeLast());
			current = new HashSet<Triple>();
			while (!arguments.isEmpty()) {
				msgStep(triplelist, arguments, current);
			}
			ret.add(current);
		}
		return ret;
	}

	private void msgStep(LinkedList<Triple> triplelist,
			LinkedList<Triple> arguments, Set<Triple> current) {
		Triple t = arguments.removeLast();
		current.add(t);
		if ((t.s.getNodeType() == Node.IRINODE)
				&& (t.o.getNodeType() == Node.IRINODE))
			return;

		if ((t.s.getNodeType() == Node.IRINODE)
				&& (t.o.getNodeType() == Node.LITERAL))
			return;

		if (t.s.getNodeType() == Node.BLANKNODE) {
			Set<Triple> s_candidates = links.get(t.s);
			for (Triple c : s_candidates) {
				if(current.contains(c))
					continue;
				if (!c.equals(t)) {
					triplelist.remove(c);
					current.add(c);
				}
				if (c.otherEnd(t.o).getNodeType() == Node.BLANKNODE)
					arguments.addLast(c);
			}
		}
		if (t.o.getNodeType() == Node.BLANKNODE) {
			Set<Triple> o_candidates = links.get(t.o);
			for (Triple c : o_candidates) {
				if(current.contains(c))
					continue;
				if (!c.equals(t)) {
					triplelist.remove(c);
					current.add(c);
				}
				if (c.otherEnd(t.o).getNodeType() == Node.BLANKNODE)
					arguments.addLast(c);
			}
		}

	}
	
	public long estimatedTripleCount()
	{
		long count=0;
		for(Node n:nodes)
			count+=associatedTriples(n).size();
		return count;
	}

	private List<Triple> associatedTriples(Node node) {
		List<Triple> triples = new ArrayList<Triple>();

		// LITERAALIT
		List<Connection> cons_lit = node.getEdges_literals();
		for (Connection c : cons_lit) {
			triples.add(new Triple(node, c.getProperty(), c.pointedNode()));
		}

		// OUT: OSOITETUT LUOKAT
		List<Connection> cons_out = node.getEdges_out();
		for (Connection c : cons_out) {
			triples.add(new Triple(node, c.getProperty(), c.pointedNode()));

		}

		return triples;
	}

}
