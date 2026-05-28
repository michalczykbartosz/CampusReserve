package campusreserve.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PracownikTest {
    @Test
    void getRole_PracownikCreated_ReturnsRole() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        Role role = p.getRole();
        // assert
        assertEquals(Role.PRACOWNIK, role);
    }

    @Test
    void getDepartment_PracownikCreated_ReturnsDepartment() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        String dept = p.getDepartment();
        // assert
        assertEquals("IT", dept);
    }

    @Test
    void getEmail_PracownikCreated_ReturnsEmail() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        String email = p.getEmail();
        // assert
        assertEquals("a@b.pl", email);
    }

    @Test
    void getFirstName_PracownikCreated_ReturnsFirstName() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        String name = p.getFirstName();
        // assert
        assertEquals("A", name);
    }

    @Test
    void isActive_PracownikCreated_TrueByDefault() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        boolean active = p.isActive();
        // assert
        assertTrue(active);
    }

    @Test
    void setDepartment_PracownikUpdated_DepartmentChanges() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        p.setDepartment("HR");
        // assert
        assertEquals("HR", p.getDepartment());
    }

    @Test
    void toString_PracownikCreated_ContainsRole() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        String value = p.toString();
        // assert
        assertTrue(value.contains("PRACOWNIK"));
    }

    @Test
    void toString_PracownikCreated_ContainsEmail() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        String value = p.toString();
        // assert
        assertTrue(value.contains("a@b.pl"));
    }

    @Test
    void setActive_PracownikUpdated_BecomesInactive() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        p.setActive(false);
        // assert
        assertFalse(p.isActive());
    }

    @Test
    void setEmail_PracownikUpdated_EmailChanges() {
        // arrange
        Pracownik p = new Pracownik("A", "B", "a@b.pl", "hash", "IT");
        // act
        p.setEmail("new@b.pl");
        // assert
        assertEquals("new@b.pl", p.getEmail());
    }
}

