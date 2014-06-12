package com.beepbeep.processor;
import com.beepbeep.processor.Processor;

public class DummyProcessor extends Processor{
  public void run(String... events){
    System.out.println("running DummyProcessor");
  }
}
