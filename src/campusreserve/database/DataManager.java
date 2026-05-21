package campusreserve.database;

import campusreserve.model.*;
import campusreserve.users.UserManager;

import java.io.*;
import java.util.ArrayList;

// odpowiada za zapis i odczyt stanu uzytkownikow do/z pliku tekstowego
public class DataManager
{
    private static final String USERS_FILE = "users.txt";

    // zapisuje wszystkich uzytkownikow do pliku
    public void saveUsers(ArrayList<User> users)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE)))
        {
            for (User user : users)
            {
                // format linii: ROLA|imie|nazwisko|email|haslo|aktywny|dodatkoweInfo
                String extraInfo = "";
                if (user instanceof Student)
                {
                    extraInfo = ((Student) user).getAlbumNumber();
                }
                else if (user instanceof Pracownik)
                {
                    extraInfo = ((Pracownik) user).getDepartment();
                }

                writer.write(user.getRole() + "|"
                        + user.getFirstName() + "|"
                        + user.getLastName() + "|"
                        + user.getEmail() + "|"
                        + user.getPasswordHash() + "|"
                        + user.isActive() + "|"
                        + extraInfo);
                writer.newLine();
            }
            System.out.println("Dane użytkowników zapisane do pliku.");
        }
        catch (IOException e)
        {
            System.out.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }

    // wczytuje uzytkownikow z pliku i rejestruje ich w UserManagerze
    public void loadUsers(UserManager userManager)
    {
        File file = new File(USERS_FILE);
        if (!file.exists())
        {
            System.out.println("Brak pliku z danymi - start z pustą listą użytkowników.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("\\|");
                if (parts.length < 6) continue; // pomijamy uszkodzone linie

                Role role = Role.valueOf(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                String email = parts[3];
                String password = parts[4];
                boolean isActive = Boolean.parseBoolean(parts[5]);
                String extraInfo = parts.length > 6 ? parts[6] : "";

                switch (role)
                {
                    case STUDENT -> userManager.registerStudentFromFile(firstName, lastName, email, password, extraInfo);
                    case PRACOWNIK -> userManager.registerPracownikFromFile(firstName, lastName, email, password, extraInfo);
                    case ADMIN -> userManager.registerAdminFromFile(firstName, lastName, email, password);
                }

                // przywracamy status aktywnosci
                if (!isActive)
                {
                    userManager.getAllUsers()
                            .stream()
                            .filter(u -> u.getEmail().equalsIgnoreCase(email))
                            .findFirst()
                            .ifPresent(u -> u.setActive(false));
                }
            }
            System.out.println("Dane użytkowników wczytane z pliku.");
        }
        catch (IOException e)
        {
            System.out.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }
}