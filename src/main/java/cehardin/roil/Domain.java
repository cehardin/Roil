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
package cehardin.roil;

import static cehardin.roil.Domain.BooleanOperator.*;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;

import cehardin.roil.exception.ValueNotInDomainException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.BiPredicate;


/**
 * A domain defines the acceptable values for a value.
 * <p>
 * @author Chad
 * @see Attribute
 * @see Value
 */
public interface Domain<T> extends Comparable<Domain<T>> {

    enum BooleanOperator {
        Equal,
        NotEqual,
        GreaterThan,
        GreaterThanOrEqual,
        LessThan,
        LessThanOrEqual
    }

    String getName();

    SortedSet<T> getRange();

    default Map<BooleanOperator, BiPredicate<T, T>> getBooleanOperators() {
        final Map<BooleanOperator, BiPredicate<T,T>> booleanOperators = new HashMap<>();
        
        booleanOperators.put(Equal, getEqualOperator());
        booleanOperators.put(NotEqual, getNotEqualOperator());
        booleanOperators.put(GreaterThan, getGreaterThanOperator());
        booleanOperators.put(GreaterThanOrEqual, getGreaterThanOrEqualOperator());
        booleanOperators.put(LessThan, getLessThanOperator());
        booleanOperators.put(LessThanOrEqual, getLessThanOrEqualOperator());
        
        return unmodifiableMap(booleanOperators);
    }

    default BiPredicate<T, T> getEqualOperator() {
        return (v1, v2) -> getRange().comparator().compare(check(v1), check(v2)) == 0;
    }

    default BiPredicate<T, T> getNotEqualOperator() {
        return getEqualOperator().negate();
    }

    default BiPredicate<T, T> getGreaterThanOperator() {
        return (v1, v2) -> getRange().comparator().compare(check(v1), check(v2)) > 0;
    }

    default BiPredicate<T, T> getGreaterThanOrEqualOperator() {
        return getEqualOperator().or(getGreaterThanOperator());
    }

    default BiPredicate<T, T> getLessThanOperator() {
        return (v1, v2) -> getRange().comparator().compare(check(v1), check(v2)) < 0;
    }

    default BiPredicate<T, T> getLessThanOrEqualOperator() {
        return getEqualOperator().or(getLessThanOperator());
    }

    /**
     * Determine if the object is in this domain.
     * <p>
     * @param o the object to check.
     * @return true if in the domain, false otherwise.
     */
    default boolean isIn(T o) {
        return getRange().contains(o);
    }

    /**
     * Check if the object is in the domain.
     * <p>
     * @param o The object to check.
     * @throws ValueNotInDomainException IF the object is not in the domain.
     */
    default T check(T o) throws ValueNotInDomainException {
        if (isIn(o)) {
            return o;
        } else {
            throw new ValueNotInDomainException(format("Not in domain: %s", o));
        }
    }
    
    @Override
    default public int compareTo(Domain<T> other) {
        return getName().compareTo(other.getName());
    }
}
