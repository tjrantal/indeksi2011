package polynomialFit;
import Jama.*;		/*Java matrix package (http://math.nist.gov/javanumerics/jama/) for polynomial fit*/
public class PolynomialFit{
	/*
		fit nth order polynomial on fitArray (^2 = 2nd order)
		fitArray[0][] X-values
		fitArray[1][] Y-values
	*/
	public static double[] polynomialFit(double[][] fitArray, int nthOrder){

		double[][] xx = new double[fitArray[0].length][nthOrder+1];
		
		for (int i = 0; i< fitArray[0].length;++i){
			for (int j = 0;j<nthOrder+1;++j){
				xx[i][j] =Math.pow(fitArray[0][i],(double)j);
			}
		}
		Matrix Y = new Matrix(fitArray[1],fitArray[1].length);
		Matrix X = new Matrix(xx);
		double[] coefficients = new double[nthOrder+1];
		try{
			Matrix solution = X.solve(Y);
			for (int j = 0;j<nthOrder+1;++j){
				coefficients[j] = solution.get(j,0);
			}
		}catch(Exception err){
			System.err.println("Matrix coefficients: " + err.getMessage());
			return null;
		}
		return coefficients;
	}
}