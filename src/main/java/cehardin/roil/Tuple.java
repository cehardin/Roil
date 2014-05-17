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
 * Tuples reside in a schema.  One can think of a tuple as a
 * "row" in a "table"
 * @author Chad
 */
public final class Tuple implements Comparable<Tuple> {
    private final Values values;
    
    public Tuple(Values values) {
        this.values = requireNonNull(values, "Values was null");
    }
    
    /**
     * Get the values of this tuple as a map keyed by an attribute.
     * @return The values as a read-only map never null and contains no null keys or values.
     */
    public Values getValues() {
        return values;
    }
    
    
    @Override
    public int compareTo(Tuple o) {
        return o == null ? 1 : values.compareTo(o.values);
    }
    
    @Override
    public int hashCode() {
        return values.hashCode();
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
            final Tuple other = getClass().cast(o);
            result = values.equals(other.values);
        }
        else {
            result = false;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return format("%s : values=%s", super.toString(), values);
    }

}
