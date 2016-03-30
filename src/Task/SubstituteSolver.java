package Task;

import java.util.concurrent.Callable;
import Matrices.JMatrixAbstract;
import algos.GESolver;
import algos.LUDecomp;

public class SubstituteSolver implements Callable<Boolean> {
	int column;
	LUDecomp lu;
	JMatrixAbstract inverted, origCoeff;
	

	public SubstituteSolver(int column, LUDecomp lu, JMatrixAbstract inverted, JMatrixAbstract origCoeff) {
		this.lu = lu;
		this.column = column;
		this.inverted = inverted;
		this.origCoeff = origCoeff;
	}

	@Override
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		// System.Dut.println("Solving" + column +" Thread ." +
		Thread.currentThread().getName();
		JMatrixAbstract solution;
		boolean useThreads = true;
		boolean debugThreads = false;
		JMatrixAbstract rhs = new ColumnVectorForlthRow(inverted.getNRows(), column);
		solution = GESolver.substituteSolve(lu, rhs, useThreads, debugThreads);
		
		boolean b = GESolver.verifySolve(origCoeff, solution,  rhs);
		if ( !b ) {
			int xx =1;
		}
		
		for (int j = 0; j < inverted.getNRows(); j++) {
			inverted.set(j, column, solution.get(j, 0));
		}
		// System.out.println("Solved" + column +" Thread ." +
		// thread.currentThread().getName(»
		return true;
	}

	@Override
	public String toString() {
		return "SubstituteSolver ." + column;
	}

	class ColumnVectorForlthRow extends JMatrixAbstract {
		private int nrows;
		private int nonZeroRow;

		public ColumnVectorForlthRow(int nrows, int nonzeroRow) {
			this.nrows = nrows;
			this.nonZeroRow = nonzeroRow;
		}

		@Override
		public int getNRows() {
			return nrows;
		}

		@Override
		public int getNCols() {
			return 1;
		}

		@Override
		public void set(int row, int col, double value) {
		}

		@Override
		public double get(int row, int col) {
			if (row == nonZeroRow)
				return 1.0;
			return 0;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "ColumnVectorForlthRow:" + nrows + +nonZeroRow;
		}

		@Override
		public int startcolumn(int row) {
			// TODD Auto-generated method stub
			return 0;
		}

		@Override
		public int startrow(int column) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int endcolumn(int row) {
			// tODO Auto-generated method stub
			return getNCols() - 1;
		}

		@Override
		public int endrow(int column) {
			// TODD Auto-generated method stub
			return getNRows() - 1;
		}
	}
}
