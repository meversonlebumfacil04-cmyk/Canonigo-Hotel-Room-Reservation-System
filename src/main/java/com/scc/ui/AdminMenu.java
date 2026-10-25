package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;

    public AdminMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n=== SYSTEM ADMINISTRATOR MENU ===");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                System.out.print("Choose an option: ");
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewAllUsers();
                case 2 -> viewPendingUsers();
                case 3 -> approveUserAccount();
                case 4 -> System.out.println("Logging out from Admin session...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 4);
    }

    private void viewAllUsers() {
        List<User> users = userDao.getAllUsers();
        printUserTable(users, "ALL SYSTEM USERS");
    }

    private void viewPendingUsers() {
        List<User> users = userDao.getPendingUsers();
        printUserTable(users, "PENDING REGISTRATIONS");
    }

    private void approveUserAccount() {
        List<User> pendingUsers = userDao.getPendingUsers();
        if (pendingUsers.isEmpty()) {
            System.out.println("\nNo pending registrations found.");
            return;
        }

        printUserTable(pendingUsers, "PENDING ACCOUNTS AVAILABLE FOR APPROVAL");

        System.out.print("Enter Target User ID to approve (or enter 0 to cancel): ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid User ID.");
            scanner.next();
            System.out.print("Enter Target User ID to approve (or enter 0 to cancel): ");
        }
        int targetId = scanner.nextInt();
        scanner.nextLine();

        if (targetId == 0) {
            System.out.println("Approval action cancelled.");
            return;
        }

        // Verify if ID exists in the pending list
        boolean existsInPending = pendingUsers.stream().anyMatch(u -> u.getUserId() == targetId);
        if (!existsInPending) {
            System.out.println("Error: User ID " + targetId + " is not found in the pending list.");
            return;
        }

        boolean success = userDao.approveUser(targetId);
        if (success) {
            System.out.println("Success: User ID " + targetId + " has been updated to 'APPROVED'.");
        } else {
            System.out.println("Failed to approve user account.");
        }
    }

    // Structured table rendering helper method using standard string formatters (%-5s, %-15s, etc.)
    private void printUserTable(List<User> users, String title) {
        System.out.println("\n=========================================================================");
        System.out.println(" " + title);
        System.out.println("=========================================================================");
        if (users.isEmpty()) {
            System.out.println(" No records found.");
            System.out.println("=========================================================================");
            return;
        }

        String format = "| %-8s | %-20s | %-25s | %-10s |%n";
        System.out.printf(format, "User ID", "Username", "Role", "Status");
        System.out.println("|----------|----------------------|---------------------------|------------|");
        for (User user : users) {
            System.out.printf(format,
                    user.getUserId(),
                    user.getUsername(),
                    user.getRole(),
                    user.getStatus()
            );
        }
        System.out.println("=========================================================================");
    }
}