## Fluent API to apply transformations on PDF pages using the Apache PDFBox

## Main Interface :
```java
public interface PdfTransformation {
	//Other default methods were removed from here to clarity
	
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

```
## Existing transformations :
- `Identity` : does nothing
- `EmbeddedFilesAndJsPdfTrans` that sets the javascripts and embedded files to null
- `RemoveTokensPdfTrans` with three existing variants : `containsString`, `regex`, `cosNameStartWith` and you can create new one by passing a `Predicate<Object>` to its constructor. 

## Dependencies :
```xml
	<!-- Java 1.8 -->
	<dependency>
		<groupId>org.bouncycastle</groupId>
		<artifactId>bcprov-jdk15on</artifactId>
		<version>1.63</version>
	</dependency>
	<dependency>
		<groupId>org.apache.pdfbox</groupId>
		<artifactId>pdfbox</artifactId>
		<version>2.0.17</version>
	</dependency>
```

## Steps :
0. Download the jar file from `releases` folder and add it to your project build path
1. Build a transformations chain starting from `PdfTransformation.identity()` or `PdfTransformation.defaults()`
2. To create custom transformations you need to implements the interface `PdfTransformation` then add them to the chain in the first step.
3. Create an object of PdfTransformer using the `PdfTransformer.from` method and set the `saveTo` file path (Where the resulting PDF file should be saved)
4. Execute the transformer

## Example :
```java
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
		//engine.setPassword("");
		//engine.setMemoryUsageStrategy(MemoryUsageSetting.setupMainMemoryOnly());
		engine.setSaveTo(new File(args[1]));
		
		try {
			engine.execute(trans);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		System.out.println("End");
	}

}

```

## Credit :
To thebabush/pdf-strip-watermark