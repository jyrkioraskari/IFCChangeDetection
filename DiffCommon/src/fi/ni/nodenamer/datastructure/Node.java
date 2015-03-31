package fi.ni.nodenamer.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;

import com.hp.hpl.jena.rdf.model.RDFNode;

import fi.ni.util.StringChecksum;

public class Node {
	public int visualizeCount=-1;
	final List<String> crossings = new ArrayList<String>();  // PathName.inx
	public static Map<Long, Node> map = new HashMap<Long, Node>();
	String jena_id = null;
	long object_id = -1;

	final static public int LITERAL = 0;
	final static public int IRINODE = 1;
	final static public int BLANKNODE = 2;
	private int nodeType;

	private boolean sameAs = false;
	private String local_uri = "";
	private String lexicalValue = "";
	private boolean collided = false;

	private String literal_chksum = "  ";

	private AANodeData aa = new AANodeData(); // Used in the AA Paths algorithm

	
	private String class_name = "";
	boolean list = false;

	List<Connection> edges_in = new ArrayList<Connection>();
	List<Connection> edges_out = new ArrayList<Connection>();
	List<Connection> edges_literals = new ArrayList<Connection>();

	// For Local Placement
	public RealMatrix local_placement=null; 
	
	public Node() {

	}

	public Node(RDFNode node, String class_name) {
		super();
		object_id = System.identityHashCode(this);
		map.put(object_id, this);
		this.class_name = class_name;
		if (node.isAnon()) {
			nodeType = BLANKNODE;
			jena_id = node.asResource().getId().toString();
		} else {
			if (node.isLiteral()) {
				nodeType = LITERAL;
				jena_id = node.asLiteral().getString();
				local_uri = node.asLiteral().getString();
				lexicalValue = node.asLiteral().getLexicalForm();
			} else {
				nodeType = IRINODE;
				jena_id = node.asResource().getURI();
				local_uri = node.asResource().getURI().toString();
			}
		}
	}

	public Node(RDFNode node, String class_name, boolean sameAs) {
		super();
		this.sameAs = sameAs;

		object_id = System.identityHashCode(this);
		map.put(object_id, this);
		this.class_name = class_name;
		if (node.isAnon()) {
			nodeType = BLANKNODE;
			jena_id = node.asResource().getId().toString();
		} else {
			if (node.isLiteral()) {
				nodeType = LITERAL;
				jena_id = node.asLiteral().getString();
				local_uri = node.asLiteral().getString();
				lexicalValue = node.asLiteral().getLexicalForm();
			} else {
				nodeType = IRINODE;
				jena_id = node.asResource().getURI();
				local_uri = node.asResource().getURI().toString();
			}
		}
	}
	
	public String getRDFClass_name() {
		if (class_name == null)
			class_name = "unknown";
		if (class_name.equals("list"))
			return "rdf:list";
		return class_name;
	}

	public void setRDFClass_name(String class_name) {
		this.class_name = class_name;
	}

	public void addINConnection(Connection c) {
		edges_in.add(c);
	}

	public void addOUTConnection(Connection c) {
		edges_out.add(c);
	}

	public void addLiteralConnection(Connection c) {
		edges_literals.add(c);
	}

	public List<Connection> getEdges_in() {
		return edges_in;
	}
	
	public List<Connection> getEdges_in(String property) {
		List<Connection> ret = new ArrayList<Connection>();
    	for (Connection c : edges_in) {
    	    if(c.getProperty().equals(property))
    	    	ret.add(c);
    	}
    	return ret;
	}
	

	public List<Connection> getEdges_out() {
		return edges_out;
	}
	
	public List<Connection> getEdges_out(String property) {
		List<Connection> ret = new ArrayList<Connection>();
    	for (Connection c : edges_out) {
    	    if(c.getProperty().equals(property))
    	    	ret.add(c);
    	}
    	return ret;
	}
	

	private void listNumbers(Node s, int inx, List<Double> dlist) {
    	List<Connection> cons_lit = s.getEdges_literals();
    	for (Connection c : cons_lit) {
    		try
    		{
    		Double d=Double.parseDouble(c.pointedNode().getLexicalValue());
    		dlist.add(d);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}

		
		for (Connection e : s.getEdges_out()) {
			if (e.getProperty().endsWith("rest")) {
				listNumbers(e.pointedNode(), inx + 1, dlist);
			}
			else
				System.err.println("Something else");
		}
	}

	public double[] giveEdges_outAsNumberVector(String property) {
		List<Double> retlist = new ArrayList<Double>();
    	for (Connection c : edges_out) {    		
    	    if(c.getProperty().equals(property))
    	    {
    	    	Node n=c.pointedNode();
    	    	if (n.getRDFClass_name().equals("rdf:list")) {
    	    		listNumbers(n, 0, retlist);
				}
    	    	else
    	    		System.err.println("-- c:"+n.getRDFClass_name());
    	    }
    	}
    	if(retlist.size()==2)
    		retlist.add(0d);
    	double[] ret=new double[retlist.size()];
    	int i=0;
    	for(Double d:retlist)
    		ret[i++]=d.doubleValue(); 
    	return ret;
	}

	public void putNumberVector(String property,RealMatrix vector) {
    	for (Connection c : edges_out) {
    	    if(c.getProperty().equals(property))
    	    {
    	    	Node n=c.pointedNode();
    	    	if (n.getRDFClass_name().equals("rdf:list")) {
    	    		putNumbers(n, 0, vector);
				}
    	    }
    	}
	}

	
	private void putNumbers(Node n, int i, RealMatrix vector) {
    	List<Connection> cons_lit = n.getEdges_literals();
    	for (Connection c : cons_lit) {
    		c.pointedNode().setLexicalValue(""+vector.getEntry(i, 0));
    	}

		
		for (Connection e : n.getEdges_out()) {
			if (e.getProperty().endsWith("rest")) {
				
				putNumbers(e.pointedNode(), i + 1, vector);
			}
		}
	}

	public List<Connection> getEdges_literals() {
		return edges_literals;
	}

	public List<Connection> getEdges_literals(String property) {
		List<Connection> ret = new ArrayList<Connection>();
    	for (Connection c : edges_literals) {
    	    if(c.getProperty().equals(property))
    	    	ret.add(c);
    	}
    	return ret;
	}
	
	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public void setURI(String local_uri) {
		this.local_uri = local_uri;
	}

	public boolean isCollided() {
		return collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}

	public String getURI() {
		if (this.sameAs)
			return local_uri + "_sameAs";
		else
			return local_uri;
	}

	public AANodeData getAa() {
		return aa;
	}

	public void setAa(AANodeData aadata) {
		this.aa = aadata;
	}

	public String getLexicalValue() {
		return lexicalValue;
	}

	public boolean isSameAs() {
		return sameAs;
	}

	// ------------------------------------------->
	// For streaming

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodetype) {
		this.nodeType = nodetype;
	}

	public String getLiteral_chksum() {
		return literal_chksum;
	}

	public void setLiteral_chksum(String literal_chksum) {
		this.literal_chksum = literal_chksum;
	}

	public void setSameAs(boolean sameAs) {
		this.sameAs = sameAs;
	}

	public void setLocal_uri(String local_uri) {
		this.local_uri = local_uri;
	}

	public void setLexicalValue(String lexicalValue) {
		this.lexicalValue = lexicalValue;
	}

	public void setEdges_in(List<Connection> edges_in) {
		this.edges_in = edges_in;
	}

	public void setEdges_out(List<Connection> edges_out) {
		this.edges_out = edges_out;
	}

	public void setEdges_literals(List<Connection> edges_literals) {
		this.edges_literals = edges_literals;
	}

	public long getObject_id() {
		return object_id;
	}

	public void setObject_id(long object_id) {
		this.object_id = object_id;
		map.put(object_id, this);
	}

	public String getJena_id() {
		return jena_id;
	}

	public void setJena_id(String jena_id) {
		this.jena_id = jena_id;
	}
    // Straigh path
	int maxPathLength=Integer.MAX_VALUE;
	List<STPath> paths =new ArrayList<STPath>();
	


	public boolean addPath(STPath p)
    {
    	if(getNodeType()!=Node.BLANKNODE)
    		return false;
    	if(p.getSteps_taken()>0)
    	{
    		if(p.getSteps_taken()<maxPathLength)
    		{
    			maxPathLength=p.getSteps_taken();
    			paths.clear();
    			paths.add(p);
    		}
    		else
        		if(p.getSteps_taken()==maxPathLength)
        		{
        			paths.add(p);
        		}
        		else
        			return false;
    	}
    	return true;
    }
    
	Map<Node,Integer>length_values=new HashMap<Node,Integer>();
	Map<Node,List<STPath>>paths_map=new HashMap<Node,List<STPath>>();
	public boolean addPathKeepX(STPath p)
    {
    	if(getNodeType()!=Node.BLANKNODE)
    		return false;
    	if(p.getSteps_taken()>0)
    	{
    		Integer plen=length_values.get(p.getFirst_node());
    		List<STPath> paths=paths_map.get(p.getFirst_node());
    		if(paths==null)
    		{
    			paths=new ArrayList<STPath>();
    			paths_map.put(p.getFirst_node(),paths);
    		}
    		if(plen==null)
    			plen=Integer.MAX_VALUE;
    		if(p.getSteps_taken()<plen)
    		{
    			length_values.put(p.getFirst_node(), p.getSteps_taken());
    			paths.clear();
    			paths.add(p);
    		}
    		else
        		if(p.getSteps_taken()==plen)
        		{
        			paths.add(p);
        		}
        		else
        			return false;
    	}
    	return true;
    }
    
	
    /*
     * Calculates URI from paths
     */
    
    public void calculateURI(boolean usehash)
    {
    	StringChecksum sc=new StringChecksum();
    	List<String> pc=new ArrayList<String>();
    	for(STPath p:paths)
    	{
    		pc.add(p.getChecksum(usehash));    		
    	}
    	Collections.sort(pc);
    	sc.update(getLiteral_chksum());  // just in case there is no path
    	for(String s:pc)
    	{
    		sc.update(s);
    	}
    	setURI(sc.getChecksumValue()+"");
    }

    
    
    public void calculateXURI(boolean usehash)
    {
    	List<String> pc=new ArrayList<String>();
    	for(Map.Entry<Node,List<STPath>> pentry:paths_map.entrySet())
    	{
    		for(STPath p:pentry.getValue())
        	{
    		 pc.add(p.getChecksum(usehash));
        	}
    	}
    	Collections.sort(pc);
    	StringChecksum sc=new StringChecksum(usehash);
    	sc.update(getLiteral_chksum());
    	sc.update(getRDFClass_name()); // this is changed after the literal calculation  
    	for(String s:pc)
    	{
    		sc.update(s);
    	}
    	setURI(sc.getChecksumValue()+"");
    }

    public String getXURI(boolean usehash)
    {
    	List<String> pc=new ArrayList<String>();
    	for(Map.Entry<Node,List<STPath>> pentry:paths_map.entrySet())
    	{
    		for(STPath p:pentry.getValue())
        	{
    		 pc.add(p.getChecksum(usehash));
        	}
    	}
    	Collections.sort(pc);
    	StringChecksum sc=new StringChecksum(usehash);
    	sc.update(getLiteral_chksum());
    	sc.update(getRDFClass_name()); // this is changed after the literal calculation
    	
    	/*for(String s:pc)
    	{
    		sc.update(s);
    	}*/
    	if(pc.size()>0)
    	{
    	  System.out.println("first: "+pc.get(0));	
    	  sc.update(pc.get(0)); // Only first
    	}
    	
    	return sc.getChecksumValue();
    }

    public void setInxOfLongestPath(boolean usehash)
    {
		System.out.println("...........");
    	for(STPath p:paths)
    	{
    		System.out.print("np: "+p.getFirst_node().getURI());
    		for(Connection c:p.getPath_links())
    		{
    		   Node n=Node.map.get(c.getPointedNode());
    		   System.out.print(" "+c.getProperty()+"."+c.getPointedNode());
    		}
    		System.out.println("");
    	}
		System.out.println("...........");
    	List<String> pc=new ArrayList<String>();
    	for(STPath p:paths)
    	{
    		int i=0;
    		String pchksum=p.getChecksum(usehash);
    		for(Connection c:p.getPath_links())
    		{
    		   Node n=Node.map.get(c.getPointedNode());
    		   if(n!=null)
    		   {
    			PathPosition pp=new PathPosition(i, pchksum, p.getSteps_taken(),p.last_node.jena_id);   
    		    n.addPathPosition(pp);
    		   } 
    		   else
    			   System.err.println("Node for: "+c+" null!");
    		   i++;
    		}
    	}
    	Collections.sort(pc);
    	StringChecksum sc=new StringChecksum(usehash);
    	sc.update(getLiteral_chksum());  // just in case there is no path
    	for(String s:pc)
    	{
    		sc.update(s);
    	}
    	setURI(sc.getChecksumValue()+"");
    }
    
    private List<PathPosition> parthOfPath=new ArrayList<PathPosition>();
    private int pathPositionPathLength=0;


	public void addPathPosition(PathPosition pp) {
		if(pp.getPath_length()<pathPositionPathLength)
			return;
		if(pp.getPath_length()>pathPositionPathLength)
		{
			parthOfPath.clear();
			pathPositionPathLength=pp.getPath_length();
		}
		parthOfPath.add(pp);
	}

	  public void calculateLongPathURI(boolean usehash)
	    {
	    	StringChecksum sc=new StringChecksum(usehash);
	    	List<String> pc=new ArrayList<String>();
	    	System.out.println("pp-------------------------------");
	    	for(PathPosition pp:parthOfPath)
	    	{
	    		
	    		pc.add(pp.getPchsum()+"."+pp.getInx());
	    		//Sama polku kerta toisensa jälkeen!
	    		System.out.println("pp: "+pp.getPchsum()+"."+pp.getInx()+" l:"+pp.getLastNode());
	    	}
	    	System.out.println("---------------------------------");
	    	Collections.sort(pc);
	    	//sc.update(getLiteral_chksum());  // just in case there is no path
	    	for(String s:pc)
	    	{
	    		sc.update(s);
	    	}
	    	setURI(sc.getChecksumValue()+"");
	    }

	public List<String> getCrossings() {
		return crossings;
	}

	    
    

}
