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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.Collections.unmodifiableSet;
import static java.util.Arrays.asList;
import static java.lang.String.format;

/**
 *
 * @author Chad
 */
public class Sets {
    
    public static <T> Set<T> filter(Set<T> set, Predicate<T> p) {
        final HashSet<T> newSet = new HashSet<>();
        
        for(final T t : set) {
            if(p.test(t)) {
                newSet.add(t);
            }
        }
        
        
        return unmodifiableSet(newSet);
    }
    
    public static <T> Set<T> transform(Set<T> input, UnaryOperator<T> transformer) {
        final Set<T> output = new HashSet<>();
        
        for(final T element : input) {
            final T newElement = transformer.apply(element);
            
            if(!output.add(newElement)) {
                throw new IllegalStateException(format("Transformer %s returned output %s more than once, this time on input %s", transformer, newElement, element));
            }
        }
        
        return output;
    }
}
