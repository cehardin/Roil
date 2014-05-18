/*
 * Copyright 2014 Chad Hardin
 * This file is part of Roil.
 *
 * Roil is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Roil is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * Roil. If not, see <http://www.gnu.org/licenses/>
 */
package cehardin.roil.domain;

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

import cehardin.roil.Domain;
import java.util.SortedSet;
import java.util.function.Function;

/**
 *
 * @author Chad
 */
public final class StringDomain implements Domain<String> 
{
    private static final int CHAR_LENGTH = 256;
    private static final char[] MIN_CHAR;
    private static final char[] MAX_CHAR;
    private static final String MIN_STRING;
    private static final String MAX_STRING;
    
    static {
        MIN_CHAR = new char[CHAR_LENGTH];
        MAX_CHAR = new char[CHAR_LENGTH];
        
        for(int i=0; i < CHAR_LENGTH; i++) {
            MIN_CHAR[i] = Character.MIN_VALUE;
            MAX_CHAR[i] = Character.MAX_VALUE;
        }
        
        MIN_STRING = new String(MIN_CHAR);
        MAX_STRING = new String(MAX_CHAR);
    }
    
    private static String toString(char[] chars) {
        return String.valueOf(chars);
    }
    
    private static char[] toChars(String string) {
        return string.toCharArray();
    }
    
    private static class SuccessorFunction implements Function<char[], char[]> {

        @Override
        public char[] apply(final char[] input) {
            final char[] output;
            int index = 0;
            
            if(requireNonNull(input, "Input char[] was null").length != CHAR_LENGTH) {
                throw new IllegalArgumentException(format("Input char[] was of length %d when it must instead be %d", input.length, CHAR_LENGTH));
            }
            
            output = input.clone();
            
            while(index < CHAR_LENGTH) {
                if(output[index] != Character.MAX_VALUE) {
                    output[index]++;
                    if(output[index] == Character.MAX_VALUE) {
                        
                    }
                }
            };
            for(int i=0; i < CHAR_LENGTH; i++) {
                if(output[i] != Character.MAX_VALUE) {
                    output[i]++;
                }
                else {
                    for(int y=(i + 1); y < CHAR_LENGTH; y++) {
                        if(output[y] != Character.MAX_VALUE) {
                            
                        }
                    }
                }
            }
        }
        
    }
    

    @Override
    public String getName() {
        return "String";
    }

    @Override
    public SortedSet<String> getRange() {
        return new RangeSet<>(
                String.class)
    }
    
}
