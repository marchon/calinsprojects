package ro.calin.wsd;

public class Word {
	private String value;
	private String role;
	
	public Word() {
	}
	
	public Word(String value) {
		setValue(value);
	}
	
	public Word(String value, String role) {
		setValue(value);
		setRole(role);
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public boolean equals(Object obj) {
		return value.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public String toString() {
		String ret = value;
		if(role != null) {
			ret += "(" + role + ")";
		}
		return ret;
	}
}
