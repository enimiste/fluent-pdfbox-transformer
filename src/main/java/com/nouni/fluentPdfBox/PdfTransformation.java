package com.nouni.fluentPdfBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.nouni.fluentPdfBox.transformations.EmbeddedFilesAndJsPdfTrans;

/**
 * 
 * @author NOUNI EL Bachir
 *
 */
public interface PdfTransformation {

	/**
	 * 
	 * @return a transformation that does nothing
	 */
	static PdfTransformation identity() {
		return IdentityPdfTransformation.getInstance();
	}

	/**
	 * Contains in order : - {@link EmbeddedFilesAndJsPdfTrans}
	 * 
	 * @return a default transformations set
	 */
	static PdfTransformation defaults() {
		return identity().chain(new EmbeddedFilesAndJsPdfTrans());
	}

	/**
	 * 
	 * @param then
	 * @return
	 */
	default PdfTransformation chain(PdfTransformation then) {
		return new ChainedPdfTransformation(this, then);
	}

	/**
	 * 
	 * @param pdf         the same instance is passed through all the transformation
	 *                    chain
	 * @param page        the current page
	 * @param pageNnumber the page number. This is a zero based counter
	 * @throws Exception
	 */
	void apply(PDDocument pdf, PDPage page, int pageNnumber) throws Exception;

}
