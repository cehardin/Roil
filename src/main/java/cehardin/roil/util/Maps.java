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

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

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

    public static <K extends Comparable, V extends Comparable> Comparator<Map<K, V>> mapComparator() {
        return mapComparator(Comparator.<K>naturalOrder(), Comparator.<V>naturalOrder());
    }

    public static <K, V> Comparator<Map<K, V>> mapComparator(Comparator<K> keyComparator, Comparator<V> valueComparator) {
        return new MapComparator<>(keyComparator, valueComparator);
    }
}
