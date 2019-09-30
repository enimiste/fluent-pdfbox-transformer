package com.nouni.fluentPdfBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pdfbox.io.MemoryUsageSetting;

/**
 * 
 * @author NOUNI EL Bachir
 *
 */
public interface PdfTransformer {

	/**
	 * 
	 * @param f input file
	 * @return an implementation of {@link PdfTransformer} interface
	 */
	static PdfTransformer from(File f) {
		return new DefaultPdfTransformer(f);
	}

	void setPassword(String string);

	void setMemoryUsageStrategy(MemoryUsageSetting setupMainMemoryOnly);

	/**
	 * 
	 * @param file Should be a file with pdf extension and not a folder
	 */
	void setSaveTo(File file);

	/**
	 * Apply the whole transformations chain on each page one after one At the end
	 * it saves the document to the "saveTo" file
	 * 
	 * @param trans transformation or a chained transformations
	 * @throws IOException if the input file doesn't exists or any error thrown when
	 *                     manipulating the input/output file
	 * @throws Exception   other cases
	 */
	void execute(PdfTransformation trans) throws IOException, Exception;

}
