package campusreserve.database;

import campusreserve.model.Role;
import campusreserve.model.User;
import campusreserve.users.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DataManagerTest {
    private static final Path USERS_FILE = Path.of("users.txt");
    private String backupContent;
    private boolean hadFile;

    @BeforeEach
    void backupUsersFile_StateUnderTest_ExpectedBehavior() throws IOException {
        // arrange
        hadFile = Files.exists(USERS_FILE);
        backupContent = hadFile ? Files.readString(USERS_FILE) : null;
        // act
        if (hadFile) {
            Files.delete(USERS_FILE);
        }
        // assert
    }

    @AfterEach
    void restoreUsersFile_StateUnderTest_ExpectedBehavior() throws IOException {
        // arrange
        // act
        if (hadFile) {
            Files.writeString(USERS_FILE, backupContent == null ? "" : backupContent);
        } else if (Files.exists(USERS_FILE)) {
            Files.delete(USERS_FILE);
        }
        // assert
    }

    @Test
    void saveUsers_EmptyList_CreatesFile() {
        // arrange
        DataManager dm = new DataManager();
        // act
        dm.saveUsers(new ArrayList<>());
        // assert
        assertTrue(Files.exists(USERS_FILE));
    }

    @Test
    void loadUsers_FileMissing_DoesNotThrowAndLoadsEmpty() {
        // arrange
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        // act
        assertDoesNotThrow(() -> dm.loadUsers(um));
        // assert
        assertTrue(um.getAllUsers().isEmpty());
    }

    @Test
    void saveUsers_ThenLoad_AdminRestored() {
        // arrange
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        um.registerAdmin("Admin", "Test", "admin@test.pl", "pass");
        dm.saveUsers(um.getAllUsers());

        UserManager um2 = new UserManager();
        // act
        dm.loadUsers(um2);
        // assert
        assertEquals(1, um2.getAllUsers().size());
        assertEquals(Role.ADMIN, um2.getAllUsers().get(0).getRole());
    }

    @Test
    void saveUsers_ThenLoad_StudentRestored() {
        // arrange
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        um.registerStudent("Jan", "Kowal", "jan@test.pl", "pass", "S111");
        dm.saveUsers(um.getAllUsers());

        UserManager um2 = new UserManager();
        // act
        dm.loadUsers(um2);
        // assert
        assertEquals(1, um2.getAllUsers().size());
        assertEquals(Role.STUDENT, um2.getAllUsers().get(0).getRole());
    }

    @Test
    void saveUsers_ThenLoad_PracownikRestored() {
        // arrange
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        um.registerPracownik("Ola", "Nowak", "ola@test.pl", "pass", "IT");
        dm.saveUsers(um.getAllUsers());

        UserManager um2 = new UserManager();
        // act
        dm.loadUsers(um2);
        // assert
        assertEquals(1, um2.getAllUsers().size());
        assertEquals(Role.PRACOWNIK, um2.getAllUsers().get(0).getRole());
    }

    @Test
    void loadUsers_UserInactive_StatusRestored() throws IOException {
        // arrange
        String line = "STUDENT|A|B|x@test.pl|hash|false|S123";
        Files.writeString(USERS_FILE, line);
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        // act
        dm.loadUsers(um);
        // assert
        assertFalse(um.getAllUsers().get(0).isActive());
    }

    @Test
    void loadUsers_BadLine_Ignored() throws IOException {
        // arrange
        Files.writeString(USERS_FILE, "BADLINE");
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        // act
        dm.loadUsers(um);
        // assert
        assertEquals(0, um.getAllUsers().size());
    }

    @Test
    void saveUsers_InactiveUser_FileWritten() {
        // arrange
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        um.getAllUsers().get(0).setActive(false);
        // act
        dm.saveUsers(um.getAllUsers());
        // assert
        assertTrue(Files.exists(USERS_FILE));
    }

    @Test
    void saveUsers_MultipleUsers_FileWritten() {
        // arrange
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        um.registerStudent("C", "D", "c@d.pl", "pass", "S1");
        // act
        dm.saveUsers(um.getAllUsers());
        // assert
        assertTrue(Files.exists(USERS_FILE));
    }

    @Test
    void loadUsers_UserLine_EmailRestored() throws IOException {
        // arrange
        String line = "ADMIN|A|B|mail@test.pl|hash|true|";
        Files.writeString(USERS_FILE, line);
        DataManager dm = new DataManager();
        UserManager um = new UserManager();
        // act
        dm.loadUsers(um);
        User user = um.getAllUsers().get(0);
        // assert
        assertEquals("mail@test.pl", user.getEmail());
    }
}

