package FirstIteration;

	/**
	 * An exception indicating a duplicate loan.
	 */
	@SuppressWarnings("serial")
	public class DuplicateLoanException extends Exception {
	    
	    public DuplicateLoanException(){
	        super();
	    }
		
	    public DuplicateLoanException(String s){
	        super(s);
	    }
	}
