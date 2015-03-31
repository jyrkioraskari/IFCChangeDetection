package fi.ni.nodenamer.fqtable;

import java.util.Map;
import java.util.TreeMap;

public class LiteralPropertyValues {
	Map<String, Long> values = new TreeMap<String, Long>();
	double values_count = 0;

	public boolean add(String value) {
		Long count = values.get(value);
		if (count == null) {
			count =0l;
		} 
		values_count += 1;
		values.put(value, count+1);
		boolean all_one=true;
		for(Map.Entry<String,Long> entry:values.entrySet())
		{
			if(entry.getValue()!=1l)
				all_one=false;
		}
		return all_one;
	}

	// Kuinka harvinainen on
	public double test(String value) {
		Long count = values.get(value);
		if (count == null) {
			return -1;
		}

		return ((double)count)/values_count;
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

	public boolean isUnique() {
		boolean all_one=true;
		for(Map.Entry<String,Long> entry:values.entrySet())
		{
			if(entry.getValue()!=1l)
				all_one=false;
		}
		return all_one;
	}

	public double getValues_count() {
		return values_count;
	}

	public void setValues_count(double values_count) {
		this.values_count = values_count;
	}


}

