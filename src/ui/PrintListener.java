package ui;

public interface PrintListener {

    void printRoomList(String roomID, String price, String roomType);

    void printPaymentOptions() ;

    void printOptions();

    void printDone();

    void printError();

    void printMessage(String message);

    void printGreetings();

}
