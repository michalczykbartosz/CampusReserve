package campusreserve.resources;
import campusreserve.model.Room;
import campusreserve.model.Equipment;
import campusreserve.database.DataStore;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class ResourceManager
{
    private DataStore database;

    public ResourceManager(DataStore database)
    {
    this.database = database;
    }

    public void addRoom(Room newRoom)
    {
        ArrayList<Room> roomList = this.database.getRoomList();
        for(Room room : roomList)
        {
            if (room.getRoomNumber() == newRoom.getRoomNumber())
            {
                throw new IllegalArgumentException("Sala o takim numerze jest już na liście!");
            }
        }
        this.database.getRoomList().add(newRoom);

    }

    public Room getRoomByUUID(UUID wantedId)
    {
        ArrayList<Room> roomList = this.database.getRoomList();
        for (Room room : roomList)
        {
            if(room.getRoomId().equals(wantedId))
            {
                return room;
            }
        }
        throw new IllegalArgumentException("Nie ma sali o takim ID!");
    }

    public Room getRoomByNumber(int roomNumber)
    {

        ArrayList<Room> roomList = this.database.getRoomList();
        for (Room room : roomList)
        {
            if(room.getRoomNumber() == roomNumber)
            {
                return room;
            }
        }
        throw new IllegalArgumentException("Nie ma sali o takim numerze!");
    }

    public void addEquipment(Equipment equipment)
    {
        this.database.getEquipmentList().add(equipment);
    }

    public ArrayList<Equipment> getEquipmentForRoom(UUID roomId)
    {
        ArrayList<Equipment> equipmentList = this.database.getEquipmentList();
        ArrayList<Equipment> roomEquipment = new ArrayList<>();

        for (Equipment equipment : equipmentList)
        {
            if (equipment.getRoomIdOwning() != null && equipment.getRoomIdOwning().equals(roomId))
            {
                roomEquipment.add(equipment);
            }
        }

        return roomEquipment;
    }

    public Equipment getEquipmentByName(String equipmentName)
    {
        ArrayList<Equipment> equipmentList = this.database.getEquipmentList();
        for(Equipment equipment : equipmentList)
        {
            if(equipment.getEquipmentName().equalsIgnoreCase(equipmentName))
            {
                return equipment;
            }
        }
        throw new IllegalArgumentException("Brak urządzeń o takiej nazwie!");
    }

    public ArrayList<Room> getRoomsByBuilding(String buildingName)
    {
        ArrayList<Room> roomList = this.database.getRoomList();
        ArrayList<Room> roomListByBuilding = new ArrayList<Room>();
        for(Room room : roomList)
        {
            if(room.getBuildingName().equalsIgnoreCase(buildingName))
            {
                roomListByBuilding.add(room);
            }
        }
        return roomListByBuilding;
    }

    public ArrayList<Room> getAvailableRooms()
    {
        ArrayList<Room> roomList = this.database.getRoomList();
        ArrayList<Room> availableRoomsList = new ArrayList<Room>();
        for(Room room : roomList)
        {
            if(room.isAvailable())
            {
                availableRoomsList.add(room);
            }
        }
        return availableRoomsList;
    }

    public Equipment getEquipmentByUUID(UUID wantedId)
    {
        ArrayList<Equipment> equipmentList = this.database.getEquipmentList();
        for (Equipment equipment : equipmentList)
        {
            if(equipment.getEquipmentId().equals(wantedId))
            {
                return equipment;
            }
        }
        throw new IllegalArgumentException("Nie ma sprzętu o takim ID!");
    }

    public void moveEquipment(UUID equipmentId, int targetRoomNumber)
    {
        Equipment targetEquipment = getEquipmentByUUID(equipmentId);
        Room targetRoom = getRoomByNumber(targetRoomNumber);
        targetEquipment.setRoomIdOwning(targetRoom.getRoomId());
    }

    public Set<String> getAllBuildings()
    {
        Set<String> buildingList = new HashSet<>();
        ArrayList<Room> roomList = this.database.getRoomList();
        for(Room room : roomList)
        {
            buildingList.add(room.getBuildingName());
        }
        return buildingList;

    }

    public void deactivateRoom(int roomNumber)
    {
        ArrayList<Room> roomList = this.database.getRoomList();
        for(Room room : roomList)
        {
            if(room.getRoomNumber() == roomNumber)
            {
                room.setActive(false);
                return;
            }
        }
        throw new IllegalArgumentException("Brak sali o takim numerze!");
    }


}
