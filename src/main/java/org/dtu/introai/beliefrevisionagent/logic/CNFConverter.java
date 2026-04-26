package org.dtu.introai.beliefrevisionagent.logic;

import org.dtu.introai.beliefrevisionagent.api.CNF;
import org.dtu.introai.beliefrevisionagent.api.Clause;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.*;

import java.util.HashSet;
import java.util.Set;

public class CNFConverter {

    public static CNF convert(Formula f){
        Formula step1 = eliminateImplications(f);
        Formula step2 = pushNegationsInward(step1);
        return distribute(step2);
    }
    public static Formula eliminateImplications(Formula f){
        if (f instanceof Atom) {
            return f;
        }

        if (f instanceof Negation) {
            Negation neg = (Negation) f;
            return new Negation(eliminateImplications(neg.getOperand()));
        }

        if (f instanceof Conjunction) {
            Conjunction c = (Conjunction) f;
            return new Conjunction(
                    eliminateImplications(c.getLeft()),
                    eliminateImplications(c.getRight())
            );
        }

        if (f instanceof Disjunction) {
            Disjunction d = (Disjunction) f;
            return new Disjunction(
                    eliminateImplications(d.getLeft()),
                    eliminateImplications(d.getRight())
            );
        }

        if (f instanceof Implication) {
            Implication impl = (Implication) f;
            return new Disjunction(
                    new Negation(eliminateImplications(impl.getLeft())),
                    eliminateImplications(impl.getRight())
            );
        }

        if (f instanceof Biconditional) {
            Biconditional bic = (Biconditional) f;
            Formula left  = eliminateImplications(bic.getLeft());
            Formula right = eliminateImplications(bic.getRight());

            return new Conjunction(
                    new Disjunction(new Negation(left),  right),
                    new Disjunction(new Negation(right), left)
            );
        }

        throw new IllegalArgumentException("Unknown formula type: " + f.getClass());
    }
    public static Formula pushNegationsInward(Formula f){
        if (f instanceof Atom) {
            return f;
        }

        if (f instanceof Negation) {
            Formula inner = ((Negation) f).getOperand();

            if (inner instanceof Atom) {
                return f;
            }

            if (inner instanceof Negation) {
                return pushNegationsInward(((Negation) inner).getOperand());
            }

            if (inner instanceof Conjunction) {
                Conjunction c = (Conjunction) inner;
                return new Disjunction(
                        pushNegationsInward(new Negation(c.getLeft())),
                        pushNegationsInward(new Negation(c.getRight()))
                );
            }

            if (inner instanceof Disjunction) {
                Disjunction d = (Disjunction) inner;
                return new Conjunction(
                        pushNegationsInward(new Negation(d.getLeft())),
                        pushNegationsInward(new Negation(d.getRight()))
                );
            }
        }

        if (f instanceof Conjunction) {
            Conjunction c = (Conjunction) f;
            return new Conjunction(
                    pushNegationsInward(c.getLeft()),
                    pushNegationsInward(c.getRight())
            );
        }

        if (f instanceof Disjunction) {
            Disjunction d = (Disjunction) f;
            return new Disjunction(
                    pushNegationsInward(d.getLeft()),
                    pushNegationsInward(d.getRight())
            );
        }

        throw new IllegalArgumentException(
                "Unexpected formula type in pushNegationsInward " +
                        "(did you call eliminateImplications first?): " + f.getClass()
        );
    }
    public static CNF distribute(Formula f){
        // A single Atom → one CNF containing one Clause containing just that Atom
        // e.g. "A"  →  CNF{ Clause{A} }
        if (f instanceof Atom) {
            Clause clause = new Clause(
                    Set.of(f));
            return new CNF(Set.of(clause));
        }

        // ¬Atom → one CNF containing one Clause containing just that Negation
        // e.g. "¬A"  →  CNF{ Clause{¬A} }
        // After pushNegationsInward, ¬ can ONLY sit directly on an Atom here.
        // If it doesn't, something went wrong in steps 1 or 2.
        if (f instanceof Negation neg) {
            if (!(neg.getOperand() instanceof Atom)) {
                throw new IllegalStateException(
                        "Negation wrapping a non-Atom reached distribute(). " +
                                "Make sure eliminateImplications and pushNegationsInward ran first."
                );
            }
            Clause clause = new Clause(Set.of(f));
            return new CNF(Set.of(clause));
        }

        // A ∧ B → convert each side to CNF independently, then merge the clause sets.
        // CNF(A) ∧ CNF(B) = just all their clauses combined into one big CNF.
        // e.g. CNF{ Clause{A}, Clause{B} }.merge( CNF{ Clause{C} } )
        //    = CNF{ Clause{A}, Clause{B}, Clause{C} }
        if (f instanceof Conjunction c) {
            CNF leftCNF  = distribute(c.getLeft());
            CNF rightCNF = distribute(c.getRight());
            return leftCNF.merge(rightCNF);
            //             ^^^^^ just unions the two Set<Clause> together
        }

        // A ∨ B → the hard case. We take the cross-product of the two clause sets.
        //
        // Why cross-product? Because if left has clauses {L1, L2} and right has {R1}:
        //   (L1 ∧ L2) ∨ R1  =  (L1 ∨ R1) ∧ (L2 ∨ R1)
        //
        // So every left clause gets merged with every right clause into one bigger clause.
        //
        // e.g. left  = CNF{ Clause{P, Q}, Clause{R} }   meaning (P∨Q) ∧ R
        //      right = CNF{ Clause{S, T} }               meaning (S∨T)
        //      result = CNF{ Clause{P,Q,S,T}, Clause{R,S,T} }
        //                    ^^^ L1 merged with R1         ^^^ L2 merged with R1
        if (f instanceof Disjunction d) {
            CNF leftCNF  = distribute(d.getLeft());
            CNF rightCNF = distribute(d.getRight());

            Set<Clause> resultClauses = new HashSet<>();

            for (Clause leftClause : leftCNF.cnf()) {
                for (Clause rightClause : rightCNF.cnf()) {
                    resultClauses.add(leftClause.merge(rightClause));
                    // ^^^^^^^^^^^^^^^^ merge() unions the two Sets<Formula> together
                    // giving us one bigger OR clause
                }
            }

            return new CNF(resultClauses);
        }

        throw new IllegalArgumentException("Unknown formula type in distribute(): " + f.getClass());
    }
}
