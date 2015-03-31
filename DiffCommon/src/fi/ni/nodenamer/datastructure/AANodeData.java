package fi.ni.nodenamer.datastructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AANodeData {
	private boolean guidBijection=false;
	private boolean changedGuid=false;
	private boolean derivedChange=false;
	
	private boolean bottomUp_islet=false;  
	private boolean bottomUp_islet_recnoiput=false;  // only one input recursively
	private String bottomUp_chksum=" ";
	private boolean hasBottomUp_cksum=false;
	final private Set<Node> bottomUPNodes=new HashSet<Node>();
	final private List<String> crossings=new ArrayList<String>();
	private int cchksum_nodecount=0;
	private double literal_prob=1;

	public boolean isGuidBijection() {
		return guidBijection;
	}

	public void setGuidBijection(boolean guidBijection) {
		this.guidBijection = guidBijection;
	}

	public boolean isDerivedChange() {
		return derivedChange;
	}

	public void setDerivedChange(boolean derivedChange) {
		this.derivedChange = derivedChange;
	}

	public String getBottomUp_chksum() {
		return bottomUp_chksum;
	}

	public void setBottomUp_chksum(String bottomUp_chksum) {
		this.bottomUp_chksum = bottomUp_chksum;
	}

	public boolean hasBottomUp_cksum() {
		return hasBottomUp_cksum;
	}

	public void setHasBottomUp_cksum(boolean hasBottomUp_cksum) {
		this.hasBottomUp_cksum = hasBottomUp_cksum;
	}

	public Set<Node> getBottomUPNodes() {
		return bottomUPNodes;
	}

	
	public List<String> getCrossings() {
		return crossings;
	}

	public int getCchksum_nodecount() {
		return cchksum_nodecount;
	}

	public void setCchksum_nodecount(int cchksum_nodecount) {
		this.cchksum_nodecount = cchksum_nodecount;
	}

	public double getLiteral_prob() {
		return literal_prob;
	}

	public void setLiteral_prob(double literal_prob) {
		this.literal_prob = literal_prob;
	}
	
	public void setBottomUp_chksum(String bottomUp_chksum,Node n) {
		this.setBottomUp_chksum(bottomUp_chksum);
		getBottomUPNodes().add(n);
	}
	public void setBottomUp_chksum(String bottomUp_chksum,Set<Node> nset) {
		this.setBottomUp_chksum(bottomUp_chksum);
		getBottomUPNodes().addAll(nset);
	}

	public int getBottomUp_nodeCount() {
		return getBottomUPNodes().size();
	}

	public boolean isBottomUp_islet() {
		return bottomUp_islet;
	}

	public void setBottomUp_islet(boolean bottomUp_islet) {
		this.bottomUp_islet = bottomUp_islet;
	}

	public boolean isBottomUp_islet_recnoiput() {
		return bottomUp_islet_recnoiput;
	}

	public void setBottomUp_islet_recnoiput(boolean bottomUp_islet_recnoiput) {
		this.bottomUp_islet_recnoiput = bottomUp_islet_recnoiput;
	}

	public boolean isChangeSetGuid() {
		return changedGuid;
	}

	public void setChangeSetGuid(boolean changedGuid) {
		this.changedGuid = changedGuid;
	}


	
}