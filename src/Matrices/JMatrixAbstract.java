package Matrices;

class JMatrixSubset extends JMatrixAbstract {
	JMatrixAbstract data;
	int startrow, startcol, endrow, endcol;
	MatrixType shape;

	// JMatrixSubset( m, startrow, startcol, endrow, endcol, shape);
	public JMatrixSubset(JMatrixAbstract data, int startrow, int startcol, int endrow, int endcol, MatrixType shape)
			throws JAnalyticException {
		this.startcol = startcol;
		this.startrow = startrow;
		this.endrow = endrow;
		this.endcol = endcol;
		this.data = data;
		this.shape = shape;
		if (startcol > data.getNCols() - 1 && endcol > data.getNCols() - 1 && startrow > data.getNRows() - 1
				&& endrow > data.getNRows() - 1)
			throw (new JAnalyticException("Invalid parameters in subset"));
	}

	@Override
	public int getNRows() {
		return endrow - startrow + 1;
	}

	@Override
	public int getNCols() {
		return endcol - startcol + 1;
	}

	@Override
	public void set(int row, int col, double value) {
		data.set(row + startrow, col + startcol, value);
	}

	@Override
	public double get(int row, int col) {
		return data.get(row + startrow, col + startcol);
	}

	@Override
	public String getName() {
		return "View:[" + startrow + "," + startcol + "," + endrow + ";" + endcol;
	}

	@Override
	public int startcolumn(int row) {
		return 0;
	}

	@Override
	public int startrow(int column) {
		if (MatrixType.LOWER == shape) {
			return column;
		}
		return 0;
	}

	@Override
	public int endcolumn(int row) {
		if (MatrixType.LOWER == shape) {
			return row;
		}
		return getNCols() - 1;
	}

	@Override
	public int endrow(int column) {
		return getNRows() - 1;
	}
}

class JMatrixTranspose extends JMatrixAbstract {
	JMatrixAbstract data;

	public JMatrixTranspose(JMatrixAbstract data) {
		this.data = data;
	}

	@Override
	public int getNRows() {
		return data.getNCols();
	}

	@Override
	public int getNCols() {
		return data.getNRows();
	}

	@Override
	public void set(int row, int col, double value) {
		data.set(col, row, value);
	}

	@Override
	public double get(int row, int col) {
		return data.get(col, row);
	}

	@Override
	public String getName() {
		// Auto-generated method stub
		return "TransposeView: [" + getNRows() + "," + getNCols() + "]";
	}

	@Override
	public int startcolumn(int row) {
		// TODO Auto-generated method stub
		return data.startrow(row);
	}

	@Override
	public int startrow(int column) {
		// TODO Auto-generated method stub
		return data.startcolumn(column);
	}

	@Override
	public int endcolumn(int row) {
		// TODO Auto-generated method stub
		return data.endrow(row);
	}

	@Override
	public int endrow(int column) {
		// TODO Auto-generated method stub
		return data.endcolumn(column);
	}
}

public abstract class JMatrixAbstract {
	public enum MatrixType {
		LOWER, UPPER, RECTANGULAR, SQUARE
	}
	public static double JZERO=0.0000005;

	public abstract int getNRows();

	public abstract int getNCols();

	public abstract double get(int row, int col);

	public abstract void set(int row, int col, double v);

	public abstract String getName();

	public abstract int startcolumn(int row);

	public abstract int startrow(int column);

	public abstract int endcolumn(int row);

	public abstract int endrow(int column);

	@Override
	public String toString() {
		if (getNRows() > 20 || getNCols() > 20)
			return ("");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < getNCols(); i++)
			sb.append(i + "\t");
		sb.append("\n");
		for (int i = 0; i < getNRows(); i++) {
			for (int j = 0; j < startcolumn(i); j++) {
				sb.append(String.format("%s", "\t"));
			}
			for (int j = startcolumn(i); j <= endcolumn(i); j++) {
				sb.append(String.format("%.4f", get(i, j)) + "\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void switchRows(int rowi, int rowj, int startcol, int endcol) {
		for (int col = startcol; col <= endcol; col++) {
			double temp = get(rowi, col);
			set(rowi, col, get(rowj, col));
			set(rowj, col, temp);
		}
	}

	public void switchRows(int rowi, int rowj) {
		for (int col = 0; col <= getNCols() - 1; col++) {
			double temp = get(rowi, col);
			double v = get(rowj, col);
			set(rowi, col, v);
			set(rowj, col, temp);
		}
	}

	public void switchRows(int rowi, int rowj, int startcol) {
		for (int col = startcol; col <= getNCols() - 1; col++) {
			double temp = get(rowi, col);
			set(rowi, col, get(rowj, col));
			set(rowj, col, temp);
		}
	}
}
