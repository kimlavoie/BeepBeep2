package com.beepbeep.processor;

import java.util.Queue;
import java.util.LinkedList;

public class Pipe{
  private Queue<String> fifo = new LinkedList<String>();
  final int MAX_BUFFERING_SIZE = 1000;
  final int SLEEPING_TIME = 10;

  public boolean canRead(){
    if(fifo.peek() == null) return false;
    else return true;
  }

  public boolean canWrite(){
    if(fifo.size() > MAX_BUFFERING_SIZE) return false;
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
    fifo.offer(event);
  }
}
