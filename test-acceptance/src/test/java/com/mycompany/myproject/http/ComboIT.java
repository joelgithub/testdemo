package com.mycompany.myproject.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.polopoly.cm.ContentId;
import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.VersionedContentId;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.management.ServiceNotAvailableException;
import com.polopoly.search.solr.SearchResult;
import com.polopoly.search.solr.SearchResultPage;
import com.polopoly.search.solr.SolrSearchClient;
import com.polopoly.testbase.ImportTestContent;
import com.polopoly.testbase.TestBaseRunner;
import com.polopoly.webtest.WebTest;

@RunWith(TestBaseRunner.class)
@ImportTestContent
public class ComboIT {

	 @Inject
	 private PolicyCMServer cmServer;
	 	 
	 @Inject
	 public WebTest webTest;
	 
	 @Inject
	 private SolrSearchClient solrClientPublic;
	
	 
	@Test
    public void surfArticleByNumericId() throws Exception {
		VersionedContentId id = cmServer.findContentIdByExternalId(new ExternalContentId("ComboIT.content"));
		String articleId = id.getContentId().getContentIdString();
        webTest.loadPage("/cmlink/" + articleId);
        webTest.assertContainsHtml("Combo Article");
    }

	@Test
    public void surfArticleByExternalId() throws Exception {
        webTest.loadPage("/cmlink/" + "ComboIT.content");
        webTest.assertContainsHtml("Combo Article");
    }
	
	@Test
	@ImportTestContent(once=true, waitUntilContentsAreIndexed={"ComboIT.searchForArticle.content"})
	public void searchForArticle() throws SolrServerException, ServiceNotAvailableException, CMException {
		
		SolrQuery query = new SolrQuery("text:Search Article");
		SearchResult searchResult = solrClientPublic.search(query , 1);
		SearchResultPage page = searchResult.getPage(0);
		List<ContentId> hits = page.getHits();
		assertTrue(hits.size() > 0);
		ContentId contentId = hits.get(0);
		
		VersionedContentId testArticleId = cmServer.findContentIdByExternalId(new ExternalContentId("ComboIT.searchForArticle.content"));
		
		assertEquals(testArticleId.getContentId(), contentId);
		cmServer.removeContent(testArticleId);
	}
}
