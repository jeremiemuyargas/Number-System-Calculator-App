import java.math.BigInteger;

/**
 * Handles all conversion and arithmetic logic between number systems.
 * Uses BigInteger internally so very large numbers are still handled correctly.
 */
public class Converter {

    /** Checks that a string only contains digits valid for the given system. */
    public static void validate(String value, NumberSystem system) throws InvalidDigitException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDigitException("Input cannot be empty.");
        }
        String v = value.trim();
        if (v.startsWith("-")) {
            v = v.substring(1);
        }
        if (v.isEmpty()) {
            throw new InvalidDigitException("Input cannot be empty.");
        }
        for (char c : v.toUpperCase().toCharArray()) {
            if (Character.digit(c, system.getRadix()) == -1) {
                throw new InvalidDigitException(
                        "'" + c + "' is not a valid " + system + " digit.");
            }
        }
    }

    /** Converts a value from one number system to another. */
    public static String convert(String value, NumberSystem from, NumberSystem to)
            throws InvalidDigitException {
        BigInteger decimalValue = toBigInteger(value, from);
        return fromBigInteger(decimalValue, to);
    }

    public static String add(String a, String b, NumberSystem system) throws InvalidDigitException {
        BigInteger result = toBigInteger(a, system).add(toBigInteger(b, system));
        return fromBigInteger(result, system);
    }

    public static String subtract(String a, String b, NumberSystem system) throws InvalidDigitException {
        BigInteger result = toBigInteger(a, system).subtract(toBigInteger(b, system));
        return fromBigInteger(result, system);
    }

    public static String multiply(String a, String b, NumberSystem system) throws InvalidDigitException {
        BigInteger result = toBigInteger(a, system).multiply(toBigInteger(b, system));
        return fromBigInteger(result, system);
    }

    /** Integer division. If there is a remainder it is reported alongside the quotient. */
    public static String divide(String a, String b, NumberSystem system)
            throws InvalidDigitException, ArithmeticException {
        BigInteger dividend = toBigInteger(a, system);
        BigInteger divisor = toBigInteger(b, system);
        if (divisor.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        BigInteger[] quotientAndRemainder = dividend.divideAndRemainder(divisor);
        String quotient = fromBigInteger(quotientAndRemainder[0], system);
        if (quotientAndRemainder[1].equals(BigInteger.ZERO)) {
            return quotient;
        }
        String remainder = fromBigInteger(quotientAndRemainder[1], system);
        return quotient + " remainder " + remainder;
    }

    private static BigInteger toBigInteger(String value, NumberSystem system) throws InvalidDigitException {
        validate(value, system);
        return new BigInteger(value.trim().toUpperCase(), system.getRadix());
    }

    private static String fromBigInteger(BigInteger value, NumberSystem system) {
        return value.toString(system.getRadix()).toUpperCase();
    }
}
