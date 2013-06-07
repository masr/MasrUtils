package in.masr.masrutils;

import in.masr.masrutils.exception.ConfigFileInitException;
import in.masr.masrutils.exception.PropertyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Will read config.ini and determine the home path of the program. This is very
 * useful. And it will also read the property of config.ini and store the value
 * in static attribute. This class will only have on instance in the runtime.
 */
public class Config {

	public static String HOME;

	private static Properties properties;
	private static Map<String, String> defaultMaps;
	private static Map<String, String> overrideMaps;
	private static final String DEFAULT_CONFIG_FILE_NAME = "config.ini";
	private static final String WHICH_CONFIG_FILE_NAME = "runtime_env";

	public static String s(String name) throws PropertyException {
		if (overrideMaps.containsKey(name)) {
			return overrideMaps.get(name);
		}
		String value = properties.getProperty(name);
		if (value != null) {
			return value;
		} else if (defaultMaps.containsKey(name)) {
			return defaultMaps.get(name);
		}
		System.err.println("Warning : " + name + " is not found in properties");
		throw new PropertyException();
	}

	public static int i(String name) throws PropertyException {
		if (overrideMaps.containsKey(name)) {
			return Integer.parseInt(overrideMaps.get(name));
		}
		String value = properties.getProperty(name);
		if (value != null) {
			return Integer.parseInt(value);
		} else if (defaultMaps.containsKey(name)) {
			return Integer.parseInt(defaultMaps.get(name));
		}
		System.err.println("Warning : " + name + " is not found in properties");
		throw new PropertyException();
	}

	static {
		try {
			init();
		} catch (ConfigFileInitException e) {
			e.printStackTrace();
		}
	}

	private static void init() throws ConfigFileInitException {
		defaultMaps = new HashMap<String, String>();
		overrideMaps = new HashMap<String, String>();
		properties = new Properties();

		String entryFilePath = entryFilePath();
		File file = new File(entryFilePath);
		if (file.isFile()) {
			file = file.getParentFile();
		}

		String configFilePath = findConfigFilePath(file);

		File configFile = new File(configFilePath);

		HOME = configFile.getParentFile().getAbsolutePath();
		try {
			properties.load(new FileReader(new File(configFilePath)));
		} catch (IOException e) {
			System.err.println("Can not read " + configFilePath);
			e.printStackTrace();
			throw new ConfigFileInitException();
		}
		setDefaultConf();

	}

	private static String entryFilePath() {
		Class entryClass = Utils.getEntryClass();
		if (entryClass.getName().equals(
				"org.eclipse.jdt.internal.junit.runner.RemoteTestRunner")) {
			entryClass = Config.class;
		}
		URL url = entryClass.getProtectionDomain().getCodeSource()
				.getLocation();
		String filePath = url.getPath();
		return filePath;
	}

	private static String findConfigFilePath(File file)
			throws ConfigFileInitException {
		boolean found = false;
		while (!found) {
			if (file == null) {
				System.err.println("Config file is not found!");
				break;
			}
			String configFileName = null;
			String whichConfigFilePath = file.getAbsolutePath()
					+ File.separator + WHICH_CONFIG_FILE_NAME;
			File whichConfigFile = new File(whichConfigFilePath);
			// default_config is found. We will look at default_config first
			// other than config.ini
			if (whichConfigFile.exists()) {
				try {
					configFileName = readFile(whichConfigFilePath).trim();
				} catch (IOException e) {
					e.printStackTrace();
					throw new ConfigFileInitException();
				}
				// default_config has content <configFileName> and it is not
				// empty
				if (!configFileName.isEmpty()) {
					String configFilePath = file.getAbsolutePath()
							+ File.separator + configFileName;
					File configFile = new File(configFilePath);
					// config file defined in default_config not exists, will
					// throw exception
					if (!configFile.exists()) {
						System.err.println("Config file " + configFileName
								+ " is not exists!");
						throw new ConfigFileInitException();
					} else {
						// config file defined in default_config exists, will
						// return defined config file path
						return configFilePath;
					}
				}
				// default_config is empty file, so config file name is
				// DEFAULT_CONFIG_NAME
				configFileName = DEFAULT_CONFIG_FILE_NAME;
				return file.getAbsolutePath() + File.separator + configFileName;
			} else {
				// default_config is not found and then we try to look for
				// config.ini. In one case, a directory only has comfig.ini but
				// has no default_config. Then config.ini is the one.
				String defaultConfigFilePath = file.getAbsolutePath()
						+ File.separator + DEFAULT_CONFIG_FILE_NAME;
				File defaultConfigFile = new File(defaultConfigFilePath);
				if (defaultConfigFile.exists()) { // only config.ini is found
					return defaultConfigFilePath;
				}
				file = file.getParentFile();
			}
		}
		throw new ConfigFileInitException();
	}

	public static void set(String name, String value) {
		synchronized (overrideMaps) {
			overrideMaps.remove(name);
			overrideMaps.put(name, value);
		}

	}

	private static String readFile(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				filePath)));
		String line = "";
		String all = "";
		while ((line = reader.readLine()) != null) {
			all += line + "\n";
		}
		reader.close();
		return all;
	}

	protected static void setDefaltValue(String name, String value) {
		synchronized (defaultMaps) {
			defaultMaps.put(name, value);
		}
	}

	private static void setDefaultConf() {
	}

}