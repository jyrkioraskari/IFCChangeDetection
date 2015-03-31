package fi.ni.nodenamer.stats;

import java.util.Map;
import java.util.TreeMap;

public class ClassLiteralCksumBag {

	Map<String, ClassValues> bag = new TreeMap<String, ClassValues>();

	public void add(String class_name, String value) {
		ClassValues class_value = bag.get(class_name);
		if (class_value == null) {
			class_value = new ClassValues();
			bag.put(class_name, class_value);
		}

		class_value.add(value);
	}

	public double test(String class_name, String value) {
		ClassValues class_value = bag.get(class_name);
		if (class_value == null) {
			class_value = new ClassValues();
			bag.put(class_name, class_value);
			System.err
					.println("ClassLiteralCksumBag  class does not exist at the bag! "+class_name);
		}
		double val = class_value.test(value);
		return val;
	}

	public Map<String, ClassValues> getBag() {
		return bag;
	}

	public void setBag(Map<String, ClassValues> bag) {
		this.bag = bag;
	}


}
