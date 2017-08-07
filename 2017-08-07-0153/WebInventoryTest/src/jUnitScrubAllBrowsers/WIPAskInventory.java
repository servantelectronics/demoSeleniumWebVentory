// WEB INVENTORY - log create WEBELEMENT object / properties LIST in CSV or HTML
//  (a) Tester configures RUNTIME settings with file "config.properties" 
//  (b) QA admin sets some CONSTANTS *and* NAVIGATION WEBELEMENTS in  CLASS CONSTANTS
//  (c) THREE CLASSES (i) WIPAskInventory.java (ii) supplemental class FileWrite.java (iii) supplemental class ReadPropertyFile.java 
//  (x) this demo version works with TARGETURL www.FINVIZ.com STOCK MARKET SCREENER
// TODO DONE TRAP MISSING FOLDER and CREATE 
// TODO READ navigation WEBELEMENTs (by locator) from DIFFERENT FILE (xml?) by varName, Locator, URL, testCase
// TODO check Firefox version compatibility - marionnette deprecated errors
// TODO add wait or some capability to give IEXPLORE more time to login
// TODO convert to TESTNG   
// TODO run minimized? increase speed.
// TODO HANDLE ADVERTS with "anti testing" POP UP IFRAMES that resist "switchto" frame/windowhandles (try javascript)
// TODO SEPARATE HISTORY FILE LOG DURATION, TESTNAME, COUNT TO SEPARATE FILE // MOVE FILENAME TO LOG ROUTINE
// TODO FIX output table to handle / filter out EXCLUDETAGs nested inside desired tags <div><tr>
// TODO move ECLIPSE JVM, JRE, workspace into REPOSITORY that can use RELATIVE PATHS 
// eclipse workspace  C:\Users\Allan\workspace\SeleniumJunitTemplate20170501
// external jars C:\Users\Allan\AppData\Roaming\Selenium\331\lib 

package jUnitScrubAllBrowsers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import static java.util.Arrays.asList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
// import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.junit.AfterClass;
//import org.apache.commons.lang3.time.DurationFormatUtils;
//import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import jUnitScrubAllBrowsers.FileWrite.LogCode;
import org.junit.runners.MethodSorters;

  


/************  CLASS VARIABLES  *****************/

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WIPAskInventory{   // WebSmokeInventory
	

    public static int intLoadTime = 5; // seconds  http://www.seleniumhq.org/docs/04_webdriver_advanced.jsp
	public static boolean askUserQUIT = true;
	public static boolean INVENTORYFLAGTF;
	public static String logPathFilename;  // why not static
	public static String tempLogFile ;
	public static String TargetURL;  // why not static
	public String errMessages; // append errMessages until a file name / path is selected
	public static WebDriver driver;  // called at line 171 
	private static String whichBrowser ; //=  "Chrome" ;// "Chrome" ;  //"IExplore";  //  "Firefox";
	static int MAXCHARS = 99;
	public static long startTestTime; 
	public long startUserTime;
	public static long endTestTime;
	public long endUserTime;
	public static String expectTitle; 
	
	public static String xpathTarget; // = "//div/*";   //  "//a/*";   //  "//div/*";
	public static String EXCLUDETAGS; // = "=HEAD,HTML,BODY,STYLE,SCRIPT,LINK,FONT,DIV,SPAN,IFRAME,FORM,TABLE,TBODY"; // READ FROM CONFIG.PROPERTIES into a list
	public static String flagClearCookiesYN;
	public static String flagUserConfirmationYN;
	public static String flagWindowMinimizeYN;
	public static String driverIexplore;
	public static String driverFireFox;
	public static String driverChrome;

	
/************  HTML TEST OBJECTS *****************/	
		By topPageObject = By.className("logo");  // CSS logo http://finviz.com/img/logo_2.png from 
	    By topPageLoginButton = By.className("nav-link_sign-in"); // <td><a href="/login.ashx" class="nav-link sign-in">Login</a></td>  
	    By topPageLoginLink = By.xpath("/html/body/table[2]/tbody/tr/td/table/tbody/tr/td[15]/a");
	    By topPageUserID = By.xpath(".//*[@id='account-dropdown']/div/a/span[2]");
	    By loginPagePassword = By.name("password"); // By.name("email"))  
	    By loginPageUsername = By.name("email"); 
	    By loginPageH1identifier = By.xpath("html/body/div[2]/div/div/div/h1");  //  <h1>Log in to Finviz</h1><p>
	    By loginPassword = By.xpath("html/body/div[2]/div/div/form/label[2]/input"); // text input 
	 
	    String topPageTitle = "Passwordzzz";
	    String  strLoginHeader = "Log in to Finviz"; 
	    
	    String strPPhrase = "Fandang0888";
	    By loginPageSubmitButton = By.xpath("/html/body/div[2]/div/div/form/input");
	    
	    By landingPageUserNameSpan = By.xpath("//*[@id='account-dropdown']/div/a/span[2]");
	    By screenerDropDown = By.xpath("//*[@id='screenerPresetsSelect']");  // use ' instead of \" ?
	    By screenerDropDownItem1 = By.xpath("//select[@id='screenerPresetsSelect']//option[.='s: upupup']");
	    By screenerDropDownItem2= By.xpath("//select[@id='screenerPresetsSelect']//option[.='s: upupup']");
	    By landingPageTABScreener = By.xpath("html/body/table[2]/tbody/tr/td/table/tbody/tr/td[3]/a");
	    By landingPageReportTabPerformance = By.xpath("//*[@id='screener-content']/table/tbody/tr[1]/td/table/tbody/tr/td[5]/a");  //<a href="screener.ashx?v=141">Performance</a>
	    By loginPageDataGrid = By.id("delete-paths-modal-success-message");	
	    By landingPage_DataGrid_RowCount = By.className("count-text"); // count-text By.xpath(".//*[@id='screener-content']/table/tbody/tr[3]/td/table/tbody/tr/td[1]/b");



/************  FRAMEWORK TESTS JUNIT and/or TESTNG *****************/
	    // @Before	 public void identifyTestObjects() {	//Path Headings

	@Test
	public void Test0000_setup() { //String[] args) throws InterruptedException {
		

		String errMessages = "";
		ReadPropertyFile prop=null;
		try { prop = new ReadPropertyFile(); } catch (Exception e1) {	
			errMessages=errMessages + "\n" + "TEST HALTED: CANNOT READ PROPERTIES FILE 'config.properties' "
						+ "in line 83 of 'xMain'";
		System.err.println(errMessages);
		e1.printStackTrace(); } 
		
	// read config.properties to variables ... next set/initialize new logfile and then history file 
	// driver = chooseDriverFromBrowserName(prop.askBrowserName()); // see line 122 trying to avoid "stale" error 
		expectTitle = prop.expectTitle(); // ="IPG - Intertape Polymer Group";
		TargetURL = prop.readTargetUrl();
		xpathTarget = prop.xpathTarget(); // = "//div/*";   //  "//a/*";   //  "//div/*";
		EXCLUDETAGS = prop.excludeTags().toUpperCase();
		whichBrowser = prop.askBrowserName();
		INVENTORYFLAGTF = (prop.reportInventory().toUpperCase().equals("TRUE"));  //only Strings (true | TRUE) will report: all else false
		flagClearCookiesYN = prop.readFlagClearCookiesYN();
		flagUserConfirmationYN = prop.readFlagUserConfirmationYN();
		flagWindowMinimizeYN = prop.readFlagWindowMinimizeYN();
		driverIexplore = prop.pathIexplore().replace("\\\\","/").replace("//","/").replace("/","//"); // not so silly, handles error editing config.properties
		driverFireFox = prop.pathFirefox().replace("\\\\","/").replace("//","/").replace("/","//");;
		driverChrome = prop.pathChrome().replace("\\\\","/").replace("//","/").replace("/","//");;
//TODO ADD VALIDATIO OF EACH CONFIG.PROPERTIES LINE (especially of comma/quote separate EXLUDETAGs)		

/******** SETUP OUTPUT FILE TO DESKTOP ********/
	// TODO find or replace logger instance/class/library???? writeWebElementToHTML(logger, driver);  
	// TODO report full file name  see Eclipse configuration var 		//  ${workspace_loc} 

		String dotExtension = ".csv";  // default CSV UNLESS inventory flag is TRUE
		if (INVENTORYFLAGTF) dotExtension = "."+getReportFileExtension(prop, driver).toLowerCase(); // 
		String BaseFileName = FileWrite.affixTimeStamp(getClass().getSimpleName()); // FileWrite.affixTimeStamp(null);
		
	   // 	String dotExtension = askReportCSVorHTML(prop, driver).toLower(); //  moved to below ReadPropertyFile		
		logPathFilename=FileWrite.createOutputFilepath(BaseFileName, dotExtension);
		tempLogFile = logPathFilename; // THIS CAN BE REMOVED or USED LATER for a separate HISTORY file
			
// TODO validate formatted URL in all legal variations of [ http.www | http | www | ].domain.ABC 		
		FileWrite.logthis(LogCode.WARN,errMessages);			
		 driver = chooseDriverFromBrowserName(whichBrowser);  //WebDriver
	    FileWrite.writeln(logPathFilename, whichBrowser + " Browser webDriver");   // (filePathName, text);logger.info(whichBrowser + " Browser webDriver");
	    FileWrite.logthis(LogCode.INFO,whichBrowser + " Browser webDriver");	
	    
driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);  // was commented out because already (?) handled by jUnit and TestNG ??		

  // Set implicit wait      System.out.println("driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);"); // handled by jUnit and TestNG already??    
   System.out.println("log file full name is: " + logPathFilename );

/******** ASK USER TO CONFIRM SETTINGS or CANCEL ********/
   // ASK USER CONFIRMATION PROP.
   		String inventoryOverRide = "";
        if (!INVENTORYFLAGTF)  inventoryOverRide = "\nCSV report overrides config.properties\n" ;
		if (prop.readFlagUserConfirmationYN().equalsIgnoreCase("Y")) {	
		System.out.println(FileWrite.readSystemProperties(true));
		   int qy = askUserYN("Confirm ''YES'' to RUN TEST with THESE SETTINGS!  ''NO'' to HALT test!",FileWrite.readSystemProperties(true)
				   + inventoryOverRide
				   +  "\r\nlog file: " + logPathFilename + "\r\n ====\r\n" +
				   prop.printlnPropsReadPropertyFile());
   		 if (qy==1 || qy==-1 ) {  		 // driver.close(); driver.quit();  driver.dismiss();
			     System.out.println("CANCELLED BY USER!");
			     EMERGENCY_STOP_TEST("CANCELLED BY USER: at Confirmation of Properties!"); }
		} // end if ask user confirmation

/******** SETUP FLAGS DETERMINE IF OUTPUT INVENTORY TO FILE ********/
	// START TEST: NAVIGATE THEN OUTPUT INVENTORY TO FILE 		
        System.out.println("Starting TEST of " + TargetURL + " at " + datetimestamp()+ 0);
         startTestTime = System.nanoTime();  
 	        
		if (INVENTORYFLAGTF) { if (dotExtension.toLowerCase().contains("csv")) { //dotExtension
					writeWebElementsToCSV(driver); }
			else if (dotExtension.toLowerCase().contains("htm"))
			{ writeWebElementsToHTML(driver);}
			else {EMERGENCY_STOP_TEST("TEST  HALT: CAN NOT DETERMINE FILE FORMAT ''CSV'' or ''HTM''");}
		} // end IF choose output file format
			
		if (prop.readFlagClearCookiesYN().equalsIgnoreCase("Y")) { 
				System.out.println("clearing cookies");
				driver.manage().deleteAllCookies();
				FileWrite.writeln(logPathFilename, "COOKIES CLEARED");
		} else {FileWrite.writeln(logPathFilename, "COOKIES NOT CLEARED");}
		
/************* OPEN TARGET URL *************/		
		FileWrite.logthis(LogCode.INFO, "Navigating to TargetURL: " + TargetURL);	
		driver.navigate().to(TargetURL);


		
	} // end of Test000_setup  


/******** BEGIN NUMBERED TEST CASES IN ORDER ********/
	
	@Test
	public void Test0001_openPage() {
		System.out.println("verify that page Open"); 

		// same as method logPageCheck()
			FileWrite.logthis(LogCode.INFO, "current URL: " + driver.getCurrentUrl()); 
			FileWrite.logthis(LogCode.INFO, "current Title: " + driver.getTitle() );					
					
/*		System.out.println("current URL: " + driver.getCurrentUrl()); 
		System.out.println("current Title: " + driver.getTitle() );	
*/		
		FileWrite.logthis(LogCode.INFO, "attribute HREF: '" + driver.findElement(topPageObject).getAttribute("href")+"'");
		// System.out.println("attribute HREF: " + driver.findElement(topPageObject).getAttribute("href"));
        boolean btemp = isDisplayedAndClickable(topPageObject, 10);
		verifyBooleanLOG("test LOGO is displayed and enabled: ", btemp);
			
	}

@Test
	public void Test0002_testTitle() throws Exception {
		// driver.getTitle();
		// 	assertEquals("Google", driver.getTitle());  	//	TestCase.
		System.out.println("begin @Test testTitle ");
		FileWrite.logthis(LogCode.INFO, "begin @TestTitle ");
		assertEquals(driver.getTitle(),expectTitle);
	}
	

	@Test
	public void Test0003_Redirection()  {
		// driver.getTitle();
		// 	assertEquals("Google", driver.getTitle());  	//	TestCase.
		System.out.println("begin @TestREDIRECTION ");
		FileWrite.logthis(LogCode.INFO, "begin @TestREDIRECTION");
		assertEquals(driver.getCurrentUrl(),TargetURL);
	}



	@Test
	public void Test001_Username_Login() {
		System.out.println("Test001_Username_Login verify username and login if not expected value");
				boolean notLoggedIn = isDisplayedAndClickable(topPageLoginLink, intLoadTime);
		verifyBooleanLOG("test if LOGIN TAB link is displayed and enabled: ", notLoggedIn);
		if (notLoggedIn) 
			{FileWrite.logthis(LogCode.INFO, "topPageLoginLink attribute innerHTML: '" + driver.findElement(topPageLoginLink).getAttribute("innerHTML") + "'");
	
	
	verifyBooleanLOG("test if topPageLoginLink is found: ", isDisplayed(topPageLoginLink, intLoadTime));

//  closeAnyIframes();
	sleep(5000);
			driver.findElement(topPageLoginLink).click();
			//parentWindowHandle = driver.getWindowHandle();

			if (isDisplayed(loginPageH1identifier, intLoadTime))  { 	// confirm page "https://finviz.com/login.ashx" loads by title, url, form textboxes, send button
						// login and submit 
					      driver.findElement(By.name("email")).sendKeys("servantelectronics@yahoo.com");
					        // driver.findElement(By.name("password")).click(); loginPageH1identifier  loginPagePassword loginPageUsername 
					      driver.findElement(By.name("password")).sendKeys(strPPhrase);
					      driver.findElement(loginPageSubmitButton).click();
					} else { 	FileWrite.logthis(LogCode.FATAL, "Login Page Caption '" + strLoginHeader + "' Not found ");
								assertTrue(false);
							} 
			}
	}  // end Test001_Username_Login
	

	
	
    @Test
    public void Test002_LoadScreener(){
    	System.out.println("Test002_LoadScreener ");
 // closeAnyIframes();  
	
    		if (isDisplayed(landingPageTABScreener, intLoadTime))  { 	// confirm page "https://finviz.com/login.ashx" loads by title, url, form textboxes, send button
			 driver.findElement(landingPageTABScreener).click();
			 				// 	parentWindowHandle = driver.getWindowHandle();			 
					 if (isDisplayedAndClickable(screenerDropDown,intLoadTime)) {
						 // find select screenerPresetsSelect  
					 // count options within screenerPresetsSelect
					 // click any/random/top/bottom? String yourSelection = screenDropDown.getFirstSelectedOption().getText();
						 
					} else { 	FileWrite.logthis(LogCode.FATAL, "NOT FOUND: Screener Tab link object ");
						assertTrue(false);
					}  // end inner if / else
		} // end outer IF	
    		
    		  
    		ifAlertPresentAcceptYN('N');  // login page remember password

    		
    }  // end Test002_LoadScreener
	
	
	
	
	
	
	@Test
	public void Test003_SelectionList(){
		System.out.println("Test003_SelectionList List should exist, have values, verify count");
		
		ifAlertPresentAcceptYN('N');  // login page remember password
		
// closeAnyIframesLIST();	
	 WebElement screenDropDown = driver.findElement(screenerDropDown);          //( By.id("screenerPresetsSelect"));
	 
        Select optList = new Select(screenDropDown);
        List<WebElement> screenlist = optList.getOptions();
        System.out.println("The options for this DropDown are : ");
        for (WebElement scrItem:screenlist)
        {
            System.out.println(scrItem.getAttribute("innerHTML"));
        }
        
        if (isDisplayedAndClickable(landingPageTABScreener, intLoadTime)) {
        	driver.findElement(landingPageTABScreener).click();
        } else 
        { driver.switchTo().defaultContent();
          driver.findElement(landingPageTABScreener).click();
        }
//         driver.findElement(By.linkText("Screener")).click();
        
        new Actions(driver).moveToElement(driver.findElement(By.linkText("Screener"))).build().perform();
        new Actions(driver).moveToElement(driver.findElement(screenerDropDown)).build().perform();
//        new Actions(driver).moveToElement(driver.findElement(By.xpath("//select[@id='screenerPresetsSelect']//option[.='s: sma20_crossedtoday']"))).build().perform();
   new Actions(driver).moveToElement(driver.findElement(By.xpath("//select[@id='screenerPresetsSelect']//option[.='s: upupup']"))).build().perform();
        if (!driver.findElement(screenerDropDown).isSelected()) {
        	driver.findElement(By.xpath("//select[@id='screenerPresetsSelect']//option[7]")).click();
        	FileWrite.logthis(LogCode.INFO, "Screener selection:  '" + 
        			driver.findElement(By.xpath("//select[@id='screenerPresetsSelect']//option[7]")).getAttribute("innerHTML") + "'");
        	
        	}  // end if
		
	}  // end Test003_SelectionList
	
	
	@Test
	public void Test004_ReportTab(){
		System.out.println("Test003_ReportTab tabCaptionPerformance = STUB (to be added) \"Performance\"");
	} // Test004_ReportTab


	@Test 
	public void Test005_DataGrid(){
// closeAnyIframes();	
		System.out.println("Test005_DataGrid");
		if (verifyBoolean("Does Data Grid total show: ", isDisplayed(landingPage_DataGrid_RowCount, intLoadTime))) {
			String strResult = driver.findElement(landingPage_DataGrid_RowCount).getAttribute("innerHTML"); 
			FileWrite.logthis(LogCode.INFO, "Does Data Grid summary show: " +strResult);  // TODO use REGEX to verify or assert
		}
		

	}  // Test005_DataGrid
	

	@Test
	public void  Test_099_printlnConfigProperties() throws IOException { //  main(String[] args) throws IOException {
			
			Properties prop = new Properties();
			InputStream input = ReadPropertyFile.class.getClassLoader().getResourceAsStream("config.properties");
			// /jUnitTemplate0425a/src/config.properties
			prop.load(input);

			//Properties prop=null;
			System.out.println("test reading property should show url: " + prop.getProperty("baseURL"));	
	//		System.out.println("url: "  + prop.getProperty("url"));
			System.out.println("username: "  + prop.getProperty("username"));
			System.out.println("password: "  + prop.getProperty("password"));		
			System.out.println("driverFirefox: "  + prop.getProperty("driverFirefox"));
			System.out.println("flagClearCookiesYN: "  + prop.getProperty("flagClearCookiesYN"));
			System.out.println("driverchrome:"  + prop.getProperty("driverChrome"));
			System.out.println("driveriexplore: "  + prop.getProperty("driverIexplore"));
			System.out.println("flagUserConfirmationYN: "  + prop.getProperty("flagUserConfirmationYN"));
			System.out.println("flagWindowMinimizeYN:"  + prop.getProperty("flagWindowMinimizeYN"));
			System.out.println(prop.stringPropertyNames());		
		}	// Test_099_printlnConfigProperties	
	


	

    @AfterClass
    public static void Test999_Cleanup(){
    	
    	endTestTime = System.nanoTime();  // used within elapsedtime inside of test99Cleanup
		
//     	test99Cleanup(driver);
	System.out.println("Ready to shutdown from within test99Cleanup");
		
		FileWrite.logthis(FileWrite.LogCode.INFO, "Ready to shutdown");
		
		String prettyTimeFormat = reportElapsedTime();  // endTestTime, startTestTime
		FileWrite.logthis(FileWrite.LogCode.INFO, "Elapsed Time: " + prettyTimeFormat); // prettyTimeFormat);
		FileWrite.writeln(FileWrite.fpathName.toString(), "***END OF TEST***"); // uses parent's WEBDRIVER
		
		  int qy = 99;
		  if (askUserQUIT)
		  {  qy =  askUserYN("EXIT TEST  TEST OBJECTS, BROWSERS?","Press Any Key (Y/N/Esc)  to close down all NOW");}
  		  if (!(qy==1 || qy==-1 )) {
			  driver.close(); 
		  driver.quit(); 
			  System.exit(1);  	// maybe exit (0) // 
  		  }
    	
    }
    
    /******** END OF TEST CASESS  ********/
    
    
 
    
    
    
    
    
    
    
    
    
    
    
    
    /******** METHODS BELOW that were CALLED FROM ABOVE TESTS CASES ********/
	
public static void closeAnyChildHandles(){
	
	String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
	String subWindowHandler = null;
  	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with HANDLE: '" + parentWindowHandler + "'");
	Set<String> handles = driver.getWindowHandles(); // get all window handles
	Iterator<String> iterator = handles.iterator();
	while (iterator.hasNext()){
	    subWindowHandler = iterator.next();
//	}
	driver.switchTo().window(subWindowHandler); // switch to popup window

   	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with HANDLE: '" + subWindowHandler + "'");
   	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with url '" + driver.getCurrentUrl()  + "'");	       	
   	if (!subWindowHandler.contentEquals(parentWindowHandler)) driver.close(); 
	driver.switchTo().window(parentWindowHandler);  // switch back to parent window
	}
}  // end method 



	
public static void closeAnyIframesLIST() {
// WebElement iframeElement = driver.findElement(By.tagName("iframe"));
	List<WebElement> iframesList = driver.findElements(By.tagName("iframe"));
	
	if (iframesList.size()>0) {	
	    for (WebElement ifrm:iframesList) {
	    	driver.switchTo().defaultContent();
	    	   	
	       	driver.switchTo().frame(ifrm);
      	sleep(5000);
	       	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with src: '" + ifrm.getAttribute("src") + "'");
	       	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with ID: '" + ifrm.getAttribute("id") + "'");
	       	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with url '" + driver.getCurrentUrl()  + "'");	       	
	       	if (!(ifrm.getAttribute("src").contains("finfiz.com"))){driver.close();}       	
	    } // end for
	} // end if 
	} // closeAnyIframesLIST
	

	 		

public void closeAnyIframes(){  // VERBOSE lists frame properties
	FileWrite.logthis(FileWrite.LogCode.INFO, "'STARTING closeAnyIframes' " ); 
	
	int loopcount=0;
	while(driver.findElements(By.tagName("iframe")).size()>0){
		driver.switchTo().defaultContent();
		loopcount++;
		int howmany = driver.findElements(By.tagName("iframe")).size();
		FileWrite.logthis(FileWrite.LogCode.INFO," ITERATION " + loopcount + " COUNT IFRAMES  '" + howmany  + "'");

		 WebElement whatframe =  driver.findElement(By.tagName("iframe")); // doesn't use LIST in case new iframe added during processing
		 FileWrite.logthis(FileWrite.LogCode.INFO," LINE 445 IFRAME getclass '" + whatframe.getClass()  + "'");
		 		// driver.switchTo().frame(driver.findElements(By.tagName("iframe").get(0));
		 driver.switchTo().frame(whatframe);
		 System.out.println(" IFRAME width " + driver.switchTo().frame(whatframe.getAttribute("width")));
		 System.out.println(" IFRAME height " + driver.switchTo().frame(whatframe.getAttribute("height")));
		 
		 
		 FileWrite.logthis(FileWrite.LogCode.INFO," LINE 448 WHATFRAME IS  '" + whatframe  + "'");

				//	if (  whatframe.isDisplayed()) {
				//  iframe properties: width, height, longdesc, marginheight, marginwidth, scrolling
				
				
		
				
				 		FileWrite.logthis(FileWrite.LogCode.INFO," IFRAME width '" + whatframe.getAttribute("width")  + "'");
				 		FileWrite.logthis(FileWrite.LogCode.INFO," IFRAME height '" + whatframe.getAttribute("height")  + "'");
					 		FileWrite.logthis(FileWrite.LogCode.INFO," IFRAME getclass '" + whatframe.getClass()  + "'");
					       	FileWrite.logthis(FileWrite.LogCode.INFO," IFRAME  url '" +  driver.getCurrentUrl()  + "'");
					       	sleep(3000);
					driver.close();  // if (whatframe(0).getTagName().equalsIgnoreCase("iframe")){driver.close();}
					sleep(3000);
					driver.switchTo().defaultContent();
					sleep(3000);
			//	}  // if 
	} // while loop
	//	 	
	
	}



public void closeAnyIframesZ(){
  driver.switchTo().defaultContent();  // must be on main page to count iframes 
  
while(driver.findElements(By.tagName("iframe")).size()>1) 	
 	{   driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
 		driver.close();
 		driver.switchTo().defaultContent();
 	}
} // closeAnyIframesZ
  



public void closeAnyIframesY(){

	while(driver.findElements(By.tagName("iframe")).size() > 0) {  // rather each of multiple indices, recreate array and user index0 each time

	 driver.switchTo().defaultContent();
	 	  FileWrite.logthis(FileWrite.LogCode.WARN, "Default Window handle: '" + driver.getWindowHandle() + "'"); // .getCurrentUrl()
	 	  FileWrite.logthis(FileWrite.LogCode.WARN, "Parent URL: '" + driver.getCurrentUrl() + "'"); // 
	 	  
          driver.switchTo().frame(0);
          WebElement whatframe =  driver.findElement(By.tagName("iframe"));
          sleep(5000);
          	FileWrite.logthis(FileWrite.LogCode.WARN, "IFRAME handle: '" + driver.getWindowHandle() + "'"); // .getCurrentUrl()
	       	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with src: '" + whatframe.getAttribute("src") + "'");
	       	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with ID: '" + whatframe.getAttribute("id") + "'");
	       	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with TAG: '" + whatframe.getTagName() + "'");	       	
	       	FileWrite.logthis(FileWrite.LogCode.INFO,"in IFRAME with url '" +  driver.getCurrentUrl()  + "'");	       	
 	if (whatframe.getTagName().equalsIgnoreCase("iframe")){driver.close();}       	
	        
	        driver.switchTo().defaultContent();

	} // end while size > 0
 }  // closeAnyIframes

	
	
		
	

	private String getReportFileExtension(ReadPropertyFile prop, WebDriver driver) { // called BEFORE FILEWRITE makes LOGNAME
		if (prop.YNreportCSV().toUpperCase()==prop.YNreportHTML().toUpperCase()) {
			System.out.println("config.properties CONFLICT - choose ONLY one of CSV and HTML reports\n switching to CSV");
			return "CSV";
		} else if (prop.YNreportCSV().toUpperCase().equalsIgnoreCase("Y"))        {
			//  FileWrite.logthis(LogCode.INFO, "Logging to CSV to " + logPathFilename);
			System.out.println("Logging to CSV to " + logPathFilename);
            
			return "CSV"; 
			 }
		else if  (prop.YNreportHTML().toUpperCase().equalsIgnoreCase("Y"))      
		   { // FileWrite.logthis(LogCode.INFO, "Logging HTML to " + logPathFilename);
			   System.out.println("Logging HTML to " + logPathFilename);
			return "HTML";

		   }	
		else { 	FileWrite.logthis(LogCode.WARN, "config.properties has INVALID VALUE: Logging will default to CSV to " + logPathFilename);
		System.out.println("config.properties has INVALID VALUE: Logging CSV to " + logPathFilename);

			return "CSV";
			
		}  // end if else if else    YNreportCSV
	}
	

	/*      *****   *****   *****   *****   *****   *****      */
	
	public int writeWebElementsToHTML(WebDriver driver) {
		int x = 1;
	
// CONSTANTs for logging to an HTML table
 		final String HTMLtableBegin = "<TABLE align=left border=1 width=\"88%\">";
 		final String HTMLtableEnd = "</TABLE>";		
// 		final String ROWTOPCENTERED = "<TR valign=top align=center><TD>";		
 		final String ROWNEW = "<TR ><TD>" ;  	// String ROWNEW = "<TR valign=top><TD>" ;
 		final String ROWEND = "</TD></TR>" ;  // " &nbsp; </TD></TR>" ;
 		final String DELIM = "</TD><TD>" ;
		String bgcolor = ""; // light green #0D0D0A	lightcyan #0C0CFF;	
		
		List<WebElement> elements=driver.findElements(By.xpath(xpathTarget));  
		List<String> excludeList = 	asList(EXCLUDETAGS.toUpperCase().split("\\s*,\\s*"));
		
		for (String element: excludeList) {
			int xyz=0;
		      System.out.println(++xyz + ": " + element);
		}
//  System.exit(0);
		
		FileWrite.write(tempLogFile,HTMLtableBegin);
//Now iterate through List and do the required operation with each individual element
		int zy = 0;

// write top row of column names	
		FileWrite.writeln(tempLogFile,ROWNEW + "#" 
				+ DELIM + "tagName" 
				+ "</td><td width=\"20%\">" + "href" 
				+ DELIM + "ID" 
				+ "</td><td width=\"20%\">" + "CLASS" 
				+ DELIM + "NAME" 
				+ DELIM +  "innerHTML" 
				+ DELIM + "Style"						
				+ ROWEND );	
		for(WebElement ele:elements)
		{ if ( 	!(excludeList.contains(ele.getTagName().toUpperCase())) )
				//!EXCLUDETAGS.toLowerCase().contains(ele.getTagName().toLowerCase())
		 { 	zy++;
		 	if ( (zy & 1) == 0 ) { bgcolor = "''"; } else { bgcolor = "BGCOLOR='#c5ffff;'"; }	// lightgreen #0D0F0A #0D0D0A	lightcyan #0C0CFF;
		  	// String bgcolor = "#dcf395;"; //  #dcf395;	green #0D0D0A;	lightcyan #0C0CFF;	#c5ffff;

//TODO TESTTEST WITH EXCEPTIONS but in REALTEST USE NOT! EXCLUDEFLAG CONTAINS		
				
		// read PROPS  try {   printlnConfigProperties(); // IS THIS REDUNDANT DUPE OF readSystemProperties? 		
				FileWrite.write(tempLogFile,"<TR valign=top " + bgcolor + "><TD>");  
			//	FileWrite.write(tempLogFile,ROWNEW);			// FileWrite.write(tempLogFile,ROWNEW);		
				FileWrite.write(tempLogFile,zy + DELIM);
				FileWrite.write(tempLogFile,ele.getTagName() + DELIM);
				FileWrite.write(tempLogFile,ele.getAttribute("href")  + DELIM);
				FileWrite.write(tempLogFile,ele.getAttribute("id") + DELIM); 
				FileWrite.write(tempLogFile,ele.getAttribute("class") + DELIM); 
				FileWrite.write(tempLogFile,ele.getAttribute("name") + DELIM);
			//	FileWrite.write(tempLogFile,ele.getAttribute("innerHTML") + DELIM); // getText
				if (ele.getTagName().toUpperCase() != "TR") FileWrite.write(tempLogFile,ele.getAttribute("innerHTML") + DELIM); // getText
				FileWrite.write(tempLogFile,ele.getAttribute("style") );				
				FileWrite.writeln(tempLogFile, ROWEND); 				
			} /*else {FileWrite.logthis(LogCode.INFO, "FOUND and EXCLUDED: "+ ROWNEW + ele.getTagName() + DELIM + ele.getAttribute("id") 
			+ DELIM  + ele.getAttribute("class") +  DELIM 	+ ele.getAttribute("name") + ROWEND ) ;} //if !EXCLUDETAG..
*/		}  // end for each element
		FileWrite.write(tempLogFile,HTMLtableEnd);
		return x;
	}  // end method writeWebElementsToHTML

		
	/*      *****   *****   *****   *****   *****   *****      */	

	public static void reportElementAttributes(WebElement ele) {  //have one for CSV and for HTML
			String DelimFieldCSV = "\",\"";
	//		String DelimFieldHTM = "</td><td>";
			String DelimUnique = "|~|";
			String clean = (ele.getTagName() + DelimUnique 
					+ ele.getAttribute("id") + DelimUnique 
					+ ele.getAttribute("class") +  DelimUnique
					+ ele.getAttribute("name") +  DelimUnique
					+ ele.getAttribute("src") +  DelimUnique
					+ ele.getAttribute("style") +  DelimUnique
					+ ele.getText()).replace("\n", "<br />").replaceAll("\t","<tab />") ;
			clean = clean.replace("\"","&quote;");
			clean = clean.replace(DelimUnique,DelimFieldCSV);
		FileWrite.logthis(FileWrite.LogCode.CSV,clean);
		}  // end method reportElementAttributes


	public static String reportElapsedTime(/* uses constants */) {
	//	endTestTime = System.nanoTime(); // long estimatedTime = System.nanoTime() - startTime;
		long TestElapsedTime = endTestTime - startTestTime; // Duration.ofNanos(
		java.time.Duration.ofNanos(TestElapsedTime); // .ofMillis(TestElapsedTime);
		String prettyTimeFormat = org.apache.commons.lang3.time.DurationFormatUtils.formatDuration(TestElapsedTime / 1000000, "HH:mm:ss.S");
		System.out.println("Elapsed Time: " + prettyTimeFormat); // nanoseconds  TestElapsedTime
		return prettyTimeFormat;
	}  // end reportElapsedTime

	
	
	
	
	public static void test99Cleanup(WebDriver driver) { // SWAG calls CLOSE inside try/catch and at end ??
		System.out.println("Ready to shutdown from within test99Cleanup");
		
		FileWrite.logthis(FileWrite.LogCode.INFO, "Ready to shutdown");
		
		String prettyTimeFormat = reportElapsedTime();  // endTestTime, startTestTime
		FileWrite.logthis(FileWrite.LogCode.INFO, "Elapsed Time: " + prettyTimeFormat); // prettyTimeFormat);
		FileWrite.writeln(FileWrite.fpathName.toString(), "***END OF TEST***"); // uses parent's WEBDRIVER
		
		  int qy = 99;
		  if (askUserQUIT)
		  {  qy =  askUserYN("EXIT TEST  TEST OBJECTS, BROWSERS?","Press Any Key (Y/N/Esc)  to close down all NOW");}
  		  if (!(qy==1 || qy==-1 )) {
			  driver.close(); 
			  driver.quit(); 
			  System.exit(1);  	// maybe exit (0) ?   
		  }
	}  // test99Cleanup
	
	
	
public void logPageCheck(){
	FileWrite.logthis(LogCode.INFO, "current URL: " + driver.getCurrentUrl());
	FileWrite.logthis(LogCode.INFO, "current Title: " + driver.getTitle() );
}  // logPageCheck



//	@Test
	public void testShouldFail() throws Exception {
		System.out.println("begin @Test testShouldFail assetNotEquals driver.getTitle at line 148");
	//	driver.getTitle();
		/// assertEquals("Google", driver.getTitle());  	//	TestCase.
		assertNotEquals("Google", driver.getTitle()); 
		
	}


	public static void EMERGENCY_STOP_TEST(String Warning) { // SWAG calls CLOSE inside try/catch and at end ??
		System.out.println("SYSTEM EXIT: " + Warning);
		FileWrite.logthis(FileWrite.LogCode.INFO, "SYSTEM EXIT: "+ Warning);
		// String prettyTimeFormat = reportElapsedTime(endTestTime, startTestTime);
		// FileWrite.logthis(FileWrite.LogCode.INFO, "Elapsed Time: " + prettyTimeFormat); // prettyTimeFormat);
	
	FileWrite.writeln(FileWrite.fpathName.toString(), "***END OF TEST***"); // uses parent's WEBDRIVER
	
	try {
	System.out.println("shutting down driver / open browser windows of '' " + whichBrowser +"''");
		driver.close();
		driver.quit();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		FileWrite.writeln(FileWrite.fpathName.toString(), "failed to close driver: MANUALLY CLOSE any REMAINING instances of '' " + whichBrowser +"''");  
		e.printStackTrace();
	}

	if (!(driver==null)){
		driver.close();
		driver.quit(); 	}
	System.exit(999);
	
	}  // EMERGENCY_STOP_TEST

	
	
	public static int askUserYN(String msgTitle, String msg) {                  //(WebDriver driver) {
		int qy = JOptionPane.showConfirmDialog(null,
				 msg, msgTitle,JOptionPane.YES_NO_OPTION);		
		return qy;  //  if (qy==1 || qy==-1 ) { System.out.println("CANCELLED BY USER!"); System.exit(1);  	 }
	}  // askUserYN
	
	
	public static WebDriver chooseDriverFromBrowserName(String whichBrowser) {
	WebDriver driver= null;
	
	/***   --------- FIREFOX  -----------   ***/
		if (whichBrowser.equalsIgnoreCase("Firefox")) {
			
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("version", "latest");
			capabilities.setCapability("platform", Platform.WINDOWS);
			capabilities.setCapability("name", "Testing Selenium");
			
		    System.setProperty("webdriver.gecko.driver", driverFireFox ); // "C:\\Users\\Allan\\workspace\\Selenium\\webdriver\\Firefox\\geckodriver.exe");
	 driver =  new FirefoxDriver();
 		
			} else if 
	/***   --------- CHROME -----------   ***/		
		
		(whichBrowser.equalsIgnoreCase("CHROME")){
				
				ChromeOptions options = new ChromeOptions();
		//		options.addArguments("--start-maximized");
				options.addArguments("--disable-web-security");
				options.addArguments("--no-proxy-server");

				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);

				options.setExperimentalOption("prefs", prefs);
				
        /********************************* ******************************/				
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				
				capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
				
				capabilities.setCapability("version", "latest");
				capabilities.setCapability("platform", Platform.WINDOWS);
				capabilities.setCapability("name", "Testing Selenium");
         /********************************* ******************************/				
				System.setProperty("webdriver.chrome.driver",driverChrome);
	 driver =  new ChromeDriver();
				 

			}
  /***   --------- EXPLORE -----------   ***/		
			else if(whichBrowser.equalsIgnoreCase("IEXPLORE")){
				//System.out.println("Internet Explorer DRIVER NOT YET INSTALLED, EXIT TEST");
				
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability("version", "latest");
				capabilities.setCapability("platform", Platform.WINDOWS);
				capabilities.setCapability("name", "Testing Selenium");
				
			{ 	System.setProperty("webdriver.ie.driver", driverIexplore); // file.getAbsolutePath() );
			    driver = new InternetExplorerDriver(); 
				// above AND below work identically except below needs import java.io.File;
				// File file = new File("C:/Users/someuser/somepath/IEDriverServer_x64_340/IEDriverServer.exe".replace("/","//"));
				// File file = new File(driverIexplore);
			    // System.setProperty("webdriver.ie.driver", file.getAbsolutePath() );
			}

						
			} else {System.out.println("invalid browser, EXIT TEST");}

		
		return driver;
	}  // askUserYN

	
	
	public static Duration timediff(long before, long after) { // uses int sometime = System.nanoTime();

			long td = (after - before)/1000000; 
			String prettyTimeFormat = org.apache.commons.lang3.time.DurationFormatUtils.formatDuration(td, "HH:mm:ss.S");
			 System.out.println("prettyTimeFormat diff: " + prettyTimeFormat);
			 System.out.println("Duration of java.time nanos: " + java.time.Duration.ofNanos(td));
			// endTestTime = System.nanoTime();   // long estimatedTime = System.nanoTime() - startTime;
		return   java.time.Duration.ofNanos(td) ;		
					
	} // timediff
	
	public static void writeWebElementsToCSV(WebDriver driver) {         //(Logger logger, WebDriver driver) {
		
	//	 final String CSVtableBegin = "";
		 final String ROWNEW = "\""; 
		 final String ROWEND = "\"\n";
		 final String DELIM = "\",\""; 
		 
	List<WebElement> elements=driver.findElements(By.xpath(".//*"));   //       t=".//*
		
	//	logger.info(CSVtableBegin ); // write top column names
	FileWrite.write(tempLogFile,ROWNEW  + "TAG" 
					+ DELIM + "id"  + DELIM + "class" 
	  				+ DELIM + "name" + DELIM + "xpath" + ROWEND
	  				);
	//Now iterate through List and do the required operation with each individual element
	int zy = 0; // initialize outside of loop, only increment when TAB not excluded
		// System.out.println(ROWNEW + zy + DELIM + "tagName" + DELIM + "id" + DELIM + "class" 		+ DELIM + "name" + DELIM + "getText" + ROWEND );

	FileWrite.writeln(tempLogFile,ROWNEW + "#" + DELIM + "tagName" + DELIM + "href" + DELIM + "tagName" + DELIM + "id" + DELIM + "class" + DELIM + "name" + DELIM + "getText" + ROWEND );
		
	for(WebElement ele:elements) {
		  if (!EXCLUDETAGS.contains(ele.getTagName().toUpperCase()) ) {
			zy++;
							//    ele.getText();   //It will print innertext of each element
				FileWrite.write(tempLogFile,ROWNEW);
				FileWrite.write(tempLogFile,zy + DELIM);
				FileWrite.write(tempLogFile,ele.getTagName() + DELIM);
				FileWrite.write(tempLogFile,ele.getAttribute("href") + DELIM);
				FileWrite.write(tempLogFile,ele.getAttribute("id") + DELIM);
				FileWrite.write(tempLogFile,ele.getAttribute("class") + DELIM);
				FileWrite.write(tempLogFile,ele.getAttribute("name") + DELIM);
				FileWrite.write(tempLogFile,ele.getAttribute("getText") + DELIM);	
				FileWrite.write(tempLogFile, ROWEND);
			}  // if not excluded
	 } // for each webelement
  }  // end method writeWebElements
	
	

	public static String datetimestamp() {
		String Tstamp = new SimpleDateFormat( "yyyy-MMddHH-mmss" ).format( Calendar.getInstance().getTime() );  // yyyy-MM-dd_HHmmss.SS
		return Tstamp;
	}  // datetimestamp
	

	
	
	
	



    //Will check to see if an element is not present Instead of returning exception this will return false
    public Boolean isDisplayed(By locator, Integer timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException exception) {
            return false;
        }
        return true;
    }  // isDisplayed
    
    

    public Boolean isDisplayedAndClickable(By locator, Integer timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            wait.until(ExpectedConditions.elementToBeClickable(locator));

        } catch (TimeoutException exception) {
            return false;
        }
        return true;  // org.openqa.selenium.
    }  // isDisplayedAndClickable


 
	
	
	

	
	

    public static boolean verifyEquals(String message, String  expected, String actual) {
        boolean isverified = false;
        if(expected.equals(actual)) {
            //  isverified =  false;
            if(expected instanceof String && actual instanceof String) {
                System.out.println("Verified Equality,  SUCCESS: " + message + " Expected: (" +  expected +") == Found: (" +  actual + ").");
                //  throw new ComparisonFailure(cleanMessage, (String)expected, (String)actual);
                isverified = true;
            } else System.out.println( "Verify NOT EQUAL, FAIL : " + message + " Expected: "
                    + expected + " of length (" + message.length() + ")  But found: " + expected.length() + ")." );
        } return isverified;
    }
    
    public static boolean verifyBoolean(String message, boolean condition) {
        if(condition) {
            System.out.println("Verify SUCCESS : " + message );
            return true;
        }
        System.out.println("Verify FAIL : " + message );
        return false;
    }

	
    public static boolean verifyEqualsLOG(String message, String  expected, String actual) {
        boolean isverified = false;
        if(expected.equals(actual)) {
            //  isverified =  false;
            if(expected instanceof String && actual instanceof String) {
                FileWrite.logthis(LogCode.PASS,"Verified Equality,  SUCCESS: " + message + " Expected: (" +  expected +") == Found: (" +  actual + ").");
                
                //  throw new ComparisonFailure(cleanMessage, (String)expected, (String)actual);
                isverified = true;
            } else 
            	FileWrite.logthis(LogCode.FAIL,"Verify NOT EQUAL, FAIL : " + message + " Expected: "
                        + expected + " of length (" + message.length() + ")  But found: " + expected.length() + ").");
        } return isverified;
    }



    
    public static boolean verifyBooleanLOG(String message, boolean condition) {
        if(condition) {
     //       String cleanMessage = message == null?"":message;
            FileWrite.logthis(LogCode.INFO,"Verify SUCCESS : " + message );
            return true;
        }
        FileWrite.logthis(LogCode.WARN,"Verify FAIL : " + message );
        return false;
    }


public static void ifAlertPresentAcceptYN(char YN ) {
    try {
        driver.switchTo().alert();
        if(YN=='N' || YN=='n') {driver.switchTo().alert().dismiss();} else {driver.switchTo().alert().accept();} 
    } catch (NoAlertPresentException e) {
        System.out.println("No Alert found");
    }
}    // isAlertPresent
    

public static void sleep(int millsecs) {
	   try { 		Thread.sleep(millsecs);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} // milliseconds
}
	
	
	
}



