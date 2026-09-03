# DonateDuration

[![Version](https://img.shields.io/badge/version-2.0-blue.svg)](https://github.com/KusokMedi/DonDuration/releases)
[![Minecraft](https://img.shields.io/badge/minecraft-1.16.5--26.2-green.svg)](https://www.spigotmc.org/)
[![Java](https://img.shields.io/badge/java-11+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)](LICENSE)

A Spigot 1.16.5 - 26.2 plugin that displays remaining donation status time via PlaceholderAPI, using data from LuckPerms.

## ✨ New in version 2.0

- 🔧 **All critical bugs fixed** - Memory leak, thread safety, color conversion
- 🔒 **Placeholder fixed** - %donduration% can no longer be changed
- 📦 **build.sh script** - Automatic build with dependency checking
- ⚡ **Asynchronous cache cleanup** - Does not affect server performance
- 🐛 **Error handling improved** - Detailed debug messages
- ✅ **Java 11+ compatibility** - Works with all 1.16.5-26.2+ versions

More about changes: [VERSION_2.0_CHANGES.md](VERSION_2.0_CHANGES.md)

## ✨ Features

- 📊 **Display remaining donation status** via PlaceholderAPI
- 🔄 **Automatic primary group detection** from LuckPerms
- ⚡ **Caching system** to reduce load (configurable)
- 🎨 **Full display format customization**
- 🌍 **Time unit localization**
- 🐛 **Debug mode** for detailed logging
- 📈 **bStats metrics** for usage statistics
- ✅ **Automatic dependency check** on startup
- 🔧 **Configuration reload** without server restart

## 📥 Installation

1. Download the latest plugin version from [Releases](https://github.com/KusokMedi/DonDuration/releases/tag/Stable)
2. Place `DonateDuration-2.0.jar` in your server's `plugins` folder
3. Make sure dependencies are installed:
   - [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
   - [LuckPerms](https://luckperms.net/)
4. Restart the server

## ⚙️ Configuration

### config.yml
```yaml
# Placeholder name (default: donateduration)
placeholder-name: "donateduration"

# Display format
# %duration% - time value
# %letter% - time unit letter
placeholder: "%duration%%letter%"

# Time unit letters
letters:
  seconds: "sek"
  minutes: "min"
  hours: "hour"
  days: "d"
  months: "mes"
  years: "g"

# Infinity symbol (for permanent groups)
infinity-symbol: "∞"

# Cache settings
cache:
  # Enable result caching (recommended: true)
  enabled: true
  # Cache TTL in seconds (default: 60)
  ttl: 60
  # Maximum cache size (default: 1000)
  max-size: 1000

# Debug mode (detailed logging)
debug: false
```

### messages.yml
```yaml
prefix: "&8[&6DonateDuration&8]&r"
reload-success: "%prefix% &aConfig successfully reloaded!"
no-permission: "%prefix% &cYou don't have permission to use this command!"
usage: "%prefix% &eUsage: /dd reload"
```

## 🎮 Usage

### Placeholder
Use `%donateduration%` (or another name from `placeholder-name`) in any PlaceholderAPI-supported plugin.

**Examples:**
- In TAB
- In scoreboard
- In chat
- In hologram plugins

### How it works
The plugin automatically determines the remaining donation status time based on the player's **primary group** in LuckPerms:

- If primary group is temporary → shows remaining time (e.g.: `30d`, `5hour`, `15min`)
- If primary group is permanent → shows infinity symbol `∞`
- If no data available → shows `0`

**LuckPerms setup example:**
```bash
# Give temporary group for 30 days
/lp user Player parent settemp vip 30d

# Set as primary group
/lp user Player parent set vip
```

### Commands
- `/donateduration` or `/dd` - main command
- `/dd reload` - reload configuration
- `/dd help` - show command help

### Permissions
- `donateduration.reload` - permission to reload configuration
- `donateduration.admin` - access to all commands (includes reload)

## 📋 Display examples

| Remaining time | Display |
|----------------|---------|
| 365 days       | `1g`    |
| 90 days        | `3mes`  |
| 7 days         | `7d`    |
| 12 hours       | `12hour`|
| 45 minutes     | `45min` |
| 30 seconds     | `30sek` |
| Permanent group| `∞`     |
| No group       | `0`     |

## 🔧 Requirements

- **Minecraft:** 1.16.5 - 26.2
- **Server:** Spigot / Paper / Purpur / LeafMC
- **Java:** 11+
- **Dependencies:**
  - PlaceholderAPI
  - LuckPerms

## 📝 Information

- **Version:** 2.0
- **Author:** [KusokMedi](https://github.com/kusokmedi)
- **Repository:** [github.com/KusokMedi/DonDuration](https://github.com/kusokmedi/donduration)
- **Build:** Ready **.jar** files available in [Releases](https://github.com/KusokMedi/DonDuration/releases/tag/Stable)

## 🛠️ Building from source

If you want to build the plugin yourself:

```bash
git clone https://github.com/kusokmedi/donduration.git
cd donduration
./build.sh
```

Or directly via Maven:

```bash
mvn clean package
```

Ready jar file will be in `target/DonateDuration-2.0.jar`

## 📄 License

This project is licensed under MIT