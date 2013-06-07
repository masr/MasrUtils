package in.masr.masrutils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileOperation {
	private String baseFolder = "";
	private String sep = "";

	public FileOperation(String baseFolder) {
		this.baseFolder = baseFolder;
		this.sep = File.separator;
	}

	public FileOperation() {
	}

	public void setBaseFolderPath(String path) {
		baseFolder = path;
	}

	public boolean fileExists(String path) {
		path = baseFolder + sep + path;
		return fileExistsHelper(path);
	}

	private boolean fileExistsHelper(String path) {
		File file = new File(path);
		return file.exists();
	}

	public String getContent(String path) {
		return getContentHelper(baseFolder + sep + path);
	}

	private String getContentHelper(String path) {
		try {
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			String all = "";
			while ((line = reader.readLine()) != null) {
				all += line + "\n";
			}

			reader.close();
			return all;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void writeFile(String path, String content) {
		path = baseFolder + sep + path;
		writeFileHelper(path, content);
	}

	private void writeFileHelper(String path, String content) {
		try {
			PrintWriter writer = new PrintWriter(new File(path));
			writer.print(content);
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void copyFile(String oldPath, String newPath) {

		copyFileHelper(baseFolder + sep + oldPath, baseFolder + sep + newPath);
	}

	private void copyFileHelper(String srcFilePath, String newFilePath) {
		try {
			BufferedInputStream bin = new BufferedInputStream(
					new FileInputStream(srcFilePath));
			BufferedOutputStream bout = new BufferedOutputStream(
					new FileOutputStream(new File(newFilePath)));
			int b = 0;
			while ((b = bin.read()) != -1) {
				bout.write(b);
			}
			bout.flush();
			bin.close();
			bout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteFile(String fileName) {
		String filePath = baseFolder + sep + fileName;
		deleteFileHelper(filePath);
	}

	private void deleteFileHelper(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public void mkDir(String path) {
		path = baseFolder + sep + path;
		mkDirHelper(path);

	}

	private void mkDirHelper(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public void copyFolder(String oldPath, String newPath) {
		copyFolderHelper(baseFolder + sep + oldPath, baseFolder + sep + newPath);
	}

	private void copyFolderHelper(String oldPath, String newPath) {
		deleteFolderHelper(newPath);
		mkDirHelper(newPath);
		File a = new File(oldPath);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (oldPath.endsWith(sep)) {
				temp = new File(oldPath + file[i]);
			} else {
				temp = new File(oldPath + sep + file[i]);
			}

			if (temp.isFile()) {
				copyFileHelper(temp.getAbsolutePath(),
						newPath + sep + (temp.getName()).toString());
			}
			if (temp.isDirectory()) {
				copyFolderHelper(oldPath + sep + file[i], newPath + sep
						+ file[i]);
			}
		}

	}

	public void emptyFolder(String path) {
		path = baseFolder + sep + path;
		emptyFolderHelper(path);
	}

	private void emptyFolderHelper(String path) {
		File file = new File(path);
		if (file.exists()) {
			deleteFolderHelper(path);
			mkDirHelper(path);
		}
	}

	public void deleteFolder(String path) {
		deleteFolderHelper(baseFolder + sep + path);
	}

	public void deleteFolderHelper(String sPath) {
		if (!sPath.endsWith(sep)) {
			sPath = sPath + sep;
		}
		File dirFile = new File(sPath);
		if (!dirFile.exists()) {
			return;
		}
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				deleteFileHelper(files[i].getAbsolutePath());
			} else {
				deleteFolderHelper(files[i].getAbsolutePath());
			}
		}
		dirFile.delete();
	}

}
