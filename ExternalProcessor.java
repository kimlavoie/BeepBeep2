import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Map;

public class ExternalProcessor{

    Process p; 
    BufferedWriter writer;
    BufferedReader reader;

  public ExternalProcessor() throws Exception{
    ProcessBuilder pb = new ProcessBuilder("./test.py");
    Map<String, String> env = pb.environment();
    env.put("PYTHONUNBUFFERED", "true");
    p = pb.redirectErrorStream(true).start(); 
    writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

    run("lala");
    run("lolo");
  }
  
  public void run(String event) throws Exception{
    writer.write(event + "\n");
    writer.flush();
    String line;
    line = reader.readLine();
    System.out.println(line);
  }

  public void cleanup(){
    p.destroy();
  }

  public static void main(String args[]) throws Exception{
    ExternalProcessor p = new ExternalProcessor();
    p.cleanup();
  }
}
