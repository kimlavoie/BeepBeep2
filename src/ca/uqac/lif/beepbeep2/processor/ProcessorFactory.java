package ca.uqac.lif.beepbeep2.processor;

import ca.uqac.lif.beepbeep2.processor.Processor;
import java.util.List;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
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
      Generate a processor from the classname, calling the constructor with the same amounts of String parameters passed
    */
    try{
      Class cl = classLoader.loadClass(className);
      Constructor cons = cl.getConstructors()[0];
      if(cons != null){
        return (Processor) cons.newInstance();
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
   /* Processor processor = processorFactory.getProcessor("ca.uqac.lif.beepbeep2.processor.DummyProcessor");
    processor.addOption("test", "success");
    Processor processor2 = processorFactory.getProcessor("ca.uqac.lif.beepbeep2.processor.ExternalProcessor");
    processor2.addOption("program", "test.py");

    processor.start();
    processor2.start();
    try{
      Thread.sleep(2000);
    }catch(Exception e){}

    Processor textProc = processorFactory.getProcessor("ca.uqac.lif.beepbeep2.processor.TextProcessor");
    textProc.addOption("value", "hello");
    Processor printProc = processorFactory.getProcessor("ca.uqac.lif.beepbeep2.processor.PrintProcessor");
    Pipe pipe = new Pipe();
    printProc.addInput(pipe);
    textProc.addOutput(pipe);

    textProc.start();
    printProc.start();
    try{
      Thread.sleep(2000);
    }catch(Exception e){}
/*	  
	  Pipe passPrint= new Pipe();
	  Pipe textPass = new Pipe();
	  
	  PassThroughProcessor passProc = new PassThroughProcessor(textPass, passPrint);
	  PrintProcessor printProc = new PrintProcessor(passPrint, null);
	  TextProcessor textProc = new TextProcessor(null, textPass);
	  textProc.start();
	  passProc.start();
	  printProc.start();
	  
	  /*
	  //Pipe plusPrint = new Pipe();
	  //Pipe text1Plus = new Pipe();
	  //Pipe text2Plus = new Pipe();
	  //Pipe text3Plus = new Pipe();
	  //List<Pipe> plusInputs = new ArrayList<Pipe>();
	  plusInputs.add(text1Plus);
	  plusInputs.add(text2Plus);
	  //plusInputs.add(text3Plus);
	  List<Pipe> plusOutputs = new ArrayList<Pipe>();
	  plusOutputs.add(plusPrint);
	  
	  TextProcessor text1Proc = new TextProcessor(null, text1Plus);
	  TextProcessor text2Proc = new TextProcessor(null, text2Plus);
	  //TextProcessor text3Proc = new TextProcessor(null, text3Plus);
	  DivisionProcessor plusProc = new DivisionProcessor(plusInputs, plusOutputs);
	  PrintProcessor printProc = new PrintProcessor(plusPrint,null);
	  
	  text1Proc.start();
	  text2Proc.start();
	  //text3Proc.start();
	  plusProc.start();
	  printProc.start();
	  */
	  
	  Pipe selectPrint = new Pipe();
	  MySQLSelectProcessor selectProc = new MySQLSelectProcessor();
	  PrintProcessor printProc = new PrintProcessor();
	  selectProc.addOutput(selectPrint);
	  printProc.addInput(selectPrint);
	  selectProc.start();
	  printProc.start();
  
  }
}
