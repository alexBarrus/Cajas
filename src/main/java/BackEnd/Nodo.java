package BackEnd;

/**
 * Cápsula de la pila: guarda una caja y un "cable" (referencia)
 * que apunta al nodo de la caja que está debajo.
 */
public class Nodo {

    private Cajas caja;
    private Nodo siguiente; // la caja de abajo

    public Nodo(Cajas caja) {
        this.caja = caja;
        this.siguiente = null;
    }

    public Cajas getCaja() {
        return caja;
    }

    public void setCaja(Cajas caja) {
        this.caja = caja;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
