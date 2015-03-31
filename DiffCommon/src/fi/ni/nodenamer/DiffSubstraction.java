package fi.ni.nodenamer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fi.ni.nodenamer.datastructure.Connection;
import fi.ni.nodenamer.datastructure.Node;
import fi.ni.vo.DiffReportVO;

public class DiffSubstraction {

	public static void doJustDiff(Set<Node> nodes1, Set<Node> nodes2,
			DiffReportVO diff_report) {
		
		TripleSetCollection tsc1=new TripleSetCollection();
		TripleSetCollection tsc2=new TripleSetCollection();

		int c1 = 0;
		for (Node n1 : nodes1) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;

			c1++;
			TripleSet statements = DiffSubstraction.listStatements(n1);
			tsc1.add(statements);
		}
		System.out.println("Nodes for statements (incl literals)1: "
				+ nodes1.size());
		System.out.println("Nodes for statements1: " + c1);
		int c2 = 0;
		for (Node n1 : nodes2) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			c2++;
			TripleSet statements = DiffSubstraction.listStatements(n1);
			tsc2.add(statements);
		}

		System.out.println("Nodes for statements 2 (incl literals): "
				+ nodes2.size());
		System.out.println("Nodes for statements2: " + c2);

		System.out.println("statements 1: " + tsc1.triple_strings.size());
		System.out.println("statements 2: " + tsc2.triple_strings.size());
		
		diff_report.setStatements1(tsc1.triple_strings.size());
		diff_report.setStatements2(tsc2.triple_strings.size());

		tsc1.countDifference("Removed", tsc2,diff_report,TripleSetCollection.REMOVED);
        tsc2.countDifference("Added", tsc1,diff_report,TripleSetCollection.ADDED);
	}

	public static void doSimpleDiff(Set<Node> nodes1, Set<Node> nodes2) {
		
		TripleSetCollection tsc1=new TripleSetCollection();
		TripleSetCollection tsc2=new TripleSetCollection();

		for (Node n1 : nodes1) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;

			TripleSet statements = DiffSubstraction.listStatements(n1);
			tsc1.add(statements);
		}
		for (Node n1 : nodes2) {
			if (n1.getNodeType() == Node.LITERAL)
				continue;
			TripleSet statements = DiffSubstraction.listStatements(n1);
			tsc2.add(statements);
		}
		tsc1.countDifference("Removed", tsc2,TripleSetCollection.REMOVED);
        tsc2.countDifference("Added", tsc1,TripleSetCollection.ADDED);
	}

	
	/*
	public static void doDiff(Set<Node> nodes1, Set<Node> nodes2,
			DiffReportVO diff_report) {
		Set<String> statements1 = new HashSet<String>();
		Set<String> statements2 = new HashSet<String>();

		Map<String, List<Triple>> statements_map1 = new HashMap<String, List<Triple>>(); // Statement,
		Map<String, List<Triple>> statements_map2 = new HashMap<String, List<Triple>>();

		System.out.println("Nodes for statements (incl literals)1: "
				+ nodes1.size());
		int c1 = 0;
		for (Node n1 : nodes1) {
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
				+ nodes2.size());
		int c2 = 0;
		for (Node n1 : nodes2) {
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
					}

			}
		}
		System.out.println("added: " + added);
		System.out.println("added collided: " + added_collision);
		diff_report.setAdded(added);
		diff_report.setAdded_collided(added_collision);

		for (String sc : add_classes.keySet())
			System.out.println("    added class: " + sc + " "
					+ add_classes.get(sc));
	}*/

	static public TripleSet listStatements(Node node) {
		TripleSet tripleset = new TripleSet();
		Set<String> triple_strings = tripleset.getTriple_strings();
		Set<String> triple_strings_orgid=tripleset.getTriple_strings_orgid();
        
		// LITERAALIT
		List<Connection> cons_lit = node.getEdges_literals();
		for (Connection c : cons_lit) {
			String key = node.getURI() + " " + c.getProperty() + " "
					+ c.pointedNode().getLexicalValue();			
			triple_strings.add(key);
			List<Triple> l = tripleset.getTriples().get(key);
			if (l == null) {
				l = new ArrayList<Triple>();
				tripleset.getTriples().put(key, l);
			}
			l.add(new Triple(node, c.getProperty(), c.pointedNode().getLexicalValue()));

			if(node.getJena_id()==null)
				continue;
			String orgkey = node.getJena_id() + " " + c.getProperty() + " "
					+ c.pointedNode().getLexicalValue();
			triple_strings_orgid.add(orgkey);
			
			List<String> pol = tripleset.getPaths_org_map().get(key);
			if (pol == null) {
				pol = new ArrayList<String>();
				tripleset.getPaths_org_map().put(key, pol);
			}
			pol.add(orgkey);
			
			List<String> opl = tripleset.getOrg_paths_map().get(orgkey);
			if (opl == null) {
				opl = new ArrayList<String>();
				tripleset.getOrg_paths_map().put(orgkey, opl);
			}
			opl.add(key);

		}

		// OUT: OSOITETUT LUOKAT tyypin mukaan
		List<Connection> cons_out = node.getEdges_out();
		for (Connection c : cons_out) {
			String key = node.getURI() + " " + c.getProperty() + " "
					+ c.pointedNode().getURI();
			triple_strings.add(key);
			List<Triple> l = tripleset.getTriples().get(key);
			if (l == null) {
				l = new ArrayList<Triple>();
				tripleset.getTriples().put(key, l);
			}
			l.add(new Triple(node, c.getProperty(), c.pointedNode()));
			if(node.getJena_id()==null)
				continue;
			if( c.pointedNode().getJena_id()==null)
				continue;
			String orgkey = node.getJena_id() + " " + c.getProperty() + " "	+ c.pointedNode().getJena_id();
			triple_strings_orgid.add(orgkey);
			
			List<String> pol = tripleset.getPaths_org_map().get(key);
			if (pol == null) {
				pol = new ArrayList<String>();
				tripleset.getPaths_org_map().put(key, pol);
			}
			pol.add(orgkey);
			
			List<String> opl = tripleset.getOrg_paths_map().get(orgkey);
			if (opl == null) {
				opl = new ArrayList<String>();
				tripleset.getOrg_paths_map().put(orgkey, opl);
			}
			opl.add(key);

		}

		return tripleset;
	}

}
