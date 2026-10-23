package com.scc;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== HOTEL RESERVATION SYSTEM MENU ===");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    System.out.println("Select Role:");
                    System.out.println("1. Front Desk Agent");
                    System.out.println("2. Housekeeping Supervisor");
                    System.out.println("3. System Administrator");
                    System.out.print("Choose role option: ");
                    int roleChoice = scanner.nextInt();
                    scanner.nextLine();

                    String role = switch (roleChoice) {
                        case 1 -> "Front Desk Agent";
                        case 2 -> "Housekeeping Supervisor";
                        case 3 -> "System Administrator";
                        default -> "Front Desk Agent";
                    };

                    boolean success = userDao.registerUser(username, password, role);
                    if (success) {
                        System.out.println("Account registered successfully! Status is PENDING.");
                    } else {
                        System.out.println("Registration failed.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    User user = userDao.login(username, password);
                    if (user != null) {
                        System.out.println("\nLogin Successful!");
                        System.out.println("Welcome, " + user.getUsername() + " | Role: " + user.getRole() + " | Status: " + user.getStatus());

                        // Session / Logout simulation
                        int sessionChoice;
                        do {
                            System.out.println("\n--- USER SESSION (" + user.getRole() + ") ---");
                            System.out.println("1. Logout");
                            System.out.print("Choose option: ");
                            sessionChoice = scanner.nextInt();
                            scanner.nextLine();
                        } while (sessionChoice != 1);

                        System.out.println("Logged out successfully.");
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
                case 3 -> System.out.println("Exiting application. Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 3);

        scanner.close();
    }
}