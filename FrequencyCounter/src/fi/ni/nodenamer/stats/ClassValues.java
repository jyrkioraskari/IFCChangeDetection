package fi.ni.nodenamer.stats;

import java.util.Map;
import java.util.TreeMap;

public class ClassValues {
	Map<String, Long> values = new TreeMap<String, Long>();
	double values_count = 0;

	public void add(String value) {
		Long count = values.get(value);
		if (count == null) {
			values.put(value, 1l);
		} else {
			values.put(value, count + 1);
		}
		values_count += 1;
	}

	public double test(String value) {
		Long count = values.get(value);
		if (count == null) {
			return 1.0;
		}

		double val = ((double)count) / values_count;
		return val;
	}

	public Map<String, Long> getValues() {
		return values;
	}

	public void setValues(Map<String, Long> values) {
		this.values = values;
	}

	public double getCsum_values_count() {
		return values_count;
	}

	public void setCsum_values_count(double csum_values_count) {
		this.values_count = csum_values_count;
	}


}

