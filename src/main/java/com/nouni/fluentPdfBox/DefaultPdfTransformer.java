package com.nouni.fluentPdfBox;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;

class DefaultPdfTransformer implements PdfTransformer {

	private File sourceFile;
	private String password;
	private MemoryUsageSetting memoryStrategy;
	private File outputFile;

	public DefaultPdfTransformer(File input) {
		this.sourceFile = input;
		this.memoryStrategy = null;
		this.password = null;
	}

	@Override
	public void setPassword(String pwd) {
		this.password = pwd;
	}

	@Override
	public void setMemoryUsageStrategy(MemoryUsageSetting memoryStrategy) {
		this.memoryStrategy = memoryStrategy;
	}

	@Override
	public void setSaveTo(File output) {
		this.outputFile = output;
	}

	@Override
	public void execute(PdfTransformation trans) throws IOException, Exception {
		PDDocument doc = createPDDocument(sourceFile, password, memoryStrategy);
        doc.setAllSecurityToBeRemoved(true);
        if (doc.isEncrypted()) {
            System.out.println("Document is encrypted");
        }
        
        for (int i=0; i<doc.getNumberOfPages(); i++) {
            trans.apply(doc, doc.getPage(i), i);
        }
        
        doc.save(outputFile);
        doc.close();
	}

	/**
	 * 
	 * @param sourceFile
	 * @param password
	 * @param memoryStrategy
	 * @return
	 * @throws IOException
	 */
	private static PDDocument createPDDocument(File sourceFile, String password, MemoryUsageSetting memoryStrategy) throws IOException {
		if(password != null && memoryStrategy != null) {
			return PDDocument.load(sourceFile, password, memoryStrategy);
		} else if(memoryStrategy != null) {
			return PDDocument.load(sourceFile, memoryStrategy);
		} else if(password != null) {
			return PDDocument.load(sourceFile, memoryStrategy);
		} else {
			return PDDocument.load(sourceFile);
		}
	}

}
