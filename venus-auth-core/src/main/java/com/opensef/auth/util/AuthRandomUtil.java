package com.opensef.auth.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机字符串工具类，复制自org.apache.commons.lang3.RandomStringUtils
 */
public class AuthRandomUtil {

    public AuthRandomUtil() {
    }

    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    // Random

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of all characters.</p>
     *
     * @param count the length of random string to create
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String random(final int count) {
        return random(count, false, false);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of alpha-numeric
     * characters as indicated by the arguments.</p>
     *
     * @param count   the length of random string to create
     * @param letters if {@code true}, generated string may include
     *                alphabetic characters
     * @param numbers if {@code true}, generated string may include
     *                numeric characters
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String random(final int count, final boolean letters, final boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of characters specified.</p>
     *
     * @param count the length of random string to create
     * @param chars the character array containing the set of characters to use,
     *              may be null
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String random(final int count, final char... chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, random());
        }
        return random(count, 0, chars.length, false, false, chars, random());
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of alpha-numeric
     * characters as indicated by the arguments.</p>
     *
     * @param count   the length of random string to create
     * @param start   the position in set of chars to start at
     * @param end     the position in set of chars to end before
     * @param letters if {@code true}, generated string may include
     *                alphabetic characters
     * @param numbers if {@code true}, generated string may include
     *                numeric characters
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers) {
        return random(count, start, end, letters, numbers, null, random());
    }

    /**
     * Creates a random string based on a variety of options, using
     * default source of randomness.
     *
     * <p>This method has exactly the same semantics as
     * {@link #random(int, int, int, boolean, boolean, char[], Random)}, but
     * instead of using an externally supplied source of randomness, it uses
     * the internal static {@link Random} instance.</p>
     *
     * @param count   the length of random string to create
     * @param start   the position in set of chars to start at
     * @param end     the position in set of chars to end before
     * @param letters if {@code true}, generated string may include
     *                alphabetic characters
     * @param numbers if {@code true}, generated string may include
     *                numeric characters
     * @param chars   the set of chars to choose randoms from.
     *                If {@code null}, then it will use the set of all chars.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     *                                        {@code (end - start) + 1} characters in the set array.
     * @throws IllegalArgumentException       if {@code count} &lt; 0.
     */
    public static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers, final char... chars) {
        return random(count, start, end, letters, numbers, chars, random());
    }

    /**
     * Creates a random string based on a variety of options, using
     * supplied source of randomness.
     *
     * <p>If start and end are both {@code 0}, start and end are set
     * to {@code ' '} and {@code 'z'}, the ASCII printable
     * characters, will be used, unless letters and numbers are both
     * {@code false}, in which case, start and end are set to
     * {@code 0} and {@link Character#MAX_CODE_POINT}.
     *
     * <p>If set is not {@code null}, characters between start and
     * end are chosen.</p>
     *
     * <p>This method accepts a user-supplied {@link Random}
     * instance to use as a source of randomness. By seeding a single
     * {@link Random} instance with a fixed seed and using it for each call,
     * the same random sequence of strings can be generated repeatedly
     * and predictably.</p>
     *
     * @param count   the length of random string to create
     * @param start   the position in set of chars to start at (inclusive)
     * @param end     the position in set of chars to end before (exclusive)
     * @param letters if {@code true}, generated string may include
     *                alphabetic characters
     * @param numbers if {@code true}, generated string may include
     *                numeric characters
     * @param chars   the set of chars to choose randoms from, must not be empty.
     *                If {@code null}, then it will use the set of all chars.
     * @param random  a source of randomness.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     *                                        {@code (end - start) + 1} characters in the set array.
     * @throws IllegalArgumentException       if {@code count} &lt; 0 or the provided chars array is empty.
     * @since 2.0
     */
    public static String random(int count, int start, int end, final boolean letters, final boolean numbers,
                                final char[] chars, final Random random) {
        if (count == 0) {
            return "";
        }
        if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }

        if (start == 0 && end == 0) {
            if (chars != null) {
                end = chars.length;
            } else if (!letters && !numbers) {
                end = Character.MAX_CODE_POINT;
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        } else if (end <= start) {
            throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
        }

        final int zeroDigitAscii = 48;
        final int firstLetterAscii = 65;

        if (chars == null && (numbers && end <= zeroDigitAscii
                || letters && end <= firstLetterAscii)) {
            throw new IllegalArgumentException("Parameter end (" + end + ") must be greater then (" + zeroDigitAscii + ") for generating digits " +
                    "or greater then (" + firstLetterAscii + ") for generating letters.");
        }

        final StringBuilder builder = new StringBuilder(count);
        final int gap = end - start;

        while (count-- != 0) {
            final int codePoint;
            if (chars == null) {
                codePoint = random.nextInt(gap) + start;

                switch (Character.getType(codePoint)) {
                    case Character.UNASSIGNED:
                    case Character.PRIVATE_USE:
                    case Character.SURROGATE:
                        count++;
                        continue;
                }

            } else {
                codePoint = chars[random.nextInt(gap) + start];
            }

            final int numberOfChars = Character.charCount(codePoint);
            if (count == 0 && numberOfChars > 1) {
                count++;
                continue;
            }

            if (letters && Character.isLetter(codePoint)
                    || numbers && Character.isDigit(codePoint)
                    || !letters && !numbers) {
                builder.appendCodePoint(codePoint);

                if (numberOfChars == 2) {
                    count--;
                }

            } else {
                count++;
            }
        }
        return builder.toString();
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of characters
     * specified by the string, must not be empty.
     * If null, the set of all characters is used.</p>
     *
     * @param count the length of random string to create
     * @param chars the String containing the set of characters to use,
     *              may be null, but must not be empty
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0 or the string is empty.
     */
    public static String random(final int count, final String chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, random());
        }
        return random(count, chars.toCharArray());
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of Latin alphabetic
     * characters (a-z, A-Z).</p>
     *
     * @param count the length of random string to create
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String randomAlphabetic(final int count) {
        return random(count, true, false);
    }

    /**
     * Creates a random string whose length is between the inclusive minimum and
     * the exclusive maximum.
     *
     * <p>Characters will be chosen from the set of Latin alphabetic characters (a-z, A-Z).</p>
     *
     * @param minLengthInclusive the inclusive minimum length of the string to generate
     * @param maxLengthExclusive the exclusive maximum length of the string to generate
     * @return the random string
     * @since 3.5
     */
    public static String randomAlphabetic(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAlphabetic(nextInt(minLengthInclusive, maxLengthExclusive));
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of Latin alphabetic
     * characters (a-z, A-Z) and the digits 0-9.</p>
     *
     * @param count the length of random string to create
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String randomAlphanumeric(final int count) {
        return random(count, true, true);
    }

    /**
     * Creates a random string whose length is between the inclusive minimum and
     * the exclusive maximum.
     *
     * <p>Characters will be chosen from the set of Latin alphabetic
     * characters (a-z, A-Z) and the digits 0-9.</p>
     *
     * @param minLengthInclusive the inclusive minimum length of the string to generate
     * @param maxLengthExclusive the exclusive maximum length of the string to generate
     * @return the random string
     * @since 3.5
     */
    public static String randomAlphanumeric(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAlphanumeric(nextInt(minLengthInclusive, maxLengthExclusive));
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of characters whose
     * ASCII value is between {@code 32} and {@code 126} (inclusive).</p>
     *
     * @param count the length of random string to create
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String randomAscii(final int count) {
        return random(count, 32, 127, false, false);
    }

    /**
     * Creates a random string whose length is between the inclusive minimum and
     * the exclusive maximum.
     *
     * <p>Characters will be chosen from the set of characters whose
     * ASCII value is between {@code 32} and {@code 126} (inclusive).</p>
     *
     * @param minLengthInclusive the inclusive minimum length of the string to generate
     * @param maxLengthExclusive the exclusive maximum length of the string to generate
     * @return the random string
     * @since 3.5
     */
    public static String randomAscii(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomAscii(nextInt(minLengthInclusive, maxLengthExclusive));
    }

    /**
     * Creates a random string whose length is the number of characters specified.
     *
     * <p>Characters will be chosen from the set of characters which match the POSIX [:graph:]
     * regular expression character class. This class contains all visible ASCII characters
     * (i.e. anything except spaces and control characters).</p>
     *
     * @param count the length of random string to create
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     * @since 3.5
     */
    public static String randomGraph(final int count) {
        return random(count, 33, 126, false, false);
    }

    /**
     * Creates a random string whose length is between the inclusive minimum and
     * the exclusive maximum.
     *
     * <p>Characters will be chosen from the set of \p{Graph} characters.</p>
     *
     * @param minLengthInclusive the inclusive minimum length of the string to generate
     * @param maxLengthExclusive the exclusive maximum length of the string to generate
     * @return the random string
     * @since 3.5
     */
    public static String randomGraph(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomGraph(nextInt(minLengthInclusive, maxLengthExclusive));
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     *
     * <p>Characters will be chosen from the set of numeric
     * characters.</p>
     *
     * @param count the length of random string to create
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String randomNumeric(final int count) {
        return random(count, false, true);
    }

    /**
     * Creates a random string whose length is between the inclusive minimum and
     * the exclusive maximum.
     *
     * <p>Characters will be chosen from the set of \p{Digit} characters.</p>
     *
     * @param minLengthInclusive the inclusive minimum length of the string to generate
     * @param maxLengthExclusive the exclusive maximum length of the string to generate
     * @return the random string
     * @since 3.5
     */
    public static String randomNumeric(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomNumeric(nextInt(minLengthInclusive, maxLengthExclusive));
    }

    /**
     * Creates a random string whose length is the number of characters specified.
     *
     * <p>Characters will be chosen from the set of characters which match the POSIX [:print:]
     * regular expression character class. This class includes all visible ASCII characters and spaces
     * (i.e. anything except control characters).</p>
     *
     * @param count the length of random string to create
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     * @since 3.5
     */
    public static String randomPrint(final int count) {
        return random(count, 32, 126, false, false);
    }


    /**
     * Creates a random string whose length is between the inclusive minimum and
     * the exclusive maximum.
     *
     * <p>Characters will be chosen from the set of \p{Print} characters.</p>
     *
     * @param minLengthInclusive the inclusive minimum length of the string to generate
     * @param maxLengthExclusive the exclusive maximum length of the string to generate
     * @return the random string
     * @since 3.5
     */
    public static String randomPrint(final int minLengthInclusive, final int maxLengthExclusive) {
        return randomPrint(nextInt(minLengthInclusive, maxLengthExclusive));
    }

    public static int nextInt(final int startInclusive, final int endExclusive) {
        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + random().nextInt(endExclusive - startInclusive);
    }

}
