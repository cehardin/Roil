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

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 *
 * @author Chad
 */
final class RangeIterator<T> implements Iterator<T> {
    private final T end;
    private final Comparator<? super T> comparator;
    private final Function<T, T> successorFunction;
    private T current;
    
    public RangeIterator(
            final T start, 
            final T end, 
            final Comparator<? super T> comparator, 
            final Function<T,T> successorFunction) {
        this.end = requireNonNull(end, "End was null");
        this.comparator = requireNonNull(comparator, "Comparator was null");
        this.successorFunction = requireNonNull(successorFunction, "Successor Function was null");
        this.current = requireNonNull(start, "Start was null");
        if(this.comparator.compare(start, end) > 0) {
            throw new IllegalArgumentException(format("Start %s was greater than end %s", start, end));
        }
    }

    @Override
    public boolean hasNext() {
        return comparator.compare(current, end) != 0;
    }

    @Override
    public T next() {
        if(!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        else {
            final T next = current;
            current = successorFunction.apply(next);
            if(comparator.compare(current, next) <= 0) {
                throw new IllegalStateException(format("Successfor function %s gave a lower value %s when applied with %s", successorFunction, current, next));
            }
            return next;
        }
    }
}
