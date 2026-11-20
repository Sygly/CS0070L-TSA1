
package com.mycompany.bookbridge_tsa1;

import java.util.Scanner;

public class BookBridgeApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        final int TOTAL_TRANSACTIONS = 5;

        // Coordinated arrays store the five transactions.
        String[] memberIds = new String[TOTAL_TRANSACTIONS];
        String[] memberNames = new String[TOTAL_TRANSACTIONS];
        String[] membershipTypes = new String[TOTAL_TRANSACTIONS];
        String[] isbns = new String[TOTAL_TRANSACTIONS];
        String[] bookTitles = new String[TOTAL_TRANSACTIONS];
        int[] requestedLoanDays = new int[TOTAL_TRANSACTIONS];
        boolean[] availabilities = new boolean[TOTAL_TRANSACTIONS];
        int[] lateDaysRecords = new int[TOTAL_TRANSACTIONS];
        String[] outcomes = new String[TOTAL_TRANSACTIONS];
        int[] lateCharges = new int[TOTAL_TRANSACTIONS];

        System.out.println("================================");
        System.out.println("       BOOKBRIDGE TSA1");
        System.out.println(" Library Loan Management System");
        System.out.println("================================");

        // Accept exactly five valid borrowing requests.
        for (int i = 0; i < TOTAL_TRANSACTIONS; i++) {

            System.out.println("\n========== TRANSACTION "
                    + (i + 1) + " OF "
                    + TOTAL_TRANSACTIONS + " ==========");

            String memberId = readRequiredText(
                    scanner, "Member ID: ");

            String memberName = readRequiredText(
                    scanner, "Member Name: ");

            String membershipType;

            while (true) {
                System.out.print(
                        "Membership Type (STUDENT/FACULTY/GUEST): ");

                membershipType = scanner.nextLine()
                        .trim().toUpperCase();

                if (validateMembershipType(membershipType)) {
                    break;
                }

                System.out.println(
                        "Invalid membership type. Please try again.");
            }

            String isbn = readRequiredText(
                    scanner, "Book ISBN: ");

            String bookTitle = readRequiredText(
                    scanner, "Book Title: ");

            int requestedDays = readInteger(
                    scanner,
                    "Requested Loan Days (1-30): ",
                    1, 30);

            boolean available;

            while (true) {
                System.out.print("Book Available? (YES/NO): ");

                String answer = scanner.nextLine().trim();

                if (answer.equalsIgnoreCase("YES")) {
                    available = true;
                    break;

                } else if (answer.equalsIgnoreCase("NO")) {
                    available = false;
                    break;
                }

                System.out.println(
                        "Invalid input. Enter YES or NO.");
            }

            int lateDays = readInteger(
                    scanner,
                    "Late Days (0 or more): ",
                    0, Integer.MAX_VALUE);

            // Apply membership borrowing policies.
            int maximumDays = getMaximumDays(membershipType);
            int dailyFine = getDailyFine(membershipType);

            String outcome = classifyRequest(
                    available, requestedDays, maximumDays);

            int estimatedLateCharge = lateDays * dailyFine;

            // Store the transaction in coordinated arrays.
            memberIds[i] = memberId;
            memberNames[i] = memberName;
            membershipTypes[i] = membershipType;
            isbns[i] = isbn;
            bookTitles[i] = bookTitle;
            requestedLoanDays[i] = requestedDays;
            availabilities[i] = available;
            lateDaysRecords[i] = lateDays;
            outcomes[i] = outcome;
            lateCharges[i] = estimatedLateCharge;

            // Display transaction details.
            System.out.println(
                    "\n========== TRANSACTION DETAILS ==========");

            System.out.println("Member ID: " + memberId);
            System.out.println("Member Name: " + memberName);
            System.out.println("Membership Type: " + membershipType);
            System.out.println("ISBN: " + isbn);
            System.out.println("Book Title: " + bookTitle);
            System.out.println("Requested Days: " + requestedDays);
            System.out.println("Available: " + available);
            System.out.println("Late Days: " + lateDays);

            System.out.println(
                    "=========================================");

            // Display borrowing classification and charges.
            System.out.println(
                    "\n========== BORROWING RESULT ==========");

            System.out.println("Membership Type: " + membershipType);
            System.out.println("Maximum Allowed Days: " + maximumDays);
            System.out.println("Requested Days: " + requestedDays);
            System.out.println("Request Status: " + outcome);
            System.out.println("Daily Late Fee: PHP " + dailyFine);
            System.out.println("Estimated Late Charge: PHP "
                    + estimatedLateCharge);

            System.out.println(
                    "======================================");

        } // End of five-transaction loop

        // Print the summary after all five transactions.
        printSummary(
                memberIds,
                bookTitles,
                requestedLoanDays,
                outcomes,
                lateCharges);

        scanner.close();
    }

    // Validates the permitted membership types.
    public static boolean validateMembershipType(String type) {

        return type.equals("STUDENT")
                || type.equals("FACULTY")
                || type.equals("GUEST");
    }

    // Rejects blank required text fields.
    public static String readRequiredText(
            Scanner scanner, String prompt) {

        while (true) {
            System.out.print(prompt);

            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println(
                    "This field cannot be blank.");
        }
    }

    // Validates numeric input and re-prompts when invalid.
    public static int readInteger(
            Scanner scanner,
            String prompt,
            int minimum,
            int maximum) {

        while (true) {
            System.out.print(prompt);

            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);

                if (value >= minimum && value <= maximum) {
                    return value;
                }

            } catch (NumberFormatException e) {
                // Invalid input is handled below.
            }

            System.out.println(
                    "Invalid number. Please try again.");
        }
    }

    // Returns the maximum loan days by membership type.
    public static int getMaximumDays(String membershipType) {

        return switch (membershipType) {
            case "STUDENT" -> 14;
            case "FACULTY" -> 30;
            case "GUEST" -> 7;

            default -> throw new IllegalArgumentException(
                    "Invalid membership type");
        };
    }

    // Returns the daily late fine in Philippine pesos.
    public static int getDailyFine(String membershipType) {

        return switch (membershipType) {
            case "STUDENT" -> 5;
            case "FACULTY" -> 2;
            case "GUEST" -> 10;

            default -> throw new IllegalArgumentException(
                    "Invalid membership type");
        };
    }

    // Classifies borrowing requests using availability and limits.
    public static String classifyRequest(
            boolean available,
            int requestedDays,
            int maximumDays) {

        if (!available) {
            return "RESERVED";

        } else if (requestedDays <= maximumDays) {
            return "APPROVED";

        } else {
            return "DENIED";
        }
    }

    // Displays the summary of all five transactions.
    public static void printSummary(
            String[] memberIds,
            String[] bookTitles,
            int[] requestedLoanDays,
            String[] outcomes,
            int[] lateCharges) {

        int approvedCount = 0;
        int reservedCount = 0;
        int deniedCount = 0;

        long totalLateCharges = 0;

        int greatestDaysIndex = 0;

        for (int i = 0; i < outcomes.length; i++) {

            switch (outcomes[i]) {
                case "APPROVED" -> approvedCount++;
                case "RESERVED" -> reservedCount++;
                case "DENIED" -> deniedCount++;
            }

            totalLateCharges += lateCharges[i];

            if (requestedLoanDays[i]
                    > requestedLoanDays[greatestDaysIndex]) {

                greatestDaysIndex = i;
            }
        }

        System.out.println(
                "\n========== BOOKBRIDGE SUMMARY ==========");
        System.out.println(
                "Total Transactions: " + outcomes.length);
        System.out.println(
                "Approved Requests: " + approvedCount);
        System.out.println(
                "Reserved Requests: " + reservedCount);
        System.out.println(
                "Denied Requests: " + deniedCount);
        System.out.println(
                "\nRequest With Greatest Loan Days:");
        System.out.println(
                "Member ID: " + memberIds[greatestDaysIndex]);
        System.out.println(
                "Book Title: " + bookTitles[greatestDaysIndex]);
        System.out.println(
                "Requested Days: "
                        + requestedLoanDays[greatestDaysIndex]);
        System.out.println(
                "\nTotal Estimated Late Charges: PHP "
                        + totalLateCharges);
        System.out.println(
                "========================================");
    }
}