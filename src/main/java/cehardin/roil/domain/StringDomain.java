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

import cehardin.roil.Domain;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 *
 * @author Chad
 */
public final class StringDomain extends AbstractIteratorBasedFiniteDomain<String> {

    public static final String MIN_VALUE;
    public static final String MAX_VALUE;
    public static final int MAX_LENGTH = 1024;
    private static final char[] MIN_CHARS = new char[MAX_LENGTH];
    private static final char[] MAX_CHARS = new char[MAX_LENGTH];

    static {
        for (int i = 0; i < MAX_LENGTH; i++) {
            MIN_CHARS[i] = Character.MAX_VALUE;
            MAX_CHARS[i] = Character.MIN_VALUE;
        }

        MIN_VALUE = new String(MIN_CHARS);
        MAX_VALUE = new String(MAX_CHARS);
    }

    private static final class StringIterator implements Iterator<String> {

        private final char[] characters = MIN_CHARS.clone();
        private int position = 0;

        @Override
        public boolean hasNext() {
            return !Arrays.equals(characters, MAX_CHARS);
        }

        @Override
        public String next() {
            final String next = new String(characters);

            if (characters[position] == Character.MAX_VALUE) {
                if (position == MAX_LENGTH - 1) {
                    throw new NoSuchElementException();
                }
                characters[position + 1] = Character.MAX_VALUE;
                for (int i = position; position >= 0; position--) {
                    characters[i] = Character.MIN_VALUE;
                }
                do {
                    position++;
                } while (characters[position] == Character.MAX_VALUE);
            } else {
                characters[position]++;
            }

            return next;
        }
    }

    private static final class StringIteratorSupplier implements Supplier<Iterator<String>> {

        @Override
        public Iterator<String> get() {
            return new StringIterator();
        }
    }

    private static final StringIteratorSupplier SUPPLIER = new StringIteratorSupplier();

    @Override
    public String getName() {
        return "STRING";
    }

    @Override
    public boolean isIn(String o) {
        return o != null && o.length() <= MAX_LENGTH;
    }

    @Override
    protected Supplier<Iterator<String>> getIteratorSupplier() {
        return SUPPLIER;
    }

    @Override
    public int compareTo(Domain<String> o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return getClass().isInstance(o);
    }
}
