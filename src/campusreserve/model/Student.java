package campusreserve.model;

// student moze przegladac sale, i skladac rezerwacje
public class Student extends User
{
    private String albumNumber; // numer albumu studenta

    public Student(String firstName, String lastName, String email, String passwordHash, String albumNumber)
    {
        super(firstName, lastName, email, passwordHash, Role.STUDENT);
        this.albumNumber = albumNumber;
    }

    public String getAlbumNumber() { return albumNumber; }
    public void setAlbumNumber(String albumNumber) { this.albumNumber = albumNumber; }
}