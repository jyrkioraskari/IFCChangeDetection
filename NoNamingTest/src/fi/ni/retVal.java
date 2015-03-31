package fi.ni;

public class retVal
{
	int removed=0;
	int added=0;
	
	public retVal(int removed, int added) {
		super();
		this.removed = removed;
		this.added = added;
	}
	
	public int getCompValue()
	{
		return removed+added;
		
	}
	
}