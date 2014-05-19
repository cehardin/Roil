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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Collections.unmodifiableMap;
import static cehardin.roil.util.Maps.mapComparator;
import static cehardin.roil.util.Maps.filterKeys;
import static cehardin.roil.util.Maps.transformKeys;

/**
 * An attribute is a combination of an Attribute Name and a Domain.
 * <p>
 * @author Chad
 * @see RelationSchema
 */
public final class Attributes implements Comparable<Attributes>, Projectable<Attributes>, Renamable<Attributes> {

    private static final Comparator<Map<AttributeName, Domain<?>>> mapComparator = mapComparator();

    private final Map<AttributeName, Domain<?>> map;

    public Attributes(Map<AttributeName, Domain<?>> map) {
        this.map = unmodifiableMap(new HashMap<AttributeName, Domain<?>>(requireNonNull(map, "The map was null")));

        if (this.map.containsKey(null)) {
            throw new NullPointerException("The map contained a null key");
        }

        if (this.map.containsValue(null)) {
            throw new NullPointerException("The map contained one or more null values");
        }
    }

    public Map<AttributeName, Domain<?>> getMap() {
        return map;
    }

    @Override
    public Function<Predicate<AttributeName>, Attributes> getProjectFunction() {
        return (p) -> new Attributes(filterKeys(map, p));
    }

    @Override
    public Function<UnaryOperator<AttributeName>, Attributes> getRenameFunction() {
        return (f) -> new Attributes(transformKeys(map, f));
    }

    

    @Override
    public int compareTo(Attributes o) {
        return o == null ? 1 : mapComparator.compare(map, o.map);
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
            final Attributes other = getClass().cast(o);
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
