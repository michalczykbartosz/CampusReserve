package campusreserve.reservations;

import campusreserve.database.DataStore;
import campusreserve.model.Reservation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {
    @Test
    void addReservation_ValidReservation_AddsToStore() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(70));
        // act
        schedule.addReservation(r);
        // assert
        assertEquals(1, store.getReservationList().size());
    }

    @Test
    void addReservation_StartInPast_Throws() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(10));
        // act
        // assert
        assertThrows(IllegalArgumentException.class, () -> schedule.addReservation(r));
    }

    @Test
    void addReservation_EndBeforeStart_Throws() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(5));
        // act
        // assert
        assertThrows(IllegalArgumentException.class, () -> schedule.addReservation(r));
    }

    @Test
    void addReservation_Overlap_Throws() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        UUID roomId = UUID.randomUUID();
        schedule.addReservation(new Reservation(UUID.randomUUID(), roomId, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(70)));
        Reservation overlap = new Reservation(UUID.randomUUID(), roomId, LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(90));
        // act
        // assert
        assertThrows(IllegalStateException.class, () -> schedule.addReservation(overlap));
    }

    @Test
    void cancelReservation_Existing_RemovesFromStore() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(70));
        schedule.addReservation(r);
        // act
        schedule.cancelReservation(r.getReservationId());
        // assert
        assertEquals(0, store.getReservationList().size());
    }

    @Test
    void cancelReservation_Missing_Throws() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        // act
        // assert
        assertThrows(IllegalArgumentException.class, () -> schedule.cancelReservation(UUID.randomUUID()));
    }

    @Test
    void getScheduleForResource_FilteredByRoom_ReturnsOnlyRoomReservations() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        UUID roomId = UUID.randomUUID();
        schedule.addReservation(new Reservation(UUID.randomUUID(), roomId, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(70)));
        schedule.addReservation(new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(70)));
        // act
        int size = schedule.getScheduleForResource(roomId).size();
        // assert
        assertEquals(1, size);
    }

    @Test
    void isTermBusy_Overlap_ReturnsTrue() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        UUID roomId = UUID.randomUUID();
        schedule.addReservation(new Reservation(UUID.randomUUID(), roomId, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(70)));
        // act
        boolean busy = schedule.isTermBusy(roomId, LocalDateTime.now().plusMinutes(20), LocalDateTime.now().plusMinutes(30));
        // assert
        assertTrue(busy);
    }

    @Test
    void isTermBusy_NoReservation_ReturnsFalse() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        UUID roomId = UUID.randomUUID();
        // act
        boolean busy = schedule.isTermBusy(roomId, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(20));
        // assert
        assertFalse(busy);
    }

    @Test
    void getScheduleForResource_UnsortedInput_ReturnsSortedByStart() {
        // arrange
        DataStore store = new DataStore();
        Schedule schedule = new Schedule(store);
        UUID roomId = UUID.randomUUID();
        Reservation r1 = new Reservation(UUID.randomUUID(), roomId, LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(40));
        Reservation r2 = new Reservation(UUID.randomUUID(), roomId, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusMinutes(20));
        schedule.addReservation(r1);
        schedule.addReservation(r2);
        // act
        var first = schedule.getScheduleForResource(roomId).get(0);
        // assert
        assertEquals(r2.getStartTime(), first.getStartTime());
    }
}

