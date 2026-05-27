package campusreserve;

import campusreserve.database.DataManager;
import campusreserve.database.DataStore;
import campusreserve.resources.ResourceManager;
import campusreserve.reservations.Schedule;
import campusreserve.ui.ConsoleMenu;
import campusreserve.users.UserManager;

import campusreserve.model.User;

import javax.swing.SwingUtilities;

public class Main
{
    public static void main(String[] args)
    {
        if (args != null && args.length > 0 && "--console".equalsIgnoreCase(args[0]))
        {
            startConsoleMode();
            return;
        }

        SwingUtilities.invokeLater(Main::startGuiMode);
    }

    private static void startGuiMode()
    {
        ApplicationContext context = createContext();
        try
        {
            Class<?> guiClass = Class.forName("campusreserve.ui.CampusReserveGui");
            Object gui = guiClass.getConstructor(ResourceManager.class, Schedule.class, UserManager.class, DataManager.class)
                    .newInstance(context.resourceManager, context.schedule, context.userManager, context.dataManager);
            guiClass.getMethod("show").invoke(gui);
        }
        catch (ReflectiveOperationException e)
        {
            throw new IllegalStateException("Nie udało się uruchomić GUI.", e);
        }
    }

    private static void startConsoleMode()
    {
        ApplicationContext context = createContext();
        ConsoleMenu menu = new ConsoleMenu(context.resourceManager, context.schedule, context.userManager, context.dataManager);
        menu.start();
    }

    private static ApplicationContext createContext()
    {
        DataStore dataStore = new DataStore();
        ResourceManager resourceManager = new ResourceManager(dataStore);
        Schedule schedule = new Schedule(dataStore);
        UserManager userManager = new UserManager();
        DataManager dataManager = new DataManager();

        dataManager.loadUsers(userManager);

        boolean dataChanged = false;
        if (userManager.getAllUsers().isEmpty())
        {
            userManager.registerAdmin("Admin", "System", "admin@campus.pl", "admin123");
            System.out.println("Utworzono domyślne konto admina: admin@campus.pl / admin123");
            dataChanged = true;
        }

        dataChanged |= seedDemoUserIfMissing(userManager, "Anna", "Kowalska", "anna.kowalska@campus.pl", () -> userManager.registerStudent("Anna", "Kowalska", "anna.kowalska@campus.pl", "student123", "S12345"));
        dataChanged |= seedDemoUserIfMissing(userManager, "Piotr", "Nowak", "piotr.nowak@campus.pl", () -> userManager.registerPracownik("Piotr", "Nowak", "piotr.nowak@campus.pl", "pracownik123", "Informatyka"));
        dataChanged |= seedDemoUserIfMissing(userManager, "Julia", "Wiśniewska", "julia.wisniewska@campus.pl", () -> userManager.registerStudent("Julia", "Wiśniewska", "julia.wisniewska@campus.pl", "student123", "S67890"));

        if (dataChanged)
        {
            dataManager.saveUsers(userManager.getAllUsers());
        }

        return new ApplicationContext(resourceManager, schedule, userManager, dataManager);
    }

    private static boolean seedDemoUserIfMissing(UserManager userManager, String firstName, String lastName, String email,
                                                 Runnable registration)
    {
        for (User user : userManager.getAllUsers())
        {
            if (user.getEmail().equalsIgnoreCase(email))
            {
                return false;
            }
        }

        registration.run();
        System.out.println("Dodano konto demo: " + firstName + " " + lastName + " (" + email + ")");
        return true;
    }

    private record ApplicationContext(ResourceManager resourceManager, Schedule schedule,
                                      UserManager userManager, DataManager dataManager)
    {
    }
}
