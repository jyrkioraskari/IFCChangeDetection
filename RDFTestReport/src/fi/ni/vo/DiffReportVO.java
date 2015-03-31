package fi.ni.vo;

import java.util.Date;

public class DiffReportVO extends ReportVO {
	final long   timestamp=(new Date()).getTime();
	String program;
    String file1;
    String file2;
    long   org_statements1=-1;
    long   org_statements2=-1;

    long  litcksums1=-1;
    long  litcksums2=-1;

    long   unique_nodes1=-1;
    long   unique_nodes2=-1;

    long   nodes1=-1;
    long   nodes2=-1;

    long   statements1=-1;
    long   statements2=-1;

    long   added=-1;
    long   removed=-1;
    
    long   added_collided=-1;
    long   removed_collided=-1;

    
    int version=0;
    int iteration=0;
    String algorithm;
    String params;
    
    long remove_diff_ff=-1;
    long remove_diff_ft=-1;
    long remove_diff_tf=-1;
    long remove_diff_tt=-1;

    long add_diff_ff=-1;
    long add_diff_ft=-1;
    long add_diff_tf=-1;
    long add_diff_tt=-1;
    
    public DiffReportVO()
    {
    	super();
    }
    
	public DiffReportVO(String program, String file1, String file2, int version, String algorithm,
			String params) {
		super();
		this.program = program;
		this.file1 = file1;
		this.file2 = file2;
		this.version = version;
		this.algorithm = algorithm;
		this.params = params;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public String getFile1() {
		return file1;
	}

	public void setFile1(String file1) {
		this.file1 = file1;
	}

	public String getFile2() {
		return file2;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}

	public long getOrg_statements1() {
		return org_statements1;
	}

	public void setOrg_statements1(long org_statements1) {
		this.org_statements1 = org_statements1;
	}

	public long getOrg_statements2() {
		return org_statements2;
	}

	public void setOrg_statements2(long org_statements2) {
		this.org_statements2 = org_statements2;
	}

	public long getLitcksums1() {
		return litcksums1;
	}

	public void setLitcksums1(long litcksums1) {
		this.litcksums1 = litcksums1;
	}

	public long getLitcksums2() {
		return litcksums2;
	}

	public void setLitcksums2(long litcksums2) {
		this.litcksums2 = litcksums2;
	}

	public long getUnique_nodes1() {
		return unique_nodes1;
	}

	public void setUnique_nodes1(long unique_nodes1) {
		this.unique_nodes1 = unique_nodes1;
	}

	public long getUnique_nodes2() {
		return unique_nodes2;
	}

	public void setUnique_nodes2(long unique_nodes2) {
		this.unique_nodes2 = unique_nodes2;
	}

	public long getNodes1() {
		return nodes1;
	}

	public void setNodes1(long nodes1) {
		this.nodes1 = nodes1;
	}

	public long getNodes2() {
		return nodes2;
	}

	public void setNodes2(long nodes2) {
		this.nodes2 = nodes2;
	}

	public long getStatements1() {
		return statements1;
	}

	public void setStatements1(long statements1) {
		this.statements1 = statements1;
	}

	public long getStatements2() {
		return statements2;
	}

	public void setStatements2(long statements2) {
		this.statements2 = statements2;
	}

	public long getAdded() {
		return added;
	}

	public void setAdded(long added) {
		this.added = added;
	}

	public long getRemoved() {
		return removed;
	}

	public void setRemoved(long removed) {
		this.removed = removed;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public long getAdded_collided() {
		return added_collided;
	}

	public void setAdded_collided(long added_collided) {
		this.added_collided = added_collided;
	}

	public long getRemoved_collided() {
		return removed_collided;
	}

	public void setRemoved_collided(long removed_collided) {
		this.removed_collided = removed_collided;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public long getRemove_diff_ff() {
		return remove_diff_ff;
	}

	public void setRemove_diff_ff(long remove_diff_ff) {
		this.remove_diff_ff = remove_diff_ff;
	}

	public long getRemove_diff_ft() {
		return remove_diff_ft;
	}

	public void setRemove_diff_ft(long remove_diff_ft) {
		this.remove_diff_ft = remove_diff_ft;
	}

	public long getRemove_diff_tf() {
		return remove_diff_tf;
	}

	public void setRemove_diff_tf(long remove_diff_tf) {
		this.remove_diff_tf = remove_diff_tf;
	}

	public long getRemove_diff_tt() {
		return remove_diff_tt;
	}

	public void setRemove_diff_tt(long remove_diff_tt) {
		this.remove_diff_tt = remove_diff_tt;
	}

	public long getAdd_diff_ff() {
		return add_diff_ff;
	}

	public void setAdd_diff_ff(long add_diff_ff) {
		this.add_diff_ff = add_diff_ff;
	}

	public long getAdd_diff_ft() {
		return add_diff_ft;
	}

	public void setAdd_diff_ft(long add_diff_ft) {
		this.add_diff_ft = add_diff_ft;
	}

	public long getAdd_diff_tf() {
		return add_diff_tf;
	}

	public void setAdd_diff_tf(long add_diff_tf) {
		this.add_diff_tf = add_diff_tf;
	}

	public long getAdd_diff_tt() {
		return add_diff_tt;
	}

	public void setAdd_diff_tt(long add_diff_tt) {
		this.add_diff_tt = add_diff_tt;
	}
    

	
}
