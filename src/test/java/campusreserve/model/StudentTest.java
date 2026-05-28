package campusreserve.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    @Test
    void getRole_StudentCreated_ReturnsRole() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        Role role = s.getRole();
        // assert
        assertEquals(Role.STUDENT, role);
    }

    @Test
    void getAlbumNumber_StudentCreated_ReturnsAlbum() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        String album = s.getAlbumNumber();
        // assert
        assertEquals("S1", album);
    }

    @Test
    void getEmail_StudentCreated_ReturnsEmail() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        String email = s.getEmail();
        // assert
        assertEquals("a@b.pl", email);
    }

    @Test
    void getFirstName_StudentCreated_ReturnsFirstName() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        String name = s.getFirstName();
        // assert
        assertEquals("A", name);
    }

    @Test
    void isActive_StudentCreated_TrueByDefault() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        boolean active = s.isActive();
        // assert
        assertTrue(active);
    }

    @Test
    void setAlbumNumber_StudentUpdated_AlbumChanges() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        s.setAlbumNumber("S2");
        // assert
        assertEquals("S2", s.getAlbumNumber());
    }

    @Test
    void toString_StudentCreated_ContainsRole() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        String value = s.toString();
        // assert
        assertTrue(value.contains("STUDENT"));
    }

    @Test
    void toString_StudentCreated_ContainsEmail() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        String value = s.toString();
        // assert
        assertTrue(value.contains("a@b.pl"));
    }

    @Test
    void setActive_StudentUpdated_BecomesInactive() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        s.setActive(false);
        // assert
        assertFalse(s.isActive());
    }

    @Test
    void setEmail_StudentUpdated_EmailChanges() {
        // arrange
        Student s = new Student("A", "B", "a@b.pl", "hash", "S1");
        // act
        s.setEmail("new@b.pl");
        // assert
        assertEquals("new@b.pl", s.getEmail());
    }
}

