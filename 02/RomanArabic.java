import java.util.HashMap;
import java.util.Map;

public class RomanArabic {

    private static String[] allRomanNumerals =
        { "MMM", "MM",  "M",
           "CM",  "D", "CD",
          "CCC", "CC",  "C",
           "XC",  "L", "XL",
          "XXX", "XX",  "X",
           "IX",  "V", "IV",
          "III", "II",  "I"};
    private static int[] allValues = 
        { 3000, 2000, 1000,
           900,  500,  400,
           300,  200,  100,
            90,   50,   40,
            30,   20,   10,
             9,    5,    4,
             3,    2,    1};

    public static int Roman2Arabic(String str) throws RomanArabicException {
        int sum = 0;
        int currentIndex = 0;

        boolean canExist = true;
        for (int i = 0; i < allRomanNumerals.length / 3; i++) {
            boolean rowUsed = false;
            for(int j = 0; j < 3; j++) {
                if (str.indexOf(allRomanNumerals[3 * i + j], currentIndex) == currentIndex) {
                    if (rowUsed || !canExist)
                        throw new RomanArabicException("The roman numerals " + str + " have a wrong format");
                    rowUsed = true;
                    canExist = j == 1 || i % 2 == 0;
                    currentIndex += allRomanNumerals[3 * i + j].length();
                    sum += allValues[3 * i + j];
                }
            }
            canExist = canExist || i % 2 == 0;
        }

        if (currentIndex != str.length())
            throw new RomanArabicException("The roman numerals " + str + " have a wrong format");

        return sum;
    }

    public static String Arabic2Roman(int number) throws RomanArabicException {
        if (number <= 0 || number > 3999)
            throw new RomanArabicException(number + " is out of range");
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < allRomanNumerals.length; i++)
        {
            if (number - allValues[i] >= 0)
            {
                builder.append(allRomanNumerals[i]);
                number -= allValues[i];
            }
        }

        if (number != 0)
            throw new RuntimeException("Something very wrong happened");

        return builder.toString();
    }

}