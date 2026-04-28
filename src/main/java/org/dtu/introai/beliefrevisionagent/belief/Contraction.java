package org.dtu.introai.beliefrevisionagent.belief;

import org.dtu.introai.beliefrevisionagent.api.CNF;
import org.dtu.introai.beliefrevisionagent.logic.CNFConverter;
import org.dtu.introai.beliefrevisionagent.logic.ResolutionEngine;
import org.dtu.introai.beliefrevisionagent.logic.propositionallogic.Formula;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Contraction {

    /**
     * Performs contraction on a given belief base.
     * <p>
     * Implements B ÷ φ = ∩ γ(B⊥φ):
     * <ol>
     *   <li>{@link Contraction#findRemainders(List, Formula)} finds all remainder sets B⊥φ (maximal subsets not entailing φ).</li>
     *   <li>{@link Contraction#selectionFunction(List)} selects the best remainders γ(B⊥φ).</li>
     *   <li>{@link Contraction#intersect(List)} gives the intersection and thats converted to a contracted belief base</li>
     * </ol>
     *
     * @param beliefBase the current belief base to contract
     * @param formula    the formula φ to remove
     * @return a new belief base that no longer entails φ
     */
    public static BeliefBase contract(BeliefBase beliefBase, Formula formula) {
        List<BeliefEntry> entries = beliefBase.getEntries();

        // If φ is not entailed, contraction has no effect
        if (!entailsFormula(entries, formula)) {
            return beliefBase;
        }

        List<List<BeliefEntry>> remainders = findRemainders(entries, formula);

        // If no remainders exist e.g. φ is a tautology
        if (remainders.isEmpty()) {
            return new BeliefBase();
        }

        List<List<BeliefEntry>> selected = selectionFunction(remainders);
        List<BeliefEntry> result = intersect(selected);

        BeliefBase contracted = new BeliefBase();
        contracted.setEntries(result);
        return contracted;
    }

    /**
     * calculate the all inclusion-maximal subsets of {@code entries} that do not entail φ.
     * <p>
     * First, all non-entailing subsets are collected via recursion.
     * Then, any subset that is strictly contained in a larger candidate is
     * removed, leaving only the maximal ones.
     *
     * @param entries the current belief base entries
     * @param phi     the formula φ to contract away
     * @return the list of remainder sets
     */
    private static List<List<BeliefEntry>> findRemainders(List<BeliefEntry> entries, Formula phi) {
        List<List<BeliefEntry>> candidates = new ArrayList<>();
        findRemaindersHelper(entries, phi, 0, new ArrayList<>(), candidates);

        // Maximality filter: remove any candidate that is a strict subset of another candidate
        return candidates.stream()
                .filter(c -> candidates.stream().noneMatch(other -> other != c && new HashSet<>(other).containsAll(c)))
                .collect(Collectors.toList());
    }

    /**
     * Recursively generates all subsets of {@code entries} via binary include/exclude
     * decisions, collecting those that do not entail φ into {@code candidates}.
     *
     * @param entries    the full list of belief base entries
     * @param phi        the formula φ to contract away
     * @param index      the current position in {@code entries}
     * @param current    the subset being built in the current recursive branch
     * @param candidates accumulator for non-entailing subsets
     */
    private static void findRemaindersHelper(
            List<BeliefEntry> entries, Formula phi, int index,
            List<BeliefEntry> current, List<List<BeliefEntry>> candidates) {

        if (index == entries.size()) {
            // Base case: all include/exclude decisions made — check if this subset entails φ
            candidates.add(new ArrayList<>(current));
            return;
        }

        // Branch 1: include entries[index]
        current.add(entries.get(index));
        if (!entailsFormula(current, phi)) {
            // Only recurse deeper if we haven't started entailing φ yet
            findRemaindersHelper(entries, phi, index + 1, current, candidates);
        }
        current.removeLast();

        // Branch 2: exclude entries[index]
        findRemaindersHelper(entries, phi, index + 1, current, candidates);
    }

    /**
     * Selects the best remainders from B⊥φ based on entrenchment.
     * <p>
     * Each remainder is scored by summing the priorities of its entries, higher priority
     * means more entrenched and should be retained. Only the remainder(s) with the
     * highest score are selected.
     *
     * @param remainders the full remainder set B⊥φ
     * @return the selected subset γ(B⊥φ) of highest-scoring remainders
     */
    private static List<List<BeliefEntry>> selectionFunction(List<List<BeliefEntry>> remainders) {
        if (remainders.isEmpty()) return List.of();

        int maxScore = remainders.stream()
                .mapToInt(r -> r.stream().mapToInt(BeliefEntry::getPriority).sum())
                .max()
                .orElse(0);

        // Keep only remainders that achieve the maximum entrenchment score
        return remainders.stream()
                .filter(r -> r.stream().mapToInt(BeliefEntry::getPriority).sum() == maxScore)
                .collect(Collectors.toList());
    }

    /**
     * Returns the intersection of the selected remainders, implementing ∩ γ(B⊥φ).
     * <p>
     * An entry survives contraction only if it appears in every selected remainder.
     * This satisfies: B ÷ φ ⊆ B and ensures that contraction loses as few beliefs as possible.
     *
     * @param remainders the selected remainders γ(B⊥φ)
     * @return the intersection of all selected remainders
     */
    private static List<BeliefEntry> intersect(List<List<BeliefEntry>> remainders) {
        if (remainders.isEmpty()) return List.of();

        // Start from the first remainder and remove anything not in all others
        List<BeliefEntry> result = new ArrayList<>(remainders.getFirst());
        for (int i = 1; i < remainders.size(); i++) {
            result.retainAll(remainders.get(i));
        }
        return result;
    }

    /**
     * Checks whether the given subset of belief entries logically entails φ.
     * <p>
     * Converts all formulas to CNF, and merge them into a single CNF,
     * and delegates to {@link ResolutionEngine#entails(CNF, Formula)}.
     *
     * @param subset the belief entries to check
     * @param phi    the formula φ to test entailment of
     * @return {@code true} if the subset entails φ, {@code false} otherwise
     */
    private static boolean entailsFormula(List<BeliefEntry> subset, Formula phi) {
        // Empty set entails nothing
        if (subset.isEmpty()) return false;

        // Merge all entries into one CNF, then check entailment
        CNF kb = subset.stream()
                .map(entry -> CNFConverter.convert(entry.getFormula()))
                .reduce(new CNF(Set.of()), CNF::merge);

        return ResolutionEngine.entails(kb, phi);
    }
}