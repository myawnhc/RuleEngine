package com.theyawns.ruleengine.rulesets;

import com.theyawns.ruleengine.rules.Rule;

import java.util.List;
import java.util.function.Function;

// T - type of item to apply rules to, e.g., Transaction
// R - type of final result from the ruleset, currently either Double (fraud rules)
//     or Boolean (credit rules)
public interface RuleSet<T,R> extends Function<T, RuleSetEvaluationResult<T,R>> {

    void add(Rule<T,R> rule);
    String getName();
    String getQualifiedName();  // includes category
    int getRuleCount();
    List<Rule<T,R>> getRules();
    // implementors can override this to filter out irrelevant input items
    default boolean isApplicableTo(T input) { return true; }
    default RuleSetSelectionFilter getSelectionFilter() { return new RuleSetSelectionFilter.AppliesToAll(); }

    // Now considering this a private implementation detail, not public API
    //RuleSetEvaluationResult<R> aggregateResults();
}
