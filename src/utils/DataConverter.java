package utils;

import data.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Constants.*;

public class DataConverter {

    public List<Room> convertToRooms(List<String> text) {
        List<Room> rooms = new ArrayList<>();

        for (String line :
                text.stream().skip(1).collect(Collectors.toList()) //skip first line of csv (header)
        ) {
            String[] roomText = line.split(SPLIT_REGEX);

            Room currentRoom = null;

            if (roomText[2].equalsIgnoreCase(ROOM_TYPE_NORMAL_SINGLE)) {
                currentRoom = new RoomNormalSingle(roomText[0], Integer.parseInt(roomText[1]), roomText[2]);
            } else if (roomText[2].equalsIgnoreCase(ROOM_TYPE_NORMAL_DOUBLE)) {
                currentRoom = new RoomNormalDouble(roomText[0], Integer.parseInt(roomText[1]), roomText[2]);
            } else if (roomText[2].equalsIgnoreCase(ROOM_TYPE_LUXURY)) {
                currentRoom = new RoomLuxury(roomText[0],Integer.parseInt(roomText[1]), roomText[2]) ;
            } else if (roomText[2].equalsIgnoreCase(ROOM_TYPE_SUITE)) {
                currentRoom = new RoomSuite(roomText[0],Integer.parseInt(roomText[1]), roomText[2]) ;
            }

            if (currentRoom != null) {
                rooms.add(currentRoom);
            }
        }
        return rooms;
    }

    public String convertToString(List<Room> rooms) {
        StringBuilder result = new StringBuilder(CSV_HEADER);
        String line = null;
        for (Room room :
                rooms) {
            if (room instanceof RoomNormalSingle) {
                line = room.getRoomID() + "," + room.getPrice() + ";" +
                        ((RoomNormalSingle) room).getPrice() + ";" + ROOM_TYPE_NORMAL_SINGLE;
            }
            if (room instanceof RoomNormalDouble) {
                line = room.getRoomID() + "," + room.getPrice() + ";" +
                        ((RoomNormalDouble) room).getPrice() + ";" + ROOM_TYPE_NORMAL_DOUBLE;
            }
            if(line != null){
                result.append("\n").append(line);
            }
        }
        return result.toString();
    }

    public String convertToString1(Reservation reservation) {
        StringBuilder result = new StringBuilder();
        String line = null;
        if(reservation!=null) {
            line = reservation.getRoom().getRoomID()+ ";" + reservation.getCustomer().getName() +
                    ";" + reservation.getCustomer().getSocialSecurityId() +";"+
                    reservation.getCustomer().getEmailAddress() + ";" +
                    reservation.getCheckInDate() + ";" + reservation.getCheckOutDate() ;
            if(line != null){
                result.append("\n").append(line);
            }
        }
        return result.toString();
    }

}
