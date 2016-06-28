import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.lang.model.element.Element;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.bind.ParseConversionEvent;

public class JCheckBoxTree extends JTree {
	

	List<TreePath> t_path = new ArrayList<TreePath>();
	DefaultMutableTreeNode s_node;
	
	JCheckBoxTree selfPointer = this;

	private class CheckedNode {
		boolean isSelected;
		boolean hasChildren;
		boolean allChildrenSelected;

		public CheckedNode(boolean isSelected_, boolean hasChildren_, boolean allChildrenSelected_) {
			isSelected = isSelected_;
			hasChildren = hasChildren_;
			allChildrenSelected = allChildrenSelected_;
		}
	}

	HashMap<TreePath, CheckedNode> nodesCheckingState;
	HashSet<TreePath> checkedPaths = new HashSet<TreePath>();

	// Defining a new event type for the checking mechanism and preparing
	// event-handling mechanism
	protected EventListenerList listenerList = new EventListenerList();

	public class CheckChangeEvent extends EventObject {

		public CheckChangeEvent(Object source) {
			super(source);
		}
	}

	public interface CheckChangeEventListener extends EventListener {
		public void checkStateChanged(CheckChangeEvent event);
	}

	public void addCheckChangeEventListener(CheckChangeEventListener listener) {
		listenerList.add(CheckChangeEventListener.class, listener);
	}

	public void removeCheckChangeEventListener(CheckChangeEventListener listener) {
		listenerList.remove(CheckChangeEventListener.class, listener);
	}

	void fireCheckChangeEvent(CheckChangeEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == CheckChangeEventListener.class) {
				((CheckChangeEventListener) listeners[i + 1]).checkStateChanged(evt);
			}
		}
	}

	// Override
	public void setModel(TreeModel newModel) {

		super.setModel(setNodeValues());
		resetCheckingState();
	}

	XMLParser parse;

	public TreeModel setNodeValues() {

		parse = new XMLParser();

		org.jdom2.Element con = (org.jdom2.Element) parse.XMLParsing();
		String root = con.getAttributeValue("name");
		

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
		TreeModel tm = new DefaultTreeModel(rootNode);

		for (int i = 0; i < con.getChildren().size(); i++) {
			String newElem = con.getChildren().get(i).getAttributeValue("name");
//			//adding to selection path
//			s_node = new DefaultMutableTreeNode(newElem);
//			t_path.add(getPath(s_node));
//			/////////////////////////////
			DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(newElem);
			rootNode.add(newChild);

			for (int j = 0; j < con.getChildren().get(i).getChildren().size(); j++) {

				String c = con.getChildren().get(i).getChildren().get(j).getAttributeValue("name");

				DefaultMutableTreeNode childOfChild = new DefaultMutableTreeNode(c);
				newChild.add(childOfChild);

				for (int k = 0; k < con.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {

					String m = con.getChildren().get(i).getChildren().get(j).getChildren().get(k)
							.getAttributeValue("name");

					DefaultMutableTreeNode innerChild = new DefaultMutableTreeNode(m);
					childOfChild.add(innerChild);

					for (int l = 0; l < con.getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren()
							.size(); l++) {

						String n = con.getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren()
								.get(l).getAttributeValue("name");
						//adding to selection path
						//s_node = new DefaultMutableTreeNode(n);
						//t_path.add(getPath(s_node));
						/////////////////////////////

						DefaultMutableTreeNode innerMostChild = new DefaultMutableTreeNode(n);
						innerChild.add(innerMostChild);
					}
				}
			}
			//s_node = new DefaultMutableTreeNode(newElem);

		}

		JTree tree = new JTree(treeModel);
		tree.setEditable(true);
		//tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		tree.setShowsRootHandles(true);

		tree.clearSelection();
		return tm;
	}

	// New method that returns only the checked paths (totally ignores original
	// "selection" mechanism)
	public TreePath[] getCheckedPaths() {
		return checkedPaths.toArray(new TreePath[checkedPaths.size()]);
	}

	// Returns true in case that the node is selected, has children but not all
	// of them are selected
	public boolean isSelectedPartially(TreePath path) {
		CheckedNode cn = nodesCheckingState.get(path);
		return cn.isSelected && cn.hasChildren && !cn.allChildrenSelected;
	}

	private void resetCheckingState() {
		nodesCheckingState = new HashMap<TreePath, CheckedNode>();
		checkedPaths = new HashSet<TreePath>();
		System.out.println("**************");
		System.out.println(getModel().getRoot());
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getModel().getRoot();

		if (node == null) {
			return;
		}
		addSubtreeToCheckingStateTracking(node);
	}

	// Creating data structure of the current model for the checking mechanism
	private void addSubtreeToCheckingStateTracking(DefaultMutableTreeNode node) {
		TreeNode[] path = node.getPath();
		TreePath tp = new TreePath(path);
		CheckedNode cn = new CheckedNode(true, node.getChildCount() > 0, false);
		nodesCheckingState.put(tp, cn);
		for (int i = 0; i < node.getChildCount(); i++) {
			addSubtreeToCheckingStateTracking(
					(DefaultMutableTreeNode) tp.pathByAddingChild(node.getChildAt(i)).getLastPathComponent());
		}
	}

	// Overriding cell renderer by a class that ignores the original "selection"
	// mechanism
	// It decides how to show the nodes due to the checking-mechanism
	private class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {
		JCheckBox checkBox;

		public CheckBoxCellRenderer() {
			super();
			this.setLayout(new BorderLayout());
			checkBox = new JCheckBox();
			add(checkBox, BorderLayout.CENTER);
			setOpaque(false);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object obj = node.getUserObject();
			TreePath tp = new TreePath(node.getPath());
			CheckedNode cn = nodesCheckingState.get(tp);
			if (cn == null) {
				return this;
			}
			checkBox.setSelected(cn.isSelected);
			checkBox.setText(obj.toString());
			checkBox.setOpaque(cn.isSelected && cn.hasChildren && !cn.allChildrenSelected);
			return this;
		}
	}

	public JCheckBoxTree() {
		// super();
		// Disabling toggling by double-click
		this.setToggleClickCount(0);
		// Overriding cell renderer by new one defined above
		CheckBoxCellRenderer cellRenderer = new CheckBoxCellRenderer();
		this.setCellRenderer(cellRenderer);

		// Overriding selection model by an empty one
		DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel() {

			// Totally disabling the selection mechanism
			public void setSelectionPath(TreePath path) {
			}

			public void addSelectionPath(TreePath path) {

			}

			public void removeSelectionPath(TreePath path) {
			}

			public void setSelectionPaths(TreePath[] pPaths) {
			}
		};
		// Calling checking mechanism on mouse click
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				TreePath tp = selfPointer.getPathForLocation(arg0.getX(), arg0.getY());
				if (tp == null) {
					return;
				}
				boolean checkMode = !nodesCheckingState.get(tp).isSelected;
				checkSubTree(tp, checkMode);
				updatePredecessorsWithCheckMode(tp, checkMode);
				// Firing the check change event
				fireCheckChangeEvent(new CheckChangeEvent(new Object()));
				// Repainting tree after the data structures were updated
				selfPointer.repaint();
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});
		this.setSelectionModel(dtsm);
	}

	// When a node is checked/unchecked, updating the states of the predecessors
	protected void updatePredecessorsWithCheckMode(TreePath tp, boolean check) {
		TreePath parentPath = tp.getParentPath();
		// If it is the root, stop the recursive calls and return
		if (parentPath == null) {
			return;
		}
		CheckedNode parentCheckedNode = nodesCheckingState.get(parentPath);
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
		parentCheckedNode.allChildrenSelected = true;
		parentCheckedNode.isSelected = false;
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			TreePath childPath = parentPath.pathByAddingChild(parentNode.getChildAt(i));
			CheckedNode childCheckedNode = nodesCheckingState.get(childPath);
			// It is enough that even one subtree is not fully selected
			// to determine that the parent is not fully selected
			if (!childCheckedNode.allChildrenSelected) {
				parentCheckedNode.allChildrenSelected = false;
			}
			// If at least one child is selected, selecting also the parent
			if (childCheckedNode.isSelected) {
				parentCheckedNode.isSelected = true;
			}
		}
		if (parentCheckedNode.isSelected) {
			checkedPaths.add(parentPath);
		} else {
			checkedPaths.remove(parentPath);
		}
		// Go to upper predecessor
		updatePredecessorsWithCheckMode(parentPath, check);
	}

	// protected void checkSubTree(TreePath tp, boolean check) {
	// CheckedNode cn = nodesCheckingState.get(tp);
	// cn.isSelected = check;
	// DefaultMutableTreeNode node = (DefaultMutableTreeNode)
	// tp.getLastPathComponent();
	// System.out.println();
	// boolean allowed = false;
	//
	// if(tp.getPathCount() >= 3){
	// System.out.println(tp.toString());
	// allowed = getChildName(tp.toString());
	// }
	//
	// for (int i = 0; i < node.getChildCount(); i++) {
	// //checkSubTree(tp.pathByAddingChild(node.getChildAt(i)), check);
	// }
	// cn.allChildrenSelected = check;
	// if (check) {
	// checkedPaths.add(tp);
	// } else {
	// checkedPaths.remove(tp);
	// }
	// }

	protected void checkSubTree(TreePath tp, boolean check) {
		System.out.println("\n====CheckSUbTree");
		CheckedNode cn = nodesCheckingState.get(tp);

		if (!check) {
			cn.isSelected = check; // false
			cn.allChildrenSelected = check;
			checkedPaths.remove(tp);
		} else {

			cn.isSelected = check;
			checkedPaths.add(tp);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
			// tp = (TreePath) tp.getPathComponent(tp.getPathCount() - 1);
			System.out.println("tp.:" + tp);

			Condition parentCondition = null;

			String childName = "";

			// if (tp.getPathCount() >= 1) {
			if (tp.getPathCount() > 1) {
				// System.out.println(tp.toString());
				childName = getChildName(tp.toString());
				parentCondition = getParent(childName);

				System.err.println("childName: " + childName);
				// System.out.println("Parent Name:" +
				// parentCondition.getName());

				if (parentCondition != null && !childName.equals("")) {
					if (parentCondition.getConditionName().equals("alt")) {
						System.out.println("Parent Name:" + parentCondition.getName() + "\nCond: "
								+ parentCondition.getConditionName());

						// for (int i = 0; i <
						// parentCondition.getConditions().size(); i++) {
						// parentCondition.getConditions().get(i).setChecked(false);
						// if
						// (parentCondition.getConditions().get(i).getName().equals(childName))
						// {
						// parentCondition.getConditions().get(i).setChecked(true);
						// }
						//
						// }
						// for (int i = 0; i <
						// parentCondition.getFeatures().size(); i++) {
						// parentCondition.getFeatures().get(i).setChecked(false);
						// if
						// (parentCondition.getFeatures().get(i).getName().equals(childName))
						// {
						// parentCondition.getFeatures().get(i).setChecked(true);
						// }
						// }
						node = (DefaultMutableTreeNode) node.getParent();

						System.out.println("node.getChildCount()" + node.getChildCount());
						for (int i = 0; i < node.getChildCount(); i++) {
							TreeNode tNode = node.getChildAt(i);
							System.out.println("tNode: " + tNode);

							CheckedNode cn1 = nodesCheckingState.get(getPath(tNode));

							if (cn1 != null) {
								System.out.println("cn1: " + cn1);
								cn1.isSelected = false;
								checkedPaths.remove(getPath(tNode));
							}
						}

						cn.isSelected = check;
						checkedPaths.add(tp);
					}
				}
			} else {
				cn.isSelected = check;
				checkedPaths.add(tp);
			}
		}
	}

	public static TreePath getTreePathFromList(ArrayList<String> nodes){
		return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
	}
	public static TreePath getPath(TreeNode treeNode) {
		List<Object> nodes = new ArrayList<Object>();
		if (treeNode != null) {
			nodes.add(treeNode);
			treeNode = treeNode.getParent();
			while (treeNode != null) {
				nodes.add(0, treeNode);
				treeNode = treeNode.getParent();
			}
		}

		return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
	}

	private String getChildName(String string) {
		// TODO Auto-generated method stub

		String child = string.split(",")[string.split(",").length - 1].trim();
		child = child.substring(0, child.length() - 1);
		child.trim();

		return child;
	}

	private Condition getParent(String child) {
		Condition ParentCondition = null;

		for (Struct struct : parse.getFeatureModel().getStruct()) {
			for (Condition condition1 : struct.getConditions()) {

				// System.out.println("condition1.getName():" +
				// condition1.getName());
				for (Condition condition2 : condition1.getConditions()) {

					// System.out.println("-Usecase: condition2.getName(): " +
					// condition2.getName());
					if (condition2.getName().trim().equals(child)) {
						ParentCondition = condition1;
						break;
					}

					for (Feature feature1 : condition2.getFeatures()) {
						// System.out.println("feature1.getName():" +
						// feature1.getName());

						// System.out.println("feature1.getName():" +
						// feature1.getName());
						if (feature1.getName().trim().equals(child)) {
							ParentCondition = condition2;
							break;
						}

					}
					for (Condition cond22 : condition2.getConditions()) {
						// System.out.println("feature1.getName():"
						if (cond22.getName().trim().equals(child)) {
							ParentCondition = condition2;
							break;
						}

					}

					// System.out.println("condition2.getConditions().size(): "
					// + condition2.getConditions().size());
					for (Condition condition3 : condition2.getConditions()) {
						// System.out.println("condition3.getName():" +
						// condition3.getName());
						for (Feature feature1 : condition3.getFeatures()) {

							// System.out.println("feature1.getName():" +
							// feature1.getName());
							if (feature1.getName().trim().equals(child)) {
								ParentCondition = condition3;
								break;
							}
						}

						for (Condition condition4 : condition3.getConditions()) {

							for (Feature feature2 : condition4.getFeatures()) {
								// System.out.println("feature2.getName():" +
								// feature2.getName() + ", " + child);

								if (feature2.getName().trim().equals(child)) {
									ParentCondition = condition4;
									break;
								}
							}

						}
					}
				}
			}
		}

		return ParentCondition;
	}

}