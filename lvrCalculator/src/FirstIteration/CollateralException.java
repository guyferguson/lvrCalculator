package FirstIteration;

	/**
	 * An exception indicating an invalid file format.
	 */
	@SuppressWarnings("serial")
	public class CollateralException extends Exception {
	    
	    public CollateralException(){
	        super();
	    
	    }
		
	    public CollateralException(String s){
	        super(s);
	    }
	}

