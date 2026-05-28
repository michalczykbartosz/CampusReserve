package campusreserve.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    void values_StateUnderTest_CountIsThree() {
        // arrange
        Role[] values = Role.values();
        // act
        int count = values.length;
        // assert
        assertEquals(3, count);
    }

    @Test
    void valueOf_AdminString_ReturnsAdmin() {
        // arrange
        // act
        Role role = Role.valueOf("ADMIN");
        // assert
        assertEquals(Role.ADMIN, role);
    }

    @Test
    void valueOf_StudentString_ReturnsStudent() {
        // arrange
        // act
        Role role = Role.valueOf("STUDENT");
        // assert
        assertEquals(Role.STUDENT, role);
    }

    @Test
    void valueOf_PracownikString_ReturnsPracownik() {
        // arrange
        // act
        Role role = Role.valueOf("PRACOWNIK");
        // assert
        assertEquals(Role.PRACOWNIK, role);
    }

    @Test
    void name_AdminRole_ReturnsName() {
        // arrange
        // act
        String name = Role.ADMIN.name();
        // assert
        assertEquals("ADMIN", name);
    }

    @Test
    void ordinal_StudentLessThanAdmin_ReturnsTrue() {
        // arrange
        int student = Role.STUDENT.ordinal();
        int admin = Role.ADMIN.ordinal();
        // act
        boolean result = student < admin;
        // assert
        assertTrue(result);
    }

    @Test
    void toString_StudentRole_ReturnsName() {
        // arrange
        // act
        String value = Role.STUDENT.toString();
        // assert
        assertEquals("STUDENT", value);
    }

    @Test
    void instance_AdminRole_IsEnum() {
        // arrange
        Role role = Role.ADMIN;
        // act
        boolean isEnum = role instanceof Enum;
        // assert
        assertTrue(isEnum);
    }

    @Test
    void values_ContainsAdmin_ReturnsTrue() {
        // arrange
        boolean found = false;
        // act
        for (Role role : Role.values()) {
            if (role == Role.ADMIN) {
                found = true;
            }
        }
        // assert
        assertTrue(found);
    }

    @Test
    void values_ContainsStudent_ReturnsTrue() {
        // arrange
        boolean found = false;
        // act
        for (Role role : Role.values()) {
            if (role == Role.STUDENT) {
                found = true;
            }
        }
        // assert
        assertTrue(found);
    }
}

