package fi.ni;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.nodenamer.fqtable.DatasetsBag;

public class ClassLiteralFrequencyTableMaker {
	private ClassLiteralFrequencyTable clfrequencytable;
	
	public ClassLiteralFrequencyTableMaker(String codebase_name)
	{
		clfrequencytable=new ClassLiteralFrequencyTable(codebase_name);
	}
	
	public void close()
	{
		clfrequencytable.close();
	}
	
	
	private void addClassLiteralInstances(String filename,Set<Node> nodes, DatasetsBag datasets_bag) {
		nameListsClasses(nodes);
		for (Node node : nodes) {			
			if(node.getNodeType()==Node.BLANKNODE) {
				List<Connection> cons_lit = node.getEdges_literals();
				for (Connection c : cons_lit) {
					datasets_bag.add(filename,node.getRDFClass_name(), c.getProperty() ,c.pointedNode().getLexicalValue());
				}
			}
		}
	}

	private void nameListsClasses(Set<Node> nodes) {
		for (Node node : nodes) {
			if (node.getNodeType() == Node.LITERAL) {
				continue;
			}
			if (node.getRDFClass_name().equals("rdf:list"))
				continue;
			for (Connection e : node.getEdges_out()) {

				if (e.pointedNode().getRDFClass_name().equals("rdf:list")) {
					nameListClasses(node, e.pointedNode(),e.getProperty());
				}

			}
		}

	}
	
	private void nameListClasses(Node list_startNode, Node current_node, String list_property) {
		current_node.setRDFClass_name(list_startNode.getRDFClass_name() + "." + list_property);

		for (Connection e : current_node.getEdges_out()) {
			if (e.getProperty().endsWith("rest")) {
				nameListClasses(list_startNode, e.pointedNode(), list_property);
			}

		}
	}


	public void addFile(String directory, String filename, String datatype) {
		Set<Node> nodes = new HashSet<Node>();

		RDFHandler ifch = new RDFHandler();
		OntModel model=ifch.handleRDF(directory, filename, datatype);
		InternalModel im=new InternalModel();
		im.handle(model, nodes);
		for (Node bn : nodes) {
			if (bn.getRDFClass_name().contains("IfcPropertySet"))
				bn.setNodeType(Node.BLANKNODE);
		}
		addClassLiteralInstances(filename, nodes, clfrequencytable.getDatasets());
		}

	
	
	static public void addFiles() {
		ClassLiteralFrequencyTableMaker fq=new ClassLiteralFrequencyTableMaker("ArchiCAD");
		/*fq.addFile("c:/2014/c_testset/","nogeomo_sms1.ifc", "IFC");
		fq.addFile("c:/2014/c_testset/","nogeomo_sms2.ifc", "IFC");
		

		fq.addFile("c:/2014/a_testset/","Drum_A.ifc", "IFC");
		fq.addFile("c:/2014/a_testset/","Drum_B.ifc", "IFC");
		fq.close();*/
		
		fq.addFile("c:/2014/a_testset/","A1.ifc", "IFC");
		fq.addFile("c:/2014/a_testset/","A2.ifc", "IFC");
		fq.addFile("c:/2014/a_testset/","A3.ifc", "IFC");
		fq.addFile("c:/2014/a_testset/","A4.ifc", "IFC");
		fq.clfrequencytable.getDatasets().listUniques();
		fq.clfrequencytable.getDatasets().listFrequencies();
		fq.close();
	}
	
	
	
	public ClassLiteralFrequencyTable getClfrequencytable() {
		return clfrequencytable;
	}

	public static void main(String[] args) {
		
		addFiles();
	}

}
