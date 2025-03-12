import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.support.ui.Select;

public class BookingcomTest {
    WebDriver driver = new ChromeDriver();
    By closePopup = By.xpath("//button[@aria-label='Dismiss sign-in info.']");
    Boolean checkPopUp = (Boolean) false;

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--ignore-ssl-errors'");
        options.addArguments("--ignore-certificate-errors");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }

    @Test
    public void openWebsite() {
        driver.get("https://www.booking.com");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        System.out.println("Bu da bir başka deneme");
        try {
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(closePopup));
            driver.findElement(closePopup);
            popup.click();
            System.out.println("Pop up kapatıldı.");
            checkPopUp = (Boolean) true;
            //System.out.println(checkPopUp);
        } catch (Exception e) {
            System.out.println("Pop up çıkmadı. Sonraki sayfada tekrar denenecek...");
        }

        WebElement destination = driver.findElement(By.xpath("//input[@placeholder='Where are you going?']"));
        destination.sendKeys("Paris");
        WebElement firstOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(("autocomplete-result-0"))));
        firstOption.click();

        WebElement startDate = driver.findElement(By.xpath("//td[@role='gridcell']//span[@data-date='2024-12-03']"));
        WebElement endDate = driver.findElement(By.xpath("//td[@role='gridcell']//span[@data-date='2024-12-06']"));
        startDate.click();
        endDate.click();

        WebElement search = driver.findElement(By.xpath("//button[@type='submit']"));
        search.click();
    }

    @Test(dependsOnMethods = {"openWebsite"})
    public void filterSelection() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        if(!checkPopUp) {
            try {
                WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(closePopup));
                driver.findElement(closePopup);
                popup.click();
                System.out.println("Pop up kapatıldı.");
            } catch (Exception e) {
                System.out.println("Pop up çıkmadı. Test devam ediyor...");
            }
        }
        else {
            System.out.println("Pop-up başta kapatıldı.");
        }

        WebElement sort = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(("//button[@type='button' and @data-testid='sorters-dropdown-trigger']"))));
        sort.click();

        WebElement sortSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(("//button[@type='button' and @aria-label='Price (lowest first)']"))));
        sortSelect.click();
        System.out.println("Sıralama yapıldı.");

        WebElement propertyTypeClick = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(("//div[@data-testid='filters-group-label-content' and text()= 'Hotels']"))));
        propertyTypeClick.click();

        WebElement meals = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(("//div[@data-testid='filters-group-label-content' and text()= 'Kitchen facilities']"))));
        meals.click();

        WebElement reviewScore = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(("//div[@data-testid='filters-group-label-content' and text()= 'Very Good: 8+']"))));
        reviewScore.click();

        WebElement distanceToCentral = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(("//div[@data-testid='filters-group-label-content' and text()= 'Less than 3 km']"))));
        distanceToCentral.click();
        System.out.println("İstenen özellikler seçildi.");

        int click = 2;
        int n = 0;

        WebElement desiredRoomNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(("//button[@type='button' and @aria-hidden='true'][2]"))));

        while (n < click) {
            desiredRoomNumber.click();
            n++;
        }

        WebElement firstHotel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(("//div[@data-testid='title'][1]"))));
        firstHotel.click();

        // Mevcut pencere (sekme) handle'larını al
        Set<String> allWindows = driver.getWindowHandles();

        Iterator<String> iterator = allWindows.iterator();
        String parentWindow = iterator.next(); // ilk sekme (veya pencere)
        String nextWindow = iterator.next();  // ikinci sekme (veya pencere)

        driver.switchTo().window(nextWindow);
    }

    @Test(dependsOnMethods = {"filterSelection"})
    public void reservation() {

        WebElement roomNumber = driver.findElement(By.xpath("//select[@data-testid='select-room-trigger']"));
        Select select = new Select(roomNumber);
        select.selectByValue("1");

        WebElement submit = driver.findElement(By.xpath("//button[@type='submit' and @data-tooltip-class='submit_holder_button_tooltip']"));
        submit.click();
    }

    @Test(dependsOnMethods = {"reservation"})
    public void detailsPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        try {
            WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(("//input[@data-testid='user-details-firstname']"))));
            name.click();
            name.sendKeys("Muhammed");

        } catch (StaleElementReferenceException e) {
            WebElement name = driver.findElement(By.xpath("//input[@data-testid='user-details-firstname']"));
            name.click();
            name.sendKeys("Muhammed");
        }

        try {
            WebElement surname = driver.findElement(By.xpath("//input[@data-testid='user-details-lastname']"));
            surname.click();
            surname.sendKeys("Ali");

        } catch (StaleElementReferenceException e) {
            WebElement surname = driver.findElement(By.xpath("//input[@data-testid='user-details-lastname']"));
            surname.click();
            surname.sendKeys("Ali");
        }

        try {
            WebElement email = driver.findElement(By.xpath(("//input[@data-testid='user-details-email']")));
            email.click();
            email.sendKeys("satera9239@lineacr.com");

        } catch (StaleElementReferenceException e) {
            WebElement email = driver.findElement(By.xpath(("//input[@data-testid='user-details-email']")));
            email.click();
            email.sendKeys("satera9239@lineacr.com");
        }

        WebElement country = driver.findElement(By.xpath("//select[@data-testid='user-details-cc1']"));
        Select select = new Select(country);
        select.selectByValue("us");

        WebElement phoneNumber = driver.findElement(By.xpath("//input[@data-testid='phone-number-input']"));
        phoneNumber.click();
        phoneNumber.sendKeys("51615615612");

        WebElement submit = driver.findElement(By.xpath("//button[@type='submit' and @data-popover-content-id='bp-submit-popover']"));
        submit.click();

        if (driver != null) {
            driver.quit();
        }
    }

/*
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }*/
}
