package org.example;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        RoomDAO roomDAO = new RoomDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        CustomerDAO customerDAO = new CustomerDAO();

        Room r1 = roomDAO.createRoom(1, 1, 50, 1);
        Room r2 = roomDAO.createRoom(2, 3, 70, 1);
        Customer c1 = customerDAO.createCustomer(1, "John Doe", "johndoe@mail.com", "+306912345678");

        List<Room> rooms = roomDAO.getAllRooms();

        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 5, 5);

        int statusId = 2; // CANCELLED
        reservationDAO.createReservation(1, 1, start, end, statusId, rooms);

        System.out.println("Reservation created!");

        reservationDAO.cancelReservation(1);
    }

}