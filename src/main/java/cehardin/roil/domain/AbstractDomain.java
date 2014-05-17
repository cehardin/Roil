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

import static java.lang.String.format;

import cehardin.roil.Domain;
import cehardin.roil.exception.ValueNotInDomainException;

/**
 *
 * @author Chad
 */
abstract class AbstractDomain<T> implements Domain<T> {

    @Override
    public final boolean isInfinite() {
        return !isFinite();
    }

    @Override
    public final void check(T o) throws ValueNotInDomainException {
        if(!isIn(o)) {
            throw new ValueNotInDomainException(format("Value is not in domain : %s", o));
        }
    }
}
