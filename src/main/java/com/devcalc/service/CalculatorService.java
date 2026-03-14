package com.devcalc.service;

/**
 * Serviço responsável por realizar operações matemáticas básicas.
 */
public class CalculatorService {

    /**
     * Soma dois números.
     * @param a primeiro operando
     * @param b segundo operando
     * @return resultado da soma
     */
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtrai dois números.
     * @param a primeiro operando
     * @param b segundo operando
     * @return resultado da subtração
     */
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplica dois números.
     * @param a primeiro operando
     * @param b segundo operando
     * @return resultado da multiplicação
     */
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divide dois números.
     * @param a dividendo
     * @param b divisor
     * @return resultado da divisão
     * @throws IllegalArgumentException se o divisor for zero
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divisão por zero não é permitida");
        }
        return a / b;
    }

    /**
     * Calcula a raiz quadrada de um número.
     * @param x número para calcular a raiz quadrada
     * @return raiz quadrada do número
     * @throws IllegalArgumentException se o número for negativo
     */
    public double sqrt(double x) {
        if (x < 0) {
            throw new IllegalArgumentException("Não é possível calcular raiz quadrada de número negativo");
        }
        return Math.sqrt(x);
    }
}
