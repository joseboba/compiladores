package org.umg.compiladores.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SemanticAnalysisResult {
    public boolean success;
    public double result;
    public String errorMessage;

    public SemanticAnalysisResult(boolean success, double result, String errorMessage) {
        this.success = success;
        this.result = result;
        this.errorMessage = errorMessage;
    }
}
