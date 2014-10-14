package ca.uqac.lif.beepbeep2.visitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.output.OutputFormatVisitor;


public class VisitorESQL implements OutputFormatVisitor {

	final int MAX_PARAMETERS = 2;
	String name;
	
	int size = 0;
	Boolean options = false, file = false, after_name = false;
	Boolean params = false, trace = false, newProcessor = false;
	Boolean binary_processor = false;
	
	ArrayList<String> opts;
	ArrayList<Object> inputs;
	Map<String, String> map;
	Stack<ProcessorData> processorStack;
	Stack<String> ProcessorInputsStack;
	
	public VisitorESQL() {
		map = new LinkedHashMap<String, String>();
		opts = new ArrayList<String>();
		inputs = new ArrayList<Object>();
		processorStack = new Stack<ProcessorData>();
		ProcessorInputsStack = new Stack<String>();
	}
	
	public void fillMap() {
		String processor_non_terminal = null, processor_shortname = null;	
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/processorsWithShortnames"));
			while (true) {
				processor_non_terminal = reader.readLine();
				if (processor_non_terminal == null) 
					break;
				processor_shortname = reader.readLine();
				map.put(processor_non_terminal, processor_shortname);
			}
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
			 
	}
	
	//TODO later : at every new grammar addition, put new <shortname> and SHORTNAME in map, or find another way
	
	public void visit(ParseNode node) {
		final String CLASS_PATH = "ca.uqac.lif.beepbeep2.";
		
		String token = node.getToken();		
		
		List<ParseNode> ChildrenList = null;
		ChildrenList = node.getChildren();
		
		if (token.equals("<2-1_processor>") || token.equals("<2-0_processor>")) {
			binary_processor = true;
		}
		
		if (map.containsKey(token)) { //is a new processor, so can get the name
			name = CLASS_PATH + map.get(token).toLowerCase().replaceAll("\\s","");
			after_name = true;
			return;
		}
		
		if (after_name) { 
			if (!ChildrenList.isEmpty()) {
				if (token.contains("_opt")) {
					options = true;
					size = node.getSize();
				}
				else {
					params = true;
				}
				after_name = false;
			}
		}
		
		if (options) {
			if (ChildrenList.isEmpty()) { 
				opts.add(token); 
			}
			size--;
			if (size == 0) { 
				options = false;
				params = true;
			}
		}
		
		
		if (params) { 
			
			if (token.equals("<trace>")) {
				trace = true;
				return;
			}
			else if (token.equals("<filename>")) {
				file = true;
				return;
			}
			else if (token.contains("processor")) {
				inputs.add("output()");
				
				params = false;
				newProcessor = true;
			}
			if (trace) {
				if (ChildrenList.isEmpty()) {
					inputs.add(token);
					if (!binary_processor || (binary_processor && inputs.size() >= MAX_PARAMETERS)) {
						newProcessor = true;
						params = false;
					}
					trace = false;
				}
			} 
			else if (file) {
				if (ChildrenList.isEmpty()) {
					inputs.add(token);
					if (!binary_processor || (binary_processor && inputs.size() >= MAX_PARAMETERS)) {
						newProcessor = true;
						params = false;
					}
					file = false;
				}
				
			}
		}
			
		if (newProcessor) {
			
			if (!binary_processor || (binary_processor && inputs.size() >= MAX_PARAMETERS)) {
				ProcessorData processor = new ProcessorData(name, opts, inputs);
				processorStack.push(processor);
				
				inputs.clear(); //TODO how to keep the inputs and options in processorStack after clear() ?
				if(!opts.isEmpty())
					opts.clear();
				
				name = null;
			}
			newProcessor = false;
		}
	
		if (token.equals(".")) {
			while (!processorStack.isEmpty()) {
				ProcessorData processor = processorStack.pop();
				inputs = processor.getInputs();
				if (inputs.contains("output()")) {
					int index = 0;
					for (Object input : inputs) {
						if (input.equals("output()")) {
							inputs.set(index, ProcessorInputsStack.pop());
						}
						index++;
					}
				}
				System.out.print("getProcessor(" + processor.getName() + ", " + processor.getOptions() + ", " + processor.getInputs() + ")\n"); //processor = getProcessor()
				ProcessorInputsStack.push("" + processor.getName()); //push(processor)
				
			}
		}
			
	}

	public void pop() {
		// TODO Auto-generated method stub

	}

	public String toOutputString() {
		// TODO Auto-generated method stub
		return null;
	}

}
