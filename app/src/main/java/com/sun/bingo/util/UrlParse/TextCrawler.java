package com.sun.bingo.util.UrlParse;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextCrawler {

	public static final int ALL = -1;
	public static final int NONE = -2;

	private final String HTTP_PROTOCOL = "http://";
	private final String HTTPS_PROTOCOL = "https://";

	private LinkViewCallback callback;

	public TextCrawler() {
	}

	public void makePreview(LinkViewCallback callback, String url) {
		this.callback = callback;
		new GetCode(ALL).execute(url);
	}

	public void makePreview(LinkViewCallback callback, String url,
							int imageQuantity) {
		this.callback = callback;
		new GetCode(imageQuantity).execute(url);
	}

	/** Get html code */
	public class GetCode extends AsyncTask<String, Void, Void> {

		private LinkSourceContent linkSourceContent = new LinkSourceContent();
		private int imageQuantity;
		private ArrayList<String> urls;

		public GetCode(int imageQuantity) {
			this.imageQuantity = imageQuantity;
		}

		@Override
		protected void onPreExecute() {
			if (callback != null) {
				callback.onBeforeLoading();
			}
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			if (callback != null) {
				callback.onAfterLoading(linkSourceContent, isNull());
			}
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(String... params) {
			// Don't forget the http:// or https://
			urls = Utils.matches(params[0]);

			if (urls.size() > 0)
				linkSourceContent
						.setFinalUrl(unshortenUrl(extendedTrim(urls.get(0))));
			else
				linkSourceContent.setFinalUrl("");

			if (!linkSourceContent.getFinalUrl().equals("")) {
				if (isImage(linkSourceContent.getFinalUrl())
						&& !linkSourceContent.getFinalUrl().contains("dropbox")) {
					linkSourceContent.setSuccess(true);

					linkSourceContent.getImages().add(linkSourceContent.getFinalUrl());

					linkSourceContent.setTitle("");
					linkSourceContent.setDescription("");

				} else {
					try {
						Document doc = Jsoup
								.connect(linkSourceContent.getFinalUrl())
								.userAgent("Mozilla").get();

						linkSourceContent.setHtmlCode(extendedTrim(doc.toString()));

						HashMap<String, String> metaTags = getMetaTags(linkSourceContent
								.getHtmlCode());

						linkSourceContent.setMetaTags(metaTags);

						linkSourceContent.setTitle(metaTags.get("title"));
						linkSourceContent.setDescription(metaTags
								.get("description"));

						if (linkSourceContent.getTitle().equals("")) {
							String matchTitle = Utils.pregMatch(
									linkSourceContent.getHtmlCode(),
									Constants.TITLE_PATTERN, 2);

							if (!matchTitle.equals(""))
								linkSourceContent.setTitle(htmlDecode(matchTitle));
						}

						if (linkSourceContent.getDescription().equals(""))
							linkSourceContent
									.setDescription(crawlCode(linkSourceContent
											.getHtmlCode()));

						linkSourceContent.setDescription(linkSourceContent
								.getDescription().replaceAll(
										Constants.SCRIPT_PATTERN, ""));

						if (imageQuantity != NONE) {
							if (!metaTags.get("image").equals(""))
								linkSourceContent.getImages().add(
										metaTags.get("image"));
							else {
								linkSourceContent.setImages(getImages(doc,
										imageQuantity));
							}
						}

						linkSourceContent.setSuccess(true);
					} catch (Exception e) {
						linkSourceContent.setSuccess(false);
					}
				}
			}

			String[] finalLinkSet = linkSourceContent.getFinalUrl().split("&");
			linkSourceContent.setUrl(finalLinkSet[0]);

			linkSourceContent.setCannonicalUrl(cannonicalPage(linkSourceContent
					.getFinalUrl()));
			linkSourceContent.setDescription(stripTags(linkSourceContent
					.getDescription()));

			return null;
		}

		/** Verifies if the content could not be retrieved */
		public boolean isNull() {
			return !linkSourceContent.isSuccess() &&
				extendedTrim(linkSourceContent.getHtmlCode()).equals("") &&
				!isImage(linkSourceContent.getFinalUrl());
		}

	}

	/** Gets content from a html tag */
	private String getTagContent(String tag, String content) {

		String pattern = "<" + tag + "(.*?)>(.*?)</" + tag + ">";
		String result = "", currentMatch = "";

		List<String> matches = Utils.pregMatchAll(content, pattern, 2);
		
		int matchesSize = matches.size();
		for (int i = 0; i < matchesSize; i++) {
			currentMatch = stripTags(matches.get(i));
			if (currentMatch.length() >= 120) {
				result = extendedTrim(currentMatch);
				break;
			}
		}

		if (result.equals("")) {
			String matchFinal = Utils.pregMatch(content, pattern, 2);
			result = extendedTrim(matchFinal);
		}

		result = result.replaceAll("&nbsp;", "");

		return htmlDecode(result);
	}

	/** Gets images from the html code */
	public List<String> getImages(Document document, int imageQuantity) {
		List<String> matches = new ArrayList<String>();

		Elements media = document.select("[src]");

		for (Element srcElement : media) {
			if (srcElement.tagName().equals("img")) {
				matches.add(srcElement.attr("abs:src"));
			}
		}

		if (imageQuantity != ALL)
			matches = matches.subList(0, imageQuantity);

		return matches;
	}

	/** Transforms from html to normal string */
	private String htmlDecode(String content) {
		return Jsoup.parse(content).text();
	}

	/** Crawls the code looking for relevant information */
	private String crawlCode(String content) {
		String result = "";
		String resultSpan = "";
		String resultParagraph = "";
		String resultDiv = "";

		resultSpan = getTagContent("span", content);
		resultParagraph = getTagContent("p", content);
		resultDiv = getTagContent("div", content);

		result = resultSpan;

		if (resultParagraph.length() > resultSpan.length()
				&& resultParagraph.length() >= resultDiv.length())
			result = resultParagraph;
		else if (resultParagraph.length() > resultSpan.length()
				&& resultParagraph.length() < resultDiv.length())
			result = resultDiv;
		else
			result = resultParagraph;

		return htmlDecode(result);
	}

	/** Returns the cannoncial url */
	private String cannonicalPage(String url) {

		String cannonical = "";
		if (url.startsWith(HTTP_PROTOCOL)) {
			url = url.substring(HTTP_PROTOCOL.length());
		} else if (url.startsWith(HTTPS_PROTOCOL)) {
			url = url.substring(HTTPS_PROTOCOL.length());
		}

		int urlLength = url.length();
		for (int i = 0; i < urlLength; i++) {
			if (url.charAt(i) != '/')
				cannonical += url.charAt(i);
			else
				break;
		}

		return cannonical;

	}

	/** Strips the tags from an element */
	private String stripTags(String content) {
		return Jsoup.parse(content).text();
	}

	/** Verifies if the url is an image */
	private boolean isImage(String url) {
		return url.matches(Constants.IMAGE_PATTERN);
	}

	/**
	 * Returns meta tags from html code
	 */
	private HashMap<String, String> getMetaTags(String content) {

		HashMap<String, String> metaTags = new HashMap<String, String>();
		metaTags.put("url", "");
		metaTags.put("title", "");
		metaTags.put("description", "");
		metaTags.put("image", "");

		List<String> matches = Utils.pregMatchAll(content,
				Constants.METATAG_PATTERN, 1);

		for (String match : matches) {
			if (match.toLowerCase().contains("property=\"og:url\"")
					|| match.toLowerCase().contains("property='og:url'")
					|| match.toLowerCase().contains("name=\"url\"")
					|| match.toLowerCase().contains("name='url'"))
				metaTags.put("url", separeMetaTagsContent(match));
			else if (match.toLowerCase().contains("property=\"og:title\"")
					|| match.toLowerCase().contains("property='og:title'")
					|| match.toLowerCase().contains("name=\"title\"")
					|| match.toLowerCase().contains("name='title'"))
				metaTags.put("title", separeMetaTagsContent(match));
			else if (match.toLowerCase()
					.contains("property=\"og:description\"")
					|| match.toLowerCase()
					.contains("property='og:description'")
					|| match.toLowerCase().contains("name=\"description\"")
					|| match.toLowerCase().contains("name='description'"))
				metaTags.put("description", separeMetaTagsContent(match));
			else if (match.toLowerCase().contains("property=\"og:image\"")
					|| match.toLowerCase().contains("property='og:image'")
					|| match.toLowerCase().contains("name=\"image\"")
					|| match.toLowerCase().contains("name='image'"))
				metaTags.put("image", separeMetaTagsContent(match));
		}

		return metaTags;
	}

	/** Gets content from metatag */
	private String separeMetaTagsContent(String content) {
		String result = Utils.pregMatch(content, Constants.METATAG_CONTENT_PATTERN,
				1);
		return htmlDecode(result);
	}

	/**
	 * Unshortens a short url
	 */
	private String unshortenUrl(String shortURL) {
		if (!shortURL.startsWith(HTTP_PROTOCOL)
				&& !shortURL.startsWith(HTTPS_PROTOCOL))
			return "";

		URLConnection urlConn = connectURL(shortURL);
		urlConn.getHeaderFields();

		String finalResult = urlConn.getURL().toString();

		urlConn = connectURL(finalResult);
		urlConn.getHeaderFields();

		shortURL = urlConn.getURL().toString();

		while (!shortURL.equals(finalResult)) {
			finalResult = unshortenUrl(finalResult);
		}

		return finalResult;
	}

	/**
	 * Takes a valid url and return a URL object representing the url address.
	 */
	private URLConnection connectURL(String strURL) {
		URLConnection conn = null;
		try {
			URL inputURL = new URL(strURL);
			conn = inputURL.openConnection();
		} catch (MalformedURLException e) {
			System.out.println("Please input a valid URL");
		} catch (IOException ioe) {
			System.out.println("Can not connect to the URL");
		}
		return conn;
	}

	/** Removes extra spaces and trim the string */
	public static String extendedTrim(String content) {
		return content.replaceAll("\\s+", " ").replace("\n", " ")
				.replace("\r", " ").trim();
	}

}
