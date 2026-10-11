package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * TDA Lista Bidireccional (doblemente enlazada) de cajas.
 * Cada nodo apunta a su siguiente y a su anterior; la lista guarda los dos
 * extremos: "primero" (posición 0) y "ultimo" (posición tamanio - 1).
 */
public class ListaCajas {

    /** Resultado de buscar por valor: la caja encontrada y en qué posición está. */
    public record ResultadoBusqueda(Caja caja, int posicion) {
    }

    private Nodo primero; // cabeza de la lista (null si está vacía)
    private Nodo ultimo;  // cola de la lista (null si está vacía)
    private int tamanio;  // cuántos nodos hay

    public ListaCajas() {
        primero = null;
        ultimo = null;
        tamanio = 0;
    }

    // ===================== INSERTAR =====================

    public void insertarAlPrincipio(Caja caja) {
        Nodo nuevo = new Nodo(caja);
        if (estaVacia()) {
            primero = nuevo; // el único nodo es primero y último a la vez
            ultimo = nuevo;
        } else {
            nuevo.setSiguiente(primero); // el nuevo apunta a la antigua cabeza
            primero.setAnterior(nuevo);  // la antigua cabeza apunta de regreso al nuevo
            primero = nuevo;             // y ahora él es la cabeza
        }
        tamanio++;
    }

    public void insertarAlFinal(Caja caja) {
        Nodo nuevo = new Nodo(caja);
        if (estaVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.setAnterior(ultimo);  // el nuevo apunta hacia atrás a la antigua cola
            ultimo.setSiguiente(nuevo); // la antigua cola apunta hacia adelante al nuevo
            ultimo = nuevo;             // ya no hay que recorrer: tenemos "ultimo"
        }
        tamanio++;
    }

    /** Válido de 0 a tamanio (tamanio equivale a insertar al final). */
    public void insertarEnPosicion(Caja caja, int posicion) {
        if (posicion < 0 || posicion > tamanio) {
            throw new IndexOutOfBoundsException("Posición " + posicion
                    + " inválida. Para insertar debe estar entre 0 y " + tamanio + ".");
        }
        if (posicion == 0) {
            insertarAlPrincipio(caja);
            return;
        }
        if (posicion == tamanio) {
            insertarAlFinal(caja);
            return;
        }
        Nodo actual = nodoEn(posicion);      // el que hoy ocupa esa posición
        Nodo previo = actual.getAnterior();
        Nodo nuevo = new Nodo(caja);
        nuevo.setAnterior(previo);  // 1) el nuevo apunta a sus dos vecinos
        nuevo.setSiguiente(actual);
        previo.setSiguiente(nuevo); // 2) los vecinos apuntan al nuevo
        actual.setAnterior(nuevo);
        tamanio++;
    }

    // ===================== REMOVER =====================

    /** Devuelve la caja removida, o null si la lista está vacía. */
    public Caja removerAlPrincipio() {
        if (estaVacia()) {
            return null;
        }
        Nodo eliminado = primero;
        primero = eliminado.getSiguiente(); // la cabeza pasa al segundo nodo
        if (primero == null) {
            ultimo = null;                  // era el único nodo
        } else {
            primero.setAnterior(null);      // la nueva cabeza no tiene anterior
        }
        eliminado.setSiguiente(null);       // aislamos el nodo removido
        tamanio--;
        return eliminado.getCaja();
    }

    /** Devuelve la caja removida, o null si la lista está vacía. */
    public Caja removerAlFinal() {
        if (estaVacia()) {
            return null;
        }
        Nodo eliminado = ultimo;
        ultimo = eliminado.getAnterior(); // la cola pasa al penúltimo, sin recorrer
        if (ultimo == null) {
            primero = null;               // era el único nodo
        } else {
            ultimo.setSiguiente(null);    // la nueva cola no tiene siguiente
        }
        eliminado.setAnterior(null);      // aislamos el nodo removido
        tamanio--;
        return eliminado.getCaja();
    }

    public Caja removerEnPosicion(int posicion) {
        validarPosicionExistente(posicion);
        if (posicion == 0) {
            return removerAlPrincipio();
        }
        if (posicion == tamanio - 1) {
            return removerAlFinal();
        }
        Nodo eliminado = nodoEn(posicion);
        Nodo previo = eliminado.getAnterior();
        Nodo posterior = eliminado.getSiguiente();
        previo.setSiguiente(posterior); // puenteamos en ambos sentidos:
        posterior.setAnterior(previo);  // previo ⇄ posterior
        eliminado.setSiguiente(null);   // aislamos el nodo removido
        eliminado.setAnterior(null);
        tamanio--;
        return eliminado.getCaja();
    }

    // ===================== MOSTRAR (no modifica la lista) =====================

    public Caja mostrarAlPrincipio() {
        return estaVacia() ? null : primero.getCaja();
    }

    public Caja mostrarAlFinal() {
        return estaVacia() ? null : ultimo.getCaja();
    }

    public Caja mostrarEnPosicion(int posicion) {
        validarPosicionExistente(posicion);
        return nodoEn(posicion).getCaja();
    }

    // ===================== BUSCAR =====================

    /** Busca por código (sin distinguir mayúsculas). Devuelve null si no existe. */
    public ResultadoBusqueda buscarPorCodigo(String codigo) {
        Nodo actual = primero;
        int posicion = 0;
        while (actual != null) {
            if (actual.getCaja().codigo().equalsIgnoreCase(codigo)) {
                return new ResultadoBusqueda(actual.getCaja(), posicion);
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return null;
    }

    /** Busca por índice; recorre desde el extremo más cercano. */
    public ResultadoBusqueda buscarPorPosicion(int posicion) {
        validarPosicionExistente(posicion);
        return new ResultadoBusqueda(nodoEn(posicion).getCaja(), posicion);
    }

    // ===================== AUXILIARES =====================

    public void vaciar() {
        primero = null; // sin referencias a los extremos, el recolector libera los nodos
        ultimo = null;
        tamanio = 0;
    }

    public boolean estaVacia() {
        return primero == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    /** Para que la vista pueda recorrer la lista hacia adelante sin modificarla. */
    public Nodo getPrimero() {
        return primero;
    }

    /** Para que la vista pueda recorrer la lista hacia atrás sin modificarla. */
    public Nodo getUltimo() {
        return ultimo;
    }

    public double pesoTotal() {
        double total = 0;
        Nodo actual = primero;
        while (actual != null) {
            total += actual.getCaja().peso();
            actual = actual.getSiguiente();
        }
        return total;
    }

    /**
     * Devuelve el nodo en "posicion" partiendo del extremo más cercano:
     * si está en la primera mitad avanza desde primero, si no retrocede desde ultimo.
     * Supone que la posición ya es válida.
     */
    private Nodo nodoEn(int posicion) {
        if (posicion < tamanio / 2) {
            Nodo actual = primero;
            for (int i = 0; i < posicion; i++) {
                actual = actual.getSiguiente();
            }
            return actual;
        }
        Nodo actual = ultimo;
        for (int i = tamanio - 1; i > posicion; i--) {
            actual = actual.getAnterior();
        }
        return actual;
    }

    private void validarPosicionExistente(int posicion) {
        if (estaVacia()) {
            throw new IndexOutOfBoundsException("La lista está vacía.");
        }
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException("Posición " + posicion
                    + " inválida. Debe estar entre 0 y " + (tamanio - 1) + ".");
        }
    }
}
