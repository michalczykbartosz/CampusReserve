package campusreserve.database;
import campusreserve.model.Room;
import campusreserve.model.Equipment;


import java.util.ArrayList;

public class DataStore
{
    public ArrayList<Room> getRoomList()
    {
        return roomList;
    }

    public ArrayList<Equipment> getEquipmentList()
    {
        return equipmentList;
    }

    private ArrayList<Room> roomList = new ArrayList<>();
    private ArrayList<Equipment> equipmentList = new ArrayList<>();
}