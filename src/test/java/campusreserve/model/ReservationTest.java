package campusreserve.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    @Test
    void getUserId_ReservationCreated_ReturnsUserId() {
        // arrange
        UUID userId = UUID.randomUUID();
        Reservation r = new Reservation(userId, UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        // act
        UUID actual = r.getUserId();
        // assert
        assertEquals(userId, actual);
    }

    @Test
    void getResourceId_ReservationCreated_ReturnsResourceId() {
        // arrange
        UUID resourceId = UUID.randomUUID();
        Reservation r = new Reservation(UUID.randomUUID(), resourceId, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        // act
        UUID actual = r.getResourceId();
        // assert
        assertEquals(resourceId, actual);
    }

    @Test
    void getStartTime_ReservationCreated_ReturnsStartTime() {
        // arrange
        LocalDateTime start = LocalDateTime.now();
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), start, start.plusHours(1));
        // act
        LocalDateTime actual = r.getStartTime();
        // assert
        assertEquals(start, actual);
    }

    @Test
    void getEndTime_ReservationCreated_ReturnsEndTime() {
        // arrange
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), start, end);
        // act
        LocalDateTime actual = r.getEndTime();
        // assert
        assertEquals(end, actual);
    }

    @Test
    void getReservationId_ReservationCreated_NotNull() {
        // arrange
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        // act
        UUID id = r.getReservationId();
        // assert
        assertNotNull(id);
    }

    @Test
    void setUserId_ReservationUpdated_UserIdChanges() {
        // arrange
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        UUID newId = UUID.randomUUID();
        // act
        r.setUserId(newId);
        // assert
        assertEquals(newId, r.getUserId());
    }

    @Test
    void setResourceId_ReservationUpdated_ResourceIdChanges() {
        // arrange
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        UUID newId = UUID.randomUUID();
        // act
        r.setResourceId(newId);
        // assert
        assertEquals(newId, r.getResourceId());
    }

    @Test
    void setStartTime_ReservationUpdated_StartTimeChanges() {
        // arrange
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        LocalDateTime newStart = LocalDateTime.now().plusDays(1);
        // act
        r.setStartTime(newStart);
        // assert
        assertEquals(newStart, r.getStartTime());
    }

    @Test
    void setEndTime_ReservationUpdated_EndTimeChanges() {
        // arrange
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        LocalDateTime newEnd = LocalDateTime.now().plusDays(1);
        // act
        r.setEndTime(newEnd);
        // assert
        assertEquals(newEnd, r.getEndTime());
    }

    @Test
    void toString_ReservationCreated_ContainsReservationIdLabel() {
        // arrange
        Reservation r = new Reservation(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        // act
        String value = r.toString();
        // assert
        assertTrue(value.contains("reservationId"));
    }
}

