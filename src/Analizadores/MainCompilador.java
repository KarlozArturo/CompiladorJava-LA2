package Analizadores;

import java.io.IOException;
import java.util.ArrayList;

public class MainCompilador {

    public static ArrayList<Token> tokens;
    public static ArrayList<Simbolos> tablaSimbolos = new ArrayList<>();

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
        recorrerTokens();
        getTablaSimbolos();
  
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
    
    
    public static void recorrerTokens(){
        Simbolos s ;
        
        for (int i = 0; i < tokens.size(); i++) {
            if(tokens.get(i).getTipo().equals("identifier")){
                //primero vamos a ver si ya existe previamente la variable en la tabla
                int nTablaSimbolos = existsInTabla(tokens.get(i).getToken());
                
                if(nTablaSimbolos==0){ 
                if(tokens.get(i-1).getTipo().equals("type")){
                    if(tokens.get(i+2).getTipo().equals("boolean literal")||tokens.get(i+2).getTipo().equals("integer literal")||tokens.get(i+1).getTipo().equals(";")){
                        if(tokens.get(i+1).getTipo().equals(";")){
                            //System.out.println(tokens.get(i).getToken()+" "+tokens.get(i-1).getToken()+" "+tokens.get(i-1).getLinea()+" "+ "Empty");
                            s = new Simbolos(tokens.get(i).getToken(), tokens.get(i-1).getToken(),"Empty", tokens.get(i-1).getLinea());
                            tablaSimbolos.add(s);
                        }else{
                //System.out.print(tokens.get(i).getToken()+" "+tokens.get(i-1).getToken()+" "+tokens.get(i-1).getLinea()+" "+tokens.get(i+2).getToken());
                s = new Simbolos(tokens.get(i).getToken(), tokens.get(i-1).getToken(), tokens.get(i+2).getToken(), tokens.get(i-1).getLinea());
                tablaSimbolos.add(s);
                        }
                }
                }
                    //System.out.println("");
               }else{
                  //si la variable ya existe en la tablaDeSimbolos
                   System.out.println(i);
                  newValue(i, nTablaSimbolos);
                    
                }
            }
        }
        
    }
    
    public static void getTablaSimbolos(){
        System.out.println("");
        System.out.println("| NOMBRE \t| TIPO \t| VALOR \t| POSICION \t| ");
        for (int i = 0; i < tablaSimbolos.size(); i++) {
            System.out.println(tablaSimbolos.get(i).getNombre()+"\t\t"+tablaSimbolos.get(i).getTipo()+" \t"+tablaSimbolos.get(i).getValor()+" \t"+tablaSimbolos.get(i).posicion);
        }
    }
    
    public static int existsInTabla(String nombre){
        int exists=0;
        for (int i = 0; i < tablaSimbolos.size(); i++){
            if(tablaSimbolos.get(i).getNombre().equals(nombre)){
                exists = i;
                System.out.println("ya existe en la tabla de simbolos");
                break;
            }
        }
        
        return exists;
    }
    
    public static void newValue(int i, int iSimmbol){
        for (int j = i+1;j <tokens.size(); j++) {
            //System.out.println(tokens.get(j).getToken());
            if(tokens.get(j).getTipo().equals("=")){
                if(tokens.get(j+2).getTipo().equals("aritmetical operator")){
                    System.out.println(tokens.get(j+2).getToken());
                    String operador = tokens.get(j+2).getToken();
                    String newValue = "";
                    int n1=Integer.parseInt(tokens.get(j+1).getToken()); 
                    int n2=Integer.parseInt(tokens.get(j+3).getToken());
                    switch ( operador){
                        case "+":
                            newValue = String.valueOf(n1+n2);
                            tablaSimbolos.get(iSimmbol).setValor(newValue);
                            break;
                        case "-":
                            newValue = String.valueOf(n1-n2);
                            tablaSimbolos.get(iSimmbol).setValor(newValue);
                            break;
                        case "/":
                            newValue = String.valueOf(n1/n2);
                            tablaSimbolos.get(iSimmbol).setValor(newValue);
                            break;
                        case "*":
                            newValue = String.valueOf(n1*n2);
                            tablaSimbolos.get(iSimmbol).setValor(newValue);
                            break;
                        case "%":
                            newValue = String.valueOf(n1%n2);
                            tablaSimbolos.get(iSimmbol).setValor(newValue);
                            break;
                    }
                }
            }
        }
    }
}
