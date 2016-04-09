package query;
import org.apache.jena.rdf.model.Model;

public abstract class Command {
	private String[] args;
	private Model model;

	public Command (Model model, String ... args) {
		this.args = args;
		this.model = model;
	}

	public String[] getArgs() {
		return args;
	}
	public Model getModel() {
		return model;
	}
	public abstract void execute ();
}
