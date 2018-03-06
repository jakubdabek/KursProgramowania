public class RomanArabic {

    public static int Roman2Arabic(String str) throws RomanArabicException {
        int sum = 0;
        int currentIndex = 0;
        boolean wasCompound = false; // for combinations of symbols with one of lower value before one of higher e.g. IX, IV

        for (int i = 0; i < allRomanNumerals.length / 3; i++) {
            boolean rowUsed = false; // a row is 3 elements
            for (int j = 0; j < 3; j++) {
                if (str.indexOf(allRomanNumerals[3 * i + j], currentIndex) == currentIndex) {
                    if (rowUsed || wasCompound)
                        throw new RomanArabicException("The roman numerals " + str + " have a wrong format");
                    rowUsed = true;
                    wasCompound = i % 2 == 1 && j != 1;
                    currentIndex += allRomanNumerals[3 * i + j].length();
                    sum += allValues[3 * i + j];
                }
            }
            wasCompound = wasCompound && i % 2 == 1;
        }

        if (currentIndex != str.length())
            throw new RomanArabicException("The roman numerals " + str + " have a wrong format");

        return sum;
    }

    public static String Arabic2Roman(int number) throws RomanArabicException {
        if (number <= 0 || number > 3999)
            throw new RomanArabicException(number + " is out of range");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < allRomanNumerals.length; i++) {
            if (number - allValues[i] >= 0) {
                builder.append(allRomanNumerals[i]);
                number -= allValues[i];
            }
        }

        if (number != 0)
            throw new RuntimeException("Something very wrong happened");

        return builder.toString();
    }

    private static final String[] allRomanNumerals =
        { "MMM", "MM",  "M",
           "CM",  "D", "CD",
          "CCC", "CC",  "C",
           "XC",  "L", "XL",
          "XXX", "XX",  "X",
           "IX",  "V", "IV",
          "III", "II",  "I"};

    private static final int[] allValues = 
        {  3000, 2000, 1000,
            900,  500,  400,
            300,  200,  100,
             90,   50,   40,
             30,   20,   10,
              9,    5,    4,
              3,    2,    1};

}