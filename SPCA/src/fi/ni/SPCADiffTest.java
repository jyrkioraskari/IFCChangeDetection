package fi.ni;

import java.util.HashSet;
import java.util.Set;

import fi.ni.nodenamer.DiffSubstraction;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.vo.DiffReportVO;

public class SPCADiffTest {
	
	NodeNamer gs1 = new NodeNamer();
	NodeNamer gs2 = new NodeNamer();
	
	public SPCADiffTest(String codebase, String application_name, boolean report,String directory, String filename1, String filename2, String type) {
		
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
		
	}
	
	
	public retVal test(TestParams p) {
		
		gs1.makeUnique(p);
		gs2.makeUnique(p);
		

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


	static public void test(boolean report, int maxsteps, boolean useHash) {
		int compValue=Integer.MAX_VALUE;
		retVal chosen=null;
		TestParams p = new TestParams(maxsteps, useHash);
		System.out.println(p);
		System.out.println("Huge Extended Plus");
		SPCADiffTest hs=new SPCADiffTest("common","Tekla Structures",report,"C:/2014/a_testset/","A2.ifc", "A3.ifc", "IFC");
		for(int n=0;n<1000;n++)
		{
		  retVal ret=hs.test(p);
		  if(chosen==null)
			  chosen=ret;
		  else
		  {
			  if((n%10)==0)	
				  System.out.println("Result: "+chosen.removed+" "+chosen.added+" n:"+n);
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
		System.out.println("Result: "+chosen.removed+" "+chosen.added);
	}

	public static void main(String[] args) {
		test(true,3000, true);
	}


}


