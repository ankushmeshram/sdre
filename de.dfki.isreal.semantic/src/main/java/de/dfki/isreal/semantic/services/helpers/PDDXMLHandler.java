package de.dfki.isreal.semantic.services.helpers;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import de.dfki.isreal.data.Statement;
import de.dfki.isreal.data.impl.StatementImpl;

public class PDDXMLHandler extends DefaultHandler {
	private static Logger logger = Logger.getLogger(PDDXMLHandler.class);
	
	String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

	int and_f = 0;
	int not_f = 0;
	int pred_f = 0;
	int param_f = 0;
	int param_c = 0;

	String pred_name = "";
	String param1 = "";
	String param2 = "";

	Statement st = null;

	ArrayList<Statement> conj_st_list = new ArrayList<Statement>();
	ArrayList<Statement> neg_conj_st_list = new ArrayList<Statement>();

	public void startDocument() {
	}

	public void endDocument() {
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		if (qName == "and") {
			and_f = 1;
		}
		if (qName == "not") {
			not_f = 1;
		}
		if (qName == "pred") {
			pred_f = 1;
			if (attributes.getIndex("name") >= 0) {
				pred_name = attributes.getValue(attributes.getIndex("name"));
			} else {
				logger.error("Error: Predicate has no name.");
			}
		}
		if (qName == "param") {
			param_f = 1;
			param_c++;
		}
	}

	public void endElement(String uri, String localName, String qName) {
		if (qName == "and") {
			and_f = 0;
		}
		if (qName == "not") {
			not_f = 0;
		}
		if (qName == "pred") {
			if (param1 != "" && pred_name != "") {
				if (param2 == "") {
					st = new StatementImpl(param1, type, pred_name);
				} else {
					st = new StatementImpl(param1, pred_name, param2);
				}
				if (and_f == 1) {
					if (not_f == 1){
						neg_conj_st_list.add(st);
					}else{
						conj_st_list.add(st);
					}
				}
			}
			pred_f = 0;
			param_c = 0;
			pred_name = "";
			param1 = "";
			param2 = "";
		}
		if (qName == "param") {
			param_f = 0;
		}
	}

	public void ignorableWhitespace(char[] ch, int start, int length) {

	}

	public void processingInstruction(String target, String data) {

	}

	public void characters(char[] ch, int start, int length) {
		String str = String.valueOf(ch, start, length);
		if (param_f == 1) {
			if (param_c == 1) {
				param1 = str;
			}
			if (param_c == 2) {
				param2 = str;
			}
		}
	}

	public List<Statement> getConjList() {
		return conj_st_list;
	}
	
	public List<Statement> getNegConjList() {
		return neg_conj_st_list;
	}

	
}
