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
import com.catpeds.model.Pedigree;
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

	<T> T getAndParsePawpedsDocument(String url,
			Function<Document, T> parserFunction) {
		return getAndParsePawpedsDocument(url, parserFunction, null);
	}

	<T> T getAndParsePawpedsDocument(String url,
			Function<Document, T> parserFunction, T defaultResult) {
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

		return defaultResult;
	}

	/**
	 * @see com.catpeds.crawler.pawpeds.PawpedsRepository#findAll(com.catpeds.model.PedigreeSearchCriteria)
	 */
	@Override
	public Collection<PedigreeSearchResult> findAll(PedigreeSearchCriteria criteria) {
		String searchUrl = pawpedsUrlService.getAdvancedSearchUrl(criteria);
		return getAndParsePawpedsDocument(searchUrl, pawpedsSearchResultParser::parseSearch, Arrays.asList());
	}

	/**
	 * @see com.catpeds.crawler.pawpeds.PawpedsRepository#findAllOffspring(long)
	 */
	@Override
	public Collection<PedigreeSearchResult> findAllOffspring(long id) {
		String offspringSearchUrl = pawpedsUrlService.getOffspringsSearchUrl(id);
		return getAndParsePawpedsDocument(offspringSearchUrl, pawpedsSearchResultParser::parseOffsprings,
				Arrays.asList());
	}

	/**
	 * @see com.catpeds.crawler.pawpeds.PawpedsRepository#findOne(long)
	 */
	@Override
	public Optional<Pedigree> findOne(long id) {
		// get pedigree information
		String offspringSearchUrl = pawpedsUrlService.getPedigreeUrl(id);
		Optional<Pedigree> pedigree = getAndParsePawpedsDocument(offspringSearchUrl, pawpedsSearchResultParser::parsePedigree,
				Optional.empty());

		// populate with the offspring list
		if (pedigree.isPresent()) {
			findAllOffspring(id).stream().mapToLong(r -> r.getId()).forEach(pedigree.get()::addOffspring);
		}

		return pedigree;
	}
}
