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
        if(!(o instanceof Atom)) return false;
        return toString().equals(((Atom) o).name);
    }

    @Override
    public int hashCode(){
        return toString().hashCode();
    }
}
