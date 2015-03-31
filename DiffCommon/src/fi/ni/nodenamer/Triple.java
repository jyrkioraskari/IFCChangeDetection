package fi.ni.nodenamer;

import fi.ni.nodenamer.datastructure.Node;

public class Triple {
	final public Node s;
	final public String p;
	public Object o=null;
	
	public Triple(Node s, String p, Node o) {
		super();
		this.s = s;
		this.p = p;
		this.o = o;
	}

	public Triple(Node s, String p, String lo) {
		super();
		this.s = s;
		this.p = p;
		this.o = lo;
	}

	public boolean isCollided()
	{
		if(s.isCollided())
			return true;
		if(o.getClass().isInstance(Node.class))
			if(((Node)o).isCollided())
				return true;
		return false;
	}
	
	private boolean hasNoBijection()
	{
		if(s.getNodeType() == Node.IRINODE)
		if(!s.getAa().isGuidBijection())
			return true;
		if(o.getClass().isInstance(Node.class))
		{
			Node on=(Node)o;
			if(on.getNodeType() == Node.IRINODE)
			  if(!on.getAa().isGuidBijection())
				 return true;
		}
		return false;
	}

	public boolean isReallyNew()
	{
		// If guid + literal, the change is new
		if(s.getNodeType() == Node.IRINODE&& (o.getClass().isInstance(String.class)))
			return true;
		
		// if bottom up and object literal
		if(s.getAa().hasBottomUp_cksum()&& (o.getClass().isInstance(String.class)))
			return true;
		
		// The list starts with a new guid
		if(s.getAa().isDerivedChange())
		{
			System.out.println("list bijection!");
			return true;
		}
		if(o.getClass().isInstance(Node.class))
			if(((Node)o).getAa().isDerivedChange())
				return true;
		
		// if new guid, the change is inevitable
		if(hasNoBijection())
			return true;
		return false;
	}



}