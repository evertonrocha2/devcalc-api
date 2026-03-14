package com.devcalc;

import com.devcalc.service.CalculatorService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Aplicação principal da DevCalc API.
 * API REST para operações matemáticas simples.
 */
public class App {

    private static final CalculatorService CALCULATOR_SERVICE = new CalculatorService();

    public static void main(String[] args) {
        Javalin app = createApp();
        app.start(7000);
    }

    /**
     * Cria e configura a aplicação Javalin com os endpoints.
     * @return instância configurada do Javalin
     */
    public static Javalin createApp() {
        Javalin app = Javalin.create(config -> {
            config.showJavalinBanner = false;
        });

        // Endpoint raiz
        app.get("/", ctx -> {
            ctx.json(new Response("DevCalc API está funcionando!", 
                "Use os endpoints: /add, /subtract, /multiply, /divide (a,b) e /sqrt (x)"));
        });

        // Endpoint de adição
        app.get("/add", ctx -> {
            handleOperation(ctx, (a, b) -> CALCULATOR_SERVICE.add(a, b));
        });

        // Endpoint de subtração
        app.get("/subtract", ctx -> {
            handleOperation(ctx, (a, b) -> CALCULATOR_SERVICE.subtract(a, b));
        });

        // Endpoint de multiplicação
        app.get("/multiply", ctx -> {
            handleOperation(ctx, (a, b) -> CALCULATOR_SERVICE.multiply(a, b));
        });

        // Endpoint de divisão
        app.get("/divide", ctx -> {
            handleOperation(ctx, (a, b) -> CALCULATOR_SERVICE.divide(a, b));
        });

        // Endpoint de raiz quadrada
        app.get("/sqrt", ctx -> {
            handleSingleOperation(ctx, x -> CALCULATOR_SERVICE.sqrt(x));
        });

        // Handler de exceções
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(new ErrorResponse(e.getMessage()));
        });

        return app;
    }

    /**
     * Processa uma operação matemática a partir dos parâmetros da requisição.
     */
    private static void handleOperation(Context ctx, Operation operation) {
        try {
            String aParam = ctx.queryParam("a");
            String bParam = ctx.queryParam("b");

            if (aParam == null || bParam == null) {
                ctx.status(400);
                ctx.json(new ErrorResponse("Parâmetros 'a' e 'b' são obrigatórios"));
                return;
            }

            double a = Double.parseDouble(aParam);
            double b = Double.parseDouble(bParam);
            double result = operation.execute(a, b);

            ctx.json(new OperationResponse(a, b, result));
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorResponse("Parâmetros devem ser números válidos"));
        } catch (IllegalArgumentException e) {
            throw e; // Será tratado pelo exception handler
        }
    }

    /**
     * Processa uma operação matemática com um único parâmetro.
     */
    private static void handleSingleOperation(Context ctx, SingleOperation operation) {
        try {
            String xParam = ctx.queryParam("x");

            if (xParam == null) {
                ctx.status(400);
                ctx.json(new ErrorResponse("Parâmetro 'x' é obrigatório"));
                return;
            }

            double x = Double.parseDouble(xParam);
            double result = operation.execute(x);

            ctx.json(new SingleOperationResponse(x, result));
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json(new ErrorResponse("Parâmetro deve ser um número válido"));
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Interface funcional para operações matemáticas com dois parâmetros.
     */
    @FunctionalInterface
    private interface Operation {
        double execute(double a, double b);
    }

    /**
     * Interface funcional para operações matemáticas com um parâmetro.
     */
    @FunctionalInterface
    private interface SingleOperation {
        double execute(double x);
    }

    /**
     * Classe para resposta de operação bem-sucedida com dois parâmetros.
     */
    private static class OperationResponse {
        final double a;
        final double b;
        final double result;

        OperationResponse(double a, double b, double result) {
            this.a = a;
            this.b = b;
            this.result = result;
        }
    }

    /**
     * Classe para resposta de operação com um parâmetro.
     */
    private static class SingleOperationResponse {
        final double x;
        final double result;

        SingleOperationResponse(double x, double result) {
            this.x = x;
            this.result = result;
        }
    }

    /**
     * Classe para resposta genérica.
     */
    private static class Response {
        final String message;
        final String info;

        Response(String message, String info) {
            this.message = message;
            this.info = info;
        }
    }

    /**
     * Classe para resposta de erro.
     */
    private static class ErrorResponse {
        final String error;

        ErrorResponse(String error) {
            this.error = error;
        }
    }
}
