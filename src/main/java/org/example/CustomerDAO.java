package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CustomerDAO {

    public Customer createCustomer(int id, String name, String email, String phone) throws Exception {

        Connection conn = DBConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            String resSql = "INSERT INTO customers VALUES (?, ?, ?, ?)";
            PreparedStatement resStmt = conn.prepareStatement(resSql);

            resStmt.setInt(1, id);
            resStmt.setString(2, name);
            resStmt.setString(3, email);
            resStmt.setString(4, phone);
            resStmt.executeUpdate();

            conn.commit();

            return new Customer(id, name, email, phone);

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
}
