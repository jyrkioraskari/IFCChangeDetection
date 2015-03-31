package fi.ni.nodenamer.fqtable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DatasetsBag {

	private Map<String, ClassLiteralProperties> bag = new TreeMap<String, ClassLiteralProperties>();
	private Map<String, Set<String>> uniques = new HashMap<String, Set<String>>();
	Map<String, LiteralPropertes> probabilities = new TreeMap<String, LiteralPropertes>();

	public void add(String dataset, String class_name, String property_name, String value) {
		ClassLiteralProperties dataset_value = bag.get(dataset);
		if (dataset_value == null) {
			dataset_value = new ClassLiteralProperties();
			bag.put(dataset, dataset_value);
		}

		dataset_value.add(class_name, property_name, value);
	}

	public double test(String dataset, String class_name, String property_name, String value) {
		ClassLiteralProperties class_value = bag.get(class_name);
		if (class_value == null) {
			class_value = new ClassLiteralProperties();
			bag.put(class_name, class_value);
			System.err.println("ClassLiteralProperties  class does not exist at the bag! " + dataset);
		}
		return class_value.test(class_name, property_name, value);
	}

	public void listUniques() {
		for (Map.Entry<String, ClassLiteralProperties> entry : bag.entrySet()) {
			uniques.putAll(entry.getValue().getUniques());
		}
		// Test that it is unique in all datasets
		for (Map.Entry<String, Set<String>> entry : uniques.entrySet()) {
			for (String property : entry.getValue()) {
				boolean is_uniq = true;
				for (Map.Entry<String, ClassLiteralProperties> dataset_entry : bag.entrySet()) {
					if (!dataset_entry.getValue().test_uniqueness(entry.getKey(), property))
						is_uniq = false;
				}
				if (is_uniq)
					System.out.println("uniq: " + entry.getKey() + " " + property);

			}
		}

	}

	public void listFrequencies() {
		for (Map.Entry<String, ClassLiteralProperties> dataset_entry : bag.entrySet()) {
			probabilities.putAll(dataset_entry.getValue().getBag());
		}
		
		for (Map.Entry<String, LiteralPropertes> lpentry : probabilities.entrySet()) {
			for (Map.Entry<String, LiteralPropertyValues> lpventry : lpentry.getValue().getProperties().entrySet()) {
				for (Map.Entry<String, Long> ventry : lpventry.getValue().getValues().entrySet()) {
					System.out.println(lpentry.getKey()+"."+lpventry.getKey()+"."+ventry.getKey()+": "+lpventry.getValue().test(ventry.getKey()));
				}	
				
			}	
		}

	}

	public Map<String, ClassLiteralProperties> getBag() {
		return bag;
	}

	public void setBag(Map<String, ClassLiteralProperties> bag) {
		this.bag = bag;
	}

	public Map<String, Set<String>> getUniques() {
		return uniques;
	}

	public void setUniques(Map<String, Set<String>> uniques) {
		this.uniques = uniques;
	}

	public Map<String, LiteralPropertes> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(Map<String, LiteralPropertes> probabilities) {
		this.probabilities = probabilities;
	}

	
}
