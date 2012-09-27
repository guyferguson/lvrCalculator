package FirstIteration;

import java.util.ArrayList;

/** A class to represent PHX loan accounts
 * 
 * @author gferguson
 * @invariants - accountNumber cannot be null
 * 				- collateralsAttached must not be null
 * 				- funded date must be positive
 * 				- balance must be >= $1
 */
public class LoanAccount {

	private int accNumber;
	private ArrayList<Integer> collaterals;
	private int funded;
	private int balance;
	
	public LoanAccount(int accNumber, int funded, ArrayList<Integer> collaterals, int balance) 
			throws NullPointerException {
		if (accNumber<1l)
			throw new NullPointerException("Empty account number: null");
		if (collaterals==null)
			throw new NullPointerException("No collaterals: null");
		//if (funded < 1)
		//	throw new NullPointerException("Funded date: invalid");
		if (balance < 0)
			throw new NullPointerException("Loan balance: invalid");
				
		this.accNumber = accNumber;
		this.collaterals = collaterals;
		this.balance = balance;
		this.funded = funded;
	}
	
	/**
	 * Add a collateral to the arraylist of an existing loan
	 * 
	 * @invariants - no duplicate collaterals added to a loan
	 */
	public void addCollateral(int collId) {
			if (!(this.collaterals.contains(collId))) {
				this.collaterals.add(collId);		
			}
	}
	
	public int getAccNum() {
		return accNumber;
	}
	
	public int getDateFunded() {
		return funded;
	}
	
	/**
	 * Balance is stored in cents
	 * @return
	 */
	public int getBalance() {
		return balance;
	}
	
	/* TODO this is not safe as it returns a modifiable array */
	public ArrayList<Integer> getCollaterals() {
		return collaterals;
	}
	
	/*
	 * A method that tells us if there is any link between these two loans
	 */
	public Boolean isLinked(LoanAccount otherLoan){
		for (int coll: this.collaterals) {
			if (otherLoan.getCollaterals().contains(coll)) return true;
		}
		return false;
		
	}
	/**
	 * Returns true if the argument loan has exactly same collaterals (no more
	 * or less) in any order as this object.
	 * @return
	 */
	public Boolean hasSameCollaterals(LoanAccount otherLoan) {
		/* First, check that the two LoanAccounts have the same number
		 *  of collaterals */
		if (!(this.collaterals.size() == otherLoan.getCollaterals().size()))
			return false;
		/* Then loop through each collateral in this LoanAccount and check that 
		 * compared LoanAccount has that collateral.
		 */
		for (int myCollaterals: this.collaterals) {
			if (!(otherLoan.collaterals.contains(myCollaterals))) return false;
			}
		/*
		 * If we get here, the two LoanAccounts are same size and have the same 
		 * collaterals, so they meet this definition of equality
		 */
		return true;
	}
	
	/*
	 * Tells if there are any loans that have a subset of these collaterals
	 * but not all are shared
	 * e.g Loan A   Coll A
	 *     Loan B   Coll A
	 *     			Coll B
	 *     Loan B sharesSomeCollaterals(LoanA) would return True, i.e
	 *     there is a loan out there (Loan A) that has a subset of Loan B's 
	 *     collaterals
	 *     
	 *     Loan A sharesSomeCollaterals(Loan B) would return false
	 */
	public Boolean sharesSomeCollaterals(LoanAccount complexLoan) {
	
		/* Then loop through each collateral in this LoanAccount and check that 
		 * compared LoanAccount has that collateral.
		 */
		int unSharedColl=0;
		for (int myCollaterals: this.collaterals) {
			if (!(complexLoan.collaterals.contains(myCollaterals))) {
				unSharedColl+=1;
			}
			}
		if (unSharedColl > 0 && (!(unSharedColl==this.collaterals.size()))) return true; 
		else return false;
	}

	public String toString() {
		
		String tmpColl = "";
		for (int i=0;i<this.collaterals.size();i++) {
			tmpColl+=this.collaterals.get(i) + ",";
		}
		return "Account " + accNumber + " : Collaterals " + tmpColl + " Balance: $" +
		balance/100 + " Funded: " + funded;
		
	}
}
