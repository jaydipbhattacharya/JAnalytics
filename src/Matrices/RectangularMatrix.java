package Matrices;

public class RectangularMatrix extends JMatrixAbstract {
	private double[] values;
	private int nrows;
	private int ncols;

	public RectangularMatrix(int nrows, int ncols) {
		this.nrows = nrows;
		this.ncols = ncols;
		this.values = new double[nrows * ncols];
	}

	@Override
	public int getNRows() {
		return nrows;
	}

	@Override
	public int getNCols() {
		return ncols;
	}

	@Override
	public void set(int row, int col, double value) {
		values[row * ncols + col] = value;
	}

	@Override
	public double get(int row, int col) {
		return values[row * ncols + col];
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "RectangularMatrix:" + nrows + ")" + ncols;
	}

	@Override
	public int startcolumn(int row) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int startrow(int column) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int endcolumn(int row) {
		// TODO Auto-generated method stub
		return getNCols() - 1;
	}

	@Override
	public int endrow(int column) {
		// TODD Auto-generated method stub
		return getNRows() - 1;
	}
}
