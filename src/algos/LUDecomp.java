package algos;

import Matrices.JAnalyticException;
import Matrices.JMatrixAbstract;
import Matrices.PermutationMatrix;
import Matrices.RectangularMatrix;
import Matrices.Utility;
import Task.RowMultiplier;
import Task.ThreadableTask;
import Task.ReduceRow;

public class LUDecomp {
	private JMatrixAbstract a;
	private PermutationMatrix permutation;
	private LowerDecomposed LowerDecomposed;
	private UpperDecomposed UpperDecomposed;
	private boolean decomposed;
	private boolean useThreads;
	private boolean debugThread;

	public LUDecomp(JMatrixAbstract _a) {
		this(_a, true, false);
	}

	public LUDecomp(JMatrixAbstract _a, boolean useThreads, boolean debugThread) {
		a = _a;
		permutation = new PermutationMatrix(a.getNRows());
		LowerDecomposed = new LowerDecomposed(a);
		UpperDecomposed = new UpperDecomposed(a);
		decomposed = false;
		this.useThreads = useThreads;
		this.debugThread = debugThread;
	}

	public LowerDecomposed getLowerDecomposed() throws JAnalyticException {
		if (decomposed)
			return LowerDecomposed;
		else
			throw (new JAnalyticException("Not yet decomposed"));
	}

	public UpperDecomposed getUpperDecomposed() throws JAnalyticException {
		if (decomposed)
			return UpperDecomposed;
		else
			throw (new JAnalyticException("Not yet decomposed"));
	}

	public final JMatrixAbstract getA() {
		return a;
	}

	public final JMatrixAbstract getPermutation() {
		return permutation;
	}

	private int getPivotRow(int startrow) {
		double big = 1E-10;
		int pivotrow = -1;
		for (int row = startrow; row <= a.getNRows() - 1; row++) {
			if (Math.abs(a.get(row, startrow)) > big) {
				big = Math.abs(a.get(row, startrow));
				pivotrow = row;
			}
		}
		return pivotrow;
	}

	public void decompose(int startrow) throws Exception {
		System.out.println("In LU " + startrow);
		if (startrow >= a.getNRows() - 1) {
			decomposed = true;
			return;
		}
		int pivotrow = getPivotRow(startrow);
		if (pivotrow < 0) {
			throw (new JAnalyticException("Pivot row could not be found "));
		}
		a.switchRows(startrow, pivotrow); // , startcol);
		permutation.switchRows(startrow, pivotrow);
		// System.out.println("Before Eli ination n + startrow );
		// System.out.println(a);
		// System.out.println(permutation);
		if (Math.abs(a.get(startrow, startrow)) < 1E-10) {
			throw (new JAnalyticException("npivot value could not be found "));
		}
		int noThreads = 0;
		if (useThreads)
			noThreads = a.getNRows() - 1 - startrow;
		ThreadableTask<Boolean> rowReducer = new ThreadableTask<Boolean>(noThreads, debugThread);
		for (int row = startrow + 1; row <= a.getNRows() - 1; row++) {
			rowReducer.submit(new ReduceRow(row, startrow, this));
		}
		rowReducer.compute();
		// System.out.println("After Elimination" + startrow );
		// ISystem.out.println(a);
		// System.out.println(permutation);
		decompose(startrow + 1);
	}

	public void reduceRow(int row, int startrow) {
		double scale = a.get(row, startrow) / a.get(startrow, startrow);
		// System.out.println( "Current Th~ead:" +
		// Thread.currentThread().getName() + ", Reduce row called for
		// startrow=" + startrow + " running row=" + row + "scale "+ scale );
		for (int col = startrow; col <= a.getNCols() - 1; col++) {
			double newvalue = a.get(row, col) - a.get(startrow, col) * scale;
			a.set(row, col, newvalue);
		}
		a.set(row, startrow, scale);
	}

	public boolean verifyLUDecomposition(JMatrixAbstract original) throws Exception {
		// System.out.println("Lower=");System.out.println(getLowerDecomposed(»;
		// System.out.println("Upper=");System.out.println(getUpperDecomposed(»;
		JMatrixAbstract PA = new RectangularMatrix(permutation.getNRows(), permutation.getNRows());
		ThreadableTask<Boolean> multiplyExecutor1 = new ThreadableTask<Boolean>(permutation.getNRows(), false);
		for (int row = 0; row < permutation.getNRows(); row++) {
			multiplyExecutor1.submit(new RowMultiplier(row, PA, permutation, original));
		}
		multiplyExecutor1.compute();
		// System.out.println( PA )j
		RectangularMatrix LU = new RectangularMatrix(getLowerDecomposed().getNRows(), getLowerDecomposed().getNRows());
		ThreadableTask<Boolean> multiplyExecutor2 = new ThreadableTask<Boolean>(getLowerDecomposed().getNRows(), false);
		for (int row = 0; row < getLowerDecomposed().getNRows(); row++) {
			multiplyExecutor2.submit(new RowMultiplier(row, LU, getLowerDecomposed(), getUpperDecomposed()));
		}
		multiplyExecutor2.compute();
		// System.out.println("PA")jSystem.out.println( PA );
		// System.out.println("LU")jSystem.out.println( LU );
		return Utility.compareOperation(LU, PA);
	}

	class UpperDecomposed extends JMatrixAbstract {
		private JMatrixAbstract data;

		public UpperDecomposed(JMatrixAbstract _data) {
			this.data = _data;
		}

		@Override
		public int getNRows() {
			return data.getNRows();
		}

		@Override
		public int getNCols() {
			return data.getNCols();
		}

		@Override
		public void set(int row, int col, double value) {
		}

		@Override
		public double get(int row, int col) {
			if (row > col)
				return 0;
			return data.get(row, col);
		}

		@Override
		public String getName() {
			// II TooO Auto-generated method stub
			return "Upper";
		}

		@Override
		public int startcolumn(int row) {
			return row;
		}

		@Override
		public int startrow(int column) {
			return 0;
		}

		@Override
		public int endcolumn(int row) {
			return getNCols() - 1;
		}

		@Override
		public int endrow(int column) {
			return column;
		}
	}

	class LowerDecomposed extends JMatrixAbstract {
		private JMatrixAbstract data;

		public LowerDecomposed(JMatrixAbstract _data) {
			this.data = _data;
		}

		@Override
		public int getNRows() {
			return data.getNRows();
		}

		@Override
		public int getNCols() {
			return data.getNCols();
		}

		@Override
		public void set(int row, int col, double value) {
		}

		@Override
		public double get(int row, int col) {
			if (row == col)
				return 1.0;
			if (row < col)
				return 0;
			return data.get(row, col);
		}

		@Override
		public String getName() {
			return "Lower";
		}

		@Override
		public int startcolumn(int row) {
			return 0;
		}

		@Override
		public int startrow(int column) {
			return column;
		}

		@Override
		public int endcolumn(int row) {
			return row;
		}

		@Override
		public int endrow(int column) {
			return getNRows() - 1;
		}
	}
}
