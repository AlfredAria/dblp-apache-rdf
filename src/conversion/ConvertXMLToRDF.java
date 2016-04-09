package conversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * This class converts the source DBLP dataset
 * into and RDF file.
 * @author Haoyuan Huang
 *
 */
public class ConvertXMLToRDF {

	
	public static void main(String[] args) {
		
		if (args.length == 2) {
			PathUtils.inputPath = args[0];
			PathUtils.outputPath = args[1];
		}
		
		System.out.println("Creating RDF model...");
		Model model = ModelFactory.createDefaultModel();
		System.out.println("Parsing the input file into RDF model...");
		model = XMLToRDFUtils.parse(PathUtils.inputPath, model);
		try {
			System.out.println("Deleting output file if exists...");
			try {
				new File(PathUtils.outputPath).delete();
			} catch (Exception e) {
				
			}
			System.out.println("Writing model to the output file...");
			model.write(new FileOutputStream(new File(PathUtils.outputPath), false));
			System.out.println("Conversion finished.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
