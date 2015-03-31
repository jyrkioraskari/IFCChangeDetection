package fi.ni.nodenamer;

import java.util.Set;

import fi.ni.TestParams;
import fi.ni.nodenamer.datastructure.Node;

public class LiteralSign {

	
	public void setliteralChecksums(Set<Node> nodes, TestParams params) {
		for (Node node : nodes) {
			if (node.getNodeType() == Node.LITERAL)
				continue;


			if (node.getNodeType() == Node.IRINODE) {
				node.setLiteral_chksum(node.getURI());
			} else {
				setLChecksum4AnonNode(node,params);
			}
		}
	}
	
	

	
	private void setLChecksum4AnonNode(Node node,TestParams params) {
		String signature = "Static";

		node.setLiteral_chksum(signature);		
	}


}
