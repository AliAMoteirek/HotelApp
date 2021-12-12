package data.model;

public abstract class Room {

    private String roomID ;
    private int price ;

    public Room(String roomID, int price) {
        this.roomID = roomID;
        this.price = price;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID='" + roomID + '\'' +
                ", price=" + price +
                '}';
    }
}
