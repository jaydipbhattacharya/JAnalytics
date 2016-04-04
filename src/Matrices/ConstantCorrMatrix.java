package Matrices;

public class ConstantCorrMatrix extends JMatrixAbstract {
	double value ;
	int sz ;
	public ConstantCorrMatrix(int _sz, double value ) {
		this.sz = _sz;
		this.value=value;
	}

	@Override
	public void set(int row, int col, double value) {
	}

	@Override
	public int getNRows() {
		// TODO Auto-generated method stub
		return sz;
	}

	@Override
	public int getNCols() {
		// TODO Auto-generated method stub
		return sz;
	}

	@Override
	public double get(int row, int col) {
		// TODO Auto-generated method stub
		if (row != col)
			return value;
		else
			return 1.0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ConstantCorrMatrix:" + getNRows();
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
