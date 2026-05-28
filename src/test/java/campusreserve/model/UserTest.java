package campusreserve.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void getUserId_UserCreated_NotNull() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        UUID id = u.getUserId();
        // assert
        assertNotNull(id);
    }

    @Test
    void getUserId_TwoUsers_DifferentIds() {
        // arrange
        User u1 = new Student("A", "B", "a@b.pl", "hash", "S1");
        User u2 = new Student("C", "D", "c@d.pl", "hash", "S2");
        // act
        UUID id1 = u1.getUserId();
        UUID id2 = u2.getUserId();
        // assert
        assertNotEquals(id1, id2);
    }

    @Test
    void setUserId_UserUpdated_IdChanges() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        UUID id = UUID.randomUUID();
        // act
        u.setUserId(id);
        // assert
        assertEquals(id, u.getUserId());
    }

    @Test
    void setFirstName_UserUpdated_FirstNameChanges() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        u.setFirstName("X");
        // assert
        assertEquals("X", u.getFirstName());
    }

    @Test
    void setLastName_UserUpdated_LastNameChanges() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        u.setLastName("Y");
        // assert
        assertEquals("Y", u.getLastName());
    }

    @Test
    void setEmail_UserUpdated_EmailChanges() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        u.setEmail("new@b.pl");
        // assert
        assertEquals("new@b.pl", u.getEmail());
    }

    @Test
    void setPasswordHash_UserUpdated_HashChanges() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        u.setPasswordHash("newhash");
        // assert
        assertEquals("newhash", u.getPasswordHash());
    }

    @Test
    void setRole_UserUpdated_RoleChanges() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        u.setRole(Role.ADMIN);
        // assert
        assertEquals(Role.ADMIN, u.getRole());
    }

    @Test
    void isActive_UserCreated_TrueByDefault() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        boolean active = u.isActive();
        // assert
        assertTrue(active);
    }

    @Test
    void toString_UserCreated_ContainsEmail() {
        // arrange
        User u = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        String value = u.toString();
        // assert
        assertTrue(value.contains("a@b.pl"));
    }
}

