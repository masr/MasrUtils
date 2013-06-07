package com.masr.masrutils.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UtilsTest.class, FileOperationNativeTest.class,
		ConfigTest.class, LoggerTest.class })
public class SuiteTest {

}
