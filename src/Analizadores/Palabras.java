/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

/**
 *
 * @author angelarturo
 */
public  class Palabras {
    String text="";
    String token ="";
    int posicion = 0;
    
    public Palabras(String text,int posicion){
        this.text = text;
        this.posicion = posicion;
    }
}
