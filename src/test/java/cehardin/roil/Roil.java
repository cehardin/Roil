/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cehardin.roil;

import cehardin.roil.Factory;
import cehardin.roil.RelationSchema;
import cehardin.roil.Name;
import cehardin.roil.Value;
import cehardin.roil.Attributes;
import cehardin.roil.Relation;
import cehardin.roil.Domain;
import cehardin.roil.Tuple;
import cehardin.roil.impl.memory.MemoryFactory;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static cehardin.roil.impl.spi.ImmutableMaps.*;
import static cehardin.roil.impl.spi.ImmutableSets.*;

/**
 *
 * @author Chad
 */
public class Roil {

    private Factory factory;

    @Before
    public void setUp() {
        factory = new MemoryFactory();
    }

    @Test
    public void createSchema() {
        final Name idName;
        final Name colorName;
        final Domain integerDomain;
        final Domain stringDomain;
        final Attributes idAttribute;
        final Attributes colorAttribute;
        final Set<Attributes> attributes;
        final RelationSchema relationSchema;
        final Value id1Value;
        final Value id2Value;
        final Value id3Value;
        final Value brownValue;
        final Value blueValue;
        final Value redValue;
        final Tuple brownTuple;
        final Tuple blueTuple;
        final Tuple redTuple;
        Relation relation;
        Relation snapshot;

        idName = factory.createName("ID");
        colorName = factory.createName("Color");
        integerDomain = factory.createIntegerDomain();
        stringDomain = factory.createStringDomain();
        idAttribute = factory.createAttribute(idName, integerDomain);
        colorAttribute = factory.createAttribute(colorName, stringDomain);
        attributes = createImmutableSet(idAttribute, colorAttribute);
        relationSchema = factory.createRelationSchema(attributes);
        id1Value = factory.createValue(1, integerDomain);
        id2Value = factory.createValue(2, integerDomain);
        id3Value = factory.createValue(3, integerDomain);
        brownValue = factory.createValue("Brown", stringDomain);
        blueValue = factory.createValue("Blue", stringDomain);
        redValue = factory.createValue("Red", stringDomain);
        brownTuple = factory.createTuple(createImmutableMap(createImmutableEntry(idAttribute, id1Value), createImmutableEntry(colorAttribute, brownValue)));
        blueTuple = factory.createTuple(createImmutableMap(createImmutableEntry(idAttribute, id2Value), createImmutableEntry(colorAttribute, blueValue)));
        redTuple = factory.createTuple(createImmutableMap(createImmutableEntry(idAttribute, id3Value), createImmutableEntry(colorAttribute, redValue)));
        relation = factory.createRelation(relationSchema);

        assertEquals(0, relation.count());
        snapshot = relation;

        relation = relation.add(brownTuple);
        assertEquals(1, relation.count());
        assertEquals(0, snapshot.count());
        snapshot = relation;

        relation = relation.add(blueTuple);
        assertEquals(2, relation.count());
        assertEquals(1, snapshot.count());
        snapshot = relation;

        relation = relation.add(redTuple);
        assertEquals(3, relation.count());
        assertEquals(2, snapshot.count());
        snapshot = relation;

        relation = relation.add(brownTuple, blueTuple, redTuple);
        assertEquals(3, relation.count());
        assertEquals(3, snapshot.count());

        assertEquals(1, relation.select((t) -> t.getAttributesAndValues().get(colorAttribute).equals(brownValue)).count());
        snapshot = relation.remove((t) -> t.getAttributesAndValues().get(colorAttribute).equals(brownValue));
        assertEquals(1, relation.select((t) -> t.getAttributesAndValues().get(colorAttribute).equals(brownValue)).count());
        assertEquals(0, snapshot.select((t) -> t.getAttributesAndValues().get(colorAttribute).equals(brownValue)).count());

        relation = relation.update((t) -> t.getAttributesAndValues().get(colorAttribute).equals(redValue), (t) -> t.change(addToImmutableMap(t.getAttributesAndValues(), colorAttribute, brownValue)));
        assertEquals(2, relation.select((t) -> t.getAttributesAndValues().get(colorAttribute).equals(brownValue)).count());
        assertEquals(0, snapshot.select((t) -> t.getAttributesAndValues().get(colorAttribute).equals(brownValue)).count());

        snapshot = relation.project(createImmutableSet(idAttribute));
        assertEquals(0, snapshot.select((t) -> t.getAttributesAndValues().getOrDefault(colorAttribute, factory.createValue()).equals(brownValue)).count());
        assertEquals(1, snapshot.select((t) -> t.getAttributesAndValues().get(idAttribute).equals(id1Value)).count());

        relation = relation.remove((t) -> true);
        assertEquals(0, relation.count());
        assertEquals(3, snapshot.count());

    }
}
