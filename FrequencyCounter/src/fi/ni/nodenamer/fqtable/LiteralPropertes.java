package fi.ni.nodenamer.fqtable;

import java.util.Map;
import java.util.TreeMap;

public class LiteralPropertes {
	Map<String, LiteralPropertyValues> properties = new TreeMap<String, LiteralPropertyValues>();

	public boolean add(String property_name,String value) {
		LiteralPropertyValues property = properties.get(property_name);
		if (property == null) {
			property=new LiteralPropertyValues();
			properties.put(property_name, property);
		} 

		return property.add(value);
	}

	public double test(String propertyname,String value) {
		LiteralPropertyValues property = properties.get(propertyname);
		if(property==null)
			return -1;
        return property.test(value);
	}
	
	public boolean test_uniqueness(String propertyname) {
		LiteralPropertyValues property = properties.get(propertyname);
		if(property==null)
			return true;
        return property.isUnique();
	}
	
	public Map<String, LiteralPropertyValues> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, LiteralPropertyValues> properties) {
		this.properties = properties;
	}


}

