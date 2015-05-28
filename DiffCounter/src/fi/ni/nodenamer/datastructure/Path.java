package fi.ni.nodenamer.datastructure;


public class Path {
	int steps_taken;
	Node last_node;

	
	public Path(Node blank_node) {
		last_node = blank_node;
		steps_taken = 0;
	}

	public Path(Path last_step, Connection edge) {
		last_node = edge.pointedNode();
		steps_taken = last_step.steps_taken + 1;
	}

	public Node getLast_node() {
		return last_node;
	}

	public int getSteps_taken() {
	    return steps_taken;
	}



}
