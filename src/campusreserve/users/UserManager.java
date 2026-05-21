package campusreserve.users;

import campusreserve.model.User;
import campusreserve.model.Student;
import campusreserve.model.Pracownik;
import campusreserve.model.Admin;

import java.util.ArrayList;
import java.util.UUID;

// zarzadza lista uzytkownikow: rejestracja, logowanie, wyszukiwanie
public class UserManager
{
    private ArrayList<User> userList = new ArrayList<>();

    public void registerStudent(String firstName, String lastName, String email, String passwordHash, String albumNumber)
    {
        checkEmailTaken(email);
        userList.add(new Student(firstName, lastName, email, hashPassword(passwordHash), albumNumber));
    }

    public void registerPracownik(String firstName, String lastName, String email, String passwordHash, String department)
    {
        checkEmailTaken(email);
        userList.add(new Pracownik(firstName, lastName, email, hashPassword(passwordHash), department));
    }

    public void registerAdmin(String firstName, String lastName, String email, String passwordHash)
    {
        checkEmailTaken(email);
        userList.add(new Admin(firstName, lastName, email, hashPassword(passwordHash)));
    }

    public void registerStudentFromFile(String firstName, String lastName, String email, String passwordHash, String albumNumber)
    {
        checkEmailTaken(email);
        userList.add(new Student(firstName, lastName, email, passwordHash, albumNumber));
    }

    public void registerPracownikFromFile(String firstName, String lastName, String email, String passwordHash, String department)
    {
        checkEmailTaken(email);
        userList.add(new Pracownik(firstName, lastName, email, passwordHash, department));
    }

    public void registerAdminFromFile(String firstName, String lastName, String email, String passwordHash)
    {
        checkEmailTaken(email);
        userList.add(new Admin(firstName, lastName, email, passwordHash));
    }
    
    // loguje uzytkownika - sprawdza email i hash hasla
    // zwraca obiekt uzytkownika lub null jesli dane zle
    public User login(String email, String passwordHash)
    {
        for (User user : userList)
        {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPasswordHash().equals(hashPassword(passwordHash)))
            {
                if (!user.isActive())
                {
                    throw new IllegalArgumentException("Konto jest dezaktywowane!");
                }
                return user;
            }
        }
        return null;
    }

    public User getUserByUUID(UUID userId)
    {
        for (User user : userList)
        {
            if (user.getUserId().equals(userId))
            {
                return user;
            }
        }
        throw new IllegalArgumentException("Nie ma użytkownika o takim ID!");
    }

    public ArrayList<User> getAllUsers()
    {
        return userList;
    }

    public void deactivateUser(UUID userId)
    {
        getUserByUUID(userId).setActive(false);
    }

    private String hashPassword(String password)
    {
        try
        {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Błąd hashowania hasła!");
        }
    }

    // email musi byc unikalny w systemie
    private void checkEmailTaken(String email)
    {
        for (User user : userList)
        {
            if (user.getEmail().equalsIgnoreCase(email))
            {
                throw new IllegalArgumentException("Użytkownik z takim emailem już istnieje!");
            }
        }
    }
}