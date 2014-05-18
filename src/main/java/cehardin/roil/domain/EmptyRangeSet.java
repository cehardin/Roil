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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

/**
 *
 * @author Chad
 */
final class EmptyRangeSet<T> extends AbstractRangeSet<T> {

    public EmptyRangeSet(Class<T> type, Comparator<? super T> comparator) {
        super(type, comparator);
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        requireNonNull(fromElement, "fromElement was null");
        requireNonNull(toElement, "toElement was null");
        throw new IllegalArgumentException("Cannot create subset from empty set");
    }

    
    
    @Override
    public T first() {
        throw new NoSuchElementException("Nothing in ths empty set");
    }

    @Override
    public T last() {
        throw new NoSuchElementException("Nothing in ths empty set");
    }


    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new EmptyRangeIterator<>();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        requireNonNull(c, "c was null");
        return c.isEmpty();
    }

    
}
