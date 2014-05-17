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
import cehardin.roil.algebra.Project;
import cehardin.roil.exception.ProjectFailedException;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 *
 * @author Chad
 */
public abstract class AbstractProjectOperator implements UnaryOperator<Relation> {
    private final Project project;
    
    protected AbstractProjectOperator(Project project) {
        this.project = requireNonNull(project, "Project was null");
    }

    @Override
    public Relation apply(Relation input) throws ProjectFailedException {
        final RelationSchema schema = requireNonNull(input, "Input was null").getSchema();
        final Set<Name> schemaAttributes = schema.getAttributes().keySet();
        final Set<Name> projectAttributes = project.getAttributesNames();
        
        if(!schemaAttributes.containsAll(projectAttributes)) {
            throw new ProjectFailedException(format("The attriubtes %s are not all contained in %s", projectAttributes, schema));
        }
        return project(input, projectAttributes);
    }
    
    protected abstract Relation project(Relation input, Set<Name> attributes) throws ProjectFailedException;
    
}
