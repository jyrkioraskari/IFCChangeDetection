package fi.ni.nodenamer.stats;

import java.util.Map;
import java.util.TreeMap;

public class ClassStatsBag {
  public long total_class_instances=0; 
  
   Map<String,Long> class_map=new TreeMap<String,Long>();   
   
   public void add(String class_name)
   {
	  Long  class_value=class_map.get(class_name);
	   if(class_value==null)
	   {
		   class_value=0l;
		   
	   }
	   class_value++;
	   class_map.put(class_name, class_value);
	   total_class_instances++;
   }
   
    
  
   public double test(String class_name)
   {
	   Long  cd=class_map.get(class_name);
	   if(cd==null)
		   return 1f;
	   
	   double cpers=((double)cd)/((double)total_class_instances);
	   return cpers;
	}



public long getTotal_class_instances() {
	return total_class_instances;
}



public void setTotal_class_instances(long total_class_instances) {
	this.total_class_instances = total_class_instances;
}



public Map<String, Long> getClass_map() {
	return class_map;
}



public void setClass_map(Map<String, Long> class_map) {
	this.class_map = class_map;
}



    
     
}
