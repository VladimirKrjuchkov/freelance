//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pb.tel.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class CharConverter {
    public static final String PATTERN_RU_DOC_GIVER = "[a-z[A-Z]а-я[А-Я][0-9]їЇіІёЁ.[-][,];:'\"№/\\\\—_()|{}&!?^~`%#$[*] ]*";
    private static final String PATTERN_RU = "[а-я[А-Я]їЇіІёЁ[-] ]*";
    private static final String PATTERN_EN = "[a-z[A-Z][-] ]*";
    private static final String PATTERN_GE = "[ა-ჰ[-] ]*";
    private static Map<Character, String> charsGe2Ru = getCharsGe2Ru();
    private static Map<Character, String> charsGe2En = getCharsGe2En();
    private static Map<Character, String> charsEn2Ge = getCharsEn2Ge();
    private static Map<Character, String> charsRu2Ge = getCharsRu2Ge();
    private static Map<Character, String> charsUa2Ru = getCharsUa2RU();
    private static Map<Character, String> charsUa2RuByRules = getCharsUa2RuByRules();
    private static Map<Character, String> charsRu2En = getCharsRu2En();

    public CharConverter() {
    }

    public static String ge2En(String input) {
        return convert(input, charsGe2En);
    }

    public static String en2Ge(String input) {
        return convert(input, charsEn2Ge);
    }

    public static String ge2Ru(String input) {
        return convert(input, charsGe2Ru);
    }

    public static String ru2Ge(String input) {
        return convert(input, charsRu2Ge);
    }

    public static String en2Ru(String input) {
        return convert(input, charsGe2Ru);
    }

    public static String ru2En(String input) {
        return convert(input, charsRu2En);
    }

    public static String Ua2Ru(String input) {
        return convert(input, charsUa2Ru);
    }

    public static String Ua2RuByRules(String input) {
        return convert(input, charsUa2RuByRules);
    }

    public static boolean hasGe(String input) {
        Set<Character> geChars = getCharsGe2En().keySet();
        char[] var2 = input.toCharArray();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Character ch = Character.valueOf(var2[var4]);
            if(geChars.contains(ch)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isGe(String input) {
        Pattern ruChars = Pattern.compile("[ა-ჰ[-] ]*");
        return ruChars.matcher(input).matches();
    }

    public static boolean isRu(String input) {
        Pattern ruChars = Pattern.compile("[а-я[А-Я]їЇіІёЁ[-] ]*");
        return ruChars.matcher(input).matches();
    }

    public static boolean isEn(String input) {
        Pattern enChars = Pattern.compile("[a-z[A-Z][-] ]*");
        return enChars.matcher(input).matches();
    }

    public static boolean isUARuEn(String inputSource) {
        String inputAsEn = ru2En(inputSource);
        if(isEn(inputAsEn)) {
            return true;
        } else {
            String inputAsRu = en2Ru(inputSource);
            return isRu(inputAsRu);
        }
    }

    public static String getRuString(String input) {
        return isRu(input)?input:en2Ru(ge2Ru(input));
    }

    public static String getGeString(String input) {
        return isGe(input)?input:ru2Ge(en2Ge(input));
    }

    public static String getEnString(String input) {
        return ru2En(ge2En(input));
    }

    public static String getNotGeString(String input) {
        return !hasGe(input)?input:ge2En(input);
    }

    private static String convert(String input, Map<Character, String> chars) {
        if(input == null) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();

            for(int i = 0; i < input.length(); ++i) {
                if(chars.get(Character.valueOf(input.charAt(i))) == null) {
                    result.append(input.charAt(i));
                } else {
                    result.append((String)chars.get(Character.valueOf(input.charAt(i))));
                }
            }

            return result.toString();
        }
    }

    private static Map<Character, String> getCharsGe2En() {
        return getCharsGe2En(false);
    }

    private static Map<Character, String> getCharsGe2En(boolean ifNeedCompare) {
        if(charsGe2En != null) {
            return charsGe2En;
        } else {
            charsGe2En = getMapInstance();
            charsGe2En.put(Character.valueOf('ა'), "A");
            charsGe2En.put(Character.valueOf('ბ'), "B");
            charsGe2En.put(Character.valueOf('გ'), "G");
            charsGe2En.put(Character.valueOf('დ'), "D");
            charsGe2En.put(Character.valueOf('ე'), "E");
            charsGe2En.put(Character.valueOf('ვ'), "V");
            charsGe2En.put(Character.valueOf('ზ'), "Z");
            charsGe2En.put(Character.valueOf('თ'), "TH");
            charsGe2En.put(Character.valueOf('ი'), "I");
            charsGe2En.put(Character.valueOf('კ'), "K");
            charsGe2En.put(Character.valueOf('ლ'), "L");
            charsGe2En.put(Character.valueOf('მ'), "M");
            charsGe2En.put(Character.valueOf('ნ'), "N");
            charsGe2En.put(Character.valueOf('ო'), "O");
            charsGe2En.put(Character.valueOf('პ'), "P");
            charsGe2En.put(Character.valueOf('ჟ'), "ZH");
            charsGe2En.put(Character.valueOf('რ'), "R");
            charsGe2En.put(Character.valueOf('ს'), "S");
            charsGe2En.put(Character.valueOf('ტ'), "T");
            charsGe2En.put(Character.valueOf('უ'), "U");
            charsGe2En.put(Character.valueOf('ფ'), "PH");
            charsGe2En.put(Character.valueOf('ქ'), "Q");
            charsGe2En.put(Character.valueOf('ღ'), "GH");
            charsGe2En.put(Character.valueOf('ყ'), "GHK");
            charsGe2En.put(Character.valueOf('შ'), "SH");
            charsGe2En.put(Character.valueOf('ჩ'), "CH");
            charsGe2En.put(Character.valueOf('ც'), "TS");
            charsGe2En.put(Character.valueOf('ძ'), "DZ");
            charsGe2En.put(Character.valueOf('წ'), "TZ");
            charsGe2En.put(Character.valueOf('ჭ'), "TCH");
            charsGe2En.put(Character.valueOf('ხ'), "KH");
            charsGe2En.put(Character.valueOf('ჯ'), "J");
            charsGe2En.put(Character.valueOf('ჰ'), "H");
            charsGe2En.put(Character.valueOf('№'), "N");
            if(ifNeedCompare) {
                charsGe2En.put(Character.valueOf(' '), "");
                charsGe2En.put(Character.valueOf('-'), "");
            }

            return charsGe2En;
        }
    }

    private static Map<Character, String> getCharsRu2En() {
        return getCharsRu2En(false);
    }

    private static Map<Character, String> getCharsRu2En(boolean ifNeedCompare) {
        if(charsRu2En != null) {
            return charsRu2En;
        } else {
            charsRu2En = getMapInstance();
            charsRu2En.put(Character.valueOf('А'), "A");
            charsRu2En.put(Character.valueOf('Б'), "B");
            charsRu2En.put(Character.valueOf('В'), "V");
            charsRu2En.put(Character.valueOf('Г'), "G");
            charsRu2En.put(Character.valueOf('Д'), "D");
            charsRu2En.put(Character.valueOf('Е'), "E");
            charsRu2En.put(Character.valueOf('Ё'), "E");
            charsRu2En.put(Character.valueOf('Ж'), "ZH");
            charsRu2En.put(Character.valueOf('З'), "Z");
            charsRu2En.put(Character.valueOf('И'), "I");
            charsRu2En.put(Character.valueOf('Й'), "Y");
            charsRu2En.put(Character.valueOf('К'), "K");
            charsRu2En.put(Character.valueOf('Л'), "L");
            charsRu2En.put(Character.valueOf('М'), "M");
            charsRu2En.put(Character.valueOf('Н'), "N");
            charsRu2En.put(Character.valueOf('О'), "O");
            charsRu2En.put(Character.valueOf('П'), "P");
            charsRu2En.put(Character.valueOf('Р'), "R");
            charsRu2En.put(Character.valueOf('С'), "S");
            charsRu2En.put(Character.valueOf('Т'), "T");
            charsRu2En.put(Character.valueOf('У'), "U");
            charsRu2En.put(Character.valueOf('Ф'), "F");
            charsRu2En.put(Character.valueOf('Х'), "KH");
            charsRu2En.put(Character.valueOf('Ц'), "TS");
            charsRu2En.put(Character.valueOf('Ч'), "CH");
            charsRu2En.put(Character.valueOf('Ш'), "SH");
            charsRu2En.put(Character.valueOf('Щ'), "SCH");
            charsRu2En.put(Character.valueOf('Ы'), "Y");
            charsRu2En.put(Character.valueOf('І'), "I");
            charsRu2En.put(Character.valueOf('Ї'), "Y");
            charsRu2En.put(Character.valueOf('Є'), "E");
            charsRu2En.put(Character.valueOf('Ь'), "");
            charsRu2En.put(Character.valueOf('Ъ'), "");
            charsRu2En.put(Character.valueOf('Э'), "E");
            charsRu2En.put(Character.valueOf('Ю'), "YU");
            charsRu2En.put(Character.valueOf('Я'), "YA");
            charsRu2En.put(Character.valueOf('а'), "a");
            charsRu2En.put(Character.valueOf('б'), "b");
            charsRu2En.put(Character.valueOf('в'), "v");
            charsRu2En.put(Character.valueOf('г'), "g");
            charsRu2En.put(Character.valueOf('д'), "d");
            charsRu2En.put(Character.valueOf('е'), "e");
            charsRu2En.put(Character.valueOf('ё'), "e");
            charsRu2En.put(Character.valueOf('ж'), "zh");
            charsRu2En.put(Character.valueOf('з'), "z");
            charsRu2En.put(Character.valueOf('и'), "i");
            charsRu2En.put(Character.valueOf('й'), "y");
            charsRu2En.put(Character.valueOf('к'), "k");
            charsRu2En.put(Character.valueOf('л'), "l");
            charsRu2En.put(Character.valueOf('м'), "m");
            charsRu2En.put(Character.valueOf('н'), "n");
            charsRu2En.put(Character.valueOf('о'), "o");
            charsRu2En.put(Character.valueOf('п'), "p");
            charsRu2En.put(Character.valueOf('р'), "r");
            charsRu2En.put(Character.valueOf('с'), "s");
            charsRu2En.put(Character.valueOf('т'), "t");
            charsRu2En.put(Character.valueOf('у'), "u");
            charsRu2En.put(Character.valueOf('ф'), "f");
            charsRu2En.put(Character.valueOf('х'), "kh");
            charsRu2En.put(Character.valueOf('ц'), "ts");
            charsRu2En.put(Character.valueOf('ч'), "ch");
            charsRu2En.put(Character.valueOf('ш'), "sh");
            charsRu2En.put(Character.valueOf('щ'), "sch");
            charsRu2En.put(Character.valueOf('ы'), "y");
            charsRu2En.put(Character.valueOf('і'), "i");
            charsRu2En.put(Character.valueOf('ї'), "y");
            charsRu2En.put(Character.valueOf('\''), "");
            charsRu2En.put(Character.valueOf('`'), "");
            charsRu2En.put(Character.valueOf('’'), "");
            charsRu2En.put(Character.valueOf('є'), "e");
            charsRu2En.put(Character.valueOf('ь'), "");
            charsRu2En.put(Character.valueOf('ъ'), "");
            charsRu2En.put(Character.valueOf('э'), "e");
            charsRu2En.put(Character.valueOf('ю'), "yu");
            charsRu2En.put(Character.valueOf('я'), "ya");
            charsRu2En.put(Character.valueOf('№'), "N");
            if(ifNeedCompare) {
                charsRu2En.put(Character.valueOf(' '), "");
                charsRu2En.put(Character.valueOf('-'), "");
                charsRu2En.put(Character.valueOf('\''), "");
            }

            return charsRu2En;
        }
    }

    private static Map<Character, String> getCharsEn2Ge() {
        if(charsEn2Ge != null) {
            return charsEn2Ge;
        } else {
            charsEn2Ge = getMapInstance();
            charsEn2Ge.put(Character.valueOf('A'), "ა");
            charsEn2Ge.put(Character.valueOf('B'), "ბ");
            charsEn2Ge.put(Character.valueOf('C'), "ც");
            charsEn2Ge.put(Character.valueOf('D'), "დ");
            charsEn2Ge.put(Character.valueOf('E'), "ე");
            charsEn2Ge.put(Character.valueOf('F'), "ფ");
            charsEn2Ge.put(Character.valueOf('G'), "გ");
            charsEn2Ge.put(Character.valueOf('H'), "ჰ");
            charsEn2Ge.put(Character.valueOf('I'), "ი");
            charsEn2Ge.put(Character.valueOf('J'), "ჯ");
            charsEn2Ge.put(Character.valueOf('K'), "კ");
            charsEn2Ge.put(Character.valueOf('L'), "ლ");
            charsEn2Ge.put(Character.valueOf('M'), "მ");
            charsEn2Ge.put(Character.valueOf('N'), "ნ");
            charsEn2Ge.put(Character.valueOf('O'), "ო");
            charsEn2Ge.put(Character.valueOf('P'), "პ");
            charsEn2Ge.put(Character.valueOf('Q'), "ქ");
            charsEn2Ge.put(Character.valueOf('R'), "რ");
            charsEn2Ge.put(Character.valueOf('S'), "ს");
            charsEn2Ge.put(Character.valueOf('T'), "ტ");
            charsEn2Ge.put(Character.valueOf('U'), "უ");
            charsEn2Ge.put(Character.valueOf('V'), "ვ");
            charsEn2Ge.put(Character.valueOf('W'), "ვ");
            charsEn2Ge.put(Character.valueOf('X'), "ხ");
            charsEn2Ge.put(Character.valueOf('Y'), "");
            charsEn2Ge.put(Character.valueOf('Z'), "ზ");
            Iterator var0 = charsEn2Ge.entrySet().iterator();

            while(var0.hasNext()) {
                Entry<Character, String> row = (Entry)var0.next();
                charsEn2Ge.put(Character.valueOf(Character.toLowerCase(((Character)row.getKey()).charValue())), row.getValue());
            }

            return charsEn2Ge;
        }
    }

    private static Map<Character, String> getCharsGe2Ru() {
        if(charsGe2Ru != null) {
            return charsGe2Ru;
        } else {
            charsGe2Ru = getMapInstance();
            charsGe2Ru.put(Character.valueOf('ა'), "А");
            charsGe2Ru.put(Character.valueOf('ბ'), "Б");
            charsGe2Ru.put(Character.valueOf('გ'), "Г");
            charsGe2Ru.put(Character.valueOf('დ'), "Д");
            charsGe2Ru.put(Character.valueOf('ე'), "Е");
            charsGe2Ru.put(Character.valueOf('ვ'), "В");
            charsGe2Ru.put(Character.valueOf('ზ'), "З");
            charsGe2Ru.put(Character.valueOf('თ'), "Т");
            charsGe2Ru.put(Character.valueOf('ი'), "И");
            charsGe2Ru.put(Character.valueOf('კ'), "К");
            charsGe2Ru.put(Character.valueOf('ლ'), "Л");
            charsGe2Ru.put(Character.valueOf('მ'), "М");
            charsGe2Ru.put(Character.valueOf('ნ'), "Н");
            charsGe2Ru.put(Character.valueOf('ო'), "О");
            charsGe2Ru.put(Character.valueOf('პ'), "П");
            charsGe2Ru.put(Character.valueOf('ჟ'), "Ж");
            charsGe2Ru.put(Character.valueOf('რ'), "Р");
            charsGe2Ru.put(Character.valueOf('ს'), "С");
            charsGe2Ru.put(Character.valueOf('ტ'), "Т");
            charsGe2Ru.put(Character.valueOf('უ'), "У");
            charsGe2Ru.put(Character.valueOf('ფ'), "Ф");
            charsGe2Ru.put(Character.valueOf('ქ'), "К");
            charsGe2Ru.put(Character.valueOf('ღ'), "Г");
            charsGe2Ru.put(Character.valueOf('ყ'), "К");
            charsGe2Ru.put(Character.valueOf('შ'), "Ш");
            charsGe2Ru.put(Character.valueOf('ჩ'), "Ч");
            charsGe2Ru.put(Character.valueOf('ც'), "Ц");
            charsGe2Ru.put(Character.valueOf('ძ'), "ДЗ");
            charsGe2Ru.put(Character.valueOf('წ'), "Ц");
            charsGe2Ru.put(Character.valueOf('ჭ'), "Ч");
            charsGe2Ru.put(Character.valueOf('ხ'), "Х");
            charsGe2Ru.put(Character.valueOf('ჯ'), "ДЖ");
            charsGe2Ru.put(Character.valueOf('ჰ'), "Г");
            return charsGe2Ru;
        }
    }

    private static Map<Character, String> getCharsRu2Ge() {
        if(charsRu2Ge != null) {
            return charsRu2Ge;
        } else {
            charsRu2Ge = getMapInstance();
            charsRu2Ge.put(Character.valueOf('А'), "ა");
            charsRu2Ge.put(Character.valueOf('Б'), "ბ");
            charsRu2Ge.put(Character.valueOf('В'), "ვ");
            charsRu2Ge.put(Character.valueOf('Г'), "გ");
            charsRu2Ge.put(Character.valueOf('Д'), "დ");
            charsRu2Ge.put(Character.valueOf('Е'), "ე");
            charsRu2Ge.put(Character.valueOf('Ё'), "ე");
            charsRu2Ge.put(Character.valueOf('Ж'), "ჟ");
            charsRu2Ge.put(Character.valueOf('З'), "ზ");
            charsRu2Ge.put(Character.valueOf('И'), "ი");
            charsRu2Ge.put(Character.valueOf('Й'), "ი");
            charsRu2Ge.put(Character.valueOf('К'), "კ");
            charsRu2Ge.put(Character.valueOf('Л'), "ლ");
            charsRu2Ge.put(Character.valueOf('М'), "მ");
            charsRu2Ge.put(Character.valueOf('Н'), "ნ");
            charsRu2Ge.put(Character.valueOf('О'), "ო");
            charsRu2Ge.put(Character.valueOf('П'), "პ");
            charsRu2Ge.put(Character.valueOf('Р'), "რ");
            charsRu2Ge.put(Character.valueOf('С'), "ს");
            charsRu2Ge.put(Character.valueOf('Т'), "ტ");
            charsRu2Ge.put(Character.valueOf('У'), "უ");
            charsRu2Ge.put(Character.valueOf('Ф'), "ფ");
            charsRu2Ge.put(Character.valueOf('Х'), "ხ");
            charsRu2Ge.put(Character.valueOf('Ц'), "ც");
            charsRu2Ge.put(Character.valueOf('Ч'), "ჩ");
            charsRu2Ge.put(Character.valueOf('Ш'), "შ");
            charsRu2Ge.put(Character.valueOf('Щ'), "შ");
            charsRu2Ge.put(Character.valueOf('Ъ'), "");
            charsRu2Ge.put(Character.valueOf('Ы'), "");
            charsRu2Ge.put(Character.valueOf('Ь'), "");
            charsRu2Ge.put(Character.valueOf('Э'), "ე");
            charsRu2Ge.put(Character.valueOf('Ю'), "იუ");
            charsRu2Ge.put(Character.valueOf('Я'), "ია");
            Iterator var0 = charsRu2Ge.entrySet().iterator();

            while(var0.hasNext()) {
                Entry<Character, String> row = (Entry)var0.next();
                charsRu2Ge.put(Character.valueOf(Character.toLowerCase(((Character)row.getKey()).charValue())), row.getValue());
            }

            return charsRu2Ge;
        }
    }

    private static Map<Character, String> getCharsUa2RuByRules() {
        if(charsUa2RuByRules != null) {
            return charsUa2RuByRules;
        } else {
            charsUa2RuByRules = getMapInstance();
            charsUa2RuByRules.put(Character.valueOf('А'), "А");
            charsUa2RuByRules.put(Character.valueOf('Б'), "Б ");
            charsUa2RuByRules.put(Character.valueOf('В'), "В");
            charsUa2RuByRules.put(Character.valueOf('Г'), "Г");
            charsUa2RuByRules.put(Character.valueOf('Ґ'), "Г");
            charsUa2RuByRules.put(Character.valueOf('Д'), "Д");
            charsUa2RuByRules.put(Character.valueOf('Е'), "Э");
            charsUa2RuByRules.put(Character.valueOf('Є'), "Е");
            charsUa2RuByRules.put(Character.valueOf('Ж'), "Ж");
            charsUa2RuByRules.put(Character.valueOf('З'), "З");
            charsUa2RuByRules.put(Character.valueOf('И'), "Ы");
            charsUa2RuByRules.put(Character.valueOf('І'), "И");
            charsUa2RuByRules.put(Character.valueOf('Ї'), "Е");
            charsUa2RuByRules.put(Character.valueOf('Й'), "Й");
            charsUa2RuByRules.put(Character.valueOf('К'), "К");
            charsUa2RuByRules.put(Character.valueOf('Л'), "Л");
            charsUa2RuByRules.put(Character.valueOf('М'), "М");
            charsUa2RuByRules.put(Character.valueOf('Н'), "Н");
            charsUa2RuByRules.put(Character.valueOf('О'), "О");
            charsUa2RuByRules.put(Character.valueOf('П'), "П");
            charsUa2RuByRules.put(Character.valueOf('Р'), "Р");
            charsUa2RuByRules.put(Character.valueOf('С'), "С");
            charsUa2RuByRules.put(Character.valueOf('Т'), "Т");
            charsUa2RuByRules.put(Character.valueOf('У'), "У");
            charsUa2RuByRules.put(Character.valueOf('Ф'), "Ф");
            charsUa2RuByRules.put(Character.valueOf('Х'), "Х");
            charsUa2RuByRules.put(Character.valueOf('Ц'), "Ц");
            charsUa2RuByRules.put(Character.valueOf('Ч'), "Ч");
            charsUa2RuByRules.put(Character.valueOf('Ш'), "Ш");
            charsUa2RuByRules.put(Character.valueOf('Щ'), "Щ");
            charsUa2RuByRules.put(Character.valueOf('Ь'), "Ь");
            charsUa2RuByRules.put(Character.valueOf('Ю'), "Ю");
            charsUa2RuByRules.put(Character.valueOf('Я'), "Я");
            charsUa2RuByRules.put(Character.valueOf('\''), "");
            charsUa2RuByRules.put(Character.valueOf('`'), "");
            return charsUa2RuByRules;
        }
    }

    private static Map<Character, String> getCharsUa2RU() {
        if(charsUa2Ru != null) {
            return charsUa2Ru;
        } else {
            charsUa2Ru = getMapInstance();
            charsUa2Ru.put(Character.valueOf('Ґ'), "Г");
            charsUa2Ru.put(Character.valueOf('Є'), "Е");
            charsUa2Ru.put(Character.valueOf('І'), "И");
            charsUa2Ru.put(Character.valueOf('Ї'), "Й");
            charsUa2Ru.put(Character.valueOf('\''), "");
            charsUa2Ru.put(Character.valueOf('`'), "");
            return charsUa2Ru;
        }
    }

    private static ConcurrentHashMap<Character, String> getMapInstance() {
        return new ConcurrentHashMap();
    }

    public static void main(String[] args) {
        System.out.println("start");
        System.out.println(getEnString("СЕНЕЦ\tАННА"));
    }

    public static String toRu(String sourceSerial) {
        return sourceSerial == null?null:sourceSerial.toUpperCase().replace("A", "А").replace("B", "В").replace("C", "С").replace("E", "Е").replace("H", "Н").replace("K", "К").replace("M", "М").replace("O", "О").replace("P", "Р").replace("T", "Т");
    }

    public static String toEn(String sourceSerial) {
        return sourceSerial == null?null:sourceSerial.toUpperCase().replace('А', 'A').replace('В', 'B').replace('Е', 'E').replace('К', 'K').replace('М', 'M').replace('Н', 'H').replace('О', 'O').replace('С', 'C');
    }
}
