# Dockerfile para liq-msa-bp-movements
FROM gradle:8.5-jdk17 AS build

# Copiar archivos de configuración de Gradle
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/

# Copiar código fuente
COPY src/ src/

# Dar permisos de ejecución al gradlew
RUN chmod +x gradlew

# Construir la aplicación
RUN ./gradlew build -x test --no-daemon --no-parallel

# Etapa de producción
FROM eclipse-temurin:17-jre-jammy

# Instalar curl para healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Crear usuario para ejecutar la aplicación (seguridad)
RUN groupadd --system --gid 1001 appgroup && \
    useradd --system --uid 1001 --gid 1001 --home /app --shell /bin/bash appuser

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR de la aplicación desde la etapa de build
COPY --from=build --chown=appuser:appgroup /home/gradle/build/libs/liq-msa-bp-movements-0.0.1-SNAPSHOT.jar app.jar

# Cambiar al usuario sin privilegios
USER appuser

# Exponer el puerto
EXPOSE 4103

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:4103/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
