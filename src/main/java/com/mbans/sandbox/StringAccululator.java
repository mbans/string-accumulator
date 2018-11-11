package com.mbans.sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.stream;

/**
 * Created by mbans on 11/11/2018.
 */
public class StringAccululator {

    private static final String DEFAULT_DELIMITER = ",";
    private static final Pattern DOUBLE_ROW_CHECK = Pattern.compile("\\//(.*)\n(.*)");

    public int add(final String input) {
        if(input == null ||input.isEmpty()) {
            return 0;
        }

        return add(getRawNumberStr(input)
                   .replace("\n", DEFAULT_DELIMITER)
                   .split(DEFAULT_DELIMITER)
                  );
    }

    private String getRawNumberStr(final String input) {
        Matcher matcher = DOUBLE_ROW_CHECK.matcher(input);
        return matcher.find() ? replaceDelimeters(matcher.group(1), matcher.group(2)) : input;
    }

    /**
     * Replaces all custom delimeters with the default delimeter
     * e.g.
     * Given custom delimeters are '$*' and '()''
     * When rawNumbers = '1$*2()3'
     * Then returns '1,2,3'
     *
     * @param rawDelimeters - pipe (|) delimited list of delimeters
     * @param rawNumbers - the delimeted list of numbers
     * @return - rawNumbers with all custom delimeters replaced with the default delimeter
     */
    private String replaceDelimeters(final String rawDelimeters, final String rawNumbers) {
        String toReturn = rawNumbers;

        for(String del : rawDelimeters.split(Pattern.quote("|"))) {
            toReturn = toReturn.replaceAll(Pattern.quote(del),DEFAULT_DELIMITER);
        }

        return toReturn;
    }

    /**
     * Accumulates given numbers, excludes numbers > 1000 and exceptions if any negatives are provided
     * @param numbers
     * @return sum of numbers, exlcudes numbers > 1000
     */
    private int add(String[] numbers) {
         List<Integer> negatives = new ArrayList<>();
         Integer sum =  stream(numbers)
                 .parallel()                        //Parrallel for performance
                 .mapToInt(Integer::parseInt)
                 .filter(i -> i <= 1000)            //Only include <= 1000
                 .map(i -> {                        //Record negatives
                             if (i < 0) {
                                 negatives.add(i);
                             }
                             return i;
                         }
                     )
                 .sum();                            //Collect sum

         if(!negatives.isEmpty()) {
             throw new RuntimeException("negatives not allowed: " + negatives);
         }

         return sum;
    }
}
