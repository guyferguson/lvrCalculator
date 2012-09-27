package FirstIteration;

import java.io.*;
import java.util.*;

public class LoanAccountReader {

public static Portfolio read(String fileName) throws IOException, FormatException {
	FileReader fr = null;
	BufferedReader bf = null;

	// create a hash set to hold Loan Accounts from file
	Portfolio portfolio = new Portfolio();

	//Wrap the reader in a Try so we can close the object if we get an IO
	//Exception
	try{
		fr = new FileReader(fileName);
		bf = new BufferedReader(fr);

		String line = bf.readLine();
		while(line!=null) {
			//Throw FormatException if line is an empty string
			if (line.length()==0){
				throw new FormatException();
				}
		
		//Throw FormatException if leading or trailing spaces
			if (line.startsWith(" ")|(line.endsWith(" "))){
				throw new FormatException();
				}
		//Arriving here we know we have a non-empty line, so parse
		String[] loanaccount = line.trim().split("\t");
			//A valid loan account contains 3 pieces of information
		if (!((loanaccount.length==4)|(loanaccount.length==3))){
			System.out.println("Not right length -" + loanaccount.length);
		}
		
		// Handle Excel dates
		
		//GregorianCalendar gc = new GregorianCalendar(1900, Calendar.JANUARY,1);
		//gc.add(Calendar.DATE, 38838-1);


		// Set variables to hold loan account properties
		int accountNumber; 
		int collateral;
		int funded;
		Float balTmp;
		float balance;
		try{
			accountNumber = Integer.parseInt(loanaccount[0]);
			collateral=  Integer.parseInt(loanaccount[1]);
			funded = Integer.parseInt(loanaccount[2]);
			try{
			
			balTmp = Float.valueOf(loanaccount[3])*100;
			balance = Math.round(balTmp);
			}
			catch (Exception ex) {balance =0;}
			finally{}
		}
		catch (Exception ex){
			// This would catch instances such as '319000123 D 45874"
			throw new FormatException();
		}
	
		//collateral must be positive integers
		if (collateral < 0 ) {
			throw new FormatException();
		}
		
		// Collaterals is an array, 
		ArrayList<Integer> collaterals = new ArrayList<Integer>();
		collaterals.add(collateral);
		
	    // Arriving here means the loan account is valid, instantiate it 
		// and add it to our Portfolio if not there already
		
		LoanAccount lTemp;
		try {
		// Create a Loan Account object from parsed data	
			lTemp = new LoanAccount(accountNumber,funded,collaterals,balance);
			portfolio.process(lTemp);	
		
		}
		
		catch (Exception ex){
			System.out.println(ex.toString());
			}
		// Move on to the next line in the file
		line = bf.readLine();
	}
}
finally
{
//Ensure that the reader is closed, no matter what.
	if (fr !=null){
		bf.close();
		}
	}
return portfolio;
}

}
