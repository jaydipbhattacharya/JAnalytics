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
		if (c.getNRows() <= cholesky_sz) {
			for (int i = 0; i < level.size(); i++) System.out.print("\t");
			System.out.println("No more partititioning, invoking cholesky at level  " + level.size() + " , No Rows=" + c.getNRows() );
			cholesky(c, r, useThreads, debugThreads);
		} else {
			for(int i=0; i< level.size(); i++) System.out.print("\t");
			System.out.print("Partitioning Matrix=(" + c.getNRows() + ") into ");
			JMatrixAbstract a11 = Utility.getSubset(c, 0, 0, c.getNRows() / 2, c.getNRows() / 2, MatrixType.LOWER); // top
																													// left
			System.out.print("A11(" + a11.getNCols() + ")  ");                                 // square
			JMatrixAbstract a12 = Utility.getSubset(c, 0, c.getNRows() / 2 + 1, c.getNRows() / 2, c.getNRows() - 1,
					MatrixType.RECTANGULAR); // top right rectangular
			
			System.out.print("A12(" +a12.getNRows() +"," +  a12.getNCols() + ")  ");
			
			JMatrixAbstract a22 = Utility.getSubset(c, c.getNRows() / 2 + 1, c.getNRows() / 2 + 1, c.getNRows() - 1,
					c.getNRows() - 1, MatrixType.SQUARE); // bottom right square
			
			System.out.println("A22 (" +a22.getNCols() + ")  ");
			
			JMatrixAbstract r11 = Utility.getSubset(r, 0, 0, r.getNRows() / 2, r.getNRows() / 2, MatrixType.LOWER); // top
																													// left
																													// half
																													// matrix
			JMatrixAbstract r21 = Utility.getSubset(r, r.getNRows() / 2 + 1, 0, r.getNRows() - 1, r.getNRows() / 2,
					MatrixType.RECTANGULAR); // bottom left rectangle
			JMatrixAbstract r22 = Utility.getSubset(r, r.getNRows() / 2 + 1, r.getNRows() / 2 + 1, r.getNRows() - 1,
					r.getNRows() - 1, MatrixType.SQUARE); // bottom right half  matrix
			for(int i=0; i< level.size(); i++) System.out.print("\t");
			System.out.println("BLOCKCHOLESKY(A11, R11) ");
			blockCholesky(a11, r11, useThreads, debugThreads);
			for(int i=0; i< level.size(); i++) System.out.print("\t");
			System.out.println("R21 = T(inverse(R11) * A12 ) r11=( " + r11.getNRows() + ")");
			// R21 = T(inverse(R11) * A12 )
			JMatrixAbstract invr11 = Utility.invertLowerMatrix(r11, useThreads, debugThreads);
			for(int i=0; i< level.size(); i++) System.out.print("\t\t");
			System.out.println("INVERT DONE");
			
			
			//System.out.println(" verify invert " + Utility.verifylnvert(r11, invr11));
			JMatrixAbstract invr11_a12 = new RectangularMatrix(invr11.getNRows(), a12.getNCols());
			Utility.multiply(invr11_a12, invr11, a12, useThreads, debugThreads);
			for(int i=0; i< level.size(); i++) System.out.print("\t\t");
			System.out.println("MULTIPLY DONE");
			JMatrixAbstract invr11_a12_Transpose = Utility.getTransposedView(invr11_a12);
			Utility.copyOperation(r21, invr11_a12_Transpose);
			for(int i=0; i< level.size(); i++) System.out.print("\t\t");
			System.out.println("COPY DONE");
			invr11_a12_Transpose = null;
			invr11_a12 = null;
			invr11 = null;
			// BLOCKCHOLESKY(A22 - R21 * T( R21 ) , R22 )
			for(int i=0; i < level.size(); i++) System.out.print("\t");
			System.out.println("BLOCKCHOLESKY(A22 - R21 * T( R21 ) , R22 )");
			JMatrixAbstract r21MultR21Transpose = new RectangularMatrix(r21.getNRows(), r21.getNRows());
			Utility.multiply(r21MultR21Transpose, r21, Utility.getTransposedView(r21), useThreads, debugThreads);
			for(int i=0; i< level.size(); i++) System.out.print("\t\t");
			System.out.println("MULTIPLY DONE");
			JMatrixAbstract temp = new RectangularMatrix(a22.getNRows(), a22.getNRows());
			Utility.minusOperation(temp, a22, r21MultR21Transpose);
			for(int i=0; i< level.size(); i++) System.out.print("\t\t");
			System.out.println("MINUS DONE");
			blockCholesky(temp, r22, useThreads, debugThreads);
		}
		for(int i=0; i< level.size(); i++) System.out.print("\t");
		System.out.println("End ");
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
