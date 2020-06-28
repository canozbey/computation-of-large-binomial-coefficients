package binomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Primes {

	
	static Comparator<Coefficient> base_comparator = new Comparator<Coefficient>() {  
	    @Override  
	    public int compare(Coefficient c1, Coefficient c2) {  
	        return Integer.compare(c1.base().intValue(), c2.base().intValue()); 
	    }
	};
	
	
	private List<Coefficient> primes = new ArrayList<>();
	private BigInteger cumulative_prime;
	
	public Primes(int n){
		this.initialize_primes(n);
	}
	
	public List<Coefficient> primes(){
		return this.primes;
	}
	
	public Coefficient get(int index) {
		return this.primes.get(index);
	}
	
	private void initialize_primes(int n) {
		if(n<11) {
			throw new IllegalArgumentException();
		}
		
		Coefficient c1 = new Coefficient(BigInteger.valueOf(2)); c1.setCumulativeBitLength(Math.log(2));
		Coefficient c2 = new Coefficient(BigInteger.valueOf(3)); c2.setCumulativeBitLength(Math.log(6));
		Coefficient c3 = new Coefficient(BigInteger.valueOf(5)); c3.setCumulativeBitLength(Math.log(30));
		Coefficient c4 = new Coefficient(BigInteger.valueOf(7)); c4.setCumulativeBitLength(Math.log(210));

		
		this.primes.add(c1);
		this.primes.add(c2);
		this.primes.add(c3);
		this.primes.add(c4);
		
		double cumulative_bitlength = Math.log(210);
		
		for(int i=9;i<n+1;i+=2) {
			
			int limit = (int)Math.sqrt((double)i);
			int size = this.primes.size();
			
			for(int k=1;k<size;k++) {
				
				int p = this.primes.get(k).base().intValue();
				
				if (i%p == 0) {
					break;
				}
				else {
					if (p > limit || k == size - 1) {
						Coefficient c = new Coefficient(BigInteger.valueOf(i));
						cumulative_bitlength += Math.log(i);
						c.setCumulativeBitLength(cumulative_bitlength);
						this.primes.add(c);
						break;
					}
				}
			}	
		}	
	}
	
	public void set_cumulative_prime() {
		this.cumulative_prime = StaticMultiplier.multiply(this.primes);
	}
	

	public int ceil_index_sqrt(int factorial) {
		BigInteger search = BigInteger.valueOf((int)Math.sqrt(factorial) + 1);
		int index = Collections.binarySearch(this.primes, new Coefficient(search), base_comparator);
		if(index < 0) index = -(index + 1);
		return index;
	}
	
	public int ceil_index_half(int factorial) {
		BigInteger search = BigInteger.valueOf(factorial/2 + 1);
		int index = Collections.binarySearch(this.primes, new Coefficient(search), base_comparator);
		if(index < 0) index = -(index + 1);
		return index;
	}
	
	public int ceil_index(int factorial) {
		BigInteger search = BigInteger.valueOf(factorial + 1);
		int index = Collections.binarySearch(this.primes, new Coefficient(search), base_comparator);
		if(index < 0) index = -(index + 1);
		return index;
	}
	
	public int index(int prime) {
		BigInteger search = BigInteger.valueOf(prime);
		return Collections.binarySearch(this.primes, new Coefficient(search), base_comparator);

	}
	
	public static int prime_power(int prime, int factorial) {
		int f = factorial;
		int m = prime;
		int s = f/m;
		int power = 0;
		while(s > 0) {
			power += s;
			m *= prime;
			s = f/m;
		}
		return power;
	}
	
	public int[] factorize(int ceil, int factorial) {
		int[] factors = new int[ceil];
		for(int i=0;i<ceil;i++) {
			int prime = this.primes.get(i).base().intValue();
			factors[i] = prime_power(prime, factorial);
		}
		return factors;
		
	}

}
