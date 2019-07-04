package Components;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import resources.base;

public class ShopApothekeUIComponents extends base{
	public WebDriver driver;
	public static Logger log=LogManager.getLogger(base.class.getName());	
	public ShopApothekeUIComponents(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver=driver;
	}
	String workingDir = System.getProperty("user.dir");
	String ItemtoAdd=prop.getProperty("ItemtoAdd");
	base b = new base();
	String elementXpath="Getxpath";
	
	
	///----old app variable
	String uploadfilename=prop.getProperty("uploadfilename");
	
	public void Login() throws IOException, InterruptedException  {
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		String email=prop.getProperty("username");
		System.out.println("Username is "+ email);
		String Password=prop.getProperty("Password");
		System.out.println("Password is "+ Password);
		
		//Capturing screenshot of Home page
		getScreenshot("Homepage");
		//Accept cookie
		//Thread.sleep(3000);
		b.click("btnAcceptcookie");
		
		//click on later if discounts related popup comes up
		//WebDriverWait wait=new WebDriverWait(driver, 45);

		try {
			elementXpath=GetXpath("rejectdiscountpopup");
			System.out.println("Popup element Xpath is "+elementXpath);
			//	Thread.sleep(25000);
			getScreenshot("rejectdiscountpopup");
			b.click("rejectdiscountpopup");
			log.info("Rejected the discount popup Successfully");
			System.out.println("Rejected the discount popup Successfully");
			getScreenshot("afterdiscountpopuprejected");
			
		} catch (Exception e) {

			log.error("No such reject discount popup "+e);
			System.out.println("No such reject discount popup "+e);
			getScreenshot("Nosuchrejectdiscountpopup");
		}
		
		//perform login
		click("btnLogin");
		b.SetText("eltusername", email);
		b.SetText("eltPassword", Password);
		b.click("btnInnerLogin");
		Thread.sleep(2000);
		
	}
	
	
	
	
	
	public void VerifyItemAdditiontoCart() throws IOException, InterruptedException, AWTException {
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		getScreenshot("HomepageafterLogin");
		
		//Enter product name to be added to cart in search box
		String ItemtoAdd=prop.getProperty("ItemtoAdd");
		System.out.println("Item to be added to cart is "+ ItemtoAdd);
		
		b.SetText("eltsearchproductbox", ItemtoAdd);
		//Select the 1st result
		b.click("eltsearchresult");
	
		//close the newsletter popup at the bottom
		try {

			//	Thread.sleep(25000);
			//getScreenshot("closenewsletterpopup");
			b.click("eltclosenewsletterpopup");
			log.info("Closed the newsletter popup Successfully");
			System.out.println("Closed the newsletter popup Successfully");
			getScreenshot("afternewsletterpopupclose");
			
		} catch (Exception e) {

			log.error("No such newsletter popup to close "+e);
			System.out.println("No such newsletter popup to close "+e);
			getScreenshot("Nosuchnewsletterpopup");
		}
		
		//Click on Add to cart button
		Thread.sleep(2000);
		b.click("btnAddtoCart");
		System.out.println("Item "+ ItemtoAdd+" is added to cart Suceesfully ");
		Thread.sleep(5000);
		getScreenshot("afteraddingitemtocart");
		
	}

	public void VerifyAdditionSuccessful() throws IOException, InterruptedException {
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		//Click on cart button
		b.click("btnCart");
	
		//Get the Actual text of the 1st product added
		String ActualProductaddedtocart=b.getelementtext("eltActualProductaddedtocart");
		log.info("Actual item added to the cart is "+ActualProductaddedtocart);
		System.out.println("Actual item added to the cart is "+ActualProductaddedtocart);
		
		//Compare the Actual text to Expected & validate if matching
		
		try {
			Assert.assertEquals(ActualProductaddedtocart, ItemtoAdd);
			log.info("Added correct item to the cart Successfully : "+ActualProductaddedtocart);
			System.out.println("Added correct item to the cart Successfully "+ActualProductaddedtocart);
			getScreenshot("CorrectlyAddeditem");
		} catch (Exception e) {

			log.error("Wrong item added to the cart "+e);
			System.out.println("Wrong item added to the cart "+e);
			getScreenshot("Wronglyaddeditem");
		}
		
		Thread.sleep(5000);
		
}


}
