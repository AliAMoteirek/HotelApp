package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    String fileNameRead = "Bok1.csv";
    String fileNameWrite = "Reservation.csv";

    private final EventHandler eventHandler ;

    public FileManager(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void readFile(){
        new Thread(() -> {
            List<String> result = generateData();
            eventHandler.readDataFromFile(result) ;
        }).start() ;
    }

    public void writeFile(String text) {
        new Thread(() -> {
            writeDataToFile(text);
            eventHandler.writeDataToFile() ;
        }).start();
    }

    //method that reads data from a file line by line
    private List<String> generateData() {
        ArrayList<String> dataList = new ArrayList<>();
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(fileNameRead))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                dataList.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Exception while reading the file was thrown");
            System.out.println(e.getMessage());
        }
        return dataList;
    }

    //method that reads data from a file line by line
    public List<String> generateReservationData() {
        ArrayList<String> dataList = new ArrayList<>();
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(fileNameWrite))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                dataList.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Exception while reading the file was thrown");
            System.out.println(e.getMessage());
        }
        return dataList;
    }

    //method that writes data to a file
    private void writeDataToFile(String text) {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(fileNameWrite, true))) {
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Exception while writing in the file was thrown");
            System.out.println(e.getMessage());
        }
    }
}
