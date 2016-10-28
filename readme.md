Seguridad
==================

Aplicación segura con Login y mensajes cifrados.

Tecnlogías usadas:
- Java 8
- Spring-Boot
- JavaFX 
- JPA 
- MySQL

Algoritmos
- Las passwords son protegidas mediante BCrypt configurado con Salt generado con SecureRandom y una seed propia.
- Los mensajes son encriptados mediantes AES; el usuario ingresa una clave y se le asigna un Salt para la encripción automáticamente.

Usage:
```mvn spring-boot:run``` 
