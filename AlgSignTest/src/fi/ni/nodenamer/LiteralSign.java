package fi.ni.nodenamer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fi.ni.TestParams;
import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.util.StringChecksum;

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
	
	
	public String algsign(Node node)
	{
	    	List<String> l_in = new ArrayList<String>();
	    	List<String> l_class = new ArrayList<String>();
	    	List<String> l_out = new ArrayList<String>();
	    	
	    	l_class.add("type"+node.getRDFClass_name());
	    	
	    	
	    	// IN: OSOITTAVAT LUOKAT tyypin mukaan
	    	List<Connection> cons_in = node.getEdges_in();
	    	for (Connection c : cons_in) {
	    	    if(c.pointedNode().getNodeType() == Node.IRINODE)
	    	      l_in.add(c.pointedNode().getURI());
	    	    else	
	    	      l_in.add("&"+c.getProperty());
	    	}

	    	// LITERAALIT
	    	List<Connection> cons_lit = node.getEdges_literals();
	    	for (Connection c : cons_lit) {
	    	    l_out.add(c.getProperty() + c.pointedNode().getLexicalValue());
	    	}

	    	// OUT: OSOITETUT LUOKAT tyypin mukaan
	    	List<Connection> cons_out = node.getEdges_out();
	    	for (Connection c : cons_out) {
	    	    if(c.pointedNode().getNodeType() == Node.IRINODE)
	    		      l_out.add(c.getProperty() + c.pointedNode().getURI());
	    		    else	
	    		      l_out.add(c.getProperty() + "&");
	    	}
	    	
	    	
	    	Collections.sort(l_in);
	    	Collections.sort(l_class);
	    	Collections.sort(l_out);

	    	StringBuffer sb=new StringBuffer();
	    	boolean first=true;
	    	for(String s:l_in)
	    	{
	    		if(first)
	    		{
	    			sb.append(s);
	    		    first=false;
	    		}
	    		else 
	    			sb.append("*"+s);
	    		
	    	}
	    	for(String s:l_class)
	    	{
	    		if(first)
	    		{
	    			sb.append(s);
	    		    first=false;
	    		}
	    		else 
	    			sb.append("*"+s);
	    		
	    	}
	    	for(String s:l_out)
	    	{
	    		if(first)
	    		{
	    			sb.append(s);
	    		    first=false;
	    		}
	    		else 
	    			sb.append("*"+s);
	    		
	    	}
	    	return sb.toString();
	}

	public String sign(Node node) {
		List<String> l_class = new ArrayList<String>();
		List<String> l_out = new ArrayList<String>();

		l_class.add("type" + node.getRDFClass_name());

		// LITERAALIT
		List<Connection> cons_lit = node.getEdges_literals();
		for (Connection c : cons_lit) {
			l_out.add(c.getProperty() + c.pointedNode().getLexicalValue());
		}

		Collections.sort(l_class);
    	Collections.sort(l_out);

		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String s : l_class) {
			if (first) {
				sb.append(s);
				first = false;
			} else
				sb.append("*" + s);

		}
		first = true;
		for (String s : l_out) {
			if (first) {
				sb.append(s);
			} else
				sb.append("*" + s);

		}
		return sb.toString();
	}

	
	private void setLChecksum4AnonNode(Node node,TestParams params) {
		String signature = algsign(node);

		node.setLiteral_chksum(signature);		
	}


}
