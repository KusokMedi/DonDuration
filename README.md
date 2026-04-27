# DonateDuration

Плагин для Spigot 1.21.x, который отображает оставшееся время донат-статуса игрока через PlaceholderAPI, используя данные из LuckPerms.

## 📥 Установка

1. Скачайте последнюю версию плагина из [Releases](https://github.com/KusokMedi/DonDuration/releases/tag/Stable)
2. Поместите `DonateDuration-6.7.jar` в папку `plugins` вашего сервера
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

- **Minecraft:** 1.21.x
- **Сервер:** Spigot / Paper
- **Java:** 21+
- **Зависимости:**
  - PlaceholderAPI
  - LuckPerms

## 📝 Информация

- **Версия:** 6.7
- **Автор:** [KusokMedi](https://github.com/kusokmedi)
- **Репозиторий:** [github.com/kusokmedi/donduration](https://github.com/kusokmedi/donduration)
- **Сборка:** Готовые jar файлы доступны в [Releases](https://github.com/KusokMedi/DonDuration/releases/tag/Stable)

## 🛠️ Сборка из исходников

Если вы хотите собрать плагин самостоятельно:

```bash
git clone https://github.com/kusokmedi/donduration.git
cd donduration
mvn clean package
```

Готовый jar файл будет в папке `target/DonateDuration-6.7.jar`

## 📄 Лицензия

Этот проект распространяется свободно для использования на серверах Minecraft.
