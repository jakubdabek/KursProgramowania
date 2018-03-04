import java.util.Scanner;

public class Program02 {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Too few arguments");
            return;
        }

        char mode;
        if ((mode = mode(args[0])) == 'X')
        {
            return;
        }

        for (int i = 1; i < args.length; i++) {
            try {
                if (mode == 'R') {
                    int value = RomanArabic.Roman2Arabic(args[i]);
                    System.out.println("Value of " + args[i] + " is " + value);
                } else {
                    String str = RomanArabic.Arabic2Roman(Integer.parseInt(args[i]));
                    System.out.println(args[i] + " in roman is " + str);
                }
            } catch (RomanArabicException e) {
                System.err.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println(args[i] + " is not an integer");
            }
        }
    }

    static String quoted(String str) {
        return "\"" + str + "\"";
    }

    static String quoted(int i) {
        return "\"" + i + "\"";
    }

    static char mode(String mode) {
        System.err.println(mode);
        if (mode.equals("TEST") || mode.equals("TEST_PRINTALL")) {
            TEST(mode.equals("TEST_PRINTALL"));
            return 'X';
        } else if (mode.equals("TEST_WRONG") || mode.equals("TEST_WRONG_PRINTALL")) {
            TEST_WRONG(mode.equals("TEST_WRONG_PRINTALL"));
            return 'X';
        } else if (mode != "R" && mode != "A") {
            System.err.println("Wrong arguments\n" + "Put mode as the first argument: "
                    + "'R' for roman to arabic, 'A' for arabic to roman");
            return 'X';
        }

        return mode.equals("R") ? 'R' : 'A';
    }

    static void TEST(boolean print) {
        boolean passed = true;

        System.err.println("Testing if the functions are reverse of each other on numbers 1-3999");
        for (int i = 1; i < 4000; i++) {
            try {
                String parsedRomanNumeral = RomanArabic.Arabic2Roman(i);
                int parsedValue = RomanArabic.Roman2Arabic(parsedRomanNumeral);
                if (parsedValue != i) {
                    passed = false;
                    System.err.println("ERROR - The results don't match: " + "i = " + quoted(i) + " "
                            + "converted to roman = " + quoted(parsedRomanNumeral) + " "
                            + "converted back to arabic = " + quoted(parsedValue));
                }
            } catch (RomanArabicException e) {
                passed = false;
                System.err.println("ERROR - Exception caught: " + e.getMessage());
            }
        }

        if (!passed) {
            System.err.println("Test failed, aborting further testing");
            return;
        } else {
            System.err.println("Test passed, next test with known values");
        }

        int expectedValue;
        String expectedRomanNumeral;
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("[,\\s]");
        while (scanner.hasNext()) {
            try {
                expectedValue = Integer.parseInt(scanner.next());
                expectedRomanNumeral = scanner.nextLine().substring(1);
                int parsedValue = RomanArabic.Roman2Arabic(expectedRomanNumeral);
                String parsedRomanNumeral = RomanArabic.Arabic2Roman(expectedValue);
                boolean flag = true;
                if (parsedValue != expectedValue) {
                    flag = passed = false;
                    System.err.println("ERROR - values don't match: " + "expected = " + quoted(expectedValue) + " "
                            + "actual = " + quoted(parsedValue));
                }
                if (!parsedRomanNumeral.equals(expectedRomanNumeral)) {
                    flag = passed = false;
                    System.err.println("ERROR - roman numerals don't match: " + "expected = "
                            + quoted(expectedRomanNumeral) + " " + "actual = " + quoted(parsedRomanNumeral));
                }
                if (print && flag) {
                    System.err.println("OK - " + quoted(parsedValue) + " = " + quoted(parsedRomanNumeral));
                }
            } catch (RomanArabicException e) {
                passed = false;
                System.err.println("ERROR - Exception caught: " + quoted(e.getMessage()));
            } catch (NumberFormatException e) {
                System.err.println("FATAL ERROR - testing aborted, wrong test input.");
                System.err.println(e.getMessage());
                return;
            }
        }

        if (!passed) {
            System.err.println("Test failed");
        } else {
            System.err.println("All tests passed");
        }
    }

    static void TEST_WRONG(boolean print) {
        boolean rom2arab = false, passed = true;
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                rom2arab = !rom2arab;
                continue;
            }

            boolean thisPassed = false;
            try {
                if (rom2arab) {
                    int parsedValue = RomanArabic.Roman2Arabic(line);
                    System.err.println("ERROR - value somehow parsed: " + quoted(line) + " = " + quoted(parsedValue));
                } else {
                    String parsedRomanNumeral = RomanArabic.Arabic2Roman(Integer.parseInt(line));
                    System.err.println("ERROR - value somehow parsed: " + quoted(line) + " = " + quoted(parsedRomanNumeral));
                }
            } catch (RomanArabicException e) {
                thisPassed = true;
                if (print) {
                    System.err.println("OK - Exception caught: " + quoted(e.getMessage()));
                }
            } catch (NumberFormatException e) {
                System.err.println("FATAL ERROR - testing aborted, wrong test input.");
                System.err.println(e.getMessage());
                System.err.println(quoted(line) + " size " + line.length());
                return;
            }
            passed = passed && thisPassed;
        }

        if (!passed) {
            System.err.println("Test failed");
        } else {
            System.err.println("All tests passed");
        }
    }

}