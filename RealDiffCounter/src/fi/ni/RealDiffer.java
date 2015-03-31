package fi.ni;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.ni.nodenamer.DiffSubstraction;
import fi.ni.nodenamer.RealChangeDetection;
import fi.ni.nodenamer.Triple;
import fi.ni.nodenamer.TripleSet;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.vo.DiffReportVO;

public class RealDiffer {
	static public void test(String codebase, String application_name,String directory, 
			String filename1, String filename2, String type) {

		GenStats gs1 = new GenStats();
		gs1.analyze(directory, filename1, type);
		gs1.makeUnique();

		GenStats gs2 = new GenStats();
		gs2.analyze(directory, filename2, type);
		gs2.makeUnique();
		System.out.println("Removed added");

		DiffReportVO diff_report = new DiffReportVO(application_name,
				filename1, filename2, 0, "RealDiff", "");

		doJustDiff(gs1, gs2, diff_report);

		/*
		 * if (report) { TestReports tr = new TestReports(); tr.openModel();
		 * tr.addTestResult(diff_report); tr.closeModel(); }
		 */
	}

	private static void doJustDiff(GenStats gs1, GenStats gs2,
			DiffReportVO diff_report) {
		Set<String> statements1 = new HashSet<String>();
		Set<String> statements2 = new HashSet<String>();

		Map<String, List<Triple>> statements_map1 = new HashMap<String, List<Triple>>(); // Statement,
		Map<String, List<Triple>> statements_map2 = new HashMap<String, List<Triple>>();

		RealChangeDetection.checkGuidBijections(gs1.getNodes(), gs2.getNodes());
		System.out.println("Nodes for statements (incl literals)1: "
				+ gs1.getNodes().size());
		int c1 = 0;
		for (Node n1 : gs1.getNodes()) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			c1++;
			TripleSet statements = DiffSubstraction.listStatements(n1);
			statements1.addAll(statements.getTriple_strings());
			for (Map.Entry<String, List<Triple>> entry : statements
					.getTriples().entrySet()) {
				statements_map1.put(entry.getKey(), entry.getValue());
			}

		}
		System.out.println("Nodes for statements1: " + c1);
		System.out.println("Nodes for statements 2 (incl literals): "
				+ gs2.getNodes().size());
		int c2 = 0;
		for (Node n1 : gs2.getNodes()) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			c2++;
			TripleSet statements = DiffSubstraction.listStatements(n1);
			statements2.addAll(statements.getTriple_strings());
			for (Map.Entry<String, List<Triple>> entry : statements
					.getTriples().entrySet()) {
				statements_map2.put(entry.getKey(), entry.getValue());
			}
		}
		System.out.println("Nodes for statements2: " + c2);

		System.out.println("statements 1: " + statements1.size());
		System.out.println("statements 2: " + statements2.size());
		diff_report.setStatements1(statements1.size());
		diff_report.setStatements2(statements2.size());
		int removed = 0;
		int removed_collision = 0;
		int real_removals = 0;
		for (String s1 : statements1) {
			if (!statements2.contains(s1)) {
				removed++;
				List<Triple> tlist = statements_map1.get(s1);
				if (tlist != null)
					for (Triple t : tlist) {
						if (t.isCollided())
							removed_collision++;
						if (t.isReallyNew())
							real_removals++;
					}

			}
		}
		System.out.println("removed: " + removed);
		System.out.println("removed collided: " + removed_collision);
		System.out.println("real removals 1: " + real_removals);

		diff_report.setRemoved(removed);
		diff_report.setRemoved_collided(removed_collision);

		int added = 0;
		int added_collision = 0;
		int real_additions = 0;
		for (String s1 : statements2) {
			if (!statements1.contains(s1)) {
				added++;
				List<Triple> tlist = statements_map2.get(s1);
				if (tlist != null)
					for (Triple t : tlist) {
						if (t.isCollided())
							added_collision++;
						if (t.isReallyNew())
							real_additions++;
					}

			}
		}
		System.out.println("added: " + added);
		System.out.println("added collided: " + added_collision);
		System.out.println("real additions 1: " + real_additions);
		diff_report.setAdded(added);
		diff_report.setAdded_collided(added_collision);

	}

	private static void doDiff(GenStats gs1, GenStats gs2,
			DiffReportVO diff_report) {
		Set<String> statements1 = new HashSet<String>();
		Set<String> statements2 = new HashSet<String>();

		Map<String, List<Triple>> statements_map1 = new HashMap<String, List<Triple>>(); // Statement,
		Map<String, List<Triple>> statements_map2 = new HashMap<String, List<Triple>>();

		RealChangeDetection.checkGuidBijections(gs1.getNodes(), gs2.getNodes());
		System.out.println("Nodes for statements (incl literals)1: "
				+ gs1.getNodes().size());
		int c1 = 0;
		for (Node n1 : gs1.getNodes()) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;

			c1++;
			TripleSet statements = DiffSubstraction.listStatements(n1);
			statements1.addAll(statements.getTriple_strings());
			for (Map.Entry<String, List<Triple>> entry : statements
					.getTriples().entrySet()) {
				statements_map1.put(entry.getKey(), entry.getValue());
			}
		}
		System.out.println("Nodes for statements1: " + c1);
		System.out.println("Nodes for statements 2 (incl literals): "
				+ gs2.getNodes().size());
		int c2 = 0;
		for (Node n1 : gs2.getNodes()) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			c2++;
			TripleSet statements = DiffSubstraction.listStatements(n1);
			statements2.addAll(statements.getTriple_strings());
			for (Map.Entry<String, List<Triple>> entry : statements
					.getTriples().entrySet()) {
				statements_map2.put(entry.getKey(), entry.getValue());
			}
		}
		System.out.println("Nodes for statements2: " + c2);

		Map<String, Integer> add_classes = new HashMap<String, Integer>();
		Map<String, Integer> remove_classes = new HashMap<String, Integer>();

		System.out.println("statements 1: " + statements1.size());
		System.out.println("statements 2: " + statements2.size());
		diff_report.setStatements1(statements1.size());
		diff_report.setStatements2(statements2.size());
		int removed = 0;
		int removed_collision = 0;
		int real_removals = 0;
		for (String s1 : statements1) {
			if (!statements2.contains(s1)) {
				removed++;
				List<Triple> tlist = statements_map1.get(s1);
				if (tlist != null)
					for (Triple t : tlist) {
						Integer count = remove_classes.get(t.s
								.getRDFClass_name());
						if (count == null)
							count = 0;
						remove_classes.put(t.s.getRDFClass_name(), count + 1);
						if (t.isCollided())
							removed_collision++;
						if (t.isReallyNew())
							real_removals++;
					}

			}
		}
		System.out.println("removed: " + removed);
		System.out.println("removed collided: " + removed_collision);
		System.out.println("real removals 1: " + real_removals);

		diff_report.setRemoved(removed);
		diff_report.setRemoved_collided(removed_collision);
		for (String sc : remove_classes.keySet())
			System.out.println("    removed class: " + sc + " "
					+ remove_classes.get(sc));

		int added = 0;
		int added_collision = 0;
		int real_additions = 0;
		for (String s1 : statements2) {
			if (!statements1.contains(s1)) {
				added++;
				List<Triple> tlist = statements_map2.get(s1);
				if (tlist != null)
					for (Triple t : tlist) {
						Integer count = add_classes.get(t.s.getRDFClass_name());
						if (count == null)
							count = 0;
						add_classes.put(t.s.getRDFClass_name(), count + 1);
						if (t.isCollided())
							added_collision++;
						if (t.isReallyNew())
							real_additions++;
					}

			}
		}
		System.out.println("added: " + added);
		System.out.println("added collided: " + added_collision);
		System.out.println("real additions 1: " + real_additions);
		diff_report.setAdded(added);
		diff_report.setAdded_collided(added_collision);

		for (String sc : add_classes.keySet())
			System.out.println("    added class: " + sc + " "
					+ add_classes.get(sc));
	}

	static public void test() {

		test("ArchiCAD", "ArchiCAD-64", "c:/2014/a_testset/","A3.ifc",
				"A4.ifc", "IFC");
	}

	public static void main(String[] args) {
		test();
	}

}
