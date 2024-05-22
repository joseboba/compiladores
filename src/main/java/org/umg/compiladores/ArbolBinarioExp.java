/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.umg.compiladores;

/**
 *
 * @author Javier
 */

public class ArbolBinarioExp {
    
    NodoArbol raiz;
    
    public ArbolBinarioExp(){
        raiz = null;
    }
    
    public ArbolBinarioExp(String cadena){
        raiz = creaArbolBE(cadena);
    }
    
    public void reiniciaArbol(){
        raiz = null;
    }
    
    public void creaNodo(Object dato){
        raiz = new NodoArbol(dato);
    }
    
    public NodoArbol creaSubArbol(NodoArbol dato2, NodoArbol dato1, NodoArbol operador){
        operador.izquierdo = dato1;
        operador.derecho = dato2;
        
        return operador;
    }
    
    public boolean arbolVacio(){
        return raiz == null;
    }
    
    public int nodosCompletos(NodoArbol subArbol){
        if(subArbol == null){
            return 0;
        } else {
            if(subArbol.izquierdo != null && subArbol.derecho != null){
                return nodosCompletos(subArbol.izquierdo) + nodosCompletos(subArbol.derecho) + 1;
            }
            
            return nodosCompletos(subArbol.izquierdo) + nodosCompletos(subArbol.derecho);
        }
    }
  
    private int prioridad(char c){
        int p=100;
        
        switch(c) {
            case '^':
                p=30;
                break;
            case '*':
            case '/':
                p=20;
                break;
            case '+':
            case '-':
                p=10;
                break;
            case '=':
                p=5;  // Baja prioridad para el operador '='
                break;
            default:
                p=0;
                break;
        }
        
        return p;
    }
    
    private boolean esOperador(char c) {
        return "()*^/+->=".indexOf(c) >= 0;
    }
    
    private boolean esLetra(char c) {
        return Character.isLetter(c);
    }
    
    private NodoArbol creaArbolBE(String cadena){
        PilaArbolExp pilaOperadores = new PilaArbolExp();
        PilaArbolExp pilaExpresiones = new PilaArbolExp();
        NodoArbol token;
        NodoArbol op1;
        NodoArbol op2;
        NodoArbol op;
        boolean seRecibeOperando = false;
        char caracterEvaluado;
        
        for(int i=0; i<cadena.length(); i++){
            caracterEvaluado = cadena.charAt(i);
            token = new NodoArbol(caracterEvaluado);
            
            if(!esOperador(caracterEvaluado)){
                if(!seRecibeOperando){
                    seRecibeOperando = true;
                    pilaExpresiones.insertar(token);
                } else {
                    String aux = pilaExpresiones.quitar().dato.toString();
                    aux = aux + caracterEvaluado;
                    token = new NodoArbol(aux);
                    pilaExpresiones.insertar(token);
                }
            } else { //es operador
                seRecibeOperando = false;
                switch(caracterEvaluado){
                    case '(':
                        pilaOperadores.insertar(token);
                        break;
                    case ')':
                        while(!pilaOperadores.pilaVacia() && !pilaOperadores.topePila().dato.equals('(')){
                            op2 = pilaExpresiones.quitar();
                            op1 = pilaExpresiones.quitar();
                            op = pilaOperadores.quitar();
                            op = creaSubArbol(op2, op1, op);
                            pilaExpresiones.insertar(op);
                        }
                        pilaOperadores.quitar();
                        break;
                    default:
                        while(!pilaOperadores.pilaVacia() && prioridad(caracterEvaluado) 
                                <= prioridad(pilaOperadores.topePila().dato.toString().charAt(0))){
                            op2 = pilaExpresiones.quitar();
                            op1 = pilaExpresiones.quitar();
                            op = pilaOperadores.quitar();
                            op = creaSubArbol(op2, op1, op);
                            pilaExpresiones.insertar(op);
                        }
                        pilaOperadores.insertar(token);
                }
            }
        }
        
        while(!pilaOperadores.pilaVacia()){
            op2 = pilaExpresiones.quitar();
            op1 = pilaExpresiones.quitar();
            op = pilaOperadores.quitar();
            op = creaSubArbol(op2, op1, op);
            pilaExpresiones.insertar(op);
        }
        
        op = pilaExpresiones.quitar();
        
        return op;
    }
    
    public double evaluaExpresion(){
        return evalua(raiz);
    }
    
    private double evalua(NodoArbol subArbol){
        double acum = 0;
        
        if(!esOperador(subArbol.dato.toString().charAt(0))){
            if(esLetra(subArbol.dato.toString().charAt(0))){
                throw new UnsupportedOperationException("No se puede evaluar una expresión con variables no definidas.");
            }
            return Double.parseDouble(subArbol.dato.toString());
        } else {
            switch (subArbol.dato.toString().charAt(0)) {
                case '^':
                    acum = Math.pow(evalua(subArbol.izquierdo), evalua(subArbol.derecho));
                    break;
                case '*':
                    acum = evalua(subArbol.izquierdo) * evalua(subArbol.derecho);
                    break;
                case '/':
                    acum = evalua(subArbol.izquierdo) / evalua(subArbol.derecho);
                    break;
                case '+':
                    acum = evalua(subArbol.izquierdo) + evalua(subArbol.derecho);
                    break;
                case '-':
                    acum = evalua(subArbol.izquierdo) - evalua(subArbol.derecho);
                    break;
                case '=':
                    throw new UnsupportedOperationException("El operador '=' no es evaluable en esta implementación.");
            }
        }
        
        return acum;
    }
}