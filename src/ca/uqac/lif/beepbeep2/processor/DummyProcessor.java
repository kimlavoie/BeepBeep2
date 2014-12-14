package ca.uqac.lif.beepbeep2.processor;

import ca.uqac.lif.beepbeep2.processor.Processor;
import java.util.Map;
import java.util.List;

public class DummyProcessor extends Processor{

  public DummyProcessor(Map<String,String> options, List<Processor> inputs){
	  System.out.println("Received options and inputs");
	  System.out.println(options);
	  System.out.println(inputs);
  }

  public void run(){
    System.out.println("running DummyProcessor");
  }
}
