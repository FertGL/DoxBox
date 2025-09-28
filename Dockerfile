# Используем официальный образ для OpenJDK
FROM openjdk:17-jdk-slim

# Копируем JAR файл в контейнер
COPY target/DoxBox-0.0.1-SNAPSHOT.jar /app/myapp.jar


EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]