import data.DataManager;
import data.EventHandler;
import data.FileManager;
import data.model.*;
import ui.PrintHandler;
import ui.PrintListener;
import utils.ControllerManager;
import utils.DataConverter;
import utils.RoomManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
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
            case 8 -> findUs();
            case 9 -> getInfo();
            default -> handleError();
        }
    }

    private void bookARoom() {
        String roomNumber = controllerManager.readRoomNumber();
        Room room = rooms.stream().
                filter(item -> item.getRoomID().
                equals(roomNumber)).findAny().orElse(null);

        if (room != null) {
            LocalDate checkInDate = controllerManager.readCheckInDate();
            LocalDate checkOutDate = controllerManager.readCheckOutDate();

            if (controllerManager.
                    checkRoomAvailability(roomNumber, checkInDate, checkOutDate)) {
                String name = controllerManager.readCustomerName();
                String socialSecurityNumber = controllerManager.readCustomerSocialSecurityNumber();
                String emailAddress = controllerManager.readCustomerEmailAdress();

                Reservation reservation = new Reservation(
                        new Customer(name, socialSecurityNumber, emailAddress), room, checkInDate, checkOutDate);

                int amountToPay = controllerManager.amountToPay(room.getPrice(), checkInDate, checkOutDate);
                printListener.printAmountToPay(roomNumber, amountToPay ,checkInDate, checkOutDate);

                controllerManager.paymentOption(name, amountToPay);
                fileManager.writeFile(dataConverter.convertToString(reservation));
            } else {
                printListener.printMessage("The room is occupied");
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
            controllerManager.removeLine(line);
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

    public void findUs(){
    EventQueue.invokeLater(new Runnable() {
        @Override
        public void run () {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }

            ImageIcon icon = new ImageIcon(Controller.class.getResource("/Hotelmap.png"));
            JOptionPane.showMessageDialog(
                    null,
                    "",
                    "Hotell - By the sea", JOptionPane.INFORMATION_MESSAGE,
                    icon);
        }
    });
}


    private void getInfo() {
        System.out.println("\"Hotell By the Sea\"\n" +
                "Lindgatan 120\n" +
                "13024 - Stockholm\n\n" +
                "Tel. 08-2344490\n" +
                "info@bytheseahotell.se");

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