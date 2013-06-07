package com.masr.masrutils.test;

import static org.junit.Assert.assertTrue;
import in.masr.masrutils.Config;
import in.masr.masrutils.FileOperation;
import in.masr.masrutils.Logger;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoggerTest {

	private static FileOperation fileOperation;
	private static FileOperation fileOperationAbs;
	private static String BASE_FOLDER = "/home/maosuhan/workspace/java/MasrUtils";

	@Test
	public void justWriteOutLog() {
		Logger.out("A simple record");
		String content = fileOperationAbs.getContent(Logger.getOutFilePath());
		assertTrue(content.contains("A simple record"));
	}

	@Test
	public void justWriteErrLog() {
		Logger.err("A simple record");
		String content = fileOperationAbs.getContent(Logger.getErrFilePath());
		assertTrue(content.contains("A simple record"));
	}

	@Test
	public void justWriteBoth() {
		Logger.err("A simple error");
		Logger.out("A simple output");
		String content = fileOperationAbs.getContent(Logger.getErrFilePath());
		assertTrue(content.contains("A simple error"));
		content = fileOperationAbs.getContent(Logger.getOutFilePath());
		assertTrue(content.contains("A simple output"));
	}

	@Test
	public void setLogPath() {
		Logger.setLogFolderAbsolutePath(Config.HOME + File.separator + "log2");
		Logger.setLogFolderAbsolutePath(Config.HOME + File.separator + "log2");
		Logger.out("useDefinedLogPath");
		Logger.err("useDefinedLogPathErr");
		String outPath = Logger.getOutFilePath();
		assertTrue(outPath.contains("log2"));
		String content = fileOperationAbs.getContent(outPath);
		assertTrue(content.contains("useDefinedLogPath"));
		String errPath = Logger.getErrFilePath();
		assertTrue(errPath.contains("log2"));
		content = fileOperationAbs.getContent(errPath);
		assertTrue(content.contains("useDefinedLogPathErr"));

		fileOperation.mkDir("log3");
		Logger.setLogFolderAbsolutePath(Config.HOME + File.separator + "log3");
		Logger.out("useDefinedLogPath");
		Logger.err("useDefinedLogPathErr");
		outPath = Logger.getOutFilePath();
		assertTrue(outPath.contains("log3"));
		content = fileOperationAbs.getContent(outPath);
		assertTrue(content.contains("useDefinedLogPath"));
		errPath = Logger.getErrFilePath();
		assertTrue(errPath.contains("log3"));
		content = fileOperationAbs.getContent(errPath);
		assertTrue(content.contains("useDefinedLogPathErr"));
		fileOperation.deleteFolder("log3");
	}

	@Test
	public void preName() {
		Logger.setPrefixName("pre");
		Logger.out("haha");
		Logger.err("hahaErr");
		String outPath = Logger.getOutFilePath();
		assertTrue(outPath.matches(".*\\/pre\\_.*"));
		String errPath = Logger.getErrFilePath();
		assertTrue(errPath.matches(".*\\/pre\\_.*"));
		String content = fileOperationAbs.getContent(Logger.getOutFilePath());
		assertTrue(content.contains("haha"));
		content = fileOperationAbs.getContent(Logger.getErrFilePath());
		assertTrue(content.contains("hahaErr"));
	}

	@Test
	public void mute() {
		Logger.out("4321");
		Logger.mute();
		Logger.out("1234");
		String content = fileOperationAbs.getContent(Logger.getOutFilePath());
		assertTrue(content.contains("4321"));
		assertTrue(!content.contains("1234"));
	}

	@Test
	public void errInMute() {
		Logger.err("hi");
		Logger.mute();
		Logger.err("huhu");
		Logger.sound();
		Logger.err("123");
		String content = fileOperationAbs.getContent(Logger.getErrFilePath());
		assertTrue(content.contains("hi"));
		assertTrue(content.contains("huhu"));
		assertTrue(content.contains("123"));
	}

	@Test
	public void stdErrInMute() {
		System.err.println("hi");
		Logger.mute();
		System.err.println("huhu");
		Logger.sound();
		System.err.println("123");
		String content = fileOperationAbs.getContent(Logger.getErrFilePath());
		assertTrue(content.contains("hi"));
		assertTrue(content.contains("huhu"));
		assertTrue(content.contains("123"));
	}

	@Test
	public void sound() {
		Logger.out("4321");
		Logger.mute();
		Logger.out("1234");
		Logger.sound();
		Logger.out("9876");
		String content = fileOperationAbs.getContent(Logger.getOutFilePath());
		assertTrue(content.contains("4321"));
		assertTrue(!content.contains("1234"));
		assertTrue(content.contains("9"));
	}

	@Test
	public void stdOut() {
		System.out.println("i love you!");
		Logger.out("i hate you!");
		System.out.println("i cheat you!");
		String content = fileOperationAbs.getContent(Logger.getOutFilePath());
		int index1 = content.indexOf("i love you");
		int index2 = content.indexOf("i hate you");
		int index3 = content.indexOf("i cheat you");
		assertTrue(index1 != -1);
		assertTrue(index2 != -1);
		assertTrue(index3 != -1);
		assertTrue(index2 > index1);
		assertTrue(index3 > index2);
	}

	@Test
	public void stdErr() {
		System.err.println("i love you!");
		Logger.err("i hate you!");
		System.err.println("i cheat you!");
		String content = fileOperationAbs.getContent(Logger.getErrFilePath());
		int index1 = content.indexOf("i love you");
		int index2 = content.indexOf("i hate you");
		int index3 = content.indexOf("i cheat you");
		assertTrue(index1 != -1);
		assertTrue(index2 != -1);
		assertTrue(index3 != -1);
		assertTrue(index2 > index1);
		assertTrue(index3 > index2);
	}

	@Test
	public void testTeception() {
		try {
			throw new Exception();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String content = fileOperationAbs.getContent(Logger.getErrFilePath());
		assertTrue(content
				.contains("at com.masr.masrutils.test.LoggerTest.testTeception"));
	}

	@Test
	public void combineUsage() {
		// Out log
		Logger.setLogFolderAbsolutePath("log2");
		Logger.setPrefixName("kickoff");
		Logger.out("str03");
		Logger.err("str04");
		System.out.println("str05");
		System.err.println("str06");
		Logger.mute();
		Logger.out("str07");
		System.out.println("str08");
		System.err.println("str09");
		Logger.err("str10");
		Logger.sound();
		Logger.out("str11");
		Logger.err("str12");
		System.out.println("str13");
		System.err.println("str14");
		String outpath = Logger.getOutFilePath();
		String errPath = Logger.getErrFilePath();
		assertTrue(outpath.matches(".*\\/kickoff\\_.*"));
		assertTrue(errPath.matches(".*\\/kickoff\\_.*"));
		String content = fileOperationAbs.getContent(outpath);
		int index1 = content.indexOf("str03");
		int index2 = content.indexOf("str05");
		int index3 = content.indexOf("str11");
		int index4 = content.indexOf("str13");
		assertTrue(index1 != -1);
		assertTrue(index2 != -1);
		assertTrue(index3 != -1);
		assertTrue(index4 != -1);
		assertTrue(index2 > index1);
		assertTrue(index3 > index2);
		assertTrue(index4 > index3);

		index1 = content.indexOf("str04");
		index2 = content.indexOf("str06");
		index3 = content.indexOf("str07");
		index4 = content.indexOf("str08");
		int index5 = content.indexOf("str09");
		int index6 = content.indexOf("str10");
		int index7 = content.indexOf("str12");
		int index8 = content.indexOf("str14");
		assertTrue(index1 == -1);
		assertTrue(index2 == -1);
		assertTrue(index3 == -1);
		assertTrue(index4 == -1);
		assertTrue(index5 == -1);
		assertTrue(index6 == -1);
		assertTrue(index7 == -1);
		assertTrue(index8 == -1);

		// Err log
		content = fileOperationAbs.getContent(errPath);
		index1 = content.indexOf("str04");
		index2 = content.indexOf("str06");
		index5 = content.indexOf("str09");
		index6 = content.indexOf("str10");
		index3 = content.indexOf("str12");
		index4 = content.indexOf("str14");
		assertTrue(index1 != -1);
		assertTrue(index2 != -1);
		assertTrue(index3 != -1);
		assertTrue(index4 != -1);
		assertTrue(index5 != -1);
		assertTrue(index6 != -1);
		assertTrue(index2 > index1);
		assertTrue(index5 > index2);
		assertTrue(index6 > index5);
		assertTrue(index3 > index6);
		assertTrue(index4 > index3);

		index1 = content.indexOf("str03");
		index2 = content.indexOf("str05");
		index3 = content.indexOf("str07");
		index4 = content.indexOf("str08");
		index7 = content.indexOf("str11");
		index8 = content.indexOf("str13");
		assertTrue(index1 == -1);
		assertTrue(index2 == -1);
		assertTrue(index3 == -1);
		assertTrue(index4 == -1);
		assertTrue(index7 == -1);
		assertTrue(index8 == -1);

	}

	@BeforeClass
	public static void init() throws IOException {
		fileOperation = new FileOperation(BASE_FOLDER);
		fileOperation.copyFile("test_fixture/config.ini", "config.ini");
		fileOperation.writeFile("runtime_env", "config.ini");
		fileOperationAbs = new FileOperation();
	}

	@Before
	public void setUp() {
		fileOperation.mkDir("log");
		fileOperation.mkDir("log2");
		// Add this to set Logger to non-init status. We need to reinit Logger
		// when in different methods. Static class unit test in Junit is very
		// tricky.
		Logger.clean();
		Logger.init();
	}

	@After
	public void tearDown() {
		fileOperation.deleteFolder("log");
		fileOperation.deleteFolder("log2");
	}

	@AfterClass
	public static void destroy() {
		fileOperation.deleteFile("config.ini");
		fileOperation.deleteFile("runtime_env");

	}

}
