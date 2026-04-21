package org.dtu.introai.beliefrevisionagent.logic.propositionallogic;

public class Biconditional extends Formula {

    Formula left;
    Formula right;

    public Biconditional(Formula left, Formula right){
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
        return "(" + left.toString() + " IFF " + right.toString() + ")";
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
