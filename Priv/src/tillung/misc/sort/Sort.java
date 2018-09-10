package tillung.misc.sort;

public class Sort {

	public static void bubbleSort(Object[] arr, int from, int to) {
		if (arr != null)
		{
			int c = from;
			
			while (c <= to)
			{
				if (c > from)
				{
					if (Sort.greater(arr[c - 1], arr[c]))
					{
						Object tmp = arr[c - 1];
						arr[c - 1] = arr[c];
						arr[c] = tmp;
						c--;
						c--;
					}
				}
				c++;
			}
		}
	}
	
	public static boolean greater(Object o1, Object o2) {
		if ((o1 != null) && (o2 != null))
		{
			String className = o1.getClass().getName();
			String className2 = o2.getClass().getName();

			if (o1.getClass().getName().equals(o2.getClass().getName()))
			{
				if (className.equals("java.lang.String"))
				{
					if (((String)o1).compareTo((String)o2) > 0)
						return true;
				}
				if (className.equals("java.lang.Integer"))
				{
					if (((Integer)o1) > ((Integer)o2))
						return true;
				}
				else
				{
					System.out.println("unknown datatype " + className);
				}
			}
			else
				System.out.println("different datatypes: " + className + " and " + className2);
		}
		else if (o1 != null) // o2 == null
			return true;
		return false;
	}

	
}
