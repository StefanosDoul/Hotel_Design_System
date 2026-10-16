package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDAO {

    // Create Reservation + Junction table
    public void createReservation(int reservationId,
                                  int customerId,
                                  LocalDate start,
                                  LocalDate end,
                                  int statusId,
                                  List<Room> rooms) throws Exception {

        Connection conn = DBConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            String resSql = "INSERT INTO reservations VALUES (?, ?, ?, ?, ?)";
            PreparedStatement resStmt = conn.prepareStatement(resSql);

            resStmt.setInt(1, reservationId);
            resStmt.setInt(2, customerId);
            resStmt.setDate(3, Date.valueOf(start));
            resStmt.setDate(4, Date.valueOf(end));
            resStmt.setInt(5, statusId);
            resStmt.executeUpdate();

            String jSql = "INSERT INTO reservation_rooms VALUES (?, ?, ?, ?)";
            PreparedStatement jStmt = conn.prepareStatement(jSql);

            for (Room room : rooms) {
                jStmt.setInt(1, reservationId);
                jStmt.setInt(2, room.getId());
                jStmt.setDouble(3, room.getPricePerNight());
                jStmt.setString(4, null);
                jStmt.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    // CANCEL
    public void cancelReservation(int reservationId) throws Exception {

        String sql = "UPDATE reservations SET reservation_status_id = 2 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);

            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Reservation not found");
            }
        }
    }

    // Availability check
    public boolean isRoomAvailable(int roomId,
                                   LocalDate start,
                                   LocalDate end) throws Exception {

        String sql = """
            SELECT COUNT(*) 
            FROM reservation_rooms rr
            JOIN reservations r ON rr.reservation_id = r.id
            WHERE rr.room_id = ?
            AND r.reservation_status_id = 1
            AND NOT (r.end_date <= ? OR r.start_date >= ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(end));

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getInt(1) == 0;
        }
    }

    // 🔹 FIND BY CUSTOMER
    public List<Reservation> findReservationsByCustomer(int customerId) throws Exception {

        String sql = """
            SELECT r.*, rm.*
            FROM reservations r
            JOIN reservation_rooms rr ON r.id = rr.reservation_id
            JOIN rooms rm ON rr.room_id = rm.id
            WHERE r.customer_id = ?
        """;

        Map<Integer, Reservation> map = new HashMap<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");

                if (!map.containsKey(id)) {
                    map.put(id, new Reservation(
                            id,
                            rs.getInt("customer_id"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getInt("reservation_status_id"),
                            new ArrayList<>()
                    ));
                }

                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getInt("room_type_id"),
                        rs.getDouble("price_per_night"),
                        rs.getInt("room_status_id")
                );

                map.get(id).getRooms().add(room);
            }
        }

        return new ArrayList<>(map.values());
    }

    // 🔹 FIND BY DATE RANGE
    public List<Reservation> findReservationsByDateRange(LocalDate start, LocalDate end) throws Exception {

        String sql = """
            SELECT r.*, rm.*
            FROM reservations r
            JOIN reservation_rooms rr ON r.id = rr.reservation_id
            JOIN rooms rm ON rr.room_id = rm.id
            WHERE r.reservation_status_id = 1
            AND NOT (r.end_date <= ? OR r.start_date >= ?)
        """;

        Map<Integer, Reservation> map = new HashMap<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");

                if (!map.containsKey(id)) {
                    map.put(id, new Reservation(
                            id,
                            rs.getInt("customer_id"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getInt("reservation_status_id"),
                            new ArrayList<>()
                    ));
                }

                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getInt("room_type_id"),
                        rs.getDouble("price_per_night"),
                        rs.getInt("room_status_id")
                );

                map.get(id).getRooms().add(room);
            }
        }

        return new ArrayList<>(map.values());
    }

}
