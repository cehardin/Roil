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

import cehardin.roil.exception.InvalidNameException;

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

/**
 * A name is used to identify various objects in the system
 * such as "fields" in a "table" or a "table's" name.
 * @author Chad Hardin
 * @see RelationSchema
 * @see Attribute
 */
public abstract class Name {
    
    private final String value;

    public Name(String value) throws InvalidNameException {
        this.value = requireNonNull(value, "Value was null").trim();
        if (this.value.isEmpty()) {
            throw new InvalidNameException(format("Name is invalid: %s", this.value));
        }
    }

    /**
     * Get the value of this name.
     * @return The value of the name, never null or empty.
     */
    public final String getValue() {
        return value;
    }
}
