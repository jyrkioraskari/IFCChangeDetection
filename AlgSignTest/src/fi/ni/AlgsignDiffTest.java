package fi.ni;

import java.util.HashSet;
import java.util.Set;

import fi.ni.nodenamer.DiffSubstraction;
import fi.ni.nodenamer.datastructure.Node;

public class AlgsignDiffTest {
	long timestamp1;
	long timestamp2;

	AlgSignNodeNamer gs1 = new AlgSignNodeNamer();
	AlgSignNodeNamer gs2 = new AlgSignNodeNamer();
	
	public AlgsignDiffTest(String codebase, String application_name, boolean report,String directory, String filename1, String filename2, String type) {
		
		if(type.equals("SAVED"))
		{
             ModelHandler mh=new ModelHandler();
             mh.open(1,filename1);
             mh.open(2,filename2);
             gs1.setInternalGraph(mh.getNodes_verson1());
             gs2.setInternalGraph(mh.getNodes_verson2());
		}
		else{
		    gs1.createInternalGraph(directory,filename1, type);
			gs2.createInternalGraph(directory,filename2, type);
		}
		timestamp1=System.nanoTime();
	}
	
	public void name(TestParams p) {
		
		gs1.calculate_signature(p);
		gs2.calculate_signature(p);
	}

	
	
	public retVal test() {
		
		gs1.makeUnique();
		gs2.makeUnique();
		
		System.out.println("Graphs ready GC");
		System.gc();
		System.out.println("Graphs ready");
		timestamp2=System.nanoTime();
		
		System.out.println("CPU time:"+((timestamp2-timestamp1)/1000000));
		
		Set<String> statements1=new HashSet<String>();
		Set<String> statements2=new HashSet<String>();
		
		for (Node n1 : gs1.getNodes()) 
		{
			   if(n1.getNodeType() != Node.LITERAL)
				statements1.addAll(DiffSubstraction.listStatements(n1).getTriple_strings());							
		}
		
		for (Node n1 : gs2.getNodes()) {
			  if(n1.getNodeType() != Node.LITERAL)
				statements2.addAll(DiffSubstraction.listStatements(n1).getTriple_strings());				
		}
		
		
		int removed=0;
		for(String s1:statements1)
		{
			if(!statements2.contains(s1))
			{
				removed++;
			}
		}

		int added=0;
		for(String s1:statements2)
		{
			if(!statements1.contains(s1))
			{
				added++;
			}
		}
        return new retVal(removed,added);		
	}

	static public void test(boolean report, boolean useHash) {
		long time1=System.currentTimeMillis(); 
		double added_all=0;
		double removed_all=0;
		double count=0;
		TestParams p = new TestParams(0, useHash);
		System.out.println(p);

		int compValue=Integer.MAX_VALUE;
		retVal chosen=null;
		System.out.println("AlgSign");
        AlgsignDiffTest hs=new AlgsignDiffTest("common","Default",report,"C:/2014/b_testset/","SMC_Rakennus.ifc", "SMC_RakennusMuutettu.ifc", "IFC");
        hs.name(p);
		for(int n=0;n<1;n++)
		{
		  retVal ret=hs.test();
		  if(chosen==null)
			  chosen=ret;
		  else
		  {
			  if((n%1000)==0)	
				  System.out.println("Result: "+chosen.removed+" "+chosen.added+" n:"+n);
			  added_all+=ret.added;
			  removed_all+=ret.removed;
			  count++;
			  if(compValue>ret.getCompValue())
			  {
				 
				  System.out.println("");
				  chosen=ret;
				  compValue=ret.getCompValue();
				  System.out.println("");
				  System.out.println("Result: "+chosen.removed+" "+chosen.added+" n:"+n);
			  }
		  }
		}
		long time2=System.currentTimeMillis(); 
		System.out.println("Result: "+chosen.removed+" "+chosen.added);
		System.out.println("Removed avg: "+(removed_all/count));
		System.out.println("Added avg: "+(added_all/count));
		System.out.println("Time: "+(time2-time1));
	}


	public static void main(String[] args) {
		test(true, true);
	}

}
