package org.dtu.introai.beliefrevisionagent.logic.propositionallogic;

public class Conjunction extends Formula{

    Formula left;
    Formula right;

    public Conjunction(Formula left, Formula right){
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
        return "(" + left.toString() + " AND " + right.toString() + ")";
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
