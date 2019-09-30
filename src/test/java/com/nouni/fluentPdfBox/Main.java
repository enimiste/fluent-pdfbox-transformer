package com.nouni.fluentPdfBox;

import java.io.File;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.nouni.fluentPdfBox.transformations.RemoveTokensPdfTrans;
import static com.nouni.fluentPdfBox.transformations.RemoveTokensPdfTrans.containsText;
import static com.nouni.fluentPdfBox.transformations.RemoveTokensPdfTrans.cosNameStartsWith;

public class Main {

	public static void main(String[] args) {
		PdfTransformation trans = PdfTransformation.defaults()
								.chain(RemoveTokensPdfTrans.from(containsText("Watermark").or(cosNameStartsWith("Fm"))))
								.chain(new PdfTransformation() {
									@Override
									public void apply(PDDocument pdf, PDPage page, int pageNnumber) throws Exception {
										System.out.println("C" + pageNnumber);
									}
								});
		
		File f = new File(args[0]);
		PdfTransformer engine = PdfTransformer.from(f);
		engine.setPassword("");
		engine.setMemoryUsageStrategy(MemoryUsageSetting.setupMainMemoryOnly());
		engine.setSaveTo(new File(args[1]));
		
		try {
			engine.execute(trans);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		System.out.println("End");
	}

}
