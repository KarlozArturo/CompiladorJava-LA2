/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

import java.util.ArrayList;

/**
 *
 * @author angelarturo
 */
public class TablaDeSimbolos {
    ArrayList<Simbolos> tabla = new ArrayList<>();
    public TablaDeSimbolos(){}
    
    public void crearEntrada(String nombre,String tipo,String valor,int posicion){
        String subSt = "";
        Simbolos e = buscarNombre(nombre);
        if(e==null){
            e = new Simbolos(nombre, tipo,valor,posicion);
            tabla.add(e);
        }
    }
    
    public String getTipo(String nombre){
        Simbolos e = buscarNombre(nombre);
        if(e ==null){
            System.out.println("variable no definida");
        }
        return e.getTipo();
    }
    public String getValor(String nombre){
        Simbolos e = buscarNombre(nombre);
        if(e ==null){
            System.out.println("variable no definida");
        }
        return e.getValor();
    }
    
    public Simbolos buscarNombre(String nombre){
        Simbolos e = null;
        int i = 0;
        while (i<tabla.size()){
            e=(Simbolos)tabla.get(i);
            if(e.getNombre().equals(nombre))break;
            e=null;
            i++;
        }
        return e;
    }
    
    public void getTabla(){
        System.out.println("");
        System.out.println("TABLA DE SIMBOLOS");
        System.out.println("NOMBRE"+" \t"+"TIPO "+" \t"+"LINEA"+" \t"+"VALOR");
        for (int i = 0; i < tabla.size(); i++) {
            System.out.println(tabla.get(i).nombre+" \t"+tabla.get(i).getTipo()
                    +" \t "+tabla.get(i).getValor()+" \t "+tabla.get(i).getPosicion());
        }
    }
}
