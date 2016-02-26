/**
 * 
 */
package net.combase.desktopcrm.swing;

/**
 * @author "Till Freier"
 *
 */
public class NumberToWord
{
	public static String convertBelow30IntToWord(int value)
	{
		switch (value)
		{
			case 1:
				return "uno";
			case 2:
				return "dos";
			case 3:
				return "tres";
			case 4:
				return "cuatro";
			case 5:
				return "cinco";
			case 6:
				return "seis";
			case 7:
				return "siete";
			case 8:
				return "ocho";
			case 9:
				return "nueve";
			case 10:
				return "diez";
			case 11:
				return "once";
			case 12:
				return "doce";
			case 13:
				return "trece";
			case 14:
				return "catorce";
			case 15:
				return "quince";
			case 16:
				return "dieciseis";
			case 17:
				return "dieciseite";
			case 18:
				return "dieciocho";
			case 19:
				return "diecinueve";
			case 20:
				return "veinte";
			case 21:
				return "veintiuno";
			case 22:
				return "veintidos";
			case 23:
				return "veintitres";
			case 24:
				return "veinticuatro";
			case 25:
				return "veinticinco";
			case 26:
				return "veintiseis";
			case 27:
				return "veintisiete";
			case 28:
				return "veintiocho";
			case 29:
				return "veintinueve";
			case 30:
				return "treinta";
		}

		return "";
	}


	public static String convertBelow100IntToWord(int value)
	{
		switch (value / 10)
		{
			case 0:
			case 1:
			case 2:
				return convertBelow30IntToWord(value);
			case 3:
				return "treinta " + ((value % 10 > 0) ? "y " : "") + convertBelow30IntToWord(value % 10);
			case 4:
				return "cuarenta " + ((value % 10 > 0) ? "y " : "") + convertBelow30IntToWord(value % 10);
			case 5:
				return "cincuenta " + ((value % 10 > 0) ? "y " : "") + convertBelow30IntToWord(value % 10);
			case 6:
				return "sesenta " + ((value % 10 > 0) ? "y " : "") + convertBelow30IntToWord(value % 10);
			case 7:
				return "setenta " + ((value % 10 > 0) ? "y " : "") + convertBelow30IntToWord(value % 10);
			case 8:
				return "ochenta " + ((value % 10 > 0) ? "y " : "") + convertBelow30IntToWord(value % 10);
			case 9:
				return "noventa " + ((value % 10 > 0) ? "y " : "") + convertBelow30IntToWord(value % 10);
		}

		return "";
	}


	public static String convertBigIntSectionToWord(int value, String word)
	{

		if (value < 1)
			return "";

		if (value > 1)
			return convertBelow30IntToWord(value) + " " + word + " ";

		return word + " ";
	}


	public static String convert100IntSectionToWord(int value)
	{

		if (value < 100)
			return "";

		switch (value)
		{
			case 100:
				return "cien ";
			case 101:
				return "ciento uno ";
			case 102:
				return "ciento dos ";
		}

		switch (value / 100)
		{
			case 5:
				return "quinientos ";
			case 7:
				return "setecientos ";
			case 9:
				return "novecientos ";

			default:
				break;
		}

		return convertBelow30IntToWord(value / 100) + "cientos ";
	}


	public static String convertToWord(int intValue)
	{

		if (intValue < 1)
			return "cero";

		if (intValue == 1)
			return "un";

		StringBuilder result = new StringBuilder();

		result.append(convertBigIntSectionToWord(intValue / 1000, "mil"));
		result.append(convert100IntSectionToWord(intValue % 1000));
		result.append(convertBelow100IntToWord((intValue % 100)));

		return result.toString();
	}


	public static String convertCurrencyToWord(double value)
	{
		value = Math.abs(value);
		long totalCents = Math.round(value * 100);
		int cents = ((int) totalCents) % 100;
		StringBuilder result = new StringBuilder();
		result.append(convertToWord((int) value).trim());
		result.append(" lempira");
		if (value >= 2)
			result.append('s');
		if (cents > 0)
			result.append(" y ").append(convertBelow100IntToWord(cents)).append(" centavos");

		return result.toString();
	}


	public static void main(String[] args)
	{
		System.out.println(convertCurrencyToWord(12.78));
		System.out.println(convertCurrencyToWord(1490.10));
		System.out.println(convertCurrencyToWord(4005.99));
		System.out.println(convertCurrencyToWord(583.05));
	}
}
