package campusreserve.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    @Test
    void getRoomNumber_RoomCreated_ReturnsNumber() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        int number = r.getRoomNumber();
        // assert
        assertEquals(101, number);
    }

    @Test
    void getRoomName_RoomCreated_ReturnsName() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        String name = r.getRoomName();
        // assert
        assertEquals("A", name);
    }

    @Test
    void getBuildingName_RoomCreated_ReturnsBuilding() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        String building = r.getBuildingName();
        // assert
        assertEquals("B", building);
    }

    @Test
    void getCapacity_RoomCreated_ReturnsCapacity() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        int capacity = r.getCapacity();
        // assert
        assertEquals(20, capacity);
    }

    @Test
    void getRoomType_RoomCreated_ReturnsType() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        String type = r.getRoomType();
        // assert
        assertEquals("Typ", type);
    }

    @Test
    void isActive_RoomCreated_ReturnsTrue() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        boolean active = r.isActive();
        // assert
        assertTrue(active);
    }

    @Test
    void isAvailable_RoomCreated_ReturnsTrue() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        boolean available = r.isAvailable();
        // assert
        assertTrue(available);
    }

    @Test
    void setRoomNumber_RoomUpdated_NumberChanges() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        r.setRoomNumber(202);
        // assert
        assertEquals(202, r.getRoomNumber());
    }

    @Test
    void setAvailable_RoomUpdated_AvailabilityChanges() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        r.setAvailable(false);
        // assert
        assertFalse(r.isAvailable());
    }

    @Test
    void getRoomId_RoomCreated_NotNull() {
        // arrange
        Room r = new Room(101, "A", "B", 20, "Typ", true, true);
        // act
        var id = r.getRoomId();
        // assert
        assertNotNull(id);
    }
}

