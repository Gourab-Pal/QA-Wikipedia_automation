
package apTests;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;
//Selenium Imports
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
///

public class TestCases {
    WebDriver driver;

    public TestCases() {
        System.out.println("Constructor: TestCases");

        WebDriverManager.chromedriver().timeout(30).setup();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        // Set log level and type
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);

        // Set path for log file
        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "chromedriver.log");

        driver = new ChromeDriver(options);

        // Set browser to maximize and wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }

    public void endTest() {
        System.out.println("End Test: TestCases");
        driver.close();
        driver.quit();
    }

    public void navigateToHomePage() {
        driver.get("https://www.wikipedia.org/");
    }

    public void showLogs(String log) {
        System.out.println(log);
    }

    public void showResult(String log, Boolean status) {
        if (status) {
            System.out.println(log + " --> PASS");
        } else {
            System.out.println(log + " --> FAIL");
        }
    }

    public Boolean checkHeader(String headerText) {
        String txt = driver.findElement(By.xpath("//h1/span")).getText();
        return txt.equals(headerText);
    }

    public Boolean checkFooterLink(String linkText) {
        String txt = driver.findElement(By.linkText(linkText)).getText();
        return txt.equals(linkText);
    }

    public void searchWithKeyword(String keyword) {
        driver.findElement(By.id("searchInput")).sendKeys(keyword);
    }

    public Boolean checkMatch(List<WebElement> we, String str) {
        for (WebElement elem : we) {
            if (elem.getText().trim().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void clickOnTarget(List<WebElement> we, String searchText) {
        for (WebElement elem : we) {
            if (elem.getText().equals(searchText)) {
                elem.click();
                break;
            }
        }
    }

    public void testCase01() {
        this.showLogs("Start Test case: testCase01");
        this.navigateToHomePage();
        Boolean status = driver.getCurrentUrl().contains("wikipedia");
        this.showResult("Valid homepage url", status);
        this.showLogs("End Test case: testCase01");
    }

    public void testCase02() {
        this.showLogs("Start Test case: testCase02");
        this.navigateToHomePage();
        this.showResult("The header contains Wikipedia", this.checkHeader("Wikipedia"));
        this.showResult("The footer contains Terms of Use", this.checkFooterLink("Terms of Use"));
        this.showResult("The footer contains Privacy Policy", this.checkFooterLink("Privacy Policy"));
        this.showLogs("End Test case: testCase02");
    }

    public void testCase03() throws InterruptedException {
        this.showLogs("Start Test case: testCase03");
        this.navigateToHomePage();
        Thread.sleep(1000);
        this.searchWithKeyword("apple");
        Thread.sleep(2000);
        List<WebElement> suggestions = driver.findElements(By.xpath("//h3[@class='suggestion-title']"));
        this.clickOnTarget(suggestions, "Apple Inc.");
        Thread.sleep(3000);
        String headerToMatch = "Steve Jobs";
        List<WebElement> founderList = driver
                .findElements(By.xpath("//table[@class='infobox ib-company vcard']/tbody/tr[9]/td/div/ul/li"));
        this.showResult("Steve Jobs is present in Founders", this.checkMatch(founderList, headerToMatch));
        this.showLogs("End Test case: testCase03");
    }

    public void testCase04() throws InterruptedException {
        this.showLogs("Start Test case: testCase04");
        this.navigateToHomePage();
        Thread.sleep(1000);
        this.searchWithKeyword("microsoft");
        Thread.sleep(2000);
        List<WebElement> suggestions = driver.findElements(By.xpath("//h3[@class='suggestion-title']"));
        this.clickOnTarget(suggestions, "Microsoft");
        Thread.sleep(3000);
        String headerToMatch = "Bill Gates";
        List<WebElement> founderList = driver
                .findElements(By.xpath("//table[@class='infobox ib-company vcard']/tbody/tr[8]/td/div/ul/li"));
        Boolean checkStatus = this.checkMatch(founderList, headerToMatch);
        if(checkStatus) {
            for(int i = 1; i<=founderList.size(); i++) {
                if(driver.findElement(By.xpath("//table[@class='infobox ib-company vcard']/tbody/tr[8]/td/div/ul/li[" + i + "]")).getText().equals(headerToMatch)) {
                    driver.findElement(By.xpath("//table[@class='infobox ib-company vcard']/tbody/tr[8]/td/div/ul/li[" + i + "]/a")).click();
                    Thread.sleep(3000);
                    String currentUrl = driver.getCurrentUrl();
                    this.showResult("The url contains Bill_Gates", currentUrl.contains("Bill_Gates"));
                    //System.out.println(currentUrl);
                    break;
                }
            }
        }
        else {
            this.showResult("Bill Gates found in founder", false);
        }
        this.showLogs("End Test case: testCase04");
    }

    public void testCase05() throws InterruptedException {
        this.showLogs("Start Test case: testCase05");
        driver.get("https://en.wikipedia.org/");
        Thread.sleep(1000);
        driver.findElement(By.id("vector-main-menu-dropdown-checkbox")).click();
        Thread.sleep(2000);
        driver.findElement(By.linkText("About Wikipedia")).click();
        Thread.sleep(3000);
        String url = driver.getCurrentUrl();
        this.showResult("The url contains About", url.contains("About"));
        this.showLogs("End Test case: testCase05");
    }

}
