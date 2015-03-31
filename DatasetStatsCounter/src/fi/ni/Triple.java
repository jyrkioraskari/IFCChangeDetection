package fi.ni;

import fi.ni.nodenamer.datastructure.Node;

public class Triple {
	final public Node s;
	final public String p;
	public Node o;
	
	public Triple(Node s, String p, Node o) {
		super();
		this.s = s;
		this.p = p;
		this.o = o;
	}

   public Node otherEnd(Node n)
   {
	   if(n==s)
		   return o;
	   else 
		   return s;
   }
}