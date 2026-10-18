# Hotel Management System

A Java application using MySQL and JDBC to manage hotel rooms, customers, and reservations.

## Features
- Manage room and customer information
- Create reservations for one or more rooms
- Check room availability and prevent overlapping active reservations
- Cancel reservations
- Search reservations by customer or date range
- Validate dates and calculate reservation costs

## Technologies
- Java
- MySQL
- JDBC

## Project Structure
- `DBConnection.java` — database connection
- `Customer.java`, `Room.java`, `Reservation.java` — domain models
- `RoomDAO.java`, `ReservationDAO.java` — database operations
- `Main.java` — application entry point

## Database
The database uses `customers`, `rooms`, `reservations`, and `reservation_rooms`, with lookup tables for room types and statuses.

## Setup
1. Create the MySQL database `hotel_db` and the required tables.
2. Configure the JDBC URL, username, and password in `DBConnection.java`.
3. Add the MySQL Connector/J dependency.
4. Run `Main.java`.
