package FirstIteration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Runner {
	static Portfolio p = new Portfolio();
	static Collaterals c = new Collaterals();
	static TreeMap<Integer,Double> lvrs= new TreeMap<Integer,Double>();
	static TreeMap<Integer,Double> lvrsTemp= new TreeMap<Integer,Double>();
	static ArrayList<Integer> trickyLoans = new ArrayList<Integer>();
	static ArrayList<Integer> trickyBad = new ArrayList<Integer>();
	static TreeMap<Integer,Integer> collDec= new TreeMap<Integer,Integer>();
	static Collection<LoanAccount> lnaccs;
	static int its=0;
	/**
	 * @param args
	 * @throws FormatException 
	 */
	public static void main(String[] args) throws FormatException {
		/* read accounts into portfolio
		 * 
		 */
		try {
			p = LoanAccountReader.read("LoanAccounts Java.txt");
		} catch (IOException e) {
			System.out.println("Could not open file" + e.getMessage());
		}

		try {
			c = CollateralReader.read("Java Collaterals.txt");
		} catch (IOException e) {
			System.out.println("Could not open file" + e.getMessage());
		}
		System.out.println("There are " + p.getLoansCount() + " loans in the portfolio");

		System.out.println("They're valued at $" + p.getValue()/100);

		System.out.println("There are " + c.getCollateralsCount() + " collaterals in the portfolio");

		System.out.println("They're valued at $" + c.getTotalValue() + " ");

		takeTwo();
	}

	public static void takeTwo() {
		// Copy the portfolio

		// Create a collection containing the values

		lnaccs = p.getValues();

		// When an account is calculated, we put the result in lvrs, so
		// when that reaches the portfolio size, we are finished
		while (!(lvrs.size()==lnaccs.size())) {
			// Count the iterations of the portfolio
			its+=1;
			System.out.println(" Iteration "+its+" Array size = " + 
					lnaccs.size() + " dealt with " + lvrs.size());
			for (LoanAccount ln:lnaccs) {
				// First check that this is not a loan we have previously calculated
				// which is in the 'ignore' list (in lvrs)
				if (!(lvrs.containsKey(ln.getAccNum()))) {


					// Check that there are no other loans with smaller 
					// collateral sets than the one we are looking at now
					if (!(existsSubLoan(ln))) {
						// Is there another loan with same collaterals exactly?
						// If there is, create ArrayLists of loans and collaterals
						ArrayList<LoanAccount> groupedLoans = new ArrayList<LoanAccount>();
						ArrayList<Collateral> groupedCollaterals = new ArrayList<Collateral>();

						for (LoanAccount lnGrp: lnaccs) {
							if (ln.hasSameCollaterals(lnGrp)) {
								groupedLoans.add(lnGrp);
								for (int collID: lnGrp.getCollaterals()) {
									if (!(groupedCollaterals.contains(c.getCollateral(collID)))) {
										groupedCollaterals.add(c.getCollateral(collID));
									}
								}
							}
						}
						totalUp(groupedLoans,groupedCollaterals);
					}
				}
			}
			// Now that we have finished an iteration, remove the loans we resolved
			// from the list of loans to work on next iteration. 
			lvrs.putAll(lvrsTemp);
			lvrsTemp.clear();

			// Decrement all the collaterals that need decrementing
			// and then clear it out
			for (Map.Entry<Integer,Integer> entry : collDec.entrySet()) {
				c.getCollateral(entry.getKey()).decUsedValue(entry.getValue());			
			}
			collDec.clear();
		}
		// Now we've finished, display each LVR
		for (Integer ln: lvrs.keySet())
		{
			System.out.println(ln + "," + lvrs.get(ln));
		}
		System.out.println(" Finished");
	}

	/*
	 * A method that takes a group of loans and another group of collaterals
	 * works out LVR and then decrements collateral value
	 * and then removes the accounts from list we use. 
	 */	
	public static void totalUp(ArrayList<LoanAccount> lns, ArrayList<Collateral> cls) {
		float totLoanBalance = 0;
		float tmpLoanBalance = 0;
		int totCollVal=0;

		// Work out loans value
		for (LoanAccount ln:lns){
			totLoanBalance+=ln.getBalance();
		}
		tmpLoanBalance = totLoanBalance/100;

		for (Collateral cl:cls) {
			int toDec=0;
			System.out.println(cl);
			if (cl==null){break;};
			totCollVal+=cl.getUsedValue();
			//		System.out.println("Used val = " + cl.getUsedValue());
			if (tmpLoanBalance > cl.getUsedValue()) {
				tmpLoanBalance -= cl.getUsedValue();
				// Wipe this collateral back to zero
				toDec+=(cl.getUsedValue());
				//System.out.println("Knocking $" + toDec + " off " + cl.getID());
			}
			else {
				//deduct the loan balance from this collateral
				//	System.out.println("Partial reduction in coll " + cl.getID() + " val of " + ((int) tmpLoanBalance/cls.size()) + "\n");
				toDec+=((int) tmpLoanBalance); 
				tmpLoanBalance = 0;
			}

			// Add this decrement amount to collaterals to be decremented at 
			// end of iteration, if it's not there already
			if (!(collDec.containsKey(cl.getID()))){
				collDec.put(cl.getID(),toDec);
			}
			//	System.out.println(" "+lns.toString()+ " coll val used= $" + cl.getUsedValue()+ " number collaterals = "+cls.size());
		}
		// Record these LVRs
		for (LoanAccount ln: lns) {
			lvrsTemp.put(ln.getAccNum(), calcLVR(totLoanBalance,totCollVal));
		}		
	}
	/*
	 * A method that tells, when handed a LoanAccount object, if there is
	 * another loan in this portfolio that is securitised by a subset of that
	 * LoanAccount's collateral set - i.e. if handed LoanA with Coll A and Coll B 
	 * as security, if there exists a LoanB with Coll B as security, then this method
	 * returns True
	 */
	public static Boolean existsSubLoan(LoanAccount myLoan) {
		ArrayList<Integer> myCollaterals = myLoan.getCollaterals(); 
		//System.out.println("Iteration " + its + " checking "+  myLoan.getAccNum());
		for (LoanAccount loanToCheck: lnaccs) {

			//System.out.println("many" + loanToCheck.getAccNum());
			ArrayList<Integer> othersCollaterals = loanToCheck.getCollaterals();

			if (myCollaterals.size()>othersCollaterals.size() && (!(lvrs.containsKey(loanToCheck.getAccNum())))) {
				//Is there some link between these two loans?
				if (myLoan.isLinked(loanToCheck)){
					for (Integer othrColl: othersCollaterals){
						//	System.out.println("Iteration "+its+"CHECKING if " + myLoan.getAccNum()+" " + myCollaterals.toString()+" contains " + othrColl);
						if (!(myCollaterals.contains(othrColl))) return false;
					}
					//	System.out.println(myLoan.toString() +" is a superset of " + loanToCheck.toString());
					return true;
				}
			}	
		}
		return false;	
	}

	/*
	 * A method to calculate an LVR to two decimal places
	 * @invariants - loans and coll >0
	 * 				- loans < coll
	 */
	private static double calcLVR(float loans, int colls) {
		return (double) Math.round((loans/colls)*100)/100;
	}

}

