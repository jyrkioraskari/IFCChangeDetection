package fi.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.relation.InvalidRoleInfoException;

import org.apache.jena.iri.IRI;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.LiteralSign;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;

public class IncAlgsignNodeNamer {
	Set<Node> nodes = new HashSet<Node>();

	

	public IncAlgsignNodeNamer() {
		
	}

	public void setInternalGraph(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public void createInternalGraph(String directory, String filename, String datatype) {
		RDFHandler ifch = new RDFHandler();
		OntModel model = ifch.handleRDF(directory, filename, datatype);
		InternalModel im = new InternalModel();

		im.handle(model, nodes);

	}


	private void name(List<Node> init_nodes,TestParams p) {
		
		LiteralSign nodeliteralsummer = new LiteralSign();
		Set<Node> node_set=new HashSet<Node>();
		for(Node n:init_nodes)
			  node_set.add(n);  // first all
		//System.out.println("Org node set size: "+node_set.size());
		for (int n = 0; n < 200; n++) {				
			nodeliteralsummer.setliteralChecksums(node_set, p);
			for (Node bn : node_set) {
				if (bn.getNodeType() != Node.LITERAL) {
					bn.setURI(bn.getLiteral_chksum());
				}
			}
			node_set.clear();
			for(Node m:setNewIRIs())
				if(m.getNodeType()==Node.BLANKNODE)
				  node_set.add(m);
			if(node_set.size()==0)
				return;
			//System.out.println("Node set size: "+node_set.size());
		}
		
	}

	private Set<Node>  setNewIRIs() {
		Set<Node> node_set=new HashSet<Node>();
		Map<String, Node> lchecksums = new HashMap<String, Node>();
		for (Node bn : nodes) {
			bn.setCollided(false);
		}
		for (Node bn : nodes) {
			Node ex = lchecksums.put(bn.getURI(), bn);
			if (ex != null) {
				 if(bn.getNodeType()==Node.IRINODE)
					 System.err.println("Should not happen!");
				 if(ex.getNodeType()==Node.IRINODE)
					 System.err.println("Should not happen!");
				ex.setCollided(true);
				bn.setCollided(true);
			}
		}
		for (Node bn : nodes) {
			if(bn.getNodeType()==Node.BLANKNODE)
			if (!bn.isCollided()) {
				bn.setNodeType(Node.IRINODE);
				
				for (Connection e : bn.getEdges_in())
					node_set.add(Node.map.get(e.getPointedNode()));
				for (Connection e : bn.getEdges_out())
					node_set.add(Node.map.get(e.getPointedNode()));

			}
		}
		return node_set;
	}


	public void makeUnique(TestParams p) {

		List<Node> node_list=new ArrayList<Node>();
		for(Node n:nodes)
		{
			n.reset();
			node_list.add(n);
		}
		java.util.Collections.shuffle(node_list);
		
        name(node_list,p);
		
		Map<String, Integer> class_inx = new HashMap<String, Integer>();
		Map<String, Node> lchecksums = new HashMap<String, Node>();
		for (Node bn : node_list) {
			bn.setCollided(false);
		}
		for (Node bn : node_list) {
			Node ex = lchecksums.put(bn.getURI(), bn);
			if (ex != null) {
				 if(bn.getNodeType()==Node.IRINODE)
					 System.err.println("Should not happen!");
				 if(ex.getNodeType()==Node.IRINODE)
					 System.err.println("Should not happen!");
				 ex.setCollided(true);
				 bn.setCollided(true);
			}
		}
		for (Node bn : node_list) {
			if(bn.getNodeType()==Node.BLANKNODE)
			if (bn.isCollided()) {
				{
					Integer count = class_inx.get(bn.getURI());
					if (count == null)
						count = 0;
					class_inx.put(bn.getURI(), count + 1);
					if (count != 0) // to be comparable with 0 count, when 1
									// removed from list of 2 items
						bn.setURI(bn.getURI() + ".#" + count);
				}
			}
		}
	}

	

	public Set<Node> getNodes() {
		return nodes;
	}

	

	

}
