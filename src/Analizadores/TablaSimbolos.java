/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analizadores;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author angelarturo
 */
public class TablaSimbolos extends JFrame {
    
    ArrayList <Simbolos> arraySimbolos;
    String nombre,tipo,valor, posicion;
    public TablaSimbolos(ArrayList <Simbolos> arraySimbolos){
        this.arraySimbolos = arraySimbolos;
        DefaultTableModel mod = new DefaultTableModel();
        mod.addColumn("Nombre");
        mod.addColumn("Tipo");
        mod.addColumn("Valor");
        mod.addColumn("Posicion");
        String encabezado[]={"NOMBRE","TIPO","VALOR","POSICION"};
        mod.addRow(encabezado);
        for (int i = 0; i<arraySimbolos.size() ; i++) {
            nombre = arraySimbolos.get(i).getNombre();
            tipo = arraySimbolos.get(i).getTipo();
            valor = arraySimbolos.get(i).getValor();
            posicion = String.valueOf(arraySimbolos.get(i).getPosicion());
            String p []= {nombre,tipo,valor,posicion};
            mod.addRow(p);
        }
        JTable ts = new JTable(mod); 
        setTitle("Tabla de simbolos");
        ts.setBounds(12,22,600,600);  
        setSize(600,600);
        add(ts);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
    }

    }

