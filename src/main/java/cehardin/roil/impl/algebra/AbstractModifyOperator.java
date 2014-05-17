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
import cehardin.roil.Tuple;
import cehardin.roil.algebra.Modify;
import cehardin.roil.exception.DeleteFailedException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

import static java.lang.String.format;

/**
 *
 * @author Chad
 */
public abstract class AbstractModifyOperator implements UnaryOperator<Relation> {
    
    protected static class DetermineKeyUsage {
        private final ExtractUtilizedPrimaryKey extractUtilizedPrimaryKey;
        private final ExtractUtilizedSecondaryKeys extractUtilizedSecondaryKeys;
        
        public KeyUsage(Tuple tuple) {
            final Set<Name> attributeNames = requireNonNull(tuple, "Tuple was null").getValues().keySet();
            this.extractUtilizedPrimaryKey = new ExtractUtilizedPrimaryKey(attributeNames);
            this.extractUtilizedSecondaryKeys = new ExtractUtilizedSecondaryKeys(attributeNames);
        }

        public ExtractUtilizedPrimaryKey getExtractUtilizedPrimaryKey() {
            return extractUtilizedPrimaryKey.;
        }

        public ExtractUtilizedSecondaryKeys getExtractUtilizedSecondaryKeys() {
            return extractUtilizedSecondaryKeys;
        }
    }
    
    private final Modify modify;
    private final IsTupleCompatibleWithSchema isMatcherCompatibleWithSchema;
    private final IsTupleCompatibleWithSchema isReplacementCompatibleWithSchema;
    private final KeyUsage matcherKeyUsage;
    private final KeyUsage replacementKeyUsage;
    private final ExtractUtilizedPrimaryKey extractMatcherUtilizedPrimaryKey;
    private final ExtractUtilizedSecondaryKeys extractMatcherUtilizedSecondaryKeys;
    private final ExtractUtilizedPrimaryKey extractReplacementUtilizedPrimaryKey;
    private final ExtractUtilizedSecondaryKeys extractReplacementUtilizedSecondaryKeys;

    protected AbstractModifyOperator(Modify modify) {
        this.modify = requireNonNull(modify, "Modify was null");
        this.isMatcherCompatibleWithSchema = new IsTupleCompatibleWithSchema(modify.getMatcher());
        this.isReplacementCompatibleWithSchema = new IsTupleCompatibleWithSchema(modify.getReplacement());
        this.extractMatcherUtilizedPrimaryKey = new ExtractUtilizedPrimaryKey(this.modify.getMatcher().getValues().keySet());
        this.extractMatcherUtilizedSecondaryKeys = new ExtractUtilizedSecondaryKeys(this.modify.getMatcher().getValues().keySet());
        this.extractReplacementUtilizedPrimaryKey = new ExtractUtilizedPrimaryKey(this.modify.getReplacement().getValues().keySet());
        this.extractReplacementUtilizedSecondaryKeys = new ExtractUtilizedSecondaryKeys(this.modify.getMatcher().getValues().keySet());
    }

    @Override
    public Relation apply(Relation input) {
        final RelationSchema schema = requireNonNull(input, "Input was null").getSchema();
        final Optional<Name> primaryKey;
        final Optional<Set<Set<Name>>> secondaryKeys;

        if (isMatcherCompatibleWithSchema.negate().test(schema)) {
            throw new DeleteFailedException(format("Matcher %s is not compatible with %s", matcher, schema));
        }

        if (isReplacementCompatibleWithSchema.negate().test(schema)) {
            throw new DeleteFailedException(format("Replacement %s is not compatible with %s", matcher, schema));
        }

        primaryKey = extractUtilizedPrimaryKey.apply(schema);
        secondaryKeys = extractUtilizedSecondaryKeys.apply(schema);

    }
}
