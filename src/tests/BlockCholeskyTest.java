package tests;

import Matrices.ConstantCorrMatrix;
import Matrices.JMatrixAbstract;
import Matrices.LowerMatrix;
import Matrices.SymmSquareMatrix;
import Matrices.Utility;
import algos.BlockCholesky;

class BlockCholeskyTest {
	public static void main(String[] args) {
		boolean useThreads = true;
		boolean debugThreads = false;
		int sz = 5000;
		//ConstantCorrMatrix corr_matrix = new ConstantCorrMatrix(sz, 0.46);
		SymmSquareMatrix corr_matrix = new SymmSquareMatrix(sz);
		for (int i = 0; i < corr_matrix.getNRows(); i++) {
			for (int j = 0; j <= i; j++) {
				if (i == j)
					corr_matrix.set(i, j, 1.0);
				else
					corr_matrix.set(i, j, 0.46);
			}
		}
		System.out.println("CorrMatrix\n" + corr_matrix);
		/* if you this sz parameter is same matrix size , BlockCholesky will NOT partition, it will simply call cholesky 
		 * to see blockcholesky in action pass this param as sz/2,sz/4 etc. sz does not have to be an even number
		 */
		BlockCholesky mc = new BlockCholesky( sz/4 );
		long start = System.currentTimeMillis();
		
		try {
			JMatrixAbstract l = new LowerMatrix(corr_matrix.getNRows());
			mc.blockCholesky(corr_matrix, l, useThreads, debugThreads);
			System.out.println("L\n" + l);
			long end = System.currentTimeMillis();
			System.out.println("Cholesky took =" + (end - start));
			JMatrixAbstract u = Utility.getTransposedView(l);
			System.out.println("U\n" + u);
			//Utility.multiplyVerifyOneCellAtATime(corr_matrix, l, u, useThreads, debugThreads);
			long end1 = System.currentTimeMillis();
			System.out.println("Verification took =" + (end1 - end));
			System.out.println("Success");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed");
		}

	}
}
