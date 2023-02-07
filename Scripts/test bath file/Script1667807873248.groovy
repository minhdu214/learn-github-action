import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration as Runconfiguration

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


	//String bf = Runconfiguration.getProjectDir() + '/' + 'TEST.bat'
	//WebUI.comment("Running batch file: " + bf)
	//Runtime.runtime.exec('C:\\Users\\duc.phan\\git\\Halo Halo\\TEST.bat')
	cmd = "cmd /c start C:\\Users\\duc.phan\\git\\Halo\\TEST.bat"

	Runtime.getRuntime().exec(cmd)
	
	
	
	WebUI.delay(5)

import com.kms.katalon.core.configuration.RunConfiguration
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path inputXML = projectDir.resolve('Input.xml')
Path outputXML = projectDir.resolve('Output.xml')

// load Input.xml into a String
String content = inputXML.toFile().getText()

// do String manipulation
String value = 'VAR4'
String replaced = content.replaceAll(
    '',
    "${value}")
// save the String into Output.xml
Files.createDirectories(outputXML.getParent())
outputXML.toFile().write(replaced)
System.out.println("PLS find the output at ${outputXML.toString()}")
