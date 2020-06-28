package binomial;

import java.math.BigInteger;

public class Demo2 {

	public static void main(String[] args) { //Computation of binomial coefficient without using the multiplication tre
		
		int n = 10_000_000;
		
		int k = 5_000_000;
		
		StaticMultiplier m = new StaticMultiplier();
		
		Primes p = new Primes(n); // all primes up to n are computed
		
		BigInteger b = m.calculateCurrentBinomialCoefficient(p, n, k);
		
		System.out.println("Size of the resultant integer is " + b.bitLength() + " bits");

	}

}
