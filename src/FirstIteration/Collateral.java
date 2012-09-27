package FirstIteration;

import java.util.ArrayList;

public class Collateral {
	
	private int collId;
	private String collDesc;
	private int collValue;
	private int usedValue;
	private ArrayList<Integer> loans;
	private Boolean crossCollateralised;
	private int zip;
	
	/**
	 * @invariant collId cannot be null
	 * 				collDesc cannot be null
	 * 				collValue>0
	 * 				loans not null, must have .size>0
	 * 				crossCollateralised not null
	 */
	public Collateral(int collId, String collDesc, int collValue, int usedValue
			, ArrayList<Integer> loans, Boolean crossCollateralised, int zip) 
		throws NullPointerException, CollateralException {
		if (collDesc==null|crossCollateralised==null|loans==null) {
			throw new NullPointerException();
		}
		if (collId<1|collValue<1|usedValue<1) {
			throw new CollateralException("Invalid collateral ID or value");
		}
		if (!(collValue==usedValue)) {
			throw new CollateralException("Used and original value different");
		}
		this.collId=collId;
		this.collDesc=collDesc;
		this.collValue=collValue;
		this.usedValue=usedValue;
		this.loans=loans;
		this.crossCollateralised=crossCollateralised;
		this.zip = zip;
	}

	public int getValue() {
		return collValue;
	}
	
	public String getDesc() {
		return collDesc;
	}
	
	public int getUsedValue() {
		return usedValue;
	}
	
	public void decUsedValue(int amount){
		this.usedValue-=amount;
	}
	
	public int getID() {
		return collId;
	}
	
	public Boolean isCrossed() {
		return crossCollateralised;
	}
	
	public ArrayList<Integer> getLoans() {
		return loans;
	}
	
	public String toString() {
		String tmpLoans = "";
		for (int i=0;i<this.loans.size();i++) {
			tmpLoans+=this.loans.get(i) + ",";
		}
		return "Collateral ID : " + collId + " Description : " + collDesc + " Valued: $" +
		collValue + " Used Value : $" + usedValue + " over loans " + tmpLoans;
	}
	
}

