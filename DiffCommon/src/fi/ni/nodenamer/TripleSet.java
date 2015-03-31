package fi.ni.nodenamer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TripleSet {
	final Set<String> triple_strings=new HashSet<String>();
	final Set<String> triple_strings_orgid=new HashSet<String>();
	final Map<String,List<Triple>> triples=new HashMap<String,List<Triple>>();

	// Kaksi kysymystä: - löytyykö muuttuneelle jena-stringille vastinparia aapaths-muutosjoukosta  added, removed
	// 					- löytyykö muuttuneelle aapaths-stringilel vastinparia jena-muutosjoukosta  added, removed

	final Map<String,List<String>> paths_org_map=new HashMap<String,List<String>>();
	final Map<String,List<String>> org_paths_map=new HashMap<String,List<String>>();
	
	public Set<String> getTriple_strings() {
		return triple_strings;
	}
	public Map<String, List<Triple>> getTriples() {
		return triples;
	}
	
	public Set<String> getTriple_strings_orgid() {
		return triple_strings_orgid;
	}
	public Map<String, List<String>> getPaths_org_map() {
		return paths_org_map;
	}
	
	public Map<String, List<String>> getOrg_paths_map() {
		return org_paths_map;
	}

}
