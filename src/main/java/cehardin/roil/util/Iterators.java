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

import static java.util.Objects.requireNonNull;
import static java.util.Comparator.naturalOrder;

import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author Chad
 */
public class Iterators {

    private static class IteratorComparator<E> implements Comparator<Iterator<E>> {

        private final Comparator<? super E> elementComparator;

        public IteratorComparator(final Comparator<? super E> elementComparator) {
            this.elementComparator = requireNonNull(elementComparator, "Element Comparator was null");
        }

        @Override
        public int compare(Iterator<E> i1, Iterator<E> i2) {
            if (i1 == null) {
                if (i2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (i2 == null) {
                return 1;
            } else {
                while (i1.hasNext() || i2.hasNext()) {
                    if (!i1.hasNext()) {
                        if (!i2.hasNext()) {
                            return 0;
                        } else {
                            return -1;
                        }
                    } else if (!i2.hasNext()) {
                        return 1;
                    } else {
                        final int elementResult = Objects.compare(i1.next(), i2.next(), elementComparator);

                        if (elementResult != 0) {
                            return elementResult;
                        }
                    }
                }

                return 0;
            }
        }
    }
    
    private static final class ReadOnlyIterator<E> implements Iterator<E> {
        private final Iterator<E> iterator;
        
        public ReadOnlyIterator(Iterator<E> iterator) {
            this.iterator = requireNonNull(iterator, "Iterator was null");
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public E next() {
            return iterator.next();
        }
    }

    public static final <E extends Comparable> Comparator<Iterator<E>> iteratorComparator() {
        return new IteratorComparator<>(naturalOrder());
    }

    public static final <E> Comparator<Iterator<E>> iteratorComparator(Comparator<E> elementComparator) {
        return new IteratorComparator<>(elementComparator);
    }
    
    public static final <E> Iterator<E> readOnlyIterator(Iterator<E> iterator) {
        return new ReadOnlyIterator<>(iterator);
    }
}
