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
import cehardin.roil.exception.DomainMismatchException;
import cehardin.roil.exception.NoSuchAttributeNameException;
import java.util.Collections;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.compare;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.emptyMap;
import static cehardin.roil.util.Maps.mapComparator;

import java.util.Map;
import java.util.Map.Entry;

import static java.util.Objects.*;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.format;
import static cehardin.roil.util.Maps.filterKeys;

/**
 *
 * @author Chad
 */
public final class Values implements Comparable<Values>, Projectable<Values>, Selectable<Values> {

    private final Map<AttributeName, Value<Object>> map;
    private final Map<AttributeName, Domain<Object>> domains;

    public Values(Map<AttributeName, Value<Object>> map) {
        final Map<AttributeName, Domain<Object>> domains = new HashMap<>();
        this.map = unmodifiableMap(new HashMap<AttributeName, Value<Object>>(requireNonNull(map, "Map was null")));

        if (this.map.containsKey(null)) {
            throw new IllegalArgumentException("Values map contains a null key");
        }
        if (this.map.containsValue(null)) {
            throw new IllegalArgumentException("Values map contains null value(s)");
        }

        for (final Entry<AttributeName, Value<Object>> nameValue : map.entrySet()) {
            final AttributeName name = nameValue.getKey();
            final Domain<Object> domain = nameValue.getValue().getDomain();

            domains.put(name, domain);
        }

        this.domains = unmodifiableMap(domains);
    }

    public Map<AttributeName, Value<Object>> getMap() {
        return map;
    }

    public Map<AttributeName, Domain<Object>> getDomainMap() {
        return domains;
    }

    @Override
    public Function<Predicate<AttributeName>, Values> getProjector() {
        return (p) -> new Values(filterKeys(map, p));
    }

    @Override
    public Set<AttributeName> getAttributeNames() {
        return getMap().keySet();
    }

    @Override
    public Function<SelectByAttribute, Values> getSelectByAttributeFunction() {
        return (s) -> {
            final BooleanOperator operator = s.getOperator();
            final AttributeName targetAttributeName = s.getTargetAttributeName();
            final AttributeName attributeName = s.getAttributeName();
            final Value<Object> targetValue = getMap().get(targetAttributeName);
            final Value<Object> value = getMap().get(attributeName);
            
            if(targetValue == null) {
                throw new NoSuchAttributeNameException(format("The target attribute %s is not present in this tuple", targetAttributeName));
            }
            
            if(value == null) {
                throw new NoSuchAttributeNameException(format("The attribute %s is not present in this tuple", attributeName));
            }
            
            if(targetValue.getBooleanOperators().get(operator).test(value)) {
                return this;
            }
            else {
                return new Values(emptyMap());
            }  
        };
    }

    @Override
    public Function<SelectByConstant, Values> getSelectByConstantFunction() {
        return (s) -> {
            final BooleanOperator operator = s.getOperator();
            final AttributeName targetAttributeName = s.getTargetAttributeName();
            final Value<Object> targetValue = getMap().get(targetAttributeName);
            final Value<Object> value = s.getConstant();
            
            if(targetValue == null) {
                throw new NoSuchAttributeNameException(format("The target attribute %s is not present in this tuple", targetAttributeName));
            }
            
            
            if(targetValue.getBooleanOperators().get(operator).test(value)) {
                return this;
            }
            else {
                return new Values(emptyMap());
            }  
        };
    }
    
    

    @Override
    public int compareTo(Values o) {
        return o == null ? 1 : compare(map, o.map, mapComparator());
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;

        if (o == null) {
            result = false;
        } else if (o == this) {
            result = true;
        } else if (getClass().isInstance(o)) {
            final Values other = getClass().cast(o);
            result = map.equals(other.map);
        } else {
            result = false;
        }

        return result;
    }

    @Override
    public String toString() {
        return format("%s : map=%s", super.toString(), map);
    }
}
