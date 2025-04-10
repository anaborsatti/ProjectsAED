package aed.collections;

import aed.utils.TemporalAnalysisUtils;
import java.util.function.Consumer;
import java.util.function.Function;

public class TestPerformance {

    public static void main(String[] args) {
        // Testando StackList
        System.out.println("Testing StackList:");
        TemporalAnalysisUtils.runDoublingRatioTest(
                generateStackList(),   // Função que gera a pilha
                testPushStackList(),    // Função que faz o push
                5                      // Número de iterações de razão dobrada
        );

        TemporalAnalysisUtils.runDoublingRatioTest(
                generateStackList(),
                testPopStackList(),
                5
        );

        // Testando ShittyStack
        System.out.println("Testing ShittyStack:");
        TemporalAnalysisUtils.runDoublingRatioTest(
                generateShittyStack(),
                testPushShittyStack(),
                5
        );

        TemporalAnalysisUtils.runDoublingRatioTest(
                generateShittyStack(),
                testPopShittyStack(),
                5
        );
    }

    // Funções que geram as pilhas com a complexidade n (tamanho da pilha)
    public static Function<Integer, StackList> generateStackList() {
        return (Integer n) -> {
            StackList stack = new StackList();
            for (int i = 0; i < n; i++) {
                stack.push(i);
            }
            return stack;
        };
    }

    public static Function<Integer, ShittyStack> generateShittyStack() {
        return (Integer n) -> {
            ShittyStack stack = new ShittyStack();
            for (int i = 0; i < n; i++) {
                stack.push(i);
            }
            return stack;
        };
    }

    // Funções que executam o push e pop em StackList
    public static Consumer<StackList> testPushStackList() {
        return (StackList stack) -> {
            stack.push(1);  // Insere um elemento para o teste de push
        };
    }

    public static Consumer<StackList> testPopStackList() {
        return (StackList stack) -> {
            stack.pop();  // Remove um elemento para o teste de pop
        };
    }

    // Funções que executam o push e pop em ShittyStack
    public static Consumer<ShittyStack> testPushShittyStack() {
        return (ShittyStack stack) -> {
            stack.push(1);  // Insere um elemento para o teste de push
        };
    }

    public static Consumer<ShittyStack> testPopShittyStack() {
        return (ShittyStack stack) -> {
            stack.pop();  // Remove um elemento para o teste de pop
        };
    }
}
