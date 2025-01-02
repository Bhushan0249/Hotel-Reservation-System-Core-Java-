package HRS_Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Hotel {

	// this is a hotel reservation project . (add entry , update entry , delete
	// entry,read entry, view all data).
	private static final String url = "jdbc:mysql://localhost:3306/Hotel_db";
	private static final String user = "root";
	private static final String pass = "bhushan";

	public static void main(String[] args) throws Exception {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Connection connection = DriverManager.getConnection(url, user, pass);

			while (true) {
				System.out.println();
				System.out.println("Hotel Managment System. please select the opation.");
				System.out.println();

				Scanner s = new Scanner(System.in);
				System.out.println("     +---------------------------------+");
				System.out.println("     |       1. Reserve a Room         |");
				System.out.println("     |       2. View Reservations      |");
				System.out.println("     |       3. Update Reservations    |");
				System.out.println("     |       4. Delete Reservations    |");
				System.out.println("     |       5. Chake id number        |");
				System.out.println("     |       6. View All reservation   |");
				System.out.println("     |       0. Exit                   |");
				System.out.println("     +---------------------------------+ Develop By Bhushan.");

				int choice = s.nextInt();

				switch (choice) {
				case 1:
					ReseveRoom(connection, s);
					break;

				case 2:
					viewReservation(connection, s);
					break;
				case 3:
					updateReservation(connection, s);
					break;

				case 4:
					deleteReservation(connection, s);
					break;

				case 5:
					chake_id(connection, s);
					break;

				case 6:
					viewAll(connection, s);
					break;

				case 0:
					exit();
					s.close();
					return;

				default:
					System.out.println("Invalid choice , Try again ");

				}

			}

		} catch (SQLException e) {
			e.getMessage();

		} catch (InterruptedException e) {

			throw new RuntimeException(e);
		}
	}

	private static void viewAll(Connection connection, Scanner s) {
		try {
			String query = "select * from reservations";

			PreparedStatement ps = connection.prepareStatement(query);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				System.out.println("id :" + rs.getInt("reservation_id"));

				System.out.println("guest name :" + rs.getString("guest_name"));

				System.out.println("room number :" + rs.getInt("room_number"));

				System.out.println("contact no. : " + rs.getString("contact_number"));

				String date = rs.getTimestamp("reservation_date").toString();
				System.out.println("time is :" + date.toString());
			}

		} catch (Exception e) {
		}
	}

	private static void ReseveRoom(Connection connection, Scanner s) {

		// reserve room
		try {

			String query = "insert into reservations(guest_name,room_number,contact_number) values" + " (?,?,?)";

			PreparedStatement ps = connection.prepareStatement(query);

			System.out.println("Enter guest name ");
			s.nextLine();
			ps.setString(1, s.next());

			System.out.println("Enter room number ");
			ps.setInt(2, s.nextInt());

			System.out.println("Enter contact number");
			ps.setString(3, s.next());

			int a = ps.executeUpdate();
			if (a > 0) {
				System.out.println("reservation successfully");
			} else {
				System.out.println("reservation failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void viewReservation(Connection connection, Scanner s) {

		// get by id .
		try {
			System.out.println("enter id ");
			String no = s.next();

			String query = "select * from reservations where reservation_id= " + no;

			PreparedStatement ps = connection.prepareStatement(query);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				System.out.println("id :" + rs.getInt("reservation_id"));

				System.out.println("guest name :" + rs.getString("guest_name"));

				System.out.println("room number :" + rs.getInt("room_number"));

				System.out.println("contact no. : " + rs.getString("contact_number"));

				String date = rs.getTimestamp("reservation_date").toString();
				System.out.println("time is :" + date.toString());
				System.out.println();
			}

		} catch (Exception e) {
		}

	}

	private static void updateReservation(Connection connection, Scanner s) {

		// update Reservation.
		try {
			System.out.println("Enter reservation id to update .");

			int reservationId = s.nextInt();

			s.nextLine();

			if (!reservationExists(connection, reservationId)) {

				System.out.println("Reservation not found the given id ");

				return;
			}

			String query = "update reservations set guest_name=?, room_number=? , contact_number=? where reservation_id="
					+ reservationId;
			PreparedStatement ps = connection.prepareStatement(query);

			System.out.println("Enter new guest name ");
			ps.setString(1, s.next());

			System.out.println("Enter new rool number ");
			ps.setInt(2, s.nextInt());

			System.out.println("Enter new contact number");
			ps.setString(3, s.next());

			int a = ps.executeUpdate();
			if (a > 0) {
				System.out.println("update successfully...");
			} else {
				System.out.println("not updated data...");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static void deleteReservation(Connection connection, Scanner s) {
// delete Reservation.

		try {

			System.out.println("Enter reservation id to delete ");
			int idDelete = s.nextInt();

			if (!reservationExists(connection, idDelete)) {

				System.out.println("Reservation not found for the given id ");
				return;
			}

			String query = "delete from reservations where reservation_id=" + idDelete;

			PreparedStatement ps = connection.prepareStatement(query);

			int a = ps.executeUpdate();

			if (a > 0) {
				System.out.println("deleted successfully");
			} else {
				System.out.println("not deleted");
			}

		} catch (Exception e) {

		}

	}

	private static void chake_id(Connection connection, Scanner s) {

		try {

			System.out.println("Enter name.......");

			String query = "select reservation_id from reservations where guest_name= ?";

			PreparedStatement ps = connection.prepareStatement(query);
			String name = s.next();
			ps.setString(1, name);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				System.out.println(name + " this name's id is : " + rs.getInt("reservation_id"));

			}

		} catch (Exception e) {
			System.out.println("chake the name , Try again");
		}

	}

	private static boolean reservationExists(Connection connection, int reservationId) {

		try {

			String query = "select reservation_id from reservations where reservation_id=" + reservationId;

			PreparedStatement ps = connection.prepareStatement(query);

			ResultSet rs = ps.executeQuery();
			return rs.next();

		} catch (Exception e) {

			return false;
		}

	}

	private static void exit() throws InterruptedException {
		// this method used to exit program.

		System.out.print("Exiting system");

		int i = 5;

		while (i != 0) {
			System.out.print(".");

			Thread.sleep(350);
			i--;
		}
		System.out.println();

		System.out.println("Thank you for using resevation system.");

	}

}
