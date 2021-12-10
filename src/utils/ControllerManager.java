package utils;

import java.time.LocalDate;
import java.util.Scanner;

public class ControllerManager {

    private Scanner scan = new Scanner(System.in) ;

    public boolean bookingIsAfterExisting(String reservationText, LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(LocalDate.parse(reservationText)) && endDate.isAfter(LocalDate.parse(reservationText));
    }

    public boolean bookingIsBeforeExisting(String reservationText, LocalDate startDate, LocalDate endDate) {
        return startDate.isBefore(LocalDate.parse(reservationText)) && endDate.isBefore(LocalDate.parse(reservationText));
    }


    public String readCustomerName() {
        System.out.println("Please enter your name");
        return scan.next().trim();
    }

    public String readRoomNumber() {
        System.out.println("Please enter room number");
        return scan.next().trim();
    }

    public String readCustomerSocialSecurityNumber() {
        System.out.println("Please enter your social security number");
        return scan.next().trim();
    }

    public String readCustomerEmailAdress() {
        System.out.println("Please enter your email address");
        return scan.next().trim();
    }

    public LocalDate readCheckInDate() {
        System.out.println("Please enter your check in date");
        String checkIn = scan.next().trim();
        return LocalDate.parse(checkIn);
    }

    public LocalDate readCheckOutDate() {
        System.out.println("Please enter your check out date");
        String checkOut = scan.next().trim();
        return LocalDate.parse(checkOut);
    }
}
