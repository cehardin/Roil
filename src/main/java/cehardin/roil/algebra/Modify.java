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

import static java.util.Objects.requireNonNull;
import static java.util.Objects.hash;
import static java.lang.String.format;

import cehardin.roil.Tuple;

/**
 *
 * @author Chad
 */
public final class Modify {
    private final Tuple matcher;
    private final Tuple replacement;
    
    public Modify(Tuple matcher, Tuple replacement) {
        this.matcher = requireNonNull(matcher, "Matcher tuple was null");
        this.replacement = requireNonNull(replacement, "Replacement tuple was null");
    }
    
    public Tuple getMatcher() {
        return matcher;
    }
    
    public Tuple getReplacement() {
        return replacement;
    }
    
    @Override
    public int hashCode() {
        return hash(matcher, replacement);
    }
    
    @Override
    public boolean equals(Object o) {
        final boolean result;
        
        if(this == o) {
            result = true;
        }
        else if(getClass().isInstance(o)) {
            final Modify other = getClass().cast(o);
            result = matcher.equals(other.matcher) && replacement.equals(other.replacement);
        }
        else {
            result = false;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return format("%s : matcher=%s, replacement=%s", super.toString(), matcher, replacement);
    }
}
