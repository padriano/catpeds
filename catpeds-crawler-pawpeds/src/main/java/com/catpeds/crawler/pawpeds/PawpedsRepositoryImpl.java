/**
 *
 */
package com.catpeds.crawler.pawpeds;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.catpeds.crawler.jsoup.DocumentRepository;
import com.catpeds.model.PedigreeSearchCriteria;
import com.catpeds.model.PedigreeSearchResult;

/**
 * {@link PawpedsRepository} default implementation.
 *
 * @author padriano
 *
 */
class PawpedsRepositoryImpl implements PawpedsRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PawpedsRepositoryImpl.class);

	private DocumentRepository documentRepository;
	private PawpedsUrlService pawpedsUrlService;
	private PawpedsDocumentParser pawpedsSearchResultParser;

	@Inject
	PawpedsRepositoryImpl(DocumentRepository documentRepository, PawpedsUrlService pawpedsUrlService,
			PawpedsDocumentParser pawpedsSearchResultParser) {
		this.documentRepository = documentRepository;
		this.pawpedsUrlService = pawpedsUrlService;
		this.pawpedsSearchResultParser = pawpedsSearchResultParser;
	}

	/**
	 * @see com.catpeds.crawler.pawpeds.PawpedsRepository#findAll(com.catpeds.model.PedigreeSearchCriteria)
	 */
	@Override
	public Collection<PedigreeSearchResult> findAll(PedigreeSearchCriteria criteria) {
		String searchUrl = pawpedsUrlService.getAdvancedSearchUrl(criteria);
		return getAndParsePawpedsDocument(searchUrl, pawpedsSearchResultParser::parseSearch);
	}

	/**
	 * @see com.catpeds.crawler.pawpeds.PawpedsRepository#findAllOffspring(long)
	 */
	@Override
	public Collection<PedigreeSearchResult> findAllOffspring(long id) {
		String offspringSearchUrl = pawpedsUrlService.getOffspringsSearchUrl(id);
		return getAndParsePawpedsDocument(offspringSearchUrl, pawpedsSearchResultParser::parseOffsprings);
	}

	Collection<PedigreeSearchResult> getAndParsePawpedsDocument(String url,
			Function<Document, Collection<PedigreeSearchResult>> parserFunction) {
		try {
			Optional<Document> searchDocument = documentRepository.get(url);
			if (searchDocument.isPresent()) {
				// successful retrieving the document at first try
				return parserFunction.apply(searchDocument.get());
			}
			// this means that there was a timeout. retrying...
			searchDocument = documentRepository.get(url);
			if (searchDocument.isPresent()) {
				return parserFunction.apply(searchDocument.get());
			}

			LOGGER.warn("Timeout retrieving document from {}", url);

		} catch (IOException e) {
			LOGGER.error("Unexpected exception occurred", e);
		}

		return Arrays.asList();
	}
}
