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

import static java.lang.String.format;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.function.Predicate;

/**
 *
 * @author Chad
 */
public interface Range<T> {
    enum EndPointType {
        Open,
        Closed;
    }
    
    EndPointType getStartEndpointType();
    
    EndPointType getStopEndpointType();
    
    T getStartEnpoint();
    
    T getStopEndpoint();
    
    Comparator<T> getComparator();
    
    default Predicate<T> getIsAboveStartEndpointPredicate() {
        return (o) -> {
            final EndPointType endPointType = getStartEndpointType();
            switch(endPointType) {
                case Open:
                    return getComparator().compare(o, getStartEnpoint()) > 0;
                case Closed:
                    return getComparator().compare(o, getStartEnpoint()) >= 0;
                default:
                    throw new IllegalStateException(format("Unknown Endpoint type: %s", endPointType));
            }
        };
    }
    
    default Predicate<T> getIsBelowStopEndpointPredicate() {
        return (o) -> {
            final EndPointType endPointType = getStopEndpointType();
            switch(endPointType) {
                case Open:
                    return getComparator().compare(o, getStartEnpoint()) < 0;
                case Closed:
                    return getComparator().compare(o, getStartEnpoint()) <= 0;
                default:
                    throw new IllegalStateException(format("Unknown Endpoint type: %s", endPointType));
            }
        };
    }
    
    default Predicate<T> getIsInRangePredicate() {
        return getIsAboveStartEndpointPredicate().and(getIsBelowStopEndpointPredicate());
    }
    
    default boolean isAboveStartEndpoint(T o) {
        return getIsAboveStartEndpointPredicate().test(o);
    }
    
    default boolean isBelowStopEndpoint(T o) {
        return getIsBelowStopEndpointPredicate().test(o);
    }
    
    default boolean isInRange(T o) {
        return getIsInRangePredicate().test(o);
    }
    
}
