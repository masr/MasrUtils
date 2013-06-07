package com.masr.masrutils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import in.masr.masrutils.Config;
import in.masr.masrutils.FileOperation;
import in.masr.masrutils.exception.ConfigFileInitException;
import in.masr.masrutils.exception.PropertyException;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigTest {

	private static FileOperation fileOperation;

	@BeforeClass
	public static void init() {
		fileOperation = new FileOperation(
				"/home/maosuhan/workspace/java/MasrUtils");
	}

	@Before
	public void setUp() throws ClassNotFoundException {

	}

	@After
	public void tearDown() {
		fileOperation.deleteFile("config2.ini");
		fileOperation.deleteFile("config.ini");
		fileOperation.deleteFile("runtime_env");
	}

	// Nothing
	@Test(expected = ConfigFileInitException.class)
	public void nothing() throws Exception {
		initConfig();
	}

	// config.ini
	@Test
	public void onlyConfigIni() throws Exception {
		fileOperation.copyFile("test_fixture/config.ini", "config.ini");
		initConfig();
		testPropertiesOfConfigIni();
	}

	// runtime_env : config.ini
	@Test(expected = ConfigFileInitException.class)
	public void onlyRimetimeEnv() throws Exception {
		fileOperation.writeFile("runtime_env", "config.ini");
		initConfig();
	}

	// runtime_env : config.ini
	// config.ini
	@Test
	public void configIniAndRuntimeEnv() throws Exception {
		fileOperation.copyFile("test_fixture/config.ini", "config.ini");
		fileOperation.writeFile("runtime_env", "config.ini");
		initConfig();
		testPropertiesOfConfigIni();
	}

	// runtime_env : config2.ini
	// config.ini
	@Test(expected = ConfigFileInitException.class)
	public void incorrespondingConfigIniAndWrongRuntimeEnv() throws Exception {
		fileOperation.copyFile("test_fixture/config.ini", "config.ini");
		fileOperation.writeFile("runtime_env", "config2.ini");
		initConfig();
	}

	// runtime_env
	// config.ini
	@Test
	public void configIniAndEmptyRuntimeEnv() throws Exception {
		fileOperation.copyFile("test_fixture/config.ini", "config.ini");
		fileOperation.writeFile("runtime_env", "");
		initConfig();
		testPropertiesOfConfigIni();
	}

	// runtime_env
	@Test(expected = ConfigFileInitException.class)
	public void configIniNotExistAndEmptyRuntimeEnv() throws Exception {
		fileOperation.writeFile("runtime_env", "");
		initConfig();
		testPropertiesOfConfigIni();
	}

	// runtime_env : config2.ini
	// config2.ini
	@Test
	public void anotherConfigIniAndRuntimeEnv() throws Exception {
		fileOperation.copyFile("test_fixture/config2.ini", "config2.ini");
		fileOperation.writeFile("runtime_env", "config2.ini");
		initConfig();
		testPropertiesOfConfig2ini();
	}

	// runtime_env : config2.ini
	// config.ini
	// config2.ini
	@Test
	public void manyConfigRuntimeEnv() throws Exception {
		fileOperation.copyFile("test_fixture/config2.ini", "config2.ini");
		fileOperation.writeFile("runtime_env", "config2.ini");
		initConfig();
		testPropertiesOfConfig2ini();
	}

	@Test
	public void setDefaultConfInCFG() throws Exception {
		fileOperation.copyFile("test_fixture/config.ini", "config.ini");
		fileOperation.writeFile("runtime_env", "config.ini");
		initConfig();
		CFG.init();
		testPropertiesOfCFG();
	}

	private void testPropertiesOfConfigIni() throws PropertyException {
		assertEquals("localhost", Config.s("host"));
		testPropertiesHelper();
	}

	private void testPropertiesOfConfig2ini() throws PropertyException {
		assertEquals("example.com", Config.s("host"));
		testPropertiesHelper();
	}

	private void testPropertiesHelper() throws PropertyException {
		assertEquals("masr", Config.s("user"));
		assertEquals("pass", Config.s("passwd"));
		assertEquals(10, Config.i("limit"));
		boolean hasException = false;
		try {
			Config.s("database");
		} catch (PropertyException ex) {
			hasException = true;
		}
		assertTrue(hasException);
		Config.set("user", "tiger");
		assertEquals("tiger", Config.s("user"));
	}

	private void testPropertiesOfCFG() throws PropertyException {
		assertEquals("localhost", CFG.s("host"));
		assertEquals("masr", CFG.s("user"));
		assertEquals("pass", CFG.s("passwd"));
		assertEquals(10, CFG.i("limit"));
		assertEquals(3306, CFG.i("port"));
		boolean hasException = false;
		try {
			CFG.s("database");
		} catch (PropertyException ex) {
			hasException = true;
		}
		assertTrue(hasException);
		CFG.set("user", "tiger");
		assertEquals("tiger", CFG.s("user"));
	}

	private void initConfig() throws Exception {
		Method method = Config.class.getDeclaredMethod("init", new Class[] {});
		method.setAccessible(true);
		try {
			method.invoke(null);
		} catch (Exception ex) {
			throw new ConfigFileInitException();
		}
	}

	private static class CFG extends Config {

		public static void init() {
			setDefaltValue("port", "3306");
		}
	}
}
