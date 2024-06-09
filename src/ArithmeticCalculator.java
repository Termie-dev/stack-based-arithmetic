import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArithmeticCalculator {

	private static OperationStack opStack = new OperationStack();
	private static VariableStack varStack = new VariableStack();
	
	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter the name of the file with the extension and path if needed:");
		String fileName = in.nextLine();
				
		ExpandableArrayStack<String> expressions = readAllLines(fileName); // Create array-based stack to hold the expressions from the input file.
		ExpandableArrayStack<String> results = new ExpandableArrayStack<>(); // Create array-based stack to hold the results for the output file.
		
		for (int i = 0; i < expressions.getSize(); i++) {
			System.out.println(expressions.get(i));
            double result = evaluate(expressions.get(i));
            results.push(expressions.get(i) + " = " + result); // Push line containing expression and result onto final stack
        }
				
        writeAllLines("results.txt", results); // Write the results into a produced output file
		
	}
	
	public static ExpandableArrayStack<String> readAllLines(String fileName) {
		
		ExpandableArrayStack<String> lines = new ExpandableArrayStack<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        	
            String expression;
            
            while ((expression = reader.readLine()) != null) {
            	
            	lines.push(expression);
            }
        } 
        
        catch (IOException e) {
        	
            e.printStackTrace();
            
        }
        
        return lines;
    }
	
	private static void doOperation() {
		
		double x = varStack.pop();
		double y = varStack.pop();
		String op = opStack.pop();
		
		double result = 0;
		
		switch (op) {
	        case "+":
	            result = y + x;
	            break;
	        case "-":
	            result = y - x;
	            break;
	        case "*":
	            result = y * x;
	            break;
	        case "/":
	            result = y / x;
	            break;
	        case "^":
	            result = Math.pow(y, x);
	            break;
	        case ">":
	            result = y > x ? 1 : 0;
	            break;
	        case "<":
	            result = y < x ? 1 : 0;
	            break;
	        case ">=":
	            result = y >= x ? 1 : 0;
	            break;
	        case "<=":
	            result = y <= x ? 1 : 0;
	            break;
	        case "==":
	            result = y == x ? 1 : 0;
	            break;
	        case "!=":
	            result = y != x ? 1 : 0;
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown operator: " + op);
	    }
		
        varStack.push(result);
		
	}

	private static void repeatOperations(String refOp) {
		
        while (!opStack.isEmpty() && precedence(refOp) <= precedence(opStack.top())) { // Loops through operations as long as stack of operations is not empty.
        																			   // If the next operator is of smaller precedence, compute the higher precedence one first that is already in the operationStack
            if (opStack.top() == "(") { // Stop when opening parenthesis is encountered to make sure we're doing the nested parenthesis first
                opStack.pop(); // Remove the "(" from the stack.
            	break;
            }
            doOperation();
        }
    }

    public static double evaluate(String expression) {

    	 String[] tokens = tokenize(expression);
    	 
         for (int i = 0; i < tokens.length; i++) {
        	 
        	 String token = tokens[i].trim();
        	 
             if (token.isEmpty()) {
                 continue; // Skip empty tokens
             }
             System.out.println("Token: '" + token + "'"); // Debugging statement
             
             if (isNumber(token)) {
                 double num = Double.parseDouble(token);
                 varStack.push(num);
                 System.out.println("Pushed number: " + num); // Debugging statement
             } 
             
             else if (token.equals("(")) {
                 opStack.push("(");
                 System.out.println("Pushed operator: ("); // Debugging statement
             } 
             
             else if (token.equals(")")) {
            	 
                 while (!opStack.isEmpty() && !opStack.top().equals("(")) {
                     doOperation();
                 }
                 
                 if (!opStack.isEmpty()) {
                     opStack.pop();
                 }
                 
             } 
             
             else {
            	 
            	 if (token.equals("=") || token.equals("!") || token.equals(">") || token.equals("<")) {
                     if (i + 1 < tokens.length) {
                         String nextToken = tokens[i + 1];
                         if (nextToken.equals("=")) {
                        	 token += "="; // Form the multi-character operator
                             i++; // Skip the next token
                         }
                     }
                 }
            	 
                 repeatOperations(token);
                 opStack.push(token);
                 System.out.println("Pushed operator: " + token); // Debugging statement
             }
         }
        
        repeatOperations("$");
        return varStack.pop(); // Return result from the line
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Determine precedence of the operator encountered
    private static int precedence(String op) {
    	
    	switch (op) {
	        case "(":
	        case ")":
	            return 6;
	        case "^":
	            return 5;
	        case "*":
	        case "/":
	            return 4;
	        case "+":
	        case "-":
	            return 3;
	        case ">":
	        case ">=":
	        case "<":
	        case "<=":
	            return 2;
	        case "==":
	        case "!=":
	            return 1;
	        case "$":
	            return -1;
	        default:
	        	System.out.println("Unknown operator: " + op);
	            throw new IllegalArgumentException("Unknown operator: " + op);
    	}
    }

    // Split the string into an array based on these tokens
    private static String[] tokenize(String expression) {
        
    	
       return expression.split("(?<=[-+*/()^><=!])|(?=[-+*/()^><=!])");

        
    }
    
    // Writes lines from an ExpandableArrayStack to an output file.
    public static void writeAllLines(String outputFileName, ExpandableArrayStack<String> lines) {
        
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
    		
    		for (int i = 0; i < lines.getSize(); i++) {
    			
    			writer.write(lines.get(i));
                writer.newLine();
                
            }
        } 
    	
    	catch (IOException e) {
    		
            e.printStackTrace();
            
        }
    }
}
	

