import java.util.HashMap;
import java.util.Map;

public class RomanArabic {
    private static String[] liczby =
        { "MMM", "MM",  "M",
           "CM",  "D", "CD",
          "CCC", "CC",  "C",
           "XC",  "L", "XL",
          "XXX", "XX",  "X",
           "IX",  "V", "IV",
          "III", "II",  "I"};

    public static int Roman2Arabic(String str) throws RomanArabicException {
        int sum = 0, multiplier = 1000;
        int currentIndex = 0;

        boolean canExist = true;
        for (int i = 0; i < liczby.length / 3; i++) {
            boolean rowUsed = false;
            for(int j = 0; j < 3; j++) {
                if (str.indexOf(liczby[3 * i + j], currentIndex) == 0) {
                    if (rowUsed || !canExist)
                        throw new RomanArabicException(); // TODO: message
                    rowUsed = true;
                    canExist = i % 2 == 0 && j != 2;
                    currentIndex += liczby[3 * i + j].length();

                    if (i % 2 == 0) {
                        sum += multiplier * (j + 1);
                    } else {
                        switch (j) {
                        case 0:
                            sum += multiplier / 10 * 9;
                            break;
                        case 1:
                            sum += multiplier;
                            break;
                        case 2:
                            sum += multiplier / 10 * 4;
                            break;
                        }
                    }
                }

            }
            if (i % 2 == 0)
                multiplier /= 10;
        }

        return sum;
    }

    public static String Arabic2Roman(int number) throws RomanArabicException {
        StringBuilder builder = new StringBuilder();
        return "XD";

    }
}