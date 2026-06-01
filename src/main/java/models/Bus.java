package models;

public class Bus {
    private String idBus;
    private String tipoBus;
    private String licenciaBus;
    private String imagen;

    public Bus() {}

    public Bus(String idBus, String tipoBus, String licenciaBus) {
        this.idBus = idBus;
        this.tipoBus = tipoBus;
        this.licenciaBus = licenciaBus;
    }

    public String getIdBus() { return idBus; }
    public void setIdBus(String idBus) { this.idBus = idBus; }

    public String getTipoBus() { return tipoBus; }
    public void setTipoBus(String tipoBus) { this.tipoBus = tipoBus; }

    public String getLicenciaBus() { return licenciaBus; }
    public void setLicenciaBus(String licenciaBus) { this.licenciaBus = licenciaBus; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    @Override
    public String toString() {
        return "Bus{" +
                "idBus='" + idBus + '\'' +
                ", tipoBus='" + tipoBus + '\'' +
                ", licenciaBus='" + licenciaBus + '\'' +
                '}';
    }

    /**
     * Este metodo muestra por pantalla toda la información de este objeto
     */
    public void mostrarInfo() {
        System.out.println("-".repeat(50) + "\n" +
                "ID Bus: " + idBus + "\n" +
                "Tipo de bus: " + tipoBus + "\n" +
                "Licencia bus: " + licenciaBus + "\n" +
                "-".repeat(50));
    }
}
