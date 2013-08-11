package de.dfki.isreal.helpers;

import java.io.File;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

/**
 * Normal FileFilter that filters for registered extensions
 * (e.g. jpg, exe, java, ...).
 * 
 * @author stenes
 *
 */
public class ExtensionFileFilter extends FileFilter {
	
	Vector<String> exts = new Vector<String>();

	/**
	 * Accepts if extension of the file is registered.
	 */
	@Override
	public boolean accept(File arg0) {
		// get extensions
		boolean acc = false;
		acc = arg0.isDirectory();
		if (arg0.isFile()){
			String file = arg0.getAbsolutePath();
			String ext = file.substring(file.lastIndexOf(".")+1);
			acc = exts.contains(ext);
		}
		return acc;
	}

	/**
	 * Return the extension descriptions as String.
	 */
	@Override
	public String getDescription() {
		String desc = "";
		for (String ext : exts){
			desc = desc + "(." + ext + ")";
		}
		return desc;
	}
	
	/**
	 * Registers ext as new extension.
	 * @param ext
	 */
	public void addExtension(String ext){
		exts.add(ext);
	}
	
	

}
