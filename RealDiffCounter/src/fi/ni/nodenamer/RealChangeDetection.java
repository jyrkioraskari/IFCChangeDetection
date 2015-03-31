package fi.ni.nodenamer;

import java.util.Set;

import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;


public class RealChangeDetection {
	public static void checkGuidBijections(Set<Node> nodes1, Set<Node> nodes2) {
		for (Node n1 : nodes1) {
			if (n1.getNodeType() == Node.LITERAL) 
				continue;
			if (n1.getNodeType() == Node.IRINODE) {
				for (Node n2 : nodes1) {
					if (n2.getNodeType() == Node.LITERAL)
						continue;
					if (n2.getNodeType() == Node.IRINODE)
						if (n1.getURI().equals(n2.getURI())) {
							n1.getAa().setGuidBijection(true);
							n2.getAa().setGuidBijection(true);
						}
				}
			}

		}
		checkListBijections(nodes1, nodes2);
	}

	private static void setListClasses(Node bn, Node start) {
		start.getAa().setDerivedChange(true);

		for (Connection e : start.getEdges_out()) {
			if (e.getProperty().endsWith("rest")) {
				setListClasses(bn, e.pointedNode());
			}

		}
	}

	private static void checkListBijections(Set<Node> nodes1, Set<Node> nodes2) {
		for (Node n1 : nodes1) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			if (n1.getNodeType() == Node.IRINODE) {
				if (!n1.getAa().isGuidBijection()) {
					for (Connection e : n1.getEdges_out()) {

						if (e.pointedNode().getRDFClass_name().equals("rdf:list")) {

							setListClasses(n1, e.pointedNode());
						}

					}

				}
			}

		}

		for (Node n2 : nodes2) {
			if (n2.getNodeType() == Node.LITERAL)
				continue;
			if (n2.getNodeType() == Node.IRINODE) {
				if (!n2.getAa().isGuidBijection()) {
					for (Connection e : n2.getEdges_out()) {
						if (e.pointedNode().getRDFClass_name().equals("rdf:list")) {
							setListClasses(n2, e.pointedNode());
						}

					}

				}

			}
		}

	}


	
	
}
