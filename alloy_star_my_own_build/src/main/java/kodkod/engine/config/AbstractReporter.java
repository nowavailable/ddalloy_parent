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
package kodkod.engine.config;


import java.util.List;
import java.util.Set;

import kodkod.ast.Decl;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.engine.bool.BooleanFormula;
import kodkod.engine.hol.HOLTranslation;
import kodkod.instance.Bounds;
import kodkod.instance.Instance;
import kodkod.util.ints.IntSet;

/**
 * A skeleton implementation of the {@link Reporter} interface.
 * The default implementation for each method has an empty body.s
 * @author Emina Torlak
 */
public abstract class AbstractReporter implements Reporter {

	/**
	 * Constructs a new abstract reporter.
	 */
	protected AbstractReporter() {}

	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.config.Reporter#detectingSymmetries(kodkod.instance.Bounds)
	 */
	public void detectingSymmetries(Bounds bounds){}

	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.config.Reporter#detectedSymmetries(java.util.Set)
	 */
	public void detectedSymmetries(Set<IntSet> parts) {}

	/**
	 * @see kodkod.engine.config.Reporter#generatingSBP()
	 */
	public void generatingSBP() {}

	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.config.Reporter#skolemizing(kodkod.ast.Decl, kodkod.ast.Relation, java.util.List)
	 */
	public void skolemizing(Decl decl, Relation skolem, List<Decl> context) {}

	/**
	 * @see kodkod.engine.config.Reporter#solvingCNF(int, int, int)
	 */
	public void solvingCNF(int primaryVars, int vars, int clauses) {}

	/**
	 * @see kodkod.engine.config.Reporter#optimizingBoundsAndFormula()
	 */
	public void optimizingBoundsAndFormula() {}

	/**
	 * @see kodkod.engine.config.Reporter#translatingToBoolean(kodkod.ast.Formula, kodkod.instance.Bounds)
	 */
	public void translatingToBoolean(Formula formula, Bounds bounds) {}

	/**
	 * @see kodkod.engine.config.Reporter#translatingToCNF(kodkod.engine.bool.BooleanFormula)
	 */
	public void translatingToCNF(BooleanFormula circuit) {}

    public void convertingToNNF() {}
    public void holLoopStart(HOLTranslation tr, Formula formula, Bounds bounds) {}
    public void holCandidateFound(HOLTranslation tr, Instance candidate) {}
    public void holVerifyingCandidate(HOLTranslation tr, Instance candidate, Formula checkFormula, Bounds bounds) {}
    public void holCandidateVerified(HOLTranslation tr, Instance candidate) {}
    public void holCandidateNotVerified(HOLTranslation tr, Instance candidate, Instance cex) {}
    public void holFindingNextCandidate(HOLTranslation tr, Formula inc) {}

	public void holSplitStart(HOLTranslation tr, Formula formula) {}
	public void holSplitChoice(HOLTranslation tr, Formula formula, Bounds bounds) {}
	public void holSplitChoiceSAT(HOLTranslation tr, Instance inst) {}
	public void holSplitChoiceUNSAT(HOLTranslation tr) {}

    public void holFixpointStart(HOLTranslation tr, Formula formula, Bounds bounds) {}
    public void holFixpointNoSolution(HOLTranslation tr) {}
    public void holFixpointFirstSolution(HOLTranslation tr, Instance candidate) {}
    public void holFixpointIncrementing(HOLTranslation tr, Formula inc) {}
    public void holFixpointIncrementingOutcome(HOLTranslation tr, Instance next) {}


}
