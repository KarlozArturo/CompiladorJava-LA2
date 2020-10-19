package Analizadores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sintactico {

    private final ArrayList<String> tiposTokens;
    private static ArrayList<Token> tokens;
    private static ArrayList<Simbolos> tablaSimbolos;
    private int contador = 0;
    private boolean bandera;

    public Sintactico(ArrayList<String> tt,ArrayList<Token> ts,ArrayList<Simbolos>tabs) {
        tiposTokens = tt;
        tokens =ts;
        tablaSimbolos = tabs;
        analizar();
    }

    private void analizar() {

        AnalizarClase(bandera);

            System.out.println("");
            System.out.println("Compilacion terminada.");
            System.out.println("");
        }
    

        public void AnalizarClase(boolean bandera) {
        if (tiposTokens.get(contador).equals("modifier"))
            contador++;
        if (tiposTokens.get(contador).equals("type")) {
            contador++;
            if (tiposTokens.get(contador).equals("identifier")) {
                contador++;
                if (tiposTokens.get(contador).equals("open key")) {
                    contador++;
                }
                if (AnalizarFieldDeclaracion(bandera)) {
                    AnalizarStatement(bandera);
                    AnalizarIfStatement(bandera);
                    AnalizarWhileStatement(bandera);
                    AnalizarType(bandera);
                    AnalizarExpresionAritmetica(bandera);
                }
                if (tiposTokens.get(contador).equals("close key")) {
                    bandera = true;
                }
            }
        } else {
            bandera = false;
        }
       
    }

    public boolean AnalizarFieldDeclaracion(boolean bandera) {
        if (AnalizarDeclaracionVariable(bandera)) {
            contador++;
            if (tiposTokens.get(contador).equals(";")) {
                contador++;
                bandera = true;
            } else
                bandera = false;
        }
        return bandera;
    }

    public boolean AnalizarDeclaracionVariable(boolean bandera) {
        if (tiposTokens.get(contador).equals("modifier"))
            contador++;
        if (tiposTokens.get(contador).equals("type")) {
            contador++;
            bandera = AnalizarDeclaradorVariable(bandera) || tiposTokens.get(contador).equals("identifier");
        } else {
            bandera = false;
        }

        return bandera;
    }

    public boolean AnalizarDeclaradorVariable(boolean bandera) {
        int c = contador;
        if (tiposTokens.get(contador).equals("identifier")) {
            contador++;
            if (tiposTokens.get(contador).equals("=")) {
                contador++;
                if (tiposTokens.get(contador).equals("integer literal") || tiposTokens.get(contador).equals("boolean literal")) {
                    bandera = true;
                }
            } else {
                contador = c;
                bandera = false;
            }
        } else {
            bandera = false;
        }
        return bandera;
    }

    public boolean AnalizarDeclaradorV(boolean bandera) {
        int c = contador;
        if (tiposTokens.get(contador).equals("identifier")) {
            contador++;
            if (tiposTokens.get(contador).equals("=")) {
                contador++;
                if (tiposTokens.get(contador).equals("integer literal") || tiposTokens.get(contador).equals("boolean literal")) {
                    contador++;
                    if (tiposTokens.get(contador).equals(";")) {
                        bandera = true;
                    }
                } else {
                    contador = c;
                    bandera = false;
                }
            } else {
                bandera = false;
            }
        }
        return bandera;
    }

    public boolean AnalizarExpresion(boolean bandera) {
        return AnalizarTestingExpresion(bandera);
    }

    public boolean AnalizarTestingExpresion(boolean bandera) {
        int tipo1 = 0;
        int tipo2 = 0;
        int linea = 0;
        if (tiposTokens.get(contador).equals("integer literal") || tiposTokens.get(contador).equals("identifier")) {
            tipo1 = contador;
            contador++;
            if (tiposTokens.get(contador).equals("relational operator")) {
                linea = tokens.get(contador).getLinea();
                contador++;
                tipo2 =contador;
                bandera = tiposCompatibles(tipo1, tipo2);
            } else {
                bandera = false;
            }
        }
        return bandera;
    }

    public boolean AnalizarStatement(boolean bandera) {
        return (
                AnalizarFieldDeclaracion(bandera) || tiposTokens.get(contador).equals("if") || tiposTokens.get(contador).equals("while")
        );

    }

    public boolean AnalizarWhileStatement(boolean bandera) {
        if (tiposTokens.get(contador).equals("while")) {
            contador++;
            if (tiposTokens.get(contador).equals("open parentheses")) {
                contador++;
                if (AnalizarExpresion(bandera)) {
                    contador++;
                    if (tiposTokens.get(contador).equals("close parentheses")) {
                        contador++;
                        if (tiposTokens.get(contador).equals("open key")) {
                            contador++;
                            if (AnalizarDeclaradorV(bandera)) {
                                contador++;
                                if (tiposTokens.get(contador).equals("close key")) {
                                    bandera = true;
                                }
                            }

                        }
                    }
                }
            }
        }
        return bandera;
    }

    public boolean AnalizarType(boolean bandera) {
        return AnalizarTypeSpecifier(bandera);
    }

    public boolean AnalizarTypeSpecifier(boolean bandera) {
        return tiposTokens.get(contador).equals("boolean literal") || tiposTokens.get(contador).equals("integer literal");
    }

    public boolean AnalizarIfStatement(boolean bandera) {
        if (tiposTokens.get(contador).equals("if")) {
            contador++;
            if (tiposTokens.get(contador).equals("open parentheses")) {
                contador++;
                if (AnalizarExpresion(bandera)) {
                    contador++;
                    if (tiposTokens.get(contador).equals("close parentheses")) {
                        contador++;
                        if (tiposTokens.get(contador).equals("open key")) {
                            contador++;
                            if (AnalizarDeclaradorV(bandera)) {
                                contador++;
                                if (tiposTokens.get(contador).equals("close key")) {
                                    bandera = true;
                                }
                            }

                        }
                    }
                }
            }
        }
        return bandera;
    }

    public boolean AnalizarExpresionAritmetica(boolean bandera) {
        if (tiposTokens.get(contador).equals("identifier")) {
            contador++;
            if (tiposTokens.get(contador).equals("=")) {
                contador++;
                if (tiposTokens.get(contador).equals("integer literal")) {
                    contador++;
                    if (tiposTokens.get(contador).equals("aritmetic operator")) {
                        contador++;
                        if (tiposTokens.get(contador).equals("integer literal")) {
                            contador++;
                            if (tiposTokens.get(contador).equals(";")) {
                                return true;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return bandera;
    }
    
     public static boolean tiposCompatibles(int posicion1,int posicion2){
        String tipo1 = tokens.get(posicion1).getTipo();
        String tipo2 = tokens.get(posicion2).getTipo();

        if(tipo1.equals("identifier")){
            tipo1 = tokens.get(posicion1).getToken();
            
            for (int i = 0; i < tablaSimbolos.size(); i++) {
                if(tablaSimbolos.get(i).getNombre().equals(tipo1)){
                    tipo1 = tablaSimbolos.get(i).getTipo();
 
                }
            }
        }
        
        if(tipo2.equals("identifier")){
            tipo2 = tokens.get(posicion2).getToken();
            
            for (int i = 0; i < tablaSimbolos.size(); i++) {
                if(tablaSimbolos.get(i).getNombre().equals(tipo2)){
                    tipo2 = tablaSimbolos.get(i).getTipo();

                }
            }
        }

        if(tipo1.equals("int")&& tipo2.equals("integer literal")||tipo1.equals("integer literal")&& tipo2.equals("int")||tipo1.equals("int")&& tipo2.equals("int")){
            return true;
        }
        if(tipo1.equals("boolean")&& tipo2.equals("boolean literal")||tipo1.equals("boolean literal")&& tipo2.equals("boolean")||tipo1.equals("boolean")&& tipo2.equals("boolean")){
            return true;
        }
        if(tipo1.equals("boolean")&& tipo2.equals("boolean literal")||tipo1.equals("boolean literal")&& tipo2.equals("boolean")){
            return true;
            
        }if(tipo1.equals("boolean literal")&& tipo2.equals("boolean literal")||tipo1.equals("integer literal")&& tipo2.equals("integer literal")){
            return true;
        }
         System.out.println("");
         System.out.println("Tipos de datos no compatibles en la linea " + getLinea("relational operator") );
        return false;
        }
     
     public static int getLinea(String tipo){
         int posicion = 0;
         for (int i = 0; i < tokens.size(); i++) {
             if(tokens.get(i).getToken().equals("==")){
                 posicion = tokens.get(i).getLinea();
             }
         }
         return posicion;
     }
     
    }
    

