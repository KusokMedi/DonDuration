#!/bin/bash

##############################################################################
# DonateDuration Build Script
# Скрипт для сборки плагина DonateDuration
# Автор: KusokMedi
# Совместимость: 1.16.5 - 26.2+
##############################################################################

set -e

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Функции логирования
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[✓]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[⚠]${NC} $1"
}

log_error() {
    echo -e "${RED}[✗]${NC} $1"
}

##############################################################################
# Проверка зависимостей
##############################################################################

log_info "Проверка зависимостей..."

# Проверяем Java
if ! command -v java &> /dev/null; then
    log_error "Java не найдена! Пожалуйста, установите Java 11 или выше."
    log_info "Скачайте Java: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[0-9]+' | head -1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    log_error "Требуется Java 11 или выше. Установлена версия $JAVA_VERSION."
    exit 1
fi
log_success "Java $JAVA_VERSION найдена"

# Проверяем Maven
if ! command -v mvn &> /dev/null; then
    log_error "Maven не найден! Пожалуйста, установите Apache Maven."
    log_info "Скачайте Maven: https://maven.apache.org/download.cgi"
    log_info "Или установите через пакетный менеджер:"
    log_info "  Ubuntu/Debian: sudo apt-get install maven"
    log_info "  CentOS/RHEL: sudo yum install maven"
    log_info "  macOS: brew install maven"
    exit 1
fi

MVN_VERSION=$(mvn -version 2>&1 | grep "Apache Maven" | awk '{print $3}')
log_success "Maven $MVN_VERSION найден"

# Проверяем Git (опционально)
if ! command -v git &> /dev/null; then
    log_warning "Git не найден. Проверка версии может не работать корректно."
else
    log_success "Git найден"
fi

##############################################################################
# Установка зависимостей Maven
##############################################################################

log_info "Загрузка зависимостей Maven..."
if ! mvn dependency:resolve -q 2>/dev/null; then
    log_warning "Не удалось загрузить зависимости автоматически. Попытка полной сборки..."
fi

##############################################################################
# Сборка проекта
##############################################################################

log_info "Начало сборки проекта DonateDuration 2.0..."

# Очистка старых сборок
if [ -d "target" ]; then
    log_info "Очистка старых файлов сборки..."
    mvn clean -q
fi

# Компиляция и упаковка
if mvn package -DskipTests -q; then
    log_success "Сборка успешно завершена!"
else
    log_error "Сборка не удалась!"
    log_info "Запуск полной сборки с детальным выводом ошибок..."
    mvn package -DskipTests
    exit 1
fi

##############################################################################
# Проверка результата
##############################################################################

JAR_FILE="target/DonateDuration-2.0.jar"

if [ -f "$JAR_FILE" ]; then
    JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
    log_success "JAR файл создан: $JAR_FILE ($JAR_SIZE)"
    
    # Проверяем целостность JAR
    if jar tf "$JAR_FILE" > /dev/null 2>&1; then
        log_success "JAR файл целостен и готов к использованию"
    else
        log_error "JAR файл повреждён!"
        exit 1
    fi
else
    log_error "JAR файл не найден в target/!"
    exit 1
fi

##############################################################################
# Информация об установке
##############################################################################

log_info "Инструкции по установке:"
echo ""
echo "  1. Скопируйте JAR файл на сервер:"
echo "     cp $JAR_FILE /path/to/server/plugins/"
echo ""
echo "  2. Установите зависимости (если не установлены):"
echo "     - PlaceholderAPI (https://www.spigotmc.org/resources/placeholderapi.6245/)"
echo "     - LuckPerms (https://luckperms.net/)"
echo ""
echo "  3. Перезапустите сервер:"
echo "     /stop"
echo ""
echo "  4. Используйте плейсхолдер: %donduration%"
echo ""

##############################################################################
# Успешное завершение
##############################################################################

log_success "Сборка DonateDuration 2.0 завершена успешно!"
log_info "Плагин готов к использованию"

exit 0
