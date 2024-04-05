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
    String identificador = "^([a-zA-Z]+|ñ|Ñ|[áéíóúÁÉÍÓÚ]).*(\n)?$"; 
    String numeros = "^[0-9]+((?:\\.[0-9]+)?\n?)$";
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
        errores = new ArrayList<>(); 
        String[] valores = fileService.datos.split(" ");
        String valorDeWhile = "";
        int caracteresWhile = 0;
        boolean seEstaValidandoCicloWhile = false;
        for(int i=0;i<valores.length; i++){
            System.out.println("valores " + valores[i]);
            if(validarToken(valores[i])){
                // validacion de ciclo while
                if(valores[i].toLowerCase().equals("while") || seEstaValidandoCicloWhile) {
                    seEstaValidandoCicloWhile= true;
                    if(caracteresWhile < 12 && i < valores.length - 1){
                        caracteresWhile++;
                        valorDeWhile += valores[i] + " ";
                     
                    } else {  
                        caracteresWhile = 0;
                        seEstaValidandoCicloWhile = false;
                       
                        if(!valorDeWhile.equals("while ( 1 == 1 ) { int valor = i ; }")){
                            errores.add("Ciclo while inválido: " + valorDeWhile);
                        }
                        valorDeWhile = "";
                    }
                }
                
                // se valida orden de asignación de un valor a identificador
                if(valores[i].equals("=")){
                    if(valores[i-2] != null && valores[i-1] != null && valores[i+1] != null){
                        if(expresionIdentificador.matcher(valores[i-2]).matches() 
                           && valores[i-1].toLowerCase().equals("int") && expresionNumeros.matcher(valores[i+1]).matches()){
                            errores.add("No es el orden esperado para declarar variables: " + valores[i-2] + " " + 
                                valores[i-1] + " " + valores[i] + " " + valores[i+1]);
                        }
                    }
                }
                
                if(expresionIdentificador.matcher(valores[i]).matches() && !palabrasReservadas.contains(valores[i]) && i > 0 && i < valores.length - 1){
           
                    if(valores[i-1] != null && valores[i+1] != null){
                        if(valores[i-1].toLowerCase().equals("int") && expresionNumeros.matcher(valores[i+1]).matches() && 
                                !valores[i+1].equals("=")){
                            errores.add("Se espera un operador para declarar bien la variable: " + valores[i-1] + " " + 
                                valores[i] + " " + valores[i+1]);
                        } else if (valores[i+2] != null) {
                            if(valores[i-1].toLowerCase().equals("int") && valores[i+1].equals("=") && 
                                 !expresionNumeros.matcher(valores[i+2]).matches()){
                                errores.add("Se espera un número como valor de variable: " + valores[i-1] + " " + 
                                valores[i] + " " + valores[i+1] + " " + valores[i+2]);
                            }
                        }
                    }
                }
                
                if(valores[i].toLowerCase().equals("do")){
                    if(i+1 < valores.length - 1){
                        if(valores[i+1] != null && valores[i+2] != null){
                            if(valores[i+1].equals("{") && valores[i+2].equals("}")){
                                errores.add("Se espera una expresión dentro del ciclo: " + valores[i] + " " + 
                                    valores[i+1] + " " + valores[i+2]);
                            }
                        } 
                    } else if (valores[i+1] != null) {
                            if(!valores[i+1].equals("{")){
                                errores.add("Se espera un signo de agrupación: " + valores[i] + " " + 
                                    valores[i+1]);
                            }
                    }
                }
                
                if(valores[i].equals("public")){
                    if(valores[i+1] != null){
                        if(!valores[i+1].toLowerCase().equals("class") || !valores[i+1].equals("int")){
                            errores.add("Se espera una palabra reservada válida luego de public: " + valores[i] + " " + 
                                valores[i+1]);
                        }
                    }
                }
            } else {
                errores.add(valores[i]);
            }
        }
        
        if(errores.isEmpty()){
          JOptionPane.showMessageDialog(null, "Se finalizó el análisis sintáctico, todo está correcto");
        } else {
            String mensaje = "";
          
            for(String error : errores) {
                mensaje = error + "\n";
            }
          
           JOptionPane.showMessageDialog(null, "Hay error en la sintaxis \nLos errores son los siguientes: \n" + mensaje);
        }
    }
    
    public boolean validarToken(String valor) {
        return expresionIdentificador.matcher(valor).matches() || expresionNumeros.matcher(valor).matches() ||
                expresionSimbolos.matcher(valor).matches() || expresionAgrupadores.matcher(valor).matches() || operadores.contains(valor)
                || palabrasReservadas.contains(valor) || valor.equals("//") || valor.equals("/*") || valor.equals("*/");
    }
    
}
