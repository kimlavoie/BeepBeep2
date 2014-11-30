package ca.uqac.lif.beepbeep2.parser;

import ca.uqac.lif.beepbeep2.parser.Tree;

public class Blueprint{
    private String processorName;
    private Tree<String> parseTree;

    public Blueprint(String processorName, Tree<String> parseTree){
        this.processorName = processorName;
	this.parseTree = parseTree;
    }

    public String toString(){
        return "ProcessorName = " + processorName + "\nParseTree = " + parseTree;
    }

    public String getProcessorName() {return processorName;}
    public Tree<String> getParseTree(){return parseTree;}
}
