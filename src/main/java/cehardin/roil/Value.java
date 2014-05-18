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

import cehardin.roil.Domain.BooleanOperator;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static java.util.Collections.unmodifiableSet;
import static java.util.Collections.unmodifiableMap;
import static cehardin.roil.util.Predicates.curryBiPredicate;

import cehardin.roil.exception.ValueNotInDomainException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;

/**
 * A value is a piece of data that is contained within a {@link Tuple}.
 * @author Chad
 */
public final class Value<T> implements Comparable<Value<T>> {
    private static final Set<Class<?>> DoesNotNeedClone;
    private static final ConcurrentMap<Class<?>, Method> CloneMethods = new ConcurrentHashMap<>();
    
    static {
        final Set<Class<?>> doesNotNeedClone = new HashSet<>();
        doesNotNeedClone.add(Integer.class);
        doesNotNeedClone.add(String.class);
        DoesNotNeedClone = unmodifiableSet(doesNotNeedClone);
    }

    private static <T> Method findCloneMethod(T o) {
        return CloneMethods.computeIfAbsent(requireNonNull(o, "Object was null").getClass(), (c) -> {
            if (!Cloneable.class.isAssignableFrom(c)) {
                throw new IllegalArgumentException(format("%s does not implement %s", o, Cloneable.class));
            }
            try {
                return c.getMethod("clone");
            }
            catch (NoSuchMethodException | SecurityException ex) {
                throw new IllegalArgumentException(format("Could not get clone method for %s", o), ex);
            }
        });
    }

    private static Object invokeCloneMethod(Object o) {
        try {
            return findCloneMethod(o).invoke(o);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException(format("Could not clone %s", o), ex);
        }
    }

    private static <T> T clone(T o) {
        final Class<?> type = requireNonNull(o, "Object was null").getClass();
        if (DoesNotNeedClone.contains(type)) {
            return o;
        } else {
            return (T)invokeCloneMethod(o);
        }
    }
    
    

    private final Domain<T> domain;
    private final T data;
    private final Map<BooleanOperator, Predicate<T>> booleanOperators;
    

    public Value(Domain<T> domain, T data) throws ValueNotInDomainException {
        final Map<BooleanOperator, Predicate<T>> booleanOperators = new HashMap<>();
        this.data = clone(requireNonNull(data, "The data was null"));
        this.domain = requireNonNull(domain, "The domain was null");
        this.domain.check(this.data);
        
        for(final Entry<BooleanOperator, BiPredicate<T,T>> entry : this.domain.getBooleanOperators().entrySet()) {
            booleanOperators.put(entry.getKey(), curryBiPredicate(entry.getValue(), this.data));
        }
        
        this.booleanOperators = unmodifiableMap(booleanOperators);
    }

    /**
     * Get the data as a Java object.
     * @return the data, never null.
     */
    public T getData() {
        return clone(data);
    }

    /**
     * Get the domain.
     * @return The domain, never null.
     */
    public Domain<T> getDomain() {
        return domain;
    }
    
    Map<BooleanOperator, Predicate<T>> getBooleanOperators() {
        return booleanOperators;
    }

    @Override
    public int compareTo(Value<T> o) {
        return domain.compareTo(o.domain);
    }
   

    @Override
    public int hashCode() {
        return hash(domain, data);
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        
        if (o == null) {
            result = false;
        } else if (o == this) {
            result = true;
        } else if (getClass().isInstance(o)) {
            final Value<?> other = getClass().cast(o);
            result = domain.equals(other.domain) && data.equals(other.data);
        } else {
            result = false;
        }
        
        return result;
    }

    @Override
    public String toString() {
        return format("%s : domain=%s, data=%s", super.toString(), domain, data);
    }
  
}
