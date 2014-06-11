import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Map;

public class ExternalProcessor{

    Process p; 
    BufferedWriter writer;
    BufferedReader reader;
    String directory = "./";

  public ExternalProcessor(String program) throws Exception{
    ProcessBuilder pb = new ProcessBuilder(directory + program);
    Map<String, String> env = pb.environment();
    env.put("PYTHONUNBUFFERED", "true");
    p = pb.redirectErrorStream(true).start(); 
    writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
  }
  
  public void run(String event) throws Exception{
    writer.write(event + "\n");
    writer.flush();
    String line;
    while((line = reader.readLine()).equals("")){System.out.println("empty line");}
    System.out.println(line);
  }

  public void cleanup(){
    p.destroy();
  }

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
}
