package campusreserve;

import campusreserve.database.DataStore;
import campusreserve.database.DataManager;
import campusreserve.resources.ResourceManager;
import campusreserve.reservations.Schedule;
import campusreserve.users.UserManager;
import campusreserve.ui.ConsoleMenu;

public class Main
{
    public static void main(String[] args)
    {
        DataStore dataStore = new DataStore();
        ResourceManager resourceManager = new ResourceManager(dataStore);
        Schedule schedule = new Schedule(dataStore);
        UserManager userManager = new UserManager();
        DataManager dataManager = new DataManager();

        dataManager.loadUsers(userManager);

        if (userManager.getAllUsers().isEmpty())
        {
            userManager.registerAdmin("Admin", "System", "admin@campus.pl", "admin123");
            System.out.println("Utworzono domyślne konto admina: admin@campus.pl / admin123");
        }

        ConsoleMenu menu = new ConsoleMenu(resourceManager, schedule, userManager, dataManager);
        menu.start();
    }
}