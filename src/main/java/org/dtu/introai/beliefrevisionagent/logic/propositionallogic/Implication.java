package org.dtu.introai.beliefrevisionagent.logic.propositionallogic;

public class Implication extends Formula{

    Formula left;
    Formula right;

    public Implication(Formula left, Formula right){
        this.left = left;
        this.right = right;
    }

    public Formula getLeft() {
        return left;
    }

    public Formula getRight() {
        return right;
    }

    @Override
    public String toString(){
        return "(" + left.toString() + " IMPLIES " + right.toString() + ")";
    }
}
