package company;

import Interpreter.ParserRulesLexer;
import Interpreter.ParserRulesParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.util.Scanner;

public class Interpreter {
    //使用listener方法遍历抽象语法树
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        CharStream input = CharStreams.fromString(s);

        ParserRulesLexer lexer = new ParserRulesLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ParserRulesParser parser = new ParserRulesParser(tokens);
        ParseTree tree = parser.specification();

        ParseTreeWalker walker = new ParseTreeWalker();
        MyListener listener = new MyListener(parser);
        walker.walk(listener, tree);

        File file = new File("src/main/java/company/SyntaxOut.txt");
        File target = new File("src/main/java/company/TargetOut.hxx");
        if(!file.exists()){
            try {
                file.createNewFile();
                target.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //语法及语义分析
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(listener.builder.toString());
            bufferedWriter.flush();

            //目标代码生成
            BufferedWriter writer = new BufferedWriter(new FileWriter(target));
            writer.write(listener.st.render());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
