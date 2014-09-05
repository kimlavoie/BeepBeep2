import java.util.ArrayList;


public class ProcessorData {
	
	String name;
	ArrayList<String> options;
	ArrayList<Object> inputs;
	
	public ProcessorData(String _name, ArrayList<String> _options, ArrayList<Object> _inputs) {
		name = _name;
		options = new ArrayList<String>();
		options.addAll(_options);
		inputs = new ArrayList<Object>();
		inputs.addAll(_inputs);
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public ArrayList<String> getOptions() {
		return options;
	}

	
	public ArrayList<Object> getInputs() {
		return inputs;
	}
	
}
