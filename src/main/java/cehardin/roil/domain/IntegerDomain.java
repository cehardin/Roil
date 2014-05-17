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

import cehardin.roil.Domain;
import java.util.Iterator;
import java.util.function.Supplier;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.MAX_VALUE;

/**
 *
 * @author Chad
 */
public final class IntegerDomain extends AbstractIteratorBasedFiniteDomain<Integer> {

    private static final class IntegerIterator implements Iterator<Integer> {

        private int number = MIN_VALUE;

        @Override
        public boolean hasNext() {
            return number != MAX_VALUE;
        }

        @Override
        public Integer next() {
            return number++;
        }
    };

    private static final class IntegerIteratorSupplier implements Supplier<Iterator<Integer>> {

        @Override
        public Iterator<Integer> get() {
            return new IntegerIterator();
        }
    }
    private static final IntegerIteratorSupplier SUPPLIER = new IntegerIteratorSupplier();

    @Override
    public String getName() {
        return "INTEGER";
    }

    @Override
    public boolean isIn(Integer o) {
        return o != null;
    }

    @Override
    protected Supplier<Iterator<Integer>> getIteratorSupplier() {
        return SUPPLIER;
    }

    @Override
    public int compareTo(Domain<Integer> o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return getClass().isInstance(o);
    }

}
