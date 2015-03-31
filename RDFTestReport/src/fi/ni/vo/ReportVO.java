package fi.ni.vo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public abstract class ReportVO {

	public void genProperties(String ns, Model model, Map<String, Property> properties) {
		Method methods[] = this.getClass().getMethods();
		for (Method method:methods) {		    
			if (method.getName().startsWith("get")) {
				properties.put(method.getName().substring(3), model.createProperty(ns+method.getName().substring(3)));
			}
		}
	}

	public void genTriples(String ns, Model model, Map<String, Property> properties) {
		Resource rthis=model.createResource(ns+UUID.randomUUID());
		Method methods[] = this.getClass().getMethods();
		for (Method method:methods) {		    
			if (method.getName().startsWith("get")) {
				try {
					Property p=properties.get(method.getName().substring(3));
					if(p==null)
						continue;
					Object o = method.invoke(this);
					if(o.getClass().getName().equals("java.lang.Long"))
					{
					  Long n=(Long)o;
					  if(n>=0)
					  {
						  rthis.addProperty(p, ""+n,XSDDatatype.XSDinteger);
					  }
					}
					else if(o.getClass().getName().equals("java.lang.String"))
					{
						  String s=(String)o;						  
						  rthis.addProperty(p, s,XSDDatatype.XSDstring);
					}
					else if(o.getClass().getName().equals("java.lang.Class"))
					;					
					else if(o.getClass().getName().equals("java.lang.Integer"))
					{
						  Integer n=(Integer)o;
						  if(n>=0)
						  {
							  rthis.addProperty(p, ""+n,XSDDatatype.XSDinteger);
						  }
					}
					else
					{
						  rthis.addProperty(p, o.toString(),XSDDatatype.XSDstring);
					}
						
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public ReportVO() {
		super();
	}

}