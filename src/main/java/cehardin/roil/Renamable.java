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
public interface Renamable<T> {
    static <T extends Renamable<T>> BiFunction<T, UnaryOperator<AttributeName>, T> getRenameBiFunction() {
        return (t,f) -> t.getRenameFunction().apply(f);
    };
    
    static <T extends Renamable<T>> UnaryOperator<T> getRenameUnaryOperator(UnaryOperator<AttributeName> renameFunction) {
        return (t) -> t.getRenameFunction().apply(renameFunction);
    }
    
    
    Function<UnaryOperator<AttributeName>, T> getRenameFunction();
    
    default Supplier<T> getRenameSupplier(UnaryOperator<AttributeName> renameFunction) {
        return () -> getRenameFunction().apply(renameFunction);
    }
    
    default T rename(UnaryOperator<AttributeName> renameFunction) {
        return getRenameFunction().apply(renameFunction);
    }
}
