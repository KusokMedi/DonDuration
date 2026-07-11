# DonateDuration

[![Version](https://img.shields.io/badge/version-1.2.1-blue.svg)](https://github.com/KusokMedi/DonDuration/releases)
[![Minecraft](https://img.shields.io/badge/minecraft-1.16.5--26.2-green.svg)](https://www.spigotmc.org/)
[![Java](https://img.shields.io/badge/java-21+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)](LICENSE)
[![bStats](https://img.shields.io/badge/bstats-metrics-brightgreen.svg)](https://bstats.org/plugin/bukkit/DonateDuration/23744)

Плагин для Spigot 1.16.5 - 26.2, который отображает оставшееся время донат-статуса игрока через PlaceholderAPI, используя данные из LuckPerms.

## ✨ Возможности

- 📊 **Отображение оставшегося времени донат-статуса** через PlaceholderAPI
- 🔄 **Автоматическое определение primary группы** из LuckPerms
- ⚡ **Система кэширования** для снижения нагрузки (настраиваемая)
- 🎨 **Полная настройка формата** отображения времени
- 🌍 **Локализация** букв единиц времени
- 🐛 **Debug режим** для детального логирования
- 📈 **bStats метрики** для статистики использования
- ✅ **Автоматическая проверка зависимостей** при запуске
- 🔧 **Перезагрузка конфигурации** без перезапуска сервера

## 📥 Установка

1. Скачайте последнюю версию плагина из [Releases](https://github.com/KusokMedi/DonDuration/releases/tag/Stable)
2. Поместите `DonateDuration-1.2.1.jar` в папку `plugins` вашего сервера
3. Убедитесь, что установлены зависимости:
   - [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
   - [LuckPerms](https://luckperms.net/)
4. Перезапустите сервер

## ⚙️ Настройка

### config.yml
```yaml
# Имя placeholder (по умолчанию: donateduration)
placeholder-name: "donateduration"

# Формат отображения
# %duration% - значение времени
# %letter% - буква единицы времени
placeholder: "%duration%%letter%"

# Буквы для единиц времени
letters:
  seconds: "сек"
  minutes: "мин"
  hours: "час"
  days: "д"
  months: "мес"
  years: "г"

# Символ бесконечности (для постоянных групп)
infinity-symbol: "∞"

# Настройки кэширования
cache:
  # Включить кэширование результатов (рекомендуется: true)
  enabled: true
  # Время жизни кэша в секундах (по умолчанию: 60)
  ttl: 60
  # Максимальный размер кэша (по умолчанию: 1000)
  max-size: 1000

# Режим отладки (подробное логирование)
debug: false
```

### messages.yml
```yaml
prefix: "&8[&6DonateDuration&8]&r"
reload-success: "%prefix% &aКонфигурация успешно перезагружена!"
no-permission: "%prefix% &cУ вас нет прав для использования этой команды!"
usage: "%prefix% &eИспользование: /dd reload"
```

## 🎮 Использование

### Placeholder
Используйте `%donateduration%` (или другое имя из `placeholder-name`) в любом плагине, поддерживающем PlaceholderAPI.

**Примеры:**
- В табе (TAB)
- В скорборде
- В чате
- В hologram плагинах

### Как работает
Плагин автоматически определяет оставшееся время донат-статуса на основе **primary группы** игрока в LuckPerms:

- Если primary группа выдана временно → показывает оставшееся время (например: `30д`, `5час`, `15мин`)
- Если primary группа постоянная → показывает символ бесконечности `∞`
- Если нет данных → показывает `0`

**Пример настройки в LuckPerms:**
```bash
# Выдать временную группу на 30 дней
/lp user Player parent settemp vip 30d

# Установить как primary группу
/lp user Player parent set vip
```

### Команды
- `/donateduration` или `/dd` - главная команда
- `/dd reload` - перезагрузить конфигурацию

### Права
- `donateduration.reload` - право на перезагрузку конфигурации

## 📋 Примеры отображения

| Оставшееся время | Отображение |
|------------------|-------------|
| 365 дней         | `1г`        |
| 90 дней          | `3мес`      |
| 7 дней           | `7д`        |
| 12 часов         | `12час`     |
| 45 минут         | `45мин`     |
| 30 секунд        | `30сек`     |
| Постоянная группа| `∞`         |
| Нет группы       | `0`         |

## 🔧 Требования

- **Minecraft:** 1.16.5 - 26.2
- **Сервер:** Spigot / Paper / Purpur / LeafMC
- **Java:** 21+
- **Зависимости:**
  - PlaceholderAPI
  - LuckPerms

## 📝 Информация

- **Версия:** 1.2.1
- **Автор:** [KusokMedi](https://github.com/kusokmedi)
- **Репозиторий:** [github.com/KusokMedi/DonDuration](https://github.com/kusokmedi/donduration)
- **Сборка:** Готовые jar файлы доступны в [Releases](https://github.com/KusokMedi/DonDuration/releases/tag/Stable)

## 🛠️ Сборка из исходников

Если вы хотите собрать плагин самостоятельно:

```bash
git clone https://github.com/kusokmedi/donduration.git
cd donduration
mvn clean package
```

Готовый jar файл будет в папке `target/DonateDuration-1.2.1.jar`

## 📄 Лицензия

Этот проект имеет MIT лицензию
