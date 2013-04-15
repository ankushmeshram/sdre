package de.dfki.isreal.semantic.oms.components.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriterFactory;

public class DefaultHTTPHandler extends AbstractHandler {

	private Logger logger = Logger.getLogger(DefaultHTTPHandler.class);
	
	protected static List<String> locations = new ArrayList<String>();
	
	public static void addLocation(String location) {
		locations.add(location);
	}
	
	private static DefaultHTTPHandler singleton = new DefaultHTTPHandler();
	
	private DefaultHTTPHandler() {
		
	}
	
	public static DefaultHTTPHandler getInstance() {
		return singleton;
	}
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
								
		response.getWriter().println("Please specify an OWLIM repository to view. Path options are: ");
		for(String location : locations)
			response.getWriter().println("/" + location);
		response.getWriter().close();
	}
}
