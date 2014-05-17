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
package cehardin.roil.algebra;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

import cehardin.roil.Name;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Chad
 */
public final class Rename {
    private final Map<Name, Name> mapping;
    
    public Rename(Map<Name, Name> mapping) {
        this.mapping = unmodifiableMap(new HashMap<>(requireNonNull(mapping, "Mapping was null")));
        final Set<Name> valueSet = new HashSet<>(this.mapping.values());
        
        if(this.mapping.keySet().contains(null)) {
            throw new IllegalArgumentException("Mapping contained null key(s)");
        }
        
        if(this.mapping.values().contains(null)) {
            throw new IllegalArgumentException("Mapping contained null value(s)");
        }
        
        if(mapping.values().size() != valueSet.size()) {
            throw new IllegalArgumentException("The mapping contained duplicate values");
        }
    }
    
    public Map<Name, Name> getMapping() {
        return mapping;
    }
    
    @Override
    public int hashCode() {
        return mapping.hashCode();
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
            final Rename other = getClass().cast(o);
            result = mapping.equals(other.mapping);
        }
        else {
            result = false;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return format("%s : mapping=%s", super.toString(), mapping);
    }
}
