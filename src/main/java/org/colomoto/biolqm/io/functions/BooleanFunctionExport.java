package org.colomoto.biolqm.io.functions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.io.BaseExporter;
import org.colomoto.biolqm.io.StreamProvider;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.MDDVariable;
import org.colomoto.mddlib.PathSearcher;

/**
 * Export logical model into a raw list of functions.
 * 
 * @author Aurelien Naldi
 */
public class BooleanFunctionExport extends BaseExporter {


	public BooleanFunctionExport(LogicalModel model, StreamProvider streams) {
		super(model, streams);
	}

	@Override
	public void export() throws IOException {
		MDDManager ddmanager = model.getMDDManager();
		MDDVariable[] variables = ddmanager.getAllVariables();
		PathSearcher searcher = new PathSearcher(ddmanager);
		
		Writer writer = streams.writer();
		int[] functions = model.getLogicalFunctions();
		for (int idx=0 ; idx<functions.length ; idx++) {
			MDDVariable var = variables[idx];
			writer.write(var+": ");
			
			int function = functions[idx];
			
			// directly write fixed values
			if (ddmanager.isleaf(function)) {
				writer.write(function+"\n");
				continue;
			}
			
			// write a normalised logical function if the value is not fixed
			int[] path = searcher.setNode(function);
			boolean first = true;
			boolean multiple = false;
			StringBuffer funcBuffer = new StringBuffer();
			for (int leaf: searcher) {
				if (leaf == 0) {
					continue;
				}
				
				if (!first) {
					funcBuffer.append(") | (");
					multiple = true;
				} else {
					first = false;
				}
				
				// write each constraint
				boolean andFirst = true;
				for (int i=0 ; i<path.length ; i++) {
					int cst = path[i];
					if (cst < 0) {
						continue;
					}
					
					if (!andFirst) {
						funcBuffer.append(" & ");
					}
					
					if (cst == 0) {
						funcBuffer.append("!"+variables[i].key);
					} else {
						// FIXME: adapt for multivalued
						funcBuffer.append(variables[i].key.toString());
					}
					andFirst = false;
				}
			}
			if (multiple) {
				writer.write("(");
				writer.write(funcBuffer.toString());
				writer.write(")");
			} else {
				writer.write(funcBuffer.toString());
			}
			writer.write("\n");
		}
		
		writer.close();
	}

}
