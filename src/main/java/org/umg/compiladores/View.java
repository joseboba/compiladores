package org.umg.compiladores;

import org.umg.compiladores.dto.ClassifyTokenDTO;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class View {

    private final FileService fileService;
    String[] headers = {"Lexema", "Tipo", "Línea"};
    String identificador = "^[a-zA-Z]+|(ñ)+|(Ñ)+|([áéíóúÁÉÍÓÚ])+$"; 
    String numeros = "^[0-9]+(?:\\.[0-9]+)?$";
    String simbolos = "^([!@#$%^&:;,.~¡¿?'\\\\])+\\n?$";
    String agrupadores = "^([{}()\\[\\]]+)(\n?)$";
    List<String> operadores = Arrays.asList("+","-","*","/","-","++","==","--","=");
    List<String> palabrasReservadas = Arrays.asList("if","else","for","while","do","int","class", "public");
    Pattern expresionIdentificador = Pattern.compile(identificador);
    Pattern expresionNumeros = Pattern.compile(numeros);
    Pattern expresionSimbolos = Pattern.compile(simbolos);
    Pattern expresionAgrupadores = Pattern.compile(agrupadores);
    List<String> errores = new ArrayList<>(); 

    public View(FileService fileService) {
        this.fileService = fileService;
    }

    public void showResult(List<ClassifyTokenDTO> classifyTokenDTOS) {
        var data = formatData(classifyTokenDTOS);
        var table = new JTable(data, headers);
        var scrollPane = new JScrollPane(table);

        var frame = new JFrame("Resultado");
        frame.setSize(600, 400);
        frame.add(scrollPane);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileService.cleanHasMap();
                analizadorSintactico();
            }
        });
    }

    private Object[][] formatData(List<ClassifyTokenDTO> dataList) {
        var data = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            var inlineData = dataList.get(i);
            data[i] = new Object[]{inlineData.getText(), inlineData.getToken(), inlineData.getLine()};
        }

        return data;
    }

    public void analizadorSintactico(){
        errores.clear(); 
        String[] valores = fileService.datos.split(" ");
        String valorDeWhile = "";
        int caracteresWhile = 0;
        boolean seEstaValidandoCicloWhile = false;
        for(int i=0;i<valores.length; i++){
 
            if(validarToken(valores[i])){
                // validacion de ciclo while
                if(valores[i].toLowerCase().equals("while") || seEstaValidandoCicloWhile) {
                    seEstaValidandoCicloWhile= true;
                    if(caracteresWhile <= 12){
                        caracteresWhile++;
                        valorDeWhile += valores[i] + " ";
                     
                    } else {  
                        caracteresWhile = 0;
                        seEstaValidandoCicloWhile = false;
                       
                        if(!valorDeWhile.equals("while ( 1 == 1 ) { int valor = 2 ; }")){
                            errores.add("Ciclo while inválido: " + valorDeWhile);
                            System.out.println("Valor" + valorDeWhile);
                        }
                        valorDeWhile = "";
                    }
                }
                
                // se valida orden de asignación de un valor a identificador
                if(valores[i].equals("=")){
                    if(valores[i-2] != null && valores[i-1] != null && valores[i+1] != null){
                        if(expresionIdentificador.matcher(valores[i-2]).matches() 
                           && valores[i-1].toLowerCase().equals("int") 
                           && expresionNumeros.matcher(valores[i+1]).matches()){
                            errores.add("No es el orden esperado para declarar variables: " + valores[i-2] + " " + 
                                valores[i-1] + " " + valores[i] + " " + valores[i+1]);
                        }
                    }
                }
                
                
                // se valida se espera un operador y se espera un número en expresión int identificador = número
                if(expresionIdentificador.matcher(valores[i]).matches() 
                        && !palabrasReservadas.contains(valores[i])
                        && i > 0 && i < valores.length - 1){
                  
                    if(valores[i-1] != null && valores[i+1] != null){
                        if(valores[i-1].toLowerCase().equals("int") 
                                && expresionNumeros.matcher(valores[i+1]).matches() && 
                                !valores[i+1].equals("=")){
                            errores.add("Se espera un operador para declarar bien la variable: " + valores[i-1] + " " + 
                                valores[i] + " " + valores[i+1]);
                        } else if (i + 2 < valores.length - 1 ) {
                            if(valores[i-1].toLowerCase().equals("int") && valores[i+1].equals("=") && 
                                 !expresionNumeros.matcher(valores[i+2]).matches()){
                                errores.add("Se espera un número como valor de variable: " + valores[i-1] + " " + 
                                valores[i] + " " + valores[i+1] + " " + valores[i+2]);
                            }
                        }
                    }
                }
                
                
                // se valida se espera una expresión y se espera un signo de agrupación
                if(valores[i].toLowerCase().equals("do")){
                    if(i+1 < valores.length - 1){
                        if(valores[i+1] != null && valores[i+2] != null){
                            if(valores[i+1].equals("{") && valores[i+2].equals("}")){
                                errores.add("Se espera una expresión dentro del ciclo: " 
                                        + valores[i] + " " + 
                                    valores[i+1] + " " + valores[i+2]);
                            } else if (!valores[i+1].equals("{")) {
                                errores.add("Se espera un signo de agrupación: " + valores[i] + " " + 
                                    valores[i+1] + " ");
                            }
                        } 
                    } else if (valores[i+1] != null) {
                            if(!valores[i+1].equals("{")){
                                errores.add("Se espera un signo de agrupación: " + valores[i] + " " + 
                                    valores[i+1]);
                            }
                    }
                }
                
                /// se valida palabra reservada
                if(valores[i].equals("public")){
                    if(valores[i+1] != null){
                        if(!valores[i+1].toLowerCase().equals("class") 
                                || !valores[i+1].equals("int")){
                            errores.add("Se espera una palabra reservada válida luego de public: " 
                                    + valores[i] + " " + 
                                valores[i+1]);
                        }
                    }
                }
                
                // se valida condición
                if(valores[i].equals("==") && i < valores.length - 1 && i > 0){
                    if(valores[i-1] != null && valores[i+1] != null){
                        if(!(expresionNumeros.matcher(valores[i-1]).matches() 
                                && expresionNumeros.matcher(valores[i+1]).matches())
                           && !(expresionIdentificador.matcher(valores[i-1]).matches() 
                                && expresionIdentificador.matcher(valores[i+1]).matches())
                           && !(expresionAgrupadores.matcher(valores[i-1]).matches() 
                                && expresionAgrupadores.matcher(valores[i+1]).matches())
                           && !(expresionSimbolos.matcher(valores[i-1]).matches() 
                                && expresionSimbolos.matcher(valores[i+1]).matches())
                           && !(palabrasReservadas.contains(valores[i-1]) 
                                && palabrasReservadas.contains(valores[i+1]))){
                           errores.add("Se espera una condición con tokens del mismo tipo: " 
                                   + valores[i-1] + " " + valores[i] + " " + valores[i+1] );
                        }
                    }
                }
                
                // se valida uso de expresión else
                if(valores[i].equals("else") && i > 0 && i < valores.length - 1){
                    if(valores[i-1] != null && valores[i+1] != null) {
                        if(!valores[i-1].equals("}") || !valores[i].equals("else") 
                                || !valores[i+1].equals("{") ){
                            errores.add("Uso de palabra else incorrecto, debe usarse como } else { se uso así: " 
                                    + valores[i-1] + " " + valores[i] + " " + valores[i+1]);
                        }
                    }
                } else if(valores[i].equals("else")) {
                    errores.add("Uso de palabra else incorrecto, debe usarse como } else { se uso así: " + valores[i]);
                }
                
                // validación de expresión aritmética
                if(operadores.contains(valores[i]) && i < valores.length 
                        && !valores[i].equals("=") 
                        && !valores[i].equals("==") ){
                 
                    if(!expresionNumeros.matcher(valores[i-1]).matches() || 
                       !expresionNumeros.matcher(valores[i+1]).matches()){
                        errores.add("Expresión aritmética incorrecta : " 
                                    + valores[i-1] + " " + valores[i] + " " + valores[i+1]);
                    }
                } else if (operadores.contains(valores[i]) && !valores[i].equals("=") && 
                        !valores[i].equals("==") ){
                    errores.add("Expresión aritmética incorrecta : " 
                                    + valores[i-1] + " " + valores[i]);
                }
                
                // validación inicio de if
                if(valores[i].equals("if") && i < valores.length) {
                    if(!valores[i+1].equals("(")){
                        errores.add("Inicio de declaración if incorrecta : " 
                            + valores[i] + " " + valores[i+1]);
                    }
                }
            } else {
                if(!valores[i].trim().equals(" ") && !valores[i].equals("") && !valores[i].equals("\n")){
                    errores.add(valores[i]);
                }
            }
        }
        
        if(errores.isEmpty()){
          JOptionPane.showMessageDialog(null, "Se finalizó el análisis sintáctico, todo está correcto");
        } else {
            String mensaje = "";
          
            for(int i=0; i<errores.size(); i++) {
                mensaje = mensaje + errores.get(i) + "\n";
            }
           JOptionPane.showMessageDialog(null, "Hay error en la sintaxis \nLos errores son los siguientes: \n" + mensaje);
        }
        
        errores.clear();
    }
    
    public boolean validarToken(String valor) {
        return expresionIdentificador.matcher(valor).matches() 
                || expresionNumeros.matcher(valor).matches() 
                || expresionSimbolos.matcher(valor).matches() 
                || expresionAgrupadores.matcher(valor).matches() 
                || operadores.contains(valor)
                || palabrasReservadas.contains(valor) 
                || valor.equals("//") || valor.equals("/*") || valor.equals("*/");
    }
    
}
