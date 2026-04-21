package org.dtu.introai.beliefrevisionagent.belief;

import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;

import java.util.List;

public class Contraction {

    public static BeliefBase contract(BeliefBase beliefBase, Formula formula){
        // TODO
        return null;
    }
    private static List<List<BeliefEntry>> findRemainders(List<BeliefEntry> entries, Formula phi){
        // TODO
        return List.of();
    }
    private static List<List<BeliefEntry>> selectionFunction(List<List<BeliefEntry>> remainders){
        // TODO
        return List.of();
    }
    private static List<BeliefEntry> intersect(List<List<BeliefEntry>> remainders){
        // TODO
        return List.of();
    }
    private static boolean entailsFormula(List<BeliefEntry> subset, Formula phi){
        // TODO
        return false;
    }
}
