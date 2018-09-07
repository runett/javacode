package no.tillung.utils;


import java.util.*;

public class StrUtils {
	private static Hashtable<Integer, String> sevStrMapping = null;

	public static boolean similar(Object a, Object b)
	{
		if ((a == null) && (b == null))
		{
			return true;
		}
		if (((a == null) && (b != null)) ||
			((a != null) && (b == null)))
		{
			return false;
		}
		else
		{
			if (a.equals(b))
				return true;
			else
				return false;
		}
	}
	public static String[] split(String str, String splitChar)
	{
		return split(str, splitChar, false);
	}

	/**
	 * Split a string into an array of substrings.
	 * @param str: the string
	 * @param splitChar: split character or split string
	 * @param keepSplitChar: if you should keep the splitcharacters as a subelement
	 * @return
	 */
	public static String[] split(String str, String splitChar, boolean keepSplitChar)
	{
		String[] retArr = null;
		try {
			int crAt;
			String strPart;
			List<String> list = new ArrayList();
			
			while (str != null)
			{
				crAt = str.indexOf(splitChar);
				if (crAt >= 0)
				{
					strPart = str.substring(0, crAt);
					str = str.substring(crAt + splitChar.length());

					list.add(strPart);
					if (keepSplitChar)
						list.add(splitChar);
				}
				else
				{
					strPart = str;
					str = null;
					
					list.add(strPart);
				}
				
			}
			retArr = new String[list.size()];
			for (int c=0; c<list.size(); c++)
			{
				retArr[c] = list.get(c);
			}
		}
		catch (Exception e)
		{
			System.out.println("StrUtils.split(...) Error!\n" + e);
		}
		return retArr;
	}
	
	/**
	 * Checks if a position in a string contains the char '1'.
	 * 
	 * @param str String containing '0' or '1'.  String can be null, false will be returned
	 * @param idx The position to check
	 * @return true if the character in position <idx> is '1'. Otherwise false.
	 */
	public static boolean index(String str, int idx)
	{
		if ((str != null) && (str.length() >= (idx + 1)))
		{			
			if ('1' == str.charAt(idx))
				return true;
		}
		return false;
	}
	/**
	 * Get the index of the first occurence of "1"
	 * @param str
	 * @return
	 */
	public static Long index(String str)
	{
		if (str != null)
		{			
			Integer idx = str.indexOf("1");
			if (idx >= 0)
				return idx.longValue();
		}
		return null;
	}
	
	public static int IndexOf(String str, String substr) {
		int idx = -1;
		try {
			if (str != null)
				idx = str.indexOf(substr);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return idx;
	}
	
	/* SEV Checksum routine
	 * 
	 */
	public static int checksum(String str)
	{
		int chsum = 0;
		
		for (int c=0; c<str.length(); c++)
		{
			// Feil??? c byttes med ch?
			char ch = str.charAt(c);
			if (c == 'Å')
				ch = new Character((char) 93);
			if (c == 'Æ')
				ch = new Character((char) 91);
			if (c == 'Ø')
				ch = new Character((char) 92);
			if (c == 'å')
				ch = new Character((char) 125);
			if (c == 'æ')
				ch = new Character((char) 123);
			if (c == 'ø')
				ch = new Character((char) 124);
			if (ch < 0)
				ch = '!';
						
			chsum += ch;
			chsum &= 0xff;
		}
		return chsum;
	}
	
	public static String padLeft(String str, int len, String pad)
	{
		String ret = str;
		if (ret == null)
			ret = new String();

		while (ret.length() < len)
			ret = pad + ret;
		//System.out.println("TEST padleft: '" + str + "' -> '" + ret + "'");
		return ret;
	}
	public static String padRight(String str, int len, String pad) {
		String ret = str;
		if (ret == null)
			ret = new String();
		
		while (ret.length() < len)
			ret += pad;
		//System.out.println("TEST padright: '" + str + "' -> '" + ret + "'");
		return ret;
	}
	@Deprecated 
	public static String stipLeadingZero(String str) {
		return stripLeadingZero(str);
	}
	public static String stripLeadingZero(String str) {
		while ((str != null) && (str.length() > 1) && (str.charAt(0) == '0'))
			str = str.substring(1);
		return str;
	}	
	public static boolean lessThan(String a, String b) {
		if ((a == null) && (b == null))
			return false;
		else if ((a == null) && (b != null))
			return true;
		else if ((a != null) && (b == null))
			return false;
		else if (a.compareTo(b) < 0)
			return true;
		else
			return false;
	}
	/**
	 * Returns a new string containing the <count> leftmost characters of the original string
	 * 
	 * @param str
	 * @param count
	 * @return
	 */
	public static String left(String str, int count) {
		try {
			if (str != null)
			{
				if (str.length() >= count)
					return str.substring(0, count);
				else
					return str;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns a new string containing the <count> rightmost characters of the original string
	 * 
	 * @param str
	 * @param count
	 * @return
	 */
	public static String right(String str, int count) {
		try {
			if ((str != null) && (count >= 0))
			{
				if (count >= str.length())
					return str;
				else
					return str.substring(str.length() - count, str.length());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static Integer bitsToInteger(String bitStr) {
		int val = 0;
		if (bitStr != null)
		{
			for (int c=0; c<bitStr.length(); c++)
			{
				Character bit = bitStr.charAt(c);

				if (!(bit.equals('0') || bit.equals('1')))
					return null;

				if (bit.equals('1'))
				{
					int raisedVal = (bitStr.length() - 1) - c;
					int bitVal = (int) (Math.pow(2.0, raisedVal));
					
					val += bitVal;
				}
				else if (bit.equals('0'))
				{
				}
			}

			return val;
		}

		return null;
	}
	public static String stripTrailing(String str, char ch) {
		while ((str != null) && (str.length() > 1) && (str.charAt(str.length() - 1) == ch))
			str = str.substring(0, str.length() - 1);
		return str;
	}

		
	/* SEV CHECKSUM routine c++
	char CTermThread::GetChSum(char *Str, char *Bfr)
	{
		int chsum = 0;
		char Buffer[512];
		unsigned char Res;
		
		int i(0);
		for (; i < (int)strlen(Str); i++)
		{
			switch(Str[i])
			{
				case 'Å' :
					Str[i] = SEV_BIG_AA;
					break;
				case 'Ä' :
					Str[i] = SEV_BIG_AE;
					break;
				case 'Ö' :
					Str[i] = SEV_BIG_OE;
					break;
				case 'å' :
					Str[i] = SEV_LITTLE_AA;
					break;
				case 'ä' :
					Str[i] = SEV_LITTLE_AE;
					break;
				case 'ö' :
					Str[i] = SEV_LITTLE_OE;
					break;
			};
			
			if (Str[i] < 0) Str[i] =_T('!');
			chsum += Str[i];
			chsum &= 0xff;
		}        
		Res = chsum;
		sprintf(Buffer,"%c%s%02X%c", 10, Str, chsum, 13);

		strcpy(Bfr, Buffer);
		
		return(chsum);
	}
	*/
	/**
	 * Convert all characters to a-z, A-Z
	 * NB! Can return a string longer than the input string because of ø->oe conversation.
	 * @param str
	 * @return
	 */
	public static String sevStringConvert(String str)
	{
		try {
		if (sevStrMapping == null)
		{
			sevStrMapping = new Hashtable<Integer, String>();
			sevStrMapping.put(new Integer('æ'), "ae");
			sevStrMapping.put(new Integer('ø'), "oe");
			sevStrMapping.put(new Integer('å'), "aa");
			sevStrMapping.put(new Integer('Æ'), "AE");
			sevStrMapping.put(new Integer('Ø'), "OE");
			sevStrMapping.put(new Integer('Å'), "AA");
			sevStrMapping.put(192, "A");
			sevStrMapping.put(193, "A");
			sevStrMapping.put(194, "A");
			sevStrMapping.put(195, "A");
			sevStrMapping.put(196, "A");
			sevStrMapping.put(199, "C");
			sevStrMapping.put(200, "E");
			sevStrMapping.put(201, "E");
			sevStrMapping.put(202, "E");
			sevStrMapping.put(203, "E");
			sevStrMapping.put(204, "I");
			sevStrMapping.put(205, "I");
			sevStrMapping.put(206, "I");
			sevStrMapping.put(207, "I");
			sevStrMapping.put(209, "N");
			sevStrMapping.put(210, "O");
			sevStrMapping.put(211, "O");
			sevStrMapping.put(212, "O");
			sevStrMapping.put(213, "O");
			sevStrMapping.put(214, "O");
			sevStrMapping.put(217, "U");
			sevStrMapping.put(218, "U");
			sevStrMapping.put(219, "U");
			sevStrMapping.put(220, "U");
			sevStrMapping.put(221, "Y");

			sevStrMapping.put(224, "a");
			sevStrMapping.put(225, "a");
			sevStrMapping.put(226, "a");
			sevStrMapping.put(227, "a");
			sevStrMapping.put(228, "a");
			sevStrMapping.put(231, "c");
			sevStrMapping.put(232, "e");
			sevStrMapping.put(233, "e");
			sevStrMapping.put(234, "e");
			sevStrMapping.put(235, "e");
			sevStrMapping.put(236, "i");
			sevStrMapping.put(237, "i");
			sevStrMapping.put(238, "i");
			sevStrMapping.put(239, "i");
			sevStrMapping.put(241, "n");
			sevStrMapping.put(242, "o");
			sevStrMapping.put(243, "o");
			sevStrMapping.put(244, "o");
			sevStrMapping.put(245, "o");
			sevStrMapping.put(246, "oe");
			sevStrMapping.put(249, "u");
			sevStrMapping.put(250, "u");
			sevStrMapping.put(251, "u");
			sevStrMapping.put(252, "u");
			sevStrMapping.put(253, "y");
			sevStrMapping.put(255, "y");
		}
		
		for (int c=0; c<str.length(); c++)
		{
			if (!StrUtils.validSevChar(str.charAt(c)))
			{
				int ch = str.charAt(c);
				String newCh = "";
				if (sevStrMapping.containsKey(ch))
					newCh = sevStrMapping.get(ch);
				str = StrUtils.left(str, c) + newCh + StrUtils.right(str, str.length() - (c + 1));
			}
		}
		/*
		str = str.replace("æ", "ae");
		str = str.replace("ø", "oe");
		str = str.replace("å", "aa");
		str = str.replace("Æ", "AE");
		str = str.replace("Ø", "OE");
		str = str.replace("Å", "AA");
		*/
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return str;
	}
	private static boolean validSevChar(Character charAt) {
		int i = new Integer(charAt);
		if ((i >= 65) && (i <= 90)) // A-Z
			return true;
		else if ((i >= 97) && (i <= 122)) // a-z
			return true;
		else if (i == 32) // space
			return true;
		return false;
	}
	/**
	 * Creates a bitstring containing '0' and '1' with the '1'
	 * at position <idx> (starting from zero)
	 * The string returned will be <len> characters long.
	 * 
	 * @param idx The position of the '1'
	 * @param len The length of the returned string (can be null)
	 * @return bitstring containing '0' and '1'
	 */
	public static String createIndex(Integer idx, Integer len) {
		if (idx < 0)
			return null;
		String idxStr = "";
		for (int c=0; c<idx; c++)
			idxStr += "0";
		idxStr += "1";
		
		if (len != null)
			while (idxStr.length() < len)
				idxStr += "0";
		return idxStr;
	}
	/**
	 * Returns a substring starting at <from>, ending at <to>.
	 * Will not throw an exception if index is out of range...
	 * 
	 * (where first pos i 0.
	 * 
	 * @param str
	 * @param from
	 * @param to
	 * @return String or null
	 */
	public static String substring(String str, int from, int to) {
		if (str != null)
		{
			if (from < 0)
				from = 0;
			if (from >= str.length())
				from = str.length() - 1;
			if (to < 0)
				to = 0;
			if (to >= str.length())
				to = str.length() - 1;
			
			if (from <= to)
				return str.substring(from, to+1);
			else
				return "";
		}
		return null;
	}
	public static String stripLeading(String str, char ch) {
		while ((str != null) && (str.length() > 1) && (str.charAt(0) == ch))
			str = str.substring(1);
		return str;
	}
	public static String replace(String str, String replaceStr, String newStr) {
		if (str != null)
		{
			Integer idx;
			while ((idx = str.indexOf(replaceStr)) > -1)
				str = StrUtils.left(str, idx) + newStr + StrUtils.right(str, str.length() - (replaceStr.length() + idx));
		}
		return str;
	}
	public static String insertAt(String str, int pos, String insStr) {
		if (str.length() >= pos)
		{
			String part1 = StrUtils.left(str, pos);
			String part2 = StrUtils.right(str, str.length() - pos);
			return part1 + insStr + part2;
		}
		return str;
	}
	public static String changeCharacterAt(String str, int pos, char insStr) {
		char chr[] = new char[str.length()];
		str.getChars(0,str.length(),  chr, 0);
		chr[pos]=insStr;
		String ret="";
		for (int i=0;i<chr.length;i++){
			ret+=chr[i];
		}
		return ret;
	}
	/**
	 * Return the last occurence's index of the character in the string
	 * If no occurence, return -1
	 * @param str
	 * @param ch
	 * @return last index, or -1
	 */
	public static int lastIndexOf(String str, char ch)
	{
		try {
			if (str != null)
				for (int c=str.length() - 1; c>=0; c--)
				{
					if (str.charAt(c) == ch)
						return c;
				}
		}
		catch (Exception e){
		}
		return -1;
	}
	/**
	 * Return the first occurence's index of the character in the string
	 * If no occurence, return -1
	 * @param str
	 * @param ch
	 * @return last index, or -1
	 */
	public static int firstIndexOf(String str, char ch) {
		try {
			if (str != null)
				for (int c=0; c<str.length(); c++)
				{
					if (str.charAt(c) == ch)
						return c;
				}
		}
		catch (Exception e){
		}
		return -1;
	}

	/**
	 * Check if str contains string subStr
	 * @param str
	 * @param subStr
	 * @return true og false
	 */
	public static boolean contains(String str, String subStr) {
		try {
			if (str != null)
			{
				if (str.contains(subStr))
					return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	public static String format(Object o, String outputFormat) {
		try {
			String classname = o.getClass().getSimpleName();
			if (classname.equals("KinDateDesc"))
				return ((KinDateDesc)o).asString(outputFormat);
			else if (classname.equals("KinTimeDesc"))
				return ((KinTimeDesc)o).asString(outputFormat);
			else if (classname.equals("Double"))
				return StrUtils.format((Double)o, outputFormat);
		}
		catch (Exception e) {
		}
		return o.toString();
	}
	*/
	
	private static String format(Double d, String outputFormat) {
		try {
			if ("INTEGER".equals(outputFormat))
				 return new Integer(d.intValue()).toString();
		}
		catch (Exception e) {
		}
		return d.toString();
	}
	
	/** Convert strings that has been converted with javascriptfunction urlEscape (script.js)
	 * Used for escaping strings passed as url parameters...
	 * @param str
	 * @return unescaped string
	 */
	public static String urlUnescape(String str)
	{
		str = StrUtils.replace(str, "_plus_", "+");
		str = StrUtils.replace(str, "_minus_", "-");
		str = StrUtils.replace(str, "_equals_", "=");
		str = StrUtils.replace(str, "_proc_", "%");
		str = StrUtils.replace(str, "_and_", "&");

		/*
		*/
		return str;
	}
	public static String urlEscape(String str) {
		str = StrUtils.replace(str, "+", "_plus_");
		str = StrUtils.replace(str, "-", "_minus_");
		str = StrUtils.replace(str, "=", "_equals_");
		str = StrUtils.replace(str, "%", "_proc_");
		str = StrUtils.replace(str, "&", "_and_");
		return str;
		
	}
	
	public static String asString(Object[] arr) {
		try {
			String str = "[";
			if (arr != null)
				for (int c=0; c<arr.length; c++)
				{
					if (c > 0)
						str += ",";
					if (arr[c] != null)
						str += arr[c].toString();
					else
						str += "null";
				}
			str += "]";
			return str;
		} catch (Exception e) {
			return "error";
		}
	}
	public static String[] pathToArray(String dir) {
		String[] v = null;
		try {
			dir = StrUtils.stripTrailing(dir, '/');
			dir = StrUtils.stripTrailing(dir, '\\');
			dir = StrUtils.replace(dir, "\\", "/");
			v = StrUtils.split(dir, "/");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}
	/**
	 * Endret til Object, slik at jeg slipper å konvertere fra ent.getField("feltnavn") før test.
	 * @param s
	 * @return
	 */
	public static boolean isNullOrEmpty(Object s){     
		if (s==null) return true;
		if (s.toString().trim().length()==0) return true;
		return false; //er ok
	}
	/**
	 * @param S som skal ha fast lengde
	 * @param Lengde lengden
	 */
	public static String fixedLength(String s, int length) {
	   StringBuffer buffer = new StringBuffer(length);
	   buffer.append(s);
	   for (int i =buffer.length()-1;i<length;i++){
		   buffer.append(' ');
	   }
	   return buffer.substring(0, length);
	}
	public static String[] toFixedWidthArray(String s, int size){
		if (size<=0)
			throw(new RuntimeException("The parameter size must be bigger than zero"));
		if (StrUtils.isNullOrEmpty(s))
			throw(new RuntimeException("The parameter s cannot be null or blank"));
		
		String expr = "(?<=\\G.{"+size+"})";
		return s.split(expr);
	}
	/**
	 * Splitter opp en komma eller plussdelt liste med id'er til en liste med kommaseparerte verdier i med et visst antall i hver 
	 * @param idString "23023+232302+244"
	 * @param chunkSize 1000
	 * @return
	 */
	public static List<String> getCommaList(String idString,int chunkSize){
		 String f = idString.replace('+',','); //kan hende vi engang får komma, så vi lar den stå.
		 String dataIn[]=f.split("\\,"); //dele opp alle verdier i en array
		 return new StrUtils().getCommaList(dataIn, chunkSize);

	}
	/**
	 * Splitter opp en array av id i bolker med X antall i hver  
	 * @param dataIn
	 * @param chunkSize
	 * @return List
	 */
	public List<String> getCommaList (String[] dataIn,int chunkSize){
	
		 List<String> ret = new ArrayList<String>();
		 int startDim=0;
		 
		 do{
			 String dims=getCommaString(dataIn,startDim,chunkSize);
			 ret.add(dims);
			 startDim+=chunkSize;
		 }while (startDim<dataIn.length); //fjernet -1, den kuttet siste verdi ved odde lengder
		 return ret;
	}
	 /**
	 * @param dataIn
	 * @param startDim
	 * @param i
	 * @return
	 */
	public String getCommaString(String[] dataIn, int startDim, int antall) {
		int sluttDim = startDim+antall-1;
		if (sluttDim>dataIn.length-1)
			sluttDim=dataIn.length-1;
		StringBuilder sb = new StringBuilder();
		String sep="";
		for (int i=startDim;i<=sluttDim;i++){
			if (StrUtils.isNullOrEmpty(dataIn[i])) continue;
			sb.append(sep);
			sb.append(dataIn[i]);
			sep=",";
		}
		return sb.toString();
	}
	/**
	 * Return the length of a string. str = null will return 0.
	 * @param str
	 * @return
	 */
	public static int length(String str) {
		if (str != null)
			return str.length();
		return 0;
	}
	static List<Object> sortedKeys(Enumeration<?> keys){
		ArrayList<Object> l=new ArrayList<Object>();
		while (keys.hasMoreElements()){
			l.add(keys.nextElement());
		}
	   Collections.sort(l, new Comparator<Object>() {
	            @Override
	            public int compare(Object a, Object b) {
	                //use instanceof to verify the references are indeed of the type in question
	                return  a.toString().compareTo(b.toString());
	            }
	        }); 
		 return l;
	}
	/**
	 * Convert an object to a json string.
	 * @param o
	 * @return
	 */
	public static String jsonString(Object o)
	{
		StringBuilder sb = new StringBuilder();
		
		if (o == null)
			return "\"\"";
		try {
			String classname = o.getClass().getSimpleName();
			if (classname.equals("Hashtable"))
			{
				sb.append('{');
				Hashtable<?, ?> ht = (Hashtable<?, ?>)o;
				List<?> keys = sortedKeys(ht.keys());
				for (int cnt=0;cnt<keys.size();cnt++)
				{
					Object key = keys.get(cnt);
					Object element = ht.get(key);
					String value = StrUtils.jsonString(element);
					sb.append("\"" + key.toString() + "\":" + value);
					if (cnt<keys.size()-1)
						sb.append(',');
				}
				sb.append('}');
			}
			else if (classname.equals("Vector"))
			{
				sb.append("[");
				Vector<?> v = (Vector<?>) o;
				for (int c=0; c<v.size(); c++)
				{
					String value = StrUtils.jsonString(v.get(c));
					if (c > 0)
						value = "," + value;
					sb.append(value);
				}
				sb.append("]");
			}
			else if (StrUtils.right(classname, 2).equals("[]"))
			{
				// Object[]
				Object[] oa = (Object[])o;
				sb.append('[');
				for (int c=0; c<oa.length; c++)
				{
					String value = StrUtils.jsonString(oa[c]);
					if (c > 0)
						value = "," + value;
					sb.append(value);
				}
				sb.append(']');
			}
			else
			{
				String s = o.toString();
				
				// RT 160923, Backslash -> &#92;
				// RT 160711 Replace \ with \\.
				//s = StrUtils.replace(s, "\\", "XbackslashX");
				//s = StrUtils.replace(s, "XbackslashX", "\\\\");
				s = StrUtils.replace(s, "\\", "&#92;");
				
				if ("{".equals(StrUtils.left(s, 1)) && "}".equals(StrUtils.right(s, 1)))
					sb.append(s);
				else
					sb.append("\"" + s + "\"");
			}
			
			
		} catch (Exception e) {
			System.out.println("JSON String convert error! Could not convert to JSON: " + o);
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static String toString(Object o) {
		String jsonStr = StrUtils.jsonString(o);
		return StrUtils.replace(jsonStr, "\"", "");
	}
	
	public static boolean testBooleanField(Object f){
		if (f==null || f.toString().equals("0")) 
			return false;
		else
			return true;
	}
	public static String getFirstNonEmpty(Object[] data){
		if (data!=null){
			for (int i = 0;i<data.length;i++){
				if (!StrUtils.isNullOrEmpty(data[i])) return data[i].toString();
			}
		}
		return null;
	}
	public static String getLastNonEmpty(Object[] data){
		if (data!=null){
			for (int i = data.length-1;i>=0;i--){
				if (!StrUtils.isNullOrEmpty(data[i])) return data[i].toString();
			}
		}
		return null;
	}

}
