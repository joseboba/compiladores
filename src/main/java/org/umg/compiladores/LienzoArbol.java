/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.umg.compiladores;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Javier
 */
public class LienzoArbol extends JPanel {
    
    private ArrayList<ArbolBinarioExp> arboles = new ArrayList<ArbolBinarioExp>();
    public static final int DIAMETRO = 30;
    public static final int RADIO = DIAMETRO/2;
    public static final int ANCHO = 30;
    private int alturaDeEspacioAparicionArbol = 10;
    private static final int ESPACIO_ENTRE_ARBOLES = 100; // Espacio en blanco entre árboles
    
    public void setArbol(ArbolBinarioExp arbol) {
        this.arboles.add(arbol);
        repaint();
    }
    
    public void setAlturaDeEspacioAparicionArbol(int altura){
        this.alturaDeEspacioAparicionArbol = altura;
    }
    
    public int getAlturaDeEspacioAparicionArbol(){
        return alturaDeEspacioAparicionArbol;
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
     
        for(int i=0; i< arboles.size(); i++){
               
            if (arboles.get(i) != null && arboles.get(i).raiz != null) {
                pintar(g, getWidth() / 2, alturaDeEspacioAparicionArbol, arboles.get(i).raiz, arboles.get(i));
                alturaDeEspacioAparicionArbol += (i+ 2) * ESPACIO_ENTRE_ARBOLES;
            }
        }
       
    }
    
    public void pintar(Graphics g, int x, int y, NodoArbol subArbol, ArbolBinarioExp arbol){
        if(subArbol != null){
            int extra = arbol.nodosCompletos(subArbol)*ANCHO/2;
            int posicionX = 0;
            
            g.drawOval(x, y, DIAMETRO, DIAMETRO);
            if(subArbol.dato.toString().length() >= 2 ){
                posicionX = x + 10;
                
            } else {
                posicionX = x + 12;
             
            }
            
            g.drawString(subArbol.dato.toString(), posicionX , y + 18);
            
            if(subArbol.izquierdo != null){
                g.drawLine(x, y + RADIO, x + RADIO - ANCHO - extra, y + ANCHO);
            } 
            
            if(subArbol.derecho != null){
                g.drawLine(x + DIAMETRO, y + RADIO, x + RADIO + ANCHO + extra, y + ANCHO);
            }
            
            pintar(g, x - ANCHO - extra, y + ANCHO, subArbol.izquierdo, arbol);
            pintar(g, x + ANCHO + extra, y + ANCHO, subArbol.derecho, arbol);
            
        } 
        
        
    }
    
}
