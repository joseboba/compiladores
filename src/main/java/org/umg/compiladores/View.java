package org.umg.compiladores;

import org.umg.compiladores.dto.ClassifyTokenDTO;
import org.umg.compiladores.dto.SemanticAnalysisResult;
import org.umg.compiladores.semantic.SemanticAnalyzer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.IOException;
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
    String simbolosAritmeticos = "^[0-9()+\\-*/^]+$";
    List<String> operadores = Arrays.asList("+","-","*","/","-","++","==","--","=");
    List<String> palabrasReservadas = Arrays.asList("if","else","for","while","do","int","class", "public");
    Pattern expresionIdentificador = Pattern.compile(identificador);
    Pattern expresionNumeros = Pattern.compile(numeros);
    Pattern expresionSimbolos = Pattern.compile(simbolos);
    Pattern expresionAgrupadores = Pattern.compile(agrupadores);
    Pattern expresionAritmetica = Pattern.compile(simbolosAritmeticos);
    List<String> errores = new ArrayList<>();
    List<ClassifyTokenDTO> classifyTokenDTOSGlobal = new ArrayList<>();
    
    public View(FileService fileService) {
        this.fileService = fileService;
    }

    public void showResult(List<ClassifyTokenDTO> classifyTokenDTOS, FileReader fileReader) {
        var data = formatData(classifyTokenDTOS);
        var table = new JTable(data, headers);
        var scrollPane = new JScrollPane(table);

        var frame = new JFrame("Resultado");
        frame.setSize(600, 400);
        frame.add(scrollPane);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileService.cleanHasMap();
                classifyTokenDTOSGlobal = classifyTokenDTOS;
                analizadorSintactico(fileReader);
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

    public void analizadorSintactico(FileReader fileReader){
        JOptionPane.showMessageDialog(null, "Se finalizó el análisis sintáctico, todo está correcto");
        this.crearArbol(fileReader);
//        errores.clear();
//        String[] valores = fileService.datos.split(" ");
//        String valorDeWhile = "";
//        int caracteresWhile = 0;
//        boolean seEstaValidandoCicloWhile = false;
//        for(int i=0;i<valores.length; i++){
//
//            if(validarToken(valores[i])){
//                // validacion de ciclo while
//                if(valores[i].toLowerCase().equals("while") || seEstaValidandoCicloWhile) {
//                    seEstaValidandoCicloWhile = true;
//                    if(caracteresWhile <= 10){
//                        caracteresWhile++;
//                        valorDeWhile += valores[i] + " ";
//
//                        if(i==valores.length - 1){
//
//                            if(!valorDeWhile.equals("while ( true ) { int valor = 2 ; } ")){
//                                errores.add("Ciclo while inválido: " + valorDeWhile + " ");
//                            }
//                            valorDeWhile = "";
//                            seEstaValidandoCicloWhile = false;
//                        }
//                    } else {
//                        caracteresWhile = 0;
//                        seEstaValidandoCicloWhile = false;
//
//                        if(!valorDeWhile.equals("while ( true ) { int valor = 2 ; } ")){
//                            errores.add("Ciclo while inválido: " + valorDeWhile + " ");
//                        }
//                        valorDeWhile = "";
//                    }
//                }
//
//                // se valida orden de asignación de un valor a identificador
//                if(valores[i].equals("=") && i - 2 >= 0){
//                    if(valores[i-2] != null && valores[i-1] != null && valores[i+1] != null){
//                        if(expresionIdentificador.matcher(valores[i-2]).matches()
//                           && valores[i-1].toLowerCase().equals("int")
//                           && expresionNumeros.matcher(valores[i+1]).matches()){
//                            errores.add("No es el orden esperado para declarar variables: " + valores[i-2] + " " +
//                                valores[i-1] + " " + valores[i] + " " + valores[i+1]);
//                        }
//                    }
//                }
//
//
//                // se valida se espera un operador y se espera un punto y coma int identificador = valor ;
//                if(expresionIdentificador.matcher(valores[i]).matches()
//                        && !palabrasReservadas.contains(valores[i])
//                        && i > 0 && i < valores.length - 1){
//
//                    if(valores[i-1] != null && valores[i+1] != null){
//                        if(valores[i-1].toLowerCase().equals("int")
//                                && expresionNumeros.matcher(valores[i+1]).matches() &&
//                                !valores[i+1].equals("=")){
//                            errores.add("Se espera un operador para declarar bien la variable: " + valores[i-1] + " " +
//                                valores[i] + " " + valores[i+1] + " ");
//                        } else if (i + 3 < valores.length - 1 ) {
//
//                            if(valores[i-1].toLowerCase().equals("int") && valores[i+1].equals("=") &&
//                                 !valores[i+3].equals(";")){
//                                errores.add("Se espera un punto y coma para declarar variable: " + valores[i-1] + " " +
//                                valores[i] + " " + valores[i+1] + " " + valores[i+2] + " ");
//                            }
//                        }
//                    }
//                }
//
//                // se valida que al declarar variable tipo int se coloque un identificador
//                if(valores[i].equals("int") && valores.length <= 4){
//                    if(!expresionIdentificador.matcher(valores[i+1]).matches()){
//                        errores.add("Se espera un identificador en: " + valores[i] + " " +
//                                valores[i+1] + " ");
//                    }
//                }
//
//
//                // se valida se espera una expresión y se espera un signo de agrupación
//                if(valores[i].toLowerCase().equals("do")){
//                    if(i+1 < valores.length - 1){
//                        if(valores[i+1] != null && valores[i+2] != null){
//                            if(valores[i+1].equals("{") && valores[i+2].equals("}")){
//                                errores.add("Se espera una expresión dentro del ciclo: "
//                                        + valores[i] + " " +
//                                    valores[i+1] + " " + valores[i+2]);
//                            } else if (!valores[i+1].equals("{")) {
//                                errores.add("Se espera un signo de agrupación: " + valores[i] + " " +
//                                    valores[i+1] + " ");
//                            }
//                        }
//                    } else if (valores[i+1] != null) {
//                            if(!expresionAgrupadores.matcher(valores[i+1]).matches()){
//                                errores.add("Se espera un signo de agrupación: " + valores[i] + " " +
//                                    valores[i+1]+ "  ");
//                            }
//                    }
//                }
//
//                /// se valida palabra reservada
//                if(valores[i].equals("public")){
//                    if(valores[i+1] != null){
//                        if(!valores[i+1].toLowerCase().equals("class")
//                                || !valores[i+1].equals("int")){
//                            errores.add("Se espera una palabra reservada válida luego de public: "
//                                    + valores[i] + " " +
//                                valores[i+1]);
//                        }
//                    }
//                }
//
//                // se valida uso de expresión else
//                if(valores[i].equals("else") && i > 0 && i < valores.length - 1){
//                    if(valores[i-1] != null && valores[i+1] != null) {
//                        if(!valores[i-1].equals("}") || !valores[i].equals("else")
//                                || !valores[i+1].equals("{") ){
//                            errores.add("Uso de palabra else incorrecto, debe usarse como } else { se uso así: "
//                                    + valores[i-1] + " " + valores[i] + " " + valores[i+1]);
//                        }
//                    }
//                } else if(valores[i].equals("else")) {
//                    errores.add("Uso de palabra else incorrecto, debe usarse como } else { se uso así: " + valores[i]);
//                }
//
//                // validación de expresión aritmética
//              /*  if(operadores.contains(valores[i]) && i < valores.length
//                        && !valores[i].equals("=")
//                        && !valores[i].equals("==") && !valores[i-1].equals(")")
//                        && !valores[i+1].equals("(")){
//
//                    if(!expresionNumeros.matcher(valores[i-1]).matches() ||
//                       !expresionNumeros.matcher(valores[i+1]).matches()){
//                        errores.add("Expresión aritmética incorrecta : "
//                                    + valores[i-1] + " " + valores[i] + " " + valores[i+1]);
//                    }
//                } else if (operadores.contains(valores[i]) && !valores[i].equals("=") &&
//                        !valores[i].equals("==") && !valores[i-1].equals(")")
//                        && !valores[i+1].equals("(")){
//                    errores.add("Expresión aritmética incorrecta : "
//                                    + valores[i-1] + " " + valores[i]);
//                }*/
//
//                // validación inicio de if
//                if(valores[i].equals("if") && i < valores.length) {
//                    if(!valores[i+1].equals("(")){
//                        errores.add("Inicio de declaración if incorrecta : "
//                            + valores[i] + " " + valores[i+1]);
//                    }
//                }
//
//            } else {
//                if(!valores[i].trim().equals(" ") && !valores[i].equals("") && !valores[i].equals("\n")){
//                    errores.add(valores[i]);
//                }
//            }
//        }
//
//        if(errores.isEmpty()){
//          JOptionPane.showMessageDialog(null, "Se finalizó el análisis sintáctico, todo está correcto");
//          this.crearArbol();
//        } else {
//            String mensaje = "";
//
//            for(int i=0; i<errores.size(); i++) {
//                mensaje = mensaje + errores.get(i) + "\n";
//            }
//           JOptionPane.showMessageDialog(null, "Hay error en la sintaxis \nLos errores son los siguientes: \n" + mensaje);
//        }
//
//        errores.clear();
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

    public void crearArbol(FileReader file){
         LienzoArbol lienzo = new LienzoArbol();

        for (String cadena : fileService.getAllLines()) {
            if(cadena.matches("^[0-9a-zA-Z()+\\-*/^=\\s]*$")){
                ArbolBinarioExp arbol = new ArbolBinarioExp(cadena.replaceAll("\\s+", ""));
                lienzo.setArbol(arbol);
            }
        }

            JFrame ventana = new JFrame("Árbol sintáctico");
            JScrollPane scrollPane = new JScrollPane(lienzo);
            ventana.getContentPane().add(scrollPane);

            ventana.setSize(800, 600);
            ventana.setVisible(true);
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventana.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        analizadorSemantico(file);
                    } catch (IOException ex) {
                        System.out.println("Exception " + ex.getMessage());
                    }
                }
            });
                         
          
    }


    public void analizadorSemantico(FileReader file) throws IOException {
        var semantic = new SemanticAnalyzer(file);
        semantic.analyzeProgram();

        System.out.println(semantic.getErrors());
        if (semantic.getErrors() > 0) {
            var frame = new BulletPointFrame(semantic.getListErrors());
            frame.setVisible(true);
        }

        JOptionPane.showMessageDialog(null, "Se finalizó el análisis semántico, todo está correcto");
    }
}
