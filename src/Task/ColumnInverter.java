package Task;

import java.util.concurrent.Callable;
import Matrices.JMatrixAbstract;

public class ColumnInverter implements Callable<Boolean> {
	private int column;
	JMatrixAbstract result;
	JMatrixAbstract source;

	public ColumnInverter(int column, JMatrixAbstract result, JMatrixAbstract source) {
		this.column = column;
		this.result = result;
		this.source = source;
	}

	@Override
	public Boolean call() throws Exception {
		int N = source.getNCols();
		for (int row = column; row < N; row++) {
			if (row == column) {
				result.set(row, column, 1 / source.get(row, column));
				continue;
			}
			double sum = 0;
			for (int k = 0; k < row; k++) {
				double a = source.get(row, k);
				double b = result.get(k, column);
				double c = source.get(row, row);
				sum -= a * b / c;
			}
			result.set(row, column, sum);
		}
		return true;
	}
}
