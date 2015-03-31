package fi.ni;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;

public class GenStats {
	Set<Node> nodes = new HashSet<Node>();

	
	public GenStats() {
	}

	public void analyze(String directory, String filename, String datatype) {
		RDFHandler ifch = new RDFHandler();
		OntModel model=ifch.handleRDF(directory, filename, datatype);
		InternalModel im=new InternalModel();
		im.handle(model, nodes);
		
		for (Node bn : nodes) {
			if (bn.getRDFClass_name().contains("IfcPropertySet"))
				bn.setNodeType(Node.BLANKNODE);
		}

	}
	public void makeUnique() {
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
		System.out.println("Uniques : " + (nodes.size() - collidecount) + " /"
				+ nodes.size());
	}

	public Set<Node> getNodes() {
		return nodes;
	}

}
