# DonateDuration v2.0 - Main Release

Плагин для Spigot 1.16.5 - 26.2, который отображает оставшееся время донат-статуса через `%donateduration%` (PlaceholderAPI + LuckPerms).

## 🆕 Новые функции версии 2.0

- 🔧 **Исправлены все критические баги** - Memory leak, thread safety, конвертация цветов
- 🔒 **Плейсхолдер зафиксирован** - %donduration% больше нельзя изменить
- 📦 **build.sh скрипт** - Автоматическая сборка с проверкой зависимостей
- ⚡ **Асинхронная очистка кэша** - Не влияет на производительность сервера
- 🐛 **Улучшена обработка ошибок** - Детальные debug сообщения
- ✅ **Java 11+ совместимость** - Работает со всеми версиями 1.16.5-26.2+

## 🐛 Исправления

- Исправлен Memory Leak в кэше
- Исправлена Thread Safety при обращении к LuckPerms API
- Исправлена конвертация цветов (& в §)
- Функционал placeholder зафиксирован на %donduration%
- Добавлена валидация конфигурации
- Побо strengthened error handling

## ⚠️ Важно

Плагин требует установки PlaceholderAPI для работы. Новые функции версии 2.0 включают улучшенную совместимость с Java 11+ и настраиваемую очистку кэша.

**Требования:** Spigot/Paper 1.16.5 - 26.2 · Java 11+ · PlaceholderAPI · LuckPerms

Подробнее в [README](https://github.com/kusokmedi/donduration#readme) · [CHANGELOG](https://github.com/kusokmedi/donduration/blob/main/CHANGELOG.md).