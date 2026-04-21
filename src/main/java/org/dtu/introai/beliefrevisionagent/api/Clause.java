package org.dtu.introai.beliefrevisionagent.api;

import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;

import java.util.HashSet;
import java.util.Set;

public record Clause(Set<Formula> literals) {

    public Clause {
        literals = new HashSet<>(literals);
    }

    public boolean isEmpty() {
        return literals.isEmpty();
    }

    public boolean contains(Formula f) {
        return literals.contains(f);
    }

    /**
     * Remove one element from the clause and return it
     * @param f the element to remove
     * @return the clause without it
     */
    public Clause without(Formula f) {
        Set<Formula> newLiterals = new HashSet<>(literals);
        newLiterals.remove(f);
        return new Clause(newLiterals);
    }

    /**
     * Combine/Merge two clauses together.
     * @param other the other Clause
     * @return The merged clause
     */
    public Clause merge(Clause other) {
        Set<Formula> merged = new HashSet<>(literals);
        merged.addAll(other.literals);
        return new Clause(merged);
    }

    @Override
    public String toString() {
        return literals.toString();
    }
}
