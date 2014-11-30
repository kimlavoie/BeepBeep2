package ca.uqac.lif.beepbeep2;

import ca.uqac.lif.beepbeep2.parser.Tree;
import ca.uqac.lif.beepbeep2.parser.Blueprint;
import ca.uqac.lif.beepbeep2.parser.BlueprintParser;
import ca.uqac.lif.beepbeep2.parser.ProcessorParser;
import ca.uqac.lif.beepbeep2.processor.Processor;
import ca.uqac.lif.beepbeep2.processor.ProcessorFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Interpreter{
    private class InvalidCommandException extends Exception{};

    private List<Blueprint> blueprints = new ArrayList<Blueprint>();
    private boolean stopInterpreting = false;

    public void init() throws InvalidCommandException{
            interpret("MAKE \"PrintProcessor\" FROM \"PRINT [AT <delay:number> PER SECOND | ON KEY <key:string>] [<quantity:numner> OF] <input> \"");
            interpret("MAKE \"MultiplyProcessor\" FROM \"MULTIPLY <input> BY <input>\"");
    }

    public void start_interactive() throws InvalidCommandException{
	init();
        System.out.println("Welcome to BeepBeep2 Complex Event Processor (CEP)'s interactive interpreter");
	Scanner scanner = new Scanner(System.in);
	while(!stopInterpreting){
	    // Because ant buffer io, I'm forced to use println here. Change it to print when ready for production
            System.out.println("Enter a command > ");
	    System.out.flush();
	    String command = scanner.nextLine();
	    try{
                interpret(command);
	    } catch(InvalidCommandException e){System.out.println("Invalid command");}
	}
    }

    public void start_from_file(String filepath) throws InvalidCommandException, FileNotFoundException, IOException{
	init();
	BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
	String line;
	while((line = br.readLine()) != null){
            interpret(line);
	}
	    System.out.println(blueprints);
	br.close();
    }

    private void interpret(String command) throws InvalidCommandException{
            if(command.trim().toUpperCase().equals("END.")){
		stopInterpreting = true;
		return;
	    }
	    try{
		BlueprintParser blueprintParser = new BlueprintParser();
		Blueprint blueprint = blueprintParser.parse(command);
		blueprints.add(blueprint);
		return;
	    } catch(BlueprintParser.NotAValidBlueprintException ex){}
	    try{
                ProcessorParser processorParser = new ProcessorParser(blueprints);
		Processor processor = processorParser.parse(command);
		processor.start();
		return;
	    }catch(ProcessorParser.InvalidProcessorException ex){}
	    throw new InvalidCommandException();
    }

    public static void main(String[] args) throws Exception{
        Interpreter interpreter = new Interpreter();
	interpreter.init();
	interpreter.start_from_file("commands.txt");
	//interpreter.start_interactive();
    }
}
