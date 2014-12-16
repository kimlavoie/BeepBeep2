package ca.uqac.lif.beepbeep2.processor;

import java.util.List;
import java.util.ArrayList;

public abstract class Processor implements Runnable{  

  protected Thread t;
	
  protected List<Pipe> inputStreams;
  protected List<Pipe> outputStreams;

  public Processor(){
    inputStreams = new ArrayList<Pipe>();
    outputStreams = new ArrayList<Pipe>();
  }

  public Processor(List<Pipe> inputs, List<Pipe> outputs){
    inputStreams = inputs;
    outputStreams = outputs;
  }

  public Processor(Pipe input, Pipe output){
    inputStreams = new ArrayList<Pipe>();
    outputStreams = new ArrayList<Pipe>();
    inputStreams.add(input);
    outputStreams.add(output);
  }
  
  public void start() {
	  if (t==null) {
		  t = new Thread(this, "Proc");
		  t.start();
	  }
  }

  public void write(String event){
    for(Pipe pipe: outputStreams){
      pipe.write(event);
    }
  }

  public String read(){
    Pipe pipe = inputStreams.get(0);
    return pipe.read();
  }

  public String[] readAll(){
    List<String> events = new ArrayList<String>();
    for(Pipe pipe: inputStreams){
      events.add(pipe.read());
    }
    return events.toArray(new String[events.size()]);
  }

  public List<Pipe> getOutputs(){
    return outputStreams;
  }

  public void cleanup(){}

  public abstract void run();
}
