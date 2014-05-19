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
import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.Supplier;


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
    
    private static class IteratorSuppliedIterable<E> implements Iterable<E> {
        private final Supplier<Iterator<E>> iteratorSupplier;
        
        public IteratorSuppliedIterable(Supplier<Iterator<E>> iteratorSupplier) {
            this.iteratorSupplier = requireNonNull(iteratorSupplier, "Iterator supplier was null");
        }

        @Override
        public Iterator<E> iterator() {
            return requireNonNull(iteratorSupplier.get(), format("Iterator Supplier gave a null iterator: %s", iteratorSupplier));
        }
    }
    
    private static class IterableIteratorSupplier<E> implements Supplier<Iterator<E>> {
        private final Iterable<E> iterable;
        
        public IterableIteratorSupplier(final Iterable<E> iterable) {
            this.iterable = requireNonNull(iterable, "Iterable was null");
        }

        @Override
        public Iterator<E> get() {
            return iterable.iterator();
        }        
    }
    
    private static class FilteredIterable<E> extends IteratorSuppliedIterable<E> {
        private final Predicate<E> filter;
        
        public FilteredIterable(Iterable<E> iterable, Predicate<E> filter) {
            super(new IterableIteratorSupplier<>(iterable));
            this.filter = requireNonNull(filter, "Filter was null");
        }

        @Override
        public Iterator<E> iterator() {
            return Iterators.filter(super.iterator(), filter);
        }
    }
    
    public static final <E extends Comparable> Comparator<Iterable<E>> iterableComparator() {
        return new IterableComparator<E>(naturalOrder());
    }
    
    public static final <E> Comparator<Iterable<E>> iterableComparator(Comparator<E> elementComparator) {
        return new IterableComparator<>(elementComparator);
    }
    
    public static final <E> Iterable<E> newIterable(final Supplier<Iterator<E>> iteratorSupplier) {
        return new IteratorSuppliedIterable<>(iteratorSupplier);
    }
    
    public static final <E> Iterable filter(Iterable<E> iterable, Predicate<E> filter) {
        return new FilteredIterable(iterable, filter);
    }
}
