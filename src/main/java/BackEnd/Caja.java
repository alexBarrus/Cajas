package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * Entidad de dominio: una caja del almacén.
 * Es un record, así que es inmutable: Java genera solo el constructor,
 * los métodos de lectura codigo(), contenido(), peso(), equals y hashCode.
 */
public record Caja(String codigo, String contenido, double peso) {

    // Constructor compacto: se ejecuta antes de guardar los campos.
    // Última defensa por si alguien crea una Caja con datos inválidos.
    public Caja {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (contenido == null || contenido.isBlank()) {
            throw new IllegalArgumentException("El contenido no puede estar vacío.");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que 0.");
        }
    }

    @Override
    public String toString() {
        return "Código: " + codigo + "\nContenido: " + contenido
                + "\nPeso: " + String.format("%.2f", peso) + " kg";
    }
}
