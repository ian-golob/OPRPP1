package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Demo class that shows an example of ObjectStack usage.
 *
 * @author Ian Golob
 */
public class StackDemo {

    /**
     * A function that evaluates a mathematical expression in prefix notation.
     * Prints an error if the amount of string arguments is not 1 or if the
     * expression is invalid.
     * @param args one string that contains a mathematical expression in prefix notation
     */
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("Wrong amount of arguments given. Expected 1, got " + args.length);
            return;
        }

        String[] elements = args[0].split(" ");

        try {
            ObjectStack stack = new ObjectStack();

            for(String element: elements){

                if(stringIsInteger(element)){
                    stack.push(element);
                }else{

                    Integer secondOperand = Integer.valueOf((String) stack.pop());
                    Integer firstOperand = Integer.valueOf((String) stack.pop());

                    stack.push(doOperation(firstOperand, element, secondOperand).toString());
                }

            }

            if(stack.size() != 1){
                throw new IllegalArgumentException();
            }

            System.out.println("Expression evaluates to " + stack.pop());

        } catch (EmptyStackException | IllegalArgumentException ex) {
            System.out.println("Expression is invalid");
        } catch (ArithmeticException ex){
            System.out.println("Illegal expression, tried to divide by zero");
        }

    }

    /**
     * Does the given operation on the given operands.
     * @param firstOperand first operand
     * @param operator operator
     * @param secondOperand second operand
     * @return result of the operation
     */
    private static Integer doOperation(Integer firstOperand, String operator, Integer secondOperand) {
        return switch (operator) {
            case "+" -> firstOperand + secondOperand;
            case "-" -> firstOperand - secondOperand;
            case "*" -> firstOperand * secondOperand;
            case "/" -> firstOperand / secondOperand;
            case "%" -> firstOperand % secondOperand;
            default -> throw new IllegalArgumentException();
        };
    }

    /**
     * Checks if a given string represents an integer.
     * @param string string to check
     * @return true if the given string represents an integer, false otherwise.
     */
    private static boolean stringIsInteger(String string){
        if(string == null){
            return false;
        }

        try {
            Integer i = Integer.valueOf(string);
            return true;
        } catch (NumberFormatException ex){
            return false;
        }
    }

}
