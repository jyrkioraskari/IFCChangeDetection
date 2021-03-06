package fi.ni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.AlgSignSummer;
import fi.ni.nodenamer.BottomUpNamer;
import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.SimpleNamer;
import fi.ni.nodenamer.datastructure.Node;

public class SPCANodeNamer {
	Set<Node> nodes = new HashSet<Node>();

	final public AlgSignSummer nodeliteralsummer; 

	public SPCANodeNamer() {
		nodeliteralsummer = new AlgSignSummer();
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

	private void name(List<Node> node_list,TestParams p) {
		nodeliteralsummer.setliteralChecksums(nodes, p);
		BottomUpNamer buNamer = new BottomUpNamer();
		buNamer.setBottomUpChecksums(nodes, p);
		SimpleNamer sn=new SimpleNamer();
		for (Node bn : nodes) {
			if (bn.getNodeType() == Node.BLANKNODE) {	
				String psum=sn.getPSum(bn, p.maxsteps);
				bn.setURI(psum);
			}
		}
	}


	
	private boolean isSame(Node bn,Node ex)
	{
		if(bn.getAa().hasBottomUp_cksum())
		{
			if(ex.getAa().hasBottomUp_cksum())
			{
				if(bn.getAa().getBottomUp_chksum().equals(ex.getAa().getBottomUp_chksum()))
					return true;
			}
		}
		return false;
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
				if(!isSame(bn, ex))
				{
				 ex.setCollided(true);
				 bn.setCollided(true);
				}
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
