//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.utils;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class StringUtil {
    private static Logger log = Logger.getLogger(StringUtil.class.getName());
    private static StringBuilder buffer = new StringBuilder();
    private static Formatter moneyFormatter;

    public StringUtil() {
    }

    public static String logOutForLabelAndValues(Object... values) {
        if(values != null && values.length != 0) {
            if(values.length % 2 != 0) {
                throw new IllegalArgumentException("values most have even count of elements");
            } else {
                StringBuilder result = new StringBuilder();

                for(int i = 0; i < values.length; ++i) {
                    StringBuilder var10000 = result.append(values[i]).append("=");
                    ++i;
                    var10000.append(values[i]).append(", ");
                }

                return result.length() > 2?result.substring(0, result.length() - 2):"";
            }
        } else {
            return "";
        }
    }

    public static String logOut(Collection<?> collection) {
        if(collection != null && collection.size() != 0) {
            StringBuilder result = new StringBuilder();
            Iterator var2 = collection.iterator();

            while(var2.hasNext()) {
                Object obj = var2.next();
                result.append("\n" + String.valueOf(obj));
            }

            return result.toString();
        } else {
            return "";
        }
    }

    public static String replaceUaCharsToRu(String x) {
        return x == null?null:x.trim().replace('Є', 'Е').replace('є', 'е').replace('І', 'И').replace('і', 'и').replace('\'', 'Ъ').replace('Ї', 'Й').replace('ї', 'й').replace("’", "");
    }

    public static String moneyFormat(Double num) {
        return moneyFormat(num, 2).replace(",", ".");
    }

    public static String arrToString(Enumeration... args) {
        Object[] dest = new Object[args.length];
        System.arraycopy(args, 0, dest, 0, args.length);
        return arrToString(dest);
    }

    public static String arrToString(Object... args) {
        if(args != null && args.length != 0) {
            StringBuilder result = new StringBuilder();
            Object[] var2 = args;
            int var3 = args.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object arg = var2[var4];
                result.append(arg).append(", ");
            }

            return result.substring(0, result.length() - 2);
        } else {
            return "";
        }
    }

    public static String arrToStringNumerated(Object... args) {
        if(args != null && args.length != 0) {
            StringBuilder result = new StringBuilder();

            for(int i = 0; i < args.length; ++i) {
                result.append("[").append(i).append("]=").append(args[i]).append(", ");
            }

            return result.substring(0, result.length() - 2);
        } else {
            return "";
        }
    }

    public static String moneyFormat(Double num, int countDigitAfterSeparator) {
        String result = moneyFormatter.format("%." + countDigitAfterSeparator + "f", new Object[]{new Double(num.doubleValue())}).toString().replace(",", ".");
        buffer.delete(0, buffer.length());
        return result;
    }

    public static String logRequest(long timestamp, Object session, Object request) {
        return "request(" + timestamp + ", " + session + ") " + request;
    }

    public static String logRequest(long timestamp, Object session, Object... requestArgs) {
        return logRequest(timestamp, session, (Object)arrToString(requestArgs));
    }

    public static String logRequest(TimeTotalizer time, Object session, Object... requestArgs) {
        return logRequest(time.getTimestamt(), session, (Object)arrToString(requestArgs));
    }

    public static String logResponse(TimeTotalizer time, Object session, Object response) {
        return logResponse(time.getTimestamt(), session, response);
    }

    public static String logResponse(long timestamp, Object session, Object response) {
        return "response(" + timestamp + ", " + TimeTotalizer.timeAnswerNs(timestamp) + ", " + session + ") " + response;
    }

    public static String logResponse(long timestamp, Object session, Object... responseArgs) {
        return logResponse(timestamp, session, (Object)arrToString(responseArgs));
    }

    public static String logResponse(TimeTotalizer time, Object session, Object... responseArgs) {
        return logResponse(time.getTimestamt(), session, (Object)arrToString(responseArgs));
    }

    public static String logArray(Object... args) {
        return StringUtils.arrayToCommaDelimitedString(args);
    }

    public static boolean compareFioAllPossibleChecks(String expectedRuLName, String expectedRuFName, String expectedUaLName, String expectedUaFName, String expectedEnFio, String actualLastName, String actualFirstName, int allowedNumberErrors, Level logLevel) {
        return compareFioAllPossibleChecks(expectedRuLName, expectedRuFName, "", expectedUaLName, expectedUaFName, "", expectedEnFio, actualLastName, actualFirstName, allowedNumberErrors, logLevel);
    }

    public static boolean compareFioAllPossibleChecks(String expectedRuLName, String expectedRuFName, String expectedUaLName, String expectedUaFName, String expectedEnFio, String actualLastName, String actualFirstName, int allowedNumberErrors) {
        return compareFioAllPossibleChecks(expectedRuLName, expectedRuFName, "", expectedUaLName, expectedUaFName, "", expectedEnFio, actualLastName, actualFirstName, allowedNumberErrors, Level.INFO);
    }

    public static boolean compareFioAllPossibleChecks(String expectedRuLName, String expectedRuFName, String expectedRuPName, String expectedUaLName, String expectedUaFName, String expectedUaPName, String expectedEnFio, String actualLastName, String actualFirstName, int allowedNumberErrors) {
        return compareFioAllPossibleChecks(expectedRuLName, expectedRuFName, "", expectedUaLName, expectedUaFName, "", expectedEnFio, actualLastName, actualFirstName, allowedNumberErrors, Level.INFO);
    }

    public static boolean compareFioAllPossibleChecks(String expectedRuLName, String expectedRuFName, String expectedRuPName, String expectedUaLName, String expectedUaFName, String expectedUaPName, String expectedEnFio, String actualLastName, String actualFirstName, int allowedNumberErrors, Level logLevel) {
        TimeTotalizer time = TimeTotalizer.create();
        Boolean original = null;
        Boolean cutSystemLfname = null;
        Boolean cutSystemFname = null;
        Boolean cutSystemLname = null;
        Boolean withEkbPname = null;
        Boolean onlySystemLname = null;
        Boolean onlySystemFname = null;
        Boolean cutLastWordLname = null;
        Boolean cutLastWordFname = null;

        try {
            original = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, actualFirstName, allowedNumberErrors));
            boolean var28;
            if(original.booleanValue()) {
                var28 = true;
                return var28;
            } else {
                log.log(logLevel, "original stage false");
                cutSystemLfname = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName.split(" ")[0], actualFirstName.split(" ")[0], allowedNumberErrors));
                if(cutSystemLfname.booleanValue()) {
                    var28 = true;
                    return var28;
                } else {
                    cutSystemFname = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, actualFirstName.split(" ")[0], allowedNumberErrors));
                    if(cutSystemFname.booleanValue()) {
                        var28 = true;
                        return var28;
                    } else {
                        cutSystemLname = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName.split(" ")[0], actualFirstName, allowedNumberErrors));
                        if(cutSystemLname.booleanValue()) {
                            var28 = true;
                            return var28;
                        } else {
                            withEkbPname = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName + " " + expectedRuPName, expectedUaLName, expectedUaFName + " " + expectedUaPName, expectedEnFio, actualLastName, actualFirstName, allowedNumberErrors));
                            if(withEkbPname.booleanValue()) {
                                var28 = true;
                                return var28;
                            } else {
                                onlySystemLname = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, "", allowedNumberErrors));
                                if(onlySystemLname.booleanValue()) {
                                    var28 = true;
                                    return var28;
                                } else {
                                    onlySystemFname = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, "", actualFirstName, allowedNumberErrors));
                                    if(onlySystemFname.booleanValue()) {
                                        var28 = true;
                                        return var28;
                                    } else {
                                        log.log(logLevel, "1 stage false");
                                        String[] fNames = actualFirstName.split(" ");
                                        if(fNames.length > 1) {
                                            cutSystemFname = Boolean.valueOf(cutSystemFname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, fNames[1], allowedNumberErrors));
                                        }

                                        if(cutSystemFname.booleanValue()) {
                                            boolean var29 = true;
                                            return var29;
                                        } else {
                                            String[] lNames = actualLastName.split(" ");
                                            if(lNames.length > 1) {
                                                cutSystemLname = Boolean.valueOf(cutSystemLname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, lNames[1], actualFirstName, allowedNumberErrors));
                                            }

                                            boolean var30;
                                            if(cutSystemLname.booleanValue()) {
                                                var30 = true;
                                                return var30;
                                            } else {
                                                log.log(logLevel, "2 stage false");
                                                String cutetLastWordLname;
                                                boolean var24;
                                                if(fNames.length > 2) {
                                                    cutSystemFname = Boolean.valueOf(cutSystemFname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, fNames[0] + " " + fNames[1], allowedNumberErrors));
                                                    cutSystemFname = Boolean.valueOf(cutSystemFname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, fNames[1] + " " + fNames[0], allowedNumberErrors));
                                                    cutSystemFname = Boolean.valueOf(cutSystemFname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, fNames[1] + " " + fNames[2], allowedNumberErrors));
                                                    cutSystemFname = Boolean.valueOf(cutSystemFname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, fNames[2] + " " + fNames[1], allowedNumberErrors));
                                                    if(cutSystemFname.booleanValue()) {
                                                        var30 = true;
                                                        return var30;
                                                    }

                                                    cutetLastWordLname = actualFirstName.substring(0, actualFirstName.lastIndexOf(" "));
                                                    cutLastWordFname = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, actualLastName, cutetLastWordLname, allowedNumberErrors));
                                                    if(cutLastWordFname.booleanValue()) {
                                                        var24 = true;
                                                        return var24;
                                                    }
                                                }

                                                if(lNames.length > 2) {
                                                    cutSystemLname = Boolean.valueOf(cutSystemLname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, lNames[0] + " " + lNames[1], actualFirstName, allowedNumberErrors));
                                                    cutSystemLname = Boolean.valueOf(cutSystemLname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, lNames[1] + " " + lNames[0], actualFirstName, allowedNumberErrors));
                                                    cutSystemLname = Boolean.valueOf(cutSystemLname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, lNames[1] + " " + lNames[2], actualFirstName, allowedNumberErrors));
                                                    cutSystemLname = Boolean.valueOf(cutSystemLname.booleanValue() | compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, lNames[2] + " " + lNames[1], actualFirstName, allowedNumberErrors));
                                                    if(cutSystemLname.booleanValue()) {
                                                        var30 = true;
                                                        return var30;
                                                    }

                                                    cutetLastWordLname = actualLastName.substring(0, actualLastName.lastIndexOf(" "));
                                                    cutLastWordLname = Boolean.valueOf(compareFio(expectedRuLName, expectedRuFName, expectedUaLName, expectedUaFName, expectedEnFio, cutetLastWordLname, actualFirstName, allowedNumberErrors));
                                                    if(cutLastWordLname.booleanValue()) {
                                                        var24 = true;
                                                        return var24;
                                                    }
                                                }

                                                if(logLevel == Level.INFO) {
                                                    log.warning("compare result: false\nexpectedRuLName='" + expectedRuLName + "'\nexpectedRuFName='" + expectedRuFName + "'\nexpectedRuPName='" + expectedRuPName + "'\nexpectedUaLName='" + expectedUaLName + "'\nexpectedUaFName='" + expectedUaFName + "'\nexpectedUaPName='" + expectedUaPName + "'\nexpectedEnFio='" + expectedEnFio + "'\nactualLastName='" + actualLastName + "'\nactualFirstName='" + actualFirstName + "'\nallowedNumberErrors='" + allowedNumberErrors);
                                                }

                                                var30 = false;
                                                return var30;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            log.log(logLevel, "result" + time.checkPointWithResetAsString() + "\noriginal:" + original + "\ncutSystemLfname:" + cutSystemLfname + "\ncutSystemFname:" + cutSystemFname + "\ncutSystemLname:" + cutSystemLname + "\nwithEkbPname:" + withEkbPname + "\nonlySystemLname:" + onlySystemLname + "\nonlySystemFname:" + onlySystemFname + "\ncutLastWordLname:" + cutLastWordLname + "\ncutLastWordFname:" + cutLastWordFname);
        }
    }

    public static boolean compareFio(String expectedRuLName, String expectedRuFName, String expectedUaLName, String expectedUaFName, String expectedEnFio, String actualLastName, String actualFirstName, int allowedNumberErrors) {
        if(compareGateLfNames(expectedRuLName, expectedRuFName, actualLastName, actualFirstName, allowedNumberErrors)) {
            return true;
        } else if(crazyCheckExpectedFio(expectedRuLName, expectedRuFName, allowedNumberErrors, actualLastName + " " + actualFirstName)) {
            return true;
        } else {
            if(expectedUaLName != null && expectedUaFName != null) {
                if(compareGateLfNames(expectedUaLName, expectedUaFName, actualLastName, actualFirstName, allowedNumberErrors)) {
                    return true;
                }

                if(crazyCheckExpectedFio(expectedUaLName, expectedUaFName, allowedNumberErrors, actualLastName + " " + actualFirstName)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static boolean crazyCheckExpectedFio(String expectedLname, String expectedFname, int allowedNumberErrors, String fioActual) {
        String regex = "[ -]+";
        String[] fioParts = fioActual.split("[ -]+");
        return fioParts.length == 0?false:(fioParts.length == 1 && compareGateLfNames(expectedLname, expectedFname, fioParts[0], "", allowedNumberErrors)?true:(fioParts.length == 2 && compareGateLfNames(expectedLname, expectedFname, fioParts[0], fioParts[1], allowedNumberErrors)?true:(fioParts.length != 3 || !compareGateLfNames(expectedLname, expectedFname, fioParts[0], fioParts[1] + fioParts[2], allowedNumberErrors) && !compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1], fioParts[2], allowedNumberErrors)?(fioParts.length != 4 || !compareGateLfNames(expectedLname, expectedFname, fioParts[0], fioParts[1] + fioParts[2] + fioParts[3], allowedNumberErrors) && !compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1], fioParts[2] + fioParts[3], allowedNumberErrors) && !compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1] + fioParts[2], fioParts[3], allowedNumberErrors)?(fioParts.length != 5 || !compareGateLfNames(expectedLname, expectedFname, fioParts[0], fioParts[1] + fioParts[2] + fioParts[3] + fioParts[4], allowedNumberErrors) && !compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1], fioParts[2] + fioParts[3] + fioParts[4], allowedNumberErrors) && !compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1] + fioParts[2], fioParts[3] + fioParts[4], allowedNumberErrors) && !compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1] + fioParts[2] + fioParts[3], fioParts[4], allowedNumberErrors)?(fioParts.length == 6 && (compareGateLfNames(expectedLname, expectedFname, fioParts[0], fioParts[1] + fioParts[2] + fioParts[3] + fioParts[4] + fioParts[5], allowedNumberErrors) || compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1], fioParts[2] + fioParts[3] + fioParts[4] + fioParts[5], allowedNumberErrors) || compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1] + fioParts[2], fioParts[3] + fioParts[4] + fioParts[5], allowedNumberErrors) || compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1] + fioParts[2] + fioParts[3], fioParts[4] + fioParts[5], allowedNumberErrors) || compareGateLfNames(expectedLname, expectedFname, fioParts[0] + fioParts[1] + fioParts[2] + fioParts[3] + fioParts[4], fioParts[5], allowedNumberErrors))?true:fioParts.length > 6 && compareGateLfNames(expectedLname, expectedFname, fioActual.replaceAll("[ -]+", ""), "", allowedNumberErrors)):true):true):true)));
    }

    public static boolean compareGateLfNames(String expectedLastName, String expectedFirstName, String actualLastName, String actualFirstName, int allowedNumberErrors) {
        String expected = expectedLastName + expectedFirstName;
        expected = replaceNotAllowedChars(expected);
        actualLastName = replaceNotAllowedChars(actualLastName);
        actualFirstName = replaceNotAllowedChars(actualFirstName);
        expected = expected.toUpperCase();
        actualLastName = actualLastName.toUpperCase();
        actualFirstName = actualFirstName.toUpperCase();
        String actual = actualLastName + actualFirstName;
        String actualBackwards = actualFirstName + actualLastName;
        if(computeLevenshteinDistance(expected, actual) > allowedNumberErrors && computeLevenshteinDistance(expected, actualBackwards) > allowedNumberErrors) {
            expected = CharConverter.getEnString(expected);
            actual = CharConverter.getEnString(actual);
            actualBackwards = CharConverter.getEnString(actualBackwards);
            if(computeLevenshteinDistance(expected, actual) > allowedNumberErrors && computeLevenshteinDistance(expected, actualBackwards) > allowedNumberErrors) {
                expected = replaceSomeChars(expected);
                actual = replaceSomeChars(actual);
                actualBackwards = replaceSomeChars(actualBackwards);
                return computeLevenshteinDistance(expected, actual) <= allowedNumberErrors || computeLevenshteinDistance(expected, actualBackwards) <= allowedNumberErrors;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private static String replaceNotAllowedChars(String str) {
        return str == null?null:str.replace(" ", "").replace("-", "").replace("ё", "e").replace("Ё", "E");
    }

    private static String replaceSomeChars(String str) {
        return str == null?null:str.replace("PH", "F").replace("Y", "I").replace("ph", "f").replace("y", "i");
    }

    public static String removeAllWithoutLatinAndSpace(String str) {
        return str == null?null:str.replaceAll("[^a-z^A-Z^ ]", "");
    }

    public static int computeLevenshteinDistance(String str1, String str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        int i;
        for(i = 0; i <= str1.length(); distance[i][0] = i++) {
            ;
        }

        for(i = 0; i <= str2.length(); distance[0][i] = i++) {
            ;
        }

        for(i = 1; i <= str1.length(); ++i) {
            for(int j = 1; j <= str2.length(); ++j) {
                distance[i][j] = min(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1)?0:1));
            }
        }

        return distance[str1.length()][str2.length()];
    }

    public static String replaceForDbLikePresentation(String str) {
        return str != null && !str.equals("")?str.replace('*', '%'):"%";
    }

    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static String showOnlyStartAndEnd(String input, String delimiter, int showCount) {
        if(input == null && showCount < 0) {
            return null;
        } else {
            int numSize = input.length();
            StringBuilder result = new StringBuilder();
            String showEnd;
            if(numSize >= showCount) {
                showEnd = input.substring(0, showCount);
                result.append(showEnd).append(delimiter);
            }

            if(numSize >= showCount + showCount) {
                showEnd = input.substring(numSize - showCount, numSize);
                result.append(showEnd);
            }

            return result.toString();
        }
    }

    public static String upperCase(String str) {
        return str == null?null:str.toUpperCase();
    }

    public static String extractBetween(String line, String before, String after) {
        if(!VerifyUtil.isEmpty(line) && before != null && after != null) {
            String[] splitBefore = line.split(before);
            if(splitBefore.length < 2) {
                return null;
            } else {
                String result = splitBefore[1];
                String[] splitAfter = result.split(after);
                if(splitAfter.length < 1) {
                    return null;
                } else {
                    result = splitAfter[0];
                    return result;
                }
            }
        } else {
            return null;
        }
    }

    public static String randomString(int sizeFrom, int sizeTo, Character... chars) {
        if(chars == null) {
            return null;
        } else if(sizeFrom > sizeTo) {
            throw new IllegalArgumentException(sizeFrom + ":" + sizeTo);
        } else {
            StringBuilder result = new StringBuilder();
            int resultSize = (new Random()).nextInt(sizeTo - sizeFrom);
            List<Character> allowedChars = allowedChars(chars);

            for(int i = 0; i < sizeFrom + resultSize; ++i) {
                result.append(randomCharFromAllowed(allowedChars));
            }

            return result.toString();
        }
    }

    public static Character randomCharFromAllowed(List<Character> allowedChars) {
        if(allowedChars == null) {
            throw new NullPointerException("allowedChars must be not null");
        } else {
            Random r = new Random();
            return (Character)allowedChars.get(r.nextInt(allowedChars.size()));
        }
    }

    public static Character randomChar(Character... chars) {
        if(chars == null) {
            return null;
        } else {
            Random r = new Random();
            List<Character> allowedChars = allowedChars(chars);
            return (Character)allowedChars.get(r.nextInt(allowedChars.size()));
        }
    }

    public static String addRandomPrefix(String sourceBase, int totalSize, Character... randomCharSetPairs) {
        if(sourceBase == null) {
            sourceBase = "";
        }

        if(sourceBase.length() >= totalSize) {
            return sourceBase.substring(0, totalSize);
        } else {
            int randomPartSize = totalSize - sourceBase.length();
            StringBuilder result = new StringBuilder();

            for(int i = 0; i < randomPartSize; ++i) {
                result.append(randomChar(randomCharSetPairs));
            }

            result.append(sourceBase);
            return result.toString();
        }
    }

    public static List<Character> allowedChars(List<Character> charsArr, Character... chars) {
        List<Character> characters = allowedChars(chars);
        characters.addAll(charsArr);
        return characters;
    }

    public static List<Character> allowedChars(Character... chars) {
        List<Character> allowedChar = new ArrayList();

        for(int it = 0; it < chars.length; ++it) {
            char from = chars[it].charValue();
            if(it + 1 == chars.length) {
                throw new IllegalArgumentException("wrong scope of chars: not paired border. From: " + from);
            }

            ++it;
            char to = chars[it].charValue();
            if(from > to) {
                throw new IllegalArgumentException("wrong scope of chars: from > to. From: " + from + ", to: " + to);
            }

            for(int i = from; i <= to; ++i) {
                allowedChar.add(Character.valueOf((char)i));
            }
        }

        return allowedChar;
    }

    public static String getSortedStringByWords(String str) {
        if(str == null) {
            return null;
        } else {
            String[] arr = str.split(" ");
            Arrays.sort(arr);
            String result = "";
            String[] var3 = arr;
            int var4 = arr.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String string = var3[var5];
                String trimed = string.trim();
                if(trimed.length() != 0) {
                    result = result + trimed + " ";
                }
            }

            return result.trim();
        }
    }

    public static String getCleanedSortedENString(String str) {
        str = CharConverter.getEnString(str).toUpperCase();
        str = removeAllWithoutLatinAndSpace(str);
        str = getSortedStringByWords(str);
        return str;
    }

    public static String hidePassword(String body) {
        return hideExpressions(body, new String[]{"password", "Password"}, "*****");
    }

    public static String hideExpressions(String body, String[] hideExpressions) {
        return hideExpressions(body, hideExpressions, "*****");
    }

    public static String hideExpressions(String body, String[] hideExpressions, String mask) {
        if(body != null) {
            String expressionE = null;

            try {
                String[] var5 = hideExpressions;
                int var6 = hideExpressions.length;

                label82:
                for(int var7 = 0; var7 < var6; ++var7) {
                    String expression = var5[var7];
                    if(body.contains(expression)) {
                        int startSearch = 0;
                        int var10 = 0;

                        while(true) {
                            while(true) {
                                if(startSearch >= body.length()) {
                                    continue label82;
                                }

                                ++var10;
                                int indExpression = body.indexOf(expression, startSearch);
                                if(indExpression > 0 && body.charAt(indExpression - 1) == 47) {
                                    startSearch = indExpression + expression.length();
                                } else {
                                    if(indExpression == -1) {
                                        continue label82;
                                    }

                                    int indExpressionStart = indExpression;
                                    indExpression += expression.length();
                                    if(indExpression > 0 && VerifyUtil.in(Character.valueOf(body.charAt(indExpressionStart - 1)), new Object[]{Character.valueOf(' '), Character.valueOf('\''), Character.valueOf('"'), Character.valueOf('<')}) && VerifyUtil.in(Character.valueOf(body.charAt(indExpression)), new Object[]{Character.valueOf(' '), Character.valueOf('\''), Character.valueOf('"'), Character.valueOf('>'), Character.valueOf('=')})) {
                                        int indBeginPasValue = body.indexOf("\"", indExpression - 1);
                                        if(indBeginPasValue > 0 && body.charAt(indBeginPasValue + 1) == 58) {
                                            indBeginPasValue += 2;
                                        }

                                        int indEndPasValue = body.indexOf("\"", indBeginPasValue + 1);
                                        if(indBeginPasValue > indExpression && indBeginPasValue - indExpression > 5 || indBeginPasValue == -1) {
                                            indBeginPasValue = body.indexOf(">", indExpression - 1);
                                            if(indBeginPasValue > 0 && body.charAt(indBeginPasValue - 1) == 47) {
                                                startSearch = indBeginPasValue + 1;
                                                continue;
                                            }

                                            indEndPasValue = body.indexOf("<", indBeginPasValue + 1);
                                        }

                                        if(indBeginPasValue >= 0 && indEndPasValue >= 0 && indBeginPasValue <= indEndPasValue) {
                                            String bodyBeforePasValue = body.substring(0, indBeginPasValue);
                                            String bodyStartWithPasValue = body.substring(indBeginPasValue);
                                            String password = body.substring(indBeginPasValue + 1, indEndPasValue).replaceAll("\"", "");
                                            bodyStartWithPasValue = bodyStartWithPasValue.replaceFirst(password, mask);
                                            body = bodyBeforePasValue + bodyStartWithPasValue;
                                            startSearch = indBeginPasValue + mask.length();
                                        } else {
                                            startSearch = indExpression;
                                        }
                                    } else {
                                        startSearch = indExpression;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception var18) {
                log.log(Level.SEVERE, "Error by hide expressions" + expressionE, var18);
                return body;
            }
        }

        return body;
    }

    public static String hideData(String body, String contentType) {
        String result = body;
        String mask = "*****";

        try {
            if(body != null) {
                StringBuilder buffer = new StringBuilder();
                String content = null;
                if(VerifyUtil.isEmpty(contentType)) {
                    contentType = "unknown";
                }

                if(!"application/xml;charset=UTF-8".contains(contentType) && !"application/xhtml+xml;charset=UTF-8".contains(contentType)) {
                    if("application/json;charset=UTF-8".contains(contentType)) {
                        content = "json";
                    } else if(content == null) {
                        if(body.indexOf("<?") != -1 && body.indexOf("?>") != -1) {
                            content = "xml";
                        } else {
                            content = "json";
                        }
                    }
                } else {
                    content = "xml";
                }

                int i;
                int index;
                int end;
                int begin;
                String str;
                String append;
                if("xml".equals(content)) {
                    int beginHead = body.indexOf("<?");
                    i = body.indexOf("?>");
                    if(beginHead != -1 && i != -1) {
                        buffer.append(body.substring(beginHead, i + 1));
                        index = body.length();
                        body = body.replace(body.substring(beginHead, i + 1), "");
                        end = body.length();
                        if(index == end) {
                            throw new IllegalArgumentException();
                        }
                    }

                    while(body.length() != 0) {
                        index = body.indexOf(">");
                        end = body.indexOf("<");
                        int lenthBefor;
                        int lenthAfter;
                        if(index > end) {
                            if(end == -1) {
                                buffer.append(">");
                                break;
                            }

                            buffer.append(body.substring(end, index));
                            lenthBefor = body.length();
                            body = replaceFirst(body, body.substring(end, index), "");
                            lenthAfter = body.length();
                            if(lenthBefor == lenthAfter) {
                                throw new IllegalArgumentException();
                            }
                        } else if(end - index == 1) {
                            buffer.append(body.substring(index, end));
                            lenthBefor = body.length();
                            body = replaceFirst(body, body.substring(index, end), "");
                            lenthAfter = body.length();
                            if(lenthBefor == lenthAfter) {
                                throw new IllegalArgumentException();
                            }
                        } else {
                            str = body.substring(index + 1, end);
                            append = body.substring(index, end);
                            begin = body.length();
                            body = replaceFirst(body, append, "");
                            end = body.length();
                            if(begin == end) {
                                throw new IllegalArgumentException();
                            }

                            buffer.append(append.replace(str, mask));
                        }
                    }

                    result = buffer.toString();
                } else if("json".equals(content)) {
                    mask = "\"*****\"";
                    String[] split = body.split(",");
                    String[] var17 = split;
                    index = split.length;

                    for(end = 0; end < index; ++end) {
                        str = var17[end];
                        str = str + ",";
                        append = (new StringBuilder(str)).reverse().toString();
                        begin = append.indexOf(",");
                        end = append.indexOf(":\"");
                        if(end == -1) {
                            end = append.indexOf("\"");
                        }

                        if(begin != -1 && end != -1) {
                            String regex = (new StringBuilder(append.substring(begin, end))).reverse().toString().replace("}]", "").replace("}},", "").replace("[", "");
                            if(",".equals(regex.trim())) {
                                buffer.append(mask + ",");
                            } else if("],".equals(regex.trim())) {
                                buffer.append(mask + "],");
                            } else if("]},".equals(regex.trim())) {
                                buffer.append(mask + "]},");
                            } else {
                                str = replaceFirst(str, regex, mask + ",");
                                buffer.append(str);
                            }
                        }
                    }

                    for(i = 0; i < 2; ++i) {
                        index = buffer.lastIndexOf(",");
                        if(index > 0) {
                            buffer.deleteCharAt(index);
                        }
                    }

                    buffer.append("}");
                    result = buffer.toString();
                }
            }
        } catch (Exception var15) {
            log.log(Level.SEVERE, "", var15);
        }

        return result;
    }

    private static String replaceFirst(String replacemrnt, String regex, String replacement) {
        return Pattern.compile(regex, 16).matcher(replacemrnt).replaceFirst(replacement);
    }

    public static String guid() {
        return UUID.randomUUID().toString();
    }

    public static String normalizePhone(String phone) {
        return VerifyUtil.isEmpty(phone)?phone:(phone.startsWith("380") && phone.length() == 12?"+" + phone:(phone.startsWith("80") && phone.length() == 11?"+3" + phone:(phone.startsWith("0") && phone.length() == 10?"+38" + phone:phone.replaceAll("[\\(\\ \\)\\-\\sa-bA-B]{1,}", ""))));
    }

    public static String stringForCharset(String text, String charsetName) {
        return stringForCharset(text, Charset.forName(charsetName), "?");
    }

    public static String stringForCharset(String text, String charsetName, String replacement) {
        return stringForCharset(text, Charset.forName(charsetName), replacement);
    }

    public static String stringForCharset(String text, Charset charset, String replacement) {
        if(text == null) {
            return null;
        } else if(charset != null && replacement != null) {
            CharsetEncoder encoder = charset.newEncoder();

            for(int i = 0; i < text.length(); ++i) {
                char eachChar = text.charAt(i);
                if(!encoder.canEncode(eachChar)) {
                    text = text.replace(String.valueOf(eachChar), replacement);
                }
            }

            return text;
        } else {
            throw new NullPointerException("Charset and replacement cannot be null: " + charset + ", " + replacement);
        }
    }

    public static byte[] bytes(String st) {
        try {
            return st.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalStateException(var2);
        }
    }

    public static String stuff(String stuff, int count) {
        StringBuilder sb = new StringBuilder(count);

        for(int i = 0; i < count; ++i) {
            sb.append(stuff);
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String expectedRuLName = "Уваркинє";
        String expectedRuFName = "Наталия";
        String expectedRuPName = "";
        String expectedUaLName = "";
        String expectedUaFName = "";
        String expectedUaPName = "";
        String expectedEnFio = "";
        String actualLastName = "UVARKINA";
        String actualFirstName = "NATALyA";
        int allowedNumberErrors = 3;
        boolean res = false;
        res = compareFioAllPossibleChecks(expectedRuLName, expectedRuFName, expectedRuPName, expectedUaLName, expectedUaFName, expectedUaPName, expectedEnFio, actualLastName, actualFirstName, allowedNumberErrors);
        log.info(String.valueOf(res));
        log.info(String.valueOf(CharConverter.getEnString(expectedRuLName)));
        log.info(String.valueOf(CharConverter.getEnString(expectedRuFName)));
        log.info(String.valueOf(actualFirstName));
        log.info(String.valueOf(actualLastName));
    }

    public static DateFormat expiryDayFormat() {
        return new SimpleDateFormat("MMyy");
    }

    static {
        moneyFormatter = new Formatter(buffer);
    }
}
