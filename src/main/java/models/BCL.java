package models;

public class BCL {
    private String idBus;
    private int numeroConductor;
    private int idLugar;
    private String diaSemana;
    private String imagen;

    public BCL() {}

    public BCL(String idBus, int numeroConductor, int idLugar, String diaSemana) {
        this.idBus = idBus;
        this.numeroConductor = numeroConductor;
        this.idLugar = idLugar;
        this.diaSemana = diaSemana;
    }

    public String getIdBus() { return idBus; }
    public void setIdBus(String idBus) { this.idBus = idBus; }

    public int getNumeroConductor() { return numeroConductor; }
    public void setNumeroConductor(int numeroConductor) { this.numeroConductor = numeroConductor; }

    public int getIdLugar() { return idLugar; }
    public void setIdLugar(int idLugar) { this.idLugar = idLugar; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    @Override
    public String toString() {
        return "BCL{" +
                "idBus='" + idBus + '\'' +
                ", numeroConductor=" + numeroConductor +
                ", idLugar=" + idLugar +
                ", diaSemana='" + diaSemana + '\'' +
                '}';
    }

    /**
     * Este metodo muestra por pantalla toda la información de este objeto
     */
    public void mostrarInfo() {
        System.out.println("-".repeat(50) + "\n" +
                "ID Bus: " + idBus + "\n" +
                "Nº Conductor: " + numeroConductor + "\n" +
                "ID Lugar: " + idLugar + "\n" +
                "Dia de la semana: " + diaSemana + "\n" +
                "-".repeat(50));
    }
}
