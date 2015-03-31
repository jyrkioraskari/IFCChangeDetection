package fi.ni;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.util.FileManager;

import fi.ni.vo.DatasetStatsVO;

public class DatasetStatsStore {
	Map<String,Property> properties=new HashMap<String,Property>();
	Model model;
	String ns_dataset="http://drum/dataset/";
	
	public DatasetStatsStore()
	{
		model =ModelFactory.createDefaultModel();
    	(new DatasetStatsVO()).genProperties(ns_dataset,model,properties);
	}
	
    public void addStats(DatasetStatsVO d)
    {
    	
    	d.genTriples(ns_dataset, model, properties);
    }
    
    public void openModel()
    {
    	readModel("c:/2014/results/datasets.ttl", "Turtle");
    }
    
    public void closeModel()
    {
    	writeModel(model,"c:/2014/results/datasets.ttl", "Turtle");
    }
	private void readModel(String filename,String type) {		
		InputStream in = FileManager.get().open(filename);
		if (in != null) {
			model.read(in, null, type);
		}
	}

	private void writeModel(Model m,String filename,String type) {
		FileWriter out;
		try {
			out = new FileWriter( filename );
			try {
			    m.write( out, type );
			}
			finally {
			   try {
			       out.close();
			   }
			   catch (IOException closeException) {
			       // ignore
			   }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
