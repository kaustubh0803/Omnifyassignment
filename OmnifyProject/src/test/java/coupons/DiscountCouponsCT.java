package coupons;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;


@Test
public class DiscountCouponsCT {

	WebDriver driver;
	WebDriverWait wait;
	
public void applyDiscountCoupon() {
		
	//Driver initialization
		ChromeOptions option= new ChromeOptions();
		option.addArguments("--incognito");
		driver=new ChromeDriver(option);
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		
		//launching the application URL
		driver.get("https://www.cleartrip.com/hotels");
		driver.manage().deleteAllCookies();
		
		//Explicit wait object
		wait=new WebDriverWait(driver, Duration.ofSeconds(10));
		
		//Cancel the login popup
		driver.findElement(By.cssSelector(".pb-1.px-1.flex.flex-middle.nmx-1 svg")).click();
		
		//Enter "New York" as city name
		driver.findElement(By.cssSelector(".sc-aXZVg.dhukqX")).click();
		driver.findElement(By.cssSelector(".sc-aXZVg.dhukqX input")).sendKeys("New York");
		
		//wait for the options list to display so that we can select New York from it
	    By searchOptions=By.cssSelector("#modify_search_list_container_id");
	    wait.until(ExpectedConditions.presenceOfElementLocated(searchOptions));
	    
	    //catch all the elements from options containing New york as text and click on our expected first element.
	    By searchExpectedOptions=By.xpath("//li[@class='ls-reset  w-100p px-3 dropdown-new__item c-pointer c-neutral-900 br-4 px-3 py-4 flex flex-start  dropdown-new__item-fill hotelSuggestionList']//p[text()='New York']");
	    wait.until(ExpectedConditions.presenceOfElementLocated(searchExpectedOptions));
	    List<WebElement> options=driver.findElements(searchExpectedOptions);
	    WebElement expectedElement=options.get(0);
		expectedElement.click();
	    
		//Navigate to the Month April 2026 in the calendar and select the dates 
	   driver.findElement(By.cssSelector(".sc-aXZVg.eJFhSz.mr-2")).click();
	   
	   wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".flex-1.ta-right svg")));
	   
	   List<String> monthValue=new ArrayList<String>();
	   do {
		   driver.findElement(By.xpath("//div[@class='flex-1 ta-right']//*[name()='svg']")).click(); //keep clicking on next month button
		   List<WebElement> months=driver.findElements(By.cssSelector(".DayPicker-Caption div"));
		   
		   for(WebElement i:months) {
			monthValue.add(i.getText());
	   }
	   }
	   while(!(monthValue.contains("April 2026")));
		
	 //pick the required dates 10 APRIL and 15 APRIL
	   
	  driver.findElement(By.xpath("//div[@class='DayPicker-Caption']/div[text()='April 2026']/parent::div/parent::div//div[@class='DayPicker-Day']/div/div[text()='10']")).click();
	  driver.findElement(By.xpath("//div[@class='DayPicker-Caption']/div[text()='April 2026']/parent::div/parent::div//div[@class='DayPicker-Day']/div/div[text()='15']")).click();
	  
	  // Click on Search Hotels button
	  driver.findElement(By.cssSelector(".sc-aXZVg.ibgoAF")).click();
	  
	  //wait until search Results header is present
	  wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sc-fqkvVR.fMTZbq.fs-24.lh-28.fw-600")));
	  
	  //Click on the first hotel from the results displayed
	  List<WebElement> results=driver.findElements(By.cssSelector(".sc-aXZVg.ipMXeA"));
	  results.get(0).click();
	  
	  //Get all windows handles and switch to the first hotel page which opens in a new window
	  Set<String> windows=driver.getWindowHandles();
	  Iterator<String> it=windows.iterator();
	  String parentWindow=it.next();
	  String resultWindow=it.next();
	  
	  driver.switchTo().window(resultWindow);
	  
	  //Clicking on the Select Room option for the Hotel
	  driver.findElement(By.xpath("//div[@id='selectRoomHighlights']//h4[@class='sc-fqkvVR dxBAiq' and text()='Select room']")).click();
	  
	  //Clicking on the first Book option available 
	  List<WebElement> bookButtonList=driver.findElements(By.cssSelector(".sc-gFqAkR.juJaJB"));
	  WebElement expectedBookElement=bookButtonList.get(0);
	  
	  ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'end'});", expectedBookElement);
	
	  expectedBookElement.click();
	  
	  //The Checkout page for the selected Hotel room option opens on a new page. So Navigate to the Checkout page
	  //Then apply the Discount coupon and  check if it is applied
	  Set<String> windows1=driver.getWindowHandles();
	  Iterator<String> it1=windows1.iterator();
	  while(it1.hasNext()) {
		  driver.switchTo().window(it1.next());
		  System.out.println(driver.getCurrentUrl());
		  if(driver.getCurrentUrl().contains("itinerary")) {
			  driver.findElement(By.xpath("//input[@id='Coupon code']")).click();
			  driver.findElement(By.xpath("//input[@id='Coupon code']")).sendKeys("SUMMER25");
			  driver.findElement(By.cssSelector(".sc-fqkvVR.fNvFQA")).click();
			  wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sc-fqkvVR.bpDEXR")));
			  String couponError=driver.findElement(By.cssSelector(".sc-fqkvVR.bpDEXR")).getText();
			  Assert.assertTrue(couponError.contains("property cannot be discounted"));  //Since it is an invalid coupon
		  };
		  
	  }
	  
	  //Click on the Proceed to Payment button
	  WebElement paymentButton=driver.findElement(By.cssSelector(".sc-gFqAkR.cSsgeC.sc-b19c115c-0.heCgQz"));
	  ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'end'});", paymentButton);
	  paymentButton.click();
	  
	  
	  
			  }


}
