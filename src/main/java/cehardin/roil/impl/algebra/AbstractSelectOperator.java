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
import cehardin.roil.Value;

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

import cehardin.roil.algebra.Select;
import cehardin.roil.exception.SelectFailedException;
import java.util.function.UnaryOperator;

/**
 *
 * @author Chad
 */
public abstract class AbstractSelectOperator implements UnaryOperator<Relation> {

    private final Select select;

    protected AbstractSelectOperator(Select select) {
        this.select = requireNonNull(select, "Select was null");
    }

    @Override
    public final Relation apply(Relation input) throws SelectFailedException {
        final Name attributeName = select.getAttributeName();
        if (!requireNonNull(input, "Input was null").getSchema().getAttributes().keySet().contains(attributeName)) {
            throw new SelectFailedException(format("Relation %s does not contain attribute %s", input, attributeName));
        }

        final Select.Operator operator = select.getOperator();
        final Select.Type type = select.getType();
        final Name target = select.getTargetAttributeName();

        switch (operator) {
            case EQ:
                switch (type) {
                    case CONSTANT:
                        return eq(input, target, select.getConstant());
                    case ATTRIBUTE:
                        return eq(input, target, select.getAttributeName());
                    default:
                        throw new SelectFailedException(format("Unrecognized type %s", type));
                }
            case NEQ:
                switch (type) {
                    case CONSTANT:
                        return neq(input, target, select.getConstant());
                    case ATTRIBUTE:
                        return neq(input, target, select.getAttributeName());
                    default:
                        throw new SelectFailedException(format("Unrecognized type %s", type));
                }
            case GT:
                switch (type) {
                    case CONSTANT:
                        return gt(input, target, select.getConstant());
                    case ATTRIBUTE:
                        return gt(input, target, select.getAttributeName());
                    default:
                        throw new SelectFailedException(format("Unrecognized type %s", type));
                }
            case GTE:
                switch (type) {
                    case CONSTANT:
                        return gte(input, target, select.getConstant());
                    case ATTRIBUTE:
                        return gte(input, target, select.getAttributeName());
                    default:
                        throw new SelectFailedException(format("Unrecognized type %s", type));
                }
            case LT:
                switch (type) {
                    case CONSTANT:
                        return lt(input, target, select.getConstant());
                    case ATTRIBUTE:
                        return lt(input, target, select.getAttributeName());
                    default:
                        throw new SelectFailedException(format("Unrecognized type %s", type));
                }
            case LTE:
                switch (type) {
                    case CONSTANT:
                        return lte(input, target, select.getConstant());
                    case ATTRIBUTE:
                        return lte(input, target, select.getAttributeName());
                    default:
                        throw new SelectFailedException(format("Unrecognized type %s", type));
                }
            default:
                throw new SelectFailedException(format("Unrecognized operator %s", operator));
        }
    }

    protected abstract Relation eq(Relation input, Name target, Value constant) throws SelectFailedException;

    protected abstract Relation eq(Relation input, Name target, Name attribute) throws SelectFailedException;

    protected abstract Relation neq(Relation input, Name target, Value constant) throws SelectFailedException;

    protected abstract Relation neq(Relation input, Name target, Name attribute) throws SelectFailedException;

    protected abstract Relation gt(Relation input, Name target, Value constant) throws SelectFailedException;

    protected abstract Relation gt(Relation input, Name target, Name attribute) throws SelectFailedException;

    protected abstract Relation gte(Relation input, Name target, Value constant) throws SelectFailedException;

    protected abstract Relation gte(Relation input, Name target, Name attribute) throws SelectFailedException;

    protected abstract Relation lt(Relation input, Name target, Value constant) throws SelectFailedException;

    protected abstract Relation lt(Relation input, Name target, Name attribute) throws SelectFailedException;

    protected abstract Relation lte(Relation input, Name target, Value constant) throws SelectFailedException;

    protected abstract Relation lte(Relation input, Name target, Name attribute) throws SelectFailedException;

}
