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
import cehardin.roil.Relation;
import cehardin.roil.RelationSchema;
import cehardin.roil.Tuple;
import cehardin.roil.algebra.Delete;
import cehardin.roil.exception.DeleteFailedException;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

/**
 *
 * @author Chad
 */
public abstract class AbstractDeleteOperator implements UnaryOperator<Relation> {
    private final Delete delete;
    private final IsTupleCompatibleWithSchema isTupleCompatibleWithSchema;
    private final ExtractUtilizedPrimaryKey extractUtilizedPrimaryKey;
    private final ExtractUtilizedSecondaryKeys extractUtilizedSecondaryKeys;
    
    protected AbstractDeleteOperator(Delete delete) {
        this.delete = requireNonNull(delete, "Delete was null");
        this.isTupleCompatibleWithSchema = new IsTupleCompatibleWithSchema(delete.getMatcher());
        this.extractUtilizedPrimaryKey = new ExtractUtilizedPrimaryKey(this.delete.getMatcher().getValues().keySet());
        this.extractUtilizedSecondaryKeys = new ExtractUtilizedSecondaryKeys(this.delete.getMatcher().getValues().keySet());
    }

    @Override
    public Relation apply(Relation input) throws DeleteFailedException {
        final RelationSchema schema = requireNonNull(input, "Input was null").getSchema();
        final Tuple matcher = delete.getMatcher();
        final Optional<Name> primaryKey;
        final Optional<Set<Set<Name>>> secondaryKeys;
        
        if(isTupleCompatibleWithSchema.negate().test(schema)) {
            throw new DeleteFailedException(format("%s is not compatible with %s", matcher, schema));
        }
        
        primaryKey = extractUtilizedPrimaryKey.apply(schema);
        secondaryKeys = extractUtilizedSecondaryKeys.apply(schema);
        
        if(primaryKey.isPresent()) {
            if(secondaryKeys.isPresent()) {
                return delete(input, primaryKey.get(), secondaryKeys.get(), matcher);
            }
            else {
                return delete(input, primaryKey.get(), matcher);
            }
        }
        else if(secondaryKeys.isPresent()) {
            return delete(input, secondaryKeys.get(), matcher);
        }
        else {
            return delete(input, matcher);
        }
    }
    
    protected abstract Relation delete(Relation input, Tuple matcher);
    
    protected abstract Relation delete(Relation input, Name primaryKey, Tuple matcher);
    
    protected abstract Relation delete(Relation input, Set<Set<Name>> secondaryKeys, Tuple matcher);
    
    protected abstract Relation delete(Relation input, Name primaryKey, Set<Set<Name>> secondaryKeys, Tuple matcher);
}
