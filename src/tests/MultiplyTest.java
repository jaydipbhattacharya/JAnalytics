package tests;

import java.util.Random;
import Matrices.JMatrixAbstract;
import Matrices.RectangularMatrix;
import Matrices.Utility;
import Task.RowMultiplier;
import Task.ThreadableTask;

public class MultiplyTest {
	public static void main(String[] args) throws Exception {
		JMatrixAbstract m = new RectangularMatrix(3, 3);
		Random rand = new Random();
		for (int i = 0; i < m.getNRows(); i++) {
			for (int j = 0; j < m.getNCols(); j++) {
				m.set(i, j, rand.nextDouble() * 0.8);
			}
		}
		System.out.println("M=");
		System.out.println(m);
		JMatrixAbstract mcopy = new RectangularMatrix(3, 3);
		Utility.copyOperation(mcopy, m);
		JMatrixAbstract n = new RectangularMatrix(3, 3);
		for (int i = 0; i < m.getNRows(); i++) {
			for (int j = 0; j < m.getNCols(); j++) {
				n.set(i, j, rand.nextDouble() * 0.8);
			}
		}
		System.out.println("N=");
		System.out.println(n);
		ThreadableTask<Boolean> multiplyExecutor = new ThreadableTask<Boolean>(0, false);
		for (int row = 0; row < m.getNRows(); row++) {
			multiplyExecutor.submit(new RowMultiplier(row, m, m, n));
		}
		multiplyExecutor.compute();
		System.out.println("MN=");
		System.out.println(m);
	}
}
