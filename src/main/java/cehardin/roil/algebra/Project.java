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

import static java.lang.String.format;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import cehardin.roil.Name;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chad
 */
public final class Project {
    private final Set<Name> attributeNames;
    
    public Project(Set<Name> attributeNames) {
        this.attributeNames = unmodifiableSet(new HashSet<>(requireNonNull(attributeNames, "Attribute Names was null")));
        if(this.attributeNames.contains(null)) {
            throw new IllegalArgumentException("Attributes Names contains null element");
        }
    }
    
    public Set<Name> getAttributesNames() {
        return attributeNames;
    }
    
    @Override
    public int hashCode() {
        return attributeNames.hashCode();
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
            final Project other = getClass().cast(o);
            result = attributeNames.equals(other.attributeNames);
        }
        else {
            result = false;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return format("%s : attributeNames=%s", super.toString(), attributeNames);
    }
}
