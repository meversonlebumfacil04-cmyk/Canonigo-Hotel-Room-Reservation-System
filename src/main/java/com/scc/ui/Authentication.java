package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Authentication {
    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void start() {
        int choice;
        do {
            System.out.println("\n=== HOTEL RESERVATION SYSTEM MENU ===");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                System.out.print("Choose an option: ");
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> handleRegistration();
                case 2 -> handleLogin();
                case 3 -> System.out.println("Exiting application. Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 3);
    }

    private void handleRegistration() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("Select Role:");
        System.out.println("1. Front Desk Agent");
        System.out.println("2. Housekeeping Supervisor");
        System.out.println("3. System Administrator");
        System.out.print("Choose role option: ");

        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print("Choose role option: ");
        }
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

    private void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDao.login(username, password);
        if (user != null) {
            System.out.println("\nLogin Successful!");
            System.out.println("Welcome, " + user.getUsername() + " | Role: " + user.getRole() + " | Status: " + user.getStatus());

            // Route user based on role
            if ("System Administrator".equals(user.getRole())) {
                AdminMenu adminMenu = new AdminMenu(userDao, scanner);
                adminMenu.showMenu();
            } else {
                runStandardUserSession(user);
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private void runStandardUserSession(User user) {
        int sessionChoice;
        do {
            System.out.println("\n--- USER SESSION (" + user.getRole() + ") ---");
            System.out.println("1. Logout");
            System.out.print("Choose option: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                System.out.print("Choose option: ");
            }
            sessionChoice = scanner.nextInt();
            scanner.nextLine();
        } while (sessionChoice != 1);

        System.out.println("Logged out successfully.");
    }
}