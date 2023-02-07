package test
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import java.nio.charset.StandardCharsets
import java.text.MessageFormat

import org.apache.commons.lang.StringUtils
import org.openqa.selenium.UnhandledAlertException

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.driver.DriverType
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.util.internal.PathUtil
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain
import com.kms.katalon.core.webui.util.WinRegistry

class test {
	/**
	 * Refresh browser
	 */
	@Keyword
	public void authenticate(final String url, String userName, String password, int timeout,
			FailureHandling flowControl) {

		WebUIKeywordMain.runKeyword({
			String osName = System.getProperty("os.name")
			DriverType executedBrowser = DriverFactory.getExecutedBrowser()

			if (osName == null || !osName.toLowerCase().contains("win")) {
				throw new Exception("Unsupported platform (only support Windows)")
			}

			if( executedBrowser != WebUIDriverType.IE_DRIVER &&
			executedBrowser != WebUIDriverType.FIREFOX_DRIVER &&
			executedBrowser != WebUIDriverType.CHROME_DRIVER &&
			executedBrowser != WebUIDriverType.EDGE_CHROMIUM_DRIVER){
				throw new Exception("Unsupported browser (only support IE, FF, Chrome and Edge Chromium)")
			}

			if (userName == null) {
				throw new IllegalArgumentException(StringConstants.KW_EXC_USERNAME_IS_NULL)
			}
			if (password == null) {
				throw new IllegalArgumentException(StringConstants.KW_EXC_PASSWORD_IS_NULL)
			}

			String usernamePasswordURL = getAuthenticatedUrl(PathUtil.getUrl(url, "https"), userName, password)
			boolean authenticateSuccess = false

			//Google Chrome and Firefox
			if (DriverFactory.getExecutedBrowser() == WebUIDriverType.CHROME_DRIVER
			|| DriverFactory.getExecutedBrowser() == WebUIDriverType.FIREFOX_DRIVER
			|| DriverFactory.getExecutedBrowser() == WebUIDriverType.EDGE_CHROMIUM_DRIVER) {
				authenticateSuccess = isNavigateOnChromeFirefoxSuccess(usernamePasswordURL)
			}

			//Internet Explorer
			if (DriverFactory.getExecutedBrowser() == WebUIDriverType.IE_DRIVER) {
				WinRegistry.enableUsernamePasswordOnURL()
				authenticateSuccess = isNavigateOnIESuccess(usernamePasswordURL, url, timeout)
			}

			if (authenticateSuccess) {
				println MessageFormat.format(StringConstants.KW_LOG_PASSED_NAVIAGTED_TO_AUTHENTICATED_PAGE, [userName, password] as Object[])
			} else {
				WebUIKeywordMain.stepFailedWithReason(StringConstants.KW_MSG_CANNOT_NAV_TO_AUTHENTICATED_PAGE, flowControl, "", false)
			}

		}, flowControl, false, StringConstants.KW_MSG_CANNOT_NAV_TO_AUTHENTICATED_PAGE)
	}

	private boolean isNavigateOnChromeFirefoxSuccess(String usernamePasswordURL) {
		try {
			WebDriver driver = DriverFactory.getWebDriver()

			driver.navigate().to(usernamePasswordURL)
			if (usernamePasswordURL.equals(driver.getCurrentUrl())) {
				return true
			}
			return false

		} catch (UnhandledAlertException e) {
			return false
		}
	}

	private boolean isNavigateOnIESuccess(String usernamePasswordURL, String url, int timeout) {
		WebDriver driver = DriverFactory.getWebDriver()
		Thread navigateThread = null

		try {
			String currentURL ="";
			//start new thread to try navigate
			if (!StringUtils.isEmpty(url)) {
				navigateThread = new Thread() {
							public void run() {
								driver.navigate().to(usernamePasswordURL)
								currentURL = driver.getCurrentUrl()
							}
						}
				navigateThread.start()
			}

			//check if it can navigate to the destination site
			long startPolling = System.currentTimeMillis();
			while((System.currentTimeMillis() - startPolling) < timeout*1000L) {
				if (usernamePasswordURL.equals(currentURL)) {
					return true;
				}
				Thread.sleep(200L)
			}
			return false;
		} finally {
			if (navigateThread != null && navigateThread.isAlive()) {
				navigateThread.interrupt()
			}
		}
	}

	private String getAuthenticatedUrl(URL url, String userName, String password) {
		StringBuilder getAuthenticatedUrl = new StringBuilder()

		getAuthenticatedUrl.append(url.getProtocol())
		getAuthenticatedUrl.append("://")
		getAuthenticatedUrl.append(URLEncoder.encode(userName, StandardCharsets.UTF_8.toString()))
		getAuthenticatedUrl.append(":")
		getAuthenticatedUrl.append(URLEncoder.encode(password, StandardCharsets.UTF_8.toString()))
		getAuthenticatedUrl.append("@")
		getAuthenticatedUrl.append(url.getHost())
		getAuthenticatedUrl.append(url.getPath())

		return getAuthenticatedUrl.toString()
	}
}