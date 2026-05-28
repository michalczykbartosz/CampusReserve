# CampusReserve

Aplikacja Java do rezerwacji sal i zasobów uczelni.

## Uruchomienie

### GUI (domyślnie)
```powershell
Set-Location 'C:\Users\bart3\Desktop\JAVA\CampusReserve'
mvn -DskipTests compile
java -cp target/classes campusreserve.Main
```

### Tryb konsolowy
```powershell
java -cp target/classes campusreserve.Main --console
```

## Dane testowe

Jeśli plik `users.txt` nie istnieje, aplikacja tworzy domyślne konto administratora:

- email: `admin@campus.pl`
- hasło: `admin123`

## Uwagi

- GUI korzysta z istniejącej logiki `UserManager`, `ResourceManager` i `Schedule`.
- Aplikacja zapisuje użytkowników do pliku `users.txt` przy zamknięciu okna lub wylogowaniu w trybie konsolowym.

## Testy (JUnit 5)

```powershell
Set-Location 'C:\Users\bart3\Desktop\JAVA\CampusReserve'
mvn test
```
