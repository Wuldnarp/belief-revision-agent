package org.dtu.introai.beliefrevisionagent.belief;

import org.dtu.introai.beliefrevisionagent.api.CNF;
import org.dtu.introai.beliefrevisionagent.logic.ResolutionEngine;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Atom;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Conjunction;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Disjunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContractionAGMTest {

    Atom p;
    Atom q;

    @BeforeEach
    void setup(){
        p = new Atom("p");
        q = new Atom("q");
    }

    @Test
    void closureTest(){

        BeliefBase bb = new BeliefBase();
        bb.expand(p, 1);
        bb.expand(q, 2);

        BeliefBase contract = Contraction.contract(bb, p);
        CNF resultCNF = contract.toCNF();

        // expect: if q is in result, then q∨r should also be entailed by result
        assertTrue(ResolutionEngine.entails(resultCNF, new Disjunction(q, new Atom("r"))));
    }

    @Test
    void successTest(){

        BeliefBase bb = new BeliefBase();
        bb.expand(p, 1);
        BeliefBase contract = Contraction.contract(bb, p);

        // expect: p not entailed
        assertTrue(contract.getEntries().isEmpty());

    }

    @Test
    void inclusionTest(){

        BeliefBase bb = new BeliefBase();
        bb.expand(p, 1);
        bb.expand(q, 2);
        BeliefBase contract = Contraction.contract(bb, p);

        // expect: q since q has higher priority and doesn't entail p
        assertTrue(bb.getEntries().containsAll(contract.getEntries()));
    }

    @Test
    void vacuityTest(){

        BeliefBase bb = new BeliefBase();
        bb.expand(q, 1);
        BeliefBase contract = Contraction.contract(bb, p);

        // expect: still contains q
        assertTrue(contract.getEntries().stream().anyMatch(e -> e.getFormula().equals(q)));
    }

    @Test
    void extensionalityTest(){

        BeliefBase bb = new BeliefBase();
        bb.expand(p, 1);
        bb.expand(q, 1);
        BeliefBase bb2 = new BeliefBase();
        bb2.expand(p, 1);
        bb2.expand(q, 1);

        BeliefBase contract = Contraction.contract(bb, new Conjunction(p, q));
        BeliefBase contract2 = Contraction.contract(bb2, new Conjunction(q, p));

        // expect: identical results
        assertEquals(contract.getEntries().size(), contract2.getEntries().size());
        assertTrue(contract.getEntries().containsAll(contract2.getEntries()));
        assertTrue(contract.getEntries().containsAll(contract2.getEntries()));

    }

    @Test
    void recoveryTest(){

        BeliefBase bb = new BeliefBase();
        bb.expand(p, 1);
        bb.expand(q, 1);

        BeliefBase contract = Contraction.contract(bb, p);
        BeliefBase recovered = Expansion.expand(contract, p, 1);

        // expect: recovered entails everything in the original
        CNF recoveredCNF = recovered.toCNF();
        for (BeliefEntry entry : bb.getEntries()) {
            assertTrue(ResolutionEngine.entails(recoveredCNF, entry.getFormula()));
        }

    }

    @Test
    void conjunctiveInclusionTest(){

        BeliefBase bb = new BeliefBase();
        bb.expand(p, 1);
        bb.expand(q, 1);
        BeliefBase bb2 = new BeliefBase();
        bb2.expand(p, 1);
        bb2.expand(q, 1);

        BeliefBase contractConjunc = Contraction.contract(bb, new Conjunction(p, q));
        BeliefBase contract = Contraction.contract(bb2, p);

        // expect: B ÷ (p∧q) ⊆ B ÷ p
        assertTrue(contract.getEntries().containsAll(contractConjunc.getEntries()));

    }

    @Test
    void conjunctivOverlapeTest(){

        BeliefBase bb = new BeliefBase();
        bb.expand(p, 1);
        bb.expand(q, 1);
        BeliefBase bb2 = new BeliefBase();
        bb2.expand(p, 1);
        bb2.expand(q, 1);
        BeliefBase bb3 = new BeliefBase();
        bb3.expand(p, 1);
        bb3.expand(q, 1);

        BeliefBase contract = Contraction.contract(bb, p);
        BeliefBase contract2 = Contraction.contract(bb2, q);
        BeliefBase contractConjunc = Contraction.contract(bb3, new Conjunction(p, q));

        // find entries in both B÷p and B÷q
        List<BeliefEntry> overlap = new ArrayList<>(contract.getEntries());
        overlap.retainAll(contract2.getEntries());

        // expect: all overlapping entries are also in B ÷ (p∧q)
        assertTrue(contractConjunc.getEntries().containsAll(overlap));
    }

}