package campusreserve.model;
import java.util.UUID;


public class Room
{

    public UUID getRoomId()
    {
        return roomId;
    }

    public void setRoomId(UUID roomId)
    {
        this.roomId = roomId;
    }

    private UUID roomId = UUID.randomUUID();

    public int getRoomNumber()
    {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber)
    {
        this.roomNumber = roomNumber;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public String getBuildingName()
    {
        return buildingName;
    }

    public void setBuildingName(String buildingName)
    {
        this.buildingName = buildingName;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public String getRoomType()
    {
        return roomType;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    public boolean isAvailable()
    {
        return isAvailable;
    }

    public void setAvailable(boolean available)
    {
        isAvailable = available;
    }

    private int roomNumber;
    private String roomName;
    private String buildingName;
    private int capacity;
    private String roomType;
    private boolean isActive; //e.g. under construction
    private boolean isAvailable; //can be booked

    public Room(int roomNumber, String roomName, String buildingName, int capacity, String roomType, boolean isActive, boolean isAvailable)
    {
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.buildingName = buildingName;
        this.capacity = capacity;
        this.roomType = roomType;
        this.isActive = isActive;
        this.isAvailable = isAvailable;
    }




}