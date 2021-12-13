package utils;

import Payment.Payment;
import Payment.CreditCard;
import Payment.Swish;
import ui.PrintHandler;
import ui.PrintListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static utils.Constants.SPLIT_REGEX;

public class ControllerManager {

    private Scanner scan = new Scanner(System.in);
    private PrintListener printListener = new PrintHandler();

    public String readCustomerName() {
        printListener.printMessage("Please enter your name");
        return scan.next().trim();
    }

    public String readRoomNumber() {
        printListener.printMessage("Please enter room number");
        return scan.next().trim();
    }

    public String readCustomerSocialSecurityNumber() {
        printListener.printMessage("Please enter your social security number");
        return scan.next().trim();
    }

    public String readCustomerEmailAdress() {
        printListener.printMessage("Please enter your email address");
        return scan.next().trim();
    }

    public LocalDate readCheckInDate() {
        try {
            printListener.printMessage("Please enter your check in date");
            String checkIn = scan.next().trim();
            return LocalDate.parse(checkIn);
        } catch (Exception e) {
            printListener.printMessage("Invalid date");
            System.exit(0);
            return null;
        }
    }

    public LocalDate readCheckOutDate() {
        try {
            printListener.printMessage("Please enter your check out date");
            String checkOut = scan.next().trim();
            return LocalDate.parse(checkOut);
        } catch (Exception e) {
            printListener.printMessage("Invalid date");
            System.exit(0);
            return null;
        }
    }

    public void removeLine(String lineContent) {
        new Thread(() -> {
            File file = new File("Reservation.csv");
            List<String> out = null;
            try {
                out = Files.lines(file.toPath())
                        .filter(line -> !line.contains(lineContent))
                        .collect(Collectors.toList());
                Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                printListener.printDone();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public int amountToPay(int pricePerNight, LocalDate checkInDate, LocalDate checkoutDate) {
        return (pricePerNight * Period.between(checkInDate, checkoutDate).getDays());
    }

    public void pay(Payment paymentStrategy, int amountToPay) {
        paymentStrategy.pay(amountToPay);
    }

    public String readCreditCard() {
        printListener.printMessage("Please enter your credit card number");
        return scan.next().trim();
    }

    public String readSwish() {
        printListener.printMessage("Please enter your phone number");
        return scan.next().trim();
    }

    public void paymentOption(String name, int amountToPay) {
        printListener.printPaymentOptions();
        Scanner input = new Scanner(System.in);
        int option = input.nextInt();
        switch (option) {
            case 1 -> {
                String creditCard = readCreditCard();
                pay(new CreditCard(name, creditCard), amountToPay);
            }
            case 2 -> {
                String phoneNumber = readSwish();
                pay(new Swish(name, phoneNumber), amountToPay);
            }
            default -> printListener.printMessage("The payment will be done at the hotel");
        }
    }

    public boolean bookingIsAfterExisting(String reservationText, LocalDate startDate, LocalDate endDate) {
        return (startDate.isAfter(LocalDate.parse(reservationText)) ||
                startDate.isEqual(LocalDate.parse(reservationText))) &&
                endDate.isAfter(LocalDate.parse(reservationText));
    }

    public boolean bookingIsBeforeExisting(String reservationText, LocalDate startDate, LocalDate endDate) {
        return startDate.isBefore(LocalDate.parse(reservationText)) &&
                (endDate.isBefore(LocalDate.parse(reservationText)) ||
                        endDate.isEqual(LocalDate.parse(reservationText)));
    }

    public boolean checkRoomAvailability(String roomNumber, LocalDate startDate, LocalDate endDate) {
        boolean condition = false;

        File file = new File("Reservation.csv");
        List<String> out = null;
        try {
            out = Files.lines(file.toPath())
                    .filter(line -> line.startsWith(roomNumber))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (out.isEmpty()) {
            condition = true;
        } else {
            for (String line : out.stream().collect(Collectors.toList())) {
                String[] reservationText = line.split(SPLIT_REGEX);
                if (bookingIsBeforeExisting(reservationText[4], startDate, endDate) ||
                        bookingIsAfterExisting(reservationText[5], startDate, endDate)) {
                    condition = true;
                } else {
                    condition = false;
                }
                if (condition == false) {
                    break;
                }
            }
        }
        return condition;
    }
}
