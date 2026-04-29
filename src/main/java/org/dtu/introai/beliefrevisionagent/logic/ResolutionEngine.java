package org.dtu.introai.beliefrevisionagent.logic;

import java.util.HashSet;
import java.util.Set;

import org.dtu.introai.beliefrevisionagent.api.CNF;
import org.dtu.introai.beliefrevisionagent.api.Clause;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Negation;

public class ResolutionEngine {


    public static boolean entails(CNF kb, Formula phi){
        CNF notPhi = CNFConverter.convert(new Negation(phi));
        CNF combined = kb.merge(notPhi);
        
        return !isConsistent(combined);
    }
    public static boolean isConsistent(CNF cnf){
        Set<Clause> clauses = new HashSet<>(cnf.cnf());

        while (true) {
            Set<Clause> newClauses = new HashSet<>();

            for (Clause c1 : clauses) {
                for (Clause c2 : clauses) {
                    if (c1.equals(c2)) continue;

                    Clause resolvent = resolve(c1, c2);
                    if (resolvent == null) {
                        continue;
                    } else if (resolvent.isEmpty()) {
                        return false; 
                    } else {
                        newClauses.add(resolvent);
                    }
                }
            }

            if (newClauses.isEmpty()) return true; 
            
            clauses.addAll(newClauses);
        }
    }
    public static Clause resolve(Clause c1, Clause c2){
        for (Formula c1Formula : c1.literals()) {
            for (Formula c2Formula : c2.literals()) {
                if (areComplementary(c1Formula, c2Formula)) {
                    return c1.without(c1Formula).merge(c2.without(c2Formula));
                }
            }
        }

        return null;
    }
    public static boolean areComplementary(Formula f1, Formula f2){
        if (f1 instanceof Negation && ((Negation) f1).getOperand().equals(f2)) return true;
        if (f2 instanceof Negation && ((Negation) f2).getOperand().equals(f1)) return true;
        return false;
    }
}
