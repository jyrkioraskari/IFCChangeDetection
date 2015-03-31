package fi.ni;

import java.util.Set;

import fi.ni.nodenamer.datastructure.Node;
import fi.ni.vo.DatasetStatsVO;


public class DatasetStatsCounter {
	static public void count(String directory, String filename, String type) {

		DatasetStatsVO dsvo=new DatasetStatsVO(filename);

		NodeStats gs = new NodeStats();
		if(type.equals("SAVED"))
		{
             ModelHandler mh=new ModelHandler();
             mh.open(1,filename);
             gs.setInternalGraph(mh.getNodes_verson1());
		}
		else{
			gs.createInternalGraph(directory, filename, type,dsvo);
		}
		gs.analyze(dsvo);
		dsvo.setCounted_triples(gs.estimatedTripleCount());
		/*Set<Set<Triple>> msgs=gs.deduceMSGs();
		double msgt=0;
		long maxmsg=0;
		for(Set<Triple> msg:msgs)
		{
			msgt+=msg.size();
			if(msg.size()>maxmsg)
				maxmsg=msg.size();
		}
		dsvo.setMax_msg(maxmsg);
		dsvo.setAvg_msg(msgt/msgs.size());
		System.out.println("msgt yhteensa"+msgt);*/
		 

		long literals=0;
		long iris=0;
		long blanks=0;
		
		for (Node n : gs.getNodes()) {
			if(n.getNodeType()==Node.LITERAL)
			{
				literals++;
				continue;
			}
			if(n.getNodeType()==Node.BLANKNODE)
				blanks++;
			else
				iris++;

		}
		dsvo.setLiterals(literals);
		dsvo.setBlank_nodes(blanks);
		dsvo.setIris(iris);
		
		DatasetStatsStore dss = new DatasetStatsStore();
		dss.openModel();
		dss.addStats(dsvo);
		dss.closeModel();
		
	}


	static public void test() {
		/*count("default","Drum_A.ifc_v1_A.xml", "SAVED");
		count("default","Drum_A.ifc_v1_B.xml", "SAVED");
		count("default","Drum_A.ifc_v1_C.xml", "SAVED");
		count("default","Drum_A.ifc_v1_D.xml", "SAVED");
		
		count("default","Drum_A.ifc_v2_A.xml", "SAVED");
		count("default","Drum_A.ifc_v2_B.xml", "SAVED");
		count("default","Drum_A.ifc_v2_C.xml", "SAVED");
		count("default","Drum_A.ifc_v2_D.xml", "SAVED");*/
		
		//count("default","Drum_A.ifc_v1_500.xml", "SAVED");
		
		/*
		count("C:/2014/a_testset/","A1.ifc", "IFC");
		count("C:/2014/a_testset/","A2.ifc", "IFC");
		count("C:/2014/a_testset/","A3.ifc", "IFC");
		count("C:/2014/a_testset/","A4.ifc", "IFC");*/
		count("C:/2014/b_testset/","SMC_Rakennus.ifc", "IFC");
		count("C:/2014/b_testset/","SMC_RakennusMuutettu.ifc", "IFC");
		/*count("C:/2014/c_testset/","nogeomo_sms1.ifc", "IFC");
		count("C:/2014/c_testset/","nogeomo_sms2.ifc", "IFC");*/
		
		
	}

	public static void main(String[] args) {
		test();
	}

}
