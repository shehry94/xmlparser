
public class Feature {
	boolean isChecked = true;
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public Feature(String name, String mandatory) {
		// TODO Auto-generated constructor stub
		
		this.name = name;
		
		
		if(mandatory.equals("true")){
			this.mandatory = true;
		}else{
			this.mandatory = false;
		}
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public Feature() {
		// TODO Auto-generated constructor stub
	}
	String name;
	boolean mandatory = false;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		if(mandatory.equals("true")){
			this.mandatory = true;
		}else{
			this.mandatory = false;
		}
	}
}
