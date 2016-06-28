import java.awt.BorderLayout;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.lang.model.element.Element;
import javax.swing.JFrame;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class Main extends JFrame {

	public Main() {
		setSize(800, 800);

		this.getContentPane().setLayout(new BorderLayout());
		final JCheckBoxTree cbt = new JCheckBoxTree();
		this.getContentPane().add(cbt);

		cbt.addCheckChangeEventListener(new JCheckBoxTree.CheckChangeEventListener() {
			public void checkStateChanged(JCheckBoxTree.CheckChangeEvent event) {
				//System.out.println("CheckChangeEventListener called");
				TreePath[] paths = cbt.getCheckedPaths();
				for (TreePath tp : paths) {
					for (Object pathPart : tp.getPath()) {
						//System.out.print(pathPart + ",");
					}
					//System.out.println();
				}
			}
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	static XMLParser parse;
	public static XMLParser getParse() {
		return parse;
	}
	public static void main(String args[]) {

		Main m = new Main();
		parse = new XMLParser();
		parse.XMLParsing();
		m.setVisible(true);

		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("filename.txt"), "utf-8"))) {
			writer.write("something");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("DONE");

	}
}