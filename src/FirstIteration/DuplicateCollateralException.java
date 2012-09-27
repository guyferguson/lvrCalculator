package FirstIteration;

	/**
	 * An exception indicating a duplicate loan.
	 */
	@SuppressWarnings("serial")
	public class DuplicateCollateralException extends Exception {
	    
	    public DuplicateCollateralException(){
	        super();
	    }
		
	    public DuplicateCollateralException(String s){
	        super(s);
	    }
	}

