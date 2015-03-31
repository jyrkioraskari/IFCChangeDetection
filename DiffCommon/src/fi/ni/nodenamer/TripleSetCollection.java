package fi.ni.nodenamer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.ni.vo.DiffReportVO;

// Kaksi kysymystä: - löytyykö muuttuneelle jena-stringille vastinparia aapaths-muutosjoukosta  added, removed
// 					- löytyykö muuttuneelle aapaths-stringilel vastinparia jena-muutosjoukosta  added, removed

// This checks if the change set triples match

public class TripleSetCollection {
    public static final int REMOVED=0;
    public static final int ADDED=1;
	public Set<String> triple_strings = new HashSet<String>();
	public Set<String> triple_strings_org = new HashSet<String>();
	public Map<String, List<Triple>> statements_map = new HashMap<String, List<Triple>>(); // Statement,
	
	// i->n!!
	final Map<String,List<String>> cksum_org_map=new HashMap<String,List<String>>();
	final Map<String,List<String>> org_cksum_map=new HashMap<String,List<String>>();


	public void add(TripleSet statements) {
		triple_strings.addAll(statements.getTriple_strings());
		triple_strings_org.addAll(statements.getTriple_strings_orgid());
		
		for (Map.Entry<String, List<Triple>> entry : statements.getTriples()
				.entrySet()) {
			statements_map.put(entry.getKey(), entry.getValue());
		}
		
		for (Map.Entry<String, List<String>> entry : statements.getPaths_org_map().entrySet()) {
			cksum_org_map.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, List<String>> entry : statements.getOrg_paths_map().entrySet()) {
			org_cksum_map.put(entry.getKey(), entry.getValue());
		}

	}
	
	private boolean matchingDiff(boolean oDiff, String org_id, TripleSetCollection other_collection) {
		List<String> cksum_ids=org_cksum_map.get(org_id);
		if(cksum_ids==null)
		{
			System.err.println("path id for "+org_id+" is not at the map");
			return false;
		}
		
		// org_id:tä tulisi vastata vain yksi string
		for(String p:cksum_ids)		
		{
			if (other_collection.triple_strings.contains(p))
			{
				if(oDiff)
				{
					System.out.println("--------------------------------------->");
					System.out.println("org_id: "+org_id);
					System.out.println("csum_ids: "+cksum_ids);
					System.out.println("<---------------------------------------");
				}
				return false;
			}

		}
		
		return true;
	}
	
	public void countDifference(String explanation, TripleSetCollection other_collection,DiffReportVO diff_report, int diffset)
	{
		int ff=0;
		int ft=0;
		int tf=0;
		int tt=0;
		int missing = 0;
		int missing_collision = 0;

		for (String s1 : triple_strings) {
			if (!other_collection.triple_strings.contains(s1)) {
				// key does not exist in _other_
				missing++;
				
				// Is it collided?
				boolean collided=false;
				List<Triple> tlist = statements_map.get(s1);				
				if (tlist != null)
					for (Triple t : tlist) {
						if (t.isCollided())
							collided=true;
					}
				if(collided)
					missing_collision++; // Yhteensopivuuden vuoksi!

			}
		}
		
		for (String org_id : triple_strings_org) { // Jena strings...
			boolean oDiff=false;
			if (!other_collection.triple_strings_org.contains(org_id)) {
				// key does not exist in _other_
				oDiff=true;
			}
			// Does a matching checksum difference exist
			boolean cDiff=matchingDiff(oDiff,org_id,other_collection);
			if(!oDiff&&!cDiff)
				ff++;
			if(!oDiff&&cDiff)
				ft++;
			if(oDiff&&!cDiff)
				tf++;
			if(oDiff&&cDiff)
				tt++;
				
		}

		if(diffset==TripleSetCollection.REMOVED)
		{
			diff_report.setRemove_diff_ff(ff);
			diff_report.setRemove_diff_ft(ft);
			diff_report.setRemove_diff_tf(tf);
			diff_report.setRemove_diff_tt(tt);
			diff_report.setRemoved(missing);
			diff_report.setRemoved_collided(missing_collision);
		}

		if(diffset==TripleSetCollection.ADDED)
		{
			diff_report.setAdd_diff_ff(ff);
			diff_report.setAdd_diff_ft(ft);
			diff_report.setAdd_diff_tf(tf);
			diff_report.setAdd_diff_tt(tt);
			diff_report.setAdded(missing);
			diff_report.setAdded_collided(missing_collision);
			
		}
		System.out.println("diff ff: "+ff);
		System.out.println("diff ft: "+ft);
		System.out.println("diff tf: "+tf);
		System.out.println("diff tt: "+tt);
		
		System.out.println(explanation+": " + missing);
		System.out.println(explanation+" collided: " + missing_collision);
	}

	public void countDifference(String explanation, TripleSetCollection other_collection, int diffset)
	{
		int ff=0;
		int ft=0;
		int tf=0;
		int tt=0;

		for (String org_id : triple_strings_org) { // Jena strings...
			boolean oDiff=false;
			if (!other_collection.triple_strings_org.contains(org_id)) {
				// key does not exist in _other_
				oDiff=true;
			}
			// Does a matching checksum difference exist
			boolean cDiff=matchingDiff(oDiff,org_id,other_collection);
			if(!oDiff&&!cDiff)
				ff++;
			if(!oDiff&&cDiff)
				ft++;
			if(oDiff&&!cDiff)
				tf++;
			if(oDiff&&cDiff)
				tt++;
				
		}
		System.out.println("diff ff: "+ff);
		System.out.println("diff ft: "+ft);
		System.out.println("diff tf: "+tf);
		System.out.println("diff tt: "+tt);
		
	}


}
