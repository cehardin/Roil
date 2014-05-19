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
package cehardin.roil.util;

import cehardin.roil.algebra.Select.Operator;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

/**
 *
 * @author Chad
 */
public final class Maps {

    private static final class EntryComparator<K, V> implements Comparator<Entry<K, V>> {

        private final Comparator<K> keyComparator;
        private final Comparator<V> valueComparator;

        public EntryComparator(Comparator<K> keyComparator, Comparator<V> valueComparator) {
            this.keyComparator = requireNonNull(keyComparator, "Key Comparator was null");
            this.valueComparator = requireNonNull(valueComparator, "Value Comparator was null");
        }

        @Override
        public int compare(Entry<K, V> e1, Entry<K, V> e2) {
            if (e1 == null) {
                if (e2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (e2 == null) {
                return 1;
            } else {
                final int keyResult = Objects.compare(e1.getKey(), e2.getKey(), keyComparator);

                if (keyResult != 0) {
                    return keyResult;
                } else {
                    return Objects.compare(e1.getValue(), e2.getValue(), valueComparator);
                }
            }
        }
    }

    private static final class MapComparator<K, V> implements Comparator<Map<K, V>> {

        private final EntryComparator<K, V> entryComparator;

        public MapComparator(Comparator<K> keyComparator, Comparator<V> valueComparator) {
            this.entryComparator = new EntryComparator<>(keyComparator, valueComparator);

        }

        @Override
        public int compare(Map<K, V> m1, Map<K, V> m2) {
            if (m1 == null) {
                if (m2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (m2 == null) {
                return 1;
            } else {
                if (m1.size() > m2.size()) {
                    return 1;
                } else if (m1.size() < m2.size()) {
                    return -1;
                } else {
                    return Objects.compare(m1.entrySet(), m2.entrySet(), Iterables.iterableComparator(entryComparator));
                }
            }
        }
    }

    private static class KeyFilter<K, V> implements BiFunction<Map<K, V>, Predicate<K>, Map<K, V>> {

        @Override
        public Map<K, V> apply(Map<K, V> input, Predicate<K> keyPredicate) {
            final HashMap<K, V> output = new HashMap<>();

            requireNonNull(keyPredicate, "Key Predicate was null");
            for (final Entry<K, V> entry : requireNonNull(input, "Input was null").entrySet()) {
                final K key = entry.getKey();
                final V value = entry.getValue();

                if (keyPredicate.test(key)) {
                    output.put(key, value);
                }
            }

            return output;
        }
    }

    public static <K extends Comparable, V extends Comparable> Comparator<Map<K, V>> mapComparator() {
        return mapComparator(Comparator.<K>naturalOrder(), Comparator.<V>naturalOrder());
    }

    public static <K, V> Comparator<Map<K, V>> mapComparator(Comparator<K> keyComparator, Comparator<V> valueComparator) {
        return new MapComparator<>(keyComparator, valueComparator);
    }

    public static <K, V> BiFunction<Map<K, V>, Predicate<K>, Map<K, V>> keyFilterer() {
        return new KeyFilter<>();
    }

    public static <K, V> Map<K, V> filterKeys(Map<K, V> input, Predicate<K> keyPredicate) {
        return Maps.<K, V>keyFilterer().apply(input, keyPredicate);
    }

    public static <K, V> Map<K, V> transformKeys(Map<K, V> input, UnaryOperator<K> keyTransformer) {
        final Map<K, V> output = new HashMap<>();

        for (final Entry<K, V> e : input.entrySet()) {
            final K key = e.getKey();
            final V value = e.getValue();
            final K newKey = keyTransformer.apply(key);

            if (output.put(newKey, value) != null) {
                throw new IllegalStateException(format("Key Transformer %s returned %s on input %s when it has already returned %s before", keyTransformer, newKey, key, newKey));
            }
        }

        return output;
    }

    public static <K1, K2, V> Map<K2, V> transformKeys(Map<K1, V> input, Function<K1, K2> keyTransformer) {
        final Map<K2, V> output = new HashMap<>();

        for (final Entry<K1, V> e : input.entrySet()) {
            final K1 key = e.getKey();
            final V value = e.getValue();
            final K2 newKey = keyTransformer.apply(key);

            if (output.put(newKey, value) != null) {
                throw new IllegalStateException(format("Key Transformer %s returned %s on input %s when it has already returned %s before", keyTransformer, newKey, key, newKey));
            }
        }

        return output;
    }
}
