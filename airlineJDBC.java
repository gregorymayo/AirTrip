package airlineproject;

import java.sql.*;
import java.util.Scanner;

public class airlineJDBC {
	// JDBC driver name and database URL
	static final String DB_URL = "jdbc:mysql://localhost/project157a?serverTimezone=UTC";
	static final String USER = "root";
	static final String PASS = "e4e5nf3nc6bb5";
 
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		try{
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			while(true)  {
				System.out.println("Select what you would like to do:");
				System.out.println("1. Create a new user.");
				System.out.println("2. Login to existing account.");
				int ret1 = scan.nextInt();
				if (ret1 == 1) {
					//create a user
					System.out.println("Let's create a user! Type a new username:");
					String loginID = scan.next();
					System.out.println("Well done! Now, choose a password:");
					String password = scan.next();
					System.out.println("Are you a public user (1) or a admin user (2) ?");
					int userRet = scan.nextInt();
					stmt = conn.createStatement();
					String queryInsert;
					if (userRet == 1) {
						queryInsert = "insert into credentials values (\'" + loginID + "\', \'" + password
								+ "\', \'user\')";
					}  else {
						queryInsert = "insert into credentials values (\'" + loginID + "\', \'" + password
								+ "\', \'admin\')";
					}
					stmt.executeUpdate(queryInsert);
					System.out.println("You are now a registered user!");
					System.out.println("Going back to main menu...");
				} else {
					System.out.println("Enter your username:");
					String usernameRet = scan.next();
					System.out.println("Enter your password:");
					String passwordRet = scan.next();
					System.out.println("Are you a public user or an admin user?");
					String typeRet = scan.next();
					String queryCheck = "select * from credentials where loginID = \'"
							+ usernameRet + "\' and password = \'" + passwordRet + "\' and "
									+ "usertype = \'" + typeRet + "\';";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(queryCheck);
					if(rs.next() == true) {
						System.out.println("Login successful!");
						if(typeRet.equals("user")) {
							while(true) {
								System.out.println("Select what you would like to do:");
								System.out.println("1. Change username.");
								System.out.println("2. Change password.");
								System.out.println("3. Change user type.");
								System.out.println("4. List all booked tickets.");
								System.out.println("5. Print count of booked tickets.");
								System.out.println("6. Book a ticket.");
								System.out.println("7. Update a ticket.");
								System.out.println("8. Cancel a ticket.");
								int userMenu = scan.nextInt();
								if(userMenu == 1) {
									System.out.println("You have chosen to change your username.");
									System.out.println("Enter your new username.");
									String newUsername = scan.next();
									String queryUpdateUsername = "update credentials set loginid = \'" 
										+ newUsername + "\' where loginid = \'" + usernameRet + "\';";
									stmt = conn.createStatement();
									stmt.executeUpdate(queryUpdateUsername);
									String queryUpdateTicketsUsername = "UPDATE tickets SET loginID = \'" + newUsername + "\' WHERE loginID = \'" + usernameRet + "\';";
									stmt = conn.createStatement();
									stmt.executeUpdate(queryUpdateTicketsUsername);
									System.out.println("Your username has changed.");
									System.out.println("Going back to menu...");
									usernameRet = newUsername;
									continue;
								}  else if (userMenu == 2) {
									System.out.println("You have chosen to change your password.");
									System.out.println("Enter your new password.");
									String newPassword = scan.next();
									String queryUpdatePassword = "update credentials set password = \'" 
									+ newPassword + "\' where loginid = \'" + usernameRet + "\';";
									stmt = conn.createStatement();
									stmt.executeUpdate(queryUpdatePassword);
									System.out.println("Your password has changed.");
									System.out.println("Going back to menu...");
									continue;
								} else if (userMenu == 3) {
									System.out.println("You have chosen to change your user type.");
									System.out.println("Enter your new user type.");
									String newUsertype = scan.next();
									String queryUpdateUsertype = "update credentials set usertype = \'" 
									+ newUsertype + "\' where loginid = \'" + usernameRet + "\';";
									stmt = conn.createStatement();
									stmt.executeUpdate(queryUpdateUsertype);
									System.out.println("Your user type has changed.");
									System.out.println("Going back to menu...");
									continue;
								} else if (userMenu == 4) {
									System.out.println("You have chosen to list all booked tickets.");
									String queryListTickets = "select * from tickets natural join ticketinfo where loginid = \'" + usernameRet + "\';";
									stmt = conn.createStatement();
									rs = stmt.executeQuery(queryListTickets);
									System.out.println("ticket number, sID, dID, seats:");
									while(rs.next()) {
										System.out.println(rs.getInt("ticketnumber") + ", " + rs.getInt("sourceairportid") + ", " + rs.getInt("destairportid") + ", " + rs.getInt("seats"));
									}
									System.out.println("\nGoing back to menu...");
									continue;
								} else if (userMenu == 5) {
									System.out.println("You have chosen to list count of all booked tickets.");
									String queryPrintTicketsNum = "select count(*) from tickets where loginid = \'" + usernameRet + "\';";
									stmt = conn.createStatement();
									rs = stmt.executeQuery(queryPrintTicketsNum);
									System.out.println("\nCount of tickets:");
									if(rs.next()) {
										System.out.println(rs.getInt("count(*)"));
									}
									System.out.println("\nGoing back to menu...");
									continue;
								} else if (userMenu == 6) {
									System.out.println("Now, let's book a flight!");
									System.out.println("Enter the source airport ID:");
									String sourceAirportID = scan.next();
									System.out.println("Enter the destination airport ID:");
									String destAirportID = scan.next();
									System.out.println("Enter the number of seats to book:");
									int seats = scan.nextInt();
									System.out.println("Enter the date of flight:");
									String dateOfFlight = scan.next();
									System.out.println("Booking your ticket...");
									stmt = conn.createStatement();
									String queryFindAirlineID = "select airlineID from routes where sourceAirportID = \'" + sourceAirportID + "\' and destAirportID = \'" + destAirportID + "\';";
									rs = stmt.executeQuery(queryFindAirlineID);
									int airlineID = 0;
									if(rs.next())
										airlineID = rs.getInt("airlineID");
									stmt = conn.createStatement();
									String insertTicketID = "insert into tickets(loginid) values (\'" + usernameRet + "\')";
									stmt.executeUpdate(insertTicketID);
									stmt = conn.createStatement();
									rs = stmt.executeQuery("select max(ticketNumber) from tickets;");
									int ticketNumberNew = 0;
									if(rs.next())
										ticketNumberNew = rs.getInt("max(ticketNumber)");
									stmt = conn.createStatement();
									String insertTicketInfo = "insert into ticketInfo(ticketNumber, airlineID, sourceAirportID,"
											+ "destAirportID, ticketDate, seats) values (\'" + ticketNumberNew + "\', \'" + airlineID + "\', \'"+ sourceAirportID + 
											"\', \'" + destAirportID + "\', \'" + dateOfFlight + "\', " + seats + ")";
									stmt.executeUpdate(insertTicketInfo);
									stmt = conn.createStatement();
									String queryInsertTicketType = "insert into ticketTypes values (\'" + ticketNumberNew + "\', \'economy\');";
									stmt.executeUpdate(queryInsertTicketType);
									System.out.println("Your ticket is booked!");
									System.out.println("Going back to menu...");
									continue;
								} else if (userMenu == 7) {
									System.out.println("You have chosen to update a ticket.");
									System.out.println("Enter the ticket number of your ticket:");
									int ticketNum = scan.nextInt();
									System.out.println("Enter the new source airport id:");
									int newSourceAID = scan.nextInt();
									System.out.println("Enter the new destination airport id:");
									int newDestAID = scan.nextInt();
									System.out.println("Enter the new number of seats:");
									int newSeats = scan.nextInt();
									String queryUpdateTicketInfo = "update ticketinfo set "
											+ "sourceairportid = " + newSourceAID + ", "
											+ "destairportid = " + newDestAID + ", seats = " 
											+ newSeats + " where ticketnumber = " + ticketNum + 
											";";
									stmt = conn.createStatement();
									stmt.executeUpdate(queryUpdateTicketInfo);
									System.out.println("Your ticket has been updated!");
									System.out.println("Going back to menu...");
									continue;
								} else if (userMenu == 8) {
									System.out.println("You have chosen to cancel a ticket.");
									System.out.println("Enter the ticket number:");
									int ticketNumDelete = scan.nextInt();
									String queryCancelTicketInfo = "delete from ticketinfo where "
											+ "ticketnumber = " + ticketNumDelete + ";";
									stmt = conn.createStatement();
									stmt.executeUpdate(queryCancelTicketInfo);
									stmt = conn.createStatement();
									String queryDeleteTicketTypes = "delete from ticketTypes where ticketNumber = " + ticketNumDelete + ";";
									stmt.executeUpdate(queryDeleteTicketTypes);
									String queryCancelTicket = "delete from tickets where "
											+ "ticketnumber = " + ticketNumDelete + ";";
									stmt = conn.createStatement();
									stmt.executeUpdate(queryCancelTicket);
									System.out.println("Your ticket has been cancelled.");
									System.out.println("Going back to menu...");
									continue;
								}
							}
						}
					} else {
						System.out.println("Incorrect credentials! Going back to main menu...");
						continue;
					}
				}
			}
    
		} catch(SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			//finally block used to close resources
			try {
				if(stmt!=null)
					stmt.close();
			} catch (SQLException se2){
			} // nothing we can do
			try {
				if(conn!=null)
					conn.close();
			} catch(SQLException se) {
				se.printStackTrace();
			} //end finally try
		} //end try
		System.out.println("Goodbye!");
	} //end main
}
