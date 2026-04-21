package org.dtu.introai.beliefrevisionagent.api;

import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;

import java.util.HashSet;
import java.util.Set;

public record CNF(Set<Clause> cnf) {

    public boolean isEmpty(){
        return cnf.isEmpty();
    }

    /**
     * Combine/Merge two CNF together.
     * @param other the other CNF
     * @return The merged CNF
     */
    public CNF merge(CNF other){
        Set<Clause> merged = new HashSet<>(cnf);
        merged.addAll(other.cnf);
        return new CNF(merged);
    }
    @Override
    public String toString(){
        return cnf.toString();
    }
}
