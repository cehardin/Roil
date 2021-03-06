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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


/**
 *
 * @author Chad
 */
public interface Projectable<T> {
    
    static <T extends Projectable<T>> BiFunction<T, Predicate<AttributeName>, T> getProjectBiFunction() {
        
        return (t,p) -> t.getProjectFunction().apply(p);
    }
    
    static <T extends Projectable<T>> UnaryOperator<T> getProjectUnaryOperator(Predicate<AttributeName> attributeNamePredicate) {
        return (t) -> t.getProjectFunction().apply(attributeNamePredicate);
    }
    
    Function<Predicate<AttributeName>, T> getProjectFunction();
    
    default Supplier<T> getProjectSupplier(Predicate<AttributeName> attributeNamePredicate) {
        return () -> getProjectFunction().apply(attributeNamePredicate);
    }
    
    default T project(Predicate<AttributeName> attributeNamePredicate) {
        return getProjectFunction().apply(attributeNamePredicate);
    }
}
