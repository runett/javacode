package no.tillung.utils;

public class Utils {
	public static String getArg(String[] args, String a)
	{
		for (int c=0; c<args.length; c++)
		{
			if (a.equalsIgnoreCase(args[c]) && ((c + 1) < args.length))
				return args[c+1];
		}
		return null;
	}
}
