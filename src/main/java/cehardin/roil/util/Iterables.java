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

import static cehardin.roil.util.Iterators.iteratorComparator;
import static java.util.Comparator.naturalOrder;

import java.util.Comparator;
import java.util.Iterator;


/**
 *
 * @author Chad
 */
public class Iterables {
    private static class IterableComparator<E> implements Comparator<Iterable<E>> {
        private final Comparator<Iterator<E>> iteratorComparator;
        
        public IterableComparator(final Comparator<E> elementComparator) {
            this.iteratorComparator = iteratorComparator(elementComparator);
        }

        @Override
        public int compare(Iterable<E> i1, Iterable<E> i2) {
            return iteratorComparator.compare(i1.iterator(), i2.iterator());
        }
    }
    
    public static final <E extends Comparable> Comparator<Iterable<E>> iterableComparator() {
        return new IterableComparator<E>(naturalOrder());
    }
    
    public static final <E> Comparator<Iterable<E>> iterableComparator(Comparator<E> elementComparator) {
        return new IterableComparator<>(elementComparator);
    }
}
