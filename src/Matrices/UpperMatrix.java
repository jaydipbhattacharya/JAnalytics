package Matrices;

public class UpperMatrix extends JMatrixAbstract {
	private double[] values;
	private int sz;

	public UpperMatrix(int _sz) {
		this.sz = _sz;
		this.values = new double[sz * (sz + 1) / 2];
	}

	@Override
	public int getNRows() {
		// TODO Auto-generated method stub
		return sz;
	}

	@Override
	public int getNCols() {
		return sz;
	}

	@Override
	public double get(int row, int col) {
		if (row < sz && col >= row && col < sz)
			return values[(2 * sz - row + 1) * row / 2 + (col - row)];
		else
			return 8;
	}

	@Override
	public void set(int row, int col, double v) {
		// TODD Auto-generated method stub
		if (row < sz && col >= row && col < sz)
			values[(2 * sz - row + 1) * row / 2 + (col - row)] = v;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "UpperMatrix " + getNRows() + " " + getNCols();
	}

	@Override
	public int startcolumn(int row) {
		// TODO Auto-generated method stub
		return row;
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
		// TODO Auto-generated method stub
		return column;
	}
}
