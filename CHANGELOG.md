# DonateDuration 2.0 - Izmjene i Ispravke

## 🔧 Glavne Ispravke Bagova

### 1. ✅ Memory Leak u Kešu

**Problem:** Istekle cache unose nikada nisu obrisane iz memorije

**Rješenje:** 
- Dodan asinkroni scheduler `startCacheCleanupTask()` koji periodički čisti istekle unose
- Metoda `cleanupExpiredCache()` automatski se pokreće svakih 5 minuta (konfigurabilno)
- Dodana validacija maksimalnog veličine kešu

### 2. ✅ Thread Safety - LuckPerms API

**Problem:** `LuckPermsProvider.get()` se pozivao svaki put - potencijalno skupo

**Rješenje:**
- LuckPerms API se sada keširuje pri inicijalizaciji plugina
- Prosleđuje se kao parametar `DurationPlaceholder` konstruktoru
- Dodane su provjerke za null vrijednosti

### 3. ✅ Konverzija Boja (&-Kodovi)

**Problem:** Boja u config vrijednostima (npr. `&7∞`) se nisu konvertovale

**Rješenje:**
- Kreirana `ChatColorUtil.java` klasa sa `translateColorCodes()` metodom
- Svi config stringovi se sada konvertuju prije prikazivanja
- Kompatibilna sa verzijama 1.16+

### 4. ✅ Fiksni Placeholder ID

**Problem:** Mogućnost promjene placeholder imena iz config-a

**Rješenje:**
- Placeholder je sada **fiksiran na `%donduration%`**
- `getIdentifier()` metoda vraća konstantu `PLACEHOLDER_ID = "donduration"`
- Config vrijedност `placeholder-name` se sada ignoriše

### 5. ✅ Validacija Konfiguracije

**Problem:** Nedostajuće config vrijednosti nisu dobile default vrijedности

**Rješenje:**
- Dodana metoda `validateConfig()` koja provjerava sve ključне vrijedности
- Automatski se postavljaju default vrijedности ako nedostaju
- Primjer: `letters.seconds`, `letters.minutes`, itd.

### 6. ✅ Poboljšan Error Handling

- Detaljnije debug poruke sa `[LP]`, `[Cache HIT]`, `[Cache MISS]` prefixima
- Bolji error handling u `getDonateTime()` metodi
- Exception stack trace se ispisuje u debug modu
- Informativnije poruke комandi

## 🚀 Nove Funkcionalnosti

### 1. ChatColorUtil Klasa

```java
ChatColorUtil.translateColorCodes(message); // Konvertuje & u §
ChatColorUtil.stripColorCodes(message);     // Uklanja sve boje
```

### 2. Poboljšana Komanda `/dd`

- `/dd reload` - Перезагрузить конфигурацию
- `/dd help` - Prikaži dostupne komande
- Detaljnija pomoćна poruka

### 3. Novi Config Parametri

```yaml
cache:
  cleanup-interval: 300  # Intervalli čišćenja kešu u sekundama
```

## 📋 Kompatibilност

- **Java:** 11+ (umjesto 21)
- **Minecraft:** 1.16.5 - 26.2+ (testirana компатибилност)
- **API verzija:** 1.20 (umjesto 1.16)
- **Maven:** 3.9.16+

## 📦 Build Sistem

### Automatska Sborka sa `build.sh`

```bash
./build.sh
```

Skripт će:
1. Provjeriti Java verziju (treba 11+)
2. Provjeriti Maven instalaciju
3. Preuzeti sve Maven ovisnosti
4. Kompajlirati i testirati kod
5. Kreirati JAR datoteku
6. Provjeriti integritet JAR-a

### Rezultat

```
target/DonateDuration-2.0.jar (44KB)
```

## 🔄 Migracijom sa Verzije 1.x

### Komapatibilност Config-a

Stari `config.yml` će raditi sa verzijom 2.0, ali се preporučuje:

1. Preimenovanje `placeholder-name` vrijedности (sada се ignoriše)
2. Dodavanje `cache.cleanup-interval` ako trebate custom interval

### Nije potrebna Migracija

- Config format ostaje isti
- Messages format ostaje isti
- LuckPerms integracija ostaje ista

## 📝 Tehnička Poboljšanja

### Code Refactoring

- Bolje strukturirani kod sa jasnim odgovornostima
- Dodani Java dokumentacijski komentari
- Konstante sa govorivm imenima

### Performance

- Brže cache lookup-e (debug mode je off по defaultу)
- Asinkrona cache cleanup (ne blokira main thread)
- Keširani LuckPerms API pozivi

### Stabilност

- Null pointer exception zaštita
- Bolji exception handling
- Validacija svih config vrijedности

## 🐛 Ostali Bugfixes

- Ispravljeno: Null vrijedности у `placeholder` tokom reload-a
- Ispravljeno: Greške код offline igrača
- Ispravljenо: Неispравна prikazivanja за male vremenske periode

## 📚 Resursi

- **GitHub:** https://github.com/kusokmedi/donduration
- **Sborka:** `./build.sh`или `mvn clean package`
- **JAR datoteka:** `target/DonateDuration-2.0.jar`

---

**Verzija:** 2.0  
**Datum:** 2026-09-03  
**Autor:** KusokMedi