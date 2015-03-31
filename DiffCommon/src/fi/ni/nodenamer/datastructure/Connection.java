package fi.ni.nodenamer.datastructure;

public class Connection {
    private String property;
    private long pointedNode;

    public Connection()
    {
    	
    }
    
    public Connection(String property, Node points_to) {
	super();
	this.property = property;
	this.pointedNode=points_to.getObject_id();
    }

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Node pointedNode() {
		Node n=Node.map.get(pointedNode);
		if(n==null)
		{
			//System.out.println("Unknown ID:"+pointedNode+" "+property);
			//new Throwable().printStackTrace();	
		}
		return n;
	}

	public long getPointedNode() {
		return pointedNode;
	}

	public void setPointedNode(long pointedNode) {
		this.pointedNode = pointedNode;
	}

    
	}
