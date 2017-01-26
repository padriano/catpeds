/**
 *
 */
package com.catpeds.crawler.jsoup;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * {@link DocumentRepository} default implementation using jsoup.
 *
 * @author padriano
 *
 */
class DocumentRepositoryImpl implements DocumentRepository {

	/**
	 * @see com.catpeds.crawler.pawpeds.DocumentRepository#get(java.lang.String)
	 */
	@Override
	public Optional<Document> get(String url) throws IOException {
		try {
			return Optional.of(Jsoup.connect(url).get());
		} catch (SocketTimeoutException e) {
			return Optional.empty();
		} catch (IOException e) {
			throw e;
		}
	}

}
