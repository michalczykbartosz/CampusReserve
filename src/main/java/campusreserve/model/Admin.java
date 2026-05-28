package campusreserve.model;

// admin ma pelny dostep - zarzadza salami, sprzetem i uzytkownikami
public class Admin extends User
{
    public Admin(String firstName, String lastName, String email, String passwordHash)
    {
        super(firstName, lastName, email, passwordHash, Role.ADMIN);
    }
}