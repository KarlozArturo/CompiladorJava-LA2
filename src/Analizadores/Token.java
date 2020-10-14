package Analizadores;

public class Token {

    private final boolean reservada;
    private final String token;
    private String tipo;    
    private int  linea, columna;

    public Token(String t,boolean r,String ti,int l,int  c){
        reservada=r;
        token=t;
        tipo=ti;
        linea=l;
        columna = c;
        if(tipo.equals("identifier") && reservada)
            tipo=token;
    }
    

    public String getTipo() {
        return tipo;
    }
    public int getLinea() {
        return linea;
    }
    public int getColumna() {
        return columna;
    }

    public String getToken() {
        return token;
    }

    public String toString(){
        return token+"\t"+reservada+"\t"+tipo;
    }
}
