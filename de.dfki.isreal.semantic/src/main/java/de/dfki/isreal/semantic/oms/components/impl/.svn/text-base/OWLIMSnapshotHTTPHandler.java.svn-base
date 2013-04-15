package de.dfki.isreal.semantic.oms.components.impl;

import java.io.IOException;
import java.util.HashSet;
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

public class OWLIMSnapshotHTTPHandler extends AbstractHandler {

	private Logger logger = Logger.getLogger(OWLIMSnapshotHTTPHandler.class);
	
	protected static String	OWL_IMPORTS = "http://www.w3.org/2002/07/owl#imports";
	
	protected String				location;	
	protected Set<String>			ignores;
	protected RepositoryConnection	repositoryConnection;
	
	public OWLIMSnapshotHTTPHandler(String location, RepositoryConnection repositoryConnection) {
		this.location = location;
		ignores = new HashSet<String>();
		for(String importUri : OMSConfig.getImportURIStrings())
		ignores.add(OMSConfig.getURIMappings().get(importUri));
		this.repositoryConnection = repositoryConnection;
	}
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// skip handling if not for the specified repository
		if(!target.toLowerCase().endsWith(location.toLowerCase()))
			return;
		
		response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        RDFXMLPrettyWriter rdfXmlWriter = (RDFXMLPrettyWriter) new RDFXMLPrettyWriterFactory().getWriter(response.getWriter());
        try {
        	RepositoryResult<Statement> statements = repositoryConnection.getStatements(null, null, null, true);
        	rdfXmlWriter.startRDF();
        	rdfXmlWriter.handleComment("Current state of " + location);
        	for(Statement statement : statements.asList())
        		if(isRelevant(statement))
        			rdfXmlWriter.handleStatement(statement);
        	rdfXmlWriter.endRDF();
		} catch (RepositoryException e) {
			logger.error("RDF repository snapshot generation (requested via HTTP) failed.", e); 
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		} catch (RDFHandlerException e) {
			logger.error("RDF repository snapshot generation (requested via HTTP) failed.", e); 
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
								
		response.getWriter().close();
	}
	
	protected boolean isRelevant(Statement statement) {
		// check for non-relevant statements (RDF, OWL imports, null context, import statements)
		if(statement.getPredicate().stringValue().equals(OWL_IMPORTS))
			return false;
		
		if(statement.getContext() == null)
		return true;
		
		for(String ignore : ignores) {
			if(statement.getContext().stringValue().contains(ignore.replaceAll("\\\\", "/").replaceAll("//", "/")))
				return false;
		}
		
		return true;
	}

}
