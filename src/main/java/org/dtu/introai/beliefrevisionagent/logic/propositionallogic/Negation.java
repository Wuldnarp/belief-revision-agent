package org.dtu.introai.beliefrevisionagent.logic.propositionallogic;

public class Negation extends Formula{

    Formula operand;

    public Negation(Formula operand){
        this.operand = operand;
    }

    public Formula getOperand() {
        return operand;
    }

    @Override
    public String toString(){
        return "NOT " + operand.toString();
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
