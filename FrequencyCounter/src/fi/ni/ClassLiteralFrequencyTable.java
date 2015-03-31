package fi.ni;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import fi.ni.nodenamer.fqtable.DatasetsBag;

public class ClassLiteralFrequencyTable{
	private DatasetsBag datasets;
	
    final String codebase_name;
	
	public ClassLiteralFrequencyTable(String codebase_name)
	{
		this.codebase_name=codebase_name;
		read_ClassLiteralStatistics();
	}
	
	public void close()
	{
		save_ClassLiteralStatistics();
	}
	

	private void read_ClassLiteralStatistics() {

		FileInputStream fis;
		XMLDecoder xmlDecoder=null;
		try {
			fis = new FileInputStream("c:\\2014\\"+codebase_name+"_ClassPropertyBag.xml");
			BufferedInputStream bis = new BufferedInputStream(fis);
			xmlDecoder = new XMLDecoder(bis);
			datasets = (DatasetsBag) xmlDecoder.readObject();
			
		} catch (FileNotFoundException e) {
			datasets = new DatasetsBag();
		}
		finally
		{
			if(xmlDecoder!=null)
			  xmlDecoder.close();
		}

	}
	
	private void save_ClassLiteralStatistics() {
		XMLEncoder e=null;
		try {
			e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("c:\\2014\\"+codebase_name+"_ClassPropertyBag.xml")));
			e.writeObject(datasets);			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(e!=null)
			  e.close();
		}
	}

	public DatasetsBag getDatasets() {
		return datasets;
	}

	public void setDatasets(DatasetsBag datasets) {
		this.datasets = datasets;
	}


}
