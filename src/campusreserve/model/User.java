package campusreserve.model;

import java.util.UUID;

// abstrakcyjna klasa bazowa uzytkownika
// tworz zawsze konkretny typ: student, pracownik, admin 
public abstract class User
{
    private UUID userId = UUID.randomUUID();
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash; // przechowujemy hash hasla
    private Role role;
    private boolean isActive; // false = konto dezaktywowane przez admina

    public User(String firstName, String lastName, String email, String passwordHash, Role role)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = true;
    }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString()
    {
        return firstName + " " + lastName + " (" + email + ") [" + role + "]";
    }
}