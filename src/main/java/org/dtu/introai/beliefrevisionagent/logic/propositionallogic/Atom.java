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
}
