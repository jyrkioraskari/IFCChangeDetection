package fi.ni;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

import fi.ni.vo.DatasetStatsVO;
import fi.ni.vo.DiffReportVO;

public class TestReports {
	Map<String,Property> properties=new HashMap<String,Property>();
	Model model;
	String ns_reports="http://drum/testreports/";
	String ns_dataset="http://drum/dataset/";
	
	public TestReports()
	{
		model =ModelFactory.createDefaultModel();
    	(new DiffReportVO()).genProperties(ns_reports,model,properties);
    	(new DatasetStatsVO()).genProperties(ns_dataset,model,properties);

	}
	
    public void addTestResult(DiffReportVO d)
    {
    	d.genTriples(ns_reports, model, properties);
    }
    
    public void addStats(DatasetStatsVO d)
    {
    	
    	d.genTriples(ns_dataset, model, properties);
    }
    
    public void openModel()
    {
    	readModel("c:/2014/results/measurements.ttl", "Turtle");
    }
    
    public void closeModel()
    {
    	writeModel(model,"c:/2014/results/measurements.ttl", "Turtle");
    }
    public void removeVersion(String algorithm,int version, final Model m)
    {
    	String query = "SELECT * \nWHERE{ ?s <http://drum/testreports/Version> "+version+".\n ?s <http://drum/testreports/Algorithm> \""+algorithm+"\"}";
    	Query q = QueryFactory.create(query); // SPARQL 1.1
    	QueryExecution qExe = QueryExecutionFactory.create(q, m);
    	ResultSet resultsRes = qExe.execSelect();
        List<RDFNode> subjects=new ArrayList<RDFNode>();
    	try {
    	  while (resultsRes.hasNext()) {                
    	    QuerySolution soln = resultsRes.nextSolution();
    	    subjects.add(soln.get("s"));
    	  }
    	} catch (Exception ex) {
    	  System.out.println(ex);
    	}
    	for(RDFNode s:subjects)
    	  m.removeAll(s.asResource(), null, null);
    }
    
	private void readModel(String filename,String type) {
		InputStream in = FileManager.get().open(filename);
		if (in != null) {
			model.read(in, null, type);  // Model closes the input
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
    
    
    public static void main(String[] args) {
		TestReports tr=new TestReports();
		tr.openModel();
		DiffReportVO d=new DiffReportVO("ArchiCAD-64","file1","file2",0,"AAPath","no specific");
		tr.addTestResult(d);
		tr.closeModel();
	}
}
