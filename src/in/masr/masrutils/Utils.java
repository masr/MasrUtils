package in.masr.masrutils;

public class Utils {
	public static String getSimpleEntryClassName() {
		Class entryClass = getEntryClass();
		return entryClass.getSimpleName();
	}

	public static Class getEntryClass() {
		StackTraceElement[] traces = Thread.getAllStackTraces().get(
				Thread.currentThread());
		StackTraceElement ele = traces[traces.length - 1];
		String entryClassName = ele.getClassName();
		Class entryClass;
		try {
			entryClass = Class.forName(entryClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return entryClass;
	}

}
