package fi.ni;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import fi.ni.nodenamer.stats.ClassLiteralCksumBag;
import fi.ni.nodenamer.stats.ClassStatsBag;

public class FrequencyTable{
	private ClassStatsBag classbag;
	private ClassLiteralCksumBag class_chksums_bag;
	
    final String codebase_name;
	
	public FrequencyTable(String codebase_name)
	{
		this.codebase_name=codebase_name;
		read_ClassStatistics();
		read_ClassLiteralStatistics();
	}
	
	public void close()
	{
		save_ClassStatistics();
		save_ClassLiteralStatistics();
	}
	
	
	private void read_ClassStatistics() {

		FileInputStream fis;
		XMLDecoder xmlDecoder=null;
		try {
			fis = new FileInputStream("c:\\2014\\"+codebase_name+"_statistics_class.xml");
			BufferedInputStream bis = new BufferedInputStream(fis);
			xmlDecoder = new XMLDecoder(bis);
			classbag = (ClassStatsBag) xmlDecoder.readObject();
			
		} catch (FileNotFoundException e) {
			classbag = new ClassStatsBag();
		}
		finally
		{
			if(xmlDecoder!=null)
			  xmlDecoder.close();
		}

	}

	private void save_ClassStatistics() {
		XMLEncoder e=null;
		try {
			e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("c:\\2014\\"+codebase_name+"_statistics_class.xml")));
			e.writeObject(classbag);			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(e!=null)
			  e.close();
		}
	}

	private void read_ClassLiteralStatistics() {

		FileInputStream fis;
		XMLDecoder xmlDecoder=null;
		try {
			fis = new FileInputStream("c:\\2014\\"+codebase_name+"_statistics_classliteral.xml");
			BufferedInputStream bis = new BufferedInputStream(fis);
			xmlDecoder = new XMLDecoder(bis);
			class_chksums_bag = (ClassLiteralCksumBag) xmlDecoder.readObject();
			
		} catch (FileNotFoundException e) {
			class_chksums_bag = new ClassLiteralCksumBag();
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
			e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("c:\\2014\\"+codebase_name+"_statistics_classliteral.xml")));
			e.writeObject(class_chksums_bag);			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(e!=null)
			  e.close();
		}
	}

	public ClassStatsBag getClassbag() {
		return classbag;
	}

	public ClassLiteralCksumBag getClass_chksums_bag() {
		return class_chksums_bag;
	}

}
