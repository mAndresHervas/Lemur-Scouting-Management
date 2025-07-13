# 🐾 Lemur - Scout Management App

**Lemur** es una aplicación de escritorio moderna desarrollada en **Java 21** con **JavaFX**, diseñada para facilitar la gestión de grupos **Scout** en sus actividades y campamentos. Utiliza tecnologías cloud como **Firebase** para base de datos en tiempo real y autenticación de usuarios, todo orquestado con **Maven** y estilizado con **CSS personalizado**.

---

## 🎯 Características principales

✅ **Asistencia**  
Gestión completa de asistencia por usuario, actividad y fecha. Registro rápido, filtrado por patrulla o rol, y persistencia en Firebase.

✅ **Inventario**  
Control total de materiales de grupo: carpas, utensilios, mochilas, etc. Con estado, cantidad, disponibilidad y observaciones.

🔄 **Alimentación (15%)**  
Planificación de menús, raciones por patrulla, lista de compras y costes (en desarrollo).

🛡️ **Control de Accesos y Roles**  
Usuarios con permisos diferenciados según su rol: *Scout*, *Jefe de Patrulla*, *Administrador*, etc.

🛡️ **Importación y exportación de PDF y hojas de cálculo**  
 
 
## 🎯 ROADMAP
 Asistencia 100% funcional

 Inventario completo y validado

 Alimentación en desarrollo (15%)

 Sistema de notificaciones

 Gestión de eventos y calendarios

 Módulo de progresión e insignias

## ⚙️ Tecnologías y dependencias

| Tecnología         | Descripción                                        |
|--------------------|----------------------------------------------------|
| 🧠 Java 21         | Lenguaje base y mejoras de rendimiento             |
| 🎨 JavaFX          | UI moderna, multiplataforma                        |
| 🧰 Maven           | Gestión de dependencias y empaquetado              |
| 🔥 Firebase        | Base de datos en tiempo real y autenticación       |
| 🎨 CSS             | Estilizado personalizado para UI                   |
| ☁️ Google Cloud    | Librerías oficiales para acceso a Firebase         |

---


---

## 🚀 Cómo ejecutar el proyecto

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu-usuario/Lemur-Scouting-Management.git
2. Asegúrate de tener:

JDK 21

Maven instalado y configurado

Acceso a tu proyecto de Firebase (agrega el archivo de configuración si es necesario)

3. Ejecuta con Maven:

bash
Copiar
Editar
mvn clean javafx:run

📜 Licencia
Este proyecto es de uso educativo. Si deseas colaborar o usarlo para tu grupo Scout, ¡no dudes en abrir un issue o pull request!
