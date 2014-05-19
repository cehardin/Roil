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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.compare;
import static java.util.Collections.unmodifiableSet;
import static cehardin.roil.util.Iterables.iterableComparator;
import static cehardin.roil.util.Sets.filter;
import static cehardin.roil.util.Sets.transform;
import static java.lang.String.format;

/**
 *
 * @author Chad
 */
public final class SecondaryKey implements Comparable<SecondaryKey>, Projectable<SecondaryKey>, Renamable<SecondaryKey> {
    private final Set<AttributeName> attributeNames;
    
    public SecondaryKey(Set<AttributeName> attributeNames) {
        this.attributeNames = unmodifiableSet(new HashSet<>(requireNonNull(attributeNames, "Attribute Names was null")));
        if(this.attributeNames.contains(null)) {
            throw new NullPointerException("Attribute Names contained a null element");
        }
    }
    
    public Set<AttributeName> getAttributeNames() {
        return attributeNames;
    }

    @Override
    public Function<Predicate<AttributeName>, SecondaryKey> getProjectFunction() {
        return (p) -> new SecondaryKey(filter(attributeNames, p));
    }

    @Override
    public Function<UnaryOperator<AttributeName>, SecondaryKey> getRenameFunction() {
        return (f) -> new SecondaryKey(transform(attributeNames, f));
    }

    @Override
    public int compareTo(SecondaryKey o) {
        return o == null ? null : compare(attributeNames, o.attributeNames, iterableComparator());
    }
    
    @Override
    public int hashCode() {
        return attributeNames.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        final boolean result;
        
        if(this == o) {
            result = true;
        }
        else if(getClass().isInstance(o)) {
            final SecondaryKey other = getClass().cast(o);
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
