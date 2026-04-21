package org.dtu.introai.beliefrevisionagent.logic.propositionallogic;

public class Atom extends Formula{

    String name;

    public Atom(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        // TODO
        return false;
    }

    @Override
    public int hashCode(){
        // TODO
        return 0;
    }
}
