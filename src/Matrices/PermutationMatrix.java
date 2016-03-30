package Matrices;

public class PermutationMatrix extends JMatrixAbstract {
	private double[] values;
	private int sz;

	public PermutationMatrix(int _sz) {
		this.sz = _sz;
		this.values = new double[sz];
		for (int i = 0; i < sz; i++)
			values[i] = i;
	}

	@Override
	public int getNRows() {
		return sz;
	}

	@Override
	public int getNCols() {
		return sz;
	}

	@Override
	public void set(int row, int eol, double value) {
	}

	@Override
	public double get(int row, int eol) {
		if (values[row] == eol)
			return 1;
		else
			return 0;
	}

	@Override
	public void switchRows(int rowi, int rowj) {
		double v = values[rowi];
		values[rowi] = values[rowj];
		values[rowj] = v;
	}

	@Override
	public void switchRows(int rowi, int rowj, int starteol, int endeol) {
		switchRows(rowi, rowj);
	}

	@Override
	public void switchRows(int rowi, int rowj, int startcol) {
		switchRows(rowi, rowj);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Permutation Matrix";
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
		// TODO Auto-generated method stub
		return getNRows() - 1;
	}
}
