import consts.ConstKeywords;

import java.io.FileReader;
import java.util.Scanner;

public class Lexer {
    private int index = 0;
    private int syn;
    private int sum;
    private String token;
    private int tag = 1;
    private boolean hasDecimalPoint = false;

    public Lexer(){};

    private Boolean IsDigit(char c){
        return Character.isDigit(c);
    }

    private Boolean IsLetter(char c){
        return Character.isLetter(c);
    }

    private int GetKeywordIndex(String s) {
        for(int i = 0; i < ConstKeywords.KEYWORDS.length; i++){
            if(s.equals(ConstKeywords.KEYWORDS[i])){
                return i;
            }
        }
        return -1;
    }

    public void GetInput(String filePath){
        try{
            Scanner sc = new Scanner(new FileReader(filePath));
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                index = 0;
                while(index < line.length()){
                    ScanLine(line);
                    switch(syn){
                        case -1:
                            System.out.println("Error");
                            syn = 0;
                            break;
                        case -2:
                            break;
                        default:
                            if(syn != 0){
                                if(syn == 10){
                                    System.out.println("<" + "id" + ", " + token + ">");
                                }
                                else{
                                    System.out.println("<" + token + ">");
                                }
                            }
                    }
                    if(index >= line.length()) break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void ScanLine(String s) throws Exception {
        final int lineLen = s.length();
        if(index > lineLen) return;
        if(s.charAt(index) == ' '){
            syn = -2;
            index++;
        }else if(s.charAt(index) == '.'){
            hasDecimalPoint = true;
            index ++;
        }
        else{
            token = "";
            if(s.charAt(index) == '#'){
                index = lineLen;
                return;
            }
            // 1. 判断字符是否为数字
            if(IsDigit(s.charAt(index))){
                token = "";
                sum = 0;
                while(index < lineLen && IsDigit(s.charAt(index))){
                    sum = (sum * 10) + (s.charAt(index) - '0');
                    index++;
                    syn = 20;
                    if(s.charAt(index) == '.'){
                        index ++;
                        token += sum + ".";
                        sum = 0;
                    }
                }
                if(hasDecimalPoint){
                    token += "0."+sum;
                    hasDecimalPoint = false;
                }else{
                    token += ""+sum;
                }
            }
            // 2. 符号为字符串
            else if(IsLetter(s.charAt(index))){
                token = "";
                while(index < lineLen && (IsDigit(s.charAt(index)) || IsLetter(s.charAt(index)))){
                    token = token + s.charAt(index);
                    index++;
                }
                syn = (GetKeywordIndex(token) == -1 ? 10 : GetKeywordIndex(token) + 1);
            }
            else{
                token = "";
                switch(s.charAt(index)){
                    case '=':
                        syn = 31;
                        index++;
                        token = "=";
                        if(index < lineLen && s.charAt(index) == '='){
                            syn = 49;
                            index++;
                            token = "==";
                        }
                        break;
                    case '+':
                        syn = 32;
                        index++;
                        token = "+";
                        break;

                    case '-':
                        syn = 33;
                        index++;
                        token = "-";
                        break;
                    case'*':
                        syn = 34;
                        index++;
                        token = "*";
                        break;

                    case'/':
                        syn = 35;
                        index++;
                        token = "/";
                        break;

                    case'(':
                        syn = 36;
                        index++;
                        token = "(";
                        break;

                    case')':
                        syn = 37;
                        index++;
                        token = ")";
                        break;

                    case'[':
                        syn = 38;
                        index++;
                        token = "[";
                        break;

                    case']':
                        syn = 39;
                        index++;
                        token = "]";
                        break;

                    case'{':
                        syn = 40;
                        index++;
                        token = "{";
                        break;

                    case'}':
                        syn = 41;
                        index++;
                        token = "}";
                        break;

                    case',':
                        syn = 42;
                        index++;
                        token = ",";
                        break;

                    case':':
                        syn = 43;
                        index++;
                        token = ":";
                        break;
                    case';':
                        syn = 44;
                        index++;
                        token = ";";
                        break;
                    case'>':
                        syn = 45;
                        index++;
                        token = ">";
                        if (index < lineLen && s.charAt(index) == '=')
                        {
                            syn = 47;
                            index++;
                            token = ">=";
                        }
                        break;

                    case'<':
                        syn = 46;
                        index++;
                        token = "<";
                        if (index < lineLen && s.charAt(index) == '=')
                        {
                            syn = 48;
                            index++;
                            token = "<=";
                        }
                        break;



                    case'!':
                        syn = -1;
                        index++;
                        if (index < lineLen && s.charAt(index) == '=')
                        {
                            syn = 50;
                            index++;
                            token = "!=";
                        }
                        break;
                    case '|':
                        syn = -1;
                        index++;
                        if(index < lineLen && s.charAt(index) == '|'){
                            syn = 51;
                            index++;
                            token = "||";
                        }
                        break;
                    case '"':
                        syn = -1;
                        token += s.charAt(index);
                        index++;
                        while(index < lineLen && s.charAt(index) != '"'){
                            if(s.charAt(index) == '#'){
                                tag = 0;
                                break;
                            }else{
                                token += s.charAt(index);
                                index++;
                            }
                        }
                        if(tag == 1){
                            token += s.charAt(index);
                            index++;
                            syn = 60;
                            break;
                        }
                        else{
                            throw new Exception("Quote didn't match");
                        }
                }
            }
        }
    }
}