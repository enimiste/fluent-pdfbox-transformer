package com.nouni.fluentPdfBox.transformations;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDPage;

import com.nouni.fluentPdfBox.PdfTransformation;

public class EmbeddedFilesAndJsPdfTrans implements PdfTransformation {

	@Override
	public void apply(PDDocument pdf, PDPage page, int pageNnumber) throws Exception {
		PDDocumentCatalog catalog = pdf.getDocumentCatalog();
        PDDocumentNameDictionary names = catalog.getNames();
        if (names != null) {
            names.setEmbeddedFiles(null);
            names.setJavascript(null);
        }
	}

}
