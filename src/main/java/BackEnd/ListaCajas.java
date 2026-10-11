package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * TDA Lista Unidireccional (simplemente enlazada) de cajas.
 * Las posiciones empiezan en 0: la caja apuntada por "primero" es la posición 0.
 */
public class ListaCajas {

    /** Resultado de buscar por valor: la caja encontrada y en qué posición está. */
    public record ResultadoBusqueda(Caja caja, int posicion) {
    }

    private Nodo primero; // cabeza de la lista (null si está vacía)
    private int tamanio;  // cuántos nodos hay

    public ListaCajas() {
        primero = null;
        tamanio = 0;
    }

    // ===================== INSERTAR =====================

    public void insertarAlPrincipio(Caja caja) {
        Nodo nuevo = new Nodo(caja);
        nuevo.setSiguiente(primero); // el nuevo apunta a la antigua cabeza
        primero = nuevo;             // y ahora él es la cabeza
        tamanio++;
    }

    public void insertarAlFinal(Caja caja) {
        Nodo nuevo = new Nodo(caja);
        if (estaVacia()) {
            primero = nuevo;
        } else {
            Nodo actual = primero;
            while (actual.getSiguiente() != null) { // avanzar hasta el último
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
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
        Nodo anterior = nodoEn(posicion - 1);
        Nodo nuevo = new Nodo(caja);
        nuevo.setSiguiente(anterior.getSiguiente()); // 1) el nuevo apunta al que estaba en esa posición
        anterior.setSiguiente(nuevo);                // 2) el anterior apunta al nuevo
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
        eliminado.setSiguiente(null);       // aislamos el nodo removido
        tamanio--;
        return eliminado.getCaja();
    }

    /** Devuelve la caja removida, o null si la lista está vacía. */
    public Caja removerAlFinal() {
        if (estaVacia()) {
            return null;
        }
        if (primero.getSiguiente() == null) { // solo hay un nodo
            return removerAlPrincipio();
        }
        Nodo penultimo = primero;
        while (penultimo.getSiguiente().getSiguiente() != null) {
            penultimo = penultimo.getSiguiente();
        }
        Caja caja = penultimo.getSiguiente().getCaja();
        penultimo.setSiguiente(null); // el penúltimo pasa a ser el último
        tamanio--;
        return caja;
    }

    public Caja removerEnPosicion(int posicion) {
        validarPosicionExistente(posicion);
        if (posicion == 0) {
            return removerAlPrincipio();
        }
        Nodo anterior = nodoEn(posicion - 1);
        Nodo eliminado = anterior.getSiguiente();
        anterior.setSiguiente(eliminado.getSiguiente()); // puenteamos: anterior -> subsiguiente
        eliminado.setSiguiente(null);                    // aislamos el nodo removido
        tamanio--;
        return eliminado.getCaja();
    }

    // ===================== MOSTRAR (no modifica la lista) =====================

    public Caja mostrarAlPrincipio() {
        return estaVacia() ? null : primero.getCaja();
    }

    public Caja mostrarAlFinal() {
        if (estaVacia()) {
            return null;
        }
        Nodo actual = primero;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getCaja();
    }

    public Caja mostrarEnPosicion(int posicion) {
        validarPosicionExistente(posicion);
        return nodoEn(posicion).getCaja();
    }

    // ===================== BUSCAR POR VALOR =====================

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

    // ===================== AUXILIARES =====================

    public void vaciar() {
        primero = null; // sin referencia a la cabeza, el recolector libera los nodos
        tamanio = 0;
    }

    public boolean estaVacia() {
        return primero == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    /** Para que la vista pueda recorrer la lista sin modificarla. */
    public Nodo getPrimero() {
        return primero;
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

    /** Avanza "posicion" saltos desde primero. Supone que la posición ya es válida. */
    private Nodo nodoEn(int posicion) {
        Nodo actual = primero;
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
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
