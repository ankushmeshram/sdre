package de.dfki.isreal.main;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import de.dfki.isreal.helpers.ExtensionFileFilter;
import de.dfki.isreal.main.ISRealSetupInitializer;


/**
 * Simple GUI for the SetupInitializer.
 * @author stenes
 *
 */
public class SetupInitializer_GUI {
	private static Logger logger = Logger.getLogger(SetupInitializer_GUI.class);
	
	private static ISRealSetupInitializer initializer;
	private static String default_config = "";

	private static JPanel mainPane;
	private static JPanel loadPane;
	private static JTextField loadTF;
	private static JButton loadButton;
	private static JButton startButton;
	private static JScrollPane compPane;
	private static JList compList;
	private static JPanel buttonPane;
	private static JButton killSel;
	private static JButton killAll;

	
	/**
	 * 
	 * @param args IsReal main config file
	 */
	public static void main(String[] args) {
		ISRealSetupInitializer i;
		
		if (args.length > 0){
			i = new ISRealSetupInitializer(args[0]);
		} else {
			// config file is set by GUI
			i = new ISRealSetupInitializer();
		}
		SetupInitializer_GUI gui = new SetupInitializer_GUI(i);
	}

	public SetupInitializer_GUI(ISRealSetupInitializer init) {
		initializer = init;
		JFrame f = new JFrame("ISReal Setup Initializer v.0.A");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 500);
		f.add(getMainPane());

		f.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		    	  ListModel model = compList.getModel();
					for (int i = 0; i < model.getSize(); i++) {
						initializer.killProcess((String) model.getElementAt(i));
					}
					compList.setListData(initializer.getProcesses().toArray());
		        }});
		
		f.setVisible(true);
	}

	private static JPanel getMainPane() {
		if (mainPane == null) {
			mainPane = new JPanel();
			mainPane.setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.fill = GridBagConstraints.BOTH;
			gc.insets = new Insets(5, 5, 5, 5);
			mainPane.add(getLoadingPane(), gc);
			gc.gridy = 1;
			gc.weightx = 1.0;
			gc.weighty = 1.0;
			mainPane.add(getCompScrollPane(), gc);
			gc.weightx = 0;
			gc.weighty = 0;
			gc.gridx = 0;
			gc.gridy = 2;
			gc.gridwidth = 2;
			mainPane.add(getButtonPane(), gc);
		}
		return mainPane;
	}

	private static JPanel getButtonPane() {
		if (buttonPane == null) {
			buttonPane = new JPanel();
			buttonPane.setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.weightx = 1.0;
			gc.insets = new Insets(5, 15, 5, 15);
			buttonPane.add(getKillSelectedButton(), gc);
			gc.gridx = 1;
			buttonPane.add(getKillAllButton(), gc);
			gc.gridx = 2;
			buttonPane.add(getStartButton(), gc);
		}
		return buttonPane;
	}

	private static Component getKillAllButton() {
		if (killAll == null) {
			killAll = new JButton("Kill All");
			killAll.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					ListModel model = compList.getModel();
					for (int i = 0; i < model.getSize(); i++) {
						initializer.killProcess((String) model.getElementAt(i));
					}
					compList.setListData(initializer.getProcesses().toArray());
				}

			});
		}
		return killAll;
	}

	private static JButton getKillSelectedButton() {
		if (killSel == null) {
			killSel = new JButton("Kill Selected");
			killSel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String sel = (String) compList.getSelectedValue();
					if (sel != null && !sel.equals("")) {
						initializer.killProcess(sel);
					}
					compList.setListData(initializer.getProcesses().toArray());
				}

			});
		}
		return killSel;
	}

	private static JScrollPane getCompScrollPane() {
		if (compPane == null) {
			compPane = new JScrollPane(getCompList());
			compPane.setBorder(BorderFactory.createTitledBorder(BorderFactory
					.createEtchedBorder(), "Components",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.PLAIN, 18)));
		}
		return compPane;
	}

	private static JList getCompList() {
		if (compList == null) {
			compList = new JList();
			compList.setListData(initializer.getProcesses().toArray());
			compList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return compList;
	}

	private static JPanel getLoadingPane() {
		if (loadPane == null) {
			loadPane = new JPanel();
			loadPane.setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.weightx = 1.0;
			gc.insets = new Insets(5, 5, 5, 5);
			gc.fill = GridBagConstraints.BOTH;
			loadPane.add(getFileTF(), gc);
			gc.gridx = 1;
			gc.weightx = 0;
			gc.fill = GridBagConstraints.NONE;
			loadPane.add(getLoadButton(), gc);
		}
		return loadPane;
	}

	private static JButton getStartButton() {
		if (startButton == null) {
			startButton = new JButton("Start");
			startButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					//String baseDir = loadTF.getText();
					//baseDir = baseDir.substring(0, baseDir.lastIndexOf(File.separator) + 1);
					//System.setProperty("isreal.basedir", baseDir);
					
					initializer.setSetupFile(loadTF.getText());
					initializer.initializeSetup();
					compList.setListData(initializer.getProcesses().toArray());
				}

			});
		}
		return startButton;
	}

	private static JButton getLoadButton() {
		if (loadButton == null) {
			loadButton = new JButton("Load");
			loadButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser chooser = new JFileChooser(new File("setups"));
					ExtensionFileFilter filter = new ExtensionFileFilter();
					filter.addExtension("isrealsetup");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						//String baseDir = loadTF.getText();
						//baseDir = baseDir.substring(0, baseDir.lastIndexOf(File.separator) + 1);
						//System.setProperty("isreal.basedir", baseDir);
						
						loadTF.setText(chooser.getSelectedFile().getAbsolutePath());
						initializer.setSetupFile(loadTF.getText());
					}

				}

			});
		}
		return loadButton;
	}

	private static JTextField getFileTF() {
		if (loadTF == null) {
			loadTF = new JTextField(default_config);
		}
		return loadTF;
	}

	public void setDefaultConfig(String string) {
		default_config = string;
		loadTF.setText(default_config);
	}
}
