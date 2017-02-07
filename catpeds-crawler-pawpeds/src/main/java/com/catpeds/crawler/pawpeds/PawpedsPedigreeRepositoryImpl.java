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
import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.catpeds.crawler.jsoup.DocumentRepository;
import com.catpeds.model.Pedigree;
import com.catpeds.model.PedigreeCriteria;

/**
 * {@link PawpedsPedigreeRepository} default implementation.
 *
 * @author padriano
 *
 */
@Named
class PawpedsPedigreeRepositoryImpl implements PawpedsPedigreeRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PawpedsPedigreeRepositoryImpl.class);

	private DocumentRepository documentRepository;
	private PawpedsUrlService pawpedsUrlService;
	private PawpedsDocumentParser pawpedsSearchResultParser;

	@Inject
	PawpedsPedigreeRepositoryImpl(DocumentRepository documentRepository, PawpedsUrlService pawpedsUrlService,
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
	 * @see com.catpeds.crawler.pawpeds.PawpedsPedigreeRepository#findAll(com.catpeds.model.PedigreeCriteria)
	 */
	@Override
	public Collection<Pedigree> findAll(PedigreeCriteria criteria) {
		LOGGER.info("findAll with criteria {}", criteria);

		String searchUrl = pawpedsUrlService.getAdvancedSearchUrl(criteria);
		LOGGER.debug("URL for advanced search {}", searchUrl);
		return getAndParsePawpedsDocument(searchUrl, pawpedsSearchResultParser::parseSearch, Arrays.asList());
	}

	/**
	 * @see com.catpeds.crawler.pawpeds.PawpedsPedigreeRepository#findAllOffspring(long)
	 */
	@Override
	public Collection<Pedigree> findAllOffspring(long id) {
		LOGGER.info("findAllOffspring with id {}", id);

		String offspringSearchUrl = pawpedsUrlService.getOffspringsSearchUrl(id);
		LOGGER.debug("URL for offsprings search {}", offspringSearchUrl);
		return getAndParsePawpedsDocument(offspringSearchUrl, pawpedsSearchResultParser::parseOffsprings,
				Arrays.asList());
	}

	/**
	 * @see com.catpeds.crawler.pawpeds.PawpedsPedigreeRepository#findOne(long)
	 */
	@Override
	public Optional<Pedigree> findOne(long id) {
		LOGGER.info("findOne with id {}", id);

		// get pedigree information
		String pedigreeSearchUrl = pawpedsUrlService.getPedigreeUrl(id);
		LOGGER.debug("URL for pedigree search {}", pedigreeSearchUrl);
		Optional<Pedigree> pedigree = getAndParsePawpedsDocument(pedigreeSearchUrl, pawpedsSearchResultParser::parsePedigree,
				Optional.empty());

		// populate with the offspring list
		if (pedigree.isPresent()) {
			findAllOffspring(id).stream().mapToLong(Pedigree::getId).forEach(pedigree.get()::addOffspring);
		}

		return pedigree;
	}
}
