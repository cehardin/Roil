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

import cehardin.roil.Domain.BooleanOperator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 *
 * @author Chad
 */
public interface Selectable<T> {

    static abstract class AbstractSelect {

        private final Domain.BooleanOperator operator;
        private final AttributeName targetAttributeName;

        protected AbstractSelect(Domain.BooleanOperator operator, AttributeName targetAttributeName) {
            this.operator = requireNonNull(operator, "Operator was null");
            this.targetAttributeName = requireNonNull(targetAttributeName, "Target Attribute Name was null");
        }

        public BooleanOperator getOperator() {
            return operator;
        }

        public AttributeName getTargetAttributeName() {
            return targetAttributeName;
        }
    }

    public static class SelectByAttribute extends AbstractSelect {

        private final AttributeName attributeName;

        public SelectByAttribute(Domain.BooleanOperator operator, AttributeName targetAttributeName, AttributeName attributeName) {
            super(operator, targetAttributeName);
            this.attributeName = requireNonNull(attributeName, "Attribute Name was null");
        }

        public AttributeName getAttributeName() {
            return attributeName;
        }
    }

    public static class SelectByConstant extends AbstractSelect {

        private final Value<?> constant;

        public SelectByConstant(Domain.BooleanOperator operator, AttributeName targetAttributeName, Value<?> constant) {
            super(operator, targetAttributeName);
            this.constant = requireNonNull(constant, "Constant was null");
        }

        public Value getConstant() {
            return constant;
        }
    }

    static <T extends Selectable<T>> BiFunction<T, SelectByAttribute, T> getSelectByAttributeBiFunction() {
        return (t,s) -> t.getSelectByAttributeFunction().apply(s);
    }

    static <T extends Selectable<T>> BiFunction<T, SelectByConstant, T> getSelectByConstantBiFunction() {
        return (t,s) -> t.getSelectByConstantFunction().apply(s);
    }
    
    static <T extends Selectable<T>> UnaryOperator<T> getSelectByAttributeUnaryOperator(SelectByAttribute s) {
        return (t) -> t.getSelectByAttributeFunction().apply(s);
    }
    
    static <T extends Selectable<T>> UnaryOperator<T> getSelectByConstantUnaryOperator(SelectByConstant s) {
        return (t) -> t.getSelectByConstantFunction().apply(s);
    }
    
    Function<SelectByAttribute, T> getSelectByAttributeFunction();
    
    Function<SelectByConstant, T> getSelectByConstantFunction();
    
    default Supplier<T> getSelectByAttributeSupplier(SelectByAttribute s) {
        return () -> getSelectByAttributeFunction().apply(s);
    }
    
    default Supplier<T> getSelectByConstantSupplier(SelectByConstant s) {
        return () -> getSelectByConstantFunction().apply(s);
    }

    default T select(SelectByAttribute s) {
        return getSelectByAttributeFunction().apply(s);
    }

    default T select(SelectByConstant s) {
        return getSelectByConstantFunction().apply(s);
    }
}
