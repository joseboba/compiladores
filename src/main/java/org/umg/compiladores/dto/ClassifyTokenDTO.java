package org.umg.compiladores.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.umg.compiladores.Token;

@Data
@AllArgsConstructor
public class ClassifyTokenDTO {

    private String text;
    private Token token;
    private Integer line;

}
