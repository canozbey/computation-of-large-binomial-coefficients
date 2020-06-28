package binomial;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Multiplier {
	
	
	public static BigInteger naive_multiply(List<Coefficient> coefficients) {
		BigInteger v = BigInteger.ONE;
		
		for(Coefficient c : coefficients) {
			v = v.multiply(c.pow());
		}
		return v;
	}
	
	public static BigInteger multiply(List<Coefficient> coefficients) {
		if(coefficients.size() == 0) return BigInteger.ONE;
		Coefficient first = coefficients.get(0);
		int size = coefficients.size();
		if(size==1) {
			return first.pow();
		}
		else if(size==2) {
			return first.pow().multiply(coefficients.get(1).pow());
		}
		else {
			Coefficient last = coefficients.get(size-1);
			double c1 = first.cumulativeBitLength();
			double c2 = last.cumulativeBitLength();
			double search = (c2 - c1) / 2.0 + c1;
			Coefficient floor = new Coefficient(search);
			
			int index = Collections.binarySearch(coefficients, floor, cumulative_comparator);
			if(index < 0) index = -(index + 1);
			
			return multiply(coefficients.subList(0, index))
					.multiply(multiply(coefficients.subList(index, size)));
			
		}
	}
	
	public static Comparator<Coefficient> cumulative_comparator = new Comparator<Coefficient>() {  
	    @Override  
	    public int compare(Coefficient c1, Coefficient c2) {  
	        return Double.compare(c1.cumulativeBitLength(), c2.cumulativeBitLength()); 
	    }  
	};

}
