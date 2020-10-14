package Analizadores;

import java.io.IOException;
import java.util.ArrayList;

public class MainCompilador {

    private static ArrayList<Token> tokens;

    public static void main(String[] args) throws IOException {
        LeerArchivos arch = new LeerArchivos("Codigo");
        Lexico analizadorLexico = new Lexico();
        tokens = new ArrayList<>();
        String linea = "";
        int l = 0;

        while (linea != null) {
            l++;
            linea = arch.leerSigLinea();
            analizadorLexico.analizarLinea(linea, l, 0);
        }

        new Sintactico(obtenerTipos(),tokens);
        generateTabla();
    }

    private static ArrayList<String> obtenerTipos() {
        ArrayList<String> al = new ArrayList<>();
        for (Token token : tokens)
            al.add(token.getTipo());
        return al;
    }
    

    public static void agregarToken(String palabra, boolean pres, String tipo,int l,int c) {
        tokens.add(new Token(palabra, pres, tipo,l,c));
           /* System.out.println("");
        System.out.println(palabra+" "+pres+" "+tipo+" "+l+" "+c);*/
    }
    public static void generateTabla(){
        TablaDeSimbolos ts = new TablaDeSimbolos();
        for (int i = 0; i <tokens.size(); i++) {
            if(tokens.get(i).getTipo().equals("identifier")){
                if(tokens.get(i+1).getTipo().equals("=")){
            ts.crearEntrada(tokens.get(i).getToken(), tokens.get(i-1).getToken(), 
                    tokens.get(i+2).getToken().toString(), tokens.get(i).getLinea());
                }
            }
        }
         ts.getTabla();
    }
}
