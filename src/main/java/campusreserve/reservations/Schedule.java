package campusreserve.reservations;

import campusreserve.database.DataStore;
import campusreserve.model.Reservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Schedule {
    private DataStore database;

    public Schedule(DataStore database) {
        this.database = database;
    }

    /**
     * Waliduje i dodaje rezerwację do wspólnego DataStore
     */
    public void addReservation(Reservation newReservation) {
        if (!validateHours(newReservation.getStartTime(), newReservation.getEndTime())) {
            throw new IllegalArgumentException("Niepoprawne ramy czasowe! Data rozpoczęcia musi być przed zakończeniem i w przyszłości.");
        }

        if (isTermBusy(newReservation.getResourceId(), newReservation.getStartTime(), newReservation.getEndTime())) {
            throw new IllegalStateException("Ta sala lub sprzęt są już zajęte w tych godzinach!");
        }

        this.database.getReservationList().add(newReservation);
    }

    /**
     * Anuluje rezerwację za pomocą jej UUID
     */
    public void cancelReservation(UUID reservationId) {
        ArrayList<Reservation> reservations = this.database.getReservationList();
        boolean removed = reservations.removeIf(r -> r.getReservationId().equals(reservationId));
        if (!removed) {
            throw new IllegalArgumentException("Brak rezerwacji o takim ID w systemie!");
        }
    }

    /**
     * Pobiera harmonogram rezerwacji dla konkretnego zasobu (posortowany chronologicznie)
     */
    public ArrayList<Reservation> getScheduleForResource(UUID resourceId) {
        ArrayList<Reservation> allReservations = this.database.getReservationList();
        ArrayList<Reservation> resourceSchedule = new ArrayList<>();

        for (Reservation r : allReservations) {
            if (r.getResourceId().equals(resourceId)) {
                resourceSchedule.add(r);
            }
        }

        resourceSchedule.sort((r1, r2) -> r1.getStartTime().compareTo(r2.getStartTime()));
        return resourceSchedule;
    }

    /**
     * Sprawdza, czy termin dla danego zasobu nakłada się na inny
     */
    public boolean isTermBusy(UUID resourceId, LocalDateTime start, LocalDateTime end) {
        ArrayList<Reservation> reservations = this.database.getReservationList();
        for (Reservation r : reservations) {
            if (r.getResourceId().equals(resourceId)) {
                if (start.isBefore(r.getEndTime()) && end.isAfter(r.getStartTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Walidacja poprawności logicznej daty i godziny
     */
    private boolean validateHours(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return false;
        }
        if (!start.isBefore(end)) {
            return false;
        }
        if (start.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }
}