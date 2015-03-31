package fi.ni.nodenamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.nodenamer.stats.ClassLiteralCksumBag;
import fi.ni.util.StringChecksum;

public class FQ_LiteralChecksummer {

	public void setliteralChecksums(Set<Node> nodes, ClassLiteralCksumBag class_chksums_bag) {
		for (Node node : nodes) {			
			if(node.getNodeType()==Node.LITERAL)
			{
				continue;
			}

			if(node.getNodeType()==Node.IRINODE) {
				node.setLiteral_chksum(node.getURI());
				if (node.getRDFClass_name().equals("rdf:nil"))
					node.getAa().setLiteral_prob(0.2);
				else
					node.getAa().setLiteral_prob(0); 
			} else {
				setLChecksum4AnonNode(class_chksums_bag, node);
			}
		}
	}

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

	private void setLChecksum4AnonNode(ClassLiteralCksumBag class_chksums_bag, Node node) {
		String signature = sign(node);
		StringChecksum lchecksum = new StringChecksum(true);
		lchecksum.update(signature);

		node.setLiteral_chksum(lchecksum.getChecksumValue());		
		class_chksums_bag.add(node.getRDFClass_name(), lchecksum.getChecksumValue());		
	}

}
