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


public class VisitorTest implements OutputFormatVisitor {

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
	
	public VisitorTest() {
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
	
	public void visit(ParseNode node) {
		final String CLASS_PATH = "ca.uqac.lif.beepbeep2.";
		
		String token = node.getToken();
		
		//later : at every new grammar addition, put new <shortname> and SHORTNAME in map		
		
		List<ParseNode> ChildrenList = null;
		ChildrenList = node.getChildren();
		System.out.print("new token: "+ token + "\n");
		
		if (token.equals("<2-1_processor>") || token.equals("<2-0_processor>")) {
			binary_processor = true;
		}
		
		if (map.containsKey(token)) { //is a new processor, so can get the name
			System.out.print("getting the name\n");
			name = CLASS_PATH + map.get(token).toLowerCase().replaceAll("\\s","");
			System.out.println(name);	
			after_name = true;
			return;
		}
		
		if (after_name) { 
			System.out.print("into after_name\n");
			if (!ChildrenList.isEmpty()) {
				if (token.contains("_opt")) {
					System.out.print("there are options\n");
					options = true;
					size = node.getSize();
				}
				else {
					params = true;
					System.out.print("there are no options, go to parameters directly\n");
				}
				after_name = false;
			}
		}
		
		if (options) {
			System.out.print("into options\n");
			if (ChildrenList.isEmpty()) { 
				opts.add(token); 
				System.out.println(token);
			}
			size--;
			if (size == 0) { 
				options = false;
				params = true;
			}
		}
		
		
		if (params) { 
			System.out.print("into params\n");
			
			if (token.equals("<trace>")) {
				System.out.print("param is a trace\n");
				trace = true;
				return;
			}
			else if (token.equals("<filename>")) {
				System.out.print("param is a filename\n");
				file = true;
				return;
			}
			else if (token.contains("processor")) {
				System.out.print("param is a new processor\n");
				inputs.add("output()");
				
				params = false;
				newProcessor = true;
			}
			if (trace) {
				if (ChildrenList.isEmpty()) {
					inputs.add(token);
					System.out.print(token + " added into inputs trace\n");
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
					System.out.print(token + " added into inputs\n");
					if (!binary_processor || (binary_processor && inputs.size() >= MAX_PARAMETERS)) {
						newProcessor = true;
						params = false;
					}
					file = false;
				}
				
			}
		}
			
		if (newProcessor) {
			System.out.print("output() added into inputs\n");
			
			if (!binary_processor || (binary_processor && inputs.size() >= MAX_PARAMETERS)) {
				ProcessorData processor = new ProcessorData(name, opts, inputs);
				processorStack.push(processor);
				
				System.out.print("a new processorData was added to stack\n");
				System.out.print(name + opts + inputs + "\n");
				
				inputs.clear(); //TODO how to keep the inputs and options in processorStack after clear() ?
				if(!opts.isEmpty())
					opts.clear();
				
				name = null;
			}
			newProcessor = false;
		}
	
		if (token.equals(".")) {
			System.out.print("end of the ESQL string\n\n");
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
				
				/*
				ProcessorData processor = processorStack.pop();
				System.out.print(processor.getName() + processor.getOptions() + processor.getInputs() + "\n\n");
				*/
			}
			//System.out.print("getProcessor(" + name + ", " + opts + ", " + inputs + ")" + "\n");
		}
			
	}
	
	/*
	 * look at stack.top(). look at inputs. if input is instance of ProcessorData, go to this processorData
	 * until no input is processorData. Then, call processors recursively TODO somehow* 
	 * with input as processor.output
	*/
	public void pop() {
		// TODO Auto-generated method stub

	}

	public String toOutputString() {
		// TODO Auto-generated method stub
		return null;
	}

}
