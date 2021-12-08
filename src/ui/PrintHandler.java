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
                (6) delete item (by name)
                (7) save
                (8) book a room
                (9) checkRoomAvailability""");

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
    public void promptForName() {
        System.out.println("Please enter room name: ");
    }

    @Override
    public void printGreetings() {
        System.out.println("Hello!");
    }
}
