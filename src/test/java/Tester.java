import com.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

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
            //wait
            myWait(0.1);

            assertEquals("In Game", driver1.findElement(By.id("status")).getText());
            //wait
            myWait(1.0);

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
            //wait
            myWait(0.1);


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
            myWait(3.0);
            assertEquals("In Game, Round1, Player1's turn turn order:left(incrementing), next: 2", driver1.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order:left(incrementing), next: 2", driver2.findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order:left(incrementing), next: 2", driver3.findElement(By.id("status")).getText());
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
    @Nested
    @DisplayName("AcceptanceTests")
    class AcceptanceTests {
        @Test
        @DisplayName("41Test")
        void FortyOneTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play 3C
            gc.setTopCard("6C");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "3C", "9S", "JD")), 1);
            gc.refresh();
            //check that it's playable
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='3C']")).getSize());
            //then, play that card:
            drivers[0].findElement(By.xpath("//button[text()='3C']")).click();
            //check that we changed the top card
            assertEquals("3C", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("3C", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("3C", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("3C", drivers[3].findElement(By.id("topCard")).getText());
            //check that the player has changed to player 2
            assertEquals("In Game, Round1, Player2's turn turn order:left(incrementing), next: 3", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:left(incrementing), next: 3", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:left(incrementing), next: 3", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:left(incrementing), next: 3", drivers[3].findElement(By.id("status")).getText());
            //good stuff.
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }

        @Test
        @DisplayName("42Test")
        void FortyTwoTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play AH
            gc.setTopCard("6H");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "AH", "3C", "9S", "JD")), 1);
            gc.refresh();
            //check that it's playable
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='AH']")).getSize());
            //then, play that card:
            drivers[0].findElement(By.xpath("//button[text()='AH']")).click();
            //we actually skipped p1 here, so they get an alert
            try {
                myWait(5);
                Alert alert = drivers[1].switchTo().alert();
                alert.accept();
                drivers[1].switchTo().defaultContent();
            } catch (NoAlertPresentException ex) {
                fail();
            }
            //check that we changed the top card
            assertEquals("AH", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("AH", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("AH", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("AH", drivers[3].findElement(By.id("topCard")).getText());
            //check that the player has changed to player 4 (reverse card)
            assertEquals("In Game, Round1, Player4's turn turn order:right(decrementing), next: 3", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player4's turn turn order:right(decrementing), next: 3", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player4's turn turn order:right(decrementing), next: 3", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player4's turn turn order:right(decrementing), next: 3", drivers[3].findElement(By.id("status")).getText());
            //good stuff.
            //set player 4's cards
            gc.setCards(new ArrayList<>(Arrays.asList("7H", "AD", "3C", "9S", "JD")), 4);
            gc.refresh();
            //wait for the refresh
            myWait(3.0);
            //make them play it:
            //check that it's playable
            assertNotEquals(0, drivers[3].findElement(By.xpath("//button[text()='7H']")).getSize());
            //then, play that card:
            drivers[3].findElement(By.xpath("//button[text()='7H']")).click();
            //check next player
            assertEquals("In Game, Round1, Player3's turn turn order:right(decrementing), next: 2", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:right(decrementing), next: 2", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:right(decrementing), next: 2", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:right(decrementing), next: 2", drivers[3].findElement(By.id("status")).getText());
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }

        @Test
        @DisplayName("44Test")
        void FortyFourTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play 3C
            gc.setTopCard("6C");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "QC", "9S", "JD")), 1);
            gc.refresh();
            //check that it's playable
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='QC']")).getSize());
            //then, play that card:
            drivers[0].findElement(By.xpath("//button[text()='QC']")).click();
            //now wait for the alert for player 2, needs to happen here, otherwise it's unexpected
            try {
                myWait(5);
                Alert alert = drivers[1].switchTo().alert();
                alert.accept();
                drivers[1].switchTo().defaultContent();
            } catch (NoAlertPresentException ex) {
                fail();
            }
            //check that we changed the top card
            assertEquals("QC", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("QC", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("QC", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("QC", drivers[3].findElement(By.id("topCard")).getText());
            //check that the player has changed to player 2
            assertEquals("In Game, Round1, Player3's turn turn order:left(incrementing), next: 4", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:left(incrementing), next: 4", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:left(incrementing), next: 4", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:left(incrementing), next: 4", drivers[3].findElement(By.id("status")).getText());
            //good stuff.
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }

        @Test
        @DisplayName("45Test")
        void FortyFiveTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play 3C
            gc.setTopCard("6C");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "7C", "9S", "JD")), 1);
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "JC", "9S", "JD")), 2);
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "2C", "9S", "JD")), 3);
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "3C", "9S", "JD")), 4);
            gc.refresh();
            //everyone plays in sequence
            drivers[0].findElement(By.xpath("//button[text()='7C']")).click();
            drivers[1].findElement(By.xpath("//button[text()='JC']")).click();
            drivers[2].findElement(By.xpath("//button[text()='2C']")).click();
            drivers[3].findElement(By.xpath("//button[text()='3C']")).click();
            //check that we changed the top card
            assertEquals("3C", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("3C", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("3C", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("3C", drivers[3].findElement(By.id("topCard")).getText());
            //check that the player has changed to player 2
            assertEquals("In Game, Round1, Player1's turn turn order:left(incrementing), next: 2", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order:left(incrementing), next: 2", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order:left(incrementing), next: 2", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player1's turn turn order:left(incrementing), next: 2", drivers[3].findElement(By.id("status")).getText());
            //good stuff.
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }

        @Test
        @DisplayName("46Test")
        void FortySixTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play 3C
            gc.setTopCard("6H");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "7H", "9S", "JD")), 1);
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "JH", "9S", "JD")), 2);
            gc.setCards(new ArrayList<>(Arrays.asList("7H", "KD", "2H", "9S", "JD")), 3);
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "AH", "9S", "JD")), 4);
            gc.refresh();
            //everyone plays in sequence
            drivers[0].findElement(By.xpath("//button[text()='7H']")).click();
            myWait(1);
            drivers[1].findElement(By.xpath("//button[text()='JH']")).click();
            myWait(1);
            drivers[2].findElement(By.xpath("//button[text()='2H']")).click();
            myWait(1);
            assertEquals("In Game, Round1, Player4's turn turn order:left(incrementing), next: 1", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player4's turn turn order:left(incrementing), next: 1", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player4's turn turn order:left(incrementing), next: 1", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player4's turn turn order:left(incrementing), next: 1", drivers[3].findElement(By.id("status")).getText());
            drivers[3].findElement(By.xpath("//button[text()='AH']")).click();
            //check that we changed the top card
            //we actually skipped p1 here, so they get an alert
            try {
                myWait(5);
                Alert alert = drivers[0].switchTo().alert();
                alert.accept();
                drivers[0].switchTo().defaultContent();
            } catch (NoAlertPresentException ex) {
                fail();
            }
            assertEquals("AH", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("AH", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("AH", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("AH", drivers[3].findElement(By.id("topCard")).getText());
            //check that the player has changed to player 2
            assertEquals("In Game, Round1, Player3's turn turn order:right(decrementing), next: 2", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:right(decrementing), next: 2", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:right(decrementing), next: 2", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player3's turn turn order:right(decrementing), next: 2", drivers[3].findElement(By.id("status")).getText());
            //p3 plays the 7H
            drivers[2].findElement(By.xpath("//button[text()='7H']")).click();
            assertEquals("In Game, Round1, Player2's turn turn order:right(decrementing), next: 1", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:right(decrementing), next: 1", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:right(decrementing), next: 1", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:right(decrementing), next: 1", drivers[3].findElement(By.id("status")).getText());
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }

        @Test
        @DisplayName("48Test")
        void FortyEightTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play 3C
            gc.setTopCard("6C");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "7C", "9S", "JD")), 1);
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "JC", "9S", "JD")), 2);
            gc.setCards(new ArrayList<>(Arrays.asList("7H", "KD", "2C", "9S", "JD")), 3);
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KD", "QC", "9S", "JD")), 4);
            gc.refresh();
            //everyone plays in sequence
            drivers[0].findElement(By.xpath("//button[text()='7C']")).click();
            myWait(1);
            drivers[1].findElement(By.xpath("//button[text()='JC']")).click();
            myWait(1);
            drivers[2].findElement(By.xpath("//button[text()='2C']")).click();
            myWait(1);
            //check that it's playable
            assertNotEquals(0, drivers[3].findElement(By.xpath("//button[text()='QC']")).getSize());
            //then, play that card:
            drivers[3].findElement(By.xpath("//button[text()='QC']")).click();
            //now wait for the alert for player 2, needs to happen here, otherwise it's unexpected
            try {
                myWait(5);
                Alert alert = drivers[0].switchTo().alert();
                alert.accept();
                drivers[0].switchTo().defaultContent();
            } catch (NoAlertPresentException ex) {
                fail();
            }
            //check that we changed the top card
            assertEquals("QC", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("QC", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("QC", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("QC", drivers[3].findElement(By.id("topCard")).getText());
            //check that the player has changed to player 2
            assertEquals("In Game, Round1, Player2's turn turn order:left(incrementing), next: 3", drivers[0].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:left(incrementing), next: 3", drivers[1].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:left(incrementing), next: 3", drivers[2].findElement(By.id("status")).getText());
            assertEquals("In Game, Round1, Player2's turn turn order:left(incrementing), next: 3", drivers[3].findElement(By.id("status")).getText());
            //good stuff.
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }
        @Test
        @DisplayName("51Test")
        void FiftyOneTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play 3C
            gc.setTopCard("KC");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KH", "3C", "9S", "JD")), 1);
            gc.refresh();
            //check that it's playable
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='KH']")).getSize());
            //then, play that card:
            drivers[0].findElement(By.xpath("//button[text()='KH']")).click();
            //check that we changed the top card
            assertEquals("KH", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("KH", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("KH", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("KH", drivers[3].findElement(By.id("topCard")).getText());
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }
        @Test
        @DisplayName("52Test")
        void FiftyTwoTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play 3C
            gc.setTopCard("KC");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KH", "7C", "9S", "JD")), 1);
            gc.refresh();
            //check that it's playable
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='7C']")).getSize());
            //then, play that card:
            drivers[0].findElement(By.xpath("//button[text()='7C']")).click();
            //check that we changed the top card
            assertEquals("7C", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("7C", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("7C", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("7C", drivers[3].findElement(By.id("topCard")).getText());
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }
        @Test
        @DisplayName("53Test")
        void FiftyThreeTest() {
            //have four players join
            WebDriver[] drivers = fourPlayersJoin(new String[]{"Cam", "Matt", "Alexander", "Cierra"});
            //set the top card so we can play 3C
            gc.setTopCard("KC");
            gc.setCards(new ArrayList<>(Arrays.asList("5H", "KH", "8H", "9S", "JD")), 1);
            gc.refresh();
            //check that it's playable
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='8H']")).getSize());
            //then, play that card:
            drivers[0].findElement(By.xpath("//button[text()='8H']")).click();
            //then, choose a suit
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='S']")).getSize());
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='C']")).getSize());
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='H']")).getSize());
            assertNotEquals(0, drivers[0].findElement(By.xpath("//button[text()='D']")).getSize());
            drivers[0].findElement(By.xpath("//button[text()='C']")).click();
            //check that we changed the top card
            assertEquals("8C", drivers[0].findElement(By.id("topCard")).getText());
            assertEquals("8C", drivers[1].findElement(By.id("topCard")).getText());
            assertEquals("8C", drivers[2].findElement(By.id("topCard")).getText());
            assertEquals("8C", drivers[3].findElement(By.id("topCard")).getText());
            //teardown
            drivers[0].close();
            drivers[1].close();
            drivers[2].close();
            drivers[3].close();
            gc.reset();
        }
    }
    //helpers
    WebDriver playerJoin(String name, int num) {
        //fixture
        WebDriverLibrary wdl = new WebDriverLibrary();
        WebDriver driver = wdl.getChromeDriver();
        driver.get("http://localhost:" + port);

        myWait(2.0);
        //type in a name
        driver.findElement(By.id("name")).sendKeys(name);
        //wait a bit
        myWait(0.5);
        //click send
        driver.findElement(By.id("join")).click();
        //wait a lot
        myWait(4.0);
        //make sure we are now on the game page
        assertEquals("http://localhost:" + port + "/game.html", driver.getCurrentUrl());
        //make sure it says my name and player number
        assertEquals("Welcome " + name + " to Crazy Eights, you are Player" + num, driver.findElement(By.id("welcome")).getText());
        return driver;
    }
    WebDriver[] fourPlayersJoin(String[] names) {
        WebDriverLibrary wdl = new WebDriverLibrary();
        WebDriver[] drivers = new WebDriver[4];
        for(int i = 0; i < 4; i++) {
            drivers[i] = wdl.getChromeDriver();
            drivers[i].get("http://localhost:" + port);
            myWait(1.0);
            drivers[i].findElement(By.id("name")).sendKeys(names[i]);
            myWait(0.5);
            drivers[i].findElement(By.id("join")).click();
            if (i == 2) {
                myWait(3);
                //added second player, click yes on player1 before cont
                drivers[0].findElement(By.id("Yes")).click();
            }
            //it'll change pages
        }
        //wait for last guy to change pages
        myWait(3);
        return drivers;
    }
    void myWait(double seconds) {
        try {
            Thread.sleep((long) (1000 * seconds));
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
