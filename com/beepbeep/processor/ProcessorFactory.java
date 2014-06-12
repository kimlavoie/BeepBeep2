package com.beepbeep.processor;

import com.beepbeep.processor.Processor;
import java.util.List;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ProcessorFactory{
  ClassLoader classLoader = ProcessorFactory.class.getClassLoader();

  public Processor getProcessor(String className){
    try{
      Class cl = classLoader.loadClass("com.beepbeep.processor." + className);
      return (Processor) cl.newInstance();
    }catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }

  public Processor getProcessor(String className, String... params){
    try{
      Object[] parameters = params;
      Class cl = classLoader.loadClass("com.beepbeep.processor." + className);
      Constructor cons = null;
      for(Constructor constructor: cl.getConstructors()){
        if(constructor.getParameterTypes().length == parameters.length){
          cons = constructor; 
        }
      }
      if(cons != null){
        return (Processor) cons.newInstance(parameters);
      }else{
        throw new Exception("Invalid constructor");
      }
    }catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }

  public static void main(String[] args){
    ProcessorFactory processorFactory = new ProcessorFactory();
    Processor processor = processorFactory.getProcessor("DummyProcessor");
    Processor processor2 = processorFactory.getProcessor("ExternalProcessor", "test.py");
    Processor processor3 = processorFactory.getProcessor("ExternalProcessor", "test.rb");
    processor.run("dummy");
    processor2.run("event: {x: 0}");
    processor3.run("event: {x: 0}");
  }
}
