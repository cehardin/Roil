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

import java.util.Comparator;

/**
 *
 * @author Chad
 */
public class Ranges {
    private static final class RangeImpl<T> implements Range<T> {
        private final EndPointType startEndpointType;
        private final EndPointType stopEndpointType;
        private final T startEndpoint;
        private final T stopEndpoint;
        private final Comparator<T> comparator;

        public RangeImpl(EndPointType startEndpointType, EndPointType stopEndpointType, T startEndpoint, T stopEndpoint, Comparator<T> comparator) {
            this.startEndpointType = startEndpointType;
            this.stopEndpointType = stopEndpointType;
            this.startEndpoint = startEndpoint;
            this.stopEndpoint = stopEndpoint;
            this.comparator = comparator;
        }
        
        
        @Override
        public EndPointType getStartEndpointType() {
            return startEndpointType;
        }

        @Override
        public EndPointType getStopEndpointType() {
            return stopEndpointType;
        }

        @Override
        public T getStartEnpoint() {
            return startEndpoint;
        }

        @Override
        public T getStopEndpoint() {
            return stopEndpoint;
        }

        @Override
        public Comparator<T> getComparator() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    private static final class IntegerRange implements Range<Integer> {

        @Override
        public Comparator<Integer> getComparator() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public EndPointType getStartEndpointType() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Integer getStartEnpoint() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Integer getStopEndpoint() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public EndPointType getStopEndpointType() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}
