import java.util.ArrayList;

public class Condition {
	//Properties
	boolean isChecked = true;
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	String conditionName;
	public String getConditionName() {
		return conditionName;
	}

	String name;
	boolean Abstract = false, mandatory = false;
	ArrayList<Condition> conditions = new ArrayList<Condition>();
	ArrayList<Feature> features = new ArrayList<Feature>();
	
	//Getter Setters	
	public ArrayList<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(ArrayList<Condition> conditions) {
		this.conditions = conditions;
	}

	public ArrayList<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<Feature> features) {
		this.features = features;
	}



//	public Condition(String name, String mandatory) {
//		// TODO Auto-generated constructor stub
//		
//		this.name = name;
//		if(mandatory.equals("true")){
//			this.mandatory = true;
//		}else{
//			this.mandatory = false;
//		}
//	}
	
	public Condition(String name, String mandatory, String Abstract) {
		// TODO Auto-generated constructor stub
		this.name = name;
	
		if(mandatory.equals("true")){
			this.mandatory = true;
		}else{
			this.mandatory = false;
		}
		
		if(Abstract.equals("true")){
			this.Abstract = true;
		}else{
			this.Abstract = false;
		}
	}
	
	public Condition() {
		// TODO Auto-generated constructor stub
	
	}

	
	public Condition(String name2, String cond) {
		// TODO Auto-generated constructor stub
		this.name = name2;
		conditionName = cond;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAbstract() {
		return Abstract;
	}

	public void setAbstract(boolean abstract1) {
		Abstract = abstract1;
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

	public void setAbstract(String Abstract) {
		// TODO Auto-generated method stub
		if(Abstract.equals("true")){
			this.Abstract = true;
		}else{
			this.Abstract = false;
		}
		
	}

}