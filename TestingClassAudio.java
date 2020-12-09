import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class TestingClass {
	
	// Expected outputs associated w/ each file
	static String outputs[] = {"What is the purpose of a gallbladder?", 
							   "If a car travels 10 miles in 10 minutes, what is its velocity in miles per hour?",
							   "What is the atomic number of oxygen?",
							   "If x + 2 equals 20, What is x?",
							   "What is the integral of x squared?",
							   "What is the area of a circle with a radius of 5?",
							   "What is a pronoun?",
							   "How much did the Louisiana Purchase cost?",
							   "What year did Genghis Khan die?"};

	public static void main(String[] args) throws MalformedURLException, InterruptedException, JavaLayerException {
		
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(MobileCapabilityType.DEVICE_NAME, "710KPFX0233715");
		dc.setCapability("platformName", "android");
		
		AndroidDriver<AndroidElement> ad = new AndroidDriver<AndroidElement>(new URL("http://0.0.0.0:4723/wd/hub/"), dc);
		
		// Loop test sequence for each test
		for (int i = 0; i < 9; i++) {
			MobileElement el1 = (MobileElement) ad.findElementByAccessibilityId("Start/Stop the voice query");
			el1.click();
			ad.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
			
			// Code to play the audio file for this loop.
			// Note that this plays the audio from your computer's speakers
			try {
				FileInputStream audio;
				if (i == 0)
					audio = new FileInputStream(
							new File("C:\\Users\\addav"
									+ "\\Desktop\\SJSU Courses"
									+ "\\SJSU Semester 5\\CMPE 187"
									+ "\\Labs\\Lab 2\\lab2-workspace"
									+ "\\lab2-proj\\src\\Audio.mp3"));
				else
					audio = new FileInputStream(
							new File("C:\\Users\\addav"
									+ "\\Desktop\\SJSU Courses"
									+ "\\SJSU Semester 5\\CMPE 187"
									+ "\\Labs\\Lab 2\\lab2-workspace"
									+ "\\lab2-proj\\src\\Audio" + (i+1) + ".mp3"));
				BufferedInputStream buffer = new BufferedInputStream(audio);
				Player player = new Player(buffer);
				
				player.play();
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
			
			// If Socratic doesn't auto complete, then manually enter the 'stop' button
			el1 = (MobileElement) ad.findElementByAccessibilityId("Start/Stop the voice query");
			try {
				if (el1 != null && el1.isDisplayed())
					el1.click();
			} catch (NoSuchElementException e) {
			}
			ad.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			// Get the text from the output element
			MobileElement el2 = (MobileElement) ad.findElementById("com.google.socratic:id/query_text");
			Thread.sleep(5000);
			
			// Compare our expected outputs with the actual output
			if(outputs[i].toLowerCase().contains(el2.getText().toLowerCase()) ||
					el2.getText().toLowerCase().contains(outputs[i].toLowerCase()) ||
					el2.getText().toLowerCase().equals(outputs[i])) {
				System.out.println("PASS:" + " Audio" + (i+1));
			} 
			// If the outputs don't match, then the test case fails
			else {
				System.out.println("FAIL: " + " Audio" + (i+1));
			}
			ad.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
			Thread.sleep(2000);
			
			// Go back to the speaker starting position for the next test in the loop
			MobileElement el3 = (MobileElement) ad.findElementByAccessibilityId("Navigate up");
			el3.click();
			Thread.sleep(2000);
		}
	}
}
