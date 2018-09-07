package no.tillung.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Diverse metoder og rutiner.
 * Konvertering, sortering, kryptering, datovalidering, tekstsplitting, osv.
 * 
 * @author Rune Tillung (rtil)
 *
 */


public class FileUtils {

	public enum Sort {ascending, descending};

	/**
	 * 
	 * @param dir: The directory
	 * @param filename: The filename without path and extension "winlink"
	 * @param cntfilename: The counter filename with extension "cntwinlink.txt"
	 * @return The next extension available
	 */
	public static String getExtention(String dir, String filename, String cntfilename)
	{
		String tryMe = "xxx";
		try {
			FileInputStream fin = null;
			String ext = null;
			//String cntfile = dir + "cnt" + filename + ".txt";
			String cntfile = dir + cntfilename;
			try {
			    fin = new FileInputStream (cntfile);
			    ext = new DataInputStream(fin).readLine();
			}
			catch (IOException e) {	}
			finally {
				if (fin != null)
					try {
						fin.close();
					} catch (IOException e) { e.printStackTrace(); }
			}
			if ((ext == null) || (ext.length() != 3))
			{
				ext = "000";
			}
			
		    boolean found = false;
		    String first = ext; // This is the first extention we try
		    tryMe = FileUtils.increase(ext);
		    
		    while (!found)
		    {
		    	String checkFilename = dir + filename + "." + tryMe;
		    	File f = new File(checkFilename);
		    	if (!f.exists())
		    		found = true;
		    	else
			    	tryMe = increase(tryMe);
		    	
		    	// Back to starting point.... use and overwrite
		    	 if (first.equals(tryMe))
		    		 found = true;
		    }
	
			BufferedWriter out = null;
			try {
				FileWriter fstream = new FileWriter(cntfile);
				out = new BufferedWriter(fstream);
				out.write(tryMe);					  
			}
			catch (Exception e) {
			}
			finally {
				try {
					if (out != null)
						out.close();
				} catch (IOException e) { e.printStackTrace(); }
			}
		}
		catch (Exception e)
		{
			
		}
		// Always return tryMe....
		return tryMe;
	}

	private static String increase(String ext) {
		try {
			if ((ext != null) && (ext.length() == 3))
			{
				int min = 48; //(new Integer('0')).intValue();
				int max = 90; //(new Integer('Z')).intValue();
				
				int byte0 = ext.charAt(0);
				int byte1 = ext.charAt(1);
				int byte2 = ext.charAt(2);
				
				byte2++;
				while ((byte2 >= 58) && (byte2 <= 64))
					byte2++;

				if (byte2 > max)
				{
					byte2 = min;
					byte1++;
					while ((byte1 >= 58) && (byte1 <= 64))
						byte1++;
				}
				if (byte1 > max)
				{
					byte1 = min;
					byte0++;
					while ((byte0 >= 58) && (byte0 <= 64))
						byte0++;
				}
				if (byte0 > max)
				{
					byte0 = min;
					byte1 = min;
					byte2 = min + 1;
				}
	
				String ret =	(new Character((char)byte0)).toString() +
								(new Character((char)byte1)).toString() +
								(new Character((char)byte2)).toString();
				return ret;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ext;
	}

	/**
	 * Convert filepath from format path\file to path/file
	 * Adds a "/" to the end of the string if there is no.
	 * @param dirFile
	 * @return
	 */
	public static String slashConvert(String dirFile) {
		if ((dirFile != null) && (dirFile.length() > 0))
			dirFile = StrUtils.replace(dirFile, "\\", "/");
		return dirFile;
	}

	
	/**
	 * Adds a "/" to the end of the string if there is no.
	 * @param dirFile
	 * @return
	 */
	public static String checkFileStr(String dirFile) {
		if ((dirFile != null) && (dirFile.length() > 0))
		{
			if (!"/".equals(StrUtils.right(dirFile, 1)) &&
				!"\\".equals(StrUtils.right(dirFile, 1)))
			dirFile += "/";
		}
		return dirFile;
	}
	public static String getDir(String str) {
		if (str != null)
		{
			int idx = StrUtils.lastIndexOf(str, '/');
			int idx2 = StrUtils.lastIndexOf(str, '\\');
			
			if (idx2 > idx)
				idx = idx2;
			if (idx > 0)
				return StrUtils.left(str, idx + 1); // Return path + '/' or '\\'
		}
		return null;
	}
	public static String getFilename(String str) {
		if (str != null)
		{
			int idx = StrUtils.lastIndexOf(str, '/');
			int idx2 = StrUtils.lastIndexOf(str, '\\');
			
			if (idx2 > idx)
				idx = idx2;
			if (idx > 0)
				return StrUtils.right(str, str.length() - (idx + 1)); // Return path + '/' or '\\'
		}
		return null;
	}
	public static String getDirname(String str) {
		String filename = FileUtils.getFilename(str);
		String dir = StrUtils.left(str, str.length() - filename.length());
		return dir;
	}
	
	public static String readFile(String filename) {
		String str = null;
		try {
			DataInputStream in = null;
			FileInputStream fstream = new FileInputStream(filename);
		  
			// Get the object of DataInputStream
			in = new DataInputStream(fstream);
			InputStreamReader sReader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(sReader);

			String strLine;
		  
			//Read File Line By Line
			str = "";
			while ((strLine = br.readLine()) != null)
				str += strLine;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * Sort files ascending on modified time
	 * @param files
	 */
	public static void sortFiles(File[] files) {
		int c = 0;
		boolean finished = false;
		while (!finished)
		{
			if (c < (files.length - 1))
			{
				finished = false;
				int nextC = (c + 1);
				
				File thisF = files[c];
				File nextF = files[nextC];
				
				if (thisF.lastModified() > nextF.lastModified())
				{
					File tmp = files[c];
					files[c] = files[nextC];
					files[nextC] = tmp;
					if (c > 0)
						c--;
				}
				else
				{
					c++;
				}
			}
			else
			{
				finished = true;
			}
		}
	}
}
