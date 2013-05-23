package com.mycompany.myproject.gui;
import com.polopoly.ps.psselenium.SimpleWebDriverTestBase;
import org.junit.Test;
import org.openqa.selenium.By;

public class GUILoginIT extends SimpleWebDriverTestBase {

    @Test
    public void edmund_login() {
        guiAgent().agentLogin().login("edmund", "edmund");
        guiAgent().agentFrame().selectUserSessionFrame();
        guiAgent().getWebDriver().findElement(
                By.xpath("//span[@class='userSettings']/a[contains(text(), 'Edmund Green')]"));

    }


}
