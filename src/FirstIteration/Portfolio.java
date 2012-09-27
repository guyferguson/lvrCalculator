package FirstIteration;

import java.util.*;

public class Portfolio {

	//private ArrayList<LoanAccount> portfolio;
	private TreeMap<Integer,LoanAccount> portfolio;
	
	public Portfolio() {
		portfolio = new TreeMap<Integer,LoanAccount>();
	}
	
	/**
	 *  Handles adding loan accounts to portfolio, sorted in order
	 *  of date funded
	 * @param lnAcct - valid loan account number
	 * @throws NullPointerException
	 * @throws DuplicateLoanException
	 */
	public void process(LoanAccount lnAcct) throws NullPointerException {
		if (lnAcct == null) throw new NullPointerException("Invalid loan account: null");
		
		int accNo = lnAcct.getAccNum();
		//If loan already exists, just update collateral
		if (portfolio.containsKey(accNo)) {
			for (int i = 0;i< lnAcct.getCollaterals().size();i++){
		        portfolio.get(accNo).getCollaterals().add(lnAcct.getCollaterals().get(i));
		        }
			return;
		}
		portfolio.put(accNo, lnAcct);
	}
	
	/**
	 * 
	 * @return the number of loans in the portfolio
	 */
	public int getLoansCount() {
		return portfolio.size();
	}
	
	// Returns a LoanAccount object for a loan account number
	public LoanAccount getLoanAccount(int key){
		return portfolio.get(key);
	}
	
	public Portfolio clone() {
		Portfolio copy = new Portfolio();
		for (Map.Entry<Integer,LoanAccount> entry : portfolio.entrySet()){
			copy.process(entry.getValue());
		}
		return copy;	
	}
	

	public void removeLoan(int lnAcct){
		portfolio.remove(lnAcct);
	}
	
	public long getValue() {
		long totVal = 0;
		
		for (Map.Entry<Integer,LoanAccount> entry : portfolio.entrySet()){
			 totVal+= entry.getValue().getBalance() ;
		}
		return totVal;
	}
	
	public String toString() {
		return "There are " + this.portfolio.size() + " loans in the portfolio";
	}

	public Set<Integer> getKeys() {
		return portfolio.keySet();
	}
	public Collection<LoanAccount> getValues() {
		return portfolio.values();
	}
	

	

	
}
