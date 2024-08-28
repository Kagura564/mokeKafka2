# Используем базовый образ с JDK 17 для сборки приложения
FROM maven:3.9.4-eclipse-temurin-17 as build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . .

# Собираем проект с использованием Maven
RUN mvn clean package -DskipTests

# Используем базовый образ с JRE 17 для запуска приложения
FROM eclipse-temurin:17-jre

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем сгенерированный .jar файл из предыдущего шага
COPY --from=build /app/target/*.jar app.jar

# Указываем команду запуска при старте контейнера
CMD ["java", "-jar", "app.jar"]
