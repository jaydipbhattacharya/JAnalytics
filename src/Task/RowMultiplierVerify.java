package Task;

import java.util.concurrent.Callable;
import Matrices.JAnalyticException;
import Matrices.JMatrixAbstract;

public class RowMultiplierVerify implements Callable<Boolean> {
	private int row;
	JMatrixAbstract matrix1;
	JMatrixAbstract matrix2;
	JMatrixAbstract compareWith;

	private static Boolean multiplyOperationVerify(JMatrixAbstract compareWith, JMatrixAbstract matrix1,
			JMatrixAbstract matrix2, int row) throws JAnalyticException {
		for (int col = 0; col < matrix2.getNCols(); col++) {
			double sum = 0;
			for (int k = Math.max(matrix1.startcolumn(row), matrix2.startrow(col)); k <= Math
					.min(matrix1.endcolumn(row), matrix2.endrow(col)); k++) {
				double a = matrix1.get(row, k);
				double b = matrix2.get(k, col);
				sum = sum + a * b;
			}
			if (Math.abs(compareWith.get(row, col) - sum) > 0.0000005) {
				throw (new JAnalyticException("Row=" + row + ", Col=" + col + " Mismatched "));
			}
		}
		return true;
	}

	public RowMultiplierVerify(int row, JMatrixAbstract compareWith, JMatrixAbstract matrix1, JMatrixAbstract matrix2) {
		this.row = row;
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.compareWith = compareWith;
	}

	@Override
	public Boolean call() throws Exception {
		return multiplyOperationVerify(compareWith, matrix1, matrix2, row);
	}

	@Override
	public String toString() {
		return "RowMultiplier :" + row + matrix1.getName() + matrix2.getName();
	}
}