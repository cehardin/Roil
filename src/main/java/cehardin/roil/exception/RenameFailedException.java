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
package cehardin.roil.exception;

/**
 *
 * @author Chad
 */
public class RenameFailedException extends RuntimeException {

    /**
     * Creates a new instance of <code>RenameFailedException</code> without
     * detail message.
     */
    public RenameFailedException() {
    }

    /**
     * Constructs an instance of <code>RenameFailedException</code> with the
     * specified detail message.
     * <p>
     * @param msg the detail message.
     */
    public RenameFailedException(String msg) {
        super(msg);
    }
}
