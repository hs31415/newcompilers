import javax.print.DocFlavor;
import java.lang.invoke.StringConcatException;

public class LexerTest {
    private static String keyWords[] =
            {"function",
                    "read",
                    "write",
                    "return",
                    "if",
                    "else",
                    "for",
                    "repeat",
                    "until",
                    "do",
                    "while",
                    "integer",
                    "double",
                    "char",
            };

    private static int syn;
    private static String token;
    private static int sum;
    private static int i = 0;
    private static int tag = 1;
    private static boolean hasDecimalPoint = false; // 是否已经遇到小数点

    public static boolean IsLetter(char ch) {
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
            return true;
        else
            return false;

    }

    public static boolean IsDigit(char ch)  //判断是否为数字
    {
        if (ch >= '0' && ch <= '9')
            return true;
        else
            return false;

    }

    public static void scan(String str) {
        char[] s = str.toCharArray();

        if (s[i] == ' ') {
            syn = -2;
            i++;
        } else if(s[i] == '.'){
            hasDecimalPoint = true;
            i++;
        } else {
            if(!hasDecimalPoint) {
                token = "";   //清空当前字符串
            }

            //  1.判断字符是否为数字
            if (IsDigit(s[i])) {
                token = ""; //清空当前字符串
                sum = 0;
                while (IsDigit(s[i])) {
                    sum = sum * 10 + (s[i] - '0');
                    i++;  //字符位置++
                    syn = 20;   //INT种别码为20
                    if(s[i] == '.'){
                        hasDecimalPoint = true;
                        i++;
                        token += sum + "." ;
                        sum = 0;
                        hasDecimalPoint = false;
                    }
                }
                if(hasDecimalPoint ) {
                    token += "0. " + sum;    //骚操作，直接转化字符串
                    hasDecimalPoint = false;
                }else{
                    token += " " + sum;    //骚操作，直接转化字符串
                }
            }
            // 2.字符为字符串，表现为字母开头衔接任意个数字或字母
            else if (IsLetter(s[i])) {
                token = ""; //清空当前字符串
                while (IsDigit(s[i]) || IsLetter(s[i])) {
                    token += s[i];   //加入token字符串
                    i++;
                }
                //s[i] = '\0';  //刚刚上面最后i++了所以补充
                syn = 10;  // 如果是标识符，种别码为10

                //如果是关键字，则用for循环将token与keyword比较找对应的种别码
                for (int j = 0; j < 12; j++) {
                    if (token == keyWords[j])    //如果都是string类型，可以直接=相比较，若相等则返回1，否则为0
                    {
                        syn = j + 1;   //种别码从1开始所以要加1
                        break;
                    }
                }
            }
            //3. 判断为符号
            else {
                token = ""; //清空当前字符串
                switch (s[i]) {
                    case '=':
                        syn = 21;
                        i++;
                        token = "=";
                        if (s[i] == '=') {
                            syn = 39;
                            i++;
                            token = "==";
                        }
                        break;

                    case '+':
                        syn = 22;
                        i++;
                        token = "+";
                        break;

                    case '-':
                        syn = 23;
                        i++;
                        token = "-";
                        break;

                    case '*':
                        syn = 24;
                        i++;
                        token = "*";
                        break;

                    case '/':
                        syn = 25;
                        i++;
                        token = "/";
                        break;

                    case '(':
                        syn = 26;
                        i++;
                        token = "(";
                        break;

                    case ')':
                        syn = 27;
                        i++;
                        token = ")";
                        break;

                    case '[':
                        syn = 28;
                        i++;
                        token = "[";
                        break;

                    case ']':
                        syn = 29;
                        i++;
                        token = "]";
                        break;

                    case '{':
                        syn = 30;
                        i++;
                        token = "{";
                        break;

                    case '}':
                        syn = 31;
                        i++;
                        token = "}";
                        break;

                    case ',':
                        syn = 32;
                        i++;
                        token = ",";
                        break;

                    case ':':
                        syn = 33;
                        i++;
                        token = ":";
                        break;

                    case ';':
                        syn = 34;
                        i++;
                        token = ";";
                        break;

                    case '>':
                        syn = 35;
                        i++;
                        token = ">";
                        if (s[i] == '=') {
                            syn = 37;
                            i++;
                            token = ">=";
                        }
                        break;

                    case '<':
                        syn = 36;
                        i++;
                        token = "<";
                        if (s[i] == '=') {
                            syn = 38;
                            i++;
                            token = "<=";
                        }
                        break;

                    case '!':
                        syn = -1;
                        i++;
                        if (s[i] == '=') {
                            syn = 40;
                            i++;
                            token = "!=";
                        }
                        break;

                    case '"':
                        syn = -1;
                        token += s[i];
                        i++;
                        while (s[i] != '"') {
                            if (s[i] == '#') {
                                tag = 0;
                                break;
                            } else {
                                token += s[i];
                                i++;
                            }
                        }
                        if (tag == 1) {
                            token += s[i];
                            i++;
                            syn = 50;
                            break;
                        } else {
                            syn = -1;
                            System.out.println("双引号只存在一个，非法输入");
                            break;
                        }

                    case '|':
                        syn = 51;
                        i++;
                        token = "|";
                        if (s[i] == '|') {
                            syn = 39;
                            i++;
                            token = "||";
                        }
                        break;


                    case '#': //结束
                        syn = 0;
                        System.out.println("\n#结束");
                        break;

                    default:
                        syn = -1;
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        String a =
                "miniRC=function(integer N, integer K, double rc){" +
                        "     integer a[10][20];" +
                        "     double b[19];" +
                        "     if(N==1) return(0);" +
                        "     integer KL=floor(K * rc);         " +
                        "     if(KL < 1 || KL > (K-1))" +
                        "            KL = 1;" +
                        "     else if (KL > .5*K)" +
                        "            KL = ceiling(KL / 2.0" +
                        "     KR = K - KL" +
                        "     integer NL = ceiling(N * KL / K)      " +
                        "     integer NR = N - NL" +
                        "     return( 1+(NL * miniRC(NL, KL, rc) + NR * miniRC(NR, KR, rc)) / N)" +
                        "}";
        do{
            scan(a);
            switch (syn)   //最后判断一波syn
            {
                case -1:
                    System.out.println("error：无结束符或存在非法字符");
                    syn = 0;
                    break;
                case -2:      //遇到空格跳过
                    break;
                default:
                    if(syn!=0)
                        System.out.println("(" + syn + "," + token + ")");
            }

        } while (syn!=0);
    }
}