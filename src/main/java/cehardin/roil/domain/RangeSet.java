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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Chad
 */
final class RangeSet<T> extends AbstractRangeSet<T> {

    private final T first;
    private final T last;
    private final Function<T, T> successorFunction;
    private final Function<T, T> predecessorFunction;

    public RangeSet(
            final Class<T> type,
            final T first,
            final T last,
            final Comparator<? super T> comparator,
            final Function<T, T> successorFunction,
            Function<T, T> predecessorFunction) {
        super(type, comparator);
        this.first = requireNonNull(first, "First was null");
        this.last = requireNonNull(last, "Last was null");
        this.successorFunction = requireNonNull(successorFunction, "Successor Function was null");
        this.predecessorFunction = requireNonNull(successorFunction, "Predecessor Function was null");

        if (comparator.compare(this.first, this.last) > 0) {
            throw new IllegalArgumentException(format("First %s was greater than last %s", this.first, this.last));
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new RangeIterator<>(
                first(), 
                predecessorFunction.apply(last()), 
                comparator(), 
                successorFunction);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        final Iterator<T> iterator = iterator();
        int size = 0;

        while (iterator.hasNext()) {
            iterator.next();
            if (size != Integer.MAX_VALUE) {
                size++;
            }
        }

        return size;
    }

    @Override
    public boolean contains(Object o) {
        if (getType().isInstance(o)) {
            final T t = getType().cast(o);
            return comparator().compare(t, first()) >= 0 && comparator().compare(t, last()) <= 0;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (final Object o : requireNonNull(c, "c was null")) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public T first() {
        return first;
    }

    @Override
    public T last() {
        return last;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        requireNonNull(fromElement, "fromElement was null");
        requireNonNull(toElement, "toElement was null");

        if (!contains(fromElement)) {
            throw new IllegalArgumentException(format("fromElement %s is not in this set", fromElement));
        }

        if (!contains(toElement)) {
            throw new IllegalArgumentException(format("toElement %s is not in this set", fromElement));
        }

        if (comparator().compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException(format("fromElement %s is higher than toElement %s", fromElement, toElement));
        }

        final T newFirst = fromElement;
        final T newLast = predecessorFunction.apply(last);

        if (comparator().compare(newFirst, newLast) == 0) {
            return new EmptyRangeSet<>(getType(), comparator());
        } else {
            return new RangeSet<>(
                    getType(),
                    first,
                    newLast, comparator(),
                    successorFunction,
                    predecessorFunction);
        }
    }

}
