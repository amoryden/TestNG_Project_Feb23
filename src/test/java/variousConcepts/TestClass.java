package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TestClass {
	String browser;
	String url;
	WebDriver driver;

	String USER_NAME;
	String PASSWORD;

	@BeforeSuite
	public void readConfig() {

		try {

			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("browser used: " + browser);
			url = prop.getProperty("url");
			USER_NAME = prop.getProperty("userName");
			PASSWORD = prop.getProperty("password");

		}

		catch (IOException e) {

			e.printStackTrace();
		}

	}

	By userNameField = By.xpath("//input[@id='username']");
	By passwordField = By.xpath("//input[@id='password']");
	By signinBottonField = By.xpath("/html/body/div/div/div/form/div[3]/button");
	By dashboardHeaderField = By.xpath("//*[@id=\"page-wrapper\"]/div[2]/div/h2");
	By customerMenuField = By.xpath("//*[@id=\"side-menu\"]/li[3]/a/span[1]");
	By addcustomerMenuField = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li[1]/a");
	By addcustomerHeaderField = By.xpath("//*[@id=\"page-wrapper\"]/div[3]/div[1]/div/div/div/div[1]/h5");
	By FullNameField = By.xpath("//*[@id=\"account\"]");
	By companyDropDownField = By.xpath(("//select[@id='cid']"));
	By emailField = By.xpath("//input[@id='email']");
	By phoneField = By.xpath("//input[@id='phone']");
	By countryField = By.xpath("//select[@id='country']");

	String Dashboard_Header_Text = "Dashboard";
	String AddCustomer_Header_Text = "Add Contact";
	String Full_Name = "Selenium Feb2023";
	String COMPANY = "Techfios";
	String EMAIL = "demoFeb23@techfios.com";
	String PHONE = "1234567";
	String COUNTRY = "Antarctica";

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();

		} else if (browser.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
			driver = new EdgeDriver();

		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority = 1)
	public void login() {

		driver.findElement(userNameField).sendKeys(USER_NAME);
		driver.findElement(passwordField).sendKeys(PASSWORD);
		driver.findElement(signinBottonField).click();
		Assert.assertEquals(driver.findElement(dashboardHeaderField).getText(), Dashboard_Header_Text,
				"Dashboard page not found");

	}

	@Test(priority = 2)
	public void addCustomer() throws InterruptedException {

		login();
		Thread.sleep(2000);
		driver.findElement(customerMenuField).click();
		driver.findElement(addcustomerMenuField).click();
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(addcustomerHeaderField));
		waitForElement(driver, 5, addcustomerHeaderField);
		Assert.assertEquals(driver.findElement(addcustomerHeaderField).getText(), AddCustomer_Header_Text,
				"Add Customer page not available");
		randomNumGenerator(999);
		driver.findElement(FullNameField).sendKeys(Full_Name + randomNumGenerator(999));

		selectFromDropdown(driver.findElement(companyDropDownField), COMPANY);

		driver.findElement(emailField).sendKeys(randomNumGenerator(9999) + EMAIL);
		driver.findElement(phoneField).sendKeys(PHONE + randomNumGenerator(99));

		selectFromDropdown(driver.findElement(countryField), COUNTRY);

	}

	private void waitForElement(WebDriver driver, int waitTime, By elementToBeLocated) {

		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.visibilityOfElementLocated(elementToBeLocated));

	}

	private void selectFromDropdown(WebElement element, String visibleText) {

		Select sel1 = new Select(element);
		sel1.selectByVisibleText(visibleText);

	}

	private int randomNumGenerator(int bound) {

		Random rnd = new Random();
		int generatedNum = rnd.nextInt(bound);
		return generatedNum;

	}

//	@AfterMethod
	public void tearDown() {

		driver.close();
		driver.quit();

	}

}
