package com.stta.SuiteOne;

import java.io.IOException;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.stta.utility.Read_XLS;
import com.stta.utility.SuiteUtility;

public class GetTitleofHomePage extends SuiteOneBase{
	
	Read_XLS FilePath = null;	
	String SheetName = null;
	String TestCaseName = null;	
	String ToRunColumnNameTestCase = null;
	String ToRunColumnNameTestData = null;
	String TestDataToRun[]=null;
	static boolean TestCasePass=true;
	static int DataSet=-1;	
	static boolean Testskip=false;
	static boolean Testfail=false;
	SoftAssert s_assert =null;
	
	@BeforeTest
	public void checkCaseToRun() throws IOException{
		//Called init() function from SuiteBase class to Initialize .xls Files
		init();	
		//To set SuiteOne.xls file's path In FilePath Variable.
		FilePath = TestCaseListExcelOne;		
		TestCaseName = this.getClass().getSimpleName();
		//SheetName to check CaseToRun flag against test case.
		SheetName = "TestCasesList";
		//Name of column In TestCasesList Excel sheet.
		ToRunColumnNameTestCase = "CaseToRun";
		//Name of column In Test Case Data sheets.
		//ToRunColumnNameTestData = "DataToRun";
		
		//To check test case's CaseToRun = Y or N In related excel sheet.
		//If CaseToRun = N or blank, Test case will skip execution. Else It will be executed.
		if(!SuiteUtility.checkToRunUtility(FilePath, SheetName,ToRunColumnNameTestCase,TestCaseName)){			
			//To report result as skip for test cases In TestCasesList sheet.
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "SKIP");
			//To throw skip exception for this test case.
			throw new SkipException(TestCaseName+"'s CaseToRun Flag Is 'N' Or Blank. So Skipping Execution Of "+TestCaseName);
		}
	}
	
	
	@Test
	public void getTitle()
	{
		//To Initialize Firefox browser.
				loadWebBrowser();
				driver.get(Param.getProperty("siteURL"));
				String sActualResult = driver.getTitle();
				String sExpectedResult = "Product Design and Development Company | Quovantis Technologies";
				if(!(sActualResult.equals(sExpectedResult))){
					Testfail=true;	
					s_assert.assertEquals(sActualResult, sExpectedResult, "Actual Result Value "+sActualResult+" And ExpectedResult Value "+sExpectedResult+" Not Match");
				}
				
				if(Testfail){
					//At last, test data assertion failure will be reported In testNG reports and It will mark your test data, test case and test suite as fail.
					s_assert.assertAll();		
				}
	}
	
	
	
	//@AfterMethod method will be executed after execution of @Test method every time.
		@AfterMethod
		public void reporterDataResults(){		
			if(Testskip){
				//If found Testskip = true, Result will be reported as SKIP against data set line In excel sheet.
				SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "SKIP");
			}
			else if(Testfail){
				//To make object reference null after reporting In report.
				s_assert = null;
				//Set TestCasePass = false to report test case as fail In excel sheet.
				TestCasePass=false;	
				//If found Testfail = true, Result will be reported as FAIL against data set line In excel sheet.
				SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "FAIL");			
			}
			else{
				//If found Testskip = false and Testfail = false, Result will be reported as PASS against data set line In excel sheet.
				SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "PASS");
			}
			//At last make both flags as false for next data set.
			Testskip=false;
			Testfail=false;
		}
		
		
		//To report result as pass or fail for test cases In TestCasesList sheet.
		@AfterTest
		public void closeBrowser(){
			//To Close the web browser at the end of test.
			closeWebBrowser();
			if(TestCasePass){
				SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "PASS");
			}
			else{
				SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "FAIL");			
			}		
		}

}
