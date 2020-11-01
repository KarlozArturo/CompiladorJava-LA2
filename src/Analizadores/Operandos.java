package Analizadores;

public class Operandos {
    private String nombre = "";
    private String valor = "";

    public Operandos(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getValor() {
        return valor;
    }
}
