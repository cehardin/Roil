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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.Collections.unmodifiableMap;

/**
 *
 * @author Chad
 */
public interface Selectable<T> {

    static abstract class AbstractSelect {

        public enum Type {

            CONSTANT,
            ATTRIBUTE
        };
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

    Set<AttributeName> getAttributeNames();

    Function<SelectByAttribute, T> getSelectByAttributeFunction();

    Function<SelectByConstant, T> getSelectByConstantFunction();

    default T selectByAttributeName(SelectByAttribute selectByAttribute) {
        return getSelectByAttributeSupplier(selectByAttribute).get();
    }

    default T selectByAttributeName(BooleanOperator operator, AttributeName targetAttributeName, AttributeName attributeName) {
        return selectByAttributeName(new SelectByAttribute(operator, targetAttributeName, attributeName));
    }

    default T selectByConstant(SelectByConstant selectByConstant) {
        return getSelectByConstantSupplier(selectByConstant).get();
    }
    
    default T selectByConstant(BooleanOperator operator, AttributeName targetAttributeName, Value constant) {
        return selectByConstant(new SelectByConstant(operator, targetAttributeName, constant));
    }

    ///START SIMPLE FUNCTIONS
    default Supplier<T> getSelectByAttributeSupplier(SelectByAttribute selectByAttribute) {
        return () -> getSelectByAttributeFunction().apply(selectByAttribute);
    }

    default Supplier<T> getSelectByConstantSupplier(SelectByConstant selectByConstant) {
        return () -> getSelectByConstantFunction().apply(selectByConstant);
    }

    default Supplier<T> getSelectByAttributeSupplier(BooleanOperator operator, AttributeName targetAttributeName, AttributeName attributeName) {
        return getSelectByAttributeSupplier(new SelectByAttribute(operator, targetAttributeName, attributeName));
    }

    default Supplier<T> getSelectByConstantSupplier(BooleanOperator operator, AttributeName targetAttributeName, Value constant) {
        return getSelectByConstantSupplier(new SelectByConstant(operator, targetAttributeName, constant));
    }

    default Function<AttributeName, T> getSelectByAttributeFunction(final BooleanOperator operator, final AttributeName targetAttributeName) {
        return (attributeName) -> getSelectByAttributeFunction().apply(new SelectByAttribute(operator, targetAttributeName, attributeName));
    }

    default Function<Value, T> getSelectByConstantFunction(final BooleanOperator operator, final AttributeName targetAttributeName) {
        return (constant) -> getSelectByConstantFunction().apply(new SelectByConstant(operator, targetAttributeName, constant));
    }

    default Function<AttributeName, T> getSelectByConstantFunction(final BooleanOperator operator, final Value constant) {
        return (targetAttributeName) -> getSelectByConstantFunction().apply(new SelectByConstant(operator, targetAttributeName, constant));
    }

    default Function<BooleanOperator, T> getSelectByConstantFunction(final AttributeName targetAttributeName, final Value constant) {
        return (operator) -> getSelectByConstantFunction().apply(new SelectByConstant(operator, targetAttributeName, constant));
    }

    default BiFunction<AttributeName, AttributeName, T> getSelectByAttributeBiFunction(final BooleanOperator operator) {
        return (targetAttributeName, attributeName) -> getSelectByAttributeFunction().apply(new SelectByAttribute(operator, targetAttributeName, attributeName));
    }

    default BiFunction<AttributeName, Value, T> getSelectByConstantBiFunction(final BooleanOperator operator) {
        return (targetAttributeName, constant) -> getSelectByConstantFunction().apply(new SelectByConstant(operator, targetAttributeName, constant));
    }

    default BiFunction<BooleanOperator, AttributeName, T> getSelectByAttributeBiFunction(final AttributeName targetAttributeName) {
        return (operator, attributeName) -> getSelectByAttributeFunction().apply(new SelectByAttribute(operator, targetAttributeName, attributeName));
    }

    default BiFunction<BooleanOperator, Value, T> getSelectByConstantBiFunction(final AttributeName targetAttributeName) {
        return (operator, constant) -> getSelectByConstantFunction().apply(new SelectByConstant(operator, targetAttributeName, constant));
    }

    default BiFunction<BooleanOperator, AttributeName, T> getSelectByConstantBiFunction(final Value constant) {
        return (operator, targetAttributeName) -> getSelectByConstantFunction().apply(new SelectByConstant(operator, targetAttributeName, constant));
    }

    ///END SIMPLE FUNCTIONS
    ///START BY OPERATORS
    default Map<BooleanOperator, BiFunction<AttributeName, AttributeName, T>> getSelectByAttributeFunctionsByOperator() {
        final Map<BooleanOperator, BiFunction<AttributeName, AttributeName, T>> functions = new HashMap<>();

        for (final BooleanOperator operator : BooleanOperator.values()) {
            functions.put(operator, getSelectByAttributeBiFunction(operator));
        }

        return unmodifiableMap(functions);
    }

    default Map<BooleanOperator, BiFunction<AttributeName, Value, T>> getSelectByConstantFunctionsByOperator() {
        final Map<BooleanOperator, BiFunction<AttributeName, Value, T>> functions = new HashMap<>();

        for (final BooleanOperator operator : BooleanOperator.values()) {
            functions.put(operator, getSelectByConstantBiFunction(operator));
        }

        return unmodifiableMap(functions);
    }

    default Map<BooleanOperator, Function<AttributeName, T>> getSelectByAttributeFunctionsByOperator(final AttributeName targetAttributeName) {
        final Map<BooleanOperator, Function<AttributeName, T>> functions = new HashMap<>();

        getSelectByAttributeFunctionsByOperator().entrySet().stream().forEach((function) -> {
            functions.put(function.getKey(), getSelectByAttributeFunction(function.getKey(), targetAttributeName));
        });

        return unmodifiableMap(functions);
    }

    default Map<BooleanOperator, Function<Value, T>> getSelectByConstantFunctionsByOperator(final AttributeName targetAttributeName) {
        final Map<BooleanOperator, Function<Value, T>> functions = new HashMap<>();

        getSelectByConstantFunctionsByOperator().entrySet().stream().forEach((function) -> {
            functions.put(function.getKey(), getSelectByConstantFunction(function.getKey(), targetAttributeName));
        });

        return unmodifiableMap(functions);
    }

    default Map<BooleanOperator, Supplier<T>> getSelectByAttributeSuppliersByOperator(final AttributeName targetAttributeName, final AttributeName attributeName) {
        final Map<BooleanOperator, Supplier<T>> suppliers = new HashMap<>();

        getSelectByAttributeFunctionsByOperator(targetAttributeName).entrySet().stream().forEach((function) -> {
            suppliers.put(function.getKey(), getSelectByAttributeSupplier(function.getKey(), targetAttributeName, attributeName));
        });

        return unmodifiableMap(suppliers);
    }

    default Map<BooleanOperator, Supplier<T>> getSelectByConstantSuppliersByOperator(final AttributeName targetAttributeName, final Value constant) {
        final Map<BooleanOperator, Supplier<T>> suppliers = new HashMap<>();

        getSelectByConstantFunctionsByOperator(targetAttributeName).entrySet().stream().forEach((function) -> {
            suppliers.put(function.getKey(), getSelectByConstantSupplier(function.getKey(), targetAttributeName, constant));
        });

        return unmodifiableMap(suppliers);
    }

    ///END BY OPERATORS
    ///START BY TARGET ATTRIBUTE NAMES
    default Map<AttributeName, BiFunction<BooleanOperator, AttributeName, T>> getSelectByAttributeFunctionsByTargetAttributeNames() {
        final Map<AttributeName, BiFunction<BooleanOperator, AttributeName, T>> functions = new HashMap<>();

        getAttributeNames().stream().forEach((targetAttributeName) -> {
            functions.put(targetAttributeName, getSelectByAttributeBiFunction(targetAttributeName));
        });

        return unmodifiableMap(functions);
    }

    default Map<AttributeName, BiFunction<BooleanOperator, Value, T>> getSelectByConstantFunctionsByTargetAttributeNames() {
        final Map<AttributeName, BiFunction<BooleanOperator, Value, T>> functions = new HashMap<>();

        getAttributeNames().stream().forEach((targetAttributeName) -> {
            functions.put(targetAttributeName, getSelectByConstantBiFunction(targetAttributeName));
        });

        return unmodifiableMap(functions);
    }

    default Map<AttributeName, Function<AttributeName, T>> getSelectByAttributeFunctionsByTargetAttributeNames(BooleanOperator operator) {
        final Map<AttributeName, Function<AttributeName, T>> functions = new HashMap<>();

        getAttributeNames().stream().forEach((targetAttributeName) -> {
            functions.put(targetAttributeName, getSelectByAttributeFunction(operator, targetAttributeName));
        });

        return unmodifiableMap(functions);
    }

    default Map<AttributeName, Function<Value, T>> getSelectByConstantFunctionsByTargetAttributeNames(BooleanOperator operator) {
        final Map<AttributeName, Function<Value, T>> functions = new HashMap<>();

        getAttributeNames().stream().forEach((targetAttributeName) -> {
            functions.put(targetAttributeName, getSelectByConstantFunction(operator, targetAttributeName));
        });

        return unmodifiableMap(functions);
    }

    default Map<AttributeName, Function<BooleanOperator, T>> getSelectByAttributeFunctionsByTargetAttributeNames(AttributeName attributeName) {
        final Map<AttributeName, Function<BooleanOperator, T>> functions = new HashMap<>();

        getAttributeNames().stream().forEach((targetAttributeName) -> {
            functions.put(targetAttributeName, (operator) -> getSelectByAttributeFunction().apply(new SelectByAttribute(operator, targetAttributeName, attributeName)));
        });

        return unmodifiableMap(functions);
    }

    default Map<AttributeName, Function<BooleanOperator, T>> getSelectByConstantFunctionsByTargetAttributeNames(Value constant) {
        final Map<AttributeName, Function<BooleanOperator, T>> functions = new HashMap<>();

        getAttributeNames().stream().forEach((targetAttributeName) -> {
            functions.put(targetAttributeName, getSelectByConstantFunction(targetAttributeName, constant));
        });

        return unmodifiableMap(functions);
    }

    default Map<AttributeName, Supplier<T>> getSelectByAttributeSuppliersByTargetAttributeNames(BooleanOperator operator, AttributeName attributeName) {
        final Map<AttributeName, Supplier<T>> suppliers = new HashMap<>();

        getAttributeNames().stream().forEach((targetAttributeName) -> {
            suppliers.put(targetAttributeName, getSelectByAttributeSupplier(new SelectByAttribute(operator, targetAttributeName, attributeName)));
        });

        return unmodifiableMap(suppliers);
    }

    default Map<AttributeName, Supplier<T>> getSelectByConstantSuppliersByTargetAttributeNames(BooleanOperator operator, Value constant) {
        final Map<AttributeName, Supplier<T>> suppliers = new HashMap<>();

        getAttributeNames().stream().forEach((targetAttributeName) -> {
            suppliers.put(targetAttributeName, () -> getSelectByConstantFunction().apply(new SelectByConstant(operator, targetAttributeName, constant)));
        });

        return unmodifiableMap(suppliers);
    }

    ///END BY TARGET ATTRIBUTE NAMES
}
