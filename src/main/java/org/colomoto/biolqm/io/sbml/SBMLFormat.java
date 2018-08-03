package org.colomoto.biolqm.io.sbml;

import org.colomoto.biolqm.MultivaluedSupport;
import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.io.AbstractFormat;
import org.colomoto.biolqm.io.InputStreamProvider;
import org.colomoto.biolqm.io.LogicalModelFormat;
import org.colomoto.biolqm.io.OutputStreamProvider;
import org.kohsuke.MetaInfServices;

import javax.xml.stream.XMLStreamException;
import java.io.*;

/**
 * Format description for SBML files, using the qual extension.
 * 
 * @author Aurelien Naldi
 */
@MetaInfServices(LogicalModelFormat.class)
public class SBMLFormat extends AbstractFormat {

	public SBMLFormat() {
		super("sbml", "SBML-qual v1.0 format", MultivaluedSupport.MULTIVALUED);
	}


	@Override
	public LogicalModel loadImpl(InputStreamProvider ip) throws IOException {
		try {
			return new SBMLqualImport(ip.getInputStream()).getModel();
		} catch (XMLStreamException e) {
			throw new IOException(e);
		}
	}

	
	@Override
	public void exportImpl(LogicalModel model, OutputStreamProvider out) throws IOException {
		try {
			new SBMLqualExport(model).export(out.getOutputStream());
		} catch (XMLStreamException e) {
			throw new IOException(e);
		}
	}

}
