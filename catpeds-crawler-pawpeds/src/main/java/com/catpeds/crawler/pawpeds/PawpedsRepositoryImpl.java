/**
 *
 */
package com.catpeds.crawler.pawpeds;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.inject.Inject;

import org.jsoup.nodes.Document;

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

		try {
			Optional<Document> searchDocument = documentRepository.get(searchUrl);
			if (searchDocument.isPresent()) {
				// successful retrieving the document at first try
				return pawpedsSearchResultParser.parseSearch(searchDocument.get());

			} else {
				// this means that there was a timeout. retrying...
				searchDocument = documentRepository.get(searchUrl);
				return pawpedsSearchResultParser.parseSearch(searchDocument.get());
			}

		} catch (NoSuchElementException e) {
			// TODO log failed criteria and return empty
		} catch (IOException e) {
			// TODO log exception
		}

		return Arrays.asList();
	}
}
