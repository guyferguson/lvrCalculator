package FirstIteration;
import java.io.*;
import java.util.*;

public class CollateralReader {

public static Collaterals read(String fileName) throws IOException, FormatException {
	FileReader fr = null;
	BufferedReader bf = null;

	// create a hash set to hold Loan Accounts from file
	Collaterals collaterals = new Collaterals();

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
		String[] collateral = line.trim().split("\t");
			//A valid collateral contains 5 pieces of information
		if (!(collateral.length==5)){
			System.out.println("Not right length -" + collateral.length);
		}
		
		// Set variables to hold loan account properties
		
		int collId;
		int collValue;
		String collDesc;
		boolean crossCollateralised;
		int zip;
		try{
			collId = Integer.parseInt(collateral[0]);
			collDesc = collateral[1];
			collValue = Integer.parseInt(collateral[2]);
			zip = Integer.parseInt(collateral[4]);
		}
		catch (Exception ex){
			// This would catch instances such as '319000123 D 45874"
			throw new FormatException();
		}
	
		//collateral must be positive integers
		if (collId < 0 ) {
			throw new FormatException();
		}
		
		// Loans are an array, 
		ArrayList<Integer> loans = new ArrayList<Integer>();
		loans.add(Integer.parseInt(collateral[3]));
		
	    // Arriving here means the collateral is valid, instantiate it 
		// and add it to our Collaterals if not there already
		
		Collateral cTemp;
		try {
		// Create a Loan Account object from parsed data	
			cTemp = new Collateral(collId,collDesc,collValue,collValue,loans,false,zip);
			collaterals.process(cTemp);	
		
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
return collaterals;
}

}
