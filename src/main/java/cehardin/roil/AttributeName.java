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

import static java.lang.String.format;

import cehardin.roil.exception.InvalidNameException;


/**
 * A name for an attribute, which identifies a value in a tuple.
 * @author Chad
 */
public final class AttributeName extends Name implements Comparable<AttributeName> {

    public AttributeName(String value) throws InvalidNameException {
        super(value);
    }
    
    @Override
    public int compareTo(AttributeName o) {
        return o == null ? 1 : getValue().compareTo(o.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;

        if (o == null) {
            result = false;
        } else if (this == o) {
            result = true;
        } else if (getClass().isInstance(o)) {
            final AttributeName other = getClass().cast(o);
            result = getValue().equals(other.getValue());
        } else {
            result = false;
        }

        return result;
    }

    @Override
    public String toString() {
        return format("%s : value=%s", super.toString(), getValue());
    }

}
