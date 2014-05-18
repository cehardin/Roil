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

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 *
 * @author Chad
 */
public class Predicates {
    private static final class CurriedBiPredicate<T, U> implements Predicate<U> {
        private final T value;
        private final BiPredicate<T,U> biPredicate;
        
        public CurriedBiPredicate(final T value, final BiPredicate<T,U> biPredicate) {
            this.value = requireNonNull(value, "Value was null");
            this.biPredicate = requireNonNull(biPredicate, "BiPredicate was null");
        }

        @Override
        public boolean test(U t) {
            return biPredicate.test(value, t);
        }
    }
    
    private static final class InCollection<T> implements Predicate<T> {
        private final Collection<T> collection;
        
        public InCollection(Collection<T> collection) {
            this.collection = requireNonNull(collection, "Collection was null");
        }
    }
    
    public static <T,U> Predicate<U> curryBiPredicate(BiPredicate<T,U> biPredicate, T value) {
        return new CurriedBiPredicate<>(value, biPredicate);
                
    }
}
