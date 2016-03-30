package Matrices;

public class SymmSquareMatrix extends JMatrixAbstract {
	LowerMatrix data;

	public SymmSquareMatrix(int _sz) {
		data = new LowerMatrix(_sz);
	}

	@Override
	public void set(int row, int col, double value) {
		data.set(row, col, value);
	}

	@Override
	public int getNRows() {
		// TODO Auto-generated method stub
		return data.getNRows();
	}

	@Override
	public int getNCols() {
		// TODO Auto-generated method stub
		return data.getNCols();
	}

	@Override
	public double get(int row, int col) {
		// TODO Auto-generated method stub
		if (row > col)
			return data.get(row, col);
		else
			return data.get(col, row);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SymmSquareMatrix:" + getNRows();
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
