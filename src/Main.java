/*
 * finalCode-C1A-Group7.java
 * Written by:
 * - Gavin Adriel Jatulan
 * - Elijah Patrick Panique
 * - Kyle Christian Salvacion
 * Date:
 * Description: Monthly Payroll Program
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

    /**
     * Returns the regular (non-overtime) part of one week's hours.
     *
     * @param hours hours worked in a single week
     * @return {@code hours}, capped at {@link #REGULAR_HOURS_PER_WEEK}
     */
    static double getRegularHours(double hours) {
        if (hours > REGULAR_HOURS_PER_WEEK) {
            return REGULAR_HOURS_PER_WEEK;
        }
        return hours;
    }

    /**
     * Returns the overtime part of one week's hours.
     *
     * @param hours hours worked in a single week
     * @return hours above {@link #REGULAR_HOURS_PER_WEEK}, or 0 if there are none
     */
    static double getOvertimeHours(double hours) {
        if (hours > REGULAR_HOURS_PER_WEEK) {
            return hours - REGULAR_HOURS_PER_WEEK;
        }
        return 0;
    }

    /**
     * Adds up the regular hours across all weeks of the month.
     *
     * @param weeklyHours hours worked in each week
     * @return total regular hours for the month
     */
    static double computeRegularHours(double[] weeklyHours) {
        double total = 0;
        for (int i = 0; i < weeklyHours.length; i++) {
            total = total + getRegularHours(weeklyHours[i]);
        }
        return total;
    }

    /**
     * Adds up the overtime hours across all weeks of the month.
     *
     * @param weeklyHours hours worked in each week
     * @return total overtime hours for the month
     */
    static double computeOvertimeHours(double[] weeklyHours) {
        double total = 0;
        for (int i = 0; i < weeklyHours.length; i++) {
            total = total + getOvertimeHours(weeklyHours[i]);
        }
        return total;
    }

    /**
     * Returns the total hours worked in the month (regular plus overtime).
     *
     * @param weeklyHours hours worked in each week
     * @return total hours for the month
     */
    static double computeTotalHours(double[] weeklyHours) {
        return computeRegularHours(weeklyHours) + computeOvertimeHours(weeklyHours);
    }

    /**
     * Computes pay for regular hours.
     *
     * @param rate  hourly rate (PHP)
     * @param hours regular hours worked
     * @return regular pay (PHP)
     */
    static double computeRegularPay(double rate, double hours) {
        return rate * hours;
    }

    /**
     * Computes pay for overtime hours at {@link #OVERTIME_MULTIPLIER} times the hourly rate.
     *
     * @param rate  regular hourly rate (PHP)
     * @param hours overtime hours worked
     * @return overtime pay (PHP)
     */
    static double computeOvertimePay(double rate, double hours) {
        double overtimeRate = rate * OVERTIME_MULTIPLIER;
        return overtimeRate * hours;
    }

    /**
     * Computes the monthly transportation allowance. Only managers receive it.
     *
     * @param isManager whether the employee is a manager
     * @return {@link #TRANSPORT_PER_WEEK} times {@link #WEEKS_IN_MONTH} for managers, otherwise 0
     */
    static double computeAllowance(boolean isManager) {
        if (isManager) {
            return TRANSPORT_PER_WEEK * WEEKS_IN_MONTH;
        }
        return 0;
    }

    /**
     * Computes the SSS contribution from gross pay.
     *
     * <ul>
     *   <li>5,000 or less: flat 105.00</li>
     *   <li>5,000.01 to 10,000: 5% of gross pay</li>
     *   <li>10,000.01 to 15,000: 8% of gross pay + 75</li>
     *   <li>Above 15,000: 12% of gross pay + 110</li>
     * </ul>
     *
     * @param grossPay monthly gross pay (PHP)
     * @return SSS contribution (PHP)
     */
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

    /**
     * Computes the Pag-IBIG contribution from gross pay.
     * It is a flat 100.00 for gross pay of 5,000 or less. Otherwise it is 3% of gross pay.
     *
     * @param grossPay monthly gross pay (PHP)
     * @return Pag-IBIG contribution (PHP)
     */
    static double computePagIbig(double grossPay) {
        if (grossPay <= 5000) {
            return 100.00;
        }

        return grossPay * 0.03;
    }

    /**
     * Computes the PhilHealth contribution. Employees who worked fewer than
     * 10 hours in the month pay nothing. Everyone else pays the fixed
     * {@link #PHILHEALTH_AMOUNT}.
     *
     * @param totalHours total hours worked in the month
     * @return PhilHealth contribution (PHP)
     */
    static double computePhilhealth(double totalHours) {
        if (totalHours < 10) {
            return 0;
        }

        return PHILHEALTH_AMOUNT;
    }

    /**
     * Returns the withholding tax rate for a given gross pay.
     *
     * <ul>
     *   <li>10,000 or less: 3%</li>
     *   <li>10,000.01 to 25,000: 8%</li>
     *   <li>25,000.01 to 40,000: 11%</li>
     *   <li>Above 40,000: 13.5%</li>
     * </ul>
     *
     * @param grossPay monthly gross pay (PHP)
     * @return tax rate as a decimal (for example, 0.08 for 8%)
     */
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
    
    /**
     * Computes monthly gross pay: regular pay + overtime pay + allowance.
     *
     * @param rate        hourly rate (PHP)
     * @param weeklyHours hours worked in each week
     * @param isManager   whether the employee is a manager (receives allowance)
     * @return monthly gross pay (PHP)
     */
    static double computeGP(double rate, double[] weeklyHours, boolean isManager) {
        double regularPay = computeRegularPay(rate, computeRegularHours(weeklyHours));
        double overtimePay = computeOvertimePay(rate, computeOvertimeHours(weeklyHours));
        double allowance = computeAllowance(isManager);
        return regularPay + overtimePay + allowance;
    }

    /**
     * Computes withholding tax. The rate from {@link #getTaxRate(double)} is
     * applied to the whole gross pay.
     *
     * @param grossPay monthly gross pay (PHP)
     * @return withholding tax (PHP)
     */
    static double computeTax(double grossPay) {
        return grossPay * getTaxRate(grossPay);
    }

    /**
     * Computes the deduction for dependents.
     *
     * @param dependents number of dependents
     * @return {@code dependents} times {@link #DEDUCTION_PER_DEPENDENT} (PHP)
     */
    static double computeDependentDed(int dependents) {
        return dependents * DEDUCTION_PER_DEPENDENT;
    }

    /**
     * Computes total deductions: SSS + Pag-IBIG + PhilHealth + withholding tax
     * + dependent deduction.
     *
     * @param grossPay   monthly gross pay (PHP)
     * @param totalHours total hours worked in the month
     * @param dependents number of dependents
     * @return total deductions (PHP)
     */
    static double computeDed(double grossPay, double totalHours, int dependents) {
        double sss = computeSSS(grossPay);
        double pagibig = computePagIbig(grossPay);
        double philhealth = computePhilhealth(totalHours);
        double tax = computeTax(grossPay);
        double dependent = computeDependentDed(dependents);
        return sss + pagibig + philhealth + tax + dependent;
    }

    /**
     * Computes net pay (gross pay minus total deductions). Net pay never goes below 0.
     *
     * @param grossPay monthly gross pay (PHP)
     * @param totalDed total deductions (PHP)
     * @return net pay (PHP), or 0 if deductions are greater than gross pay
     */
    static double computeNP(double grossPay, double totalDed) {
        double net = grossPay - totalDed;
        if (net < 0) {
            return 0;
        }
        return net;
    }

    /**
     * Prints a formatted payslip. It shows employee information, an hours
     * breakdown, earnings, each deduction, and net pay. Overtime rows are only
     * shown when the employee worked overtime.
     *
     * @param lastName    employee's last name
     * @param firstName   employee's first name
     * @param middleName  employee's middle name (may be empty; shown as an initial)
     * @param department  employee's department
     * @param position    employee's position
     * @param isManager   whether the employee is a manager
     * @param dependents  number of dependents
     * @param rate        hourly rate (PHP)
     * @param weeklyHours hours worked in each week
     * @param grossPay    monthly gross pay (PHP)
     * @param totalDed    total deductions (PHP)
     * @param netPay      net pay (PHP)
     */
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

    /**
     * Runs one payroll computation. It asks for the employee's details and
     * weekly hours, computes gross pay, deductions, and net pay, clears the
     * screen, and prints the payslip. It also prints a warning if deductions
     * use up the whole gross pay.
     *
     * <p>Input limits: dependents 0 to 10, hourly rate 1 to 100,000, and
     * weekly hours 0 to 168. The middle name may be left blank. All other
     * text fields are required.</p>
     */
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

    /**
     * Formats an input label so all prompts line up.
     *
     * @param label text to show before the input
     * @return the label padded to 22 characters, followed by {@code ": "}
     */
    static String prompt(String label) {
        return String.format("%-22s: ", label);
    }

    /**
     * Prints a label-value line on the payslip.
     *
     * @param label row label
     * @param value text to display
     */
    static void printRow(String label, String value) {
        System.out.printf("  %-22s: %s%n", label, value);
    }

    /**
     * Prints a label and a number on the payslip.
     *
     * @param label  row label
     * @param amount value to display
     */
    static void printAmount(String label, double amount) {
        System.out.printf("  %-22s: %,12.2f%n", label, amount);
    }

    /**
     * Prints a 40-character divider line.
     *
     * @param c character to repeat (for example, {@code '='} or {@code '-'})
     */
    static void printDivider(char c) {
        System.out.println(String.valueOf(c).repeat(40));
    }

    /**
     * Reads a required line of text. It keeps asking until the user enters
     * something that is not blank.
     *
     * @param prompt text shown before the input
     * @return the entered text, with leading and trailing spaces removed
     */
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

    /**
     * Clears the console. It runs {@code cls} on Windows and {@code clear}
     * on other systems. This may not work in some IDE consoles.
     */
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

    /**
     * Reads a whole number in a range. It keeps asking until the input is a
     * valid integer from {@code min} to {@code max} (inclusive).
     *
     * @param prompt text shown before the input
     * @param min    smallest allowed value
     * @param max    largest allowed value
     * @return the validated number
     */
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

    /**
     * Reads a decimal number in a range. It keeps asking until the input is a
     * valid number from {@code min} to {@code max} (inclusive).
     *
     * @param prompt text shown before the input
     * @param min    smallest allowed value
     * @param max    largest allowed value
     * @return the validated number
     */
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

    /**
     * Asks a yes/no question. It accepts Y, YES, N, or NO in any letter case
     * and keeps asking until it gets one of them.
     *
     * @param prompt question shown to the user
     * @return {@code 'Y'} for yes or {@code 'N'} for no
     */
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
