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
package cehardin.roil.impl;

import static java.util.Objects.requireNonNull;

import cehardin.roil.Relation;
import cehardin.roil.RelationSchema;

/**
 *
 * @author Chad
 */
public abstract class AbstractRelation implements Relation {
    private final RelationSchema schema;
    
    protected AbstractRelation(RelationSchema schema) {
        this.schema = requireNonNull(schema, "Schema was null");
    }
    @Override
    public final RelationSchema getSchema() {
        return schema;
    }

    @Override
    public final int compareTo(Relation o) {
        return getSchema().compareTo(o.getSchema());
    }
    
}
