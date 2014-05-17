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
package cehardin.roil.impl.algebra;

import cehardin.roil.Domain;
import cehardin.roil.Name;

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;


import cehardin.roil.RelationSchema;
import cehardin.roil.Tuple;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author Chad
 */
public final class IsTupleCompatibleWithSchema implements Predicate<RelationSchema> {
    private final Tuple tuple;
    
    public IsTupleCompatibleWithSchema(final Tuple tuple) {
        this.tuple = requireNonNull(tuple, "Tuple was null");
    }

    @Override
    public boolean test(RelationSchema schema) {
        final Map<Name,Domain<?>> fromSchema = requireNonNull(schema, "Schema was null").getAttributes();
        final Map<Name, Domain<?>> fromTuple = tuple.getDomains();
       
        
        return fromSchema.entrySet().containsAll(fromTuple.entrySet());
    }
    
    
}