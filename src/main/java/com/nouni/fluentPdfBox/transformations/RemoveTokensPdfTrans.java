package com.nouni.fluentPdfBox.transformations;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;

import com.nouni.fluentPdfBox.PdfTransformation;

/**
 * @author NOUNI EL Bachir
 * 
 */
public class RemoveTokensPdfTrans implements PdfTransformation {
	/*
	 * The code used here, in "apply" and "remove" was inspired from this repo
	 * "thebabush/pdf-strip-watermark"
	 */
	private Predicate<Object> shouldRemoveToken;

	public RemoveTokensPdfTrans(Predicate<Object> removeToken) throws IllegalArgumentException {
		this.shouldRemoveToken = removeToken;
	}

	@Override
	public void apply(PDDocument pdf, PDPage page, int pageNnumber) throws Exception {
		page.setAnnotations(new ArrayList<>());

		PDFStreamParser parser = new PDFStreamParser(page);
		parser.parse();

		ArrayList<Object> tokens = new ArrayList<>(parser.getTokens());
		ArrayList<Integer> remove = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Object token = tokens.get(i);
			if (shouldRemoveToken.test(token))
				i = removeState(tokens, remove, i);
		}

		remove.sort((o, t1) -> t1 - o);
		for (Integer i : remove) {
			tokens.remove((int) i);
		}

		PDStream newContent = new PDStream(pdf);
		OutputStream out = newContent.createOutputStream();
		ContentStreamWriter writer = new ContentStreamWriter(out);
		writer.writeTokens(tokens);
		out.close();
		page.setContents(newContent);
	}

	private static int removeState(ArrayList<Object> tokens, ArrayList<Integer> remove, int i) {
		remove.add(i);
		for (int j = i - 1; j >= 0; j--) {
			Object token = tokens.get(j);
			remove.add(j);
			if (token instanceof Operator) {
				if (((Operator) token).getName().equals("q")) {
					break;
				}
			}
		}
		for (int j = i + 1; j < tokens.size(); j++) {
			i = j;
			Object token = tokens.get(j);
			remove.add(j);
			if (token instanceof Operator) {
				if (((Operator) token).getName().equals("Q")) {
					break;
				}
			}
		}

		return i + 1;
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws IllegalArgumentException if the text parameter is empty
	 */
	public static Predicate<Object> containsText(String text) throws IllegalArgumentException {
		if (text == null || text.isEmpty())
			throw new IllegalArgumentException("textOrRegex should not be empty or null");
		return (token) -> {
			if (token instanceof COSString) {
				String str = ((COSString) token).getString();

				return str.contains(text);
			}
			return false;
		};
	}

	/**
	 * 
	 * @param regex
	 * @return
	 * @throws IllegalArgumentException if the regex parameter is an invalid regex
	 */
	public static Predicate<Object> regex(String regex) throws IllegalArgumentException {
		Pattern.compile(regex);
		return (token) -> {
			if (token instanceof COSString) {
				String str = ((COSString) token).getString();
				return str.matches(regex);
			}
			return false;
		};
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws IllegalArgumentException if the text parameter is empty
	 */
	public static Predicate<Object> cosNameStartsWith(String text) throws IllegalArgumentException {
		if (text == null || text.isEmpty())
			throw new IllegalArgumentException("textOrRegex should not be empty or null");
		return (token) -> {
			if (token instanceof COSName) {
				COSName cosname = (COSName) token;
				return cosname.getName().startsWith(text);
			}
			return false;
		};
	}

	/**
	 * 
	 * @param removeToken a predicate that tells if this transformation should
	 *                    remove the given token from the page or not
	 * @return
	 */
	public static PdfTransformation from(Predicate<Object> removeToken) {
		return new RemoveTokensPdfTrans(removeToken);
	}
}
