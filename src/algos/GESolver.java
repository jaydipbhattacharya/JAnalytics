package algos;

import Matrices.JAnalyticException;
import Matrices.JMatrixAbstract;
import Matrices.RectangularMatrix;
import Matrices.Utility;
import Task.RowMultiplier;
import Task.ThreadableTask;

public class GESolver {
	/****
	 * Ax=B PAx=PB LUx=PB Let Ux=Y So, LY=PB Solve for Y
	 * 
	 * 
	 * then Ux=Y Solve for x
	 * 
	 * @throws JAnalyticException
	 */
	public static JMatrixAbstract solve(JMatrixAbstract coeff, JMatrixAbstract rhs, boolean useThreads,
			boolean debugThreads) throws Exception {
		LUDecomp lu = new LUDecomp(coeff, useThreads, debugThreads);
		lu.decompose(0);
		return substituteSolve(lu, rhs, useThreads, debugThreads);
	}

	public static JMatrixAbstract substituteSolve(LUDecomp lu, JMatrixAbstract rhs, boolean useThreads,
			boolean debugThreads) throws Exception {
		int size = lu.getPermutation().getNRows();
		JMatrixAbstract PB = new RectangularMatrix(size, 1);
		Utility.multiply(PB, lu.getPermutation(), rhs, useThreads, debugThreads);
		JMatrixAbstract y = new RectangularMatrix(size, 1);
		for (int i = 0; i < size; i++) {
			double sum = 0;
			for (int k = 0; k < i; k++) {
				sum += lu.getLowerDecomposed().get(i, k) * y.get(k, 0);
			}
			y.set(i, 0, (PB.get(i, 0) - sum) / lu.getLowerDecomposed().get(i, i));
		}
		/*
		 * verify LY=PB ThreadableTaskAbstract multiplyExecutor3 = new
		 * ExecuteMatrixMultiply2(lu.getLowerDecomposed().getNRows();
		 * ColumnVectorAbstract LY = new ColumnVectorAbstract(
		 * lu.getLowerDecomposed().getNRows()); multiplyExecutor3.compute(LY,
		 * lu.getLowerDecomposed(), y ); boolean b = PB.equals(LY);
		 */
		JMatrixAbstract solution = new RectangularMatrix(size, 1);
		for (int i = size - 1; i >= 0; i--) {
			double sum = 0;
			for (int k = i; k < size; k++) {
				sum += lu.getUpperDecomposed().get(i, k) * solution.get(k, 0);
			}
			solution.set(i, 0, (y.get(i, 0) - sum) / lu.getUpperDecomposed().get(i, i));
		}
		/*
		 * Verify UX=Y ThreadableTaskAbstract multiplyExecutor4 = new
		 * ExecuteMatrixMultiply2(lu.getUpperDecomposed().getNRows());
		 * ColumnVectorAbstract ux = new ColumnVectorAbstract(
		 * lu.getUpperDecomposed().getNRows()); multiplyExecutor4.compute(ux,
		 * lu.getUpperDecomposed(), solution); b = ux.equals(y);
		 * System.out.println(b);
		 */
		return solution;
	}

	public static boolean verifySolve(JMatrixAbstract original, JMatrixAbstract solution, JMatrixAbstract rhs)
			throws Exception {
		JMatrixAbstract result = new RectangularMatrix(original.getNRows(), 1);
		ThreadableTask<Boolean> multiplyExecutor = new ThreadableTask<Boolean>(original.getNRows(), false);
		for (int row = 0; row < original.getNRows(); row++) {
			multiplyExecutor.submit(new RowMultiplier(row, result, original, solution));
		}
		multiplyExecutor.compute();
		return Utility.compareOperation(result, rhs);
	}
}
