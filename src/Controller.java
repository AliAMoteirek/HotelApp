import data.DataManager;
import data.EventHandler;
import data.FileManager;
import data.model.*;
import ui.PrintHandler;
import ui.PrintListener;
import utils.ControllerManager;
import utils.DataConverter;
import utils.RoomManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static utils.Constants.SPLIT_REGEX;

public class Controller implements EventHandler {

    private final PrintListener printListener = new PrintHandler();
    private final RoomManager roomManager = new RoomManager();
    private final FileManager fileManager = new FileManager(this);
    private final DataConverter dataConverter = new DataConverter();
    private ControllerManager controllerManager = new ControllerManager();

    private List<Room> rooms = new ArrayList<>();

    public void start() {
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
            case 6 -> bookARoom();
            case 7 -> removeReservation();
            default -> handleError();
        }
    }

    private boolean checkRoomAvailability(String roomNumber, LocalDate startDate, LocalDate endDate) {
        boolean roomExists = false;
        boolean condition = false;
        List<String> text = fileManager.generateReservationData();

        for (String line : text.stream().skip(1).collect(Collectors.toList())) {
            String[] reservationText = line.split(SPLIT_REGEX);
            if (reservationText[0].equalsIgnoreCase(roomNumber)) {
                roomExists = true;
            }
        }
        if (!roomExists) {
            condition = true;
        }

        File file = new File("Reservation.csv");
        List<String> out = null;
        try {
            out = Files.lines(file.toPath())
                    .filter(line -> line.startsWith(roomNumber))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (out == null) {
            condition = true;
        } else {
            for (String line : out.stream().collect(Collectors.toList())) {
                String[] reservationText = line.split(SPLIT_REGEX);
                if (controllerManager.bookingIsBeforeExisting(reservationText[4], startDate, endDate) ||
                        controllerManager.bookingIsAfterExisting(reservationText[5], startDate, endDate)) {
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

    private int amountToPay(int pricePerNight, LocalDate checkInDate, LocalDate checkoutDate) {
        return (pricePerNight * Period.between(checkInDate, checkoutDate).getDays());
    }

    private void bookARoom() {
        String roomNumber = controllerManager.readRoomNumber();
        Room room = rooms.stream().filter(item -> item.getRoomID().equals(roomNumber)).findAny().orElse(null);

        if (room != null) {
            LocalDate checkInDate = controllerManager.readCheckInDate();
            LocalDate checkOutDate = controllerManager.readCheckOutDate();
            if (checkRoomAvailability(roomNumber, checkInDate, checkOutDate)) {
                String name = controllerManager.readCustomerName();
                String socialSecurityNumber = controllerManager.readCustomerSocialSecurityNumber();
                String emailAddress = controllerManager.readCustomerEmailAdress();
                Reservation reservation = new Reservation(
                        new Customer(name, socialSecurityNumber, emailAddress), room, checkInDate, checkOutDate);
                fileManager.writeFile(dataConverter.convertToString1(reservation));
                System.out.println("Room number " + roomNumber + " will cost " +
                        amountToPay(room.getPrice(), checkInDate, checkOutDate) +
                        "kr from " + checkInDate + " to " + checkOutDate);
            } else {
                System.out.println("The room is occupied");
            }
        }
    }

    private void removeReservation() {
        String roomNumber = controllerManager.readRoomNumber();
        Room room = rooms.stream().filter(item -> item.getRoomID().equals(roomNumber)).findAny().orElse(null);
        if (room != null) {
            LocalDate checkInDate = controllerManager.readCheckInDate();
            LocalDate checkOutDate = controllerManager.readCheckOutDate();

            String name = controllerManager.readCustomerName();
            String socialSecurityNumber = controllerManager.readCustomerSocialSecurityNumber();
            String emailAddress = controllerManager.readCustomerEmailAdress();
            String line = roomNumber + ";" + name +
                    ";" + socialSecurityNumber + ";" +
                    emailAddress + ";" +
                    checkInDate + ";" + checkOutDate;
            removeLine(line);

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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
