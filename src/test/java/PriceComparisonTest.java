import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;

public class PriceComparisonTest {
    private WebDriver driver;
    private double price1, price2, price3;

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }

    @Test
    public void testGetPriceFromSite1() {
        try {
            driver.get("https://www.amazon.com.tr/Samsung-Galaxy-Telefonu-T%C3%BCrkiye-Garantili/dp/B0CR6RCV1Q/?_encod" +
                    "ing=UTF8&pd_rd_w=iuB3u&content-id=amzn1.sym.b7346c4c-1e1f-4543-96c9-dedbec11bf63%3Aamzn1.symc.cd" +
                    "b151ed-d8fe-485d-b383-800c8b0e3fd3&pf_rd_p=b7346c4c-1e1f-4543-96c9-dedbec11bf63&pf_rd_r=YHXF3T0204" +
                    "3W75QBV9NQ&pd_rd_wg=CM8ev&pd_rd_r=d1bcfbb" +
                    "a-d83a-45af-a5de-4efcb69a0f8c&ref_=pd_hp_d_atf_ci_mcx_mr_hp_atf_m&th=1");
            WebElement priceElement = driver.findElement(By.className("a-price-whole"));
            String priceText = priceElement.getText();
            price1 = parsePrice(priceText);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to get price from Site 1");
        }
    }

    @Test
    public void testGetPriceFromSite2() {
        try {
            driver.get("https://shop.samsung.com/tr/galaxy-s24/");
            WebElement priceElement = driver.findElement(By.className("sticky-bottom-bar--price"));
            String priceText = priceElement.getText();
            price2 = parsePrice(priceText);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to get price from Site 2");
        }
    }

    @Test
    public void testGetPriceFromSite3() {
        try {
            driver.get("https://www.teknosa.com/samsung-galaxy-s24-8gb256gb-siyah-akilli-telefon-p-125079446");
            WebElement priceElement = driver.findElement(By.className("prd-prc2"));
            String priceText = priceElement.getText();
            price3 = parsePrice(priceText);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to get price from Site 3");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        generateReport();
    }

    private double parsePrice(String priceText) {
        priceText = priceText.replaceAll("[^\\d.]", "");
        return Double.parseDouble(priceText);
    }

    private void generateReport() {
        double minPrice = Math.min(price1, Math.min(price2, price3));
        double maxPrice = Math.max(price1, Math.max(price2, price3));
        double avgPrice = (price1 + price2 + price3) / 3;

        System.out.println("Price Comparison Report");
        System.out.println("-------------------------");
        System.out.println("Amazon TR: TL" + price1);
        System.out.println("Samsung TR: TL" + price2);
        System.out.println("Teknosa: TL" + price3);
        System.out.println("-------------------------");
        System.out.println("Cheapest Price: TL" + minPrice);
        System.out.println("Most Expensive Price: TL" + maxPrice);
        System.out.println("Average Price: TL" + avgPrice);
    }
}