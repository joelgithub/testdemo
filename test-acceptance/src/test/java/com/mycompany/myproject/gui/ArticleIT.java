package com.mycompany.myproject.gui;


import com.google.inject.Inject;
import com.polopoly.cm.ContentId;
import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.ps.psselenium.SimpleWebDriverTestBase;
import com.polopoly.search.solr.PostFilteredSolrSearchClient;
import com.polopoly.search.solr.SearchResultPage;
import com.polopoly.testbase.ImportTestContent;
import com.polopoly.testbase.TestBaseRunner;
import example.content.article.StandardArticlePolicy;
import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@RunWith(TestBaseRunner.class)
@ImportTestContent(files = {"article.content"}, once = true)
public class ArticleIT extends SimpleWebDriverTestBase {

    @Inject
    private PolicyCMServer cmServer;

    @Inject
    private PostFilteredSolrSearchClient internalSearchClient;

    @Test
    public void readTestArticle() throws CMException {
        StandardArticlePolicy article =(StandardArticlePolicy)cmServer.getPolicy(
                new ExternalContentId("myproject.test.article"));
        Assert.assertEquals("Test article", article.getName());
    }

    @Test
    public void search_for_test_article_in_gui() throws CMException, InterruptedException {
        StandardArticlePolicy testArticle =(StandardArticlePolicy)cmServer.getPolicy(new ExternalContentId("myproject.test.article"));
        waitForInternalIndex(testArticle.getContentId().getContentId());
        guiAgent().agentLogin().loginAsSysadmin();
        guiAgent().agentSearch().search("Test article");
        Assert.assertTrue(guiAgent().agentSearch().isPresentInSearchResult("just playing around"));
    }

    public void waitForInternalIndex(ContentId unversionedContentId) throws InterruptedException {
        int tries = 5;
        while (tries > 0) {
            TimeUnit.SECONDS.sleep(60);
            Iterator<SearchResultPage> searchResult = internalSearchClient.filteredSearch(
                    new SolrQuery("title:Test article"), 10).iterator();
            while (searchResult.hasNext()) {
                SearchResultPage page = searchResult.next();
                for (ContentId contentId : page.getHits()) {
                    if (contentId.getContentId().equals(unversionedContentId)) return;
                }
            }
            tries--;
        }
        Assert.fail("Content " + unversionedContentId.getContentIdString() +
                " was not found in index after 5 minutes");
    }



}