package run

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Nullable;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiSelector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.appium.uiautomator2.common.exceptions.InvalidElementStateException;
import io.appium.uiautomator2.common.exceptions.InvalidSelectorException;
import io.appium.uiautomator2.common.exceptions.UiAutomator2Exception;
import io.appium.uiautomator2.model.ScreenOrientation;
import io.appium.uiautomator2.utils.Device;
import io.appium.uiautomator2.utils.Logger;
import io.appium.uiautomator2.utils.NodeInfoList;
import io.appium.uiautomator2.utils.ReflectionUtils;

import static io.appium.uiautomator2.utils.AXWindowHelpers.getCachedWindowRoots;
import static io.appium.uiautomator2.utils.Device.getUiDevice;
import static io.appium.uiautomator2.utils.ReflectionUtils.getConstructor;
import static io.appium.uiautomator2.utils.ReflectionUtils.getField;
import static io.appium.uiautomator2.utils.ReflectionUtils.invoke;
import static io.appium.uiautomator2.utils.ReflectionUtils.getMethod;

 public ScreenOrientation setOrientationSync(ScreenOrientation desired) {
        if (ScreenOrientation.current() == desired) {
            return desired;
        }

        getInstrumentation().getUiAutomation().setRotation(desired.ordinal());
        long start = System.currentTimeMillis();
        do {
            if (ScreenOrientation.current() == desired) {
                return desired;
            }
            SystemClock.sleep(100);
        } while (System.currentTimeMillis() - start < CHANGE_ORIENTATION_TIMEOUT_MS);
        throw new InvalidElementStateException(String.format("Screen orientation cannot be changed to %s after %sms. " +
                "Is it locked programmatically?", desired.toString(), CHANGE_ORIENTATION_TIMEOUT_MS));
    }
