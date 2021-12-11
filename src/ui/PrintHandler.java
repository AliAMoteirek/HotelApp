package ui;

public class PrintHandler implements PrintListener{

    @Override
    public void printRoomList(String roomID, String price, String roomType) {
        System.out.println(roomID + " is a " + roomType + " room - " + price + " kr.");
    }

    @Override
    public void printOptions() {
        System.out.println("""
                choose an option:
                (0) exit
                (1) print All Rooms
                (2) print Normal Single Rooms
                (3) print Normal Double Rooms
                (4) print Luxury Rooms
                (5) print Suite Rooms
                (6) book a room
                (7) remove a reservation""");
    }

    @Override
    public void printPaymentOptions() {
        System.out.println("""
                choose an option:
                (1) pay by card
                (2) pay by swish
                If no option is chosen the payment will be done at the hotel""");
    }

    @Override
    public void printDone() {
        System.out.println("Done!");
    }

    @Override
    public void printError() {
        System.out.println("Something went wrong!");
    }

    @Override
    public void printGreetings() {
        System.out.println("Hello!");
    }

    @Override
    public void printMessage (String message) {
        System.out.println(message);
    }
}
