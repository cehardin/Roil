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

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

/**
 *
 * @author Chad
 */
public final class PrimaryKey implements Comparable<PrimaryKey> {
    private final AttributeName attributeName;
    
    public PrimaryKey(AttributeName attributeName) {
        this.attributeName = requireNonNull(attributeName, "Attribute Name was null");
    }
    
    public AttributeName getAttributeName() {
        return attributeName;
    }
    
    @Override
    public int compareTo(PrimaryKey other) {
        if(other == null) {
            return 1;
        }
        else {
            return attributeName.compareTo(other.attributeName);
        }
    }
    
    @Override
    public int hashCode() {
        return attributeName.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        boolean result;
        if(o == this) {
            result = true;
        }
        else if(getClass().isInstance(o)) {
            final PrimaryKey other = getClass().cast(o);
            result = attributeName.equals(other.attributeName);
        }
        else {
            result = false;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return format("%s : attributeName=%s", super.toString(), attributeName);
    }
}
