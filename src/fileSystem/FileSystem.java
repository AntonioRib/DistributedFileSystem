package fileSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSystem {

	public static String[] dir(String path) throws InfoNotFoundException {
		File f = new File(path);
		if (f.exists())
			return f.list();
		else
			throw new InfoNotFoundException("Directory not found :" + path);
	}

	public static FileInfo getFileInfo(String path)
			throws InfoNotFoundException {
		File f = new File(path);
		if (f.exists())
			return new FileInfo(path, f.length(), new Date(f.lastModified()),
					f.isFile());
		else
			throw new InfoNotFoundException("File not found.");
	}

	public static boolean makeDir(String name) {
		File dir = new File(name);
		return dir.mkdir();
	}

	public static boolean removeFile(String path, boolean isFile) {
		File f = new File(path);

		if ((isFile && f.isFile()) || (!isFile && f.isDirectory()))
			return f.delete();

		return false;
	}

	public static byte[] getData(String fromPath) throws IOException {
		File f = new File(fromPath);

		if (f.isDirectory())
			return null;

		RandomAccessFile rf = new RandomAccessFile(f, "r");
		byte[] b = new byte[(int) f.length()];
		rf.readFully(b);
		rf.close();

		return b;
	}

	public static boolean createFile(String toPath, byte[] data)
			throws IOException {
		File toFile = new File(toPath);
		try {
			FileOutputStream dest = new FileOutputStream(toFile);
			dest.write(data);
			dest.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
