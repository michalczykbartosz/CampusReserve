package campusreserve.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    @Test
    void getRole_AdminCreated_ReturnsAdmin() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        Role role = admin.getRole();
        // assert
        assertEquals(Role.ADMIN, role);
    }

    @Test
    void isActive_AdminCreated_TrueByDefault() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        boolean active = admin.isActive();
        // assert
        assertTrue(active);
    }

    @Test
    void getFirstName_AdminCreated_ReturnsFirstName() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        String name = admin.getFirstName();
        // assert
        assertEquals("A", name);
    }

    @Test
    void getLastName_AdminCreated_ReturnsLastName() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        String name = admin.getLastName();
        // assert
        assertEquals("B", name);
    }

    @Test
    void getEmail_AdminCreated_ReturnsEmail() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        String email = admin.getEmail();
        // assert
        assertEquals("a@b.pl", email);
    }

    @Test
    void getPasswordHash_AdminCreated_ReturnsHash() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        String hash = admin.getPasswordHash();
        // assert
        assertEquals("hash", hash);
    }

    @Test
    void toString_AdminCreated_ContainsRole() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        String value = admin.toString();
        // assert
        assertTrue(value.contains("ADMIN"));
    }

    @Test
    void toString_AdminCreated_ContainsEmail() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        String value = admin.toString();
        // assert
        assertTrue(value.contains("a@b.pl"));
    }

    @Test
    void setActive_AdminUpdated_BecomesInactive() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        admin.setActive(false);
        // assert
        assertFalse(admin.isActive());
    }

    @Test
    void setEmail_AdminUpdated_EmailChanged() {
        // arrange
        Admin admin = new Admin("A", "B", "a@b.pl", "hash");
        // act
        admin.setEmail("new@b.pl");
        // assert
        assertEquals("new@b.pl", admin.getEmail());
    }
}

