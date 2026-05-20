package campusreserve.database;
import campusreserve.model.Room;
import campusreserve.model.Equipment;
import campusreserve.model.Reservation;


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

    public ArrayList<Reservation> getReservationList() { return reservationList; }

    private ArrayList<Room> roomList = new ArrayList<>();
    private ArrayList<Equipment> equipmentList = new ArrayList<>();
    private ArrayList<Reservation> reservationList = new ArrayList<>();


}