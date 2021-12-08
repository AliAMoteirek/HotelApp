import data.DataManager;
import data.EventHandler;
import data.FileManager;
import data.model.*;
import ui.PrintHandler;
import ui.PrintListener;
import utils.DataConverter;
import utils.RoomManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static utils.Constants.SPLIT_REGEX;

public class Controller implements EventHandler {

    private final PrintListener printListener = new PrintHandler() ;
    private final RoomManager roomManager = new RoomManager();
    private final FileManager fileManager = new FileManager(this);
    private final DataConverter dataConverter = new DataConverter();

    private List<Room> rooms = new ArrayList<>();

    public void start(){
        printListener.printGreetings();
        fileManager.readFile();
    }

    private void chooseOption() {
        Scanner input = new Scanner(System.in);
        int option = input.nextInt();
        while (option != 0) {
            onOptionSelected(option);
            option = input.nextInt();
        }
    }

    private void onOptionSelected(int option) {
        switch (option) {
            case 1 -> printAllRooms();
            case 2 -> printNormalSingleRooms();
            case 3 -> printNormalDoubleRooms();
            case 4 -> printLuxuryRooms();
            case 5 -> printSuiteRooms();
            case 6 -> deleteBooking();
            case 7 -> saveRooms();
            case 8 -> bookARoom();
            case 9 -> printAvailableRooms();
            default -> handleError();
        }
    }

    private boolean checkRoomAvailability(String roomNumber, LocalDate startDate, LocalDate endDate) {
        boolean roomExists = false;
        List<String> text = fileManager.generateReservationData();
        //skip first line of csv (header)
        for (String line : text.stream().skip(1).collect(Collectors.toList())) {
            String[] reservationText = line.split(SPLIT_REGEX);
            if (reservationText[0].equalsIgnoreCase(roomNumber)) {
                roomExists = true;
            }
        }
        if (!roomExists) {
            return true;
        }
        //skip first line of csv (header)
        for (String line : text.stream().skip(1).collect(Collectors.toList())) {
            String[] reservationText = line.split(SPLIT_REGEX);
            //Check if room exists in file. If not return true
            if (bookingIsBeforeExisting(reservationText[4], startDate, endDate) ||
                    bookingIsAfterExisting(reservationText[5], startDate, endDate)) {
                return true;
            }
        }
        return false;
    }

    private boolean bookingIsAfterExisting(String reservationText, LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(LocalDate.parse(reservationText)) && endDate.isAfter(LocalDate.parse(reservationText));
    }

    private boolean bookingIsBeforeExisting(String reservationText, LocalDate startDate, LocalDate endDate) {
        return startDate.isBefore(LocalDate.parse(reservationText)) && endDate.isBefore(LocalDate.parse(reservationText));
    }

    private void bookARoom() {
        System.out.println("Please enter room number:");
        Scanner scan = new Scanner(System.in);
        String roomNumber = scan.next().trim();
        Room room = rooms.stream().filter(item -> item.getRoomID().equals(roomNumber)).findAny().orElse(null);
        if (room != null) {
            System.out.println("Please enter your name:");
            String name = scan.next().trim();
            System.out.println("Please enter your social security number:");
            String socialSecurityNumber = scan.next().trim();
            System.out.println("Please enter your email address:");
            String emailAddress = scan.next().trim();
            System.out.println("Please enter your check in date:");
            String checkInDate = scan.next().trim();
            LocalDate localDate = LocalDate.parse(checkInDate);
            System.out.println("Please enter your check out date:");
            String checkOut = scan.next().trim();
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            Reservation reservation = new Reservation(
                    new Customer(name, socialSecurityNumber, emailAddress), room, localDate, checkOutDate);
            fileManager.writeFile(dataConverter.convertToString1(reservation));
        }
    }

    private void printAvailableRooms() {
        System.out.println("Please enter room number:");
        Scanner scan = new Scanner(System.in);
        String roomNumber = scan.next().trim();
        Room room = rooms.stream().filter(item -> item.getRoomID().equals(roomNumber)).findAny().orElse(null);
        if (room != null) {
            System.out.println("Please enter your check in date:");
            String checkInDate = scan.next().trim();
            LocalDate localDate = LocalDate.parse(checkInDate);
            System.out.println("Please enter your check out date:");
            String checkOut = scan.next().trim();
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            System.out.println(checkRoomAvailability(roomNumber, localDate, checkOutDate));
        }
    }


    // need to change from room to booking
    private void deleteBooking() {
        printListener.promptForName();
        Scanner input = new Scanner(System.in);
        String name = input.nextLine();
        Room room = rooms.stream()
                .filter(item -> item.getRoomID().equals(name))
                .findFirst().orElse(null);
        if(room != null){
            rooms.remove(room);
            printListener.printDone();
        } else {
            printListener.printError();
        }
    }

    private void handleError() {
        printListener.printError();
    }

    private void printAllRooms() {
        printNormalSingleRooms();
        printNormalDoubleRooms();
        printLuxuryRooms();
        printSuiteRooms();
    }

    private void printNormalSingleRooms() {
        List<RoomNormalSingle> normalSingleRooms = roomManager.getNormalRoomsSingle(rooms);
        for (RoomNormalSingle item :
                normalSingleRooms) {
            printListener.printRoomList(
                    item.getRoomID(),
                    String.valueOf(item.getPrice()),
                    item.getRoomType()
            );
        }
    }

    private void printNormalDoubleRooms() {
        List<RoomNormalDouble> normalDoubleRooms = roomManager.getNormalRoomsDouble(rooms);
        for (RoomNormalDouble item :
                normalDoubleRooms) {
            printListener.printRoomList(
                    item.getRoomID(),
                    String.valueOf(item.getPrice()),
                    item.getRoomType()
            );
        }
    }

    private void printLuxuryRooms() {
        List<RoomLuxury> luxuryRooms = roomManager.getLuxuryRooms(rooms);
        for (RoomLuxury item :
                luxuryRooms) {
            printListener.printRoomList(
                    item.getRoomID(),
                    String.valueOf(item.getPrice()),
                    item.getRoomType()
            );
        }
    }


    private void printSuiteRooms() {
        List<RoomSuite> suiteRooms = roomManager.getSuiteRooms(rooms);
        for (RoomSuite item :
                suiteRooms) {
            printListener.printRoomList(
                    item.getRoomID(),
                    String.valueOf(item.getPrice()),
                    item.getRoomType()
            );
        }
    }

    private void saveRooms() {
        if (DataManager.getInstance().getRooms() != null) {
            fileManager.writeFile(dataConverter.convertToString(
                    DataManager.getInstance().getRooms()
            ));
        }
    }


    @Override
    public void readDataFromFile(List<String> result) {
        DataManager.getInstance().setRooms(result);
        printListener.printOptions();
        rooms = DataManager.getInstance().getRooms();
        chooseOption();
    }

    @Override
    public void writeDataToFile() {
        printListener.printDone();
    }
}
