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
import static java.util.Collections.unmodifiableSet;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

import static java.lang.Integer.MAX_VALUE;

/**
 *
 * @author Chad
 */
abstract class AbstractIteratorBasedDomain<T> extends AbstractDomain<T> {
    private final static class IteratorSet<T> extends AbstractSet<T> {
        private final boolean finite;
        private final Supplier<Iterator<T>> iteratorSupplier;
        
        public IteratorSet(boolean finite, Supplier<Iterator<T>> iteratorSupplier) {
            this.finite = finite;
            this.iteratorSupplier = requireNonNull(iteratorSupplier, "Iterator Supplier was null");
        }

        @Override
        public Iterator<T> iterator() {
            return requireNonNull(iteratorSupplier.get(), format("Iterator Supplier returned null : %s", iteratorSupplier));
        }

        @Override
        public int size() {
            if(finite) {
                final Iterator<T> iterator = iterator();
                int size = 0;
            
                while(iterator.hasNext() && size != Integer.MAX_VALUE) {
                    iterator.next();
                    size++;
                }
            
                return size;
            }
            else {
                return MAX_VALUE;
            }   
        }

        @Override
        public String toString() {
            return format("%s : finite=%s, iteratorSupplier=%s", super.toString(), finite, iteratorSupplier);
        }

        @Override
        public boolean isEmpty() {
            return !iterator().hasNext();
        }
    };
    
    protected abstract Supplier<Iterator<T>> getIteratorSupplier();
    
    @Override
    public final Set<T> getRange() {
        return unmodifiableSet(new IteratorSet<>(isFinite(), getIteratorSupplier()));
    }
    
}
