package Analizadores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sintactico {

    private final ArrayList<String> tiposTokens;
    private final ArrayList<Token> tokens;
    private int contador = 0;
    private boolean bandera;

    public Sintactico(ArrayList<String> tt,ArrayList<Token> ts) {
        tiposTokens = tt;
        tokens =ts;
        analizar();
    }

    private void analizar() {

        if (AnalizarClase(bandera)) {
            System.out.println("El código ha compilado con éxito.");
        }
        else {
            System.out.println("Compilacion terminada.");
        }
    }

        public boolean AnalizarClase(boolean bandera) {
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
        return bandera;
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
        if (tiposTokens.get(contador).equals("integer literal") || tiposTokens.get(contador).equals("identifier")) {
            contador++;
            if (tiposTokens.get(contador).equals("relational operator")) {
                contador++;
                bandera = tiposTokens.get(contador).equals("integer literal") || tiposTokens.get(contador).equals("identifier");
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
    
    /* public int getErrorLine(String tipo) {
       el caso para el modificadores seria la linea donde este la primera palabra, y en los otros casos:
        para la clase,donde este el modificador, en la linea donde este el modificador
        para el indentificador, la linea donde este la clase
        y para el corchete, la linea donde este el identificador: 
        
        int count = 0;
        int linea = 0;
        switch(tipo){
                case ";":
                {
                    while(tokens.get(count).getTipo()!=";"){
                    linea = tokens.get(count).getLinea();

            count++;
                }
        }
               
        }
        return linea;
    }*/
}
