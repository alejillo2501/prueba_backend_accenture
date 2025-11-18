FROM mysql:8.0

# Copiar el script SQL al directorio de inicialización
COPY init.sql /docker-entrypoint-initdb.d/

# Exponer el puerto
EXPOSE 3306

# Configuración de salud
HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
    CMD mysqladmin ping -h localhost -u root -p$${MYSQL_ROOT_PASSWORD}