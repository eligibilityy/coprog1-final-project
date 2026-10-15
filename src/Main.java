/*
 * finalCode-C1A-Group7.java
 * Written by:
 * - Gavin Adriel Jatulan
 * - Elijah Patrick Panique
 * - Kyle Christian Salvacion
 * Date:
 * Description  : Monthly Payroll Program
 */

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static final int WEEKS_IN_MONTH = 4;
    static final double REGULAR_HOURS_PER_WEEK = 40.0;
    static final double OVERTIME_MULTIPLIER = 1.25;
    static final double TRANSPORT_PER_WEEK = 5000.0;
    static final double DEDUCTION_PER_DEPENDENT = 1000.0;
    static final double PHILHEALTH_AMOUNT = 120.0;

    static final String[] COMMANDS = {"help", "exit", "compute", "clear"};
    static final String[] DESCRIPTIONS = {"Display this help message.", "Exit the program.", "Compute the payroll for an employee.", "Clears the screen."};

    static Scanner sc = new Scanner(System.in);

    // Computation Logic

    static double getRegularHours(double hours) {
        if (hours > REGULAR_HOURS_PER_WEEK) {
            return REGULAR_HOURS_PER_WEEK;
        }
        return hours;
    }

    static double getOvertimeHours(double hours) {
        if (hours > REGULAR_HOURS_PER_WEEK) {
            return hours - REGULAR_HOURS_PER_WEEK;
        }
        return 0;
    }

    static double computeRegularHours(double[] weeklyHours) {
        double total = 0;
        for (int i = 0; i < weeklyHours.length; i++) {
            total = total + getRegularHours(weeklyHours[i]);
        }
        return total;
    }

    static double computeOvertimeHours(double[] weeklyHours) {
        double total = 0;
        for (int i = 0; i < weeklyHours.length; i++) {
            total = total + getOvertimeHours(weeklyHours[i]);
        }
        return total;
    }

    static double computeTotalHours(double[] weeklyHours) {
        return computeRegularHours(weeklyHours) + computeOvertimeHours(weeklyHours);
    }

    static double computeRegularPay(double rate, double hours) {
        return rate * hours;
    }

    static double computeOvertimePay(double rate, double hours) {
        double overtimeRate = rate * OVERTIME_MULTIPLIER;
        return overtimeRate * hours;
    }

    static double computeAllowance(boolean isManager) {
        if (isManager) {
            return TRANSPORT_PER_WEEK * WEEKS_IN_MONTH;
        }
        return 0;
    }

    static double computeSSS(double grossPay) {
        if (grossPay <= 5000) {
            return 105.00;
        } else if (grossPay <= 10000) {
            return grossPay * 0.05;
        } else if (grossPay <= 15000) {
            return grossPay * 0.08 + 75;
        } else {
            return grossPay * 0.12 + 110;
        }
    }

    static double computePagIbig(double grossPay) {
        if (grossPay <= 5000) {
            return 100.00;
        }

        return grossPay * 0.03;
    }

    static double computePhilhealth(double totalHours) {
        if (totalHours < 10) {
            return 0;
        }

        return PHILHEALTH_AMOUNT;
    }

    static double getTaxRate(double grossPay) {
        if (grossPay <= 10000) {
            return 0.03;
        } else if (grossPay <= 25000) {
            return 0.08;
        } else if (grossPay <= 40000) {
            return 0.11;
        } else {
            return 0.135;
        }
    }

    static double computeGP(double rate, double[] weeklyHours, boolean isManager) {
        double regularPay = computeRegularPay(rate, computeRegularHours(weeklyHours));
        double overtimePay = computeOvertimePay(rate, computeOvertimeHours(weeklyHours));
        double allowance = computeAllowance(isManager);
        return regularPay + overtimePay + allowance;
    }

    static double computeTax(double grossPay) {
        return grossPay * getTaxRate(grossPay);
    }

    static double computeDependentDed(int dependents) {
        return dependents * DEDUCTION_PER_DEPENDENT;
    }

    static double computeDed(double grossPay, double totalHours, int dependents) {
        double sss = computeSSS(grossPay);
        double pagibig = computePagIbig(grossPay);
        double philhealth = computePhilhealth(totalHours);
        double tax = computeTax(grossPay);
        double dependent = computeDependentDed(dependents);
        return sss + pagibig + philhealth + tax + dependent;
    }

    static double computeNP(double grossPay, double totalDed) {
        double net = grossPay - totalDed;
        if (net < 0) {
            return 0;
        }
        return net;
    }

    static void paySlip(String lastName, String firstName, String middleName, String department, String position, boolean isManager, int dependents, double rate, double[] weeklyHours, double grossPay, double totalDed, double netPay) {
        double regularHours = computeRegularHours(weeklyHours);
        double overtimeHours = computeOvertimeHours(weeklyHours);
        double totalHours = computeTotalHours(weeklyHours);

        newLine();
        printDivider('=');
        System.out.printf("%23s%n", "PAYSLIP");
        printDivider('=');

        String middleInitial = middleName.isEmpty() ? "" : Character.toUpperCase(middleName.charAt(0)) + ".";

        System.out.println("Employee Information");
        printRow("Name", lastName + ", " + firstName + " " + middleInitial);
        printRow("Department", department);
        printRow("Position", position);
        printRow("Manager", isManager ? "Yes" : "No");
        printRow("Dependents", String.valueOf(dependents));
        printDivider('-');

        System.out.println("Hours");
        printAmount("Regular Hours", regularHours);
        printAmount("Overtime Hours", overtimeHours);
        printAmount("Total Hours", totalHours);
        printDivider('-');

        System.out.println("Earnings (PHP)");
        printAmount("Rate per Hour", rate);
        if (overtimeHours > 0) {
            printAmount("Overtime Rate", rate * OVERTIME_MULTIPLIER);
        }
        printAmount("Regular Pay", computeRegularPay(rate, regularHours));
        if (overtimeHours > 0) {
            printAmount("Overtime Pay", computeOvertimePay(rate, overtimeHours));
        }
        printAmount("Allowance", computeAllowance(isManager));
        printAmount("Gross Pay", grossPay);
        printDivider('-');

        System.out.println("Deductions (PHP)");
        printAmount("SSS", computeSSS(grossPay));
        printAmount("Pag-IBIG", computePagIbig(grossPay));
        printAmount("PhilHealth", computePhilhealth(totalHours));
        printAmount("Withholding Tax", computeTax(grossPay));
        printAmount("Dependent Deduction", computeDependentDed(dependents));
        printAmount("Total Deductions", totalDed);
        printDivider('=');

        printAmount("NET PAY", netPay);
        printDivider('=');
        newLine();
    }

    static void computePayroll() {
        System.out.println("Please enter employee information.");
        String lastName = input(prompt("Last Name"));
        String firstName = input(prompt("First Name"));
        System.out.print(prompt("Middle Name (optional)"));
        String middleName = sc.nextLine().trim();
        String department = input(prompt("Department"));
        String position = input(prompt("Position"));
        boolean isManager = readYesNo(prompt("Manager? (Y/N)")) == 'Y';
        int dependents = checkInt(prompt("Number of Dependents"), 0, 10);
        double rate = checkDouble(prompt("Rate per Hour"), 1, 100000);

        newLine();
        System.out.println("Enter the number of hours worked per week:");
        double[] weeklyHours = new double[WEEKS_IN_MONTH];
        for (int week = 0; week < WEEKS_IN_MONTH; week++) {
            weeklyHours[week] = checkDouble(prompt("Week " + (week + 1)), 0, 168);
        }

        double totalHours = computeTotalHours(weeklyHours);
        double grossPay = computeGP(rate, weeklyHours, isManager);
        double totalDed = computeDed(grossPay, totalHours, dependents);
        double netPay = computeNP(grossPay, totalDed);

        clearScreen();

        paySlip(lastName, firstName, middleName, department, position, isManager, dependents, rate, weeklyHours, grossPay, totalDed, netPay);

        if (netPay <= 0) {
            System.out.println("WARNING: Deductions exceed gross pay of employee. Please review if this is intentional.");
        }
    }

    static void printBanner() {
        System.out.println("JPS-PAYROLL-OS v1.0.0");
        System.out.println("Copyright (C) C1A Corporation. All rights reserved.");
        System.out.println("Type 'help' for a list of commands.");
    }

    // Wow estetik

    static String prompt(String label) {
        return String.format("%-22s: ", label);
    }

    static void printRow(String label, String value) {
        System.out.printf("  %-22s: %s%n", label, value);
    }

    static void printAmount(String label, double amount) {
        System.out.printf("  %-22s: %,12.2f%n", label, amount);
    }

    static void printDivider(char c) {
        System.out.println(String.valueOf(c).repeat(40));
    }

    static String input(String prompt) {
        String text;
        do {
            System.out.print(prompt);
            text = sc.nextLine().trim();
            if (text.isEmpty()) {
                System.out.println("This field cannot be blank.");
            }
        } while (text.isEmpty());
        return text;
    }

    static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Works in IDE terminals but it makes the next print statement weird
        /* for (int i = 0; i < 50; i++) {
         *     System.out.println();
        } */
    }

    static void newLine() {
        System.out.println();
    }

    // validators

    static int checkInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {

            }
            System.out.println("Invalid input. Enter a whole number from " + min + " to " + max + ".");
        }
    }

    static double checkDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {

            }
            System.out.printf("Invalid input. Enter a number from %,.0f to %,.0f.%n", min, max);
        }
    }

    static char readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = sc.nextLine().trim().toUpperCase();
            if (answer.equals("Y") || answer.equals("YES")) {
                return 'Y';
            } else if (answer.equals("N") || answer.equals("NO")) {
                return 'N';
            }
            System.out.println("Please enter Y or N.");
        }
    }

    public static void main(String[] args) {
        clearScreen();
        printBanner();

        String command;

        do {
            command = input("> ").toLowerCase();
            if (command.equals("help")) {
                for (int i = 0; i < COMMANDS.length; i++) {
                    System.out.printf("%-15s: %s%n", COMMANDS[i], DESCRIPTIONS[i]);
                }
            } else if (command.equals("exit")) {
                break;
            } else if (command.equals("clear")) {
                clearScreen();
                printBanner();
            } else if (command.equals("compute")) {
                do {
                    clearScreen();
                    computePayroll();
                } while (readYesNo("Do you want to compute another payroll? (Y/N) ") == 'Y');
                clearScreen();
                printBanner();
            } else {
                System.out.println("Invalid command. Type 'help' for a list of commands.");
            }
        } while (true);

        System.out.println("Thank you!");
        sc.close();
    }
}
