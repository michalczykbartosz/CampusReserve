package campusreserve.resources;

import campusreserve.database.DataStore;
import campusreserve.model.Equipment;
import campusreserve.model.Room;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {
    @Test
    void addRoom_ValidRoom_AddsToList() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        // act
        rm.addRoom(new Room(1, "A", "B", 10, "T", true, true));
        // assert
        assertEquals(1, store.getRoomList().size());
    }

    @Test
    void addRoom_DuplicateNumber_Throws() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        rm.addRoom(new Room(1, "A", "B", 10, "T", true, true));
        // act
        // assert
        assertThrows(IllegalArgumentException.class, () -> rm.addRoom(new Room(1, "X", "B", 10, "T", true, true)));
    }

    @Test
    void getRoomByNumber_RoomExists_ReturnsRoom() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        Room room = new Room(2, "A", "B", 10, "T", true, true);
        rm.addRoom(room);
        // act
        Room found = rm.getRoomByNumber(2);
        // assert
        assertEquals(room.getRoomId(), found.getRoomId());
    }

    @Test
    void getRoomsByBuilding_Filter_ReturnsMatches() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        rm.addRoom(new Room(1, "A", "B1", 10, "T", true, true));
        rm.addRoom(new Room(2, "A", "B2", 10, "T", true, true));
        // act
        int size = rm.getRoomsByBuilding("B1").size();
        // assert
        assertEquals(1, size);
    }

    @Test
    void getAvailableRooms_Filter_ReturnsOnlyAvailable() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        rm.addRoom(new Room(1, "A", "B", 10, "T", true, true));
        rm.addRoom(new Room(2, "A", "B", 10, "T", true, false));
        // act
        int size = rm.getAvailableRooms().size();
        // assert
        assertEquals(1, size);
    }

    @Test
    void deactivateRoom_RoomExists_SetsInactive() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        rm.addRoom(new Room(1, "A", "B", 10, "T", true, true));
        // act
        rm.deactivateRoom(1);
        // assert
        assertFalse(rm.getRoomByNumber(1).isActive());
    }

    @Test
    void addEquipment_Valid_AddsToList() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        // act
        rm.addEquipment(new Equipment("desc", "proj", 1, false, UUID.randomUUID()));
        // assert
        assertEquals(1, store.getEquipmentList().size());
    }

    @Test
    void getEquipmentForRoom_Filter_ReturnsOnlyRoomEquipment() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        Room room = new Room(1, "A", "B", 10, "T", true, true);
        rm.addRoom(room);
        rm.addEquipment(new Equipment("desc", "proj", 1, false, room.getRoomId()));
        rm.addEquipment(new Equipment("desc", "proj2", 1, false, UUID.randomUUID()));
        // act
        int size = rm.getEquipmentForRoom(room.getRoomId()).size();
        // assert
        assertEquals(1, size);
    }

    @Test
    void moveEquipment_TargetRoom_UpdatesRoomId() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        Room r1 = new Room(1, "A", "B", 10, "T", true, true);
        Room r2 = new Room(2, "A", "B", 10, "T", true, true);
        rm.addRoom(r1);
        rm.addRoom(r2);
        Equipment e = new Equipment("desc", "proj", 1, false, r1.getRoomId());
        rm.addEquipment(e);
        // act
        rm.moveEquipment(e.getEquipmentId(), 2);
        // assert
        assertEquals(r2.getRoomId(), e.getRoomIdOwning());
    }

    @Test
    void getAllBuildings_DuplicateBuildings_ReturnsUnique() {
        // arrange
        DataStore store = new DataStore();
        ResourceManager rm = new ResourceManager(store);
        rm.addRoom(new Room(1, "A", "B", 10, "T", true, true));
        rm.addRoom(new Room(2, "A", "B", 10, "T", true, true));
        // act
        int size = rm.getAllBuildings().size();
        // assert
        assertEquals(1, size);
    }
}

