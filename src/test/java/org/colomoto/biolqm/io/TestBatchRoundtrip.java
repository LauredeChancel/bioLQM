package org.colomoto.biolqm.io;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.colomoto.TestHelper;
import org.colomoto.biolqm.LQMServiceManager;
import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.LogicalModelComparator;
import org.colomoto.biolqm.ReferenceModels;
import org.junit.Assert;
import org.junit.Test;

/**
 * Brute force tests for all formats supporting both import and export.
 * 
 * @author Aurelien Naldi
 */
public class TestBatchRoundtrip {
	
	@Test
	public void test() {

		String[] names = ReferenceModels.getNames();
		List<LogicalModelFormat> ioformats = new ArrayList<LogicalModelFormat>();
		for (LogicalModelFormat format: LQMServiceManager.getFormats()) {
			if (format.canImport() && format.canExport()) {
				ioformats.add(format);
			}
		}
		System.out.println("*************************************************");
		System.out.println("     E/I roundtrips: "+names.length+" models ; "+ioformats.size()+" formats");
		System.out.println("*************************************************");
		
		boolean failedModels = false;
		for (String name: names) {
			LogicalModel model = null;
			try {
				model = ReferenceModels.getModel(name);
			} catch (Exception e) {}
			
			if (model == null) {
				System.out.println("[FAIL] "+name);
				failedModels = true;
				continue;
			}
			System.out.println("* "+name);
			
			for (LogicalModelFormat format: ioformats) {
				System.out.println("   - "+format.getID());
				try {
					roundtrip(format, name, model);
				} catch (Exception e) {
					fail("Format "+format.getID()+" failed on "+name+"\nMessage: "+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		if (failedModels) {
			fail("Some models could not be loaded");
		}
	}

	/**
	 * Test a format on a specific model.
	 * This will export the model, then import it back and check that the result is consistent.
	 * 
	 * @param format
	 * @param model
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void roundtrip(LogicalModelFormat format, String name, LogicalModel model) throws FileNotFoundException, IOException {
		String ioName = name+"."+format.getID();
		File f = TestHelper.getTestOutput("io_roundtrips", ioName);
		format.export(model, new OutputStreamProvider(f));
		LogicalModel importedModel = format.importFile(f);
		Assert.assertTrue( LogicalModelComparator.compare(model, importedModel) );
	}
}
