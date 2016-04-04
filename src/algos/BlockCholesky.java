package algos;

import java.util.Stack;
import Matrices.JAnalyticException;
import Matrices.JMatrixAbstract;
import Matrices.JMatrixAbstract.MatrixType;
import Matrices.RectangularMatrix;
import Matrices.Utility;

public class BlockCholesky {
	private Stack<Integer> level;
	private int cholesky_sz;

	public BlockCholesky( int cholesky_sz) {
		level = new Stack<Integer>();
		this.cholesky_sz = cholesky_sz;
	}
	public BlockCholesky() {
		level = new Stack<Integer>();
		this.cholesky_sz = 1000;
	}

	public void blockCholesky(JMatrixAbstract c, JMatrixAbstract r, boolean useThreads, boolean debugThreads)
			throws Exception {
		level.push(1);
		for (int i = 0; i < level.size(); i++)
			System.out.print("\t");
		if (c.getNRows() <= cholesky_sz) {
			System.out.println("No more partititioning, invoking cholesky at level  " + level.size() + " , No Rows=" + c.getNRows() );
			cholesky(c, r, useThreads, debugThreads);
		} else {
			System.out.println("Partitioning at " + level.size() + " , No Rows=" + c.getNRows() );
			JMatrixAbstract a11 = Utility.getSubset(c, 0, 0, c.getNRows() / 2, c.getNRows() / 2, MatrixType.LOWER); // top
																													// left
																													// square
			JMatrixAbstract a12 = Utility.getSubset(c, 0, c.getNRows() / 2 + 1, c.getNRows() / 2, c.getNRows() - 1,
					MatrixType.RECTANGULAR); // top right rectangular
			JMatrixAbstract a22 = Utility.getSubset(c, c.getNRows() / 2 + 1, c.getNRows() / 2 + 1, c.getNRows() - 1,
					c.getNRows() - 1, MatrixType.SQUARE); // bottom right square
			JMatrixAbstract r11 = Utility.getSubset(r, 0, 0, r.getNRows() / 2, r.getNRows() / 2, MatrixType.LOWER); // top
																													// left
																													// half
																													// matrix
			JMatrixAbstract r21 = Utility.getSubset(r, r.getNRows() / 2 + 1, 0, r.getNRows() - 1, r.getNRows() / 2,
					MatrixType.RECTANGULAR); // bottom left rectangle
			JMatrixAbstract r22 = Utility.getSubset(r, r.getNRows() / 2 + 1, r.getNRows() / 2 + 1, r.getNRows() - 1,
					r.getNRows() - 1, MatrixType.SQUARE); // bottom right half
															// matrix
			// BLOCKCHOLESKY(A11, R11)
			blockCholesky(a11, r11, useThreads, debugThreads);
			for(int i=0; i< level.size(); i++) System.out.print("\t");
			System.out.println("Inverting " + r11.getNRows() +"," + r11.getNCols() + "\n");
			// R21 = T(inverse(R11) * A12 )
			JMatrixAbstract invr11 = Utility.invertLowerMatrix(r11, useThreads, debugThreads);
			//System.out.println(" verify invert " + Utility.verifylnvert(r11, invr11));
			for(int i=0; i< level.size(); i++) System.out.print("\t");
			System.out.println("Multiplying " + invr11.getNRows() +"," + invr11.getNCols() + "X" + a12.getNRows() + "," + a12.getNCols() + "\n");
			JMatrixAbstract invr11_a12 = new RectangularMatrix(invr11.getNRows(), a12.getNCols());
			Utility.multiply(invr11_a12, invr11, a12, useThreads, debugThreads);
			JMatrixAbstract invr11_a12_Transpose = Utility.getTransposedView(invr11_a12);
			for(int i=0; i< level.size(); i++) System.out.print("\t");
			System.out.println("Copying " + invr11_a12_Transpose.getNRows() +"," + invr11_a12_Transpose.getNCols() +"\n");
			Utility.copyOperation(r21, invr11_a12_Transpose);
			invr11_a12_Transpose = null;
			invr11_a12 = null;
			invr11 = null;
			// BLOCKCHOLESKY(A22 - R21 * T( R21 ) , R22 )
			for(int i=0; i < level.size(); i++) System.out.print("\t");
			System.out.println("Multiplying R21" + r21.getNRows() +"," + r21.getNCols() + "With XPose\n");
			JMatrixAbstract r21MultR21Transpose = new RectangularMatrix(r21.getNRows(), r21.getNRows());
			Utility.multiply(r21MultR21Transpose, r21, Utility.getTransposedView(r21), useThreads, debugThreads);
			for(int i=0; i< level.size(); i++) System.out.print("\t");
			System.out.println("Subtracting from a22" + r21MultR21Transpose.getNRows() +"," + r21MultR21Transpose.getNCols() +"\n");
			JMatrixAbstract temp = new RectangularMatrix(a22.getNRows(), a22.getNRows());
			Utility.minusOperation(temp, a22, r21MultR21Transpose);
			blockCholesky(temp, r22, useThreads, debugThreads);
		}
		for(int i=0; i< level.size(); i++) System.out.print("\t");
		System.out.println("End " + level.size());
		level.pop();
	}

	public void cholesky(JMatrixAbstract a, JMatrixAbstract l, boolean useThreads, boolean debugThreads)
			throws JAnalyticException {
		int sz = a.getNRows();
		for (int j = 0; j < sz; j++) {
			double sum = 0;
			for (int k = 0; k < j; k++)
				sum += Math.pow(l.get(j, k), 2);
			if (a.get(j, j) < sum)
				throw (new JAnalyticException("Not positive semidefinite"));
			l.set(j, j, Math.pow(a.get(j, j) - sum, 0.5));
			for (int i = j + 1; i < sz; i++) {
				sum = 0;
				for (int k = 0; k <= j; k++) {
					sum = sum + l.get(i, k) * l.get(j, k);
				}
				l.set(i, j, (a.get(i, j) - sum) / l.get(j, j));
			}
		}
	}
}
