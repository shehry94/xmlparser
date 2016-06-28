import java.util.ArrayList;

public class FeatureModel {

	ArrayList<Struct> struct = new ArrayList<Struct>();
	Constraints constraints = new Constraints();
	Calculations calculations = new Calculations();
	Comments commetns = new Comments();

	int chosenLayoutAlgorithm;

	public int getChosenLayoutAlgorithm() {
		return chosenLayoutAlgorithm;
	}

	public ArrayList<Struct> getStruct() {
		return struct;
	}

	public Constraints getConstraints() {
		return constraints;
	}

	public Calculations getCalculations() {
		return calculations;
	}

	public Comments getCommetns() {
		return commetns;
	}

}

// -FeatureModel
class Constraints {

}

// -FeatureModel
class Calculations {
	boolean auto = false, constraints = false, features = false, redundant = false, tautology = false;
}

// -FeatureModel
class Comments {

}

// -FeatureModel
class FeatureOrder {
	boolean userDefined = false;
}