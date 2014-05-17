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
package cehardin.roil.util;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Optional;

/**
 *
 * @author Chad
 */
public class Optionals {
    private static class OptionalComparator<T> implements Comparator<Optional<T>> {
        private final Comparator<T> valueComparator;
        
        public OptionalComparator(Comparator<T> valueComparator) {
            this.valueComparator = requireNonNull(valueComparator, "Value comparator was null");
        }
        
        @Override
        public int compare(Optional<T> o1, Optional<T> o2) {
            final int result;
            
            if(o1 == null) {
                if(o2 == null) {
                    result = 0;
                }
                else {
                    result = -1;
                }
            }
            else if(o2 == null) {
                result = 1;
            }
            else {
                if(!o1.isPresent()) {
                    if(!o2.isPresent()) {
                        result = 0;
                    }
                    else {
                        result = -1;
                    }
                }
                else if(!o2.isPresent()) {
                    result = 1;
                }
                else {
                    result = valueComparator.compare(o1.get(), o2.get());
                }
            }
            
            return result;
        }
    }
    
    public static <T extends Comparable> Comparator<Optional<T>> optionalComparator() {
        return new OptionalComparator<T>(Comparator.naturalOrder());
    }
    
    public static <T> Comparator<Optional<T>> optionalComparator(Comparator<T> valueComparator) {
        return new OptionalComparator<>(valueComparator);
    }
}
