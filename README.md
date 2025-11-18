# Franchise API

Bienvenido a la documentación de **Franchise API**, un servicio backend robusto y escalable construido con un enfoque moderno y nativo de la nube. Esta documentación está dirigida a desarrolladores, arquitectos y personal de operaciones para comprender la arquitectura, las decisiones de diseño y cómo desplegar y desarrollar la aplicación.

## Autor

*   **Oscar Alejandro Londoño Torres**
*   **Empresa dirigida:** Accenture

## Índice

1.  [Descripción General](#descripción-general)
2.  [Arquitectura de la Aplicación](#arquitectura-de-la-aplicación)
    *   [Infraestructura en la Nube (AWS)](#infraestructura-en-la-nube-aws)
    *   [Componentes Principales](#componentes-principales)
3.  [Stack Tecnológico](#stack-tecnológico)
4.  [Programación Reactiva: El Corazón de la API](#programación-reactiva-el-corazón-de-la-api)
5.  [Paso a paso de la aplicación (Desarrollo Local)](#paso-a-paso-de-la-aplicación-desarrollo-local)
    *   [Prerrequisitos](#prerrequisitos)
    *   [Levantando la Base de Datos](#levantando-la-base-de-datos)
    *   [Esquema de la Base de Datos](#esquema-de-la-base-de-datos)
    *   [Ejecutando la Aplicación](#ejecutando-la-aplicación)
6.  [Configuración](#configuración)
7.  [Despliegue en AWS](#despliegue-en-aws)

---

## Descripción General

`franchise-api` es una API RESTful diseñada para gestionar franquicias y sus tiendas asociadas. La aplicación está construida sobre Spring Boot con el stack reactivo (WebFlux), lo que le permite manejar una alta concurrencia con un consumo de recursos optimizado, haciéndola ideal para escenarios de microservicios y aplicaciones de alto rendimiento.

## Arquitectura de la Aplicación

La arquitectura está diseñada para ser nativa de la nube, utilizando **Infraestructura como Código (IaC)** con Terraform para un despliegue automatizado, repetible y seguro en Amazon Web Services (AWS).

### Infraestructura en la Nube (AWS)

La infraestructura completa se define en los archivos de Terraform ubicados en el directorio `/terraform`.

!AWS Architecture Diagram <!-- Reemplaza con un diagrama real si lo tienes -->

### Componentes Principales

*   **Red (VPC):** Se crea una Virtual Private Cloud (VPC) para aislar los recursos de la aplicación. Dentro de esta VPC, se configuran subredes privadas para alojar la base de datos, asegurando que no sea accesible directamente desde internet.
*   **Base de Datos (AWS RDS):** Utilizamos una instancia de **MySQL 8.0** gestionada a través de Amazon RDS. Esto abstrae la complejidad del mantenimiento, backups y escalabilidad de la base de datos. El acceso está restringido por un grupo de seguridad que solo permite conexiones desde recursos autorizados.
*   **Computación (AWS ECS):** La aplicación está contenedorizada y diseñada para ejecutarse en **Amazon Elastic Container Service (ECS)**. ECS orquesta el despliegue de los contenedores, gestionando la escalabilidad (auto-scaling) y la alta disponibilidad de manera automática.
*   **Seguridad:** Se utilizan grupos de seguridad para controlar el tráfico entre los servicios y hacia la base de datos, aplicando el principio de mínimo privilegio.

## Stack Tecnológico

*   **Lenguaje:** Java 17+
*   **Framework:** Spring Boot 3 (con WebFlux para reactividad)
*   **Acceso a Datos:** Spring Data R2DBC para acceso reactivo a la base de datos.
*   **Base de Datos:** MySQL
*   **Migraciones:** Flyway
*   **Build Tool:** Maven
*   **Contenerización:** Docker
*   **Infraestructura como Código:** Terraform
*   **Plataforma Cloud:** AWS

## Programación Reactiva: El Corazón de la API

Una de las decisiones arquitectónicas clave fue el uso de **programación reactiva**. A diferencia del modelo tradicional de "un hilo por petición", el modelo reactivo utiliza un pequeño número de hilos (event loops) para manejar múltiples peticiones de forma asíncrona y no bloqueante.

**¿En qué casos se utiliza?**

1.  **Acceso a la Base de Datos:** Gracias a **R2DBC** (Reactive Relational Database Connectivity), las consultas a la base de datos no bloquean el hilo de ejecución. Mientras la base de datos procesa una consulta, el hilo queda libre para atender otras peticiones, mejorando drásticamente la escalabilidad.
2.  **Endpoints de API:** Todos los controladores y servicios están construidos con `Mono` y `Flux` de Project Reactor. Esto asegura que todo el flujo de la petición, desde que llega hasta que se devuelve la respuesta, sea asíncrono.
3.  **Alta Concurrencia:** Es ideal para esta API, ya que se espera que maneje un gran volumen de solicitudes simultáneas sin degradar el rendimiento y utilizando los recursos del servidor de manera mucho más eficiente.

Este enfoque nos permite construir un sistema más resiliente, elástico y responsivo.

## Paso a paso de la aplicación (Desarrollo Local)

Para facilitar el desarrollo en un entorno local, el proyecto incluye una configuración de `docker-compose` que provisiona la base de datos necesaria.

### Prerrequisitos

*   JDK 17 o superior
*   Docker y Docker Compose
*   Maven

### Levantando la Base de Datos

El archivo `docker-compose.yml` en la raíz del proyecto está configurado para:
1.  Crear un contenedor con una base de datos **MySQL**.
2.  Inicializar la base de datos  con el nombre `franchise_db` y la API.
3.  **Insertar un esquema de tablas y agregar datos de prueba** automáticamente al iniciar. Esto se logra a través de scripts de inicialización o usando Flyway, que se ejecuta al arrancar la aplicación.

Para iniciar la base de datos, ejecuta el siguiente comando desde la raíz del proyecto:

```bash
docker-compose up -d
```

Este comando levanta el contenedor de la base de datos en segundo plano (`-d`).


### Ejecutando la Aplicación

Una vez que la base de datos esté en funcionamiento, puedes iniciar la aplicación Spring Boot usando el Maven Wrapper incluido:

```bash
# En Windows
./mvnw.cmd spring-boot:run

# En Linux/macOS
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

La documentacion Swagger estará diponible en `http://localhost:8080/swagger-ui.html`.

## Configuración

La configuración de la aplicación se gestiona a través del archivo `src/main/resources/application.yml`. Los valores sensibles o dependientes del entorno (como credenciales de la base de datos) se inyectan mediante variables de entorno, siguiendo las mejores prácticas de 12-Factor App.

## Despliegue en AWS

El despliegue en AWS se automatiza con Terraform.

1.  **Inicializar Terraform:**
    `terraform init`
2.  **Planificar los cambios:**
    `terraform plan`
3.  **Aplicar la infraestructura:**
    `terraform apply`

Esto creará todos los recursos de AWS definidos en los archivos `.tf`.

> **Nota:** El despliegue a AWS es una simulación propuesta para una buena arquitectura.
