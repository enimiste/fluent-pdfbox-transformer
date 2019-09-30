package com.nouni.fluentPdfBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

final class IdentityPdfTransformation implements PdfTransformation {
	static PdfTransformation IDENTITY;

	public IdentityPdfTransformation() {
	}

	public static synchronized PdfTransformation getInstance() {
		if (IDENTITY == null) {
			IDENTITY = new IdentityPdfTransformation();
		}
		return IDENTITY;
	}

	@Override
	public void apply(PDDocument pdf, PDPage page, int pageNnumber) throws Exception {
		//Nothing to do
	}

}
