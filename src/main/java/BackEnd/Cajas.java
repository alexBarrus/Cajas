package BackEnd;

/**
 * Objeto que se guarda en la pila (CajaMercancia).
 * Solo almacena los datos de una caja del almacén.
 */
public class Cajas {

    private String codigo;
    private String contenido;
    private double peso;

    public Cajas(String codigo, String contenido, double peso) {
        this.codigo = codigo;
        this.contenido = contenido;
        this.peso = peso;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + "\nContenido: " + contenido
                + "\nPeso: " + String.format("%.2f", peso) + " kg";
    }
}
