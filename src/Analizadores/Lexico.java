package Analizadores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexico {
    private final ArrayList<String> PalabrasReservadas;
    private final String[] tiposDeTokens = {"modifier", "identifier", "type", "relational operator", "aritmetical operator",
            "boolean literal", "integer literal", "open key", ";", "=", "open parentheses", "close key",
            "close parentheses"};
    private final String[] palabrasReservadas = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
            "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto",
            "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected",
            "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
            "try", "void", "volatile", "while"};

    public Lexico() {
        PalabrasReservadas = new ArrayList<>();
        PalabrasReservadas.addAll(Arrays.asList(palabrasReservadas));
    }

    public void analizarLinea(String linea, int l, int c) {
        while (linea != null && linea.length() > 0) {
            int corte = linea.indexOf(" ");
            String palabra;
            if (corte == -1) {
                palabra = linea;
               System.out.print(palabra +" ");
                if (palabra.contains("{") || palabra.contains("=") ||
                        palabra.contains(";") || palabra.contains(">") || palabra.contains("<")) {
                    separaTokens(palabra);
                    linea = "";
                    continue;
                }
                linea = "";
            } else {
                palabra = linea.substring(0, corte);
                System.out.print(palabra +' ');
                if (palabra.contains("{") || palabra.contains("=") ||
                        palabra.contains(";") || palabra.contains(">") || palabra.contains("<")) {
                    separaTokens(palabra);
                    linea = linea.substring(corte + 1);
                    continue;
                }
                if (palabra.equals("")) {
                    linea = linea.substring(corte + 1);
                    continue;
                }
                linea = linea.substring(corte + 1);
            }
            boolean pres = comprobarPalabra(palabra);
            String tipo = tipoDeToken(palabra);
            if (tipo.equals("Error")) {
                mostrarError(palabra.charAt(0), l, c + 1);
                continue;
            }
            c = c + corte + 1;
            MainCompilador.agregarToken(palabra, pres, tipo,l,c);
        }
    }


    //Retorna el tipo de token en base a la palabra
    public String tipoDeToken(String palabra) {
        if (palabra.equals("(")){
            return tiposDeTokens[10];
        }
        if (palabra.equals(")")){

            return tiposDeTokens[12];
        }
        if (palabra.equals("=")){
            return tiposDeTokens[9];
        }
        if (palabra.equals(";")){
            return tiposDeTokens[8];
        }
        if (palabra.equals("{")){
            return tiposDeTokens[7];
        }
        if (palabra.equals("}")){
            return tiposDeTokens[11];
        }
        if (palabra.equals("+") || palabra.equals("-") || palabra.equals("*") || palabra.equals("/")|| palabra.equals("%")){

            return tiposDeTokens[4];
        }
        if (palabra.equals("public") || palabra.equals("private")){
 
            return tiposDeTokens[0];
        }
        if (palabra.equals("int") || palabra.equals("boolean")|| palabra.equals("double")|| palabra.equals("char")|| palabra.equals("byte")|| palabra.equals("float")
                || palabra.equals("long")|| palabra.equals("short")|| palabra.equals("class")|| palabra.equals("interface")){
            return tiposDeTokens[2];
        }
        if (palabra.equals("<") || palabra.equals("==") || palabra.equals("!=") || palabra.equals(">") || palabra.equals("<=") || palabra.equals(">=")){
            return tiposDeTokens[3];
        }
        if (palabra.equals("true") || palabra.equals("false")){
            return tiposDeTokens[5];
        }
        try {
            Integer.parseInt(palabra);
            return tiposDeTokens[6];
        } catch (Exception ignored) {
        }
        if (validarExpresion(palabra)){
            return tiposDeTokens[1];
        }
        
        return "Error";
        
    }

    //Primer llamado de la busqueda binaria
    public boolean comprobarPalabra(String palabra) {
        return busquedaBinaria(palabra, 0, PalabrasReservadas.size());
    }



    //Muestra errores léxicos
    public void mostrarError(char token, int linea, int columna) {
        System.out.println("");
        System.out.println( "Error en la línea " + linea + ",columna " + columna + ", [ " + token + " ] es un simbolo no permitido" );
    }

    //Valida que el identifier cumpla con la expresion
    public boolean validarExpresion(String palabra) {
        Pattern pat = Pattern.compile("[A-Za-z0-9]");
        Matcher mat = pat.matcher(palabra);
        return mat.find();
    }

    //Separa tokens sin espacios entre ellos
    public void separaTokens(String palabra) {
        int i;
        String t;
        while (palabra.length() != 0) {
            if (palabra.charAt(0) == '(' || palabra.charAt(0) == ')') {
                
                MainCompilador.agregarToken(palabra.charAt(0) + "", false, tipoDeToken(palabra.charAt(0) + ""),0,0);
                if (palabra.length() != 1) {
                    palabra = palabra.substring(1);
                    continue;
                }
                palabra = "";
                continue;
            }
            if (palabra.indexOf("{") == 0 || palabra.indexOf("}") == 0) {
                MainCompilador.agregarToken(palabra.substring(0, 1), false, tipoDeToken(palabra.charAt(0) + ""),0,0);
                if (palabra.length() != 1) {
                    palabra = palabra.substring(1);
                    continue;
                }
                palabra = "";
                continue;
            }
            if (palabra.indexOf(";") == 0) {
                MainCompilador.agregarToken(";", false, ";",0,0);
                if (palabra.length() != 1) {
                    palabra = palabra.substring(1);
                    continue;
                }
                palabra = "";
                continue;
            }
            try {
                if (
                        (palabra.charAt(0) == '=' || palabra.charAt(0) == '>' || palabra.charAt(0) == '<')
                                &&
                                (palabra.charAt(1) == '=' || palabra.charAt(1) == '>' || palabra.charAt(1) == '<')
                ) {
                    String aux = palabra.substring(0, 2);
                    MainCompilador.agregarToken(aux, false, tipoDeToken(aux),0,0);
                    if (palabra.length() != 2) {
                        palabra = palabra.substring(2);
                        continue;
                    }
                    palabra = "";
                    continue;
                }
            } catch (Exception ignored) {

            }
            if (palabra.charAt(0) == '=') {
                MainCompilador.agregarToken(palabra.charAt(0) + "", false, tipoDeToken(palabra.charAt(0) + ""),0,0);
                if (palabra.length() != 1) {
                    palabra = palabra.substring(1);
                    continue;
                }
                palabra = "";
                continue;
            }
            for (i = 0; validarExpresion(palabra.charAt(i)); i++) ;
            t = palabra.substring(0, i);
            boolean pres = comprobarPalabra(t);
            String tipo = tipoDeToken(t);
            MainCompilador.agregarToken(t, pres, tipo,0,0);
            palabra = palabra.substring(i);
        }
    }

    //Busqueda para verificar palabra reservada
    private boolean busquedaBinaria(String palabra, int izq, int der) {
        int medio = (der + izq) / 2;
        int band = palabra.compareTo(PalabrasReservadas.get(medio));
        if (band == 0)
            return true;
        if (izq == der || (izq == der - 1))
            return false;
        if (band > 0)
            return busquedaBinaria(palabra, medio, der);
        return busquedaBinaria(palabra, izq, medio);
    }

    //Valida que sean Letras de la a-z mayusculas o minusculas o numeros del 0-9
    public static boolean validarExpresion(char c) {
        Pattern pat = Pattern.compile("[A-Za-z0-9]");
        Matcher mat = pat.matcher(c + "");
        return mat.find();
    }
}
