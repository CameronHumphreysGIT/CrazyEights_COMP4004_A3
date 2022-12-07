import com.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes= CrazyEightsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class Tester {
    @Autowired
    GameController gc;

    @LocalServerPort
    private int port;

    @Nested
    @DisplayName("ConnectionTests")
    class ConnectionTests {
        @Test
        @DisplayName("PlayerJoinTest")
        void PlayerJoinTest() throws InterruptedException {
            //helper does test for us now
            WebDriver driver = playerJoin("Cameron", 1);
            //teardown
            driver.close();
            //reset the game object for the next test
            gc.reset();
        }
        @Test
        @DisplayName("ThreePlayerJoinTest")
        void ThreePlayerJoinTest() {
            //first player joins
            WebDriver driver1 = playerJoin("Cameron", 1);
            assertEquals("Waiting...0 other players", driver1.findElement(By.id("status")).getText());
            //second player joins
            WebDriver driver2 = playerJoin("Micheal", 2);
            assertEquals("Waiting...1 other players", driver1.findElement(By.id("status")).getText());
            assertEquals("Waiting...1 other players", driver2.findElement(By.id("status")).getText());
            //third player joins
            WebDriver driver3 = playerJoin("Joey", 3);
            assertEquals("Waiting...2 other players", driver2.findElement(By.id("status")).getText());
            assertEquals("Waiting...2 other players", driver3.findElement(By.id("status")).getText());
            //player1 has option to close lobby
            assertEquals("Three total players, would you like to wait for another?", driver1.findElement(By.id("status")).getText());
            driver1.findElement(By.id("No")).click();
            try {
                Thread.sleep(100);
            }  catch(Exception e) {
                System.out.println(e.getMessage());
            }
            assertEquals("In Game", driver1.findElement(By.id("status")).getText());
            try {
                Thread.sleep(1000);
            }  catch(Exception e) {
                System.out.println(e.getMessage());
            }
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver1.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver2.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver3.findElement(By.id("status")).getText());
            //teardown
            driver1.close();
            driver2.close();
            driver3.close();
            //reset the game object for the next test
            gc.reset();
        }
        @Test
        @DisplayName("FourPlayerJoinTest")
        void FourPlayerJoinTest() {
            //first player joins
            WebDriver driver1 = playerJoin("Cameron", 1);
            assertEquals("Waiting...0 other players", driver1.findElement(By.id("status")).getText());
            //second player joins
            WebDriver driver2 = playerJoin("Micheal", 2);
            assertEquals("Waiting...1 other players", driver1.findElement(By.id("status")).getText());
            assertEquals("Waiting...1 other players", driver2.findElement(By.id("status")).getText());
            //third player joins
            WebDriver driver3 = playerJoin("Joey", 3);
            assertEquals("Waiting...2 other players", driver2.findElement(By.id("status")).getText());
            assertEquals("Waiting...2 other players", driver3.findElement(By.id("status")).getText());
            //player1 has option to close lobby
            assertEquals("Three total players, would you like to wait for another?", driver1.findElement(By.id("status")).getText());
            driver1.findElement(By.id("Yes")).click();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            assertEquals("Waiting...2 other players", driver1.findElement(By.id("status")).getText());
            assertEquals("Waiting...2 other players", driver2.findElement(By.id("status")).getText());
            assertEquals("Waiting...2 other players", driver3.findElement(By.id("status")).getText());
            WebDriver driver4 = playerJoin("Hans", 4);
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver1.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver2.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver3.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver4.findElement(By.id("status")).getText());

            //teardown
            driver1.close();
            driver2.close();
            driver3.close();
            driver4.close();
            //reset the game object for the next test
            gc.reset();
        }
        @Test
        @DisplayName("GameTest")
        void GameTest() {
            //three players join
            WebDriver driver1 = playerJoin("Cameron", 1);
            WebDriver driver2 = playerJoin("Mike", 2);
            WebDriver driver3 = playerJoin("Ross", 3);
            driver1.findElement(By.id("No")).click();
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver1.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver2.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order: left(incrementing), next: 2", driver3.findElement(By.id("status")).getText());
            // deck should be 52 - 15 = 37 - 1 for topcard
            assertEquals("36", driver1.findElement(By.id("deck")).getText());
            assertEquals("36", driver2.findElement(By.id("deck")).getText());
            assertEquals("36", driver3.findElement(By.id("deck")).getText());
            //that's all while i still can't rig the game.
            //teardown
            driver1.close();
            driver2.close();
            driver3.close();
            //reset the game object for the next test
            gc.reset();
        }
    }
    //helpers
    WebDriver playerJoin(String name, int num) {
        //fixture
        WebDriverLibrary wdl = new WebDriverLibrary();
        WebDriver driver = wdl.getChromeDriver();
        driver.get("http://localhost:" + port);

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
        assertEquals("http://localhost:" + port + "/game.html", driver.getCurrentUrl());
        //make sure it says my name and player number
        assertEquals("Welcome " + name + " to Crazy Eights, you are Player" + num, driver.findElement(By.id("welcome")).getText());
        return driver;
    }
}
