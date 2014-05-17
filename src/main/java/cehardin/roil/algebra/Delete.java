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

import cehardin.roil.Tuple;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.hash;
import static java.lang.String.format;

/**
 *
 * @author Chad
 */
public class Delete {
    private final Tuple matcher;
    
    public Delete(Tuple matcher) {
        this.matcher = requireNonNull(matcher, "Matcher was null");
    }
    
    public Tuple getMatcher() {
        return matcher;
    }
}
