package FirstIteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/* A class to handle collaterals and their attached loans and used values */

public class Collaterals {

	private TreeMap<Integer,Collateral> collaterals;
	
	public Collaterals() {
		collaterals = new TreeMap<Integer,Collateral>();
	}
	
	/**
	 *  Handles adding collateral to collaterals
	 * @param lnAcct - valid loan account number
	 * @throws NullPointerException
	 * @throws DuplicateLoanException
	 */
	public void process(Collateral coll) throws NullPointerException,
	DuplicateCollateralException {
		if (coll == null) throw new NullPointerException("Invalid collateral: null");
		
		//If coll already exists, just update loans
		int collId = coll.getID();
		
		if (collaterals.containsKey(collId)) {			
			for (int i = 0;i< coll.getLoans().size();i++){
		        collaterals.get(collId).getLoans().add(coll.getLoans().get(i));
		        }
			return;
		}		collaterals.put(collId,coll);
	
	}
	/**
	 * 
	 * @return the number of collaterals in the collaterals
	 */
	public int getCollateralsCount() {
		return collaterals.size();
	}
	
	// Returns a collateral 
	public Collateral getCollateral(int key){
		return collaterals.get(key);
	}
	
	public int getCollValue(int collId){
		if (collaterals.containsKey(collId)){
			return collaterals.get(collId).getValue();
		}
		return 0;
	}
	
	public int getUsedValue(int collId){
		if (collaterals.containsKey(collId)){
			return collaterals.get(collId).getUsedValue();
		}
		return 0;
	}
	
	public long getTotalValue() {
		long totVal = 0;
		for (Map.Entry<Integer,Collateral> entry : collaterals.entrySet()){
			totVal+=entry.getValue().getValue();
		}
		return totVal;
	}
	
	/* A method to return an array of all loans attached to a set of coll ids
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	public ArrayList<Integer> getLoans(int collID) {
		ArrayList<Integer> allLoansForCollateral = new ArrayList<Integer>();
		for (Map.Entry<Integer,Collateral> entry : collaterals.entrySet()){
			if (entry.getValue().getID() == collID){
				for (Integer ln: entry.getValue().getLoans()){
					allLoansForCollateral.add(ln);
				}
			}
		}
		return allLoansForCollateral;
	}
	
	public String toString() {
		return "There are " + this.collaterals.size() + " collaterals in the portfolio";
	}

}
