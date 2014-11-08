package ca.uqac.lif.beepbeep2.processor;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public abstract class Processor implements Runnable{  

  protected Thread t;
	
  protected List<Pipe> inputStreams;
  protected List<Pipe> outputStreams;
  protected Map<String, String> options;

  public Processor(){
    inputStreams = new ArrayList<Pipe>();
    outputStreams = new ArrayList<Pipe>();
    options = new HashMap<String, String>();
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

  public void setInputs(List<Pipe> inputs){
    this.inputStreams = inputs;
  }

  public void setOutputs(List<Pipe> outputs){
    this.outputStreams = outputs;
  }

  public void setOptions(Map<String, String> options){
    this.options = options;
  }

  public void addInput(Pipe input){
    inputStreams.add(input);
  }

  public void addOutput(Pipe output){
    outputStreams.add(output);
  }

  public void addOption(String key, String value){
    options.put(key, value);
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

  public void cleanup(){}

  public abstract void run();
}
