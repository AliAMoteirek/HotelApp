package ui;

import java.time.LocalDate;

public interface PrintListener {

    void printRoomList(String roomID, String price, String roomType);

    void printAmountToPay(String roomNumber, int amountToPay, LocalDate checkInDate, LocalDate checkOutDate);

    void printPaymentOptions();

    void printOptions();

    void printDone();

    void printError();

    void printMessage(String message);

    void printGreetings();

}
