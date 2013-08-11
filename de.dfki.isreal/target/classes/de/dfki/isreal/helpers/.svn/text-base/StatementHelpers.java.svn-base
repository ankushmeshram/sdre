package de.dfki.isreal.helpers;

/**
 * Helper class for ISReal Statements.
 * @author stenes
 *
 */
public class StatementHelpers {
	
	public static String getLocalnameFromURI(String uri){
		return uri.substring(uri.lastIndexOf('#'), uri.length());
	}

	public static String getNamespaceFromURI(String uri) {
		return uri.substring(0, uri.lastIndexOf('#'));
	}
	
	public static boolean isVariable(String s){
		return s.startsWith("?");
	}
	
	
}
