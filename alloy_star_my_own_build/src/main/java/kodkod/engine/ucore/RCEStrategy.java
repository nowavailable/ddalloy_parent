/* 
 * Kodkod -- Copyright (c) 2005-present, Emina Torlak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package kodkod.engine.ucore;

import kodkod.engine.fol2sat.TranslationLog;
import kodkod.engine.fol2sat.Translator;
import kodkod.engine.satlab.ReductionStrategy;
import kodkod.engine.satlab.ResolutionTrace;
import kodkod.util.ints.IntCollection;
import kodkod.util.ints.IntIterator;
import kodkod.util.ints.IntSet;
import kodkod.util.ints.Ints;

/**
 * Recycling Core Extraction is a strategy for generating unsat cores that are minimal at the logic level.  
 * Specifically, let C be a core that is minimal according to this strategy, 
 * and let F(C) be the top-level logic constraints
 * corresponding to C.  Then, this strategy guarantees that there is no clause
 * c in C such that F(C - c) is a strict subset of F(C). Furthermore, it also
 * guarantees that for all f in F(C), F(C) - f is satisfiable.  This is a stronger
 * guarantee than that of {@linkplain HybridStrategy}.  In general, using this strategy
 * is more expensive, timewise, than using {@linkplain HybridStrategy}.
 * 
 * <p>This implementation of RCE will work properly only on CNFs generated by the kodkod {@linkplain Translator}. </p>
 * @author Emina Torlak
 * @see HybridStrategy
 */
public class RCEStrategy implements ReductionStrategy {
	private final IntCollection varsToTry;
	private final int dist;
	
	/**
	 * Constructs an RCE strategy that will use the given translation
	 * log to relate the cnf clauses back to the logic constraints from 
	 * which they were generated. By default, all relevant resolvents
	 * are used in each iteration.
	 */
	public RCEStrategy(final TranslationLog log) {
		this(log, Integer.MAX_VALUE);
	}
	

	/**
	 * Constructs an RCE strategy that will use the given translation
	 * log to relate the cnf clauses back to the logic constraints from 
	 * which they were generated. The relevant resolvents
	 * used in each iteration are reachable from the relevant axioms
	 * in at most <tt>dist</tt> steps.
	 * @requires dist >= 0
	 */
	public RCEStrategy(final TranslationLog log, int dist) {
		if (dist<0) throw new IllegalArgumentException("Resolution distance must be non-negative: " + dist);
		varsToTry = StrategyUtils.rootVars(log);
		this.dist = dist;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.satlab.ReductionStrategy#next(kodkod.engine.satlab.ResolutionTrace)
	 */
	public IntSet next(ResolutionTrace trace) {
		if (varsToTry.isEmpty()) return Ints.EMPTY_SET; // tried everything
		final IntSet relevantVars = StrategyUtils.coreTailUnits(trace);
		
		for(IntIterator varItr = varsToTry.iterator(); varItr.hasNext();) {
			final int var = varItr.next();
			varItr.remove();
			if (relevantVars.remove(var)) { // remove maxVar from the set of relevant variables
				if (relevantVars.isEmpty()) break; // there was only root formula left
				// get all axioms and resolvents corresponding to the clauses that
				// form the translations of formulas identified by relevant vars
				final IntSet relevantClauses = clausesFor(trace, relevantVars); 
				assert !relevantClauses.isEmpty() && !relevantClauses.contains(trace.size()-1);
				return relevantClauses;
			}
		}
		
		varsToTry.clear();		
		return Ints.EMPTY_SET;
	}
	

	/**
	 * Returns the indices of all axioms and resolvents
	 * in the given trace that form the translations of the formulas
	 * identified by the given variables.  This method assumes that
	 * the axioms in the given trace were generated by the Kodkod
	 * {@linkplain Translator}.
	 * @return 
	 * let C = { c: trace.prover.clauses | c.maxVariable() in relevantVars },
	 *     T = { c1, c2: C | c2.maxVariable() in abs(c1.literals) },
	 *     A = C.*T | 
	 *     trace.backwardReachable(A) - trace.backwardReachable(trace.axioms() - A)
	 */
	private IntSet clausesFor(ResolutionTrace trace, IntSet relevantVars) { 

		final IntSet relevantAxioms = StrategyUtils.clausesFor(trace, relevantVars);

		if (dist<trace.resolvents().size()) { 
			IntSet relevant = relevantAxioms;
			for(int i = 0, lastSize = 0; lastSize < relevant.size() && i < dist; i++) { 
				lastSize = relevant.size();
				relevant = trace.directlyLearnable(relevant);
			}
			return relevant;
		} else {
			return trace.learnable(relevantAxioms); // return all resolvents
		}
		
	
//		System.out.println("level 1 resolvents " + (relevant.size()-relevantAxioms.size()) + ",  axioms " + relevantAxioms.size());
		
	}
}
