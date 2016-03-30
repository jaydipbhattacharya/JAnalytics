package tests;

import java.util.Random;
import Matrices.JAnalyticException;
import Matrices.JMatrixAbstract;
import Matrices.UpperMatrix;
import Matrices.Utility;

public class TransposeTest {
	public static void main(String[] args) throws JAnalyticException {
		UpperMatrix m = new UpperMatrix(4);
		Random rand = new Random();
		for (int i = 0; i < m.getNRows(); i++) {
			for (int j = i; j < m.getNCols(); j++) {
				m.set(i, j, rand.nextDouble() * 0.8);
			}
		}
		System.out.println("U=");
		System.out.println(m);
		JMatrixAbstract mTranspose = Utility.getTransposedView(m);
		System.out.println("Tranpose=");
		System.out.println(mTranspose);
		mTranspose.set(3, 0, -10);
		System.out.println("After setting(3,0)=");
		System.out.println(mTranspose);
		mTranspose.set(1, 3, -20);
		System.out.println("After setting(1,3)=");
		System.out.println(mTranspose);
		mTranspose = Utility.getTransposedView(mTranspose);
		System.out.println("Re-Tranpose=");
		System.out.println(mTranspose);
	}
}
