/**
 * @author zwzk8
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import line.*;
public class Compiler {
    static int pointer=0;
    static ArrayList<Token> tokenList = new ArrayList<>();
    static ArrayList<Token> tokenListPlus = new ArrayList<Token>();
    static ArrayList<Error> errorList = new ArrayList<Error>();
    static ArrayList<Table> tableList = new ArrayList<>();
    static ArrayList<Line> lineList = new ArrayList<Line>();
    static ArrayList<TreeNode> stack = new ArrayList<TreeNode>();
    static ArrayList<String> stringStack = new ArrayList<String>();
    static Table table0 = new Table(0,-1);
    static Token token0;
    static Error error0;
    static int line=1;
    static int tableId = 0;
    static int level = 0;
    static int regId = 1;
    static int nowtag = 0;
    static String tab = "   ";
    static HashMap <String,TreeNode> global= new HashMap <> ();
    static TreeNode root_compUnit = new TreeNode(0,0,"<CompUnit>");
    static TreeNode child = new TreeNode();
    public static void main(String[] args) {
        Token token0;
        String inputFile = "testfile.txt";
        String outputFile = "output1.txt";
        String str;
        int line=1;
        int p=0;
        tableList.add(table0);

        lineList.add(new Line(0,"declare i32 @getint()\n"));
        lineList.add(new Line(0,"declare void @putint(i32)\n"));
        lineList.add(new Line(0,"declare void @putch(i32)\n"));
        lineList.add(new Line(0,"declare void @putstr(i8*)\n"));

        //最外层的符号表，所有的函数、全局变量、全局常量都在这里
        //Table outermostTable = new Table(0,-1);
        //表明现在处于的符号表的id
        //tableList.add(outermostTable);

        try {
            // 创建文件读取流
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            // 创建文件写入流
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            StringBuilder fileContent = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                // 将每行内容追加到fileContent中，可以根据需要添加换行符
                fileContent.append(str).append("\n");
            }
            str = fileContent.toString();
            str=removeMultiLineComments(str);
            str=removeSingleLineComments(str);
            System.out.println(str);
            while(p<str.length()){
                switch (str.charAt(p)){
                    case ' ':

                        break;
                    case '\n':
                        line++;

                        break;
                    case '!':
                        if(str.charAt(p+1)=='='){
                            writer.write("NEQ"+" "+"!=");
                            writer.newLine();
                            token0 = new Token("!=", "NEQ", line);
                            tokenList.add(token0);
                            p++;
                        } else{
                            writer.write("NOT"+" "+str.charAt(p));
                            writer.newLine();
                            token0 = new Token("!", "NOT", line);
                            tokenList.add(token0);
                        }
                        break;
                    case '|':
                        if(str.charAt(p+1)=='|'){
                            writer.write("OR"+" "+"||");
                            writer.newLine();
                            token0 = new Token("||", "OR", line);
                            tokenList.add(token0);
                            p++;
                        } else{
                            System.out.println("error of ||");
                        }
                        break;
                    case '&':
                        if(str.charAt(p+1)=='&'){
                            writer.write("AND"+" "+"&&");
                            writer.newLine();
                            token0 = new Token("&&", "AND", line);
                            tokenList.add(token0);
                            p++;
                        } else{
                            System.out.println("error of &&");
                        }
                        break;
                    case '+':
                        writer.write("PLUS"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("+", "PLUS", line);
                        tokenList.add(token0);
                        break;
                    case '-':
                        writer.write("MINU"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("-", "MINU", line);
                        tokenList.add(token0);
                        break;
                    case '*':
                        writer.write("MULT"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("*", "MULT", line);
                        tokenList.add(token0);
                        break;
                    case '/':
                        writer.write("DIV"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("/", "DIV", line);
                        tokenList.add(token0);
                        break;
                    case '%':
                        writer.write("MOD"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("%", "MOD", line);
                        tokenList.add(token0);
                        break;
                    case '<':
                        if(str.charAt(p+1)=='='){
                            writer.write("LEQ"+" "+"<=");
                            writer.newLine();
                            token0 = new Token("<=", "LEQ", line);
                            tokenList.add(token0);
                            p++;
                        } else{
                            writer.write("LSS"+" "+str.charAt(p));
                            writer.newLine();
                            token0 = new Token("<", "LSS", line);
                            tokenList.add(token0);
                        }
                        break;
                    case '>':
                        if(str.charAt(p+1)=='='){
                            writer.write("GEQ"+" "+">=");
                            writer.newLine();
                            token0 = new Token(">=", "GEQ", line);
                            tokenList.add(token0);
                            p++;
                        } else{
                            writer.write("GRE"+" "+str.charAt(p));
                            writer.newLine();
                            token0 = new Token(">", "GRE", line);
                            tokenList.add(token0);
                        }
                        break;
                    case '=':
                        if(str.charAt(p+1)=='='){
                            writer.write("EQL"+" "+"==");
                            writer.newLine();
                            token0 = new Token("==", "EQL", line);
                            tokenList.add(token0);
                            p++;
                        } else{
                            writer.write("ASSIGN"+" "+str.charAt(p));
                            writer.newLine();
                            token0 = new Token("=", "ASSIGN", line);
                            tokenList.add(token0);
                        }
                        break;
                    case ';':
                        writer.write("SEMICN"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token(";", "SEMICN", line);
                        tokenList.add(token0);
                        break;
                    case ',':
                        writer.write("COMMA"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token(",", "COMMA", line);
                        tokenList.add(token0);
                        break;
                    case '(':
                        writer.write("LPARENT"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("(", "LPARENT", line);
                        tokenList.add(token0);
                        break;
                    case ')':
                        writer.write("RPARENT"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token(")", "RPARENT", line);
                        tokenList.add(token0);
                        break;
                    case '[':
                        writer.write("LBRACK"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("[", "LBRACK", line);
                        tokenList.add(token0);
                        break;
                    case ']':
                        writer.write("RBRACK"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("]", "RBRACK", line);
                        tokenList.add(token0);
                        break;
                    case '{':
                        writer.write("LBRACE"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("{", "LBRACE", line);
                        tokenList.add(token0);
                        break;
                    case '}':
                        writer.write("RBRACE"+" "+str.charAt(p));
                        writer.newLine();
                        token0 = new Token("}", "RBRACE", line);
                        tokenList.add(token0);
                        break;
                    case '"':
                        int p0=p;
                        p++;
                        while (str.charAt(p)!='"'){
                            p++;
                        }
                        writer.write("STRCON"+" "+str.substring(p0,p+1));
                        writer.newLine();
                        token0 = new Token(str.substring(p0,p+1), "STRCON", line);
                        tokenList.add(token0);
                        if(!isFormatStringValid(token0.getContent())){
                            error0 = new Error("a",line);
                            errorList.add(error0);
                        }
                        break;
                    default:
                        //如果当前字符是字母或下划线
                        if (Character.isLetter(str.charAt(p))||str.charAt(p)=='_') {
                            String ind=readIdentifier(str,p);
                            p=p+ind.length()-1;
                            if(getCode(ind)!=null){
                                writer.write(getCode(ind)+" "+ind);
                                writer.newLine(); // 写入换行符
                                token0 = new Token(ind, getCode(ind), line);
                                tokenList.add(token0);
                            }
                            else{
                                writer.write("IDENFR "+ind);
                                writer.newLine(); // 写入换行符
                                token0 = new Token(ind, "IDENFR", line);
                                tokenList.add(token0);
                            }
                        }
                        //如果是数字
                        else if(Character.isDigit(str.charAt(p))) {
                            String ind=readNumberString(str,p);
                            p=p+ind.length()-1;
                            writer.write("INTCON "+ind);
                            writer.newLine(); // 写入换行符
                            token0 = new Token(ind, "INTCON", line);
                            tokenList.add(token0);
                        }else{
                            System.out.println("error :"+str.charAt(p));
                        }
                }
                p++;
            }
            // 关闭文件流
            reader.close();
            writer.close();

        } catch (IOException e) {
            System.err.println("发生错误: " + e.getMessage());
        }
        parseCompUnit(0,0, root_compUnit);
        setLoop();
        //给main函数的返回值类型设为int
        tableList.get(tableList.size()-1).isInt = 1;
        /*
        String fileName = "output.txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName, false); // 使用false参数表示覆盖写入
            for (Token item : tokenListPlus) {
                if(!("<BlockItem>".equals(item.getType()) || "<Decl>".equals(item.getType()) || "<BType>".equals(item.getType()))){
                    fileWriter.write(item + "\n");
                }
            }
            fileWriter.close();
            System.out.println("ArrayList的内容已成功写入文件 " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入文件时出现错误");
        }
        */
        errorHandle(root_compUnit);
        sortErrorList();
        String fileName = "error.txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName, false); // 使用false参数表示覆盖写入
            for (Error item : errorList) {
                //if(!(item.getType()=="<BlockItem>"||item.getType()=="<Decl>"||item.getType()=="<BType>")){
                fileWriter.write(item + "\n");
                //}
            }
            fileWriter.close();
            System.out.println("errorlist的内容已成功写入文件 " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入文件时出现错误");
        }

        printTable();
        //printTokenList(tokenListPlus);
        System.out.println("");
        //postOrderTraversal1(node);
        postOrderTraversalToFile(root_compUnit);

        printTree(root_compUnit);
        generate(root_compUnit);

        fileName = "llvm_ir.txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName, false); // 使用false参数表示覆盖写入
            for (Line item : lineList) {
                fileWriter.write(item + "");
            }
            fileWriter.close();
            System.out.println("lineList的内容已成功写入文件 " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("写入文件时出现错误");
        }
    }
    public static int parseTerminal(int p, int tableId, TreeNode node){
        node.line = tokenList.get(p).getLineNum();
        token0 = tokenList.get(p);
        tokenListPlus.add(token0);
        return p+1;
    }
    public static int parseCompUnit(int p, int tableId, TreeNode node){
        //解析所有Decl
        while(true){
            if(tokenList.get(p).getType()=="CONSTTK"){
                TreeNode child = new TreeNode(p,tableId,"<Decl>");
                node.addChild(child);
                p=parseDecl(p, tableId, child);
            }
            else if(tokenList.get(p).getType()=="INTTK"&&tokenList.get(p+1).getType()=="IDENFR"&&tokenList.get(p+2).getType()!="LPARENT"){
                TreeNode child = new TreeNode(p,tableId,"<Decl>");
                node.addChild(child);
                p=parseDecl(p, tableId, child);
            }
            else{
                break;
            }
        }
        //解析所有FuncDef
        while(true){
            if(tokenList.get(p).getType()=="VOIDTK"){
                TreeNode child = new TreeNode(p,tableId,"<FuncDef>");
                node.addChild(child);
                p=parseFuncDef(p, tableId,child);
            }
            else if(tokenList.get(p).getType()=="INTTK"&&tokenList.get(p+1).getType()=="IDENFR"&&tokenList.get(p+2).getType()=="LPARENT"){
                TreeNode child = new TreeNode(p,tableId,"<FuncDef>");
                node.addChild(child);
                p=parseFuncDef(p, tableId,child);
            }
            else{
                break;
            }
        }
        //现在该解析MainFuncDef了
        if(tokenList.get(p+1).getType()=="MAINTK"){
            TreeNode child = new TreeNode(p,tableId,"<MainFuncDef>");
            node.addChild(child);
            p=parseMainFuncDef(p, tableId, child);
        }
        else {
            System.out.println("error of CompUnit");
        }
        token0 = new Token("", "<CompUnit>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseForStmt(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"<LVal>");
        node.addChild(child);
        p= parseLVal(p, tableId, child);
        //解析=
        child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);
        child = new TreeNode(p,tableId,"<Exp>");
        node.addChild(child);
        p=parseExp(p, tableId, child);
        token0 = new Token("", "<ForStmt>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parsePrimaryExp(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="LPARENT"){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            TreeNode child1 = new TreeNode(p,tableId,"<Exp>");
            node.addChild(child1);
            p=parseExp(p, tableId, child1);
            node.expType = child1.expType;
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
        }
        else if(Objects.equals(tokenList.get(p).getType(), "IDENFR")){
            child = new TreeNode(p,tableId,"<LVal>");
            node.addChild(child);
            p= parseLVal(p, tableId, child);
            //System.out.println("<LVal>"+child.expType);
            node.expType = node.children.get(0).expType;
            //...
            System.out.println(node+"   0o0o");
        }
        else if(tokenList.get(p).getType()=="INTCON"){
            node.expType = 0;
            child = new TreeNode(p,tableId,"<Number>");
            node.addChild(child);
            p=parseNumber(p, tableId, child);
        }
        token0 = new Token("", "<PrimaryExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseCond(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"<LOrExp>");
        node.addChild(child);
        p=parseLOrExp(p, tableId, child);
        token0 = new Token("", "<Cond>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseLOrExp(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"<LAndExp>");
        node.addChild(child);
        p=parseLAndExp(p, tableId, child);

        while(tokenList.get(p).getType()=="OR"){
            TreeNode last=node.children.get(node.children.size() - 1);
            int size = node.children.size();
            node.children.remove(size-1);
            TreeNode newNode =new TreeNode(last.p, tableId,"<LOrExp>");
            newNode.children.add(last);
            node.children.add(newNode);

            token0 = new Token("", "<LOrExp>",0);
            tokenListPlus.add(token0);

            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);

            child = new TreeNode(p,tableId,"<LAndExp>");
            node.addChild(child);
            p=parseLAndExp(p, tableId, child);
        }
        token0 = new Token("", "<LOrExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseLAndExp(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"<EqExp>");
        node.addChild(child);
        p=parseEqExp(p, tableId, child);
        while(tokenList.get(p).getType()=="AND"){
            TreeNode last=node.children.get(node.children.size() - 1);
            int size = node.children.size();
            node.children.remove(size-1);
            TreeNode newNode =new TreeNode(last.p, tableId,"<LAndExp>");
            newNode.children.add(last);
            node.children.add(newNode);

            token0 = new Token("", "<LAndExp>",0);
            tokenListPlus.add(token0);
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            child = new TreeNode(p,tableId,"<EqExp>");
            node.addChild(child);
            p=parseEqExp(p, tableId, child);
        }
        token0 = new Token("", "<LAndExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseEqExp(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"<RelExp>");
        node.addChild(child);
        p=parseRelExp(p, tableId, child);
        while(tokenList.get(p).getType()=="EQL"||tokenList.get(p).getType()=="NEQ"){
            TreeNode last=node.children.get(node.children.size() - 1);
            int size = node.children.size();
            node.children.remove(size-1);
            TreeNode newNode =new TreeNode(last.p, tableId,"<EqExp>");
            newNode.children.add(last);
            node.children.add(newNode);

            token0 = new Token("", "<EqExp>",0);
            tokenListPlus.add(token0);
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            child = new TreeNode(p,tableId,"<RelExp>");
            node.addChild(child);
            p=parseRelExp(p, tableId, child);
        }
        token0 = new Token("", "<EqExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseRelExp(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"<AddExp>");
        node.addChild(child);
        p=parseAddExp(p, tableId, child);
        while(tokenList.get(p).getType()=="LSS"||tokenList.get(p).getType()=="GRE"||tokenList.get(p).getType()=="LEQ"||tokenList.get(p).getType()=="GEQ"){
            TreeNode last=node.children.get(node.children.size() - 1);
            int size = node.children.size();
            node.children.remove(size-1);
            TreeNode newNode =new TreeNode(last.p, tableId,"<RelExp>");
            newNode.children.add(last);
            node.children.add(newNode);
            token0 = new Token("", "<RelExp>",0);
            tokenListPlus.add(token0);

            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            child = new TreeNode(p,tableId,"<AddExp>");
            node.addChild(child);
            p=parseAddExp(p, tableId, child);
        }
        token0 = new Token("", "<RelExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseConstExp(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"<AddExp>");
        node.addChild(child);
        p=parseAddExp(p, tableId, child);
        token0 = new Token("", "<ConstExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseAddExp(int p, int tableId, TreeNode node){
        TreeNode child = new TreeNode(p,tableId,"<MulExp>");
        node.addChild(child);
        p=parseMulExp(p, tableId, child);
        node.expType = child.expType;
        while(tokenList.get(p).getType()=="PLUS"||tokenList.get(p).getType()=="MINU"){

            TreeNode last=node.children.get(node.children.size() - 1);
            int size = node.children.size();
            node.children.remove(size-1);
            TreeNode newNode =new TreeNode(last.p, tableId,"<AddExp>");
            newNode.children.add(last);
            node.children.add(newNode);

            token0 = new Token("", "<AddExp>",0);
            tokenListPlus.add(token0);
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            child = new TreeNode(p,tableId,"<MulExp>");
            node.addChild(child);
            p=parseMulExp(p, tableId, child);
        }
        token0 = new Token("", "<AddExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseMulExp(int p, int tableId, TreeNode node){
        TreeNode child = new TreeNode(p,tableId,"<UnaryExp>");
        node.addChild(child);
        p=parseUnaryExp(p, tableId, child);
        node.expType = child.expType;
        while(tokenList.get(p).getType()=="MULT"||tokenList.get(p).getType()=="DIV"||tokenList.get(p).getType()=="MOD"){
            TreeNode last=node.children.get(node.children.size() - 1);
            int size = node.children.size();
            node.children.remove(size-1);
            TreeNode newNode =new TreeNode(last.p, tableId,"<MulExp>");
            newNode.children.add(last);
            node.children.add(newNode);

            token0 = new Token("", "<MulExp>",0);
            tokenListPlus.add(token0);
            token0 = new Token("", "<MulExp>",0);
            tokenListPlus.add(token0);
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);

            child = new TreeNode(p,tableId,"<UnaryExp>");
            node.addChild(child);
            p=parseUnaryExp(p, tableId, child);
        }
        token0 = new Token("", "<MulExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseUnaryExp(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="IDENFR"&&tokenList.get(p+1).getType()=="LPARENT"){
            //parse一个Ident
            //node.expType =
            int success = 0;
            int expType = -2;
            int tableId0 = tableId;
            while(tableId0>=0){
                Table t0 = tableList.get(tableId0);
                if(t0.symbolList.containsKey(tokenList.get(p).getContent())){
                    success = 1;
                    expType = t0.symbolList.get(tokenList.get(p).getContent()).type;
                    break;
                }
                else{
                    tableId0 = t0.fatherId;
                }
            }
            node.expType = expType;
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            //解析(
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            if(Objects.equals(tokenList.get(p).getType(), "RPARENT")){
                //解析)
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            else{
                child = new TreeNode(p,tableId,"<FuncRParams>");
                node.addChild(child);
                p=parseFuncRParams(p, tableId, child);
                /*
                //解析)
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
                */
                if(tokenList.get(p).getType().equals("RPARENT")) {
                    child = new TreeNode(p,tableId,"RPARENT",")");
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);//]
                }
                else {
                    child = new TreeNode(p,tableId,"RPARENT",")");
                    node.addChild(child);
                    error0 = new Error("j",tokenList.get(p).getLineNum());
                    errorList.add(error0);
                }
            }
        }
        else if(tokenList.get(p).getType()=="PLUS"||tokenList.get(p).getType()=="MINU"||tokenList.get(p).getType()=="NOT"){
            child = new TreeNode(p,tableId,"<UnaryOp>");
            node.addChild(child);
            p=parseUnaryOp(p, tableId, child);
            child = new TreeNode(p,tableId,"<UnaryExp>");
            node.addChild(child);
            p=parseUnaryExp(p, tableId, child);
            node.expType = child.expType;
        }
        else{
            TreeNode child = new TreeNode(p,tableId,"<PrimaryExp>");
            node.addChild(child);
            p=parsePrimaryExp(p, tableId, child);
            node.expType = child.expType;
        }
        token0 = new Token("", "<UnaryExp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseExp(int p, int tableId, TreeNode node){
        TreeNode child = new TreeNode(p,tableId,"<AddExp>");
        node.addChild(child);
        p=parseAddExp(p, tableId, child);
        node.expType = child.expType;
        token0 = new Token("", "<Exp>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseFuncRParams(int p, int tableId, TreeNode node){
        TreeNode child = new TreeNode(p,tableId,"<Exp>");
        node.addChild(child);
        p=parseExp(p, tableId, child);
        node.FuncRParamsType.add(child.expType);
        while(tokenList.get(p).getType()=="COMMA"){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            child = new TreeNode(p,tableId,"<Exp>");
            node.addChild(child);
            p=parseExp(p, tableId, child);
            node.FuncRParamsType.add(child.expType);
        }
        //System.out.println("qeidqdjqwiod"+node.FuncRParamsType);
        token0 = new Token("", "<FuncRParams>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseLVal(int p, int tableId, TreeNode node){
        int expType = -2;
        while(tableId>=0){
            Table t0 = tableList.get(tableId);
            if(t0.symbolList.containsKey(tokenList.get(p).getContent())){
                expType = t0.symbolList.get(tokenList.get(p).getContent()).type;
                break;
            }
            else{
                tableId = t0.fatherId;
            }
        }
        child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);

        while(tokenList.get(p).getType()=="LBRACK"){
            expType--;
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);

            child = new TreeNode(p,tableId,"<Exp>");
            node.addChild(child);
            p=parseExp(p, tableId, child);
            /*
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            //要求这里必须是]
            p=parseTerminal(p, tableId, child);
            */
            if(tokenList.get(p).getType().equals("RBRACK")) {
                child = new TreeNode(p,tableId,"RBRACK",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//]
            }
            else {
                child = new TreeNode(p,tableId,"RBRACK","]");
                node.addChild(child);
                error0 = new Error("k",tokenList.get(p).getLineNum());
                errorList.add(error0);
                //System.out.println("<ConstExp> "+p);
                //p--;
            }
        }
        node.expType = expType;
        System.out.println(node.toString()+"  ede232");
        token0 = new Token("", "<LVal>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseUnaryOp(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="PLUS"||tokenList.get(p).getType()=="MINU"||tokenList.get(p).getType()=="NOT"){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            token0 = new Token("", "<UnaryOp>",0);
            tokenListPlus.add(token0);
        }
        else {
            System.out.println("error of parseUnaryOp");
        }
        return p;
    }
    public static int parseNumber(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);
        token0 = new Token("", "<Number>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseBType(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="INTTK"){
            child = new TreeNode(p,tableId,"INTTK","int");
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//int
            token0 = new Token("", "<BType>",0);
            tokenListPlus.add(token0);
        }
        else {
            System.out.println("error of parseBType");
        }
        return p;
    }
    public static int parseInitVal(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="LBRACE"){
            if(tokenList.get(p+1).getType()=="RBRACE"){
                child = new TreeNode(p,tableId,"LBRACE",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
                child = new TreeNode(p,tableId,"RBRACE",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            else{
                child = new TreeNode(p,tableId,"LBRACE",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);

                child = new TreeNode(p,tableId,"<InitVal>");
                node.addChild(child);
                p=parseInitVal(p, tableId, child);
                while(tokenList.get(p).getType()=="COMMA"){
                    child = new TreeNode(p,tableId,"COMMA",tokenList.get(p).getContent());
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);
                    child = new TreeNode(p,tableId,"<InitVal>");
                    node.addChild(child);
                    p=parseInitVal(p, tableId, child);
                }
                child = new TreeNode(p,tableId,"RBRACE",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
        }
        else {
            child = new TreeNode(p,tableId,"<Exp>");
            node.addChild(child);
            p=parseExp(p, tableId, child);
        }
        token0 = new Token("", "<InitVal>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseConstInitVal(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="LBRACE"){
            if(tokenList.get(p+1).getType()=="RBRACE"){
                child = new TreeNode(p,tableId,"LBRACE","{");
                node.addChild(child);
                p=parseTerminal(p, tableId, child);

                child = new TreeNode(p,tableId,"RBRACE","}");
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            else{
                child = new TreeNode(p,tableId,"LBRACE","{");
                node.addChild(child);
                p=parseTerminal(p, tableId, child);

                child = new TreeNode(p,tableId,"<ConstInitVal>");
                node.addChild(child);
                p=parseConstInitVal(p, tableId, child);
                while(tokenList.get(p).getType()=="COMMA"){
                    child = new TreeNode(p,tableId,"COMMA",",");
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);

                    child = new TreeNode(p,tableId,"<ConstInitVal>");
                    node.addChild(child);
                    p=parseConstInitVal(p, tableId, child);
                }
                child = new TreeNode(p,tableId,"RBRACE","}");
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//}
            }
        }
        else {
            child = new TreeNode(p,tableId,"<ConstExp>");
            node.addChild(child);
            p=parseConstExp(p, tableId, child);
        }
        token0 = new Token("", "<ConstInitVal>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseMainFuncDef(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"INTTK","int");
        node.addChild(child);
        p=parseTerminal(p, tableId, child);//int

        child = new TreeNode(p,tableId,"MAINTK","main");
        node.addChild(child);
        p=parseTerminal(p, tableId, child);//main

        child = new TreeNode(p,tableId,"LPARENT","(");
        node.addChild(child);
        p=parseTerminal(p, tableId, child);//(

        child = new TreeNode(p,tableId,"RPARENT",")");
        node.addChild(child);
        p=parseTerminal(p, tableId, child);

        child = new TreeNode(p,tableId,"<Block>");
        node.addChild(child);
        p=parseBlock(p, tableId, child);
        token0 = new Token("", "<MainFuncDef>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseFuncType(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);
        token0 = new Token("", "<FuncType>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseBlock(int p, int tableId, TreeNode node){
        ArrayList<Integer> FuncFParamsList = node.FuncFParams;
        ArrayList<String> FuncFParamsNameList = node.FuncFParamsName;
        //这两行建语法树后作废
        //node.FuncFParams = null;
        //node.FuncFParamsName = null;
        child = new TreeNode(p,tableId,"LBRACE","{");
        node.addChild(child);
        p=parseTerminal(p, tableId, child);//{

        //新建一个表并为其分配id，并设置好父id(如果这个表是函数定义对应的那个表，还要添加FuncDef传来的形参)
        int id  = tableList.size();
        node.tableId = id;
        Table table0 = new Table(id,tableId);
        if(FuncFParamsList!=null){
            table0.isFunction = 1;
            table0.isInt = node.isInt;
            for(int i=0;i<FuncFParamsList.size();i++){
                //table0.symbolList.put(FuncFParamsList.get(i));
                Symbol s =new Symbol(id,FuncFParamsNameList.get(i),FuncFParamsList.get(i),0);
                table0.symbolList.put(FuncFParamsNameList.get(i),s);
            }
        }
        tableList.add(table0);
        while(tokenList.get(p).getType()!="RBRACE"){
            System.out.print(tokenList.get(p).getContent()+" ");
            System.out.println(tokenList.get(p+1).getContent());

            child = new TreeNode(p,id,"<BlockItem>");
            node.addChild(child);
            p=parseBlockItem(p, id, child);
        }

        child = new TreeNode(p,tableId,"RBRACE","}");
        node.addChild(child);
        p=parseTerminal(p, tableId, child);//}
        token0 = new Token("", "<Block>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseBlockItem(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="CONSTTK"||tokenList.get(p).getType()=="INTTK"){
            child = new TreeNode(p,tableId,"<Decl>");
            node.addChild(child);
            p=parseDecl(p, tableId, child);
        }
        else {
            child = new TreeNode(p,tableId,"<Stmt>");
            node.addChild(child);
            p=parseStmt(p, tableId, child);
        }
        token0 = new Token("", "<BlockItem>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseStmt(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="PRINTFTK"){
            int line = tokenList.get(p).getLineNum();
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//printf
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//(
            //为了检测printf中格式字符与表达式个数是否匹配，须得知FormatString里“%d”的数量
            String formatString =tokenList.get(p).getContent();
            int numOfPer = 0;
            int numOfComma = 0;
            for(int i=0; i<formatString.length(); i++){
                if((i!=formatString.length()-1)&&(formatString.charAt(i)=='%'&&formatString.charAt(i+1)=='d')){
                    numOfPer++;
                }
            }
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//FormatString
            while(tokenList.get(p).getType()=="COMMA"){
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
                child = new TreeNode(p,tableId,"<Exp>");
                node.addChild(child);
                p=parseExp(p, tableId, child);
                numOfComma++;
            }
            if(numOfPer!=numOfComma){
                error0 = new Error("l",line);
                errorList.add(error0);
            }
            /*
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, node);
            */
            if(tokenList.get(p).getType().equals("RPARENT")) {
                child = new TreeNode(p,tableId,"RPARENT",")");
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//)
            }
            else {
                child = new TreeNode(p,tableId,"RPARENT",")");
                node.addChild(child);
                error0 = new Error("j",tokenList.get(p).getLineNum());
                errorList.add(error0);
            }
            if(tokenList.get(p).getType()!="SEMICN"){
                line = tokenList.get(p-1).getLineNum();
                error0 = new Error("i",line);
                errorList.add(error0);
                //p++;
            }
            else {
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
        }
        else if(tokenList.get(p).getType()=="BREAKTK"){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            if(tokenList.get(p).getType()!="SEMICN"){
                int line = tokenList.get(p-1).getLineNum();
                error0 = new Error("i",line);
                errorList.add(error0);
                p++;
            }
            else {
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
        }
        else if(tokenList.get(p).getType()=="CONTINUETK"){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            if(tokenList.get(p).getType()!="SEMICN"){
                int line = tokenList.get(p-1).getLineNum();
                error0 = new Error("i",line);
                errorList.add(error0);
                p++;
            }
            else {
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
        }
        else if(tokenList.get(p).getType()=="IFTK"){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//if
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//(
            child = new TreeNode(p,tableId,"<Cond>");
            node.addChild(child);
            p=parseCond(p, tableId, child);
            if(tokenList.get(p).getType().equals("RPARENT")) {
                child = new TreeNode(p,tableId,"RPARENT",")");
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//)
            }
            else {
                child = new TreeNode(p,tableId,"RPARENT",")");
                node.addChild(child);
                error0 = new Error("j",tokenList.get(p).getLineNum());
                errorList.add(error0);
            }
            /*
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//(
            */
            child = new TreeNode(p,tableId,"<Stmt>");
            node.addChild(child);
            p=parseStmt(p, tableId, child);
            if(tokenList.get(p).getType()=="ELSETK"){
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
                child = new TreeNode(p,tableId,"<Stmt>");
                node.addChild(child);
                p=parseStmt(p, tableId, child);
            }
        }
        else if(tokenList.get(p).getType()=="FORTK"){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//for
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//(
            if(tokenList.get(p).getType()=="SEMICN"){
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            else {
                child = new TreeNode(p,tableId,"<ForStmt>");
                node.addChild(child);
                p=parseForStmt(p, tableId, child);
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            if(tokenList.get(p).getType()=="SEMICN"){
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            else {
                child = new TreeNode(p,tableId,"<Cond>");
                node.addChild(child);
                p=parseCond(p, tableId, child);
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            if(tokenList.get(p).getType()=="RPARENT"){
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            else {
                child = new TreeNode(p,tableId,"<ForStmt>");
                node.addChild(child);
                p=parseForStmt(p, tableId, child);
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            tokenList.get(p).loopStart=1;
            child = new TreeNode(p,tableId,"<Stmt>");
            node.addChild(child);
            p=parseStmt(p, tableId, child);
            tokenList.get(p-1).loopEnd=1;
        }
        else if(tokenList.get(p).getType()=="RETURNTK"){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//return
            if(Objects.equals(tokenList.get(p).getType(), "SEMICN")){
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            //这个FIRST集表明return后面还有一个Exp
            else if(tokenList.get(p).getType()=="LPARENT"||tokenList.get(p).getType()=="PLUS"||tokenList.get(p).getType()=="MINU"||tokenList.get(p).getType()=="NOT"||tokenList.get(p).getType()=="IDENFR"|| Objects.equals(tokenList.get(p).getType(), "INTCON")){
                child = new TreeNode(p,tableId,"<Exp>");
                node.addChild(child);
                p=parseExp(p, tableId, child);
                if(Objects.equals(tokenList.get(p).getType(), "SEMICN")){
                    System.out.println("sudqiwejoqiwd");
                    child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);
                }
                else {
                    int line = tokenList.get(p-1).getLineNum();
                    error0 = new Error("i",line);
                    errorList.add(error0);
                    child = new TreeNode(p,tableId,"SEMICN",";");
                    node.addChild(child);
                }
            }
            else{//只有一个return，且缺分号
                int line = tokenList.get(p-1).getLineNum();
                error0 = new Error("i",line);
                errorList.add(error0);
                child = new TreeNode(p,tableId,"SEMICN",";");
                node.addChild(child);
                //p=parseTerminal(p, tableId, child);
                //p++;
            }
        }
        else if(tokenList.get(p).getType()=="LBRACE"){
            child = new TreeNode(p,tableId,"<Block>");
            node.addChild(child);
            p=parseBlock(p, tableId, child);
        }
        else if(tokenList.get(p).getType()=="LPARENT"||tokenList.get(p).getType()=="INTCON"||tokenList.get(p).getType()=="SEMICN"){
            if(tokenList.get(p).getType()=="SEMICN"){
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            else {
                child = new TreeNode(p,tableId,"<Exp>");
                node.addChild(child);
                p=parseExp(p, tableId, child);
                if(tokenList.get(p).getType()!="SEMICN"){
                    int line = tokenList.get(p-1).getLineNum();
                    error0 = new Error("i",line);
                    errorList.add(error0);
                }
                else {
                    child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);
                }
            }
        }
        /*  else if(tokenList.get(p).getType()=="IDENFR"){
            int p0 = p;
            int flag=0;
            if(tokenList.get(p+1).getType()=="LPARENT"){
                flag = 0;
                p=parseExp(p);
                if(tokenList.get(p).getType()!="SEMICN"){
                    int line = tokenList.get(p-1).getLineNum();
                    error0 = new Error("i",line);
                    errorList.add(error0);
                }
                else {
                    p=parseTerminal(p);
                }
            }
            else {
                p=parseLval(p);
                if(tokenList.get(p+1).getType()=="ASSIGN"){
                    p=parseTerminal(p);//=
                    p=parseExp(p);
                }
                else if(tokenList.get(p+1).getType()=="SEMICN"){
                    p=parseTerminal(p);
                }
            }
            if(flag==0){//没等号
                p=parseExp(p);
                if(tokenList.get(p).getType()!="SEMICN"){
                    int line = tokenList.get(p-1).getLineNum();
                    error0 = new Error("i",line);
                    errorList.add(error0);
                }
                else {
                    p=parseTerminal(p);
                }
            }
            else{
                p=parseLval(p);
                p=parseTerminal(p);
                if(tokenList.get(p).getType()=="GETINTTK"){
                    p=parseTerminal(p);
                    p=parseTerminal(p);
                    p=parseTerminal(p);
                    if(tokenList.get(p).getType()!="SEMICN"){
                        int line = tokenList.get(p-1).getLineNum();
                        error0 = new Error("i",line);
                        errorList.add(error0);
                    }
                    else {
                        p=parseTerminal(p);
                    }
                }
                else{
                    p=parseExp(p);
                    if(tokenList.get(p).getType()!="SEMICN"){
                        int line = tokenList.get(p-1).getLineNum();
                        error0 = new Error("i",line);
                        errorList.add(error0);
                    }
                    else {
                        p=parseTerminal(p);
                    }
                }
            }
        }   */
        else if(tokenList.get(p).getType()=="IDENFR"){
            int p0 = p;
            int flag=0;
            while(tokenList.get(p0).getContent()!=";"){
                if(tokenList.get(p0).getContent()=="="){
                    flag=1;
                }
                p0++;
            }
            if(flag==0){
                child = new TreeNode(p,tableId,"<Exp>");
                node.addChild(child);
                p=parseExp(p, tableId, child);
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
            else{
                child = new TreeNode(p,tableId,"<LVal>");
                node.addChild(child);
                p= parseLVal(p, tableId, child);
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
                if(tokenList.get(p).getType()=="GETINTTK"){
                    child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);//getint
                    child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);//(
                    /*
                    child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);//)
                    */
                    if(tokenList.get(p).getType().equals("RPARENT")) {
                        child = new TreeNode(p,tableId,"RPARENT",")");
                        node.addChild(child);
                        p=parseTerminal(p, tableId, child);//)
                    }
                    else {
                        child = new TreeNode(p,tableId,"RPARENT",")");
                        node.addChild(child);
                        error0 = new Error("j",tokenList.get(p).getLineNum());
                        errorList.add(error0);
                    }
                    child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);//;
                }
                else{
                    child = new TreeNode(p,tableId,"<Exp>");
                    node.addChild(child);
                    p=parseExp(p, tableId, child);
                    child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);
                }
            }
        }
        else if(tokenList.get(p).getType()=="PLUS"||tokenList.get(p).getType()=="MINU"||tokenList.get(p).getType()=="NOT"){
            child = new TreeNode(p,tableId,"<Exp>");
            node.addChild(child);
            p=parseExp(p, tableId, child);
            if(tokenList.get(p).getType()!="SEMICN"){
                int line = tokenList.get(p-1).getLineNum();
                error0 = new Error("i",line);
                errorList.add(error0);
            }
            else {
                child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);
            }
        }
        else{
            System.out.println("error of parseStmt");
            System.out.println(tokenList.get(p).getLineNum());
            System.out.println(tokenList.get(p).getContent());
        }
        token0 = new Token("", "<Stmt>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseDecl(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="CONSTTK"){
            child = new TreeNode(p,tableId,"<ConstDecl>");
            node.addChild(child);
            p=parseConstDecl(p, tableId, child);
            token0 = new Token("", "<Decl>",0);
            tokenListPlus.add(token0);
        }
        else if(tokenList.get(p).getType()=="INTTK"){
            child = new TreeNode(p,tableId,"<VarDecl>");
            node.addChild(child);
            p=parseVarDecl(p, tableId, child);
            token0 = new Token("", "<Decl>",0);
            tokenListPlus.add(token0);
        }
        else {
            System.out.println("error of parseDecl");
        }
        return p;
    }
    public static int parseConstDecl(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()=="CONSTTK"){
            child = new TreeNode(p,tableId,"CONSTTK","const");
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
        }
        else {
            System.out.println("error of parseConstDecl1");
        }
        child = new TreeNode(p,tableId,"<BType>");
        node.addChild(child);
        p=parseBType(p, tableId, child);

        child = new TreeNode(p,tableId,"<ConstDef>");
        node.addChild(child);
        p=parseConstDef(p, tableId, child);
        while(tokenList.get(p).getType()=="COMMA"){
            child = new TreeNode(p,tableId,"COMMA",",");
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//,

            child = new TreeNode(p,tableId,"<ConstDef>");
            node.addChild(child);
            p=parseConstDef(p, tableId, child);
        }
        if(tokenList.get(p).getType()!="SEMICN"){
            int line = tokenList.get(p-1).getLineNum();
            error0 = new Error("i",line);
            errorList.add(error0);
            p++;
        }
        else {
            child = new TreeNode(p,tableId,"SEMICN",";");
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//;
        }
        token0 = new Token("", "<ConstDecl>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseConstDef(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()!="IDENFR"){
            System.out.println("error1 in parseConstDef");
        }
        int p0 = p;
        int dem = 0;
        //**Ident
        child = new TreeNode(p,tableId,"IDENFR",tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);
        //这个循环最多两次
        while(tokenList.get(p).getType()=="LBRACK"){
            child = new TreeNode(p,tableId,"LBRACK",tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//[

            child = new TreeNode(p,tableId,"<ConstExp>");
            node.addChild(child);
            p=parseConstExp(p, tableId, child);
            //System.out.println("<ConstExp> "+p);
            //检测有没有缺右方括号
            if(tokenList.get(p).getType().equals("RBRACK")) {
                child = new TreeNode(p,tableId,"RBRACK",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//]
            }
            else {
                child = new TreeNode(p,tableId,"RBRACK","]");
                node.addChild(child);
                error0 = new Error("k",tokenList.get(p).getLineNum());
                errorList.add(error0);
                //System.out.println("<ConstExp> "+p);
                //p--;
            }
            dem++;
        }
        if(tokenList.get(p).getType()!="ASSIGN"){
            System.out.println("error2 in parseConstDef");
        }
        String token = tokenList.get(p0).getContent();
        if(queryNameDef(token, node.tableId)==1){
            error0 = new Error("b",getLine(node));
            errorList.add(error0);
        }
        else {
            Symbol symbol = new Symbol(tableId,token,dem,1);
            tableList.get(tableId).symbolList.put(token,symbol);
        }

        child = new TreeNode(p,tableId,"ASSIGN",tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);//=

        child = new TreeNode(p,tableId,"<ConstInitVal>");
        node.addChild(child);
        p=parseConstInitVal(p, tableId, child);
        token0 = new Token("", "<ConstDef>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseVarDef(int p, int tableId, TreeNode node){
        if(tokenList.get(p).getType()!="IDENFR"){
            System.out.println("error1 in parseVarDef");
        }
        int p0 = p;
        int dem = 0;
        child = new TreeNode(p,tableId,"IDENFR",tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);
        while(tokenList.get(p).getType()=="LBRACK"){
            child = new TreeNode(p,tableId,"LBRACK",tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//[

            child = new TreeNode(p,tableId,"<ConstExp>");
            node.addChild(child);
            p=parseConstExp(p, tableId, child);

            if(tokenList.get(p).getType().equals("RBRACK")) {
                child = new TreeNode(p,tableId,"RBRACK",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//]
            }
            else {
                child = new TreeNode(p,tableId,"RBRACK","]");
                node.addChild(child);
                error0 = new Error("k",tokenList.get(p).getLineNum());
                errorList.add(error0);
                //System.out.println("<ConstExp> "+p);
                //p--;
            }
            dem++;
        }
        if(tokenList.get(p).getType()=="ASSIGN"){
            child = new TreeNode(p,tableId,"ASSIGN",tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//=
            child = new TreeNode(p,tableId,"<InitVal>");
            node.addChild(child);
            p=parseInitVal(p, tableId, child);
        }

        String token = tokenList.get(p0).getContent();
        if(queryNameDef(token, node.tableId)==1){
            error0 = new Error("b",getLine(node));
            errorList.add(error0);
        }
        else {
            Symbol symbol = new Symbol(tableId,token,dem,0);
            tableList.get(tableId).symbolList.put(token,symbol);
        }

        token0 = new Token("", "<VarDef>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static int parseVarDecl(int p, int tableId, TreeNode node){
        child = new TreeNode(p,tableId,"<BType>");
        node.addChild(child);
        p=parseBType(p, tableId, child);

        child = new TreeNode(p,tableId,"<VarDef>");
        node.addChild(child);
        p=parseVarDef(p, tableId, child);
        while (tokenList.get(p).getType()=="COMMA"){
            child = new TreeNode(p,tableId,"COMMA",",");
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
            child = new TreeNode(p,tableId,"<VarDef>");
            node.addChild(child);
            p=parseVarDef(p, tableId, child);
        }
        if(tokenList.get(p).getType()!="SEMICN"){
            int line = tokenList.get(p-1).getLineNum();
            error0 = new Error("i",line);
            errorList.add(error0);
            p++;
        }
        else {
            child = new TreeNode(p,tableId,"SEMICN",";");
            node.addChild(child);
            p=parseTerminal(p, tableId, child);
        }
        token0 = new Token("", "<VarDecl>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static FuncFParam parseFuncFParam(int p, int tableId, TreeNode node){
        FuncFParam f = new FuncFParam();
        int type = 0;
        child = new TreeNode(p,tableId,"<BType>");
        node.addChild(child);
        p=parseBType(p, tableId, child);
        //解析Ident
        f.name = tokenList.get(p).getContent();
        child = new TreeNode(p,tableId,"IDENFR",tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);
        if(tokenList.get(p).getType()=="LBRACK"){
            child = new TreeNode(p,tableId,"LBRACK",tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//[
            /*
            child = new TreeNode(p,tableId,"RBRACK",tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//]
            */
            if(tokenList.get(p).getType().equals("RBRACK")) {
                child = new TreeNode(p,tableId,"RBRACK","]");
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//]
            }
            else {
                child = new TreeNode(p,tableId,"RBRACK","]");
                node.addChild(child);
                error0 = new Error("k",tokenList.get(p).getLineNum());
                errorList.add(error0);

            }
            type++;
            if(tokenList.get(p).getType()=="LBRACK"){
                child = new TreeNode(p,tableId,"LBRACK",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//[
                child = new TreeNode(p,tableId,"<ConstExp>");
                node.addChild(child);
                p=parseConstExp(p, tableId, child);
                /*
                child = new TreeNode(p,tableId,"RBRACK",tokenList.get(p).getContent());
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//]
                type++;
                 */
                if(tokenList.get(p).getType().equals("RBRACK")) {
                    child = new TreeNode(p,tableId,"RBRACK","]");
                    node.addChild(child);
                    p=parseTerminal(p, tableId, child);//]
                }
                else {
                    child = new TreeNode(p,tableId,"RBRACK","]");
                    node.addChild(child);
                    error0 = new Error("k",tokenList.get(p).getLineNum());
                    errorList.add(error0);
                }
            }
        }
        f.type=type;
        f.p=p;
        token0 = new Token("", "<FuncFParam>",0);
        tokenListPlus.add(token0);
        return f;
    }
    public static FuncFParam parseFuncFParams(int p, int tableId, TreeNode node){
        //每一个parseFuncFParam都要返回一个函数参数类型
        FuncFParam f0 = new FuncFParam();//这个用来存参数表
        FuncFParam f = new FuncFParam();
        child = new TreeNode(p,tableId,"<FuncFParam>");
        node.addChild(child);
        f=parseFuncFParam(p, tableId, child);
        p=f.p;
        f0.FuncFParams.add(f.type);
        f0.FuncFParamName.add(f.name);
        //int funcType = node.children.get(node.children.size()-1).FuncFParam;
        //node.FuncFParams.add(funcType);
        while(tokenList.get(p).getType()=="COMMA"){
            child = new TreeNode(p,tableId,"COMMA",tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//,
            int p_ident = tokenList.get(p).getLineNum();
            //System.out.println("p_ident "+p_ident);
            child = new TreeNode(p,tableId,"<FuncFParam>");
            node.addChild(child);
            f=parseFuncFParam(p, tableId, child);
            p=f.p;

            String token = f.name;
            if(f0.FuncFParamName.contains(token)){
                error0 = new Error("b",p_ident);
                errorList.add(error0);
            }
            else {
                f0.FuncFParams.add(f.type);
                f0.FuncFParamName.add(f.name);
            }
        }
        f0.p=p;
        token0 = new Token("", "<FuncFParams>",0);
        tokenListPlus.add(token0);
        return f0;
    }
    public static int parseFuncDef(int p, int tableId, TreeNode node){
        //要给当前的table添加一个Symbol
        ArrayList<Integer> FuncFParamsList = new ArrayList<Integer>();
        ArrayList<String> FuncFParamsNameList = new ArrayList<String>();
        int funcType = 3764;
        if(tokenList.get(p).getContent().equals("void")){
            funcType = 1;
        }
        else if(tokenList.get(p).getContent().equals("int")){
            funcType = 0;
        }
        child = new TreeNode(p,tableId,"<FuncType>");
        node.addChild(child);
        p=parseFuncType(p, tableId, child);
        String token = tokenList.get(p).getContent();
        int lineNum = tokenList.get(p).getLineNum();
        child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);//Ident
        child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
        node.addChild(child);
        p=parseTerminal(p, tableId, child);//(

        if(Objects.equals(tokenList.get(p).getType(), "RPARENT")){
            child = new TreeNode(p,tableId,tokenList.get(p).getType(),tokenList.get(p).getContent());
            node.addChild(child);
            p=parseTerminal(p, tableId, child);//)
        }
        else if(Objects.equals(tokenList.get(p).getType(),"LBRACE")){
            child = new TreeNode(p,tableId,"RPARENT","j");
            node.addChild(child);
            error0 = new Error("j",tokenList.get(p).getLineNum());
            errorList.add(error0);
        }
        else {
            //这个函数的参数数量和类型都需从FuncFParams中得知
            //也不一定是有参数，还可能是缺右小括号
            child = new TreeNode(p,tableId,"<FuncFParams>");
            node.addChild(child);
            FuncFParam f = parseFuncFParams(p, tableId, child);
            p=f.p;
            FuncFParamsList = f.FuncFParams;
            FuncFParamsNameList = f.FuncFParamName;
            //System.out.println("2132+"+FuncFParamsNameList+"="+FuncFParamsNameList);
            node.FuncFParams=FuncFParamsList;
            node.FuncFParamsName=FuncFParamsNameList;

            if(tokenList.get(p).getType().equals("RPARENT")) {
                child = new TreeNode(p,tableId,"RPARENT",")");
                node.addChild(child);
                p=parseTerminal(p, tableId, child);//)
            }
            else {
                child = new TreeNode(p,tableId,"RPARENT",")");
                node.addChild(child);
                error0 = new Error("j",tokenList.get(p).getLineNum());
                errorList.add(error0);
            }
        }
        child = new TreeNode(p,tableId,"<Block>");
        child.FuncFParams = FuncFParamsList;
        child.FuncFParamsName=FuncFParamsNameList;
        child.isInt = 1-funcType;
        node.addChild(child);
        p=parseBlock(p, tableId, child);
        Symbol symbol = new Symbol(tableId,token,-1,funcType,FuncFParamsList.size(),FuncFParamsList,FuncFParamsNameList);
        if(queryNameDef(token,tableId)==1){
            error0 = new Error("b",lineNum);
            errorList.add(error0);
        }
        else{
            tableList.get(tableId).symbolList.put(token,symbol);
        }
        token0 = new Token("", "<FuncDef>",0);
        tokenListPlus.add(token0);
        return p;
    }
    public static boolean isFormatStringValid(String s) {
        int n = s.length();
        if (n < 2 || s.charAt(0) != '"' || s.charAt(n - 1) != '"') {
            // 格式字符串必须以双引号包围
            return false;
        }
        boolean ans = true;
        for (int i = 1; i < n - 1; i++) {
            char currentChar = s.charAt(i);
            int ascii = (int) currentChar;
            if(ascii==37){//这是%
                if((int)s.charAt(i+1)==100){//这是d
                    continue;
                }
                else {
                    ans=false;
                    break;
                }
            }
            if(!(ascii==32||ascii==33||(ascii>=40&&ascii<=126)))
            {
                //System.out.println("2"+ascii);
                ans=false;
                break;
            }
            if(ascii==92){
                //System.out.println("ewde");
                if((int)s.charAt(i+1)!=110){
                    ans=false;
                }
            }
        }
        return ans;
    }
    public static String getCode(String s) {
        if ("main".equals(s)) {
            return "MAINTK";
        } else if ("const".equals(s)) {
            return "CONSTTK";
        } else if ("int".equals(s)) {
            return "INTTK";
        } else if ("break".equals(s)) {
            return "BREAKTK";
        } else if ("continue".equals(s)) {
            return "CONTINUETK";
        } else if ("if".equals(s)) {
            return "IFTK";
        } else if ("else".equals(s)) {
            return "ELSETK";
        } else if ("for".equals(s)) {
            return "FORTK";
        } else if ("getint".equals(s)) {
            return "GETINTTK";
        } else if ("printf".equals(s)) {
            return "PRINTFTK";
        } else if ("return".equals(s)) {
            return "RETURNTK";
        } else if ("void".equals(s)) {
            return "VOIDTK";
        } else {
            return null;
        }
    }
    public static String readIdentifier(String input, int startIndex) {
        StringBuilder identifier = new StringBuilder();
        int index = startIndex;
        // 判断当前字符是否是字母、下划线或数字
        while (index < input.length() && (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
            identifier.append(input.charAt(index));
            index++;
        }
        return identifier.toString();
    }
    public static String readNumberString(String input, int startIndex) {
        StringBuilder numberString = new StringBuilder();
        int i = startIndex;

        // 从指定下标开始查找数字字符
        while (i < input.length() && !Character.isDigit(input.charAt(i))) {
            i++;
        }

        // 从找到的数字字符开始读取数字串
        while (i < input.length() && Character.isDigit(input.charAt(i))) {
            numberString.append(input.charAt(i));
            i++;
        }

        return numberString.toString();
    }
    public static String removeMultiLineComments(String input) {
        String result="";
        //真值表示现在在注释中
        boolean state=false;
        //真值表示在字符串中
        boolean state1=false;

        for(int i=0;i<input.length();i++){
            //在字符串中
            if(state1){
                if(input.charAt(i)=='"'){
                    state1=!state1;
                    result=result+input.charAt(i);
                }
                else{
                    result=result+input.charAt(i);
                }
            }
            else{
                //不在注释中
                if(!state){
                    if(input.charAt(i)=='/'&&input.charAt(i+1)=='*'){
                        state=true;
                        i++;
                    }
                    else if(input.charAt(i)=='"'){
                        state1=true;
                        result=result+input.charAt(i);
                    }
                    else{
                        result=result+input.charAt(i);
                    }
                }
                //在注释中
                else{
                    if(input.charAt(i)=='*'&&input.charAt(i+1)=='/'){
                        state=false;
                        i++;
                    }
                    else{
                        if(input.charAt(i)=='\n'){
                            result=result+"\n";
                        }
                    }
                }
            }
        }
        return result;
    }
    public static String removeSingleLineComments(String input) {
        String result="";
        //真值表示现在在注释中
        boolean state=false;
        //真值表示在字符串中
        boolean state1=false;
        for(int i=0;i<input.length();i++){
            //在字符串中
            if(state1){
                if(input.charAt(i)=='"'){
                    state1=!state1;
                    result=result+input.charAt(i);
                }
                else{
                    result=result+input.charAt(i);
                }
            }
            //不在字符串中
            else{
                //不在注释中
                if(!state){
                    if(input.charAt(i)=='/'&&input.charAt(i+1)=='/'){
                        state=true;
                        i++;
                    }
                    else if(input.charAt(i)=='"'){
                        state1=true;
                        result=result+input.charAt(i);
                    }
                    else{
                        result=result+input.charAt(i);
                    }
                }
                //在注释中
                else{
                    if(input.charAt(i)=='\n'){
                        result=result+"\n";
                        state=false;
                    }
                }
            }
        }
        return result;
    }
    public static void printTokenList(ArrayList<Token> tokenList) {
        for (Token token : tokenList) {
            System.out.println(token.toString());
        }
    }
    /*
    public static void addSymbolTable(String name){
        Symbol s = new Symbol();
        //s=name.t
        tableList.get(tableNum).addToSymbolList(s);
    }
    //给定一个变量名。判断能否插入当前符号表，能则返回0，否则返回-1
    public static int isInsertable(String name){
        Table table0 = tableList.get(tableNum);
        for(int i=0;i<table0.getSymbolList().size();i++){
            if(table0.getSymbolList().get(i).getName().equals(name)){
                return -1;
            }
        }
        return 0;
    }
     */
    public static void printTable(){
        for(int i=0;i<tableList.size();i++){
            System.out.print(tableList.get(i).isInt);
            System.out.println("id为"+tableList.get(i).id+"的表的信息有：");
            HashMap<String, Symbol> map = tableList.get(i).symbolList;
            for (Map.Entry<String, Symbol> entry : map.entrySet()) {
                //String token = entry.getKey();
                Symbol value = entry.getValue();
                System.out.println(value.toString());
            }

        }
    }
    public static void postOrderTraversalToFile(TreeNode root) {
        try {
            FileWriter fileWriter = new FileWriter("output.txt");
            postOrderTraversal(root, fileWriter);
            fileWriter.close(); // 关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void postOrderTraversal(TreeNode root, FileWriter fileWriter) throws IOException {
        if (root == null) {
            return;
        }
        // 遍历根节点的子节点
        for (TreeNode child : root.getChildren()) {
            postOrderTraversal(child, fileWriter);
        }
        // 访问根节点
        if (!(root.content == null || root.content.equals(""))) {
            if (!(root.type.equals("<BlockItem>") || root.type.equals("<Decl>") || root.type.equals("<BType>"))) {
                fileWriter.write(root.type+" "+root.content + System.lineSeparator()); // 写入文件并换行
            }
        } else {
            fileWriter.write(root.type + System.lineSeparator()); // 写入文件并换行
        }
    }
    public static void printTree(TreeNode ast){
        if (ast == null) {
            return;
        }
        // 遍历根节点的子节点
        for (TreeNode child : ast.getChildren()) {
            printTree(child);
        }
        // 访问根节点
        if(!(ast.getType()=="<BlockItem>"||ast.getType()=="<Decl>"||ast.getType()=="<BType>")){
            //System.out.println(ast.type+" "+ast.content);

            if(ast.content==""||ast.content==null){
                System.out.println(ast.type);
            }
            else{
                System.out.println(ast.content);
            }
        }
    }

    public static void errorHandle(TreeNode root) {
        if (root == null) {
            return;
        }
        // 遍历根节点的子节点
        for (TreeNode child : root.getChildren()) {
            errorHandle(child);
        }
        //重定义类错误 b
        /*
        if(Objects.equals(root.type, "<ConstDef>")||Objects.equals(root.type, "<VarDef>")){
            String ident = root.children.get(0).content;
            if(queryNameDef(ident, root.tableId)==1){
                error0 = new Error("b",getLine(root));
                errorList.add(error0);
            }
        }
        */
        //声明类错误（未定义的名字） c
        if(Objects.equals(root.type, "<LVal>")||Objects.equals(root.type, "<UnaryExp>")){
            if(root.children.get(0).type=="IDENFR"){
                String ident = root.children.get(0).content;
                if(queryNameUse(ident, root.tableId)==0){
                    //如果没查到
                    //System.out.println("rootLine "+getLine(root)+" "+root.children.get(0));
                    error0 = new Error("c",getLine(root));
                    errorList.add(error0);
                    System.out.println("c :"+ident);
                }
            }
        }
        //函数参数个数不匹配 d 类型不匹配 e
        if(Objects.equals(root.type, "<UnaryExp>")){
            if(root.children.size()==3){//形如f()
                System.out.println("wuquwedqiw   ");
                if(root.children.get(0).FuncFParamsName==null){

                }
                else if(root.children.get(0).FuncFParamsName.size()==0){

                }
                else if(root.children.get(0).FuncFParamsName.size()!=0){
                    error0 = new Error("d",getLine(root));
                    errorList.add(error0);
                }
            }
            else if(root.children.size()==4)//形如f(...)
            {
                System.out.println("ewewe   "+root.children.get(3).content);
                Symbol ident = getName(root.children.get(0).content,root.tableId);
                //System.out.println(" rfr "+root.children.get(2).FuncRParamsType.size());
                if(ident.paramTypeList.size()!=root.children.get(2).FuncRParamsType.size()){
                    error0 = new Error("d0",getLine(root));
                    errorList.add(error0);
                }
                else {//个数匹配且不为0，检测类型是否匹配 e
                    //System.out.println();
                    if(!ident.paramTypeList.equals(root.children.get(2).FuncRParamsType)){
                        error0 = new Error("e",getLine(root));
                        errorList.add(error0);
                    }
                }
            }
        }
        //无返回值的函数存在不匹配的return语句 f
        if(Objects.equals(root.type, "<Stmt>")){
            //如果不是返回值为int的函数
            if(Objects.equals(root.children.get(0).content, "return")&&tableList.get(root.tableId).isInt!=1&&root.children.size()==3){
                error0 = new Error("f",getLine(root));
                errorList.add(error0);
            }
        }
        //有返回值的函数缺少return语句 g
        if(Objects.equals(root.type, "<Block>")){
            //System.out.println("uwncqecnie "+root.children.get(root.children.size()-2).type);
            //block的id直接从node.tableId获取
            //如果这个函数体里没有语句
            if(tableList.get(root.tableId).isInt==1&&root.children.size()==2){
                error0 = new Error("g",root.children.get(root.children.size()-1).line);
                errorList.add(error0);
            }
            else{
                if(tableList.get(root.tableId).isInt==1&&root.children.get(root.children.size()-2).type=="<BlockItem>"){
                    TreeNode node_BlockItem = root.children.get(root.children.size()-2);
                    //System.out.println("21321  32  "+tokenList.get(node_BlockItem.p).getContent());
                    if(!Objects.equals(tokenList.get(node_BlockItem.p).getContent(), "return")){
                        error0 = new Error("g",root.children.get(root.children.size()-1).line);
                        errorList.add(error0);
                    }
                    //TreeNode Stmt = node_BlockItem.children.get(0);
                }
            }
        }
        //不能改变常量的值	h
        if(Objects.equals(root.type, "<Stmt>")){
            TreeNode LVal = root.children.get(0);
            if(Objects.equals(LVal.type,"<LVal>")){
                String token = LVal.children.get(0).content;
                int tableId  = LVal.tableId;
                if(getName(token,tableId)!=null){
                    if(getName(token,tableId).con==1){
                        error0 = new Error("h",getLine(LVal));
                        errorList.add(error0);
                    }
                }
            }
        }
        //在非循环块中使用break和continue语句 m
        if(Objects.equals(root.type, "<Stmt>")){
            if(Objects.equals(root.children.get(0).content, "break") || Objects.equals(root.children.get(0).content, "continue")){
                if(tokenList.get(root.p).isLoop==0){
                    error0 = new Error("m",getLine(root));
                    errorList.add(error0);
                }
            }
        }
    }
    public static int judgeReverse(TreeNode node){
        if(node.children.size()==3){
            if(node.children.get(0).type.equals("<AddExp>")||
                    node.children.get(0).type.equals("<MulExp>")||
                    node.children.get(0).type.equals("<RelExp>")||
                    node.children.get(0).type.equals("<UnaryExp>")||
                    node.children.get(0).type.equals("<EqExp>")||
                    node.children.get(0).type.equals("<LAndExp>")
            ){
                // 交换第一个和第三个节点
                TreeNode firstChild = node.children.get(0);
                TreeNode thirdChild = node.children.get(2);

                // 交换节点内容
                node.children.set(0, thirdChild);
                node.children.set(2, firstChild);
                return 1;
            }
        }
        return 0;
    }
    //递归向上查找
    public static int queryNameUse(String token,int tableId){
        System.out.println("21ds3  "+token + " "+tableId);
        while(tableId>=0){
            Table t0 = tableList.get(tableId);
            if(t0.symbolList.containsKey(token)){
                return 1;
            }
            else{
                tableId = t0.fatherId;
            }
        }
        return 0;
    }
    public static Symbol getName(String token,int tableId){
        while(tableId>=0){
            Table t0 = tableList.get(tableId);
            if(t0.symbolList.containsKey(token)){
                return t0.symbolList.get(token);
            }
            else{
                tableId = t0.fatherId;
            }
        }
        return null;
    }
    //只在当前符号表查找
    public static int queryNameDef(String token,int tableId){
        Table t0 = tableList.get(tableId);
        if(t0.symbolList.containsKey(token)){
            return 1;
        }
        return 0;
    }
    public static int getLine(TreeNode node){
        while (node.children.size()!=0){
            node=node.children.get(0);
        }
        return node.line;
    }
    public static void setLoop(){
        int loop = 0;
        for (int i=0; i<tokenList.size(); i++){
            if(tokenList.get(i).loopStart==1){
                loop++;
            }
            if(tokenList.get(i).loopEnd==1){
                loop--;
            }
            if(loop>0){
                tokenList.get(i).isLoop=1;
            }
        }
    }
    public static void sortErrorList() {
        // 使用 Collections.sort() 方法，并传入一个自定义的 Comparator
        Collections.sort(errorList, new Comparator<Error>() {
            @Override
            public int compare(Error error1, Error error2) {
                // 根据 lineNumber 属性升序排序
                return Integer.compare(error1.getLineNumber(), error2.getLineNumber());
            }
        });
    }
    /*
    public static void generate(TreeNode node) {
        if (node == null) {
            return;
        }
        // 遍历根节点的子节点
        for (TreeNode child : node.getChildren()) {
            generate(child);
        }
        if(node.type.equals("<MainFuncDef>")){
            Line line = new Line(0,"define dso_local i32 @main() {");
        }
    }*/
    public static void generate(TreeNode ast){
        //printTree(ast);
        if(ast.getType().equals("<ConstDef>")){ConstDef(ast);}
        else if(ast.getType().equals("<ConstInitVal>")){ConstInitVal(ast);}
        else if(ast.getType().equals("<ConstExp>")){ConstExp(ast);}
        else if(ast.getType().equals("<VarDef>")){VarDef(ast);}
        else if(ast.getType().equals("<InitVal>")){ConstInitVal(ast);}
        else if(ast.getType().equals("<FuncDef>")){FuncDef(ast);}
        else if(ast.getType().equals("<FuncFParams>")){FuncFParams(ast);}
        else if(ast.getType().equals("<FuncFParam>")){FuncFParam(ast);}
        else if(ast.getType().equals("<MainFuncDef>")){MainFuncDef(ast);}
        else if(ast.getType().equals("<Block>")){Block(ast);}
        else if(ast.getType().equals("<Stmt>")){Stmt(ast);}
        else if(ast.getType().equals("<ForStmt>")){
            ForStmt(ast);}
        else if(ast.getType().equals("<Number>")){Number1(ast);}
        else if(ast.getType().equals("<Exp>")){Exp(ast);}
        else if(ast.getType().equals("<Cond>")){Cond(ast);}
        else if(ast.getType().equals("<LVal>")){LVal(ast);}
        else if(ast.getType().equals("<FuncRParams>")){FuncRParams(ast);}
        else if(ast.getType().equals("<PrimaryExp>")){PrimaryExp(ast);}
        else if(ast.getType().equals("<UnaryExp>")){UnaryExp(ast);}
        else if(ast.getType().equals("<MulExp>")){AddMulExp(ast);}
        else if(ast.getType().equals("<AddExp>")){AddMulExp(ast);}
        else if(ast.getType().equals("<RelExp>")){RelEqExp(ast);}
        else if(ast.getType().equals("<EqExp>")){RelEqExp(ast);}
        else if(ast.getType().equals("<LAndExp>")){LAndExp(ast);}
        else if(ast.getType().equals("<LOrExp>")){LOrExp(ast);}
        else{
            for(int i=0;i<ast.children.size();i++){
                ast.children.get(i).setContinueId(ast.getContinueId());
                ast.children.get(i).setBreakId(ast.getBreakId());
                ast.children.get(i).setValue(ast.getValue());
                ast.children.get(i).setYesId(ast.yesId);
                ast.children.get(i).setNoId(ast.noId);
                //ast.children.get(i).setBreakId(ast.getBreakId());
                //ast.children.get(i).set
                generate(ast.children.get(i));
            }
        }
    }

    public static void ConstDef(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        TreeNode ident = a.get(0);
        ValueDetail k=ident.getKey();
        if(a.size()==3){
            if(level!=0){
                output(tags()+"%v"+regId+" = alloca i32\n");
                ident.setValue("%v"+regId);
                ident.setRegId("%v"+regId);
                regId++;
            }
            k.setDim(0);
            a.get(2).setKey(k);
            generate(a.get(2));
            k.setIntVal(a.get(2).getKey().getIntVal());
            if(level==0){
                output("@"+ident.getContent()+" = dso_local global i32 "+k.getIntVal()+"\n");
            }
            else{
                output(tags()+"store i32 "+a.get(2).getValue()+", i32* "+ident.getRegId()+"\n");
            }
        }
        else if(a.size()==6){
            int l=level;
            level=0;
            generate(a.get(2));
            level=l;
            if(level!=0){
                output(tags()+"%v"+regId+" = alloca ["+a.get(2).getValue()+" x i32]\n");
                ident.setValue("%v"+regId);
                ident.setRegId("%v"+regId);
                regId++;
            }
            k.setDim(1);
            k.setD1(Integer.parseInt(a.get(2).getValue()));
            a.get(5).setKey(k);
            generate(a.get(5));

            if(level==0){
                output("@"+ident.getContent()+" = dso_local constant ["+k.d1+" x i32] [");
                String []d1v = k.getD1Value();
                for(int i=0;i<k.d1-1;i++){
                    output("i32 "+d1v[i]+", ");
                }
                output("i32 "+k.getD1Value()[k.d1-1]+"]\n");
            }
            else{
                String []d1v = k.getD1Value();
                for(int i=0;i<k.d1;i++){
                    if(!(d1v[i].equals("NuLL"))){
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x i32], ["+k.d1+" x i32]*"+ident.getRegId()+", i32 0, i32 "+i+"\n");
                        output(tags()+"store i32 "+d1v[i]+", i32* %v"+regId+"\n");
                        regId++;
                    }
                }
            }
        }
        else if(a.size()==9){
            int l=level;
            level=0;
            generate(a.get(2));
            generate(a.get(5));
            level=l;
            if(level!=0){
                output(tags()+"%v"+regId+" = alloca ["+a.get(2).getValue()+" x [ "+a.get(5).getValue() +" x i32]]\n");
                ident.setValue("%v"+regId);
                ident.setRegId("%v"+regId);
                regId++;
            }
            k.setDim(2);
            k.setD1(Integer.parseInt(a.get(2).getValue()));
            k.setD2(Integer.parseInt(a.get(5).getValue()));
            a.get(8).setKey(k);
            generate(a.get(8));
            if(level==0){
                output("@"+ident.getContent()+" = dso_local constant ["+k.d1+" x ["+k.d2+" x i32]] [[");
                String [][]d2v = k.getD2Value();
                for(int i=0;i<k.d1-1;i++){
                    output(k.d2+" x i32] [");
                    for(int j=0;j<k.d2-1;j++){
                        output("i32 "+d2v[i][j]+", ");
                    }
                    output("i32 "+k.getD2Value()[i][k.d2-1]+"], [");
                }
                output(k.d2+" x i32] [");
                for(int j=0;j<k.d2-1;j++){
                    output("i32 "+d2v[k.d1-1][j]+", ");
                }
                output("i32 "+k.getD2Value()[k.d1-1][k.d2-1]+"]]\n");
            }
            else{
                String [][]d2v = k.getD2Value();
                for(int i=0;i<k.d1;i++){
                    for(int j=0;j<k.d2;j++){
                        if(!(d2v[i][j].equals("NuLL"))){
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x ["+k.d2+" x i32]], ["+k.d1+" x ["+k.d2+" x i32]]*"+ident.getRegId()+", i32 0, i32 "+i+", i32 "+j+"\n");
                            output(tags()+"store i32 "+d2v[i][j]+", i32* %v"+regId+"\n");
                            regId++;
                        }
                    }
                }
            }
        }
        ident.setKey(k);
        if(level==0){
            global.put(ident.getContent(),ident);
        }
        else{
            ident.setLevel(level);
            stack.add(ident);
        }
    }
    public static void ConstInitVal(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        ValueDetail k= ast.getKey();
        if(k.dimension==0){
            generate(a.get(0));
            ast.getKey().setIntVal(a.get(0).getValue());
            ast.setValue(a.get(0).getValue());
        }
        else if(k.dimension==1){
            int j=0;
            String[] d1v = k.getD1Value();
            for(int i=1;i<a.size()-1;i+=2){
                ValueDetail k1=a.get(i).getKey();
                k1.setDim(0);
                a.get(i).setKey(k1);
                generate(a.get(i));
                d1v[j]=a.get(i).getValue();
                j++;
            }
            if(j<k.d1){
                for(;j<k.d1;j++){
                    if(level!=0){d1v[j]="NuLL";}
                    else{d1v[j]="0";}
                }
            }
            k.setD1Value(d1v);
        }
        else if(k.dimension==2){
            int m=0;
            String[][] d2v = k.getD2Value();
            if(a.get(1).children.get(0).getContent().equals("{")){
                for(int i=1;i<a.size()-1;i+=2){
                    ValueDetail k1=a.get(i).getKey();
                    k1.setDim(1);
                    k1.setD1(k.d2);
                    a.get(i).setKey(k1);
                    generate(a.get(i));
                    String[] d=a.get(i).getKey().getD1Value();
                    for(int n=0;n<a.get(i).getKey().d1;n++){
                        d2v[m][n]=d[n];
                    }
                    m++;
                }
            }
            else{
                int j=0;
                for(int i=1;i<a.size()-1;i+=2){
                    ValueDetail k1=a.get(i).getKey();
                    k1.setDim(0);
                    a.get(i).setKey(k1);
                    generate(a.get(i));
                    d2v[0][j]=a.get(i).getValue();
                    j++;
                }
                if(j<k.d1){
                    for(;j<k.d1;j++){
                        if(level!=0){d2v[0][j]="NuLL";}
                        else{d2v[0][j]="0";}
                    }
                }
            }
            if(m<k.d1){
                for(;m<k.d1;m++){
                    for(int n=0;n<k.d2;n++){
                        if(level!=0){d2v[m][n]="NuLL";}
                        else{d2v[m][n]="0";}
                    }
                }
            }
        }
    }
    public static void ConstExp(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        if(a.size()==1){
            generate(a.get(0));
            ast.setValue(a.get(0).getValue());
        }
    }
    public static void VarDef(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        TreeNode ident = a.get(0);
        ValueDetail k=ident.getKey();
        if(a.size()==1||a.size()==3){
            if(level!=0){
                output(tags()+"%v"+regId+" = alloca i32\n");
                ident.setValue("%v"+regId);
                ident.setRegId("%v"+regId);
                regId++;
            }
            k.setDim(0);
            if(a.size()==3){
                generate(a.get(2));
                k.setIntVal(a.get(2).getKey().getIntVal());
                if(level!=0){
                    output(tags()+"store i32 "+a.get(2).getValue()+", i32* "+ident.getRegId()+"\n");
                }
            }
            else if(a.size()==1){
                k.setIntVal("0");
            }
            if(level==0){
                output("@"+ident.getContent()+" = dso_local global i32 "+k.getIntVal()+"\n");
            }
        }
        else if(a.size()==4||a.size()==6){
            int l=level;
            level=0;
            generate(a.get(2));
            level=l;
            if(level!=0){
                output(tags()+"%v"+regId+" = alloca ["+a.get(2).getValue()+" x i32]\n");
                ident.setValue("%v"+regId);
                ident.setRegId("%v"+regId);
                regId++;
            }
            k.setDim(1);
            k.setD1(Integer.parseInt(a.get(2).getValue()));
            String []d1v = k.getD1Value();
            if(a.size()==6){
                a.get(5).setKey(k);
                generate(a.get(5));
            }
            else if(a.size()==4){
                for(int i=0;i<k.d1;i++){
                    if(level==0){d1v[i]="0";}
                    else{d1v[i]="NuLL";}
                }
                k.setD1Value(d1v);
            }
            if(level==0){
                if(a.size()==6){
                    output("@"+ident.getContent()+" = dso_local global ["+k.d1+" x i32] [");
                    d1v=k.getD1Value();
                    for(int i=0;i<k.d1-1;i++){
                        output("i32 "+d1v[i]+", ");
                    }
                    output("i32 "+k.getD1Value()[k.d1-1]+"]\n");
                }
                else{
                    output("@"+ident.getContent()+" = dso_local global ["+k.d1+" x i32] zeroinitializer\n");
                }
            }
            else{
                d1v = k.getD1Value();
                for(int i=0;i<k.d1;i++){
                    if(!(d1v[i].equals("NuLL"))){
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x i32], ["+k.d1+" x i32]*"+ident.getRegId()+", i32 0, i32 "+i+"\n");
                        output(tags()+"store i32 "+d1v[i]+", i32* %v"+regId+"\n");
                        regId++;
                    }
                }
            }
        }
        else if(a.size()==7||a.size()==9){
            int l=level;
            level=0;
            generate(a.get(2));
            generate(a.get(5));
            level=l;
            if(level!=0){
                output(tags()+"%v"+regId+" = alloca ["+a.get(2).getValue()+" x [ "+a.get(5).getValue() +" x i32]]\n");
                ident.setValue("%v"+regId);
                ident.setRegId("%v"+regId);
                regId++;
            }
            k.setDim(2);
            k.setD1(Integer.parseInt(a.get(2).getValue()));
            k.setD2(Integer.parseInt(a.get(5).getValue()));
            String [][]d2v = k.getD2Value();
            if(a.size()==9){
                a.get(8).setKey(k);
                generate(a.get(8));
            }
            else if(a.size()==7){
                for(int i=0;i<k.d1;i++){
                    for(int j=0;j<k.d2;j++){
                        if(level==0){
                            d2v[i][j]="0";
                        }
                        else{
                            d2v[i][j]="NuLL";
                        }
                    }
                }
                k.setD2Value(d2v);
            }
            if(level==0){
                if(a.size()==9){
                    output("@"+ident.getContent()+" = dso_local global ["+k.d1+" x ["+k.d2+" x i32]] [[");
                    d2v=k.getD2Value();
                    for(int i=0;i<k.d1-1;i++){
                        output(k.d2+" x i32] [");
                        for(int j=0;j<k.d2-1;j++){
                            output("i32 "+d2v[i][j]+", ");
                        }
                        output("i32 "+k.getD2Value()[i][k.d2-1]+"], [");
                    }
                    output(k.d2+" x i32] [");
                    for(int j=0;j<k.d2-1;j++){
                        output("i32 "+d2v[k.d1-1][j]+", ");
                    }
                    output("i32 "+k.getD2Value()[k.d1-1][k.d2-1]+"]]\n");
                }
                else{
                    output("@"+ident.getContent()+" = dso_local global ["+k.d1+" x ["+k.d2+" x i32]] zeroinitializer\n");
                }
            }
            else{
                d2v = k.getD2Value();
                for(int i=0;i<k.d1;i++){
                    for(int j=0;j<k.d2;j++){
                        if(!(d2v[i][j].equals("NuLL"))){
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x ["+k.d2+" x i32]], ["+k.d1+" x ["+k.d2+" x i32]]*"+ident.getRegId()+", i32 0, i32 "+i+", i32 "+j+"\n");
                            output(tags()+"store i32 "+d2v[i][j]+", i32* %v"+regId+"\n");
                            regId++;
                        }
                    }
                }
            }
        }
        ident.setKey(k);
        if(level==0){
            global.put(ident.getContent(),ident);
        }
        else{
            ident.setLevel(level);
            stack.add(ident);
        }
    }
    public static void FuncDef(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        String Type = a.get(0).children.get(0).getContent();
        TreeNode ident = a.get(1);
        if(Type.equals("int")){Type="i32";}
        else if(Type.equals("void")){Type="void";}
        ident.setReturnType(Type);
        output("define dso_local "+Type+" @"+ident.getContent());
        global.put(ident.getContent(),ident);
        //这里好像只能是(
        if(a.get(2).getContent().equals("(")){
            output("(");
            if(a.get(4).getContent().equals(")")){
                generate(a.get(3));
                output(") {\n");
                generate(a.get(5));
            }
            else{
                output(") {\n");
                generate(a.get(4));
            }
        }
        if(Type.equals("void")){
            nowtag+=1;
            output(tags()+"ret void\n");
            nowtag-=1;
        }
        output("}\n");

    }
    public static void FuncFParams(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        generate(a.get(0));
        for(int i=2;i<a.size();i+=2){
            output(", ");
            generate(a.get(i));
        }
    }
    public static void FuncFParam(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        TreeNode ident = a.get(1);
        ident.setLevel(1);
        if(a.size()==2){
            output("i32 %v"+regId);
            ident.setValue("%v"+regId);
            ident.getKey().setAddressType("i32");
            regId++;
            stack.add(ident);
        }
        else if(a.size()==4){
            output("i32* %v"+regId);
            ident.setValue("%v"+regId);
            ident.getKey().setAddressType("i32*");
            ident.getKey().setDim(1);
            ident.getKey().setD1(0);
            regId++;
            stack.add(ident);
        }
        else if(a.size()==7){
            generate(a.get(5));
            output("["+a.get(5).getValue()+" x i32] *%v"+regId);
            ident.setValue("%v"+regId);
            ident.getKey().setDim(2);
            ident.getKey().setD1(0);
            ident.getKey().setD2(Integer.parseInt(a.get(5).getValue()));
            ident.getKey().setAddressType("["+a.get(5).getValue()+" x i32]*");
            regId++;
            stack.add(ident);
        }

    }
    public static void MainFuncDef(TreeNode ast){
        output("\ndefine dso_local i32 @main() {\n");
        generate(ast.children.get(4));//Block
        output("}\n");
    }
    public static void Block(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        for(int i=0;i<a.size();i++){
            if(a.get(i).getContent().equals("{")){
                if(level==0){
                    nowtag+=1;
                }
                level+=1;
                if(level==1){
                    for(int j=stack.size()-1;j>=0;j--){
                        if(stack.get(j).getRegId().equals("")&&stack.get(j).level==1){
                            output(tags()+"%v"+regId+" = alloca "+stack.get(j).getKey().addressType+"\n");
                            stack.get(j).setRegId("%v"+regId);
                            output(tags()+"store "+stack.get(j).getKey().addressType+" "+stack.get(j).getValue()+", "+stack.get(j).getKey().addressType+" * "+stack.get(j).getRegId()+"\n");
                            regId++;
                        }
                    }
                }
            }
            else if(a.get(i).getContent().equals("}")){
                for(int j=stack.size()-1;j>=0;j--){
                    if(stack.get(j).level==level){stack.remove(j);}
                }
                level-=1;
                if(level==0){
                    nowtag-=1;
                }
            }
            else{
                a.get(i).setContinueId(ast.getContinueId());
                a.get(i).setBreakId(ast.getBreakId());
                System.out.println("test2 "+ast.getBreakId());
                System.out.println(a.get(i).type);
                generate(a.get(i));
            }
        }
    }
    public static void Stmt(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        if(a.get(0).getType().equals("<Block>")){
            a.get(0).setContinueId(ast.getContinueId());
            a.get(0).setBreakId(ast.getBreakId());
            generate(a.get(0));
        }
        else if(a.get(0).getContent().equals("return")){
            if(a.get(1).getContent().equals(";")){
                output(tags()+"ret void\n");
            }
            else{
                generate(a.get(1));//Exp
                output(tags()+"ret i32 "+a.get(1).getValue()+"\n");
            }

        }
        else if(a.get(0).getType().equals("<LVal>")){
            generate(a.get(0));//LVal
            if(a.get(2).getType().equals("<Exp>")){
                generate(a.get(2));//Exp
                output(tags()+"store i32 "+a.get(2).getValue()+", i32* "+a.get(0).getRegId()+"\n");
            }
            else if(a.get(2).getContent().equals("getint")){
                output(tags()+"%v"+regId+" = call i32 @getint()"+"\n");
                output(tags()+"store i32 "+"%v"+regId+", i32* "+a.get(0).getRegId()+"\n");
                regId++;
            }
        }
        else if(a.get(0).getType().equals("<Exp>")){generate(a.get(0));}
        else if(a.get(0).getContent().equals("printf")){
            int parNum=4;
            String s=a.get(2).getContent();
            for(int i=1;i<s.length()-1;i++){
                if(s.charAt(i)=='%'&&s.charAt(i+1)=='d'){
                    i++;
                    generate(a.get(parNum));
                    parNum+=2;
                }
            }
            parNum=4;
            for(int i=1;i<s.length()-1;i++){
                if(s.charAt(i)=='%'&&s.charAt(i+1)=='d'){
                    i++;
                    output(tags()+"call void @putint(i32 "+a.get(parNum).getValue()+")\n");
                    parNum+=2;
                }
                else if(s.charAt(i)=='\\'&&s.charAt(i+1)=='n'){
                    i++;
                    output(tags()+"call void @putch(i32 10)\n");

                }
                else{
                    output(tags()+"call void @putch(i32 "+(int) s.charAt(i)+")\n");
                }
            }


        }
        else if(a.get(0).getContent().equals("if")){
            output(tags()+"br label %v"+regId+"\n");
            output("\nv"+regId+":\n");
            a.get(2).setYesId(regId+1);
            int YesId = regId+1;
            int NoId=0;
            int StmtId;
            if(a.size()>5){
                a.get(2).setNoId(regId+2);
                a.get(2).setStmtId(regId+3);
                a.get(4).setStmtId(regId+3);
                a.get(4).setContinueId(ast.getContinueId());
                a.get(4).setBreakId(ast.getBreakId());
                a.get(6).setStmtId(regId+3);
                a.get(6).setContinueId(ast.getContinueId());
                a.get(6).setBreakId(ast.getBreakId());
                NoId = regId+2;
                StmtId = regId+3;
                regId+=4;
            }
            else{
                a.get(2).setNoId(regId+2);
                a.get(2).setStmtId(regId+2);
                a.get(4).setStmtId(regId+2);
                a.get(4).setContinueId(ast.getContinueId());
                a.get(4).setBreakId(ast.getBreakId());
                StmtId = regId+2;
                regId+=3;
            }
            generate(a.get(2));
            output("\nv"+YesId+":\n");
            generate(a.get(4));
            if(a.size()>5){
                output("\nv"+NoId+":\n");
                generate(a.get(6));
            }
            output("\nv"+StmtId+":\n");
        }
        else if(a.get(0).getContent().equals("while")){
            output(tags()+"br label %v"+regId+"\n");
            output("\nv"+regId+":\n");
            int YesId = regId+1;
            int StmtId=regId+2;
            a.get(2).setYesId(regId+1);
            a.get(2).setNoId(regId+2);
            a.get(2).setStmtId(regId+2);
            a.get(4).setStmtId(regId);
            a.get(4).setBreakId(regId+2);
            a.get(4).setContinueId(regId);
            regId+=3;
            generate(a.get(2));
            output("\nv"+YesId+":\n");
            generate(a.get(4));
            output("\nv"+StmtId+":\n");
        }
        else if(a.get(0).getContent().equals("for")){
            output(tags()+"br label %v"+regId+"\n");
            output("\nv"+regId+":\n");
            int count=0;
            int CondId=regId+1;
            int YesId=regId+2;
            int NoId=regId+3;
            int StmtId=regId+4;
            regId+=5;
            int for2=0;
            int i=0;
            boolean hasForStmt=false;
            boolean hasCond=false;
            for(;i<a.size();i++){
                if(count>=2){break;}
                if(a.get(i).getContent().equals(";")){count+=1;}
                if(a.get(i).getType().equals("<ForStmt>")){
                    hasForStmt=true;
                    a.get(i).setStmtId(CondId);
                    generate(a.get(i));

                }
                if(a.get(i).getType().equals("<Cond>")){
                    output("\nv"+CondId+":\n");
                    hasCond=true;
                    a.get(i).setYesId(YesId);
                    a.get(i).setNoId(NoId);
                    a.get(i).setStmtId(CondId);
                    generate(a.get(i));
                }
                if(count==1&&!hasForStmt){
                    output(tags()+"br label %v"+CondId+"\n");
                }
                if(count==2&&!hasCond){
                    output("\nv"+CondId+":\n");
                    output(tags()+"br label %v"+YesId+"\n");
                }
            }
            for(;i<a.size();i++){
                if(a.get(i).getType().equals("<ForStmt>")){for2=i;}
                if(a.get(i).getType().equals("<Stmt>")){
                    output("\nv"+YesId+":\n");
                    a.get(i).setStmtId(StmtId);
                    a.get(i).setBreakId(NoId);
                    System.out.println("StmtId "+StmtId);
                    a.get(i).setContinueId(StmtId);
                    System.out.println("StmtId "+a.get(i).type);
                    generate(a.get(i));
                }
            }
            output("\nv"+StmtId+":\n");
            if(for2==0){
                output(tags()+"br label %v"+CondId+"\n");
            }
            else{
                a.get(for2).setStmtId(CondId);
                generate(a.get(for2));
            }
            output("\nv"+NoId+":\n");

        }
        else if(a.get(0).getContent().equals("break")){
            //System.out.println("tset1 "+ast.getBreakId());
            ast.setStmtId(ast.getBreakId());
        }
        else if(a.get(0).getContent().equals("continue")){
            //System.out.println(ast.getContinueId());
            ast.setStmtId(ast.getContinueId());
        }
        //System.out.println("Stmt212");
        //System.out.println("dewiudqd "+a.get(0).getType()+" "+ast.getStmtId());
        if(ast.getStmtId()!=0){
            //System.out.println("dewiudqd "+a.get(0).getType());
            output(tags()+"br label %v"+ast.getStmtId()+"\n");
        }
    }
    public static void ForStmt(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        generate(a.get(0));//LVal
        generate(a.get(2));//Exp
        output(tags()+"store i32 "+a.get(2).getValue()+", i32* "+a.get(0).getRegId()+"\n");
        output(tags()+"br label %v"+ast.getStmtId()+"\n");
    }
    public static void LVal(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        TreeNode ident = a.get(0);
        String identName=ident.getContent();
        ValueDetail k = ident.getKey();
        int check=0;
        for(int i=stack.size()-1;i>=0;i--){
            if(stack.get(i).getContent().equals(identName)){
                k=stack.get(i).getKey();
                if(a.size()==1){
                    if(stack.get(i).getKey().dimension==0){//int a;
                        output(tags()+"%v"+regId+" = load i32, i32* "+stack.get(i).getRegId()+"\n");
                        k.setAddressType("i32");
                        ast.setValue("%v"+regId);
                        ast.setRegId(stack.get(i).getRegId());
                        regId++;
                    }
                    else if(stack.get(i).getKey().dimension==1){
                        if(k.d1!=0){//int a[2]{func(a)}
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x i32], ["+k.d1+" x i32]*"+stack.get(i).getRegId()+", i32 0, i32 0\n");
                            k.setAddressType("i32*");
                            ast.setValue("%v"+(regId));
                            ast.setRegId("%v"+(regId));
                            regId+=2;
                        }
                        else{//func(int a[]){func(a)}
                            k.setAddressType("i32*");
                            output(tags()+"%v"+regId+" = load "+k.addressType+", "+k.addressType+" * "+stack.get(i).getRegId()+"\n");
                            ast.setValue("%v"+(regId));
                            ast.setRegId("%v"+(regId));
                            regId+=1;
                        }
                    }
                    else if(stack.get(i).getKey().dimension==2){
                        if(k.d1!=0){//int a[2][3]{func(a)}
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x ["+k.d2+" x i32]], ["+k.d1+" x ["+k.d2+" x i32]]*"+stack.get(i).getRegId()+", i32 0, i32 0\n");
                            k.setAddressType("["+k.d2+" x i32]*");
                            ast.setValue("%v"+(regId));
                            ast.setRegId("%v"+(regId));
                            regId++;
                        }
                        else{//func(int a[][3]){func(a)}
                            output(tags()+"%v"+regId+" = load ["+k.d2+" x i32] *, ["+k.d2+" x i32]* * "+stack.get(i).getRegId()+"\n");
                            k.setAddressType("["+k.d2+" x i32]*");
                            ast.setValue("%v"+(regId));
                            ast.setRegId(stack.get(i).getRegId());
                            regId++;
                        }
                    }
                }
                else if(a.size()==4){
                    if(stack.get(i).getKey().dimension==1){
                        if(k.d1!=0){//int a[2]{func(a[1])}
                            generate(a.get(2));
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x i32], ["+k.d1+" x i32]*"+stack.get(i).getRegId()+", i32 0, i32 "+a.get(2).getValue()+"\n");
                            output(tags()+"%v"+(regId+1)+" = load i32, i32* %v"+regId+"\n");
                            k.setAddressType("i32");
                            ast.setValue("%v"+(regId+1));
                            ast.setRegId("%v"+(regId));
                            regId+=2;
                        }
                        else{//func(int b,int a[]){func(a[2],xxx)}
                            generate(a.get(2));
                            output(tags()+"%v"+regId+" = load i32*, i32* * "+stack.get(i).getRegId()+"\n");
                            output(tags()+"%v"+(regId+1)+" = getelementptr i32, i32* %v"+regId+", i32 "+a.get(2).getValue()+"\n");
                            output(tags()+"%v"+(regId+2)+" = load i32, i32* %v"+(regId+1)+"\n");
                            ast.setValue("%v"+(regId+2));
                            ast.setRegId("%v"+(regId+1));
                            k.setAddressType("i32");
                            regId+=3;
                        }
                    }
                    else if(stack.get(i).getKey().dimension==2){
                        if(k.d1!=0){//int a[2][3]{func(a[1])}
                            generate(a.get(2));
                            output(tags()+"%v"+regId+" = mul i32 "+a.get(2).getValue()+", "+k.d2+"\n");
                            regId++;
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x ["+k.d2+" x i32]], ["+k.d1+" x ["+k.d2+" x i32]]*"+stack.get(i).getRegId()+", i32 0, i32 0\n");
                            regId++;
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d2+" x i32], ["+k.d2+" x i32]* %v"+(regId-1)+", i32 0, i32 %v"+(regId-2)+"\n");
                            k.setAddressType("i32*");
                            ast.setValue("%v"+(regId));
                            ast.setRegId("%v"+(regId));
                            regId+=1;
                        }
                        else{//func(int b[],int a[][3]){func(a[2],xxx)}
                            generate(a.get(2));
                            output(tags()+"%v"+regId+" = load ["+k.d2+" x i32] *, ["+k.d2+" x i32]* * "+stack.get(i).getRegId()+"\n");
                            regId++;
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d2+" x i32], ["+k.d2+" x i32]* %v"+(regId-1)+", i32 "+a.get(2).getValue()+"\n");
                            regId++;
                            output(tags()+"%v"+regId+" = getelementptr ["+k.d2+" x i32], ["+k.d2+" x i32]* %v"+(regId-1)+", i32 0, i32 0\n");
                            k.setAddressType("i32*");
                            ast.setValue("%v"+(regId));
                            ast.setRegId("%v"+(regId));
                            regId++;
                        }
                    }
                }
                else if(a.size()==7){
                    generate(a.get(2));
                    generate(a.get(5));
                    if(k.d1!=0){//int a[2][3]{func(a[1][2])}
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x ["+k.d2+" x i32]], ["+k.d1+" x ["+k.d2+" x i32]]*"+stack.get(i).getRegId()+", i32 0, i32 "+a.get(2).getValue()+", i32 "+a.get(5).getValue()+"\n");
                        output(tags()+"%v"+(regId+1)+" = load i32, i32* %v"+regId+"\n");
                        k.setAddressType("i32");
                        ast.setValue("%v"+(regId+1));
                        ast.setRegId("%v"+(regId));
                        regId+=2;
                    }
                    else{//func(int b,int a[][3]){func(a[2][2],xxx)}
                        output(tags()+"%v"+regId+" = load ["+k.d2+" x i32] *, ["+k.d2+" x i32]* * "+stack.get(i).getRegId()+"\n");
                        regId++;
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d2+" x i32], ["+k.d2+" x i32]* %v"+(regId-1)+", i32 "+a.get(2).getValue()+"\n");
                        regId++;
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d2+" x i32], ["+k.d2+" x i32]* %v"+(regId-1)+", i32 0, i32 "+a.get(5).getValue()+"\n");
                        output(tags()+"%v"+(regId+1)+" = load i32, i32 *%v"+(regId)+"\n");
                        k.setAddressType("i32");
                        ast.setValue("%v"+(regId+1));
                        ast.setRegId("%v"+(regId));
                        regId+=2;
                    }
                }
                check=1;
                break;
            }
        }
        if(check==0){
            k=global.get(identName).getKey();
            if(level>0){
                if(a.size()==1){
                    if(k.dimension==0){
                        output(tags()+"%v"+regId+" = load i32, i32* @"+identName+"\n");
                        k.setAddressType("i32");
                        ast.setValue("%v"+regId);
                        ast.setRegId("@"+identName);
                        regId++;
                    }
                    else if(k.dimension==1){
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x i32], ["+k.d1+" x i32]* @"+identName+", i32 0, i32 0\n");
                        k.setAddressType("i32*");
                        ast.setValue("%v"+(regId));
                        ast.setRegId("%v"+(regId));
                        regId+=2;
                    }
                    else if(k.dimension==2){
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x ["+k.d2+" x i32]], ["+k.d1+" x ["+k.d2+" x i32]]* @"+identName+", i32 0, i32 0\n");
                        k.setAddressType("["+k.d2+" x i32]*");
                        ast.setValue("%v"+(regId));
                        ast.setRegId("%v"+(regId));
                        regId++;
                    }
                }
                else if(a.size()==4){
                    if(k.dimension==1){
                        generate(a.get(2));
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x i32], ["+k.d1+" x i32]* @"+identName+", i32 0, i32 "+a.get(2).getValue()+"\n");
                        output(tags()+"%v"+(regId+1)+" = load i32, i32* %v"+regId+"\n");
                        k.setAddressType("i32");
                        ast.setValue("%v"+(regId+1));
                        ast.setRegId("%v"+(regId));
                        regId+=2;
                    }
                    else if(k.dimension==2){
                        generate(a.get(2));
                        output(tags()+"%v"+regId+" = mul i32 "+a.get(2).getValue()+", "+k.d2+"\n");
                        regId++;
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x ["+k.d2+" x i32]], ["+k.d1+" x ["+k.d2+" x i32]]* @"+identName+", i32 0, i32 0\n");
                        regId++;
                        output(tags()+"%v"+regId+" = getelementptr ["+k.d2+" x i32], ["+k.d2+" x i32]* %v"+(regId-1)+", i32 0, i32 %v"+(regId-2)+"\n");
                        k.setAddressType("i32*");
                        ast.setValue("%v"+(regId));
                        ast.setRegId("%v"+(regId));
                        regId+=1;
                    }
                }
                else if(a.size()==7){
                    generate(a.get(2));
                    generate(a.get(5));
                    output(tags()+"%v"+regId+" = getelementptr ["+k.d1+" x ["+k.d2+" x i32]], ["+k.d1+" x ["+k.d2+" x i32]]* @"+identName+", i32 0, i32 "+a.get(2).getValue()+", i32 "+a.get(5).getValue()+"\n");
                    output(tags()+"%v"+(regId+1)+" = load i32, i32* %v"+regId+"\n");
                    k.setAddressType("i32");
                    ast.setValue("%v"+(regId+1));
                    ast.setRegId("%v"+(regId));
                    regId+=2;
                }
            }
            else{
                if(a.size()==1){
                    ast.setValue(global.get(identName).getKey().getIntVal());
                }
                else if(a.size()==4){
                    generate(a.get(2));
                    ast.setValue(global.get(identName).getKey().getD1Value()[Integer.parseInt(a.get(2).getValue())]);
                }
                else if(a.size()==7){
                    generate(a.get(2));
                    generate(a.get(5));
                    ast.setValue(global.get(identName).getKey().getD2Value()[Integer.parseInt(a.get(2).getValue())][Integer.parseInt(a.get(5).getValue())]);
                }
            }
        }
        ast.setKey(k);
    }
    public static void Exp(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        generate(ast.children.get(0));//AddExp
        ast.setValue(ast.children.get(0).getValue());
        ast.setRegId(ast.children.get(0).getRegId());
        ast.setKey(ast.children.get(0).getKey());

    }
    public static void Cond(TreeNode ast){
        ast.children.get(0).setNoId(ast.noId);
        ast.children.get(0).setYesId(ast.yesId);
        ast.children.get(0).setStmtId(ast.getStmtId());
        generate(ast.children.get(0));//LOrExp
        ast.setValue("%v"+regId);
        regId++;
    }
    public static void PrimaryExp(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;

        if(a.get(0).getType().equals("<Number>")){
            generate(a.get(0));//Number
            ast.setValue(a.get(0).getValue());
        }
        else if(a.get(0).getContent().equals("(")){
            generate(a.get(1));//Exp
            ast.setValue(a.get(1).getValue());
        }
        else if(a.get(0).getType().equals("<LVal>")){
            generate(a.get(0));//LVal
            ast.setValue(a.get(0).getValue());
            ast.setRegId(a.get(0).getRegId());
            ast.setKey(a.get(0).getKey());
        }
    }
    public static void Number1(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        ast.setValue(a.get(0).getContent());
    }
    public static void UnaryExp(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        if(a.get(0).getType().equals("<UnaryOp>")){
            generate(a.get(1));//UnaryExp
            if(a.get(0).children.get(0).getContent().equals("-")){
                if(level>0){
                    output(tags()+"%v"+regId+" = sub i32 0, "+a.get(1).getValue()+"\n");
                    ast.setRegId("%v"+regId);
                    ast.setValue("%v"+regId);
                    regId++;
                }
                else{
                    ast.setValue(mathCalculate("0","-",a.get(1).getValue()));
                }
            }
            else if(a.get(0).children.get(0).getContent().equals("+")){ast.setValue(a.get(1).getValue());}
            else if(a.get(0).children.get(0).getContent().equals("!")){
                output(tags()+"%v"+regId+" = icmp eq i32 0, "+a.get(1).getValue()+"\n");
                regId++;
                output(tags()+"%v"+regId+" = zext1 i1 %v"+(regId-1)+" to i32\n");
                ast.setRegId("%v"+regId);
                ast.setValue("%v"+regId);
                regId++;
            }
        }
        else if(a.get(0).getType().equals("<PrimaryExp>")){
            generate(a.get(0));//PrimaryExp
            ast.setValue(a.get(0).getValue());
            ast.setRegId(a.get(0).getRegId());
            ast.setKey(a.get(0).getKey());
        }
        else if(a.size()>2&&a.get(1).getContent().equals("(")){
            TreeNode ident=a.get(0);
            String identName = ident.getContent();
            TreeNode identGlobe=global.get(identName);
            ident.setReturnType(identGlobe.getReturnType());
            if(ident.getReturnType().equals("i32")){
                if(a.get(2).getContent().equals(")")){
                    output(tags()+"%v"+regId+" = call "+ident.getReturnType()+" @"+ident.getContent()+"()\n");
                    ast.setValue("%v"+regId);
                    regId++;
                }
                else{
                    generate(a.get(2));
                    output(tags()+"%v"+regId+" = call "+ident.getReturnType()+" @"+ident.getContent()+"("+a.get(2).getValue()+")\n");
                    ast.setValue("%v"+regId);
                    regId++;
                }
            }
            else if(ident.getReturnType().equals("void")){
                if(a.get(2).getContent().equals(")")){
                    output(tags()+"call "+ident.getReturnType()+" @"+ident.getContent()+"()\n");
                }
                else{
                    generate(a.get(2));
                    output(tags()+"call "+ident.getReturnType()+" @"+ident.getContent()+"("+a.get(2).getValue()+")\n");
                }
            }
        }
    }
    public static void FuncRParams(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        generate(a.get(0));
        StringBuilder Value;
        Value=new StringBuilder(a.get(0).getKey().addressType+" "+a.get(0).getValue());
        for(int i=2;i<a.size();i+=2){
            generate(a.get(i));
            Value.append(", ").append(a.get(i).getKey().addressType).append(" ").append(a.get(i).getValue());
        }
        ast.setValue(Value.toString());
    }
    public static void AddMulExp(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        generate(a.get(0));//AddExp/MulExp
        String left=a.get(0).getValue();
        if(a.size()>1){
            for(int i=1;i<a.size();i+=2){
                String op=a.get(i).getContent();
                generate(a.get(i+1));
                String right=a.get(i+1).getValue();
                String opt=Operator(op);
                if(level>0){
                    output(tags()+"%v"+regId+" = "+opt+" i32 "+left+", "+right+"\n");
                    a.get(i+1).setRegId("%v"+regId);
                    a.get(i+1).setValue("%v"+regId);
                    regId++;
                }
                else{
                    a.get(i+1).setValue(mathCalculate(left,op,right));
                }
                left=a.get(i+1).getValue();
            }
            ast.setValue(a.get(a.size()-1).getValue());
        }
        else{
            ast.setKey(a.get(0).getKey());
            ast.setValue(left);
            ast.setRegId(a.get(0).getRegId());
        }
    }
    public static void RelEqExp(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        generate(a.get(0));//RelExp/EqExp
        String left=a.get(0).getValue();
        if(a.size()>1){
            for(int i=1;i<a.size();i+=2){
                String op=a.get(i).getContent();
                generate(a.get(i+1));
                String right=a.get(i+1).getValue();
                String opt=Operator(op);
                if(level>0){
                    output(tags()+"%v"+regId+" = icmp "+opt+" i32 "+left+", "+right+"\n");
                    regId++;
                    output(tags()+"%v"+regId+" = zext i1 %v"+(regId-1)+" to i32\n");
                    a.get(i+1).setRegId("%v"+regId);
                    a.get(i+1).setValue("%v"+regId);
                    regId++;
                }
                else{
                    a.get(i+1).setValue(mathCalculate(left,op,right));
                }
                left=a.get(i+1).getValue();
            }
            ast.setValue(a.get(a.size()-1).getValue());
        }
        else{
            ast.setValue(left);
        }
    }
    public static void LAndExp(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        if(a.size()==1){
            generate(a.get(0));
            ast.setValue(a.get(0).getValue());
        }
        else{
            for(int i=0;i<a.size()-2;i+=2){
                generate(a.get(i));//LAndExp
                output(tags()+"%v"+regId+" = icmp ne i32 0, "+a.get(i).getValue()+"\n");
                output(tags()+"br i1 %v"+regId+", label %v"+(regId+1)+", label %v"+ast.noId+"\n");
                regId+=2;
                output("\nv"+(regId-1)+":\n");
            }
            int max=a.size()-1;
            generate(a.get(max));
            if(a.size()==1){
                ast.setValue(a.get(max).getValue());
            }
            output(tags()+"%v"+regId+" = icmp ne i32 0, "+a.get(max).getValue()+"\n");
            output(tags()+"br i1 %v"+regId+", label %v"+ast.yesId+", label %v"+ast.noId+"\n");
            regId+=1;

        }
    }
    public static void LOrExp(TreeNode ast){
        ArrayList<TreeNode> a=ast.children;
        //System.out.println("my lifetime time "+ast.type);

        //System.out.println("my lifetime "+a.size());
        for(int i=0; i<a.size(); i++){
            //System.out.println("my lifetime "+a.get(i).type);
            if(i==2){
                //System.out.println("1my lifetime "+a.get(i).children.size());;
            }
        }
        for(int i=0;i<a.size()-2;i+=2){
            if(a.get(i).children.get(0).children.size()==1){
                System.out.println("llvmmmmmm");
                generate(a.get(i).children.get(0));//LOrExp
                output(tags()+"%v"+regId+" = icmp ne i32 0, "+a.get(i).children.get(0).getValue()+"\n");
                output(tags()+"br i1 %v"+regId+", label %v"+ast.yesId+", label %v"+(regId+1)+"\n");
                regId+=2;
                output("\nv"+(regId-1)+":\n");
            }
            else{
                System.out.println("llvmmmmmmppp");
                a.get(i).setYesId(ast.yesId);
                a.get(i).setStmtId(ast.getStmtId());

                a.get(i).setNoId(regId);
                regId++;
                generate(a.get(i));//特殊
                output("\nv"+a.get(i).noId+":\n");
            }
        }
        int max=a.size()-1;
        if(a.get(max).children.size()==1){
            generate(a.get(max));//LOrExp
            output(tags()+"%v"+regId+" = icmp ne i32 0, "+a.get(max).getValue()+"\n");
            output(tags()+"br i1 %v"+regId+", label %v"+ast.yesId+", label %v"+ast.noId+"\n");
            regId+=1;
        }
        else{
            a.get(max).setYesId(ast.yesId);
            a.get(max).setStmtId(ast.getStmtId());
            a.get(max).setNoId(ast.noId);
            regId++;
            generate(a.get(max));//特殊
        }

    }
    public static String Operator(String op){
        String opt="";
        switch(op){
            case "+": opt="add";break;
            case "-": opt="sub";break;
            case "*": opt="mul";break;
            case "/": opt="sdiv";break;
            case "%": opt="srem";break;
            case "==": opt="eq";break;
            case "!=": opt="ne";break;
            case ">": opt="sgt";break;
            case ">=": opt="sge";break;
            case "<": opt="slt";break;
            case "<=": opt="sle";break;
            case "&&": opt="and";break;
            case "||": opt="or";break;
        }
        return opt;
    }
    public static String mathCalculate(String left,String op,String right){
        int a=Integer.parseInt(left);
        int b=Integer.parseInt(right);
        int ans=0;
        switch(op){
            case "+":ans=a+b;break;
            case "-":ans=a-b;break;
            case "*":ans=a*b;break;
            case "/":ans=a/b;break;
            case "%":ans=a%b;break;
            case "==": ans=(a==b)?1:0;break;
            case "!=": ans=(a!=b)?1:0;break;
            case ">": ans=(a>b)?1:0;break;
            case ">=": ans=(a>=b)?1:0;break;
            case "<": ans=(a<b)?1:0;break;
            case "<=": ans=(a<=b)?1:0;break;
        }
        return ans+"";
    }
    public static void output(String content){
        Line line = new Line(0,content);
        lineList.add(line);
    }
    public static String tags(){
        StringBuilder s=new StringBuilder();
        for(int i=0;i<4*nowtag;i++){
            s.append(" ");
        }
        return s.toString();
    }
}
