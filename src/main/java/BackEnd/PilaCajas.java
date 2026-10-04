package BackEnd;

/**
 * Pila (Stack) hecha a mano con nodos enlazados. Regla LIFO:
 * la última caja que entra (cima) es la primera que sale.
 */
public class PilaCajas {

    private Nodo cima;   // caja de hasta arriba
    private int tamanio; // cuántas Cajas hay

    public PilaCajas() {
        cima = null;
        tamanio = 0;
    }

    /** Apila una caja nueva encima de todas. */
    public void push(Cajas caja) {
        Nodo nuevo = new Nodo(caja);
        nuevo.setSiguiente(cima); // la nueva apunta a la que estaba arriba
        cima = nuevo;             // ahora la nueva es la cima
        tamanio++;
    }

    /** Saca y devuelve la caja de arriba. Devuelve null si la pila está vacía. */
    public Cajas pop() {
        if (estaVacia()) {
            return null;
        }
        Cajas caja = cima.getCaja();
        cima = cima.getSiguiente(); // la de abajo pasa a ser la cima
        tamanio--;
        return caja;
    }

    /** Devuelve la caja de arriba sin sacarla. Devuelve null si la pila está vacía. */
    public Cajas peek() {
        if (estaVacia()) {
            return null;
        }
        return cima.getCaja();
    }

    /** Vacía toda la pila. */
    public void clear() {
        cima = null;
        tamanio = 0;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    /** Primer nodo (cima), para recorrer la pila de arriba hacia abajo. */
    public Nodo getCima() {
        return cima;
    }

    /** Revisa si ya existe una caja con ese código. */
    public boolean existeCodigo(String codigo) {
        Nodo actual = cima;
        while (actual != null) {
            if (actual.getCaja().getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /** Suma el peso de todas las Cajas apiladas. */
    public double pesoTotal() {
        double total = 0;
        Nodo actual = cima;
        while (actual != null) {
            total += actual.getCaja().getPeso();
            actual = actual.getSiguiente();
        }
        return total;
    }
}
