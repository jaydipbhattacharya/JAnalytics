package Task;

import java.util.concurrent.Callable;
import algos.LUDecomp;

public class ReduceRow implements Callable<Boolean> {
	int row, startrow;
	LUDecomp lu;

	public ReduceRow(int row, int startrow, LUDecomp lu) {
		this.row = row;
		this.startrow = startrow;
		this.lu = lu;
	}

	@Override
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		lu.reduceRow(row, startrow);
		return true;
	}

	@Override
	public String toString() {
		return "ReduceRow :" + row + "," + startrow;
	}
}
