/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.umg.compiladores;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Javier
 */
public class LienzoArbol extends JPanel {
    
    private ArbolBinarioExp arbol;
    public static final int DIAMETRO = 30;
    public static final int RADIO = DIAMETRO/2;
    public static final int ANCHO = 30;
    
    public void setArbol(ArbolBinarioExp arbol) {
        this.arbol = arbol;
        repaint();
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        pintar(g,getWidth()/2, 20, arbol.raiz);
    }
    
    public void pintar(Graphics g, int x, int y, NodoArbol subArbol){
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
            
            pintar(g, x - ANCHO - extra, y + ANCHO, subArbol.izquierdo);
            pintar(g, x + ANCHO + extra, y + ANCHO, subArbol.derecho);
        }
        
        
    }
    
}
