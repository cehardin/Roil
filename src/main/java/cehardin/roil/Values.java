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

import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.compare;
import static java.util.Collections.unmodifiableMap;
import static cehardin.roil.util.Maps.mapComparator;

import java.util.Map;
import java.util.Map.Entry;

import static java.lang.String.format;

/**
 *
 * @author Chad
 */
public final class Values implements Comparable<Values> {
    private final Map<AttributeName, Value<?>> map;
    private final Map<AttributeName, Domain<?>> domains;
    
    public Values(Map<AttributeName, Value<?>> map) {
        final Map<AttributeName, Domain<?>> domains = new HashMap<>();
        this.map = unmodifiableMap(new HashMap<AttributeName, Value<?>>(requireNonNull(map, "Map was null")));
        
        if(this.map.containsKey(null)) {
            throw new IllegalArgumentException("Values map contains a null key");
        }
        if(this.map.containsValue(null)) {
            throw new IllegalArgumentException("Values map contains null value(s)");
        }
        
        for(final Entry<AttributeName, Value<?>> nameValue : map.entrySet()) {
            final AttributeName name = nameValue.getKey();
            final Domain<?> domain = nameValue.getValue().getDomain();
            
            domains.put(name, domain);
        }
        
        this.domains = unmodifiableMap(domains);
    }
    
    public Map<AttributeName, Value<?>> getMap() {
        return map;
    }
    
    public Map<AttributeName, Domain<?>> getDomainMap() {
        return domains;
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
        
        if(o == null) {
            result = false;
        }
        else if(o == this) {
            result = true;
        }
        else if(getClass().isInstance(o)) {
            final Values other = getClass().cast(o);
            result = map.equals(other.map);
        }
        else {
            result = false;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return format("%s : map=%s", super.toString(), map);
    }
}
