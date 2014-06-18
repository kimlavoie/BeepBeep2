package ca.uqac.lif.beepbeep2.processor;

import ca.uqac.lif.beepbeep2.processor.Processor;

public class DummyProcessor extends Processor{
  public void run(String... events){
    System.out.println("running DummyProcessor");
  }
}
