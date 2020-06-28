package binomial;

import java.math.BigInteger;

public class Demo1 {  //Computation of binomial coefficient with Goetgheluck's algorithm combined with the multiplication tree

	public static void main(String[] args) {
		
		int n = 10_000_000;
		
		int k = 5_000_000;
		
		StaticMultiplier m = new StaticMultiplier();
		
		Primes p = new Primes(n); // all primes up to n are computed
		
		PrimeMultiplicationNode root = PrimeMultiplicationNode.construct(p.primes());  // prime multiplication tree is constructed to speed up the multiplication of consecutive primes

		BigInteger b = m.calculateCurrentBinomialCoefficient(p, root, n, k);
		
		System.out.println("Size of the resultant integer is " + b.bitLength() + " bits");

	}

}
