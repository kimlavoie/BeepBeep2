package ca.uqac.lif.beepbeep2.yaml;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class YAMLWrapper{
  private Object root;
  final private String pathToken= new String("/");
  final private String rangeToken = new String("..");
  final private String dictListToken = new String(",");
  final private String internToken = new String("!");

  public static void main(String[] args) throws Exception{
    String str = "{a: 1, b: {c: {e: [1,2,3]}, d: [4, 5, 6, 7, 8], f: {e: [7,6,5,4]}}, t: [{a: 2, b: 3}, {a: 3, b: 4}]}";
    YAMLWrapper wrapper = new YAMLWrapper(str);
    int s = (int) wrapper.get("/b/c//e//0/");
    System.out.println(s);
    List l = (List) wrapper.get("b/d");
    System.out.println(l);
    Map m = (Map) wrapper.get("b");
    System.out.println(m);
    int i = (int) wrapper.get("b/d/0");
    System.out.println(i);
    List r = (List) wrapper.get("b/d/0..2");
    System.out.println(r);
    List r2 = (List) wrapper.get("b/d/..4");
    System.out.println(r2);
    List r3 = (List) wrapper.get("b/d/2..");
    System.out.println(r3);
    List r4 = (List) wrapper.get("b/d/2../..2");
    System.out.println(r4);
    List dictList = (List) wrapper.get("b/c,f/e");
    System.out.println(dictList);
    List dictList2 = (List) wrapper.get("b/c,f/e/!0");
    System.out.println(dictList2);
    List dictList3 = (List) wrapper.get("b/c,f/e/!0..2/!1");
    System.out.println(dictList3);
    List dictList4 = (List) wrapper.get("t/a,b/!0");
    System.out.println(dictList4);
    System.out.println(wrapper.dump());
  }

  public YAMLWrapper(){}

  public YAMLWrapper(String str){
    load(str);
  }

  public void load(String s){
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
    Yaml yaml = new Yaml(options);
    root = yaml.load(s); 
  }

  public String dump(){
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
    Yaml yaml = new Yaml(options);
    return yaml.dump(root); 
  }
 
  private List<String> getPathTokens(String path){
    String[] paths = path.split(pathToken);
    ArrayList<String> nodes = new ArrayList<String>();
    for(String s : paths){
      if(!s.equals("")){
        nodes.add(s);
      }
    }
    return nodes;
  } 

  private Object processMap(String currentNode, Map map) throws Exception{
    boolean isDictList = currentNode.contains(dictListToken);
    boolean isInt = isInteger(currentNode);
    if(!isInt && !isDictList){
      return map.get(currentNode); 
    }
    else if(isDictList){
      String[] list = currentNode.split(Pattern.quote(dictListToken));
      ArrayList<Object> arr = new ArrayList<Object>();
      for(String s : list){
        arr.add(map.get(s));
      }
      return arr;
    }
    else throw new Exception("Map process error");
  }

  private Object processListRange(String currentNode, List list){
          String[] indices = currentNode.split(Pattern.quote(rangeToken));
          int start = 0;
          int end = list.size();
          if(!indices[0].equals("")){
            start = Integer.parseInt(indices[0]);
          }
          if(indices.length == 2){
            end = Integer.parseInt(indices[1]);
          }
          return list.subList(start,end);
  }

  private Object processList(String currentNode, List list) throws Exception{
    /**
      This method process a List node, modifying it's behaviour in function of currentNode's type.
    */
    boolean isIntern = currentNode.contains(internToken);
    currentNode = currentNode.replace("!", "");
    boolean isRange = currentNode.contains(rangeToken);
    boolean isInt = isInteger(currentNode);
    if(!isInt && !isRange){  //If it's not an int or a range, we fetch map data to each list's element
      ArrayList<Object> arr = new ArrayList<Object>();
      for(Object ob : list){
        Map m = (Map) ob;
        arr.add(process(currentNode, ob));
      }
      return arr;
    }
    else if(isInt){
      if(isIntern){
        ArrayList<Object> inList = new ArrayList<Object>();
        for(Object ob : list){
          List l = (List) ob;
          inList.add(l.get(Integer.parseInt(currentNode)));
        }
        return inList;
      }
      else{
        return list.get(Integer.parseInt(currentNode));
      }
    }
    else if(isRange){
      if(isIntern){
        ArrayList<Object> arr = new ArrayList<Object>();
        for(Object ob : list){
          List l = (List) ob;
          arr.add(processListRange(currentNode, list));
        }
        return arr;
      }
      else{
        return processListRange(currentNode, list);
      }
    }
    else throw new Exception("List process error");
  }

  private Object process(String currentNode, Object currentObj) throws Exception{
    /** 
      This function redirect the currentObj to his collection type,
      throwing an exception if it's not a supported collection.
    */
    if(currentObj instanceof Map){
      return processMap(currentNode, (Map) currentObj);
    }
    else if(currentObj instanceof List){
      return processList(currentNode, (List) currentObj);
    }
    else throw new Exception("Could not parse path: leaf node reached before end of path");
  }

  public Object get(String path) throws Exception{
    List<String> nodes = getPathTokens(path);
    Object currentObj = root;
    for(String currentNode : nodes){
      currentObj = process(currentNode, currentObj);
    }
    return currentObj;  
  }


  public boolean isInteger(String s) {
    try { 
        Integer.parseInt(s); 
    } catch(NumberFormatException e) { 
        return false; 
    }
    return true;
  }
}
