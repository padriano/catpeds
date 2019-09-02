/**
 *
 */
package com.catpeds.crawler.jsoup;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;

import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DocumentRepository} default implementation using jsoup.
 *
 * @author padriano
 *
 */
@Named
class DocumentRepositoryImpl implements DocumentRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRepositoryImpl.class);

	/**
	 * @see DocumentRepository#get(String)
	 */
	@Override
	public Optional<Document> get(String url) throws IOException {
		try {
			return Optional.of(Jsoup.connect(url).get());
		} catch (SocketTimeoutException e) {
			LOGGER.warn("Timeout retrieving: {} \n{}", url, e);
			return Optional.empty();
		} catch (IOException e) {
			throw e;
		}
	}

}
