package org.dtu.introai.beliefrevisionagent.belief;

import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;

public class BeliefEntry {

    private Formula formula;
    private int priority;

    public BeliefEntry(Formula formula, int priority){
        this.formula = formula;
        this.priority = priority;
    }

    public Formula getFormula(){
        // TODO
        return null;
    }

    public int getPriority(){
        // TODO
        return 0;
    }

    @Override
    public String toString(){
        // TODO
        return "";
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof BeliefEntry)) return false;
        return this.formula.equals(((BeliefEntry) o).getFormula());
    }

    @Override
    public int hashCode() {
        return formula.hashCode();
    }
}
