package ca.uqac.lif.beepbeep2.processor;

import ca.uqac.lif.beepbeep2.processor.Processor;
import java.util.List;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.net.URLClassLoader;
import java.net.URL;
import java.lang.reflect.Method;
import java.io.File;

public class ProcessorFactory{
  ClassLoader classLoader; 


  public ProcessorFactory(){
    try{
      URL[] urls = createURLs();
      createClassLoader(urls);
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public URL[] createURLs(){
    ArrayList<URL> urls = new ArrayList<URL>();
    try{
      File dir = new File("external");
      for(File f : dir.listFiles()){
        String[] ext = f.getName().split("\\.");
        if(ext.length > 1 && ext[1].equals("jar")){
          urls.add(new URL("jar:file:external/" + f.getName() + "!/"));
        }
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return urls.toArray(new URL[urls.size()]);
  }

  public void createClassLoader(URL[] urls){
    try { 
      classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
    } catch (Throwable t) { 
      t.printStackTrace(); 
    }
  }

  public Processor getProcessor(String className){
    /**
      Generate a processor from the classname by calling the constructor without parameters
    */
    try{
      Class cl = classLoader.loadClass("ca.uqac.lif.beepbeep2.processor." + className);
      return (Processor) cl.newInstance();
    }catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }

  public Processor getProcessor(String className, String... params){
    /**
      Generate a processor from the classname, calling the constructor with the same amounts of String parameters passed
    */
    try{
      Object[] parameters = params;
      Class cl = classLoader.loadClass("ca.uqac.lif.beepbeep2.processor." + className);
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

  // For test purpose
  public static void main(String[] args){
    ProcessorFactory processorFactory = new ProcessorFactory();
    Processor processor = processorFactory.getProcessor("DummyProcessor");
    Processor processor2 = processorFactory.getProcessor("ExternalProcessor", "test.py");
    Processor processor3 = processorFactory.getProcessor("ExternalProcessor", "test.rb");
    Processor processor4 = processorFactory.getProcessor("ExternalProcessor", "test.pl");
    Processor processor5 = processorFactory.getProcessor("ProcessorTest");
    processor.run();
    processor2.run("event: {x: 0}");
    processor3.run("event: {x: 0}");
    processor4.run("event: {x: 8}");
    processor5.run();
  }
}
