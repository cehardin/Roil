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

import cehardin.roil.Name;

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

import cehardin.roil.Relation;
import cehardin.roil.RelationSchema;
import cehardin.roil.algebra.Rename;
import cehardin.roil.exception.ProjectFailedException;
import cehardin.roil.exception.RenameFailedException;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Chad
 */
public abstract class AbstractRenameOperator implements UnaryOperator<Relation> {
    private final Rename rename;
    
    protected AbstractRenameOperator(Rename rename) {
        this.rename = requireNonNull(rename, "Rename was null");
    }
    @Override
    public Relation apply(Relation input) throws RenameFailedException {
        final RelationSchema schema = requireNonNull(input, "Input was null").getSchema();
        final Set<Name> schemaAttributes = schema.getAttributes().keySet();
        final Set<Name> renameAttributes = rename.getMapping().keySet();
        
        if(!schemaAttributes.containsAll(renameAttributes)) {
            throw new ProjectFailedException(format("The attriubtes %s are not all contained in %s", renameAttributes, schema));
        }
        
        return rename(input, rename.getMapping());
    }
    
    protected abstract Relation rename(Relation input, Map<Name, Name> nameMapping) throws RenameFailedException;
    
}
