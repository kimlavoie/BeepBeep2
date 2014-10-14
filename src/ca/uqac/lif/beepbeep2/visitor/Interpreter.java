package ca.uqac.lif.beepbeep2.visitor;

import java.io.File;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.ParseException;
import ca.uqac.lif.bullwinkle.ParseNode;
import ca.uqac.lif.bullwinkle.output.GraphvizVisitor;


public class Interpreter {

	public static void main(String[] args) {
		String filename = "data/grammar.bnf";
		String expression = "PRINT 2 OF MULTIPLY MULTIPLY 1 BY 2 BY MULTIPLY 3 BY 4 .";//"PRINT 2 OF YAML INPUT FILE \"filename.txt\" .";//"DEFINE (MULBY) AS A (BINARY_PROC), PARSE AS (X:TRACE) MULTIPLY (Y:TRACE), REWRITE AS (X) * (Y).";//"OUTPUT TO FILE \"filename.txt\" ALL OF INPUT FILE \"filename.txt\""; 
		try
	    {
	      BnfParser parser = new BnfParser(new File(filename));
	      ParseNode node = parser.parse(expression);
	      
	      VisitorTest visitorTest = new VisitorTest();
	      visitorTest.fillMap();
	      VisitorESQL visitorESQL = new VisitorESQL();
	      visitorESQL.fillMap();
	      node.prefixAccept(visitorTest);
	      //node.prefixAccept(visitorESQL);

	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }

	}

}
