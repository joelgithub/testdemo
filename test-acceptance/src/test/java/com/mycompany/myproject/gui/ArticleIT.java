package com.mycompany.myproject.gui;


import com.google.inject.Inject;
import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.testbase.ImportTestContent;
import com.polopoly.testbase.TestBaseRunner;
import example.content.article.StandardArticlePolicy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestBaseRunner.class)
@ImportTestContent(files = {"article.content"}, once = true)
public class ArticleIT {

    @Inject
    private PolicyCMServer cmServer;

    @Test
    public void readTestArticle() throws CMException {
        StandardArticlePolicy article =(StandardArticlePolicy)cmServer.getPolicy(
                new ExternalContentId("myproject.test.article"));
        Assert.assertEquals("Test article", article.getName());
    }

}