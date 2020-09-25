package Analizadores;

import java.util.ArrayList;

public class Sintactico {

    private final ArrayList<String> tiposTokens;
    private int contador = 0;
    private boolean bandera;

    public Sintactico(ArrayList<String> tt) {
        tiposTokens = tt;
        analizar();
    }

    private void analizar() {

        if (AnalizarClase(bandera)) {
            System.out.println("El código ha compilado con éxito.");
        }
        else {
            System.out.println("ERROR: Al crear la clase. Hay un error Sintáctico.");
        }
    }

    public boolean AnalizarClase(boolean bandera) {
        if (tiposTokens.get(contador).equals("modifier"))
            contador++;
        if (tiposTokens.get(contador).equals("class")) {
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
}
