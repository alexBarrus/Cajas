package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */
public class Nodo {

    private final Caja caja; // el dato (record) que guarda este vagón
    private Nodo siguiente;  // enganche al próximo nodo (null si es el último)
    private Nodo anterior;   // enganche al nodo previo (null si es el primero)

    public Nodo(Caja caja) {
        this.caja = caja;
        this.siguiente = null;
        this.anterior = null;
    }

    public Caja getCaja() {
        return caja;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public Nodo getAnterior() {
        return anterior;
    }

    public void setAnterior(Nodo anterior) {
        this.anterior = anterior;
    }
}
