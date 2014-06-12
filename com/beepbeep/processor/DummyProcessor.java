package com.beepbeep.processor;
import com.beepbeep.processor.Processor;

public class DummyProcessor implements Processor{
  public void run(String event){
    System.out.println("running DummyProcessor");
  }
}
