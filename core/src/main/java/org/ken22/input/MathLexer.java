package org.ken22.input;

import org.ken22.input.tokens.Token;
import org.ken22.input.tokens.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.ken22.input.tokens.TokenType.*;

public class MathLexer {

    private static class LexError extends RuntimeException {
        LexError(Token token, String message) {
            super(message + " at " + token);
        }
    }

    public static void main(String[] args) {
        // TODO:allowing NUMBER VARIABLE with no STAR in between -> intuitiveness, but more complicated parser
        // String source = "ln(2, 0.5)";
        // String source = "1x^3 - 3*x*y"; //NUMBER VARIABLE POW NUMBER MINUS NUMBER STAR VARIABLE STAR VARIABLE
        // String source = "y''' = 1*x_1''^3 - 3*(x*y)/3 + log(4)";
        String source = "x'' = 1*x_1^3 - 3*(x*y)/3 + 4";
        // TODO: Find a way to keep track of the variables in a system table or sth
        // TODO: Only validate a set of statements if each variable has an expression for its derivative
        MathLexer lexer = new MathLexer(source);
        System.out.println();
        for (Token token : lexer.tokens) {
            System.out.println(token);
        }
        System.out.println(lexer.variables);
    }

    private static final HashMap<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("log", LOG);
        keywords.put("root", ROOT);
        keywords.put("ln", LN);
        keywords.put("exp", EXP);
        keywords.put("abs", ABS);
        keywords.put("factorial", FACTORIAL);
        keywords.put("sin", SINE);
        keywords.put("cos", COSINE);
        keywords.put("tan", TANGENT);
        keywords.put("sec", SECANT);
        keywords.put("csc", COSECANT);
        keywords.put("cot", COTANGENT);
    }

    private final String source;
    private final HashSet<String> variables = new HashSet<>();
    private final ArrayList<Token> tokens = new ArrayList<>();
    private String lambdaVar;

    private int start = 0;
    private int current = 0;

    public MathLexer(String source) {
        this.source = source;
        scanTokens();
        compileVariables();
        compileLambdaVar();
    }

    private void compileLambdaVar() {
        this.lambdaVar = tokens.get(0).lexeme.substring(0, tokens.get(0).lexeme.length() - 1);
    }

    /**
     * Compiles a hashset of variables from the tokens. Every time a derivative is encountered, the variable whose derivative it is is also added to the hashset, and the variable and DERIV tokens are replace with a single variable token with the derivative as variable + "'" (e.g. x' for the derivative of x)."
     * TODO: write better documentation.
     * @return
     */
    private void compileVariables() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            while (token.type == DERIV) {    
                tokens.remove(i);
                tokens.set(i-1, new Token(VARIABLE, tokens.get(i - 1).lexeme + "'", null));
                variables.add(tokens.get(i - 1).lexeme);
                token = tokens.get(i);
            }
            if (token.type == VARIABLE) {
                variables.add(token.lexeme);
            }
        }
    }

    // > scan-tokens
    private void scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(END_OF_STATEMENT, "", null));
    }
    private void scanToken() {
        char c = advance();
        switch (c) {
            // single character tokens
            case '(':
                addToken(LEFT_PARENTHESIS);
                break;
            case ')':
                addToken(RIGHT_PARENTHESIS);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case '*':
                addToken(STAR);
                break;
            case '/':
                addToken(SLASH);
                break;
            case '!':
                addToken(FACTORIAL);
                break;
            case '^':
                addToken(POW);
                break;
            case '\'':
                if(tokens.get(tokens.size()-1).type == VARIABLE || tokens.get(tokens.size()-1).type == DERIV){
                    addToken(DERIV, null);
                } else {
                    throw new LexError(tokens.get(tokens.size()-1), "Derivative must be preceded by a variable.");
                }
                break;
            case '=':
                for(Token t: tokens){
                    if(t.type == EQUALS){
                        throw new LexError(t, "Only one equals sign allowed.");
                    }
                }
                addToken(EQUALS);
                break;

            // ignore whitespace
            case ' ':
            case '\r':
            case '\t':
            case '\n':
            case ',': // ignore commas
                break;

            default:
              if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new IllegalArgumentException("Unexpected Character");
                }
                    break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();
        String text = source.substring(start, current);
        // See if the identifier is a reserved word.
        TokenType type = keywords.get(text);
        // If it's not, it's a variable.
        if (type == null)
            type = VARIABLE;
        addToken(type);
    }

    private void number() {
        while (isDigit(peek()))
            advance();
        
        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();
            while (isDigit(peek()))
                advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
            c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal));
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public HashSet<String> getVariables() {
        return variables;
    }

    public String getLambdaVar() {
        return lambdaVar;
    }
}