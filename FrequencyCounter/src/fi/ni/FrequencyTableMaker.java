package fi.ni;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.FQ_LiteralChecksummer;
import fi.ni.nodenamer.InternalModel;
import fi.ni.nodenamer.RDFHandler;
import fi.ni.nodenamer.datastructure.Node;

public class FrequencyTableMaker {
	FrequencyTable frequencytable;
	
	public FQ_LiteralChecksummer nodeliteralsummer=new FQ_LiteralChecksummer();
	
	public FrequencyTableMaker(String codebase_name)
	{
		frequencytable=new FrequencyTable(codebase_name);
	}
	
	public void close()
	{
		frequencytable.close();
	}
	
	public void addFile(String directory, String filename, String datatype) {
		Set<Node> nodes = new HashSet<Node>();

		RDFHandler ifch = new RDFHandler();
		OntModel model=ifch.handleRDF(directory, filename, datatype);
		InternalModel im=new InternalModel();
		im.handle(model, nodes);
		for (Node node : nodes) {
			frequencytable.getClassbag().add(node.getRDFClass_name());
		}
		for (Node bn : nodes) {
			if (bn.getRDFClass_name().contains("IfcPropertySet"))
				bn.setNodeType(Node.BLANKNODE);
		}
		nodeliteralsummer.setliteralChecksums(nodes, frequencytable.getClass_chksums_bag());
	}

	
	
	static public void addFiles() {
		/*FrequencyTableHandler smcfq=new FrequencyTableHandler("smc");
		smcfq.addFile("c:/2014/c_testset/nogeomo_sms1.ifc", "IFC");
		smcfq.addFile("c:/2014/c_testset/nogeomo_sms2.ifc", "IFC");
		smcfq.close();*/

		/*FrequencyTableHandler tsfq=new FrequencyTableHandler("ts");
		tsfq.addFile("c:/2014/a_testset/Drum_A.ifc", "IFC");
		tsfq.addFile("c:/2014/a_testset/Drum_B.ifc", "IFC");
		tsfq.close();*/
		
		FrequencyTableMaker acfq=new FrequencyTableMaker("ArchiCAD");
		acfq.addFile("c:/2014/a_testset/","A1.ifc", "IFC");
		acfq.addFile("c:/2014/a_testset/","A2.ifc", "IFC");
		acfq.addFile("c:/2014/a_testset/","A3.ifc", "IFC");
		acfq.addFile("c:/2014/a_testset/","A4.ifc", "IFC");
		acfq.close();
	}
	
	public static void main(String[] args) {
		
		addFiles();
	}

}
