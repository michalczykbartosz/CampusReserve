package campusreserve.model;
import java.util.UUID;


public class Equipment
{
    private UUID equipmentId = UUID.randomUUID();
    private String equipmentName;
    private int equipmentQuantity;
    private String equipmentDescription;
    private UUID roomIdOwning;
    private boolean isBroken;

    public Equipment(String equipmentDescription, String equipmentName, int equipmentQuantity, boolean isBroken, UUID roomIdOwning)
    {
        this.equipmentDescription = equipmentDescription;
        this.equipmentName = equipmentName;
        this.equipmentQuantity = equipmentQuantity;
        this.isBroken = isBroken;
        this.roomIdOwning = roomIdOwning;
    }


    public boolean isBroken()
    {
        return isBroken;
    }

    public void setBroken(boolean broken)
    {
        isBroken = broken;
    }

    public String getEquipmentDescription()
    {
        return equipmentDescription;
    }

    public void setEquipmentDescription(String equipmentDescription)
    {
        this.equipmentDescription = equipmentDescription;
    }

    public UUID getEquipmentId()
    {
        return equipmentId;
    }

    public void setEquipmentId(UUID equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName()
    {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName)
    {
        this.equipmentName = equipmentName;
    }

    public int getEquipmentQuantity()
    {
        return equipmentQuantity;
    }

    public void setEquipmentQuantity(int equipmentQuantity)
    {
        this.equipmentQuantity = equipmentQuantity;
    }

    public UUID getRoomIdOwning()
    {
        return roomIdOwning;
    }

    public void setRoomIdOwning(UUID roomIdOwning)
    {
        this.roomIdOwning = roomIdOwning;
    }



}