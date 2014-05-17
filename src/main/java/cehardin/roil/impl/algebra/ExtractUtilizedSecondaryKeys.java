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

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableSet;

import cehardin.roil.Name;
import cehardin.roil.Relation;
import cehardin.roil.RelationSchema;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 *
 * @author Chad
 */
public final class ExtractUtilizedSecondaryKeys implements Function<RelationSchema, Optional<Set<Set<Name>>>> {
    private final Set<Name> attributeNames;
    
    public ExtractUtilizedSecondaryKeys(Set<Name> attributeNames) {
        this.attributeNames = unmodifiableSet(new HashSet<>(requireNonNull(attributeNames, "Attribute NAmes was null")));
        
        if(this.attributeNames.contains(null)) {
            throw new NullPointerException(format("Attribute Names set contains null : %s", this.attributeNames));
        }
    }

    @Override
    public Optional<Set<Set<Name>>> apply(RelationSchema relationSchema) {
        final Set<Set<Name>> secondaryKeys = new HashSet<>();
        
        for(Set<Name> secondaryKey : relationSchema.getSecondaryKeys()) {
            if(secondaryKey.containsAll(attributeNames)) {
                secondaryKeys.add(secondaryKey);
            }
        }
        
        if(secondaryKeys.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(unmodifiableSet(secondaryKeys));
        }
    }
}
