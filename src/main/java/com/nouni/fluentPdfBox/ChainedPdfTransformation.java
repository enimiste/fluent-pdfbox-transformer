package com.nouni.fluentPdfBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

class ChainedPdfTransformation implements PdfTransformation {

	protected PdfTransformation first;
	protected PdfTransformation then;

	ChainedPdfTransformation(PdfTransformation first, PdfTransformation then) {
		this.first = first;
		this.then = then;
	}

	@Override
	public void apply(PDDocument pdf, PDPage page, int pageNnumber) throws Exception {
		first.apply(pdf, page, pageNnumber);
		then.apply(pdf, page, pageNnumber);
	}

}
