package org.example;

import java.sql.*;
import java.util.*;

public class RoomDAO {

    public List<Room> getAllRooms() throws Exception {

        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM rooms";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("id"),
                        rs.getInt("room_type_id"),
                        rs.getDouble("price_per_night"),
                        rs.getInt("room_status_id")
                ));
            }
        }

        return rooms;
    }

    public Room createRoom(int id, int room_type_id, int price_per_night, int room_status_id) throws Exception {

        Connection conn = DBConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            String resSql = "INSERT INTO rooms VALUES (?, ?, ?, ?)";
            PreparedStatement resStmt = conn.prepareStatement(resSql);

            resStmt.setInt(1, id);
            resStmt.setInt(2, room_type_id);
            resStmt.setDouble(3, price_per_night);
            resStmt.setInt(4, room_status_id);
            resStmt.executeUpdate();

            conn.commit();

            return new Room(id, room_type_id, price_per_night, room_status_id);

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
}
