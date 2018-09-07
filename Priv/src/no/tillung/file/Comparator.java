package no.tillung.file;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.io.FileUtils;
import no.tillung.utils.Log;

public class Comparator {
	
	private Log log = new Log();
	private String baseDir = null;
    private Hashtable<Long, Vector<String>> files = new Hashtable<Long, Vector<String>>();

	/**
	 * 
	 * @param masterDir
	 */
	public Comparator(String baseDir)
	{
		this.baseDir = baseDir;
	}
	
	public Comparator(String[] args) {
		for (int c=0; c<args.length; c++)
		{
			String arg = args[c];
			
			if ("-dir".equalsIgnoreCase(arg))
			{
				this.baseDir = args[c+1];
				if (log == null)
					this.log = new Log(no.tillung.utils.FileUtils.checkFileStr(this.baseDir) + "log.txt");
				c++;
			}

			if ("-log".equalsIgnoreCase(arg))
				this.log = new Log(args[c+1]);
		}
	}

	/**
	 * Test metode
	 * @param args
	 */
	public static void main(String[] args) {
		Comparator comparator = null;
		if (args.length == 0)
		{
			Log log = new Log("./synclog.txt");
			log.trace(  "-dir <path>          (katalognavn. Alle filer på denne katalogen vil bli sjekket)\n" +
						"-log <path/filename> (loggfil)");
		}
		else
		{
			comparator = new Comparator(args);
			comparator.compare();
		}
    }
	/**
	 * Do the synchronization/logging
	 */
    public void compare() {

    	// Sjekk at master katalog finnes
    	File baseDir = new File(this.baseDir);
		if (!baseDir.exists())
		{
			log.trace("Katalogen/filen " + this.baseDir + " finnes ikke");
			return;
		}

    	log.buffer(500); // Only write batches of 500 lines to file
        log.trace("Comparator dir: " + baseDir.getPath());
        
        this.scanBase(baseDir);
        this.checkResult();
        
        log.flush(); // Make sure everything is written to file
	}
    

	private void checkResult() {
		boolean found = false;
		Enumeration<Long> sizes = this.files.keys();
		while (sizes.hasMoreElements())
		{
			Long size = sizes.nextElement();
			Vector<String> sizeFiles = this.files.get(size);
			
			//log.trace("Str " + size + " bytes. " + sizeFiles.size() + " filer");
			
			if (sizeFiles.size() > 1)
			{
				int pos1 = 0;
				int pos2 = 1;

				File f1 = new File(sizeFiles.get(pos1));

				do {
					//log.trace(pos1 + "/" + pos2);
					File f2 = new File(sizeFiles.get(pos2));
					
					if (Comparator.compareContent(f1, f2))
					{
						log.trace("Filene " + f1.getPath() + " " + f2.getPath() + " er like");
						found = true;
					}
					
					pos2++;
					if (pos2 == sizeFiles.size())
					{
						pos1++;
						pos2 = (pos1 + 1);
						
						f1 = new File(sizeFiles.get(pos1));
					}
					
				} while (pos1 < (sizeFiles.size() - 1));
			}
		}
		if (!found)
			log.trace("Ingen like filer funnet...");
	}

	/**
	 * 
	 * @param dir
	 */
	private void scanBase(File dir) {
		//log.trace("scan " + dir.getPath());
		File[] filelist = dir.listFiles();
		
		// Loop files
		for (int c=0; c<filelist.length; c++)
		{
			File f = filelist[c];
			if (f.isFile())
				this.addFile(f);
			else if (f.isDirectory())
				scanBase(f);
		}
    }

    private void addFile(File f) {

    	if (f.isFile())
    	{
	    	Long fSize = FileUtils.sizeOf(f);
	    	
	    	if (!files.containsKey(fSize))
	    		files.put(fSize, new Vector<String>());
	    	Vector<String> elem = files.get(fSize);
	    	elem.add(f.getPath());
    	}
	}

	/**
	 * Compare two files for same size and content
	 * @param f1
	 * @param f2
	 * @return true: same size/content  false: not same size/content
	 */
	public static boolean compareContent(File f1, File f2)
	{
		//Long f1Size = FileUtils.sizeOf(f1);
		//Long f2Size = FileUtils.sizeOf(f2);
		
		// If same size, check content...
		//if (f1Size.equals(f2Size))
		//{
			try {
				if (FileUtils.contentEquals(f1, f2))
					return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		//}
		return false;
	}
	
}
