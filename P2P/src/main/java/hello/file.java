package hello;

public class file {
	
	private String name ;
	private long size ;
	
	
	public file (String name, long size) {
		this.name=name;
		this.size=size;
		
	}
	

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	

}
