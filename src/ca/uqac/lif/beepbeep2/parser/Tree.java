package ca.uqac.lif.beepbeep2.parser;

import java.util.List;
import java.util.ArrayList;

public class Tree<T>{
    private T value;
    private List<Tree<T>> children;

    public Tree(T value){
        this.value = value;
        this.children = new ArrayList<Tree<T>>();
    }

    public Tree<T> addChild(T childValue){
        Tree<T> t = new Tree<T>(childValue);
        children.add(t);
        return t;
    } 

    public Tree<T> addChild(Tree<T> child){
        children.add(child);
        return child;
    } 

    public void removeChild(int pos){
        children.remove(pos);
    }

    public Tree<T> getChild(int pos){
        return children.get(pos);
    }

    public List<Tree<T>> getChildren(){
        return children;
    }

    public String toString(){
        String temp = " " + this.value + " [";
        for(Tree<T> t : children){
            temp += t;
        }
        return temp + "]";
    }

    public T getValue(){return value;}
    public void setValue(T value){this.value = value;}

    public static void main(String[] args){
        Tree<String> tree = new Tree<String>("Test1");
        tree.addChild("Test2"); 
        System.out.println(tree.getChild(0).getValue());
    }
}
