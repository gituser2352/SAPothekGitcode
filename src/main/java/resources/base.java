package resources;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;


public class base {

	//Declaration & initialization of variables
public static WebDriver driver;
public static Properties prop;
String workingDir = System.getProperty("user.dir");
public static Logger log=LogManager.getLogger(base.class.getName());


public WebDriver initializeDriver() throws IOException{

	prop=new Properties();
//FileInputStream fis=new FileInputStream("C:\\Users\\rameda\\E2EProject\\src\\main\\java\\resources\\data.properties");
	//FileInputStream fis=new FileInputStream("C:\\Users\\warri\\.jenkins\\E2EProject\\src\\main\\java\\resources\\data.properties");
	String workingDir = System.getProperty("user.dir");
    System.out.println("My current directory is " +workingDir);
    
    String PropertiesfilePath=workingDir+"\\src\\main\\java\\resources\\data.properties";
    System.out.println("the path of the image file is "+PropertiesfilePath);
    FileInputStream fis=new FileInputStream(PropertiesfilePath);
	prop.load(fis);
String browserName=prop.getProperty("browser");
System.out.println(browserName);
String browserdriverpath="Samplechrompath";
if(browserName.equalsIgnoreCase("chrome"))
{
	browserdriverpath=workingDir+"\\Chromedrivers\\2.46\\chromedriver.exe";
	System.out.println("Chromedriver path is "+browserdriverpath);
	//execute in chrome browser
	//System.setProperty("webdriver.chrome.driver", "E:\\MobileAutomation\\Atmn\\Selenium\\Chromedrivers\\2.46\\chromedriver.exe");
	System.setProperty("webdriver.chrome.driver", browserdriverpath);
	// Initialize browser
	driver=new ChromeDriver();
			
}
else if(browserName.equalsIgnoreCase("firefox"))
{
	//execute in firefox browser
	System.setProperty("webdriver.firefox.driver", "E:\\MobileAutomation\\Atmn\\Selenium\\Geckodrivers\\Firefox\\geckodriver.exe");
	
	// Initialize browser
	driver=new FirefoxDriver();
	
}
else if(browserName.equalsIgnoreCase("IE"))
{
	//execute in IE browser
}

driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
driver.manage().window().maximize();
return driver;

}

public void getScreenshot(String result) throws IOException{
	
	//date format for screenshot
	DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy h-m-s");
	//DateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy h-m-s");
	Date date = new Date();
	String Screenshotpath=workingDir+"\\Screenshots\\"+result+dateFormat.format(date)+".png";
	System.out.println("Screenshot path is "+Screenshotpath);
	
	//Capture screenshot
	File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	
	//FileUtils.copyFile(src, new File("D:\\Selenium\\Screenshots\\E2EprojectScreenshots"));
	FileHandler.copy(src, new File(Screenshotpath));
	
	//FileUtils.copyFile(src, new File("D:\\mobilescreenshots\\"+Anumber+"-"+Testtype+"-"+Captstate+"screenshot-"+dateFormat.format(date)+".png"));
	System.out.println("Captured Screenshot successfully");
	log.info("Captured Screenshot successfully for"+result);


}

//Click an element
public void click(String elementname) throws IOException
{
	String elementXpath=GetXpath(elementname);
	getScreenshot(elementname);
	driver.findElement(By.xpath(elementXpath)).click();
	log.info("Clicked on the element successfully");
}

//Get text from an element
public String getelementtext(String elementname) throws IOException {
	
	String elementXpath=GetXpath(elementname);
	
	String elementtext="returntext";
	try {
		elementtext=driver.findElement(By.xpath(elementXpath)).getText();
		getScreenshot("gettext"+elementname);
		log.info("Successfully retreived the text from the element ",elementname);
		System.out.println("Successfully retreived the text from the element "+elementname);
		
	} catch (Exception e) {

		log.error("Failed to get the text from the element "+elementname+e);
		
	}
	
	return elementtext;
}

//Set text in a box/field
public String SetText(String elementname, String texttoenter) throws IOException {
	
	String elementXpath=GetXpath(elementname);
	
	String elementtext="returntext";
	
	try {
		driver.findElement(By.xpath(elementXpath)).sendKeys(texttoenter); 
		log.info("Successfully entered the text for the element: ",elementname);
		System.out.println("Successfully entered the text for the element "+elementname);
		getScreenshot(elementname);
		
	} catch (Exception e) {

		log.error("Failed to enter the text for the element "+elementname+e);
		
	}
	
	return elementtext;
}

//Select a value from a dropdown

public void SelectValuebyVisibleText(String selectdropdown, String valuetoselect) throws IOException {
	String dropdownxpath=GetXpath(selectdropdown);

	try {
		Select dropdown = new Select(driver.findElement(By.xpath(dropdownxpath)));
		dropdown.selectByVisibleText(valuetoselect);
		System.out.println("Selected value "+valuetoselect);
		log.info("Successfully selected value from the dropdown: ",valuetoselect);
		getScreenshot(valuetoselect);
		
	} catch (Exception e) {

		log.error("Failed to select value from the dropdown for the element "+valuetoselect+e);
		
	}
}

	
//Get Xpath value
public String GetXpath(String elementname) throws IOException
{
	String elementnameinUImap= null;//element name in uiMap
	String elementXpath = null; //Xpath

	//code to get excel input data
	//String Excelinppath ="E://MobileAutomation//Atmn//Exceldata//MobileMSISDNTests.xlsx";
	String Excelinppath =workingDir+"\\Excelsheets\\uiMap.xlsx";
	System.out.println("uiMap sheet path is "+Excelinppath);
	String testsheetname = "ElementLocators";	
	//String testcaseName = "TestCase";
	ArrayList<String> data = new ArrayList<String>();
	int rowsize =0;
	//exceldata input
	Exceldata d=new Exceldata();
	//code to get number of rows with data(sheet name)
	int datarows = d.getrowscount(testsheetname, Excelinppath);
	if(datarows!=0)
	{
	data = d.getData(elementname, Excelinppath, testsheetname);
	//get no. of columns in the row
	rowsize = data.size();
	System.out.println("rowsize = "+rowsize);
	//code to fetch single row
	if (rowsize!= 0)
	{
		elementnameinUImap= data.get(0);//element name
		elementXpath = data.get(1); // Xpath

		System.out.println("Element name in uiMap is : "+ elementnameinUImap);//element name
		System.out.println("Xpath of element "+elementname+ " is : "+elementXpath);//Xpath
	}
	
	}
	
	return elementXpath;
	
}

public static String[] GetdbRecords(String datbasename, String querytoexecute) throws SQLException {
	// TODO Auto-generated method stub

	String host="localhost"; String port="3306";
	//String datbasename="demo";
	//String querytoexecute="select * from credentials where scenario='zerobalancecard'";
	//String username;
	String[] elementslist = {"abc","abc","abc","abc","abc"}; int i=0;
	
	//List<String> elementslist = new ArrayList<String>();
	
	//DriverManager.getConnection("url", "username", "password");
	//url = "jdbc:mysql//"+host+":"+port+"/databasename"
	
	Connection con=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+datbasename, "root", "root");
	//Connection con=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/demo", "root", "root");
	Statement s=con.createStatement();
	
	ResultSet rs=s.executeQuery(querytoexecute);
	while(rs.next())
	{
		elementslist[0]=rs.getString("element1");
		elementslist[1]=rs.getString("element2");
		elementslist[2]=rs.getString("element3");
		elementslist[3]=rs.getString("element4");
		elementslist[4]=rs.getString("element5");
		
		for(i=0;i<5;i++)
		{
		System.out.println("value in element"+i+"is "+elementslist[i]);	
		}
		
	}
	
	
	return elementslist;	
}

//Attach file

public void AttachFile(String path) throws AWTException{
	   // setClipboardData(String path);
	
	   StringSelection stringSelection = new StringSelection(path);
	   System.out.println("actul path is "+stringSelection);
	   Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	   Robot robot = new Robot();
	   robot.keyPress(KeyEvent.VK_CONTROL);
	   robot.keyPress(KeyEvent.VK_V);
	   robot.keyRelease(KeyEvent.VK_V);
	   robot.keyRelease(KeyEvent.VK_CONTROL);
	   robot.keyPress(KeyEvent.VK_ENTER);
	   robot.keyRelease(KeyEvent.VK_ENTER);
	
	   
}

}
