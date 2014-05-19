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

import java.util.Set;

/**
 * A relation is like a "table". It contains "rows" ({@link Tuple}) and conforms
 * to a {@link RelationSchema}.
 * <p>
 * @author Chad
 */
public interface Relation extends Comparable<Relation>, Projectable<Relation>, Selectable<Relation>, Renamable<Relation>, Deletable<Relation>, Modifiable<Relation> {

    /**
     * Get this relation's schema.
     * <p>
     * @return This relation's schema, never null.
     */
    RelationSchema getSchema();
    
    Set<Tuple> getTuples();
}
