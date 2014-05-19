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
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 *
 * @author Chad
 */
public interface Deletable<T> {
    
    static <T extends Deletable<T>> BiFunction<T, Tuple, T> getDeleteBiFunction() {
        return (t, tuple) -> t.getDeleteFunction().apply(tuple);
    }
    
    static <T extends Deletable<T>> UnaryOperator<T> getDeleteUnaryOperator(final Tuple tuple) {
        return (t) -> t.getDeleteFunction().apply(tuple);
    }
    
    Function<Tuple, T> getDeleteFunction();
    
    default Supplier<T> getDeleteSupplier(final Tuple tuple) {
        return () -> getDeleteFunction().apply(tuple);
    }
    
    default T delete(final Tuple tuple) {
        return getDeleteFunction().apply(tuple);
    }
}
