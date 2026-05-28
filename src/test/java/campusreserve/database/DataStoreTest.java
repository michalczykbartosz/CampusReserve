package campusreserve.database;

import campusreserve.model.Equipment;
import campusreserve.model.Reservation;
import campusreserve.model.Room;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DataStoreTest {
    @Test
    void getRoomList_InitialState_Empty() {
        // arrange
        DataStore store = new DataStore();
        // act
        boolean empty = store.getRoomList().isEmpty();
        // assert
        assertTrue(empty);
    }

    @Test
    void getEquipmentList_InitialState_Empty() {
        // arrange
        DataStore store = new DataStore();
        // act
        boolean empty = store.getEquipmentList().isEmpty();
        // assert
        assertTrue(empty);
    }

    @Test
    void getReservationList_InitialState_Empty() {
        // arrange
        DataStore store = new DataStore();
        // act
        boolean empty = store.getReservationList().isEmpty();
        // assert
        assertTrue(empty);
    }

    @Test
    void getRoomList_AddRoom_ListSizeIncreases() {
        // arrange
        DataStore store = new DataStore();
        // act
        store.getRoomList().add(new Room(1, "A", "B1", 10, "Typ", true, true));
        // assert
        assertEquals(1, store.getRoomList().size());
    }

    @Test
    void getEquipmentList_AddEquipment_ListSizeIncreases() {
        // arrange
        DataStore store = new DataStore();
        // act
        store.getEquipmentList().add(new Equipment("desc", "proj", 1, false, UUID.randomUUID()));
        // assert
        assertEquals(1, store.getEquipmentList().size());
    }

    @Test
    void getReservationList_AddReservation_ListSizeIncreases() {
        // arrange
        DataStore store = new DataStore();
        Reservation reservation = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        // act
        store.getReservationList().add(reservation);
        // assert
        assertEquals(1, store.getReservationList().size());
    }

    @Test
    void getRoomList_Clear_DoesNotThrow() {
        // arrange
        DataStore store = new DataStore();
        // act
        store.getRoomList().clear();
        store.getEquipmentList().clear();
        store.getReservationList().clear();
        // assert
        assertNotNull(store.getRoomList());
    }

    @Test
    void getRoomList_MultipleCalls_ReturnSameReference() {
        // arrange
        DataStore store = new DataStore();
        // act
        var first = store.getRoomList();
        var second = store.getRoomList();
        // assert
        assertSame(first, second);
    }

    @Test
    void getEquipmentList_MultipleCalls_ReturnSameReference() {
        // arrange
        DataStore store = new DataStore();
        // act
        var first = store.getEquipmentList();
        var second = store.getEquipmentList();
        // assert
        assertSame(first, second);
    }

    @Test
    void getReservationList_MultipleCalls_ReturnSameReference() {
        // arrange
        DataStore store = new DataStore();
        // act
        var first = store.getReservationList();
        var second = store.getReservationList();
        // assert
        assertSame(first, second);
    }
}

