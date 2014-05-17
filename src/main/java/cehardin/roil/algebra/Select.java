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
package cehardin.roil.algebra;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.hash;

import cehardin.roil.Name;
import cehardin.roil.Value;
import java.util.Optional;

/**
 *
 * @author Chad
 */
public final class Select {
    public enum Operator {
      EQ,
      NEQ,
      GT,
      GTE,
      LT,
      LTE
    };
    
    public enum Type {
        CONSTANT,
        ATTRIBUTE
    };
    
    private final Operator operator;
    private final Type type;
    private final Name targetAttributeName;
    private final Optional<Value> constant;
    private final Optional<Name> attributeName;
    
    public Select(Operator operator, Name targetAttributeName, Value constant) {
        this(operator, Type.CONSTANT, targetAttributeName, Optional.of(requireNonNull(constant, "Constant was null")), Optional.empty());
    }
    
    public Select(Operator operator, Name targetAttributeName,Name attributeName) {
        this(operator, Type.ATTRIBUTE, targetAttributeName, Optional.empty(), Optional.of(requireNonNull(attributeName, "Attribute Name was null")));
    }

    private Select(Operator operator, Type type, Name targetAttributeName, Optional<Value> constant, Optional<Name> attributeName) {
        this.operator = requireNonNull(operator, "Operator was null");
        this.type = requireNonNull(type, "Type was null");
        this.targetAttributeName = requireNonNull(targetAttributeName, "Target attribute name was null");
        this.constant = requireNonNull(constant, "Constant was null");
        this.attributeName = requireNonNull(attributeName, "Attribute Name was null");
        
        if(this.type == Type.CONSTANT) {
            if(!constant.isPresent()) {
                throw new IllegalArgumentException(format("Type was %s yet constant was not present", this.type));
            }
            if(attributeName.isPresent()) {
                throw new IllegalArgumentException(format("Type was %s yet attribute name was present", this.type));
            }
        }
        else {
            if(constant.isPresent()) {
                throw new IllegalArgumentException(format("Type was %s yet constant was present", this.type));
            }
            if(!attributeName.isPresent()) {
                throw new IllegalArgumentException(format("Type was %s yet attribute name was not present", this.type));
            }
        }
    }
    
    public Operator getOperator() {
        return operator;
    }
    
    public Type getType() {
        return type;
    }
    
    public Name getTargetAttributeName() {
        return targetAttributeName;
    }
    
    public Value getConstant() {
        return constant.orElseThrow(() -> new IllegalStateException(format("Cannot get constant for type ", getType())));
    }
    
    public Name getAttributeName() {
        return attributeName.orElseThrow(()-> new IllegalStateException(format("Cannot get attribute name for type ", getType())));
    }
    
    @Override 
    public int hashCode() {
        return hash(operator, type, targetAttributeName, constant, attributeName);
    }
    
    @Override
    public boolean equals(Object o) {
        final boolean result;
        
        if(o == null) {
            result = false;
        }
        else if(o == this) {
            result = true;
        }
        else if(getClass().isInstance(o)) {
            final Select other = getClass().cast(o);
            result = operator.equals(other.operator) &&
                    type.equals(other.type) &&
                    targetAttributeName.equals(other.targetAttributeName) &&
                    constant.equals(other.constant) &&
                    attributeName.equals(other.attributeName);
        }
        else {
            result = false;
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return format(
                "%s : operator=%s, type=%s, targetAttributeName=%s, constant=%s, attributeName=%s", 
                super.toString(), 
                operator, 
                type, 
                targetAttributeName,
                constant, 
                attributeName);
    }

}
