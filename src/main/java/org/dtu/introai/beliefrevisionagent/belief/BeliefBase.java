package org.dtu.introai.beliefrevisionagent.belief;

import org.dtu.introai.beliefrevisionagent.api.CNF;
import org.dtu.introai.beliefrevisionagent.logic.CNFConverter;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Negation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BeliefBase {

    private List<BeliefEntry> entries;

    public BeliefBase(){
        this.entries = new ArrayList<>();
    }

    public List<BeliefEntry> getEntries(){
        return entries;
    }

    public void setEntries(List<BeliefEntry> entries){
        this.entries = entries;
    }

    public void expand(Formula formula, int priority){
        entries.add(new BeliefEntry(formula, priority));
    }

    public BeliefBase contract(Formula formula){
        return Contraction.contract(this, formula);
    }

    // Revise the formular with using the Levi Identity.
    public BeliefBase revise(Formula formula, int priority){
        Formula negation = new Negation(formula);
        BeliefBase contracted = this.contract(negation);
        contracted.expand(formula, priority);
        return contracted;
    }

    // Converts into CNF using De Morgan's law 
    public CNF toCNF() {
        CNF result = new CNF(Set.of());
        for (BeliefEntry entry : entries) {
            CNF entryCNF = CNFConverter.convert(entry.getFormula());
            result = result.merge(entryCNF);
        }
        return result;
    }

    @Override
    public String toString(){
        return "BeliefBase: " + entries.toString();
    }
}
