package org.example;

public class Room {

    private int id;
    private int roomTypeId;      // FK → room_types
    private double pricePerNight;
    private int roomStatusId;    // FK → room_statuses

    public Room(int id, int roomTypeId, double pricePerNight, int roomStatusId) {
        this.id = id;
        this.roomTypeId = roomTypeId;
        this.pricePerNight = pricePerNight;
        this.roomStatusId = roomStatusId;
    }

    public int getId() { return id; }
    public double getPricePerNight() { return pricePerNight; }

}
