package utils;

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

public class ControllerManager {

    private Scanner scan = new Scanner(System.in);
    private PrintListener printListener = new PrintHandler();

    public boolean bookingIsAfterExisting(String reservationText, LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(LocalDate.parse(reservationText)) && endDate.isAfter(LocalDate.parse(reservationText));
    }

    public boolean bookingIsBeforeExisting(String reservationText, LocalDate startDate, LocalDate endDate) {
        return startDate.isBefore(LocalDate.parse(reservationText)) && endDate.isBefore(LocalDate.parse(reservationText));
    }


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
        printListener.printMessage("Please enter your check in date");
        String checkIn = scan.next().trim();
        return LocalDate.parse(checkIn);
    }

    public LocalDate readCheckOutDate() {
        printListener.printMessage("Please enter your check out date");
        String checkOut = scan.next().trim();
        return LocalDate.parse(checkOut);
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

}
