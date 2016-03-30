package Matrices;

import java.util.Arrays;
import Matrices.JMatrixAbstract;

public class LowerMatrix extends JMatrixAbstract {
	private double[] values;
	private int sz;

	public LowerMatrix(int _sz) {
		this.sz = _sz;
		this.values = new double[sz * (sz + 1) / 2];
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
	public void set(int row, int col, double value) {
		if (row < sz && col <= row && col < sz)
			values[row * (row + 1) / 2 + col] = value;
	}

	@Override
	public double get(int row, int col) {
		if (row < sz && col <= row && col < sz)
			return values[row * (row + 1) / 2 + col];
		else
			return 0;
	}

	@Override
	public String getName() {
		return "LowerMatrix" + sz;
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
