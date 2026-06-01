package models;

public class Conductor {


    /** Número identificador único del conductor (clave primaria). */
    private int numeroConductor;

    /** Nombre del conductor. */
    private String nombreConductor;

    /** Apellido o apellidos del conductor. */
    private String apellidoConductor;

    /**
     * Nombre del fichero de imagen personalizado almacenado en la base de datos
     */
    private String imagen;


    /**
     * Constructor vacío. Crea un conductor sin inicializar ningún campo.
     */
    public Conductor() {}

    /**
     * Constructor con los tres campos principales del conductor.
     * El campo iamgen queda como null por defecto.
     *
     * @param numeroConductor   Número identificador del conductor.
     * @param nombreConductor   Nombre del conductor.
     * @param apellidoConductor Apellido del conductor.
     */
    public Conductor(int numeroConductor, String nombreConductor, String apellidoConductor) {
        this.numeroConductor   = numeroConductor;
        this.nombreConductor   = nombreConductor;
        this.apellidoConductor = apellidoConductor;
    }

    public Conductor(int numeroConductor, String nombreConductor, String apellidoConductor, String imagen) {
        this.numeroConductor   = numeroConductor;
        this.nombreConductor   = nombreConductor;
        this.apellidoConductor = apellidoConductor;
        this.imagen = imagen;
    }


    /**
     * Devuelve el número identificador del conductor.
     *
     * @return El número del conductor.
     */
    public int getNumeroConductor() { return numeroConductor; }

    /**
     * Establece el número identificador del conductor.
     *
     * @param numeroConductor El nuevo número del conductor.
     */
    public void setNumeroConductor(int numeroConductor) { this.numeroConductor = numeroConductor; }

    /**
     * Devuelve el nombre del conductor.
     *
     * @return El nombre del conductor.
     */
    public String getNombreConductor() { return nombreConductor; }

    /**
     * Establece el nombre del conductor.
     *
     * @param nombreConductor El nuevo nombre del conductor.
     */
    public void setNombreConductor(String nombreConductor) { this.nombreConductor = nombreConductor; }

    /**
     * Devuelve el apellido del conductor.
     *
     * @return El apellido del conductor.
     */
    public String getApellidoConductor() { return apellidoConductor; }

    /**
     * Establece el apellido del conductor.
     *
     * @param apellidoConductor El nuevo apellido del conductor.
     */
    public void setApellidoConductor(String apellidoConductor) { this.apellidoConductor = apellidoConductor; }

    /**
     * Devuelve el nombre del fichero de imagen personalizado almacenado en la BBDD
     * @return El nombre del fichero de imagen, o null si no tiene imagen personalizada.
     */
    public String getImagen() { return imagen; }

    /**
     * Establece el nombre del fichero de imagen personalizado
     * @param imagen El nombre del fichero, o null para eliminar la personalización.
     */
    public void setImagen(String imagen) { this.imagen = imagen; }


    /**
     * Devuelve una representación textual del conductor con todos sus campos.
     *
     * @return Cadena con todos los atributos del conductor.
     */
    @Override
    public String toString() {
        return "Conductor{" +
                "numeroConductor="    + numeroConductor     +
                ", nombreConductor='" + nombreConductor     + '\'' +
                ", apellidoConductor='"+ apellidoConductor  + '\'' +
                ", imagen='"          + imagen              + '\'' +
                '}';
    }


    /**
     * Muestra por pantalla toda la información del conductor con un formato legible.
     */
    public void mostrarInfo() {
        System.out.println("-".repeat(50) + "\n" +
                "Nº Conductor: " + numeroConductor  + "\n" +
                "Nombre: "       + nombreConductor   + "\n" +
                "Apellido: "     + apellidoConductor  + "\n" +
                "Imagen: "       + (imagen != null ? imagen : "(por defecto)") + "\n" +
                "-".repeat(50));
    }
}