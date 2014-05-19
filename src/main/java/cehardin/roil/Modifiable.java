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
public interface Modifiable<T> {
    public static final class Modify {
        private final Tuple matchTuple;
        private final Tuple newTuple;
        
        public Modify(Tuple matchTuple, Tuple newTuple) {
            this.matchTuple = matchTuple;
            this.newTuple = newTuple;
        }
        
        public Tuple getMatchTuple() {
            return matchTuple;
        }
        
        public Tuple getNewTuple() {
            return newTuple;
        }
    }
    
    static <T extends Modifiable<T>> BiFunction<T, Modify, T> getModifyBiFunction() {
        return (t, m) -> t.getModifyFunction().apply(m);
    }
    
    static <T extends Modifiable<T>> UnaryOperator<T> getModifiyUnaryOperator(final Modify m) {
        return (t) -> t.getModifyFunction().apply(m);
    }
    
    Function<Modify, T> getModifyFunction();
    
    default Supplier<T> getModifySupplier(final Modify m) {
        return () -> getModifyFunction().apply(m);
    }
    
    default T modify(final Modify m) {
        return getModifyFunction().apply(m);
    }
    
}
