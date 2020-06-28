package binomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class StaticMultiplier extends Multiplier{
	
	
	public BigInteger calculateCurrentBinomialCoefficient(Primes primes, int n, int k) {
		
		if(k == 0) return BigInteger.ONE;
		
		if(k == 1) return BigInteger.valueOf(n);

		return multiply(this.goetgheluck(primes, n, k));
		
	}
	
	public BigInteger calculateCurrentBinomialCoefficient(Primes primes, PrimeMultiplicationNode multiplication_tree, int n, int k) {
		
		if(k == 0) return BigInteger.ONE;
		
		if(k == 1) return BigInteger.valueOf(n);

		return multiply(this.goetgheluck(primes, multiplication_tree, n, k));
		
	}
	
	public List<Coefficient> goetgheluck(Primes prime, int n, int k){
		List<Coefficient> coefficients = new ArrayList<>();
		List<Coefficient> primes = prime.primes();
		double bitLength = 0.0;
		int d = n - k + 1;
		int bound = Collections.binarySearch(primes, new Coefficient(BigInteger.valueOf(d)), Primes.base_comparator);
		if(bound < 0) bound = -(bound + 1);
		for(int index = 0;index<primes.size();index++) {
			BigInteger i = primes.get(index).base();
			int n_ = n;
			int k_ = k;
			int e = 0;
			int r = 0;
			int p = i.intValue();
			double p_d = (double) p;
			if (p_d <= n/p_d) {
				int a, b;
				while(n_>0) {
					a = n_ % p;
					n_ = n_ / p;
					b = k_ % p + r;
					k_ = k_ / p;
					if(a < b) {
						e++;
						r = 1;
					}
					else {
						r = 0;
					}
				}
				if(e != 0) {
					Coefficient c = new Coefficient(i, e);
					bitLength += c.bitLength();
					c.setCumulativeBitLength(bitLength);
					coefficients.add(c);
				}
			}
			else if(p_d <= n/2.0) {
				if(n_ % p < k_ % p) {
					Coefficient c = new Coefficient(i, 1);
					bitLength += c.bitLength();
					c.setCumulativeBitLength(bitLength);
					coefficients.add(c);
				}
			}
			else if(p < d) {
				index = bound - 1;
			}
			else if(p <= n) {//send it to prime multiplication tree
				Coefficient c = new Coefficient(i, 1);
				bitLength += c.bitLength();
				c.setCumulativeBitLength(bitLength);
				coefficients.add(c);
			}
			else {
				break;
			}
		}
		return coefficients;
	}
	
	public List<Coefficient> goetgheluck(Primes prime, PrimeMultiplicationNode multiplication_tree, int n, int k){
		List<Coefficient> coefficients = new ArrayList<>();
		List<Coefficient> primes = prime.primes();
		double bitLength = 0.0;
		int d = n - k + 1;
		int bound = Collections.binarySearch(primes, new Coefficient(BigInteger.valueOf(d)), Primes.base_comparator);
		if(bound < 0) bound = -(bound + 1);
		for(int index = 0;index<primes.size();index++) {
			BigInteger i = primes.get(index).base();
			int n_ = n;
			int k_ = k;
			int e = 0;
			int r = 0;
			int p = i.intValue();
			double p_d = (double) p;
			if (p_d <= n/p_d) {
				int a, b;
				while(n_>0) {
					a = n_ % p;
					n_ = n_ / p;
					b = k_ % p + r;
					k_ = k_ / p;
					if(a < b) {
						e++;
						r = 1;
					}
					else {
						r = 0;
					}
				}
				if(e != 0) {
					Coefficient c = new Coefficient(i, e);
					bitLength += c.bitLength();
					c.setCumulativeBitLength(bitLength);
					coefficients.add(c);
				}
			}
			else if(p_d <= n/2.0) {
				if(n_ % p < k_ % p) {
					Coefficient c = new Coefficient(i, 1);
					bitLength += c.bitLength();
					c.setCumulativeBitLength(bitLength);
					coefficients.add(c);
				}
			}
			else if(p < d) {
				index = bound - 1;
			}
			else if(p <= n) {//send it to prime multiplication tree
				
				int n_bound = Collections.binarySearch(primes, new Coefficient(BigInteger.valueOf(n)), Primes.base_comparator);
				
				n_bound = (n_bound < 0) ? -(n_bound + 1) : n_bound + 1;
				
				List<Coefficient> base_primes = multiplication_tree.multiply(index, n_bound, primes, bitLength);
				
				coefficients.addAll(base_primes);
				
				break;
				
			}
			else {
				break;
			}
		}
		
		return coefficients;
	}
	
	
}
