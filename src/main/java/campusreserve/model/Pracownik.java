package campusreserve.model;

// pracownik moze rezerwowac sale i zarzadzac swoimi rezerwacjami
public class Pracownik extends User
{
    private String department; // wydzial/katedra pracownika

    public Pracownik(String firstName, String lastName, String email, String passwordHash, String department)
    {
        super(firstName, lastName, email, passwordHash, Role.PRACOWNIK);
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}