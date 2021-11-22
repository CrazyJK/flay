package jk.kamoru.flayground.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class MatchTest {

    String testInput = "[You][did a][good job][][asdad, adsa][2021.11.22]!";

    @Test
    public void group() {
        String regex = "[\\[](.*?)[\\]]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(testInput);
        while (matcher.find()) {
            System.out.format("%-15s - %-15s - %s%n", matcher.group(), matcher.group(0), matcher.group(1));
        }
        System.out.println(matcher.groupCount());
    }

    @Test
    void split() {
        String[] split = testInput.split("\\]\\[");
        for (String str : split) {
            System.out.println(str);
        }
    }

    @Test
    void testStringUtilsSplit() {
        String[] split = StringUtils.split(" aaa, vvvv,,2,", ",");
        for (String s : split) {
            System.out.println("[" + s + "]");
        }
    }
}
