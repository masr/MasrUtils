package com.masr.masrutils.test;

import java.io.File;
import java.io.IOException;

import in.masr.masrutils.FileOperation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileOperationNativeTest {
	private static FileOperation fileOperation;
	private static FileOperation fileOperationAbs;
	private static String FILE_OPERATION_FIXTURE = "MasrUtils/test_fixture"
			+ File.separator + "file_operation_fixture";
	private static String TEST_FOLDER = "MasrUtils/file_operation_test_folder";
	private static String BASE_FOLDER = "/home/maosuhan/workspace/java";

	@Test
	public void fileExists() {
		boolean exists = fileOperation.fileExists(FILE_OPERATION_FIXTURE
				+ File.separator + "welcome.txt");
		assertTrue(exists);
		exists = fileOperation.fileExists(FILE_OPERATION_FIXTURE
				+ File.separator + "welcome.txt2");
		assertFalse(exists);
		exists = fileOperation.fileExists(FILE_OPERATION_FIXTURE
				+ File.separator + "welcome");
		assertFalse(exists);

	}

	@Test
	public void fileGetContent() throws IOException {
		String path = FILE_OPERATION_FIXTURE + File.separator + "welcome.txt";
		String content = fileOperation.getContent(path);
		assertEquals("welcome\nwelcome1\nwelcome2\n", content);
		path = FILE_OPERATION_FIXTURE + File.separator + "welcome2.txt";
		content = fileOperation.getContent(path);
		assertEquals("welcome\nwelcome1\n", content);
	}

	@Test
	public void writeFile() {
		String path = TEST_FOLDER + File.separator + "test.txt";
		fileOperation.writeFile(path, "123");
		String content = fileOperation.getContent(path);
		assertEquals("123\n", content);
		fileOperation.writeFile(path, "123\n");
		content = fileOperation.getContent(path);
		assertEquals("123\n", content);
	}

	@Test
	public void copyFile() {
		String fromPath = FILE_OPERATION_FIXTURE + File.separator + "love"
				+ File.separator + "test.html";
		String toPath = TEST_FOLDER + File.separator + "test2.html";
		fileOperation.copyFile(fromPath, toPath);
		boolean exists = fileOperation.fileExists(toPath);
		assertTrue(exists);
		String content = fileOperation.getContent(toPath);
		assertEquals("<html></html>\n", content);
	}

	@Test
	public void deleteFile() {
		String path = TEST_FOLDER + File.separator + "test.txt";
		fileOperation.writeFile(path, "123");
		boolean fileExists = fileOperation.fileExists(path);
		assertTrue(fileExists);
		fileOperation.deleteFile(path);
		fileExists = fileOperation.fileExists(path);
		assertFalse(fileExists);
	}

	@Test
	public void mkDir() {
		String path = TEST_FOLDER + File.separator + "play";
		fileOperation.mkDir(path);
		boolean fileExists = fileOperation.fileExists(path);
		assertTrue(fileExists);
		String fullPath = BASE_FOLDER + File.separator + path;
		File file = new File(fullPath);
		assertTrue(file.isDirectory());
	}

	@Test
	public void copyDir() {
		String guess = TEST_FOLDER + File.separator + "guess";
		fileOperation.copyFolder(FILE_OPERATION_FIXTURE, guess);
		assertTrue(fileOperation.fileExists(guess));
		assertTrue(fileOperation.fileExists(guess + File.separator + "love"));
		assertTrue(fileOperation.fileExists(guess + File.separator
				+ "welcome.txt"));
		assertTrue(fileOperation.fileExists(guess + File.separator
				+ "welcome2.txt"));
		assertTrue(fileOperation.fileExists(guess + File.separator + "love"
				+ File.separator + "test.html"));

		String content = fileOperation.getContent(guess + File.separator
				+ "love" + File.separator + "test.html");
		assertEquals("<html></html>\n", content);
	}

	@Test
	public void emptyFolder() {
		String guess = TEST_FOLDER + File.separator + "guess";
		fileOperation.copyFolder(FILE_OPERATION_FIXTURE, guess);
		assertTrue(fileOperation.fileExists(guess + File.separator + "love"
				+ File.separator + "test.html"));
		fileOperation.emptyFolder(guess);
		assertFalse(fileOperation.fileExists(guess + File.separator + "love"));
		assertFalse(fileOperation.fileExists(guess + File.separator
				+ "welcome.txt"));
		assertFalse(fileOperation.fileExists(guess + File.separator
				+ "welcome2.txt"));
	}

	@Test
	public void deleteFolder() {
		String guess = TEST_FOLDER + File.separator + "guess";
		fileOperation.copyFolder(FILE_OPERATION_FIXTURE, guess);
		assertTrue(fileOperation.fileExists(guess + File.separator + "love"
				+ File.separator + "test.html"));
		fileOperation.deleteFolder(guess);
		assertFalse(fileOperation.fileExists(guess));
	}

	@Test
	public void writeFileWithAbsolutePath() {
		String path = BASE_FOLDER + File.separator + TEST_FOLDER
				+ File.separator + "ok.txt";
		fileOperationAbs.writeFile(path, "321");
		assertTrue(fileOperationAbs.fileExists(path));
		assertTrue(fileOperation.fileExists(TEST_FOLDER + File.separator
				+ "ok.txt"));
	}

	@BeforeClass
	public static void init() {
		fileOperation = new FileOperation(BASE_FOLDER);
		fileOperationAbs = new FileOperation();
	}

	@Before
	public void setUp() {
		fileOperation.mkDir(TEST_FOLDER);
	}

	@After
	public void tearDown() {
		fileOperation.deleteFolder(TEST_FOLDER);
	}

	@AfterClass
	public static void destroy() {

	}

}
