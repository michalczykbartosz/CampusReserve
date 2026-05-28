package campusreserve.users;

import campusreserve.model.Role;
import campusreserve.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {
    @Test
    void registerStudent_ValidData_AddsUser() {
        // arrange
        UserManager um = new UserManager();
        // act
        um.registerStudent("A", "B", "a@b.pl", "pass", "S1");
        // assert
        assertEquals(1, um.getAllUsers().size());
    }

    @Test
    void registerPracownik_ValidData_AddsUser() {
        // arrange
        UserManager um = new UserManager();
        // act
        um.registerPracownik("A", "B", "a@b.pl", "pass", "IT");
        // assert
        assertEquals(1, um.getAllUsers().size());
    }

    @Test
    void registerAdmin_ValidData_AddsUser() {
        // arrange
        UserManager um = new UserManager();
        // act
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        // assert
        assertEquals(1, um.getAllUsers().size());
    }

    @Test
    void registerUser_DuplicateEmail_Throws() {
        // arrange
        UserManager um = new UserManager();
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        // act
        // assert
        assertThrows(IllegalArgumentException.class, () -> um.registerStudent("C", "D", "a@b.pl", "pass", "S1"));
    }

    @Test
    void login_ValidCredentials_ReturnsUser() {
        // arrange
        UserManager um = new UserManager();
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        // act
        User user = um.login("a@b.pl", "pass");
        // assert
        assertNotNull(user);
    }

    @Test
    void login_InvalidCredentials_ReturnsNull() {
        // arrange
        UserManager um = new UserManager();
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        // act
        User user = um.login("a@b.pl", "wrong");
        // assert
        assertNull(user);
    }

    @Test
    void deactivateUser_ExistingUser_SetsInactive() {
        // arrange
        UserManager um = new UserManager();
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        User user = um.getAllUsers().get(0);
        // act
        um.deactivateUser(user.getUserId());
        // assert
        assertFalse(user.isActive());
    }

    @Test
    void activateUser_InactiveUser_SetsActive() {
        // arrange
        UserManager um = new UserManager();
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        User user = um.getAllUsers().get(0);
        um.deactivateUser(user.getUserId());
        // act
        um.activateUser(user.getUserId());
        // assert
        assertTrue(user.isActive());
    }

    @Test
    void getUserByUUID_UserExists_ReturnsUser() {
        // arrange
        UserManager um = new UserManager();
        um.registerAdmin("A", "B", "a@b.pl", "pass");
        User user = um.getAllUsers().get(0);
        // act
        User found = um.getUserByUUID(user.getUserId());
        // assert
        assertEquals(user.getUserId(), found.getUserId());
    }

    @Test
    void registerStudentFromFile_ValidData_SetsRole() {
        // arrange
        UserManager um = new UserManager();
        // act
        um.registerStudentFromFile("A", "B", "a@b.pl", "hash", "S1");
        // assert
        assertEquals(Role.STUDENT, um.getAllUsers().get(0).getRole());
    }
}

