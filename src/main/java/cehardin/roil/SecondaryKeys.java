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

import static cehardin.roil.util.Iterables.iterableComparator;
import static cehardin.roil.util.Sets.filter;
import static java.util.Objects.requireNonNull;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.compare;

/**
 *
 * @author Chad
 */
public final class SecondaryKeys implements Comparable<SecondaryKeys>, Projectable<SecondaryKeys> {

    private final Set<SecondaryKey> set;

    public SecondaryKeys(Set<SecondaryKey> set) {
        this.set = unmodifiableSet(new HashSet<>(requireNonNull(set, "Secondary Keys set was null")));
        if (this.set.contains(null)) {
            throw new NullPointerException("Secondary Keys set contained a null element");
        }
    }

    public Set<SecondaryKey> getSet() {
        return set;
    }

    @Override
    public Function<Predicate<AttributeName>, SecondaryKeys> getProjector() {
        return (p) -> {
            final Set<SecondaryKey> newSet = new HashSet<>();
            set.stream().map((secondaryKey) -> secondaryKey.project(p)).forEach((newSecondaryKey) -> {
                newSet.add(newSecondaryKey);
            });
            return new SecondaryKeys(newSet);
        };
    }
    
    

    @Override
    public int compareTo(SecondaryKeys o) {
        return o == null ? 1 : compare(set, o.set, iterableComparator());
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;

        if (this == o) {
            result = true;
        } else if (getClass().isInstance(o)) {
            final SecondaryKeys other = getClass().cast(o);
            result = set.equals(other.set);
        } else {
            result = false;
        }

        return result;
    }

    @Override
    public String toString() {
        return format("%s : set=%s", super.toString(), set);
    }
}
