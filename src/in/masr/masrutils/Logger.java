package in.masr.masrutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public static PrintWriter out;
	public static PrintWriter err;
	private static boolean canWrite = true;
	private static String logFolderPath = null;
	private static String preName = null;
	private static String outFilePath = null;;
	private static String errFilePath = null;
	private static PrintStream stdERR = null;
	private static PrintStream stdOUT = null;

	static {
		logFolderPath = Config.HOME + File.separator + "log";
		stdERR = System.err;
		stdOUT = System.out;
		preName = Utils.getSimpleEntryClassName();
	}

	// main purpose is to set to un-init state, so when Logger.out after clean,
	// will init again
	public static void clean() {
		canWrite = true;
		out = null;
		err = null;
		outFilePath = null;
		errFilePath = null;
		logFolderPath = Config.HOME + File.separator + "log";
		System.setErr(stdERR);
		System.setOut(stdOUT);
		preName = Utils.getSimpleEntryClassName();
	}

	public static void init() {
		Date date = new Date();
		int hour = date.getHours();
		int minute = date.getMinutes();
		int seconds = date.getSeconds();
		int day = date.getDate();
		int month = date.getMonth() + 1;
		int year = date.getYear() - 100;

		outFilePath = logFolderPath + File.separator + preName + "_" + year
				+ "-" + month + "-" + day + "_" + hour + ":" + minute + ":"
				+ seconds + ".log";
		errFilePath = logFolderPath + File.separator + preName + "_" + year
				+ "-" + month + "-" + day + "_" + hour + ":" + minute + ":"
				+ seconds + ".err";
		try {
			out = new PrintWriter(new File(outFilePath));
			err = new PrintWriter(new File(errFilePath));
			captureSDERR();
			captureSDOUT();
		} catch (FileNotFoundException e) {
			System.err.println("Logger init error.");
			e.printStackTrace();
		}
	}

	public static void setPrefixName(String name) {
		preName = name;
		init();
	}

	public static void setLogFolderAbsolutePath(String path) {
		logFolderPath = path;
		init();
	}

	private static boolean hasInit() {
		return err != null && out != null;
	}

	public static void out(String info) {
		if (!hasInit()) {
			init();
		}
		if (canWrite == false)
			return;

		echoHelper(out, info);
	}

	private static void echoHelper(PrintWriter writer, String str) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dstr = df.format(date);
		synchronized (writer) {
			writer.println(str);
			writer.println("**********************************" + dstr);
			writer.flush();
		}

		if (writer == out) {
			stdOUT.println(str);
		}
		if (writer == err) {
			stdERR.println(str);
		}
	}

	public static void err(String info) {
		if (!hasInit()) {
			init();
		}
		echoHelper(err, info);

	}

	public static void mute() {
		if (!hasInit()) {
			init();
		}
		synchronized (out) {
			canWrite = false;
		}

	}

	public static void sound() {
		if (!hasInit()) {
			init();
		}
		synchronized (out) {
			canWrite = true;
		}

	}

	private static void captureSDOUT() {
		try {
			PrintStream ps = new LoggerOutPrintStream(new File(outFilePath));
			System.setOut(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void captureSDERR() {
		try {
			PrintStream ps = new LoggerErrPrintStream(new File(errFilePath));
			System.setErr(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getOutFilePath() {
		return outFilePath;
	}

	public static String getErrFilePath() {
		return errFilePath;
	}

	private static class LoggerOutPrintStream extends PrintStream {

		public LoggerOutPrintStream(File file) throws FileNotFoundException {
			super(file);
		}

		@Override
		public void println(String str) {
			out(str);
		}
	}

	private static class LoggerErrPrintStream extends PrintStream {

		public LoggerErrPrintStream(File file) throws FileNotFoundException {
			super(file);
		}

		@Override
		public void println(String str) {
			err(str);
		}
	}
}
