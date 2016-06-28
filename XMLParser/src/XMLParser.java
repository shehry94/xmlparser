import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box.Filler;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLParser {

	FeatureModel featureModel;

	public Element XMLParsing() {

		System.out.println("MAIN Started");

		featureModel = new FeatureModel();

		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("feature_xml.txt");
		Element condition1 = null;
		try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement(); // feature Model
			rootNode.getChildren();

			// for (int i = 0; i < rootNode.getChildren().size(); i++) {
			// if (rootNode.getChildren().get(i).getName().equals("struct")) {
			// Struct newStuct = new Struct();
			//
			// condition = rootNode.getChildren().get(0).getChildren().get(0);
			// System.out.println("main node: " + condition.getName());
			//
			// System.out.println();
			// Condition condObj = new Condition();
			//
			// if (condition.getAttributeValue("mandatory") != null) {
			// condObj.setMandatory(condition.getAttributeValue("mandatory"));
			// }
			// if (condition.getAttributeValue("abstract") != null) {
			// condObj.setAbstract(condition.getAttributeValue("abstract"));
			// }
			// condObj.setName(condition.getAttributeValue("name"));
			// fillData(condition, condObj);
			//
			// newStuct.getConditions().add(condObj);
			// featureModel.getStruct().add(newStuct);
			// } else if
			// (rootNode.getChildren().get(i).getName().equals("struct")) {
			//
			// }
			//
			// }
			for (int i = 0; i < rootNode.getChildren().size(); i++) {
				if (rootNode.getChildren().get(i).getName().equals("struct")) {
					Element structElement = rootNode.getChildren().get(i);
					Struct newStuct = new Struct();
					featureModel.getStruct().add(newStuct);

					for (int j = 0; j < structElement.getChildren().size(); j++) {
						condition1 = structElement.getChildren().get(j);
						Condition cond1 = new Condition(condition1.getAttributeValue("name"), condition1.getName());
						System.out.println("cond1.getName(): " + cond1.getName());

						newStuct.getConditions().add(cond1);

						// USECASE

						for (int k = 0; k < condition1.getChildren().size(); k++) {
							System.out.println("Usecase");
							Element condition2 = condition1.getChildren().get(k);
							Condition cond2 = new Condition(condition2.getAttributeValue("name"), condition2.getName());
							System.out.println("cond2.getName(): " + cond2.getName());
							//cond2.setChecked(true);

							cond1.getConditions().add(cond2);

							// FEARTURE 1

							System.out.println("Features");
							System.out.println("condition2.getName(): " + condition2.getName());
							for (int l = 0; l < condition2.getChildren().size(); l++) {
								Element condition22 = condition2.getChildren().get(l);

								if (condition22.getName().equals("feature")) {
									Feature feature1 = new Feature();
									feature1.setName(condition22.getAttributeValue("name"));
									System.out.println("feature1.getName(): " + feature1.getName());

									cond2.getFeatures().add(feature1);

								} else {
									// InnerCondition
									System.out.println("InnerCOndition1");
									Condition cond3 = new Condition(condition22.getAttributeValue("name"), condition22.getName());
									System.out.println("cond3.getName(): " + cond3.getName());
									Element condition3 = condition22;
									cond2.getConditions().add(cond3);
									
									
									for (int k1 = 0; k1 < condition3.getChildren().size(); k1++) {
										Element condition4 = condition3.getChildren().get(k1);
										
										if (condition4.getName().equals("feature")) {
											Feature feature3 = new Feature();
											feature3.setName(condition4.getAttributeValue("name"));
											System.out.println("feature3.getName(): " + feature3.getName());
											cond3.getFeatures().add(feature3);

										} else {
											// InnerCondition
											System.out.println("InnerCOndition2");
											Condition cond4 = new Condition(condition4.getAttributeValue("name"), condition4.getName());
											System.out.println("cond4.getName(): " + cond4.getName());
											Element condition5 = condition4;
											cond3.getConditions().add(cond4);
											
											for (int k2 = 0; k2 < condition5.getChildren().size(); k2++) {
												Feature feature4 = new Feature();
												feature4.setName(condition5.getChildren().get(k2).getAttributeValue("name"));
												System.out.println("feature4.getName(): " + feature4.getName());
												cond4.getFeatures().add(feature4);
											}									

										}
									}
									
									

								}
							}

						}

					}

				}

			}
			// rootNode.

			// fullList()
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		System.out.println("MAIN ENDED");
		return condition1;
	}

	public FeatureModel getFeatureModel() {
		return featureModel;
	}

//	public void fillData(Element currNode, Condition cond) {
//		String name;
//		System.err.println("fillData");
//		System.out.println("currNode: " + currNode.getAttributeValue("name"));
//		name = currNode.getAttributeValue("name");
//		System.out.println("currNode Ki value: " + name);
//		cond.getConditions().add(new Condition(name));
//
//		System.out.println();
//		for (int i = 0; i < currNode.getChildren().size(); i++) {
//
//			if (currNode.getChildren().get(i).getName().equals("feature")) {
//				Feature feature = new Feature();
//				String featureName = currNode.getChildren().get(i).getAttributeValue("name");
//				featureName.trim();
//
//				if (currNode.getAttributeValue("mandatory") != null) {
//					feature.setMandatory(currNode.getAttributeValue("mandatory"));
//				}
//
//				feature.setName(featureName);
//				cond.getFeatures().add(feature);
//				System.out.println("feature: " + currNode.getName());
//				System.out.println("featureName: " + currNode.getChildren().get(i).getAttributeValue("name"));
//				// System.out.println("Mandatory : " + feature.mandatory);
//
//				System.out.println();
//			} else {
//
//				System.out.println("cond: " + currNode.getName());
//
//				cond.getConditions().add(
//						new Condition(currNode.getAttributeValue("name"), currNode.getAttributeValue("mandatory")));
//				fillData(currNode.getChildren().get(i), cond.getConditions().get(cond.getConditions().size() - 1));
//
//			}
//		}
//	}
}// end XML Parser