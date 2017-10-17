package company.kch.calculator.parser;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Kirill on 21.09.17.
 */

public class MathParser {

    private HashMap<String, Double> variables;

    public MathParser()
    {
        variables = new HashMap<String, Double>();
    }

    public void setVariable(String variableName, Double variableValue)
    {
        variables.put(variableName, variableValue);
    }

    private Double getVariable(String variableName)
    {
        if (!variables.containsKey(variableName)) {
            System.err.println( "Error: Try get unexists variable '"+variableName+"'" );
            return 0.0;
        }
        return variables.get(variableName);
    }

    public BigDecimal Parse(String s, int n) throws Exception
    {
        Result result = PlusMinus(s);
        if (!result.rest.isEmpty()) {
            System.err.println("Error: can't full parse");
            System.err.println("rest: " + result.rest);
        }
        return new BigDecimal(""+result.acc).setScale(n, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPI() {return new BigDecimal("" + Math.PI).setScale(10, BigDecimal.ROUND_HALF_UP);}
    public BigDecimal getE() {return new BigDecimal("" + Math.E).setScale(10, BigDecimal.ROUND_HALF_UP);}


    private Result PlusMinus(String s) throws Exception
    {
        Result current = MulDiv(s);
        double acc = current.acc;

        while (current.rest.length() > 0) {
            if (!(current.rest.charAt(0) == '+' || current.rest.charAt(0) == '-')) break;

            char sign = current.rest.charAt(0);
            String next = current.rest.substring(1);

            current = MulDiv(next);
            if (sign == '+') {
                acc += current.acc;
            } else {
                acc -= current.acc;
            }
        }
        return new Result(acc, current.rest);
    }

    private Result Bracket(String s) throws Exception
    {
        char zeroChar = s.charAt(0);
        if (zeroChar == '(') {
            Result r = PlusMinus(s.substring(1));
            if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                System.err.println("Error: not close bracket");
            }
            return r;
        }
        return FunctionVariable(s);
    }

    private Result FunctionVariable(String s) throws Exception
    {
        String f = "";
        int i = 0;
        // ищем название функции или переменной
        // имя обязательно должна начинаться с буквы
        while (i < s.length() && (Character.isLetter(s.charAt(i)) || ( Character.isDigit(s.charAt(i)) && i > 0 ))) {
            f += s.charAt(i);
            i++;
        }
        if (!f.isEmpty()) { // если что-нибудь нашли
            if ( s.length() > i && s.charAt( i ) == '(') { // и следующий символ скобка значит - это функция
                Result r = Bracket(s.substring(f.length()));
                return processFunction(f, r);
            } else { // иначе - это переменная
                return new Result(getVariable(f), s.substring(f.length()));
            }
        }
        return Num(s);
    }

    private Result MulDiv(String s) throws Exception {
        Result current = exponentiation(s);
        double acc = current.acc;
        while (true) {
            if (current.rest.length() == 0) {
                return current;
            }
            char sign = current.rest.charAt(0);
            if ((sign != '×' && sign != '÷' && sign != 'E')) return current;

            String next = current.rest.substring(1);
            Result right = exponentiation(next);

            if (sign == '×') {
                acc *= right.acc;
            } else if (sign == 'E') {
                acc = acc * Math.pow(10, right.acc);
            } else {
                acc /= right.acc;
            }

            current = new Result(acc, right.rest);
        }
    }

    private Result exponentiation(String s) throws Exception{
        Result cur = Bracket(s);
        double acc = cur.acc;
        cur.rest = skipSpaces(cur.rest);
        while(true){
            if(cur.rest.length() == 0) return cur;
            if(cur.rest.charAt(0) !='^') break;
            String next = cur.rest.substring(1);
            cur = Bracket(next);
            cur.acc = Math.pow(acc,cur.acc);
        }
        return cur;
    }

    private Result Num(String s) throws Exception
    {
        int i = 0;
        int dot_cnt = 0;
        boolean negative = false;
        // число также может начинаться с минуса
        if( s.charAt(0) == '-' ){
            negative = true;
            s = s.substring( 1 );
        }
        // разрешаем только цифры и точку
        while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
            // но также проверям, что в числе может быть только одна точка!
            if (s.charAt(i) == '.' && ++dot_cnt > 1) {
                throw new Exception("not valid number '" + s.substring(0, i + 1) + "'");
            }
            i++;
        }
        if( i == 0 ){ // что-либо похожее на число мы не нашли
            throw new Exception( "can't get valid number in '" + s + "'" );
        }
        double dPart = Double.parseDouble(s.substring(0, i));
        if( negative ) dPart = -dPart;
        String restPart = s.substring(i);

        return new Result(dPart, restPart);
    }

    // Тут определяем все нашие функции, которыми мы можем пользоватся в формулах
    private Result processFunction(String func, Result r)
    {
        switch (func) {
            case "sin":
                return new Result(Math.sin(Math.toRadians(r.acc)), r.rest);
            case "cos":
                return new Result(Math.cos(Math.toRadians(r.acc)), r.rest);
            case "tan":
                return new Result(Math.tan(Math.toRadians(r.acc)), r.rest);
            case "sqrt":
                return new Result(Math.sqrt(r.acc), r.rest);
            case "ln":
                return new Result(Math.log(r.acc), r.rest);
            case "log":
                return new Result(Math.log10(r.acc), r.rest);
            default:
                System.err.println("function '" + func + "' is not defined");
                break;
        }
        return r;
    }

    private String skipSpaces(String s){
        return s.trim();
    }
}

