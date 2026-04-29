package org.dtu.introai.beliefrevisionagent.belief;

import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;

public class Expansion {

    public static BeliefBase expand(BeliefBase beliefBase, Formula formula, int priority){
        beliefBase.expand(formula, priority);
        return beliefBase;
    }
}
