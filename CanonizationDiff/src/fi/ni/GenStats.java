package fi.ni;

import com.hp.hpl.jena.ontology.OntModel;

import fi.ni.nodenamer.RDFHandler;

public class GenStats {
	OntModel model;
	
    public GenStats() {
    }
    
	public void createInternalGraph(String directory, String filename,String datatype) {
		RDFHandler ifch = new RDFHandler();
		model=ifch.handleRDF(directory, filename, datatype);
	}



	public OntModel getModel() {
		return model;
	}

    
}
