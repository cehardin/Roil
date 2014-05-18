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

import java.util.function.Function;
import java.util.function.Predicate;


/**
 *
 * @author Chad
 */
public interface Projectable<T> {
     
    Function<Predicate<AttributeName>, T> getProjector();
    
    default T project(Predicate<AttributeName> attributeNamePredicate) {
        return getProjector().apply(attributeNamePredicate);
    }
}
