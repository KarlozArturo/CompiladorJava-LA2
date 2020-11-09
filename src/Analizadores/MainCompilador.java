package Analizadores;

import java.io.IOException;
import java.util.ArrayList;

public class MainCompilador {

    public static ArrayList<Token> tokens;
    public static ArrayList<Simbolos> tablaSimbolos = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        LeerArchivos arch = new LeerArchivos("test");
        Lexico analizadorLexico = new Lexico();
        tokens = new ArrayList<>();
        String linea = "";
        int l = 0;

        while (linea != null) {
            l++;
            linea = arch.leerSigLinea();
            analizadorLexico.analizarLinea(linea, l, 0);

        }
        recorrerTokens();
        new Sintactico(obtenerTipos(), tokens, tablaSimbolos);

        TablaSimbolos t = new TablaSimbolos(tablaSimbolos);

    }

    private static ArrayList<String> obtenerTipos() {
        ArrayList<String> al = new ArrayList<>();
        for (Token token : tokens)
            al.add(token.getTipo());
        return al;
    }

    public static void agregarToken(String palabra, boolean pres, String tipo, int l, int c) {
        tokens.add(new Token(palabra, pres, tipo, l, c));
    }

    public static void recorrerTokens() {
        Simbolos s;
        boolean compatible;

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getTipo().equals("identifier")) {
                String identificador = tokens.get(i).getToken();
                // primero vamos a ver si ya existe previamente la variable en la tabla

                int nTablaSimbolos = encontrarVariable(identificador);
                // y si esa variable no se encuentra repetida con un type de dato
                if (nTablaSimbolos == 0) {
                    if (tokens.get(i - 1).getTipo().equals("type")) {

                        if (tokens.get(i + 2).getTipo().equals("boolean literal")
                                || tokens.get(i + 2).getTipo().equals("integer literal")
                                || tokens.get(i + 1).getTipo().equals(";")) {

                            if (tokens.get(i + 1).getTipo().equals(";")) {
                                s = new Simbolos(tokens.get(i).getToken(), tokens.get(i - 1).getToken(), "Empty",
                                        tokens.get(i - 1).getLinea());
                                tablaSimbolos.add(s);

                            } else {
                                compatible = verficiarDeclaracion(tokens.get(i).getToken(),
                                        tokens.get(i - 1).getToken(), tokens.get(i + 2).getToken(),
                                        tokens.get(i - 1).getLinea());
                                if (compatible) {
                                    s = new Simbolos(tokens.get(i).getToken(), tokens.get(i - 1).getToken(),
                                            tokens.get(i + 2).getToken(), tokens.get(i - 1).getLinea());
                                    tablaSimbolos.add(s);
                                } else {
                                    break;
                                }
                            }

                        }
                    }
                } else {
                    // si la variable ya existe en la tablaDeSimbolos
                    if (tokens.get(i - 1).getTipo().equals("type")) {
                        System.out.println("La variable " + tokens.get(i).getToken() + " en la linea "
                                + tokens.get(i).getLinea() + " se encunentra repetida");

                    } else if (tokens.get(i + 1).getToken().equals("=")) {
                        compatible = verficiarDeclaracion(tokens.get(i).getToken(), tokens.get(i - 1).getToken(),
                                tokens.get(i + 2).getToken(), tokens.get(i - 1).getLinea());
                        if (compatible) {
                            cuadruplos(getLineOpearacion(i + 1), nTablaSimbolos,
                                    tablaSimbolos.get(nTablaSimbolos).getNombre());
                        } else {
                            break;
                        }
                    }

                }
            }
        }
    }

    public static int encontrarVariable(String nombre) {
        int exists = 0;
        for (int i = 0; i < tablaSimbolos.size(); i++) {
            if (tablaSimbolos.get(i).getNombre().equals(nombre)) {
                exists = i;
                break;
            }

        }

        return exists;
    }

    public static String getValorVariable(String nombre) {
        String valor = "";
        for (int i = 0; i < tablaSimbolos.size(); i++) {
            if (tablaSimbolos.get(i).getNombre().equals(nombre)) {
                valor = tablaSimbolos.get(i).getValor();
            }
        }
        return valor;
    }

    public static boolean verficiarDeclaracion(String nombre, String tipo, String valor, int linea) {
        boolean compatible = true;
        if (tipo.equals("int") && !isNumeric(valor)) {
            System.out.println(nombre + " es de tipo " + tipo + " y no se le puedo asignar el valor " + valor);
            compatible = false;
            System.out.println(compatible);
        }

        if (tipo.equals("boolean")) {
            if (!valor.equals("false") && !valor.equals("true")) {
                System.out.println(nombre + " es de tipo " + tipo + " y no se le puedo asignar el valor " + valor);
                compatible = false;
            }
        }

        return compatible;
    }

    public static void isInitialize(String nombre, int linea) {
        boolean inicializada = true;
        for (int i = 0; i < tablaSimbolos.size(); i++) {
            if (tablaSimbolos.get(i).getNombre().equals(nombre)) {
                if (tablaSimbolos.get(i).getValor().equals("Empty")) {
                    inicializada = false;
                    System.out.println(
                            "La variable " + nombre + " en la linea " + linea + " no se encuentra inicializada");
                    System.exit(0);
                }
            }
        }

    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void cuadruplos(String linea, int posicionTablaSim, String nombreVariable) {
        String[] caracteres = linea.split("");
        // los pasamos a un arraylist para manejarlo con mayor facilidad
        ArrayList<Operandos> variables = new ArrayList<>();
        Operandos o;
        for (String c : caracteres) {
            // si hay variables en los operandos, buscamos su valor en la tabla de simbolos
            String valor = getValorVariable(c);
            if (valor.equals("")) {
                valor = c;
            }
            o = new Operandos(c, valor);
            variables.add(o);

        }

        int contadorTemporales = 0;
        int arturoTemporal = 0;
        // recorrer el array para encontrar los operadores con mayor prioridad
        System.out.println();
        System.out.println("Operador" + "\t" + "Operando1" + "\t" + "Operando2" + "\t" + "Resultado" + "\t");
        // primero busca los parentesis
        for (int j = 0; j < variables.size(); j++) {
            if (variables.get(j).getNombre().equals("(")) {

                // ahora va a recorrer lo que esta dentro del parentesis
                for (int i = j + 1; i < variables.size(); i++) {
                    // Por si encuentra una multiplicacion o division
                    if (variables.get(i).getNombre().equals("*") || variables.get(i).getNombre().equals("/")) {
                        if (variables.get(i - 1).getNombre().equals(")")
                                || variables.get(i + 1).getNombre().equals(")")) {
                            break; // este if con break es en caso de uno de los operandos sea un parentesis que
                                   // cierra, indicando que las operaciones dentro del parentsis terminaron
                        }
                        contadorTemporales++;
                        System.out.println(variables.get(i).getNombre() + "\t\t" + variables.get(i - 1).getNombre()
                                + "\t\t" + variables.get(i + 1).getNombre() + "\t\t" + "Arturo" + contadorTemporales);

                        if (variables.get(i).getNombre().equals("*")) {
                            arturoTemporal = Integer.parseInt(variables.get(i - 1).getValor())
                                    * Integer.parseInt(variables.get(i + 1).getValor());
                        } else {
                            arturoTemporal = Integer.parseInt(variables.get(i - 1).getValor())
                                    / Integer.parseInt(variables.get(i + 1).getValor());
                        }
                        String temporal = Integer.toString(arturoTemporal);
                        variables.get(i + 1).setValor(temporal);
                        variables.get(i + 1).setNombre("Arturo" + contadorTemporales);
                        variables.remove(i);
                        variables.remove(i - 1);
                        i = j;

                    }

                }

                for (int i = j + 1; i < variables.size(); i++) {

                    if (variables.get(i).getNombre().equals("+") || variables.get(i).getNombre().equals("-")) {
                        if (variables.get(i - 1).getNombre().equals(")")
                                || variables.get(i + 1).getNombre().equals(")")) {
                            break;
                        }
                        contadorTemporales++;
                        System.out.println(variables.get(i).getNombre() + "\t\t" + variables.get(i - 1).getNombre()
                                + "\t\t" + variables.get(i + 1).getNombre() + "\t\t" + "Arturo" + contadorTemporales);

                        if (variables.get(i).getNombre().equals("+")) {
                            arturoTemporal = Integer.parseInt(variables.get(i - 1).getValor())
                                    + Integer.parseInt(variables.get(i + 1).getValor());
                        } else {
                            arturoTemporal = Integer.parseInt(variables.get(i - 1).getValor())
                                    - Integer.parseInt(variables.get(i + 1).getValor());
                        }
                        String temporal = Integer.toString(arturoTemporal);
                        variables.get(i + 1).setValor(temporal);
                        variables.get(i + 1).setNombre("Arturo" + contadorTemporales);
                        variables.remove(i);
                        variables.remove(i - 1);
                        i = j;

                    }

                }

            }
        }

        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).getNombre().equals("(") || variables.get(i).getNombre().equals(")")) {
                variables.remove(i);
            }
        }

        // para todo lo que este fuera de parentesis
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).getNombre().equals("*") || variables.get(i).getNombre().equals("/")) {
                contadorTemporales++;
                System.out.println(variables.get(i).getNombre() + "\t\t" + variables.get(i - 1).getNombre() + "\t\t"
                        + variables.get(i + 1).getNombre() + "\t\t" + "Arturo" + contadorTemporales);

                if (variables.get(i).getNombre().equals("*")) {
                    arturoTemporal = Integer.parseInt(variables.get(i - 1).getValor())
                            * Integer.parseInt(variables.get(i + 1).getValor());
                } else {
                    arturoTemporal = Integer.parseInt(variables.get(i - 1).getValor())
                            / Integer.parseInt(variables.get(i + 1).getValor());
                }
                String temporal = Integer.toString(arturoTemporal);

                // cambiamos el valor en esa posicion por el resultado de la operacion, y
                // eliminamos el simbolo y el otro operador
                variables.get(i + 1).setValor(temporal);
                variables.get(i + 1).setNombre("Arturo" + contadorTemporales);
                variables.remove(i);
                variables.remove(i - 1);
                i = -1;// para que regrese al inicio del array por que si no terminaria el ciclo
                       // despues de haber eliminado las "variables" anteriores
            }
        }

        // recorrido para sumas y restas
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).getNombre().equals("+") || variables.get(i).getNombre().equals("-")) {
                contadorTemporales++;
                if (variables.size() == 3) {// es 3 por que cuando hace la ultima operacion siempre son 3 elementos,
                                            // ambos operandos y el operador
                    System.out.println(variables.get(i).getNombre() + "\t\t" + variables.get(i - 1).getNombre() + "\t\t"
                            + variables.get(i + 1).getNombre() + "\t\t" + "Arturo" + contadorTemporales);
                } else
                    System.out.println(variables.get(i).getNombre() + "\t\t" + variables.get(i - 1).getNombre() + "\t\t"
                            + variables.get(i + 1).getNombre() + "\t\t" + "Arturo" + contadorTemporales);

                if (variables.get(i).getNombre().equals("+")) {
                    arturoTemporal = Integer.parseInt(variables.get(i - 1).getValor())
                            + Integer.parseInt(variables.get(i + 1).getValor());
                } else {
                    arturoTemporal = Integer.parseInt(variables.get(i - 1).getValor())
                            - Integer.parseInt(variables.get(i + 1).getValor());
                }
                String temporal = Integer.toString(arturoTemporal);

                // cambiamos el valor en esa posicion por el resultado de la operacion, y
                // eliminamos el simbolo y el otro operador
                variables.get(i + 1).setValor(temporal);
                variables.get(i + 1).setNombre("Arturo" + contadorTemporales);
                variables.remove(i);
                variables.remove(i - 1);
                i = -1;// para que regrese al inicio del array por que si no terminaria el ciclo
                       // despues de haber eliminado las "variables" anteriores
            }

        }
        tablaSimbolos.get(posicionTablaSim).setValor(Integer.toString(arturoTemporal));
        System.out.println("Arturo" + contadorTemporales + " = " + arturoTemporal);
        System.out.println(tablaSimbolos.get(posicionTablaSim).getNombre() + " = " + "Arturo" + contadorTemporales);
    }

    public static String getLineOpearacion(int posicionTokenIgual) {
        // usaremos el igual como punto de partida hasta llegar al ; para obtener la
        // cadena
        // de la expresion
        String expresion = "";
        int i = posicionTokenIgual + 1;// mas 1 para no incluir el igual
        while (!tokens.get(i).getTipo().equals(";")) {

            expresion += tokens.get(i).getToken();
            i++;
        }

        return expresion;
    }

}
