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
import static java.util.Objects.hash;
import static java.util.Objects.compare;
import static java.util.Objects.deepEquals;
import static java.lang.String.format;
import static java.util.Comparator.naturalOrder;
import static cehardin.roil.util.Optionals.optionalComparator;

import java.util.Optional;

/**
 * The schema for a relation. One can think of this as the definition of what
 * "fields" are allowed in a "table"
 * <p>
 * @author Chad
 * @see Relation
 */
public final class RelationSchema implements Comparable<RelationSchema> {

    private final Attributes attributes;
    private final Optional<PrimaryKey> primaryKey;
    private final SecondaryKeys secondaryKeys;

    public RelationSchema(Attributes attributes, Optional<PrimaryKey> primaryKey, SecondaryKeys secondaryKeys) {
        this.attributes = requireNonNull(attributes, "Attributes was null");
        this.primaryKey = requireNonNull(primaryKey, "Primary Key was null");
        this.secondaryKeys = requireNonNull(secondaryKeys, "Secondary Keys was null");
    }

    /**
     * Get the attributes of this schema
     * <p>
     * @return The attributes as a read-only map, never null and never contains
     *         null keys or values.
     */
    public Attributes getAttributes() {
        return attributes;
    }

    public Optional<PrimaryKey> getPrimaryKey() {
        return primaryKey;
    }

    public SecondaryKeys getSecondaryKeys() {
        return secondaryKeys;
    }

    @Override
    public int compareTo(RelationSchema other) {
        final int result;

        if (other == null) {
            result = 1;
        } else {
            final int attributesResult = compare(attributes, other.attributes, naturalOrder());

            if (attributesResult != 0) {
                result = attributesResult;
            } else {
                final int secondaryKeysResult = compare(secondaryKeys, other.secondaryKeys, naturalOrder());

                if (secondaryKeysResult != 0) {
                    result = secondaryKeysResult;
                } else {
                    result = compare(primaryKey, other.primaryKey, optionalComparator());
                }
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        return hash(attributes, primaryKey, secondaryKeys);
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;

        if (o == null) {
            result = false;
        } else if (o == this) {
            result = true;
        } else if (getClass().isInstance(o)) {
            final RelationSchema other = getClass().cast(o);
            result = deepEquals(attributes, other.attributes) && 
                    deepEquals(primaryKey, other.primaryKey) && 
                    deepEquals(secondaryKeys, other.secondaryKeys);
        } else {
            result = false;
        }

        return result;
    }

    @Override
    public String toString() {
        return format("%s : attributes=%s, primaryKey=%s, secondaryKeys=%s", super.toString(), attributes, primaryKey, secondaryKeys);
    }

}
