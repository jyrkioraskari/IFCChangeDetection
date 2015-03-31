package fi.ni.vo;

import java.util.Date;

public class DatasetStatsVO extends ReportVO {
	final long   timestamp=(new Date()).getTime();
    String file_name;

	long   org_statements=-1;
    long   filtered_statements=-1;
    long   counted_triples=-1;
    long   litcksums=-1;
    long   unique_nodes=-1;
    long   blank_nodes=-1;
    long   iris=-1;
    long   literals=-1;
    long   nodes=-1;
    
    long   max_msg=-1;
    double avg_msg=-1;

    public DatasetStatsVO()
    {
    	super();
    }
    
    public DatasetStatsVO(String file_name) {
		super();
		this.file_name = file_name;
	}



	public String getFile_name() {
		return file_name;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public long getOrg_statements() {
		return org_statements;
	}


	public void setOrg_statements(long org_statements) {
		this.org_statements = org_statements;
	}


	public long getFiltered_statements() {
		return filtered_statements;
	}


	public void setFiltered_statements(long filtered_statements) {
		this.filtered_statements = filtered_statements;
	}


	public long getLitcksums() {
		return litcksums;
	}


	public void setLitcksums(long litcksums) {
		this.litcksums = litcksums;
	}


	public long getUnique_nodes() {
		return unique_nodes;
	}


	public void setUnique_nodes(long unique_nodes) {
		this.unique_nodes = unique_nodes;
	}


	public long getBlank_nodes() {
		return blank_nodes;
	}


	public void setBlank_nodes(long blank_nodes) {
		this.blank_nodes = blank_nodes;
	}


	public long getIris() {
		return iris;
	}


	public void setIris(long iris) {
		this.iris = iris;
	}


	public long getLiterals() {
		return literals;
	}


	public void setLiterals(long literals) {
		this.literals = literals;
	}


	public long getNodes() {
		return nodes;
	}


	public void setNodes(long nodes) {
		this.nodes = nodes;
	}


	public long getMax_msg() {
		return max_msg;
	}


	public void setMax_msg(long max_msg) {
		this.max_msg = max_msg;
	}

	public double getAvg_msg() {
		return avg_msg;
	}

	public void setAvg_msg(double avg_msg) {
		this.avg_msg = avg_msg;
	}

	public long getCounted_triples() {
		return counted_triples;
	}

	public void setCounted_triples(long counted_triples) {
		this.counted_triples = counted_triples;
	}



}
