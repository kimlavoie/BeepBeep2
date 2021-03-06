package ca.uqac.lif.beepbeep2.processor;

import java.util.concurrent.ArrayBlockingQueue;

public class Pipe{
  private ArrayBlockingQueue<String> fifo;
  final int MAX_BUFFERING_SIZE = 1000;
  final int SLEEPING_TIME = 10;

  Pipe() {
	  fifo = new ArrayBlockingQueue<String>(MAX_BUFFERING_SIZE);
  }
  public boolean canRead(){
	  //System.out.println(fifo.size());
    if(fifo.isEmpty()) return false;
    else return true;
  }

  public boolean canWrite(){
    if(fifo.remainingCapacity() == 0) return false;
    else return true;
  }

  public String read(){
    try{
      while(!canRead()){
        Thread.sleep(SLEEPING_TIME);
      }
    }catch(Exception e){ 
      e.printStackTrace();
    }
    return fifo.remove();
  }

  public void write(String event){
    try{
    while(!canWrite()){
        Thread.sleep(SLEEPING_TIME);
      }
    }catch(Exception e){ 
      e.printStackTrace();
    }
    fifo.add(event);
    //System.out.println(fifo.size());
  }
}
