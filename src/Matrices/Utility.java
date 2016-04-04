package Matrices;

import Task.LowerMatrixColumnInverter;
import Task.RowMultiplier;
import Task.RowMultiplierVerify;
import Task.SubstituteSolver;
import Task.ThreadableTask;
import algos.LUDecomp;


public class Utility {
	// Only lower matrix inverts now
	public static JMatrixAbstract invertLowerMatrix(JMatrixAbstract source, boolean useThreads, boolean debugThreads)
			throws Exception {
		if (source.getNRows() != source.getNCols()) {
			throw (new JAnalyticException("No Rows!=No Cols"));
		}
		ThreadableTask<Boolean> columnlnverter = new ThreadableTask<Boolean>(useThreads ? source.getNCols() : 0,
				debugThreads);
		JMatrixAbstract inv = null;
		inv = new LowerMatrix(source.getNRows());
		for (int column = 0; column < inv.getNCols(); column++) {
			columnlnverter.submit(new LowerMatrixColumnInverter(column, inv, source));
		}
		columnlnverter.compute();
		return inv;
	}

	public static JMatrixAbstract invert(JMatrixAbstract source, boolean useThreads, boolean debugThreads)
			throws Exception {
		if (source.getNRows() != source.getNCols()) {
			throw (new JAnalyticException("No Rows!=No Cols"));
		}
		JMatrixAbstract mcopy = new RectangularMatrix(source.getNRows(), source.getNRows());
		Utility.copyOperation(mcopy, source);
		JMatrixAbstract inverted = new RectangularMatrix(source.getNRows(), source.getNRows());
		LUDecomp lu = new LUDecomp(source, useThreads, debugThreads);
		lu.decompose(0);
		// System.out.println( "LU De.~QmQ done ");
		//System.out.println( "Verfied LU = " + lu.verifyLUDecomposition(mcopy) );
		int noThreads = 0;
		if (useThreads)
			noThreads = inverted.getNCols();
		ThreadableTask<Boolean> solver = new ThreadableTask<Boolean>(noThreads, debugThreads);
		for (int column = 0; column < inverted.getNCols(); column++) {
			solver.submit(new SubstituteSolver(column, lu, inverted, mcopy));
		}
		solver.compute();
		return inverted;
	}

	public static JMatrixAbstract invertUpperMatrix(JMatrixAbstract u) throws JAnalyticException {
		if (u.getNRows() != u.getNCols()) {
			throw (new JAnalyticException("No Rows!=No Cols"));
		}
		JMatrixAbstract result = new UpperMatrix(u.getNRows());
		int N = u.getNCols();
		for (int col = 0; col < N; col++) {
			for (int row = col; row >= 0; row--) {
				if (row == col) {
					result.set(row, col, 1 / u.get(row, col));
					continue;
				}
				double sum = 0;
				for (int k = row + 1; k <= col; k++) {
					double a = u.get(row, k);
					double b = result.get(k, col);
					double c = u.get(row, row);
					sum -= a * b / c;
				}
				result.set(row, col, sum);
			}
		}
		return result;
	}

	public static boolean verifylnvert(JMatrixAbstract source, JMatrixAbstract inverted) throws Exception {
		JMatrixAbstract result = new RectangularMatrix(source.getNRows(), source.getNRows());
		ThreadableTask<Boolean> multiplyExecutor = new ThreadableTask<Boolean>(source.getNRows(), false); // source.getNRows(),
		// false
		// );
		for (int row = 0; row < source.getNRows(); row++) {
			multiplyExecutor.submit(new RowMultiplier(row, result, source, inverted));
		}
		multiplyExecutor.compute();
		PermutationMatrix p = new PermutationMatrix(source.getNRows());
		System.out.println("Verified=\n" + result);
		return Utility.compareOperation(result, p);
	}

	public static void multiply(JMatrixAbstract result, JMatrixAbstract op1, JMatrixAbstract op2, boolean useThreads,
			boolean debugThreads) throws Exception {
		if (op1.getNCols() != op2.getNRows()) {
			throw (new JAnalyticException("opl.getNCols() != op2.getNRows() "));
		}
		if (result.getNRows() != op1.getNRows() || result.getNCols() != op2.getNCols()) {
			throw (new JAnalyticException("result has wrong size "));
		}
		ThreadableTask<Boolean> multiplyExecutor = new ThreadableTask<Boolean>(useThreads ? op1.getNRows() : 0,
				debugThreads);
		for (int row = 0; row < op1.getNRows(); row++) {
			multiplyExecutor.submit(new RowMultiplier(row, result, op1, op2));
		}
		multiplyExecutor.compute();
	}

	public static void multiplyVerifyOneCellAtATime(JMatrixAbstract compareWith, JMatrixAbstract opl,
			JMatrixAbstract op2, boolean useThreads, boolean debugThreads) throws Exception {
		if (opl.getNCols() != op2.getNRows()) {
			throw (new JAnalyticException("opl.getNCols() != op2.getNRows() "));
		}
		if (compareWith.getNRows() != opl.getNRows() || compareWith.getNCols() != op2.getNCols()) {
			throw (new JAnalyticException("result has wrong size "));
		}
		ThreadableTask<Boolean> multiplyExecutor = new ThreadableTask<Boolean>(useThreads ? opl.getNRows() : 0,
				debugThreads);
		for (int row = 0; row < opl.getNRows(); row++) {
			multiplyExecutor.submit(new RowMultiplierVerify(row, compareWith, opl, op2));
		}
		multiplyExecutor.compute();
	}

	public static void copyOperation(JMatrixAbstract lhs, JMatrixAbstract rhs) throws JAnalyticException {
		if (rhs.getNRows() != lhs.getNRows() || rhs.getNCols() != lhs.getNCols()) {
			throw (new JAnalyticException("source /target of different size "));
		}
		for (int col = 0; col < rhs.getNCols(); col++) {
			for (int row = 0; row < rhs.getNRows(); row++) {
				lhs.set(row, col, rhs.get(row, col));
			}
		}
	}

	/* result = opl-op2 */
	public static void minusOperation(JMatrixAbstract result, JMatrixAbstract opl, JMatrixAbstract op2)
			throws JAnalyticException {
		if (opl.getNRows() != op2.getNRows() || opl.getNCols() != op2.getNCols()) {
			throw (new JAnalyticException("source /target of different size "));
		}
		for (int col = 0; col < opl.getNCols(); col++) {
			for (int row = 0; row < opl.getNRows(); row++) {
				result.set(row, col, opl.get(row, col) - op2.get(row, col));
			}
		}
	}

	public static boolean compareOperation(JMatrixAbstract rhs, JMatrixAbstract lhs) throws JAnalyticException {
		if (rhs.getNRows() != lhs.getNRows() || rhs.getNCols() != lhs.getNCols()) {
			throw (new JAnalyticException("source /target of different size "));
		}
		for (int i = 0; i < lhs.getNRows(); i++) {
			for (int j = 0; j < lhs.getNCols(); j++) {
				if (Math.abs(lhs.get(i, j) - rhs.get(i, j)) > 1e-5) {
					System.out.println(
							"Mismatch : i=" + i + ",j=" + j + " valuel=" + lhs.get(i, j) + ", value2=" + rhs.get(i, j));
					return false;
				}
			}
		}
		return true;
	}

	public static JMatrixAbstract getSubset(JMatrixAbstract m, int startrow, int startcol, int endrow, int endcol,
			Matrices.JMatrixAbstract.MatrixType shape) throws JAnalyticException {
		return new JMatrixSubset(m, startrow, startcol, endrow, endcol, shape);
	}

	public static JMatrixAbstract getTransposedView(JMatrixAbstract m) {
		return new JMatrixTranspose(m);
	}
}
