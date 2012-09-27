package FirstIteration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class Runner {
	static Portfolio p = new Portfolio();
	static Collaterals c = new Collaterals();
	static TreeMap<Integer,Double> lvrs= new TreeMap<Integer,Double>();
	static ArrayList<Integer> trickyLoans = new ArrayList<Integer>();
	static ArrayList<Integer> trickyBad = new ArrayList<Integer>();
	
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
			// Echo the number of loans in portfolio
		}


		try {
			c = CollateralReader.read("Java Collaterals.txt");
		} catch (IOException e) {
			System.out.println("Could not open file" + e.getMessage());
			// Echo the number of loans in portfolio
		}
		System.out.println("There are " + p.getLoansCount() + " loans in the portfolio");

		System.out.println("They're valued at $" + p.getValue()/100);

		System.out.println("There are " + c.getCollateralsCount() + " collaterals in the portfolio");

		System.out.println("They're valued at $" + c.getTotalValue() + " ");

		fullyCrossed();
	}

	public static void fullyCrossed() {

		//Look for loans fully cross collateralised
		//Make a copy of the portfolio and loop through a trimmed version
		//of it, deducting each compared and found loan

		Portfolio q = new Portfolio();

		q=p.clone();
		System.out.println("New portfolio is size " + q.getLoansCount());
		System.out.println("Used value 102086 is " + c.getUsedValue(102086));

		//q.removeLoan(319000065);
		//System.out.print("After removing 319000065, new portfolio is " + q.getLoansCount() + " and old portfolio is " + p.getLoansCount()+ "\n");

		Collection<LoanAccount> lnaccs;
		lnaccs = q.getValues();
		Set<Integer> lns = q.getKeys();
		int lvrCalculated=0;
		// Start looping through all loans
		for (LoanAccount ln: lnaccs){
			//System.out.println("Working with " + ln.getAccNum());
			// Get all the loan accounts associated with this loan through its 
			// collaterals
			ArrayList<Integer> allLinkedLoans = new ArrayList<Integer>();
			for (int j:ln.getCollaterals()) {
				for (int cl: c.getLoans(j)){
					if (!(allLinkedLoans.contains(cl))){
						allLinkedLoans.add(cl);
					}
				}
			}
			//Create an array of all collaterals
			ArrayList<Integer> allLinkedCollaterals = new ArrayList<Integer>();
			for (int j:allLinkedLoans) {
				for (int cl: p.getLoanAccount(j).getCollaterals()){
					if (!(allLinkedCollaterals.contains(cl))){
						allLinkedCollaterals.add(cl);
					}
				}
			}

			//System.out.println(" All linked loans :" + allLinkedLoans.toString());

			if (sharedFully(allLinkedLoans)){
				int totCollVal=0;
				for (int i=0;i<allLinkedCollaterals.size();i++){
					totCollVal+=c.getCollValue(ln.getCollaterals().get(i));
					//System.out.println("Using a total collateral value of $"+totCollVal);
				}

				float totLoanBalance=0;
				for (int i=0;i<allLinkedLoans.size();i++){
					totLoanBalance+=p.getLoanAccount(allLinkedLoans.get(i)).getBalance();
					//	System.out.println("Using a total loan balance of $"+totLoanBalance);
				}
				lvrCalculated+=1;	
				//	System.out.println(allLinkedLoans.toString());
				double lvr = calcLVR(totLoanBalance,totCollVal);
				lvrs.put(ln.getAccNum(),lvr);
				System.out.println(ln.getAccNum() + " LVR="+ lvr);
				//	}
			}
			else {

			//	System.out.println("Complex loan: " + ln.getAccNum());
				
				// A complex loan is one whereby no other loan exists that shares
				// just some of its collaterals. Not being a complex loan means
				// that there is another loan out there that uses some of its 
				// collaterals
				if (!(complexLoan(ln))) {
					// See if the loan we are about to add has any shared
					// collateral loans in the portfolio already
				//	System.out.println("About to add " + trickyLoans.size());
		
					trickyLoans.add(ln.getAccNum());
					
					
				}
			}
		}
		
		// Now check the tricky loans
		//System.out.println("There are still " + trickyLoans.size() + " tricky bastards");
			for (int trickyLoan: trickyLoans){
					LoanAccount trickyLoanA = p.getLoanAccount(trickyLoan);
					for (int trickyComp: trickyLoans){
						LoanAccount ln = p.getLoanAccount(trickyComp);
							
				if (trickyLoanA.isLinked(ln) && (!(trickyLoan==ln.getAccNum()))) {
						//They are linked, so work out which to keep
					if (trickyLoanA.getCollaterals().size() > ln.getCollaterals().size()) {
						if (!(trickyBad.contains(trickyLoan))){
						trickyBad.add(trickyLoan); 
						}
						}
						
					}
					}
				}
			//now remove those bad ones
		//	System.out.println("Bad ones size = " + trickyBad.size());
			for (int bad:trickyBad) {
			//	System.out.println("Removing index" + bad);
	}
				trickyLoans.removeAll(trickyBad);
			//}
			
	//	System.out.println("After editing there are " + trickyLoans.size() + " tricky bastards");
		ArrayList<LoanAccount> trickies = new ArrayList<LoanAccount>();

		// Create a set of the tricky LoanAccounts
		for (int i:trickyLoans){
			trickies.add(p.getLoanAccount(i));
		}


		for (int i:trickyLoans){
			//Orig coll value
			int chngdVal = 0;
			for (int j:p.getLoanAccount(i).getCollaterals()){
				chngdVal+=c.getUsedValue(j);
			}
			//See if this tricky loan has another tricky loan with same collaterals..
			int trickyBalance=0;
			ArrayList<LoanAccount> trickyShares=new ArrayList<LoanAccount>();
			for (LoanAccount ln:trickies){
				if (p.getLoanAccount(i).hasSameCollaterals(ln)) {
					trickyShares.add(ln);
					trickyBalance+=ln.getBalance();
				//	System.out.println("Tricky balance is now $"+ trickyBalance/100);
				}
			}
			int tmpLoanBalance = trickyBalance/100;
			//Decrement the collateral value for when we use these collaterals 
			//again in badLoans
			
			for (int coll:p.getLoanAccount(i).getCollaterals()){
					//Recognise how much of the collateral value has been 'used up'
				if (tmpLoanBalance > c.getUsedValue(coll)) {
					tmpLoanBalance -=c.getUsedValue(coll);

					// Decrease this collateral to zero
				//	System.out.println(trickyBalance/100 + " is the amt..Decreasing " + i + " coll " + coll + " value by " + c.getUsedValue(coll)/trickyShares.size());
					c.getCollateral(coll).decUsedValue(c.getUsedValue(coll)/trickyShares.size());
				}
				else {
					//System.out.println(trickyBalance/100 + " is the amt..Decreasing " + i + " coll " + coll + " value by " + (int) tmpLoanBalance/trickyShares.size());	
					c.getCollateral(coll).decUsedValue((int) tmpLoanBalance/trickyShares.size());
					tmpLoanBalance = 0;
				}
			}
			
			double lvr;
			lvr=(double) Math.round((trickyBalance/chngdVal)*100)/100;
			//Now we have an array, trickyShares, of loans with same collaterals
			lvrs.put(p.getLoanAccount(i).getAccNum(),lvr);

			System.out.print(p.getLoanAccount(i).getAccNum() + " LVR=" + lvr + "\n");




			//System.out.println(i + " Bad Original coll " + " used value=" + chngdVal);
		}
		
		ArrayList<LoanAccount> baddies = new ArrayList<LoanAccount>();

		System.out.println("Now firing through the bad loans");
		for (int i:trickyBad){
			baddies.add(p.getLoanAccount(i));
		}
		for (int i:trickyBad){
			//Orig coll value
			int chngdVal = 0;
			for (int j:p.getLoanAccount(i).getCollaterals()){
				chngdVal+=c.getUsedValue(j);
			}
			//See if this tricky loan has another tricky loan with same collaterals..
			int trickyBalance=0;
			ArrayList<LoanAccount> trickyShares=new ArrayList<LoanAccount>();
			for (LoanAccount ln:baddies){
				if (p.getLoanAccount(i).hasSameCollaterals(ln)) {
					trickyShares.add(ln);
					trickyBalance+=ln.getBalance();
				//	System.out.println("Tricky balance is now $"+ trickyBalance/100);
				}
			}
			int tmpLoanBalance = trickyBalance/100;
			//Decrement the collateral value for when we use these collaterals 
			//again in badLoans
			
			for (int coll:p.getLoanAccount(i).getCollaterals()){
					//Recognise how much of the collateral value has been 'used up'
				if (tmpLoanBalance > c.getUsedValue(coll)) {
					tmpLoanBalance -=c.getUsedValue(coll);

					// Decrease this collateral to zero
				//	System.out.println(trickyBalance/100 + " is the amt..Decreasing " + i + " coll " + coll + " value by " + c.getUsedValue(coll)/trickyShares.size());
					c.getCollateral(coll).decUsedValue(c.getUsedValue(coll)/trickyShares.size());
				}
				else {
				//	System.out.println(trickyBalance/100 + " is the amt..Decreasing " + i + " coll " + coll + " value by " + (int) tmpLoanBalance/trickyShares.size());	
					c.getCollateral(coll).decUsedValue((int) tmpLoanBalance/trickyShares.size());
					tmpLoanBalance = 0;
				}
			}
			
			double lvr;
			lvr=(double) Math.round((trickyBalance/chngdVal)*100)/100;
			//Now we have an array, trickyShares, of loans with same collaterals
			lvrs.put(p.getLoanAccount(i).getAccNum(),lvr);

			System.out.print(p.getLoanAccount(i).getAccNum() + " LVR=" + lvr + "\n");




		//	System.out.println(i + " Bad Original coll " + " used value=" + chngdVal);
		}

	
		System.out.println(" We have calculated " + lvrs.size() + " LVRs");

	}

	// Loop through all the loans linked together and see if they 
	// all share the same collaterals, in which case we can calculate
	// LVR quite simply
	public static Boolean sharedFully(ArrayList<Integer> loans) {
		// See if all loans in an array share the same collateral
		if (loans.size() == 1) return true;
		int lns=1;
		for (int i: loans){
			LoanAccount comp = p.getLoanAccount(loans.get(lns));
			if (!(p.getLoanAccount(i).hasSameCollaterals(comp))) return false;
			if (lns<loans.size()-1){
				lns+=1;
			}
		}
		return true;
	}

	/*
	 *  A method to handle loans with non-equally shared collaterals, e.g. this situation:
	 *  	Loan A		Coll A
	 *  				Coll B
	 *      Loan B		Coll A
	 *      			Coll B
	 *      Loan C		Coll A
	 *      			Coll B
	 *      			Coll C
	 *      
	 *      In the above, Loan A and B will have the same LVR
	 *      Loan C will have an LVR calculated by:
	 *      
	 *      Loan C/(Coll C + Coll A + Coll B -Loan A - Loan B)
	 */
	public static Boolean complexLoan(LoanAccount complex){
		//We start off being handed a loan - see if it shares the same collaterals
		// with any other loans - make an array of loans it shares equally with.
		// In the above example this should return Loan B if handed Loan A

		Collection<LoanAccount> lnaccs;
		lnaccs = p.getValues();

		//An array to hold sub-set of all loans with same collaterals
		ArrayList<Integer> sameCollaterals = new ArrayList<Integer>();
		for (LoanAccount ln: lnaccs){
			if (complex.hasSameCollaterals(ln)){
				sameCollaterals.add(ln.getAccNum());
			}
		}
		//System.out.println(complex.getAccNum() + " shares collaterals with " + sameCollaterals.toString());
		// See if there are any loans that share a subset of these collaterals
		// if not, calculate LVR
		for (LoanAccount ln: lnaccs){
			// See if there is another loan that has some but not all of this loan's collaterals
			if (complex.sharesSomeCollaterals(ln)){
				//System.out.println("Another loan, " + ln.getAccNum() + " shares some not all collaterals with " + complex.getAccNum());
				return false;
			}
		}
		//If we get here, there are no loans with a subset of this loan's collaterals

		// So work out these simpler LVRs:
		float totLoanBalance = 0;
		float tmpLoanBalance = 0;
		int totCollVal=0;
		for (int ln:sameCollaterals){
			totLoanBalance+=p.getLoanAccount(ln).getBalance();
		}	
		tmpLoanBalance = totLoanBalance/100;
		//System.out.print("About to calc LVR for "+complex.getAccNum());
		// Now adjust the used values of the collaterals by the sum of the loans
		for (int coll:complex.getCollaterals()){
			totCollVal+=c.getCollValue(coll);
			//Recognise how much of the collateral value has been 'used up'
			if (tmpLoanBalance > c.getUsedValue(coll)) {
				tmpLoanBalance-=c.getUsedValue(coll);

				// Decrease this collateral to zero
				//System.out.println(tmpLoanBalance + " is the amt..Decreasing " + complex.getAccNum()+ " coll " + coll + " value by " + c.getUsedValue(coll)/sameCollaterals.size());
				c.getCollateral(coll).decUsedValue(c.getUsedValue(coll)/sameCollaterals.size());
			}
			else {
				//System.out.println(tmpLoanBalance + " is the amt..Decreasing " + complex.getAccNum()+ " coll " + coll + " value by " + (int) tmpLoanBalance/sameCollaterals.size());	
				c.getCollateral(coll).decUsedValue((int) tmpLoanBalance/sameCollaterals.size());
				tmpLoanBalance = 0;
			}
		}
		double lvr=calcLVR(totLoanBalance,totCollVal);
		System.out.println(complex.getAccNum() + " LVR=" + lvr);
		lvrs.put(complex.getAccNum(),lvr);
		return true;
	}

	/*
	 * A method to calculate an LVR to two decimal places
	 * @invariants - loans and coll >0
	 * 				- loans < coll
	 */
	private static double calcLVR(float loans, int colls) {
		double lvr = (double) Math.round((loans/colls)*100)/100;	
		return lvr;
	}
	
	/*
	 *  A method to handle loans with non-equally shared collaterals, e.g. this situation:
	 *  	Loan A		Coll A
	 *  				Coll B
	 *      Loan B		Coll A
	 *      			Coll B
	 *      Loan C		Coll A
	 *      			Coll B
	 *      			Coll C
	 *      
	 *      In the above, Loan A and B will have the same LVR
	 *      Loan C will have an LVR calculated by:
	 *      
	 *      Loan C/(Coll C + Coll A + Coll B -Loan A - Loan B)
	 */
	
}

