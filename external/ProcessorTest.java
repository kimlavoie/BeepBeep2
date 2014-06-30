package ca.uqac.lif.beepbeep2.processor;
     
import ca.uqac.lif.beepbeep2.processor.Processor;
     
public class ProcessorTest extends Processor
{
  public ProcessorTest()
  {
    System.out.println("Test reussi!");
  }
   
  public void run(String ... events)
  {
    System.out.println("dans ProcessorTest.run()");
  }
}
