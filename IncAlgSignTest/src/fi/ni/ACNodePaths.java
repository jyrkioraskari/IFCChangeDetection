package fi.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.LiteralSign;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.datastructure.Node;

public class ACNodePaths {
	Set<Node> nodes = new HashSet<Node>();

	

	public ACNodePaths() {
		
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


	public void name(List<Node> node_list,TestParams p) {
		LiteralSign nodeliteralsummer = new LiteralSign();	
		for (int n = 0; n < 200; n++) {				
			nodeliteralsummer.setliteralChecksums(nodes, p);
			for (Node bn : node_list) {
				if (bn.getNodeType() != Node.LITERAL) {
					bn.setURI(bn.getLiteral_chksum());
				}
			}
			setNewIRIs();
		}
	}

	public void setNewIRIs() {
		Map<String, Node> lchecksums = new HashMap<String, Node>();
		for (Node bn : nodes) {
			bn.setCollided(false);
		}
		for (Node bn : nodes) {
			Node ex = lchecksums.put(bn.getURI(), bn);
			if (ex != null) {
				ex.setCollided(true);
				bn.setCollided(true);
			}
		}
		for (Node bn : nodes) {
			if (!bn.isCollided()) {
				bn.setNodeType(Node.IRINODE);
			}
		}
	}


	public void makeUnique(TestParams p) {

		List<Node> node_list=new ArrayList<Node>();
		for(Node n:nodes)
			node_list.add(n);
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
				 ex.setCollided(true);
				 bn.setCollided(true);
			}
		}
		for (Node bn : node_list) {
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
