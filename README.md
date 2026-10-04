# Almacén de Cajas – Pila (Stack) en Java

Pila LIFO implementada desde cero con nodos enlazados (sin `ArrayList` ni colecciones de Java) y una interfaz gráfica Swing hecha con el diseñador de NetBeans.

## Estructura

- `BackEnd/Cajas.java` – objeto caja (código, contenido, peso)
- `BackEnd/Nodo.java` – nodo que guarda una caja y apunta a la de abajo
- `BackEnd/PilaCajas.java` – operaciones `push`, `pop`, `peek`, `clear`
- `FrontEnd/GUICajas.java` / `.form` – ventana con formulario y dibujo de la torre

## Ejecutar

Abrir el proyecto en NetBeans y presionar **Run** (clase principal: `FrontEnd.GUICajas`).
