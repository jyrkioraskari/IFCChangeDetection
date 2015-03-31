package fi.ni.nodenamer.fqtable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ClassLiteralProperties {

	private Map<String, LiteralPropertes> bag = new TreeMap<String, LiteralPropertes>();
	private Map<String, Set<String>> uniques = new TreeMap<String, Set<String>>();  // Class, properties

	public void add(String class_name, String property_name, String value) {
		LiteralPropertes class_value = bag.get(class_name);
		if (class_value == null) {
			class_value = new LiteralPropertes();
			bag.put(class_name, class_value);
		}

		Set<String> uniq_properties=uniques.get(class_name);
		if(uniq_properties==null)
		{
			uniq_properties=new HashSet<String>();
			uniques.put(class_name, uniq_properties);
		}
			
		if(class_value.add(property_name,value))
		{
			uniq_properties.add(property_name);
		}
		else
		{
			// remove if exists
			if(uniq_properties.contains(property_name))
				uniq_properties.remove(property_name);
			if(uniq_properties.size()==0)
				uniques.remove(class_name);
		}
	}

	public double test(String class_name, String property_name, String value) {
		LiteralPropertes class_value = bag.get(class_name);
		if (class_value == null) {
			return -1;
		}
		return class_value.test(property_name,value);
	}

	public boolean test_uniqueness(String class_name, String property_name) {
		LiteralPropertes class_value = bag.get(class_name);
		if (class_value == null) {
			class_value = new LiteralPropertes();
			bag.put(class_name, class_value);
		}
		return class_value.test_uniqueness(property_name);
	}

	public Map<String, LiteralPropertes> getBag() {
		return bag;
	}

	public void setBag(Map<String, LiteralPropertes> bag) {
		this.bag = bag;
	}

	public Map<String, Set<String>> getUniques() {
		return uniques;
	}

	public void setUniques(Map<String, Set<String>> uniques) {
		this.uniques = uniques;
	}


}
