package org.dtu.introai.beliefrevisionagent.belief;

import org.dtu.introai.beliefrevisionagent.api.CNF;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;

import java.util.List;

public class BeliefBase {

    private List<BeliefEntry> entries;

    public BeliefBase(){

    }
    public List<BeliefEntry> getEntries(){
        // TODO
        return List.of();
    }
    public void setEntries(List<BeliefEntry> entries){
        // TODO
    }
    public void expand(Formula formula, int priority){
        // TODO
    }
    public BeliefBase contract(Formula formula){
        // TODO
        return null;
    }
    public BeliefBase revise(Formula formula, int priority){
        // TODO
        return null;
    }
    public CNF toCNF(){
        // TODO
        return null;
    }
    @Override
    public String toString(){
        // TODO
        return "";
    }
}
