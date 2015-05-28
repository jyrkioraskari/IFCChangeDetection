package fi.ni;

import fi.ni.nodenamer.RealChangeDetection;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;

public class ChangeEstimateCounter {
	static public void test(String codebase, String application_name,String directory, 
			String filename1, String filename2, String type) {
      
		System.out.println("file 1:"+filename1);
		System.out.println("file 2:"+filename2);
		NodeNamer tr1 = new NodeNamer();
		NodeNamer tr2 = new NodeNamer();
		if(type.equals("SAVED"))
		{
             ModelHandler mh=new ModelHandler();
             mh.open(1,filename1);
             mh.open(2,filename2);
             tr1.setInternalGraph(mh.getNodes_verson1());
             tr2.setInternalGraph(mh.getNodes_verson2());
		}
		else{
		    tr1.createInternalGraph(directory,filename1, type);
			tr2.createInternalGraph(directory,filename2, type);
		}
		 System.out.println("name pseudo 1:");
		tr1.namePseudo();
		 System.out.println("name pseudo 2:");
 		tr2.namePseudo();

		System.out.println("Removed added");
		diff(tr1, tr2);

	}

	private static void diff(NodeNamer gs1, NodeNamer gs2) {
		RealChangeDetection rcd=new RealChangeDetection();
		System.out.println("Check guid bijection");
		rcd.checkGuidBijections(gs1.getNodes(), gs2.getNodes());
        System.out.println("deduceRemovedTriples:");
		deduceRemovedTriples(gs1, rcd);
		 System.out.println("deduceAddedTriples:");
		deduceAddedTriples(gs2, rcd);
		System.out.println("diff ready");
	}

	private static void deduceRemovedTriples(NodeNamer gs1, RealChangeDetection rcd) {
		int guids = 0;
		int removed_guids = 0;
		int removed = 0;
		for (Node n1 : gs1.getNodes()) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			if (n1.getAa().isChangeSetGuid()) {
				// n1 is extra
				removed_guids++;
				guids++;
				removed += n1.getEdges_literals().size();
				removed += n1.getEdges_out().size();
			} else if (n1.getAa().isGuidBijection()) {
				guids++;
				Node n2 = rcd.nodemap1.get(n1);
				if (n2 == null) {
					System.err.println("No node R2");
					continue;
				}
				// Pointed nodes are extra
				for (Connection o : n1.getEdges_out()) {
					if (o.pointedNode().getAa().isChangeSetGuid())
						removed++;					
				}
				// No common literal
				for (Connection l1 : n1.getEdges_literals()) {
					boolean found = false;
					for (Connection l2 : n2.getEdges_literals()) {
						if ((l1.getProperty() + l1.pointedNode().getLexicalValue()).equals(l2.getProperty() + l2.pointedNode().getLexicalValue())) {
							found = true;
						}
					}
					if (!found)
						removed++;
				}
			} else if (n1.getAa().isDerivedChange()) {
				removed += n1.getEdges_literals().size();
				removed += n1.getEdges_out().size();
			}
		}
		System.out.println("Removed guids: " + removed_guids+ " all: "+guids);
		System.out.println("Removed triples: " + removed);
	}

	private static void deduceAddedTriples(NodeNamer gs2, RealChangeDetection rcd) {
		int guids = 0;
		int added_guids = 0;
		int added = 0;
		for (Node n2 : gs2.getNodes()) {
			if (n2.getNodeType() == Node.LITERAL)
				continue;

			if (n2.getAa().isChangeSetGuid()) {
				// n1 is extra
				added_guids++;
				guids++;
				added += n2.getEdges_literals().size();
				added += n2.getEdges_out().size();
			} else if (n2.getAa().isGuidBijection()) {
				guids++;
				Node n1 = rcd.nodemap2.get(n2);
				if (n1 == null) {
					System.err.println("No node A1");
					continue;
				}
				// Pointed nodes are extra
				for (Connection o : n2.getEdges_out()) {
					if (o.pointedNode().getAa().isChangeSetGuid())
						added++;
				}
				// No common literal
				for (Connection l2 : n2.getEdges_literals()) {
					boolean found = false;
					for (Connection l1 : n1.getEdges_literals()) {
						if ((l2.getProperty() + l2.pointedNode().getLexicalValue()).equals(l1.getProperty() + l1.pointedNode().getLexicalValue())) {
							found = true;
						}
					}
					if (!found)
						added++;
				}

			} else if (n2.getAa().isDerivedChange()) {
				added += n2.getEdges_literals().size();
				added += n2.getEdges_out().size();
			} 
		}

		System.out.println("Added guids: " + added_guids+ " all :"+guids);
		System.out.println("Added triples: " + added);
	}

	

	static public void test() {

		test("ArchiCAD", "ArchiCAD-64", "c:/2014/b_testset/","SMC_Rakennus.ifc","SMC_RakennusMuutettu.ifc", "IFC");
		//test("ArchiCAD", "ArchiCAD-64", "c:/2014/a_testset/","A3.ifc","A4.ifc", "IFC");
	}

	public static void main(String[] args) {
		test();
	}

}
