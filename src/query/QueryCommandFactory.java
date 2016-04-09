package query;
import java.util.TreeSet;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DC_11;

public class QueryCommandFactory {

	/**
	 * Returns a command object for execution.
	 * Will return null if the command is invalid.
	 * @param model
	 * @param args
	 * @return
	 */
	public static Command create(Model model, String ... args) {
		if (args.length == 0) {
			return null;
		} else if (args[0].equals("details")) {
			return new FindDetailInfoCommand (model, args);
		} else if (args[0].equals("coauthor")) {
			return new FindCoauthorsOfPaperCommand(model, args);
		} else {
			return null;
		}
	}

	/*
	 * 
	 * Factory pattern here.
	 * Just don't want to create too many files
	 *
	 */


	/**
	 * Find the detailed information about a paper using a full title.
	 * @author Haoyuan Huang
	 *
	 */
	public static class FindDetailInfoCommand extends Command {

		public FindDetailInfoCommand(Model model, String... args) {
			super (model, args);
		}
		@Override
		public void execute() {
			if (getArgs().length < 2) {
				System.out.println("Usage: details <paper_name>");
				return;
			}
			try {
				System.out.println("Searching...");
				Model model = getModel();
				String title = getArgs()[1];
				ResIterator iterator = model
						.listSubjectsWithProperty(DC_11.title, title);
				while (iterator.hasNext()) {
					Resource resource = iterator.next();
					print(resource);
				}
			} catch (Exception e) {
				System.err.println("Something went wrong with this command.");
			}
		}

	}
	
	/**
	 * Filter all the resources that has the 
	 * author's name as the object of the
	 * DC_11.creator property.
	 * 
	 * Then use a set to collect all the 
	 * unique names within these resources (papers)
	 * 
	 * @author Haoyuan Huang
	 *
	 */
	public static class FindCoauthorsOfPaperCommand extends Command {

		public FindCoauthorsOfPaperCommand(Model model, String[] args) {
			super(model, args);
		}

		@Override
		public void execute() {
			String [] args = getArgs();
			if (args.length < 2) {
				System.out.println("Usage: coauthor <author_name>");
				return;
			}
			Model model = getModel();
			String authorName = args[1];
			ResIterator iterator = 
				model.listSubjectsWithProperty(DC_11.creator, authorName);
			TreeSet <String> coauthors = new TreeSet <String> ();
			while (iterator.hasNext()) {
				Resource resource = iterator.next();
				StmtIterator stit =
						resource.listProperties(DC_11.creator);
				while (stit.hasNext()) {
					coauthors.add(
							stit.next().getString());
				}
			}
			coauthors.remove(authorName); // No need to list coauthor himself
			System.out.println(coauthors.toString());
		}
		
	}



	/**
	 * Print a statement that represents one paper Subject
	 * @param statement
	 */
	public static void print (Resource resource) {
		printHelper(resource, DC_11.title);
		printHelper(resource, DC_11.creator);
		printHelper(resource, DC_11.contributor);
		printHelper(resource, DC_11.date);
		printHelper(resource, DC_11.description);
		printHelper(resource, DC_11.subject);
		printHelper(resource, DC_11.identifier);
		printHelper(resource, DC_11.publisher);
	}

	/**
	 * Print one Property from the paper RDF resource.
	 * @param resource
	 * @param property
	 */	
	public static void printHelper (Resource resource, Property property) {
		Statement statement = resource.getProperty(property);
		if (statement != null) {
			System.out.println(statement.getString());
		}
	}
}
