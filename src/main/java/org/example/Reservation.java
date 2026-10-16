package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Reservation {

    private int id;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int statusId; // FK → reservation_statuses
    private List<Room> rooms;

    public Reservation(int id, int customerId,
                       LocalDate startDate, LocalDate endDate,
                       int statusId,
                       List<Room> rooms) {

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("Invalid dates");
        }

        this.id = id;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.statusId = statusId;
        this.rooms = rooms;
    }

    // Pricing ανά μέρα
    public double calculateTotalPrice() {
        long nights = ChronoUnit.DAYS.between(startDate, endDate);

        double total = 0;
        for (Room room : rooms) {
            total += room.getPricePerNight() * nights;
        }
        return total;
    }

    public int getId() { return id; }
    public List<Room> getRooms() { return rooms; }

}
