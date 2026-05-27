# CampusReserve

Aplikacja Java do rezerwacji sal i zasobów uczelni.

## Uruchomienie

### GUI (domyślnie)
```powershell
Set-Location 'C:\Users\bart3\Desktop\JAVA\CampusReserve'
$files = Get-ChildItem -Path 'src' -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
New-Item -ItemType Directory -Force -Path 'out' | Out-Null
javac -d out $files
java -cp out campusreserve.Main
```

### Tryb konsolowy
```powershell
java -cp out campusreserve.Main --console
```

## Dane testowe

Jeśli plik `users.txt` nie istnieje, aplikacja tworzy domyślne konto administratora:

- email: `admin@campus.pl`
- hasło: `admin123`

## Uwagi

- GUI korzysta z istniejącej logiki `UserManager`, `ResourceManager` i `Schedule`.
- Aplikacja zapisuje użytkowników do pliku `users.txt` przy zamknięciu okna lub wylogowaniu w trybie konsolowym.

