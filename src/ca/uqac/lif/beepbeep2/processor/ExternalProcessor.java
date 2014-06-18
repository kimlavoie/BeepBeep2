package ca.uqac.lif.beepbeep2.processor;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import ca.uqac.lif.beepbeep2.processor.Processor;

public class ExternalProcessor extends Processor{

    ProcessBuilder pb;
    Process p; 
    BufferedWriter writer;
    BufferedReader reader;
    String directory = "external/";
    String program;

  public ExternalProcessor(String program) throws Exception{
    this.program = program;
    specificInit();
    p = pb.redirectErrorStream(true).start(); 
    writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
  }

  public void specificInit(){
    String ext = program.split("\\.")[1];
    if(ext.equals("py")) pythonInit();
    else if(ext.equals("rb")) rubyInit();
    else if(ext.equals("pl")) perlInit();
    else otherInit();
  }

  public void pythonInit(){
    pb = new ProcessBuilder("python", directory + program);
    Map<String, String> env = pb.environment();
    env.put("PYTHONUNBUFFERED", "true");         //To force python scripts to not buffer IO
  }


  public void rubyInit(){
    String tmp = System.getProperty("java.io.tmpdir") + "/" + program;
    try{
      String fileContent = new String(Files.readAllBytes(Paths.get(directory + program)));
      fileContent = "$stdout.sync = true\n" + fileContent;
      PrintWriter pw = new PrintWriter(tmp);
      pw.print(fileContent);
      pw.close();
      pb = new ProcessBuilder("ruby",  tmp);
    }catch(Exception e){
      e.printStackTrace();
    }
    
  }
  
  public void perlInit(){
    String tmp = System.getProperty("java.io.tmpdir") + "/" + program;
    try{
      String fileContent = new String(Files.readAllBytes(Paths.get(directory + program)));
      fileContent = "$| = 1;\n" + fileContent;
      PrintWriter pw = new PrintWriter(tmp);
      pw.print(fileContent);
      pw.close();
      pb = new ProcessBuilder("perl",  tmp);
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void otherInit(){
    pb = new ProcessBuilder(directory + program);
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
      System.out.print("ExternalProcessor response for " + program + ":\n" + response);
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
