package ca.uqac.lif.beepbeep2.processor;

import ca.uqac.lif.beepbeep2.processor.Processor;
import java.util.Map;
import java.util.List;

public class DummyProcessor extends Processor{
  public void run(){
    System.out.println("running DummyProcessor");
    System.out.println("With option: " + options.get("test"));
  }
}
