package query;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * This class uses an interactive shell for user to query a loaded rdf model
 * @author Haoyuan Huang
 *
 */
public class RDFQuery {
	
	public static void main(String[] args) {
		System.out.println("Creating rdf model...");
		Model model = ModelFactory.createDefaultModel();
		try {
			System.out.println("Reading dblp.rdf from file...");
			model.read(new FileInputStream(new File(conversion.PathUtils.outputPath)), null);
			System.out.println("Read dblp.rdf from file complete.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		/**
		 * User interaction section
		 */
		Scanner s = new Scanner (System.in);
		while (true) {
			System.out.print("input>");
			String[] arguments = 
				s.nextLine().split(" ", 2);
			if (arguments.length > 0) {
				if (arguments[0].equals("exit")) {
					break;
				} else {
					Command command = 
						QueryCommandFactory.create(model, arguments);
					if (command != null)
						command.execute();					
				}
			}
		}
		try {
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
