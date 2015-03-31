package fi.ni;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
import com.hp.hpl.jena.util.FileManager;

import fi.ni.vo.DatasetStatsVO;
import fi.ni.vo.DiffReportVO;

public class ExcelReport {
	Map<String, Property> properties = new HashMap<String, Property>();
	Model model;
	String ns_reports = "http://drum/testreports/";
	String ns_dataset = "http://drum/dataset/";

	public ExcelReport() {
		model = ModelFactory.createDefaultModel();
		(new DiffReportVO()).genProperties(ns_reports, model, properties);
		(new DatasetStatsVO()).genProperties(ns_dataset, model, properties);

	}

	public void openModel() {
		readModel("c:/2014/results/measurements.ttl", "Turtle");
		readModel("c:/2014/results/datasets.ttl", "Turtle");
	}

	private void readModel(String filename, String type) {
		InputStream in = FileManager.get().open(filename);
		if (in != null) {
			model.read(in, null, type);
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static final String qResults=""
			+ "SELECT * \n"
			+ "WHERE{ "
			+ "?s <http://drum/testreports/Algorithm> ?algorithm ."
			+ "?s <http://drum/testreports/File1> ?f1 ."
			+ "?s <http://drum/testreports/File2> ?f2 ."
			+ "?s <http://drum/testreports/Add_diff_ff> ?aff ."
			+ "?s <http://drum/testreports/Add_diff_ft> ?aft ."
			+ "?s <http://drum/testreports/Add_diff_tf> ?atf ."
			+ "?s <http://drum/testreports/Add_diff_tt> ?att ."
			+ "?s <http://drum/testreports/Remove_diff_ff> ?rff ."
			+ "?s <http://drum/testreports/Remove_diff_ft> ?rft ."
			+ "?s <http://drum/testreports/Remove_diff_tf> ?rtf ."
			+ "?s <http://drum/testreports/Remove_diff_tt> ?rtt ."
			+ "}";
	
	public void printResults() {
		Query q = QueryFactory.create(qResults); 
		QueryExecution qExe = QueryExecutionFactory.create(q, model);
		ResultSet resultsRes = qExe.execSelect();
		try {
			while (resultsRes.hasNext()) {
				QuerySolution soln = resultsRes.nextSolution();
				System.out.println("Algorithm: " + soln.get("algorithm").asLiteral().getLexicalForm());
				System.out.println("F1: " + soln.get("f1").asLiteral().getLexicalForm());
				System.out.println("F2: " + soln.get("f2").asLiteral().getLexicalForm());
				System.out.println("AFF: " + soln.get("aff").asLiteral().getLexicalForm());
				System.out.println("AFT: " + soln.get("aft").asLiteral().getLexicalForm());
				System.out.println("ATF: " + soln.get("atf").asLiteral().getLexicalForm());
				System.out.println("ATT: " + soln.get("att").asLiteral().getLexicalForm());
				
				System.out.println("RFF: " + soln.get("rff").asLiteral().getLexicalForm());
				System.out.println("RFT: " + soln.get("rft").asLiteral().getLexicalForm());
				System.out.println("RTF: " + soln.get("rtf").asLiteral().getLexicalForm());
				System.out.println("RTT: " + soln.get("rtt").asLiteral().getLexicalForm());}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public static void main(String[] args) {
		ExcelReport tr = new ExcelReport();
		tr.openModel();
		tr.printResults();
	}
}
