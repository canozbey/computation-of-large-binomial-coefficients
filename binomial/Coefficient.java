package binomial;

import java.math.BigInteger;

public class Coefficient implements Comparable<Coefficient>{

	
	public BigInteger base = null;
	public int power = 1;
	double bitlength = 0.0;
	double cumulative_bitlength = 0.0;
	public int hash = 0;
	
	
	public Coefficient(){
		this.base = BigInteger.valueOf(1);
		this.bitlength = 0.0;
		this.cumulative_bitlength = 0.0;
		this.hash = 1;
	}
	
	Coefficient(BigInteger base){
		this.base = base;
		this.bitlength = Math.log(this.base.intValue());
		this.setHash();
	}
	
	public Coefficient(BigInteger base, double bitlength){
		this.base = base;
		this.bitlength = bitlength;
		this.setHash();
	}
	
	Coefficient(BigInteger base, int power){
		this.base = base;
		this.power = power;
		this.bitlength = Math.log(this.base.intValue()) * this.power;
		this.setHash();
	}
	
	Coefficient(Coefficient c){
		this.base = c.base();
		this.power = c.power();
		this.bitlength = c.bitLength();
		this.hash = c.hash();
		this.cumulative_bitlength = c.cumulativeBitLength();
	}
	
	
	Coefficient(double cumulative_bitlength){
		this.cumulative_bitlength = cumulative_bitlength;
	}
	
	public BigInteger base() {
		return this.base;
	}
	
	public int power() {
		return this.power;
	}
	
	public int hash() {
		return this.hash;
	}
	
	public double bitLength() {
		return this.bitlength;
	}
	
	public double cumulativeBitLength() {
		return this.cumulative_bitlength;
	}
	
	public void setCumulativeBitLength(double cumulative_bitlength) {
		this.cumulative_bitlength = cumulative_bitlength;
	}
	
	public void setHash() {
		this.hash = new Double(this.bitlength).hashCode();
	}
	
	public BigInteger pow() {
		if(this.power == 1) return this.base;
		this.base = this.base.pow(this.power);
		this.power = 1;
		return this.base;
	}
	
	public Coefficient multiply(Coefficient coefficient) {
		BigInteger base = this.base.multiply(coefficient.base());
		double bitlength = this.bitlength + coefficient.bitlength;
		return new Coefficient(base, bitlength);
	}
	
	@Override
    public int hashCode() {
		return this.hash;
    }
	
	@Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Coefficient)) {
            return false;
        }

        Coefficient c = (Coefficient) o;

        return this.bitlength == c.bitLength();
    }
	
	@Override
	public int compareTo(Coefficient other) {
		return Double.compare(this.bitlength, other.bitLength());
	}

}

