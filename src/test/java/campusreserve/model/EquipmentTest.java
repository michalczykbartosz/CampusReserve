package campusreserve.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentTest {
    @Test
    void getEquipmentName_EquipmentCreated_ReturnsName() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 1, false, UUID.randomUUID());
        // act
        String name = e.getEquipmentName();
        // assert
        assertEquals("proj", name);
    }

    @Test
    void getEquipmentDescription_EquipmentCreated_ReturnsDescription() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 1, false, UUID.randomUUID());
        // act
        String desc = e.getEquipmentDescription();
        // assert
        assertEquals("desc", desc);
    }

    @Test
    void getEquipmentQuantity_EquipmentCreated_ReturnsQuantity() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 5, false, UUID.randomUUID());
        // act
        int qty = e.getEquipmentQuantity();
        // assert
        assertEquals(5, qty);
    }

    @Test
    void isBroken_EquipmentCreated_ReturnsFlag() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 1, true, UUID.randomUUID());
        // act
        boolean broken = e.isBroken();
        // assert
        assertTrue(broken);
    }

    @Test
    void getRoomIdOwning_EquipmentCreated_ReturnsRoomId() {
        // arrange
        UUID roomId = UUID.randomUUID();
        Equipment e = new Equipment("desc", "proj", 1, false, roomId);
        // act
        UUID actual = e.getRoomIdOwning();
        // assert
        assertEquals(roomId, actual);
    }

    @Test
    void setBroken_EquipmentUpdated_FlagChanges() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 1, false, UUID.randomUUID());
        // act
        e.setBroken(true);
        // assert
        assertTrue(e.isBroken());
    }

    @Test
    void setEquipmentDescription_EquipmentUpdated_DescriptionChanges() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 1, false, UUID.randomUUID());
        // act
        e.setEquipmentDescription("new");
        // assert
        assertEquals("new", e.getEquipmentDescription());
    }

    @Test
    void setEquipmentName_EquipmentUpdated_NameChanges() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 1, false, UUID.randomUUID());
        // act
        e.setEquipmentName("new");
        // assert
        assertEquals("new", e.getEquipmentName());
    }

    @Test
    void setEquipmentQuantity_EquipmentUpdated_QuantityChanges() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 1, false, UUID.randomUUID());
        // act
        e.setEquipmentQuantity(10);
        // assert
        assertEquals(10, e.getEquipmentQuantity());
    }

    @Test
    void setRoomIdOwning_EquipmentUpdated_RoomIdChanges() {
        // arrange
        Equipment e = new Equipment("desc", "proj", 1, false, UUID.randomUUID());
        UUID newRoom = UUID.randomUUID();
        // act
        e.setRoomIdOwning(newRoom);
        // assert
        assertEquals(newRoom, e.getRoomIdOwning());
    }
}

