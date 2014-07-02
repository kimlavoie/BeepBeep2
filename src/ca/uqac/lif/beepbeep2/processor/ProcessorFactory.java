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
  final String externalDir = "./external";

  private class ExternalDirectoryNotFoundException extends Exception{}

  public ProcessorFactory(){
    try{
      URL[] urls = createURLs();
      createClassLoader(urls);
    }catch(ExternalDirectoryNotFoundException e){
      System.err.println("\"" + externalDir + "\" directory not found.");
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public URL[] createURLs() throws ExternalDirectoryNotFoundException, Exception{
    File dir = new File(externalDir);
    if(!dir.isDirectory() || !dir.exists()) throw new ExternalDirectoryNotFoundException();
    ArrayList<URL> urls = new ArrayList<URL>();
    for(File f : dir.listFiles()){
      String[] ext = f.getName().split("\\.");
      if(ext.length > 1 && ext[1].equals("jar")){
        urls.add(new URL("jar:file:external/" + f.getName() + "!/"));
      }
    }
    return urls.toArray(new URL[urls.size()]);
  }

  public void createClassLoader(URL[] urls) throws Exception{
      classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
  }

  public Processor getProcessor(String className){
    /**
      Generate a processor from the classname by calling the constructor without parameters
    */
    try{
      Class cl = classLoader.loadClass(className);
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
      Class cl = classLoader.loadClass(className);
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
    Processor processor = processorFactory.getProcessor("ca.uqac.lif.beepbeep2.processor.DummyProcessor");
    Processor processor2 = processorFactory.getProcessor("ca.uqac.lif.beepbeep2.processor.ExternalProcessor", "test.py");
    Processor processor3 = processorFactory.getProcessor("ca.uqac.lif.beepbeep2.processor.ExternalProcessor", "test.rb");
    Processor processor4 = processorFactory.getProcessor("ca.uqac.lif.beepbeep2.processor.ExternalProcessor", "test.pl");
    Processor processor5 = processorFactory.getProcessor("org.test.processor.ProcessorTest");
    processor.run();
    processor2.run("event: {x: 0}");
    processor3.run("event: {x: 0}");
    processor4.run("event: {x: 8}");
    processor5.run();
  }
}
