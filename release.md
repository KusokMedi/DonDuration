# DonateDuration v1.2.1 - Bugfix

Показывает оставшееся время донат-статуса через `%donateduration%` (PlaceholderAPI + LuckPerms).

## 🐛 Исправления

**Критическое исправление загрузки**:
- ✅ Изменен `depend` → `softdepend` для PlaceholderAPI
- ✅ Плагин теперь корректно загружается и выводит понятное сообщение об отсутствующих зависимостях
- ✅ Исправлена ошибка `UnknownDependencyException` при отсутствии PlaceholderAPI

**Что было**: `Could not load 'DonateDuration-1.2.jar' - Unknown/missing dependency plugins: [PlaceholderAPI]`  
**Что стало**: Плагин загружается и сообщает о необходимости установки PlaceholderAPI через собственный механизм проверки

## ⚠️ Важно

Плагин по-прежнему **требует PlaceholderAPI для работы**. Изменение только улучшает обработку ошибок.

**Требования:** Spigot/Paper 1.16.5 - 1.26.2 · Java 21+ · PlaceholderAPI · LuckPerms

Подробнее в [README](https://github.com/kusokmedi/donduration#readme) · [CHANGELOG](https://github.com/kusokmedi/donduration/blob/main/CHANGELOG.md).
