package com.beepbeep.processor;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Map;
import com.beepbeep.processor.Processor;

public class ExternalProcessor extends Processor{

    Process p; 
    BufferedWriter writer;
    BufferedReader reader;
    String directory = "external/";

  public ExternalProcessor(String program) throws Exception{
    ProcessBuilder pb = new ProcessBuilder(directory + program);
    Map<String, String> env = pb.environment();
    env.put("PYTHONUNBUFFERED", "true");         //To force python scripts to not buffer IO
    p = pb.redirectErrorStream(true).start(); 
    writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
  }
  
  public void run(String... events){
    /**
      Overriden from Processor
      Send event to program stdin, then listen for response on stdout
    */
    String event = events[0];
    try{
      writer.write(event + "\n");
      writer.flush();
      String response = readBuffer();
      System.out.print("ExternalProcessor response:\n" + response);
    }catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
  }

  private String readBuffer() throws Exception{
    /**
      Called within run routine to read everything sent to the stdout by the external program
    */
      String response = new String();
      String line;
      line = reader.readLine();
      response += line + "\n";
      while(reader.ready()){
        line = reader.readLine();
        response += line + "\n";
      }
      return response;
    
  }

  public void cleanup(){
    p.destroy();
  }

// For testing purposes
/*
  public static void main(String args[]) throws Exception{
    System.out.println("PYTHON");
    System.out.println("------");
    ExternalProcessor p = new ExternalProcessor("test.py");
    p.run("event: {x: 0}");
    p.run("event: {x: 34}");
    p.cleanup();

    System.out.println("");
    System.out.println("RUBY");
    System.out.println("----");
    ExternalProcessor p2 = new ExternalProcessor("test.rb");
    p2.run("event: {x: 0}");
    p2.run("event: {x: 10}");
    p2.cleanup();
  }
*/
}
