# Almacén de Cajas – Lista Bidireccional en Java

TDA Lista Doblemente Enlazada implementada desde cero con nodos (`primero`, `ultimo`, `siguiente`, `anterior`), sin `ArrayList` ni colecciones de Java, con una interfaz gráfica Swing.

## Estructura

- `BackEnd/Caja.java` – record inmutable de la caja (código, contenido, peso)
- `BackEnd/Nodo.java` – nodo con la caja y enlaces `siguiente` y `anterior`
- `BackEnd/ListaCajas.java` – insertar, remover y mostrar (principio, final, posición), buscar por código o posición, vaciar
- `FrontEnd/GUICajas.java` – ventana con formulario, botones, tabla y recorrido adelante / atrás

## Ejecutar

Abrir el proyecto en NetBeans y presionar **Run** (clase principal: `FrontEnd.GUICajas`).
