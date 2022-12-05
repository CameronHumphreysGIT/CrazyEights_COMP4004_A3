import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

public class Tester {
    @Nested
    @DisplayName("ConnectionTests")
    class ConnectionTests {
        @Test
        @DisplayName("PlayerJoinTest")
        void PlayerJoinTest() {
            //fixture
            WebDriverLibrary wdl = new WebDriverLibrary();
            WebDriver driver = wdl.getChromeDriver();
            String name = "Cameron";

            //get localhost, this will be for all tests
            driver.get("http://localhost:8080");

            //test

            //wait a bit
            try {
                Thread.sleep(2000);
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
            //type in a name
            driver.findElement(By.id("name")).sendKeys(name);
            //wait a bit
            try {
                Thread.sleep(500);
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
            //click send
            driver.findElement(By.id("join")).click();
            //wait a lot
            try {
                Thread.sleep(4000);
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
            //make sure we are now on the game page
            assertEquals("http://localhost:8080/game.html", driver.getCurrentUrl());
            //make sure it says my name and player number
            assertEquals("Welcome " + name + " to Crazy Eights, you are Player1", driver.findElement(By.id("welcome")).getText());
        }
    }
}
