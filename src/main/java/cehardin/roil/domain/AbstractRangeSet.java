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
import java.util.SortedSet;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Chad
 */
abstract class AbstractRangeSet<T> implements SortedSet<T> {

    private final Class<T> type;
    private final Comparator<? super T> comparator;

    public AbstractRangeSet(
            final Class<T> type,
            final Comparator<? super T> comparator) {
        this.type = requireNonNull(type, "Type was null");
        this.comparator = requireNonNull(comparator, "Comparator was null");
    }

    @Override
    public final Comparator<? super T> comparator() {
        return comparator;
    }

    protected final Class<T> getType() {
        return type;
    }

    @Override
    public final SortedSet<T> headSet(T toElement) {
        requireNonNull(toElement, "toElement was null");
        return subSet(first(), toElement);
    }

    @Override
    public final SortedSet<T> tailSet(T fromElement) {
        requireNonNull(fromElement, "fromElement was null");
        return subSet(fromElement, last());
    }

    @Override
    public final Object[] toArray() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public final <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public final boolean add(T e) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override
    public final boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Read Only");
    }

}
