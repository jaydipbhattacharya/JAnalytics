package Task;

import java.util.concurrent.Callable;
import Matrices.JMatrixAbstract;

public class RowMultiplier implements Callable<Boolean> {
	private int row;
	JMatrixAbstract matrix1;
	JMatrixAbstract matrix2;
	JMatrixAbstract target;

	// JMatrixAbstract onerow;
	private static Boolean multiplyOperation(JMatrixAbstract target, JMatrixAbstract matrix1, JMatrixAbstract matrix2,
			int row) {
		if (matrix1.getNCols() != matrix2.getNRows())
			return false;
		double[] temp = new double[matrix2.getNCols()];
		// System.out.println("begin mult ro "+ row);
		for (int col = 0; col < matrix2.getNCols(); col++) {
			double sum = 0;
			for (int k = Math.max(matrix1.startcolumn(row), matrix2.startrow(col)); k <= Math
					.min(matrix1.endcolumn(row), matrix2.endrow(col)); k++) {
				double a = matrix1.get(row, k);
				double b = matrix2.get(k, col);
				sum = sum + a * b;
			}
			temp[col] = sum;
		}
		for (int i = 0; i < matrix2.getNCols(); i++)
			target.set(row, i, temp[i]);
		// System.out.println("End mult row" + row);
		return true;
	}

	public RowMultiplier(int row, JMatrixAbstract target, JMatrixAbstract matrix1, JMatrixAbstract matrix2) {
		this.row = row;
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.target = target;
		// this.onerow = new RectangularMatrix( 1, matrix2.getNCols());
	}

	@Override
	public Boolean call() throws Exception {
		// II TODO Auto-generated method stub
		return multiplyOperation(target, matrix1, matrix2, row);
		/*
		 * for ( int col = 0; col < matrix2.getNCols(); col++){ target.set( row,
		 * col, onerow.get(0, col)); }
		 */
	}

	@Override
	public String toString() {
		return "RowMultiplier :" + row + "," + matrix1.getName() + matrix2.getName();
	}
}
