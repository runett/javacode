package no.tillung.file;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.apache.commons.io.FileUtils;
import no.tillung.utils.Log;
import no.tillung.utils.StrUtils;

public class Synchronizer {
	
	private Log log = new Log();
	private String masterDir = null;
	private String backupDir = null;
	private boolean copyNew = false;
	private boolean deleteRemoved = false;
	private Integer[] stat = {0, 0, 0, 0};
	/**
	 * Copying files from master to backup (also removing from backup if deleted from master) 
	 * @param masterDir
	 * @param backupDir
	 * @param copyNew		true: is copying from master to backup.  false: just logging
	 * @param deleteRemoved true: is removing files from backup which are removed from master.  false: just logging
	 */
	public Synchronizer(String masterDir, String backupDir, boolean copyNew, boolean deleteRemoved)
	{
		this.masterDir = masterDir;
		this.backupDir = backupDir;
		this.copyNew = copyNew;
		this.deleteRemoved = deleteRemoved;
	}
	
	public Synchronizer(String[] args) {
		for (int c=0; c<args.length; c++)
		{
			String arg = args[c];
			
			if ("-master".equalsIgnoreCase(arg))
			{
				this.masterDir = args[c+1];
				if (log == null)
					this.log = new Log(no.tillung.utils.FileUtils.checkFileStr(this.masterDir) + "log.txt");
				c++;
			}

			if ("-backup".equalsIgnoreCase(arg))
			{
				this.backupDir = args[c+1];
				c++;
			}
			
			if ("-delete".equalsIgnoreCase(arg))
				this.deleteRemoved = true;
			if ("-copy".equalsIgnoreCase(arg))
				this.copyNew = true;
			if ("-log".equalsIgnoreCase(arg))
				this.log = new Log(args[c+1]);
		}
	}

	/**
	 * Test metode
	 * @param args
	 */
	public static void main(String[] args) {
		Synchronizer sync = null;
		if (args.length == 0)
		{
			/*
			sync = new Synchronizer("c:/temp/synchronizer/master", "c:/temp/synchronizer/backup");
			sync.log = new Log("c:/temp/synchronizer/master/log.txt");
			sync.copyNew = true;
			sync.deleteRemoved = true;
			*/
			Log log = new Log("./synclog.txt");
			log.trace(  "-master <path>          (master katalog. Hvor det kopieres fra)\n" +
						"-backup <path>          (katalog det kopieres til)\n" +
						"-copy                   (angir om nye filer på master skal kopieres til backup, ellers bare logg)\n" +
						"-delete                 (angir om slettede filer (master) skal fjernes fra backup, hvis ikke bare logg)\n" +
						"-log    <path/filename> (loggfil)");
		}
		else
		{
			sync = new Synchronizer(args);
			sync.sync();
		}
    }
	/**
	 * Do the synchronization/logging
	 */
    public void sync() {

    	// Sjekk at master katalog finnes
    	File mDir = new File(this.masterDir);
		if (!mDir.exists())
		{
			log.trace("Katalogen/filen " + this.masterDir + " finnes ikke");
			return;
		}

    	// Sjekk at kopi katalog finnes dersom vi kopierer en hel katalog. (Dersom vi kopi en enkelt fil sjekker vi ikke selvsagt)
		if (mDir.isDirectory())
		{
	    	File cDir = new File(this.backupDir);
			if (!cDir.exists())
			{
				log.trace("Backupkatalogen " + this.backupDir + " finnes ikke");
				return;
			}
		}
		
    	log.buffer(500); // Only write batches of 500 lines to file
        log.trace("Scanner master: " + mDir.getPath());
        
        reset();
        this.scanMaster(mDir);
        
        log.trace("Filer kopiert:       " + stat[0]);
        log.trace("Filer slettet:       " + stat[1]);
        log.trace("Kataloger opprettet: " + stat[2]);
        log.trace("Kataloger slettet:   " + stat[3]);
        log.flush(); // Make sure everything is written to file
	}
    
	private void reset() {
		stat[0] = 0;
		stat[1] = 0;
		stat[2] = 0;
		stat[3] = 0;
	}

	/**
     * Copy files from a directory
     * @param dir
     */
	private void scanMaster(File dir) {
		//log.trace("scan " + dir.getPath());
		String mPath = dir.getPath();
		File[] filelist = dir.listFiles();
		
		Vector<String> mDirs = new Vector<String>();
		Vector<String> mFiles = new Vector<String>();
		
		this.populateFilelists(filelist, mDirs, mFiles);
		
		// Check backup
		String bPath = this.getBackupfilename(dir.getPath());
		
		File bDir = new File(bPath);
		File[] bFilelist = bDir.listFiles();
		
		Vector<String> bDirs = new Vector<String>();
		Vector<String> bFiles = new Vector<String>();
		
		this.populateFilelists(bFilelist, bDirs, bFiles);

		// Loop and copy files...
		Vector<String> missingBackupFiles = this.getMissing(mFiles, bFiles);
		for (int c=0; c<missingBackupFiles.size(); c++)
		{
			String fName = missingBackupFiles.get(c);
			
			String from = no.tillung.utils.FileUtils.checkFileStr(mPath) + fName;
			String to = no.tillung.utils.FileUtils.checkFileStr(bPath) + fName;

			if (this.copyNew)
			{
				log.trace("Kopierer fil: " + from + " -> " + to);
				if (this.copyFile(from, to))
					stat[0]++;
				else
					log.trace("ERROR! Kunne ikke kopiere: " + from);
			}
			else
			{
				log.trace("Ny fil: " + to);
			}
		}
		
		// Loop and remove files...
		Vector<String> deletedMasterFiles = this.getMissing(bFiles, mFiles);
		for (int c=0; c<deletedMasterFiles.size(); c++)
		{
			String fName = deletedMasterFiles.get(c);
			String pathFile = no.tillung.utils.FileUtils.checkFileStr(bPath) + fName;
			
			if (this.deleteRemoved)
			{
				log.trace("Sletter fil: " + pathFile);
				if (this.remove(pathFile))
					stat[1]++;
				else
					log.trace("ERROR! Kunne ikke slette: " + pathFile);
			}
			else
			{
				log.trace("Slettet fil: " + pathFile);
			}
		}

		// Loop and copy directories...
		Vector<String> missingBackupDirectories = this.getMissing(mDirs, bDirs);
		for (int c=0; c<missingBackupDirectories.size(); c++)
		{
			String dName = missingBackupDirectories.get(c);
			
			//String from = mPath + dName;
			String to = no.tillung.utils.FileUtils.checkFileStr(bPath) + dName;
			bDir = new File(to);
			if (!bDir.exists())
			{
				if (this.copyNew)
				{
					log.trace("Oppretter katalog: " + to);
					try {
						bDir.mkdir();
						stat[2]++;
					} catch (Exception e) {
						log.trace("ERROR! Kunne ikke opprette katalog: " + to);
					}
				}
				else
				{
					log.trace("Ny katalog: " + to);
				}
			}
		}
		
		// Loop and scan subdirectories
		for (int c=0; c<filelist.length; c++)
		{
			File f = filelist[c];
			if (f.isDirectory())
				this.scanMaster(f);
		}
		
		// Loop and remove directories...
		Vector<String> deletedMasterDirs = this.getMissing(bDirs, mDirs);
		for (int c=0; c<deletedMasterDirs.size(); c++)
		{
			String dName = deletedMasterDirs.get(c);
			String pathFile = no.tillung.utils.FileUtils.checkFileStr(bPath) + dName;
			
			if (this.deleteRemoved)
			{
				log.trace("Sletter katalog: " + pathFile);
				if (this.remove(pathFile))
					stat[3]++;
				else
					log.trace("ERROR! Kunne ikke slette: " + pathFile);
			}
			else
			{
				log.trace("Slettet katalog: " + pathFile);
			}
		}
    }

    
    /**
     * Returns names from files1 that is missing in files2
     * @param files1
     * @param files2
     * @return
     */
    private Vector<String> getMissing(Vector<String> files1, Vector<String> files2) {
		Vector<String> ret = new Vector<String>();

		for (int c=0; c<files1.size(); c++)
    	{
    		String f = files1.get(c);
    		if (!files2.contains(f))
				ret .add(f);
    	}
		
		return ret;
	}

    /**
     * Create lists of directorynames and filenames out of filelist
     * @param filelist
     * @param dirs
     * @param files
     */
	private void populateFilelists(File[] filelist, Vector<String> dirs, Vector<String> files)
    {
		if (filelist != null)
		{
			for (int c=0; c<filelist.length; c++)
			{
				File f = filelist[c];
				String fName = f.getName();
				
				if (f.isDirectory())
					dirs.add(fName);
				else if (f.isFile())
					files.add(fName);
			}
		}
	}


	
	/**
	 * Kopierer fra masterPathFile til backupPathFile
	 * Oppretter kataloger dersom de ikke finnes.
	 * Oppretter ikke tomme kataloger.
	 * 
	 * @param masterPathFile
	 * @param backupPathFile
	 */
	private boolean copyFile(String masterPathFile, String backupPathFile) {

		File from = null;
		try {
	        from = new File(masterPathFile);
	        if ((from != null) && from.isFile())
	        {
	        	File to = new File(backupPathFile);
	        	if (to != null)
	        	{
		        	try {
		        		FileUtils.copyFile(from, to, true);
		        		return true;
		        	}
		        	catch (Exception e) {
		        		return false;
		        	}
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Sletter katalog med alle filer/underkataloger.
	 * @param dir
	 * @return true: Slettet ok, false: Kunne ikke slette
	 */
	private boolean remove(String path)
	{
		File f = new File(path);
		
		if (f.isDirectory())
		{
			try {
				// TODO
				// Lage sikker sletting av katalog med alle underliggende kataloger og filer (scann gjennom alt og slett under kataloger/filer) først.
				// Feiler iblant ved sletting av store katalogstrukturer
				
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				return false;
			}
		}
		else if (f.isFile())
		{
			try {
				FileUtils.forceDelete(f);
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	private String getBackupfilename(String pathFile) {
		String ret = this.backupDir + StrUtils.right(pathFile, pathFile.length() - this.masterDir.length());
		return ret;
	}
}
