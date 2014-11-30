package ca.uqac.lif.beepbeep2.parser;

import ca.uqac.lif.beepbeep2.parser.Tree;
import ca.uqac.lif.beepbeep2.parser.Blueprint;
import java.util.List;
import java.util.ArrayList;

public class BlueprintParser{
    String current;

    public static class InvalidParserException extends Exception{}
    public static class NotAValidBlueprintException extends Exception{}

    public Blueprint parse(String toParse) throws NotAValidBlueprintException{
            current = toParse;
            stripFrom("make");
            String processorName = getString();
            stripFrom("from");
            String templateStr = getString();
            Tree<String> parseTree =  parseTemplate(templateStr);
	    return new Blueprint(processorName, parseTree);
    }

    private void stripFrom(String toStrip) throws NotAValidBlueprintException{
        for(int i = 0; i<toStrip.length(); i++){
            if(toStrip.toUpperCase().charAt(i) != current.toUpperCase().charAt(i)) throw new NotAValidBlueprintException();
        }
        current = current.substring(toStrip.length()).trim();
    }

    private String getString() throws NotAValidBlueprintException{
        String temp = "";
        if(current.charAt(0) != '"') throw new NotAValidBlueprintException();
        int i = 1;
        while(current.charAt(i) != '"'){
            temp += current.charAt(i);
            i++;
        }
        current = current.substring(i+1).trim();
        return temp;
    }

    private Tree<String> parseTemplate(String templateStr) throws NotAValidBlueprintException{
        Tree<String> root = parseProcessor(templateStr);
        return root;
    }

    private Tree<String> parseProcessor(String templateStr) throws NotAValidBlueprintException{
        Tree<String> processorTree = new Tree<String>("<processor>");
        while(templateStr.length() > 0){
            templateStr = templateStr.trim();
            try{
                templateStr = parseKeyword(templateStr, processorTree);
                continue;
            } catch(InvalidParserException e){}

            try{
                templateStr = parseInput(templateStr, processorTree);
                continue;
            } catch(InvalidParserException e){}

            try{
                templateStr = parseParam(templateStr, processorTree);
                continue;
            } catch(InvalidParserException e){}

            try{
                templateStr = parseOptional(templateStr, processorTree);
                continue;
            } catch(InvalidParserException e){}

	    throw new NotAValidBlueprintException();
        }
        return processorTree;
    }

    private String parseOptional(String templateStr, Tree<String> parent) throws InvalidParserException{
        Tree<String> optionalTree = new Tree<String>("<optional>");
        if(templateStr.charAt(0) != '[') throw new InvalidParserException();
        String optional = "";
        int i = 1;
        while(templateStr.charAt(i) != ']'){
            optional += templateStr.charAt(i);
            i++;
        }
        
        List<String> options = new ArrayList<String>();
        int j = 0;
        while(optional.length() > 0 && j < optional.length()){
            if(optional.charAt(j) == '|'){
                options.add(optional.substring(0, j));
                optional = optional.substring(j+1);
                j = 0;
            }
            else{
                j++;
            }
        }
        options.add(optional);
        for(String s : options){
            parseOption(s, optionalTree);
        }
        
        parent.addChild(optionalTree);
        return templateStr.substring(i+1);
        
    }

    private void parseOption(String templateStr, Tree<String> parent) throws InvalidParserException{
        Tree<String> optionTree = new Tree<String>("<option>");
        while(templateStr.length() > 0){
            templateStr = templateStr.trim();
            try{
                templateStr = parseKeyword(templateStr, optionTree);
                continue;
            } catch(InvalidParserException e) {}

            try{
                templateStr = parseParam(templateStr, optionTree);
                continue;
            } catch(InvalidParserException e){}
            if(templateStr.length() > 0){
                throw new InvalidParserException();
            }
        }
        parent.addChild(optionTree);
    }

    private String parseInput(String templateStr, Tree<String> parent) throws InvalidParserException{
        String inputStr = "<input>";
        for(int i = 0; i < inputStr.length(); i++){
            if(inputStr.charAt(i) != templateStr.charAt(i)) throw new InvalidParserException();
        }
        parent.addChild(inputStr);
        return templateStr.substring(inputStr.length());
    }

    private String parseKeyword(String templateStr, Tree<String> tree) throws InvalidParserException{
        Tree<String> keywordTree = new Tree<String>("<keyword>");
        String temp = "";
        int i = 0;
        while(i < templateStr.length() && Character.isLetter(templateStr.charAt(i)) ){
            temp += templateStr.charAt(i);
            i++;
        }
        if(i == 0) throw new InvalidParserException();
        keywordTree.addChild(temp);
        tree.addChild(keywordTree);
        return templateStr.substring(i);
    }

    private String parseParam(String templateStr, Tree<String> parent) throws InvalidParserException{
        Tree<String> paramTree = new Tree<String>("<param>");

        if(templateStr.charAt(0) != '<') throw new InvalidParserException();

        String key = "";
        int i = 1;
        while(templateStr.charAt(i) != ':'){
            // TODO: manage errors
            key += templateStr.charAt(i);
            i++;
        }
        String restriction = "<";
        i++;
        while(templateStr.charAt(i) != '>'){
            // TODO: manage errors
            restriction += templateStr.charAt(i);
            i++;
        }
        restriction += ">";
        paramTree.addChild(key);
        paramTree.addChild(restriction);
        parent.addChild(paramTree);
        return templateStr.substring(i+1); 
    }
   
    //private String parseOptional

    public static void main(String[] args) throws NotAValidBlueprintException{
        BlueprintParser bp = new BlueprintParser();
        System.out.println(bp.parse("MAKE \"PrintProcessor\" FROM \"PRINT [AT <delay:number> PER SECOND | ON KEY <key:string>] [<quantity:number> OF] <input>\""));
        System.out.println(bp.parse("MAKE \"PrintProcessor\" FROM \"PRINT <quantity:number> OF <input>\""));
        System.out.println(bp.parse("MAKE \"MultiplyProcessor\" FROM \"MULTIPLY <input> BY <input>\""));
    }


}
