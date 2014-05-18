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
package cehardin.roil.domain;

import cehardin.roil.Domain;
import java.util.SortedSet;

import static java.util.Comparator.naturalOrder;

/**
 *
 * @author Chad
 */
public final class IntegerDomain implements Domain<Integer> {

    @Override
    public String getName() {
        return "Integer";
    }

    @Override
    public SortedSet<Integer> getRange() {
        return new RangeSet<>(
                Integer.class,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                naturalOrder(),
                (i) -> i + 1,
                (i) -> i - 1);
    }
}
