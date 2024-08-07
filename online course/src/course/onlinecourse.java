package course;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class onlinecourse {

    // Database URL, username and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gobika";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Step 1: Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Open a connection
            System.out.println("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            boolean running = true;
            while (running) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert a new course");
                System.out.println("2. Insert a new reservation");
                System.out.println("3. Retrieve all courses");
                System.out.println("4. Update available seats");
                System.out.println("5. Delete a reservation");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // Insert a new course
                        System.out.print("Enter course name: ");
                        String courseName = scanner.nextLine();
                        System.out.print("Enter instructor name: ");
                        String instructor = scanner.nextLine();
                        System.out.print("Enter available seats: ");
                        int availableSeats = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        String sqlInsertCourse = "INSERT INTO courses (course_name, instructor, available_seats) VALUES (?, ?, ?)";
                        pstmt = conn.prepareStatement(sqlInsertCourse);
                        pstmt.setString(1, courseName);
                        pstmt.setString(2, instructor);
                        pstmt.setInt(3, availableSeats);
                        int rowsInserted = pstmt.executeUpdate();
                        System.out.println("Rows inserted into courses: " + rowsInserted);
                        break;

                    case 2:
                        // Insert a new reservation
                        System.out.print("Enter course ID: ");
                        int courseId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter student name: ");
                        String studentName = scanner.nextLine();

                        String sqlInsertReservation = "INSERT INTO reservations (course_id, student_name) VALUES (?, ?)";
                        pstmt = conn.prepareStatement(sqlInsertReservation);
                        pstmt.setInt(1, courseId);
                        pstmt.setString(2, studentName);
                        rowsInserted = pstmt.executeUpdate();
                        System.out.println("Rows inserted into reservations: " + rowsInserted);
                        break;

                    case 3:
                        // Retrieve all courses
                        String sqlSelectCourses = "SELECT * FROM courses";
                        pstmt = conn.prepareStatement(sqlSelectCourses);
                        rs = pstmt.executeQuery();

                        // Extract data from result set
                        while (rs.next()) {
                            int id = rs.getInt("course_id");
                            String name = rs.getString("course_name");
                            String instr = rs.getString("instructor");
                            int seats = rs.getInt("available_seats");

                            // Display values
                            System.out.print("Course ID: " + id);
                            System.out.print(", Course Name: " + name);
                            System.out.print(", Instructor: " + instr);
                            System.out.println(", Available Seats: " + seats);
                        }
                        break;

                    case 4:
                        // Update available seats
                        System.out.print("Enter course ID: ");
                        int updateCourseId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new available seats: ");
                        int newSeats = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        String sqlUpdateSeats = "UPDATE courses SET available_seats = ? WHERE course_id = ?";
                        pstmt = conn.prepareStatement(sqlUpdateSeats);
                        pstmt.setInt(1, newSeats);
                        pstmt.setInt(2, updateCourseId);
                        int rowsUpdated = pstmt.executeUpdate();
                        System.out.println("Rows updated in courses: " + rowsUpdated);
                        break;

                    case 5:
                        // Delete a reservation
                        System.out.print("Enter reservation ID: ");
                        int reservationId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        String sqlDeleteReservation = "DELETE FROM reservations WHERE reservation_id = ?";
                        pstmt = conn.prepareStatement(sqlDeleteReservation);
                        pstmt.setInt(1, reservationId);
                        int rowsDeleted = pstmt.executeUpdate();
                        System.out.println("Rows deleted from reservations: " + rowsDeleted);
                        break;

                    case 6:
                        // Exit
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }

            // Step 8: Clean-up environment
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // Finally block used to close resources
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException se2) {
            } // Nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } // End finally try
        } // End try
        scanner.close();
        System.out.println("Goodbye!");
    }
}
