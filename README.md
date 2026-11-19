# UNIAJC Logistics Core

Este es nuestro proyecto final de Ingeniería de Software II (UNIAJC, grupo 6303A). Hicimos un prototipo en Java del motor de pedidos de una tienda en línea, usando patrones de comportamiento:

- Chain of Responsibility para validar el pedido (stock, fraude y cobertura).
- State para el ciclo de vida del pedido.
- Strategy para calcular el envío.
- Observer para las notificaciones y la auditoría.
- Command para aplicar descuentos y cambiar la dirección, con deshacer.

Integrantes:

- Juan Felipe Chilito Hoyos
- Jhonny Stiven Cárdenas
- Valentina Ceballos

## Cómo correrlo

Necesitamos Java 21. Para compilar y correr las pruebas:

```
probar.bat
```

El script usa `JAVA_HOME` si está definido. Si no, usa el `java` del PATH. JUnit viene en `lib/`.

## Carpetas

- `src/`: el código.
- `test/`: las pruebas unitarias.
- `docs/`: los diagramas y la explicación.
