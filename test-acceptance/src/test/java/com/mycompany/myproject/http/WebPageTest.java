package com.mycompany.myproject.http;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.inject.Inject;
import com.polopoly.cm.client.CMException;
import com.polopoly.webtest.WebTest;
import com.polopoly.webtest.WebTestRunner;

@RunWith(WebTestRunner.class)
public class WebPageTest {

    @Inject
    public WebTest webTest;

    @Test
    public void testCookie() throws CMException {
        Cookie cookie = new Cookie(webTest.getDomain(), "the-cookie", "cookie monster");

        webTest.addCookie(cookie);
        webTest.assertCookieSet("the-cookie");
        webTest.assertCookieValue("the-cookie", "cookie monster");
    }
    
    @Test
    public void surfDispatcher() throws Exception
    {
        webTest.loadPage("/");
        webTest.assertContainsHtml("Greenfield");
    }
    @Test
    public void surfGui() throws Exception
    {
        webTest.loadPage("/polopoly");
        webTest.assertContainsHtml("Polopoly");
    }
   

}