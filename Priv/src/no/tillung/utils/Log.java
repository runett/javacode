package no.tillung.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

public class Log {
	String filename = null;
	Vector<String> buffer = null;
	private int buffersize = 0;
	private boolean logDateTime = false;
	
	public Log(String filename)
	{
		this.filename = filename;
	}
	
	public Log() {
	}

	/**
	 * Set buffer with size. Must be flushed with flush(). Auto flush after <size> traces.	 *
	 * If size = 0, dont use buffer...
	 *  
	 * @param size
	 */
	public void buffer(int size)
	{
		buffersize  = size;
		if (size > 0)
			buffer = new Vector<String>(buffersize);
		else
			buffer = null;
	}
	
	/**
	 * Write buffer to file and remove content of buffer
	 */
	public void flush() {
		this.write(this.buffer);
		this.buffer.clear();
	}

	/**
	 * Log a line. To buffer (must be flushed later) or directly to file...
	 * @param o
	 */
	public void trace(Object o)
	{
		String ts = "";
		if (this.logDateTime)
			ts += new Date() + ": ";
		try {
			if (filename != null)
			{
				if (buffer != null)
				{
					if (buffer.size() >= buffersize)
						this.flush();
					buffer.add(o.toString());
				}
				else
				{
					this.write(ts + o.toString());
				}
			}
			System.out.println(ts + o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write o to file. Open, write, close...
	 * @param o
	 */
	private void write(Object o) {
		
		String clsname = o.getClass().getSimpleName();
		FileWriter fw;
		try {
			fw = new FileWriter(this.filename, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			if ("String".equalsIgnoreCase(clsname))
			{
				bw.write(o.toString() + "\n");
			}
			else if ("Vector".equalsIgnoreCase(clsname))
			{
				Vector<?> v = (Vector<?>)o;
				for (int c=0; c<v.size(); c++)
					bw.write(v.get(c).toString() + "\n");
			}
			
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logDateTime(boolean b) {
		this.logDateTime  = b;
	}

}
