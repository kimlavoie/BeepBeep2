package ca.uqac.lif.beepbeep2.parser;

import ca.uqac.lif.beepbeep2.parser.Blueprint;
import ca.uqac.lif.beepbeep2.processor.Processor;
import ca.uqac.lif.beepbeep2.processor.Pipe;
import ca.uqac.lif.beepbeep2.parser.Tree;
import ca.uqac.lif.beepbeep2.processor.ProcessorFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ProcessorParser{
    public class InvalidProcessorException extends Exception{};
    public class InvalidBlueprintException extends Exception{};
    public class InvalidKeywordException extends Exception{};
    public class InvalidOptionException extends Exception{};
    public class InvalidOptionalException extends Exception{};
    public class InvalidParamException extends Exception{};
    public class InvalidInputException extends Exception{};

    private List<Blueprint> blueprints;
    private List<Pipe> inputs = new ArrayList<Pipe>();
    private Map<String, String> options = new HashMap<String, String>();

    public ProcessorParser(List<Blueprint> blueprints){
	this.blueprints = blueprints;
    }

    public class ProcessorAndCommand{
        private Processor processor;
	private String command;

	public ProcessorAndCommand(Processor processor, String command){
            this.processor = processor;
	    this.command = command;
	}

	public void setProcessor(Processor processor){this.processor = processor;}
	public Processor getProcessor(){return processor;}
	public void setCommand(String command){this.command = command;}
	public String getCommand(){return command;}
    }

    public Processor parse(String command) throws InvalidProcessorException{
	for(Blueprint blueprint : blueprints){
            try{
                ProcessorAndCommand pac =  parseFromBlueprint(blueprint, command);
		if(!pac.getCommand().trim().equals(".")) throw new InvalidProcessorException();
		return pac.getProcessor();
	    }catch(InvalidBlueprintException ex) {}
	}
	throw new InvalidProcessorException();
    }

    public ProcessorAndCommand parseFromBlueprint(Blueprint blueprint, String command) throws InvalidBlueprintException{
	    inputs.clear();
	    options.clear();
	    Tree<String> root = blueprint.getParseTree();
	    if(!root.getValue().equals("<processor>")) throw new InvalidBlueprintException();
	    String currentCommand = command;
	    for(Tree<String> child : root.getChildren()){
		currentCommand = currentCommand.trim();
                try{
                    currentCommand = parseKeyword(child, currentCommand);
		    continue;
		}catch(InvalidKeywordException ex){}
		try{
                    currentCommand = parseParam(child, currentCommand);
		    continue;
		}catch(InvalidParamException ex){}
		try{
                    currentCommand = parseInput(child, currentCommand);
		    continue;
		}catch(InvalidInputException ex){}
		try{
                    currentCommand = parseOptional(child, currentCommand);
		    continue;
		}catch(InvalidOptionalException ex){}
		throw new InvalidBlueprintException();
	    }
	    ProcessorFactory factory = new ProcessorFactory();
	    Processor processor = factory.getProcessor(blueprint.getProcessorName(), this.options, this.inputs);
            return new ProcessorAndCommand(processor, currentCommand);
    }

    private String parseKeyword(Tree<String> root, String command) throws InvalidKeywordException{
        if(!root.getValue().equals("<keyword>")) throw new InvalidKeywordException();
	String keywordValue = root.getChild(0).getValue();
	int i;
	for(i = 0; i < keywordValue.length(); i++){
            if(keywordValue.toUpperCase().charAt(i) != command.toUpperCase().charAt(i)) throw new InvalidKeywordException();
	}
	if(Character.isLetterOrDigit(command.charAt(i))) throw new InvalidKeywordException();
	return command.substring(i);
    }

    private String parseParam(Tree<String> root, String command) throws InvalidParamException{
        if(!root.getValue().equals("<param>")) throw new InvalidParamException();
	String restriction = root.getChild(1).getValue();
	String key = root.getChild(0).getValue();
	if(restriction.equals("number")){
            if(!Character.isDigit(command.charAt(0))) throw new InvalidParamException();
	    String param = "";
	    int i = 0;
	    while(Character.isDigit(command.charAt(i))){
                param += command.charAt(i);
	    }
	    // Might cause problem if we backtrack. Maybe put in a temporary Map?
	    options.put(key, param);
	    return command.substring(i);
	}else if(restriction.equals("string")){
	    if(command.charAt(0) != '"') throw new InvalidParamException();
	    String param = "";
	    int i = 1;
	    while(command.charAt(i) != '"'){
                param += command.charAt(i);
	    }
	    options.put(key,param);
	    return command.substring(i+1);
	}else{
            //wrong restriction, correct that in blueprintparser
	    System.err.println("RESTRICTION ERROR IN PROCESSORPARSER!!!!!");
	    System.exit(99);
	}
    }

    private String parseInput(Tree<String> root, String command) throws InvalidInputException{
	if(!root.getValue().equals("<input>")) throw new InvalidInputException();
	ProcessorParser parser = new ProcessorParser(blueprints);
	for(Blueprint blueprint : blueprints){
            try{
                ProcessorAndCommand pac =  parser.parseFromBlueprint(blueprint, command);
		for(Pipe pipe : pac.getProcessor().getOutputs()){
		    inputs.add(pipe);
		}
		return pac.getCommand();
	    }catch(InvalidBlueprintException ex) {}
	}
	throw new InvalidProcessorException();
 
    }

    private String parseOptional(Tree<String> root, String command) throws InvalidOptionalException{
        if(!root.getValue().equals("<optional>")) throw new InvalidOptionalException();
	for(Tree<String> child : root.getChildren()){
            try{
                return parseOption(child, command);
	    }catch(InvalidOptionException ex){}
	}
	throw new InvalidOptionalException();
    }

    private String parseOption(Tree<String> root, String command) throws InvalidOptionException{
        if(!root.getValue().equals("<option>")) throw new InvalidOptionException();
	String currentCommand = command;
	for(Tree<String> child : root.getChildren()){
	    currentCommand = currentCommand.trim();
            try{
                currentCommand = parseKeyword(child, currentCommand);
                continue;
	    }catch(InvalidKeywordException ex){}
      	    try{
                currentCommand = parseParam(child, currentCommand);
		continue;
	    }catch(InvalidParamException ex){}
	    try{
                currentCommand = parseInput(child, currentCommand);
		continue;
	    }catch(InvalidInputException ex){}
            try{
                currentCommand = parseOptional(child, currentCommand);
		continue;
	    }catch(InvalidOptionalException ex){}
	    throw new InvalidOptionException();
	}
        return currentCommand;
    }
    
}
