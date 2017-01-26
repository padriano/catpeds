/**
 *
 */
package com.catpeds.crawler.jsoup;

import java.io.IOException;
import java.util.Optional;

import org.jsoup.nodes.Document;

/**
 * Repository to retrieve HTML {@link Document} from an URL.
 *
 * @author padriano
 *
 */
public interface DocumentRepository {

	/**
	 * Retrieves the HTML document from the specified URL and parses it into a
	 * {@link Document}.
	 *
	 * @param url
	 *            URL to extract the document from
	 * @return {@link Optional} with the parsed {@link Document} or empty on
	 *         socket timeout for the given url.
	 * @throws IOException
	 *             on error
	 */
	Optional<Document> get(String url) throws IOException;
}
