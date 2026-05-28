package campusreserve.ui;

import campusreserve.model.User;
import campusreserve.model.Role;
import campusreserve.model.Room;
import campusreserve.model.Reservation;
import campusreserve.resources.ResourceManager;
import campusreserve.reservations.Schedule;
import campusreserve.users.UserManager;
import campusreserve.database.DataManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

// obsluguje interfejs konsolowy - menu, logowanie i pobieranie danych od uzytkownika
public class ConsoleMenu
{
    private Scanner scanner;
    private ResourceManager resourceManager;
    private Schedule schedule;
    private UserManager userManager;
    private User loggedInUser; // aktualnie zalogowany uzytkownik (null jesli nikt)
    private DataManager dataManager;

    // format daty uzywany w ca;ym menu
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConsoleMenu(ResourceManager resourceManager, Schedule schedule, UserManager userManager, DataManager dataManager)
    {
        this.resourceManager = resourceManager;
        this.schedule = schedule;
        this.userManager = userManager;
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }

    // uruchamia aplikacje
    public void start()
    {
        System.out.println("=== CampusReserve ===");
        startMenu();
    }

    // menu startowe - przed zalogowaniem
    private void startMenu()
    {
        boolean running = true;
        while (running)
        {
            System.out.println("\n--- Menu Startowe ---");
            System.out.println("1. Zaloguj się");
            System.out.println("2. Zarejestruj się");
            System.out.println("0. Wyjście");
            System.out.print("Wybierz opcję: ");

            String input = scanner.nextLine().trim();

            switch (input)
            {
                case "1" -> handleLogin();
                case "2" -> handleRegister();
                case "0" ->
                {
                    dataManager.saveUsers(userManager.getAllUsers());
                    System.out.println("Do widzenia!");
                    running = false;
                }
                default -> System.out.println("Nieprawidłowa opcja, spróbuj ponownie.");
            }
        }
    }

    private void handleLogin()
    {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();

        try
        {
            User user = userManager.login(email, password);
            if (user == null)
            {
                System.out.println("Nieprawidłowy email lub hasło.");
                return;
            }
            loggedInUser = user;
            System.out.println("Zalogowano jako: " + loggedInUser);
            mainMenu();
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Błąd logowania: " + e.getMessage());
        }
    }

    private void handleRegister()
    {
        System.out.println("\n--- Rejestracja ---");
        System.out.println("1. Student");
        System.out.println("2. Pracownik");
        System.out.print("Wybierz typ konta: ");
        String choice = scanner.nextLine().trim();

        System.out.print("Imię: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Nazwisko: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();

        try
        {
            switch (choice)
            {
                case "1" ->
                {
                    System.out.print("Numer albumu: ");
                    String albumNumber = scanner.nextLine().trim();
                    userManager.registerStudent(firstName, lastName, email, password, albumNumber);
                }
                case "2" ->
                {
                    System.out.print("Wydział/Katedra: ");
                    String department = scanner.nextLine().trim();
                    userManager.registerPracownik(firstName, lastName, email, password, department);
                }
                default ->
                {
                    System.out.println("Nieprawidłowy wybór.");
                    return;
                }
            }
            System.out.println("Zarejestrowano pomyślnie! Możesz się teraz zalogować.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Błąd rejestracji: " + e.getMessage());
        }
    }

    // kieruje do odpowiedniego menu zaleznie od roli zalogowanego uzytkownika
    private void mainMenu()
    {
        if (loggedInUser.getRole() == Role.ADMIN)
        {
            adminMenu();
        }
        else
        {
            userMenu();
        }
    }

    // menu dla studenta i pracownika
    private void userMenu()
    {
        boolean running = true;
        while (running)
        {
            System.out.println("\n--- Menu [" + loggedInUser.getRole() + "] ---");
            System.out.println("1. Wyświetl dostępne sale");
            System.out.println("2. Wyświetl sale według budynku");
            System.out.println("3. Złóż rezerwację sali");
            System.out.println("4. Anuluj rezerwację");
            System.out.println("5. Harmonogram sali");
            System.out.println("0. Wyloguj");
            System.out.print("Wybierz opcję: ");

            String input = scanner.nextLine().trim();

            switch (input)
            {
                case "1" -> showAvailableRooms();
                case "2" -> showRoomsByBuilding();
                case "3" -> handleMakeReservation();
                case "4" -> handleCancelReservation();
                case "5" -> handleShowSchedule();
                case "0" ->
                {
                    loggedInUser = null;
                    System.out.println("Wylogowano.");
                    running = false;
                }
                default -> System.out.println("Nieprawidłowa opcja, spróbuj ponownie.");
            }
        }
    }

    // menu dla admina z dodatkowymi opcjami zarzadzania
    private void adminMenu()
    {
        boolean running = true;
        while (running)
        {
            System.out.println("\n--- Menu Admina ---");
            System.out.println("1. Wyświetl dostępne sale");
            System.out.println("2. Wyświetl sale według budynku");
            System.out.println("3. Złóż rezerwację sali");
            System.out.println("4. Anuluj rezerwację");
            System.out.println("5. Harmonogram sali");
            System.out.println("6. Wyświetl wszystkich użytkowników");
            System.out.println("7. Dezaktywuj użytkownika");
            System.out.println("8. Dezaktywuj salę");
            System.out.println("0. Wyloguj");
            System.out.print("Wybierz opcję: ");

            String input = scanner.nextLine().trim();

            switch (input)
            {
                case "1" -> showAvailableRooms();
                case "2" -> showRoomsByBuilding();
                case "3" -> handleMakeReservation();
                case "4" -> handleCancelReservation();
                case "5" -> handleShowSchedule();
                case "6" -> showAllUsers();
                case "7" -> handleDeactivateUser();
                case "8" -> handleDeactivateRoom();
                case "0" ->
                {
                    loggedInUser = null;
                    System.out.println("Wylogowano.");
                    running = false;
                }
                default -> System.out.println("Nieprawidłowa opcja, spróbuj ponownie.");
            }
        }
    }

    private void showAvailableRooms()
    {
        ArrayList<Room> rooms = resourceManager.getAvailableRooms();
        if (rooms.isEmpty())
        {
            System.out.println("Brak dostępnych sal.");
            return;
        }
        System.out.println("\n--- Dostępne sale ---");
        for (Room room : rooms)
        {
            System.out.println("Nr " + room.getRoomNumber()
                    + " | " + room.getRoomName()
                    + " | " + room.getBuildingName()
                    + " | Pojemność: " + room.getCapacity()
                    + " | Typ: " + room.getRoomType());
        }
    }

    private void showRoomsByBuilding()
    {
        System.out.print("Podaj nazwę budynku: ");
        String buildingName = scanner.nextLine().trim();
        ArrayList<Room> rooms = resourceManager.getRoomsByBuilding(buildingName);
        if (rooms.isEmpty())
        {
            System.out.println("Brak sal w budynku: " + buildingName);
            return;
        }
        System.out.println("\n--- Sale w budynku " + buildingName + " ---");
        for (Room room : rooms)
        {
            System.out.println("Nr " + room.getRoomNumber()
                    + " | " + room.getRoomName()
                    + " | Pojemność: " + room.getCapacity()
                    + " | Dostępna: " + (room.isAvailable() ? "Tak" : "Nie"));
        }
    }

    private void handleMakeReservation()
    {
        showAvailableRooms();
        System.out.print("Podaj numer sali do rezerwacji: ");
        try
        {
            int roomNumber = Integer.parseInt(scanner.nextLine().trim());
            Room room = resourceManager.getRoomByNumber(roomNumber);

            System.out.println("Podaj datę i godzinę w formacie: yyyy-MM-dd HH:mm");
            System.out.print("Początek: ");
            LocalDateTime start = LocalDateTime.parse(scanner.nextLine().trim(), DATE_FORMAT);
            System.out.print("Koniec: ");
            LocalDateTime end = LocalDateTime.parse(scanner.nextLine().trim(), DATE_FORMAT);

            Reservation reservation = new Reservation(loggedInUser.getUserId(), room.getRoomId(), start, end);
            schedule.addReservation(reservation);
            System.out.println("Rezerwacja złożona pomyślnie! ID: " + reservation.getReservationId());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Nieprawidłowy numer sali.");
        }
        catch (DateTimeParseException e)
        {
            System.out.println("Nieprawidłowy format daty. Użyj: yyyy-MM-dd HH:mm");
        }
        catch (IllegalArgumentException | IllegalStateException e)
        {
            System.out.println("Błąd rezerwacji: " + e.getMessage());
        }
    }

    private void handleCancelReservation()
    {
        System.out.print("Podaj ID rezerwacji do anulowania: ");
        try
        {
            java.util.UUID reservationId = java.util.UUID.fromString(scanner.nextLine().trim());
            schedule.cancelReservation(reservationId);
            System.out.println("Rezerwacja anulowana.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    private void handleShowSchedule()
    {
        showAvailableRooms();
        System.out.print("Podaj numer sali: ");
        try
        {
            int roomNumber = Integer.parseInt(scanner.nextLine().trim());
            Room room = resourceManager.getRoomByNumber(roomNumber);
            ArrayList<Reservation> reservations = schedule.getScheduleForResource(room.getRoomId());

            if (reservations.isEmpty())
            {
                System.out.println("Brak rezerwacji dla tej sali.");
                return;
            }
            System.out.println("\n--- Harmonogram sali nr " + roomNumber + " ---");
            for (Reservation r : reservations)
            {
                System.out.println("ID: " + r.getReservationId()
                        + " | Od: " + r.getStartTime().format(DATE_FORMAT)
                        + " | Do: " + r.getEndTime().format(DATE_FORMAT));
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Nieprawidłowy numer sali.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    private void showAllUsers()
    {
        ArrayList<User> users = userManager.getAllUsers();
        if (users.isEmpty())
        {
            System.out.println("Brak użytkowników w systemie.");
            return;
        }
        System.out.println("\n--- Wszyscy użytkownicy ---");
        for (User user : users)
        {
            System.out.println(user + " | Aktywny: " + (user.isActive() ? "Tak" : "Nie"));
        }
    }

    private void handleDeactivateUser()
    {
        showAllUsers();
        System.out.print("Podaj email użytkownika do dezaktywacji: ");
        String email = scanner.nextLine().trim();
        for (User user : userManager.getAllUsers())
        {
            if (user.getEmail().equalsIgnoreCase(email))
            {
                userManager.deactivateUser(user.getUserId());
                System.out.println("Dezaktywowano: " + user);
                return;
            }
        }
        System.out.println("Nie znaleziono użytkownika z takim emailem.");
    }

    private void handleDeactivateRoom()
    {
        System.out.print("Podaj numer sali do dezaktywacji: ");
        try
        {
            int roomNumber = Integer.parseInt(scanner.nextLine().trim());
            resourceManager.deactivateRoom(roomNumber);
            System.out.println("Sala nr " + roomNumber + " została dezaktywowana.");
        }
        catch (NumberFormatException e)
        {
            System.out.println("Nieprawidłowy numer sali.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}