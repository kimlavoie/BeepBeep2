package ca.uqac.lif.beepbeep2.parser;

import ca.uqac.lif.beepbeep2.parser.Blueprint;
import ca.uqac.lif.beepbeep2.processor.Processor;
import java.util.List;

public class ProcessorParser{
    public class InvalidProcessorException extends Exception{};
    public class InvalidBlueprintException extends Exception{};

    private List<Blueprint> blueprints;

    public ProcessorParser(List<Blueprint> blueprints){
	this.blueprints = blueprints;
    }

    public Processor parse(String command) throws InvalidProcessorException{
	for(Blueprint blueprint : blueprints){
            try{
                return parseFromBlueprint(blueprint);
	    }catch(InvalidBlueprintException ex) {}
	}
	throw new InvalidProcessorException();
    }

    public Processor parseFromBlueprint(Blueprint blueprint) throws InvalidBlueprintException{
	    throw new InvalidBlueprintException();
    }
    
}
