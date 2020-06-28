package binomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


public class PrimeMultiplicationNode {

	
	Coefficient integer = null;
	int from;
	int to;
	PrimeMultiplicationNode left = null;
	PrimeMultiplicationNode right = null;
	static BigInteger identity = BigInteger.ONE;
	
	
	private PrimeMultiplicationNode(List<Coefficient> coefficients, int from){
		int size = coefficients.size();
		int to = from + size;
		this.from = from;
		this.to = to;
		if(size==1) {
			this.integer = new Coefficient(coefficients.get(0));
		}
		else if(size==2) {
			this.left = new PrimeMultiplicationNode(coefficients.subList(0, 1), from);
			this.right = new PrimeMultiplicationNode(coefficients.subList(1, 2), from + 1);
			this.integer = this.left.value().multiply(this.right.value());
		}
		else {
			Coefficient first = coefficients.get(0);
			Coefficient last = coefficients.get(size-1);
			double c1 = first.cumulativeBitLength();
			double c2 = last.cumulativeBitLength();
			double search = (c2 - c1) / 2.0 + c1;
			Coefficient floor = new Coefficient(search);
			
			int index = Collections.binarySearch(coefficients, floor, Multiplier.cumulative_comparator);
			if(index < 0) index = -(index + 1);
			
			this.left = new PrimeMultiplicationNode(coefficients.subList(0, index), from);
			this.right = new PrimeMultiplicationNode(coefficients.subList(index, size), from + index);
			this.integer = this.left.value().multiply(this.right.value());
			
		}
		
	}
	
	public static PrimeMultiplicationNode construct(List<Coefficient> coefficients) {
		return new PrimeMultiplicationNode(coefficients, 0);
	}

	
	public List<PrimeMultiplicationNode> serialize(){
		List<PrimeMultiplicationNode> coefficients = new ArrayList<>();
		this.bfa(coefficients, this);
		return coefficients;
	}
	
	private void bfa(List<PrimeMultiplicationNode> coefficients, PrimeMultiplicationNode node) {
		if(node.left() == null) coefficients.add(node);
		else {
			this.bfa(coefficients, node.left());
			coefficients.add(node);
			this.bfa(coefficients, node.right());
		}
	}
	
	public static BigInteger multiply(BigInteger last, BigInteger current) {
		if(last.intValue()==1) return current;
		if(current.intValue()==1) return last;
		return last.multiply(current);
	}
	
	public static BigInteger divide(BigInteger num, BigInteger denom) {
		if(denom.intValue()==1) return num;
		return num.divide(denom);
	}
	

	
	
	public List<Coefficient> multiply(int from_index, int to_index, List<Coefficient> primes, double cumul_start) { //cumulative start value may change. see goethgeluck
		
		List<Coefficient> values = new LinkedList<>();
		if(from_index >= to_index) return values;
		ListIterator<Coefficient> iterator = values.listIterator();
		this.multiply(from_index, to_index, primes, iterator);
		double sum = cumul_start;
		for(Coefficient c : values) {
			sum += c.bitLength();
			c.setCumulativeBitLength(sum);
		}
		return values;
	}
	
	public static ListIterator<Coefficient> add(ListIterator<Coefficient> iterator, Coefficient value){
		if(!iterator.hasPrevious()) {
			iterator.add(value);
			return iterator;
		}
		Coefficient next = iterator.previous();
		if(value.bitLength() >= next.bitLength()) {
			iterator.next();
			iterator.add(value);
			return iterator;
		}
		while(iterator.hasPrevious()) {
			Coefficient prev = iterator.previous();
			if(value.bitLength() >= prev.bitLength()) {
				iterator.next();
				iterator.add(value);
				return iterator;
			}
		}
		iterator.add(value);
		return iterator;
		
	}
	
	private ListIterator<Coefficient> multiply(int from_index, int to_index, List<Coefficient> coefficients, ListIterator<Coefficient> values) {
		if(from_index == to_index) return values;
		if(to_index - from_index == 1) {
			Coefficient v = coefficients.get(from_index);
			values = add(values, v);
			return values;
		}
		
		if(from_index==this.from) {
			
			if(to_index == this.to) {
				values = add(values, this.integer);
				return values;
			}
			else {
				
				int d = this.left.to;
				
				if(to_index <= d) {
					return this.left.multiply(from_index, to_index, coefficients, values);
				}
				else {
					ListIterator<Coefficient> current_values = this.left.multiply(from_index, d, coefficients, values);
					return this.right.multiply(d, to_index, coefficients, current_values);
				}
			}
			
		}
		else {
			
			int d = this.left.to;

			if(to_index == this.to) {
				
				if(from_index >= d) {

					return this.right.multiply(from_index, to_index, coefficients, values);
				}
				else {
					ListIterator<Coefficient> current_values = this.left.multiply(from_index, d, coefficients, values);
					return this.right.multiply(d, to_index, coefficients, current_values);
					
				}
				
			}
			else {
				
				
				if(from_index >= d) {
					return this.right.multiply(from_index, to_index, coefficients, values);
				}
				else if(to_index <= d){
					return this.left.multiply(from_index, to_index, coefficients, values);
				}
				else {
					ListIterator<Coefficient> current_values = this.left.multiply(from_index, d, coefficients, values);
					return this.right.multiply(d, to_index, coefficients, current_values);
					
				}
				
			}
			
		}
		
	}
	
	
	public Coefficient value() {
		return this.integer;
	}
	
	public PrimeMultiplicationNode left() {
		return this.left;
	}
	
	public PrimeMultiplicationNode right() {
		return this.right;
	}

	
	
	public static int[] rand_nk(int size, List<Coefficient> coefficients) {
		int k = new Random().nextInt(size/2) + 1;
		int n = new Random().nextInt(size - 2*k) + 2*k;
		
		k = 4_000_000;
		n = 9_990_000;
		
		Coefficient search_k = new Coefficient(BigInteger.valueOf(n-k));
		Coefficient search_n = new Coefficient(BigInteger.valueOf(n + 1));
		
		int i1 = Collections.binarySearch(coefficients, search_k, bigint_comparator);
		int i2 = Collections.binarySearch(coefficients, search_n, bigint_comparator);
		
		if (i1<0) i1 = -(i1 + 1);
		if (i2<0) i2 = -(i2 + 1);
//		System.out.println(k + " " + n + " " + i1 + " " + i2);
		return new int[] {i1, i2};
	}
	
	public static Comparator<Coefficient> bigint_comparator = new Comparator<Coefficient>() {  
	    @Override  
	    public int compare(Coefficient c1, Coefficient c2) {  
	        return Integer.compare(c1.base().intValue(), c2.base().intValue()); 
	    }  
	};


}
