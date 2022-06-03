package company;


import Interpreter.ParserRulesBaseListener;
import Interpreter.ParserRulesParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;


public class MyListener extends ParserRulesBaseListener {
    ParserRulesParser parser;
    //文本输出
    StringBuilder builder;
    //符号表
    //名称为键，类型为属性，通过栈实现作用域的嵌套
    HashMap<String, Stack<String>> typeTable;
    //struct定义表，名称为键，所处命名空间为属性
    HashMap<String, String> structTable;
    //STGroup文件
    STGroupFile file;
    //ST模板引擎
    ST st;


    public MyListener(ParserRulesParser parser)  {
        this.parser = parser;
        this.builder = new StringBuilder();
        this.typeTable = new LinkedHashMap<>();
        this.structTable = new LinkedHashMap<>();
        file= new STGroupFile("C:\\Users\\86153\\Desktop\\MyInterpreter\\src\\main\\java\\company\\translator.stg");
        st = file.getInstanceOf("struct");
    }


    @Override
    public void enterSpecification(ParserRulesParser.SpecificationContext ctx) {
        builder.append("specification\n");
        System.out.println("specification");

        //如果存在结构体定义，将其存入符号表
        List<ParserRulesParser.DefinitionContext> definitionContexts = ctx.definition();
        for(ParserRulesParser.DefinitionContext dc : definitionContexts){
            //定义在specification下的struct为公共命名空间
            if(dc.type_decl() != null){
                if(dc.type_decl().struct_type() != null){
                    structTable.put(dc.type_decl().struct_type().ID().getText(), "public");
                } else {
                    structTable.put(dc.type_decl().ID().getText(), "public");
                }
            }
            //定义在module下的struct为特定module下的命名空间
            if(dc.module() != null){
                List<ParserRulesParser.DefinitionContext> mdcList = dc.module().definition();
                for(ParserRulesParser.DefinitionContext mdc : mdcList){
                    // 一层嵌套
                    if(mdc.type_decl() != null){
                        if(mdc.type_decl().struct_type() != null){
                            structTable.put(mdc.type_decl().struct_type().ID().getText(), mdc.module().ID().getText());
                        } else {
                            structTable.put(mdc.type_decl().ID().getText(), mdc.module().ID().getText());
                        }
                    }
                    //多层嵌套，找到距离struct最近的module
                    if(mdc.module().definition(0).type_decl() != null){
                        if(mdc.module().definition(0).type_decl().struct_type() != null){
                            structTable.put(mdc.module().definition(0).type_decl().struct_type().ID().getText(), mdc.module().ID().getText());
                        } else {
                            structTable.put(mdc.module().definition(0).type_decl().ID().getText(), mdc.module().ID().getText());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void enterDefinition(ParserRulesParser.DefinitionContext ctx){
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        builder.append(table).append("definition\n");
        System.out.println(table +"definition");
    }


    @Override
    public void enterModule(ParserRulesParser.ModuleContext ctx) throws Exception {

        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        builder.append(table).append("module ").append(ctx.ID()).append("\n");
        builder.append(table).append("{\n");
        System.out.println(table+"module "+ctx.ID());
        System.out.println(table+"{");
        //检查符号表是否存在同名变量
        //如不存在当前变量，当前变量及其类型序号加入符号表
        //若存在，检查是否为同类型
        //栈顶为同类型时,报错
        //栈顶非同类型时,当前变量类型入栈
        if(typeTable.containsKey(ctx.ID().getText())){
            Stack<String> stack = typeTable.get(ctx.ID().getText());
            if(stack.isEmpty()){
                stack.push("module");
                typeTable.put(ctx.ID().getText(), stack);
            } else {
                if (stack.peek().equals("module")) {
                    String exception = "重复命名!" + " 出错变量定义: " + ctx.start.getText()
                            + ", 出错位置: " + ctx.start.getStartIndex();
                    builder.append(table).append(exception).append("\n");
                    throw new Exception(exception);
                }else {
                    stack.push("module");
                    typeTable.put(ctx.ID().getText(), stack);
                }
            }
        } else {
            Stack<String> stack = new Stack<>();
            stack.push("module");
            typeTable.put(ctx.ID().getText(), stack);
        }
    }

    @Override
    public void exitModule(ParserRulesParser.ModuleContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        builder.append(table).append("}\n");
        System.out.println(table+"}");

        //退出时删除符号表中存放的当前节点的所有子结点对应的类型值
        List<ParserRulesParser.DefinitionContext> definitionContexts = ctx.definition();
        for(ParserRulesParser.DefinitionContext dc : definitionContexts){
            Stack<String> stack = null;
            //module的子结点可能为module或type_dec
            //type_dec可能为struct或struct_type
            if(dc.module() != null){
                if(typeTable.containsKey(dc.module().ID().getText())){
                    stack = typeTable.get(dc.module().ID().getText());
                }
            }
            if(dc.type_decl() != null){
                if(dc.type_decl().struct_type() != null){
                    if(typeTable.containsKey(dc.type_decl().struct_type().ID().getText())){
                        stack = typeTable.get(dc.type_decl().struct_type().ID().getText());
                    }
                } else {
                    if(typeTable.containsKey(dc.type_decl().ID().getText())){
                        stack = typeTable.get(dc.type_decl().ID().getText());
                    }
                }
            }
            if(stack != null && !stack.isEmpty()){
                if(stack.peek().equals("struct")){
                    stack.pop();
                }
            }
        }
    }

    @Override
    public void enterType_decl(ParserRulesParser.Type_declContext ctx) throws Exception {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        if(ctx.struct_type() != null){
            System.out.println(table+"type_decl");
            builder.append(table).append("type_decl\n");
        } else {
            //只声明
            System.out.println(table+"type_decl");
            System.out.println(table+"\tstruct "+ctx.ID());
            builder.append(table).append("type_decl\n");
            builder.append(table).append("\tstruct ").append(ctx.ID()).append("\n");
        }

        //判断是否为struct声明(2个节点)还是struct结构体(1个节点)
        //若为结构体则由struct结构体函数处理
        //若为struct声明则由当前函数处理
        if(ctx.getChildCount() > 1){
            //记录struct定义

//            structTable.put(ctx.ID().getText(), "struct_type");

            if(typeTable.containsKey(ctx.ID().getText())){
                Stack<String> stack = typeTable.get(ctx.ID().getText());
                if(stack.isEmpty()){
                    stack.push("struct");
                    typeTable.put(ctx.ID().getText(), stack);
                    st.add("name", ctx.ID().getText());
                    st.add("space", structTable.getOrDefault(ctx.ID().getText(), ""));
                    boolean nested = !structTable.get(ctx.ID().getText()).equals("public");
                    st.add("nested", nested);
                    System.out.println(st.render());
                } else {
                    if(stack.peek().equals("struct")){
                        String exception = "重复命名!" + " 出错变量定义: " + ctx.start.getText()
                                + ", 出错位置: " + ctx.start.getStartIndex();
                        builder.append(table).append(exception).append("\n");
                        throw new Exception(exception);
                    } else {
                        stack.push("struct");
                        typeTable.put(ctx.ID().getText(), stack);
                        st.add("name", ctx.ID().getText());
                        st.add("space", structTable.getOrDefault(ctx.ID().getText(), ""));
                        boolean nested = !structTable.get(ctx.ID().getText()).equals("public");
                        st.add("nested", nested);
                        System.out.println(st.render());
                    }
                }
            } else {
                Stack<String> stack = new Stack<>();
                stack.push("struct");
                typeTable.put(ctx.ID().getText(), stack);
                st.add("name", ctx.ID().getText());
                st.add("space", structTable.getOrDefault(ctx.ID().getText(), ""));
                boolean nested = !structTable.get(ctx.ID().getText()).equals("public");
                st.add("nested", nested);
                System.out.println(st.render());
            }
        }
    }


    @Override
    public void enterStruct_type(ParserRulesParser.Struct_typeContext ctx) throws Exception {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"struct "+ctx.ID());
        System.out.println(table+"{");
        builder.append(table).append("struct ").append(ctx.ID()).append("\n");
        builder.append(table).append("{\n");

//        //记录struct定义
//        structTable.put(ctx.ID().getText(), "struct_type");

        //符号表存入struct
        if(typeTable.containsKey(ctx.ID().getText())){
            Stack<String> stack = typeTable.get(ctx.ID().getText());
            if(stack.isEmpty()){
                stack.push("struct");
                typeTable.put(ctx.ID().getText(), stack);
                st.add("name", ctx.ID().getText());
                st.add("space", structTable.getOrDefault(ctx.ID().getText(), ""));
                boolean nested = !structTable.get(ctx.ID().getText()).equals("public");
                st.add("nested", nested);
                System.out.println(st.render());
            } else {
                if(stack.peek().equals("struct")){
                    String exception = "重复命名!" + " 出错变量定义: " + ctx.start.getText()
                            + ", 出错位置: " + ctx.start.getStartIndex();
                    builder.append(table).append(exception).append("\n");
                    throw new Exception(exception);
                } else {
                    stack.push("struct");
                    typeTable.put(ctx.ID().getText(), stack);
                    st.add("name", ctx.ID().getText());
                    st.add("space", structTable.getOrDefault(ctx.ID().getText(), ""));
                    boolean nested = !structTable.get(ctx.ID().getText()).equals("public");
                    st.add("nested", nested);
                    System.out.println(st.render());
                }
            }
        } else {
            Stack<String> stack = new Stack<>();
            stack.push("struct");
            typeTable.put(ctx.ID().getText(), stack);
            st.add("name", ctx.ID().getText());
            st.add("space", structTable.getOrDefault(ctx.ID().getText(), ""));
            boolean nested = !structTable.get(ctx.ID().getText()).equals("public");
            st.add("nested", nested);
            System.out.println(st.render());
        }
        //填充变量及值
        ParserRulesParser.Member_listContext memberlist = ctx.member_list();
        List<ParserRulesParser.Type_specContext> typelist = memberlist.type_spec();
        List<ParserRulesParser.DeclaratorsContext> declaratorsContextList = memberlist.declarators();

    }

    @Override
    public void exitStruct_type(ParserRulesParser.Struct_typeContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"}");
        builder.append(table).append("}\n");

        //删除当前节点下所有子节点在符号表中的值
        if(ctx.member_list().getChildCount() != 0){
            List<ParserRulesParser.DeclaratorsContext> list = ctx.member_list().declarators();
            for(ParserRulesParser.DeclaratorsContext dc : list){
                List<ParserRulesParser.DeclaratorContext> list1 = dc.declarator();
                for(ParserRulesParser.DeclaratorContext d : list1){
                    if(d.simple_declarator() != null){
                        if(typeTable.containsKey(d.simple_declarator().ID().getText())){
                            Stack<String> stack = typeTable.get(d.simple_declarator().ID().getText());
                            if(!stack.isEmpty()){
                                stack.pop();
                            }
                        }
                    }
                    if(d.array_declarator() != null){
                        if(typeTable.containsKey(d.array_declarator().ID().getText())){
                            Stack<String> stack = typeTable.get(d.array_declarator().ID().getText());
                            if(!stack.isEmpty()){
                                stack.pop();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void enterMember_list(ParserRulesParser.Member_listContext ctx) throws Exception {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"member_list");
        builder.append(table).append("member_list\n");

        //重复命名及类型检查
        if(ctx.getChildCount() != 0){
            //类型列表
            List<ParserRulesParser.Type_specContext> tc  = ctx.type_spec();
            //ID及赋值列表
            List<ParserRulesParser.DeclaratorsContext> dc = ctx.declarators();

            //有数组定义时，获取对应的类型
            for(int i=0; i<dc.size(); ++i) {
                if(dc.get(i).declarator(0).array_declarator() != null){
                    String type = tc.get(i).base_type_spec().getText();
                    if(type.equals("short")){
                        st.add("type","Short");
                    }
                    if(type.equals("long")){
                        st.add("type","Long");
                    }
                    if(type.equals("longlong")){
                        st.add("type","LongLong");
                    }
                    if(type.equals("unsignedshort")){
                        st.add("type","UnsignedShort");
                    }
                    if(type.equals("unsignedlong")){
                        st.add("type","UnsignedLong");
                    }
                    if(type.equals("unsignedlonglong")){
                        st.add("type","UnsignedLongLong");
                    }
                    if(type.equals("char")){
                        st.add("type","Char");
                    }
                    if(type.equals("string")){
                        st.add("type","String");
                    }
                    if(type.equals("boolean")){
                        st.add("type","Boolean");
                    }
                    if(type.equals("float")){
                        st.add("type","Float");
                    }
                    if(type.equals("double")){
                        st.add("type","Double");
                    }
                    if(type.equals("long double")){
                        st.add("type","LongDouble");
                    }
                }
            }

            for(int i=0; i<tc.size(); ++i){
                //代码生成
                List<String> translatorDeclarators = new ArrayList<>();
                List<String> translatorValues = new ArrayList<>();
                List<String> arrayValues = new ArrayList<>();
                String lastDel = "";
                String lastVal = "";

                String type = "";
                String variableType = "";
                ParserRulesParser.Type_specContext tc_instance = tc.get(i);
                ParserRulesParser.DeclaratorsContext dc_instance = dc.get(i);
                //获取当前变量的类型
                if(tc_instance.base_type_spec() != null){
                    if(tc_instance.base_type_spec().integer_type() != null){
                        ParserRulesParser.Signed_intContext signedInt = tc_instance.base_type_spec().integer_type().signed_int();
                        ParserRulesParser.Unsigned_intContext unsignedInt =tc_instance.base_type_spec().integer_type().unsigned_int();
                        if(signedInt != null) {
                            if(signedInt.getText().equals("short")){
                                variableType = "Short";
                                st.add("isShort",true);
                            }
                            if(signedInt.getText().equals("long")){
                                variableType = "Long";
                                st.add("isLong",true);
                            }
                            if(signedInt.getText().equals("longlong")){
                                variableType = "LongLong";
                                st.add("isLongLong",true);
                            }
                        }
                        if(unsignedInt != null){
                            if(unsignedInt.getText().equals("unsignedshort")){
                                variableType = "UnsignedShort";
                                st.add("isUnsignedShort", true);
                            }
                            if(unsignedInt.getText().equals("unsignedlong")){
                                variableType = "UnsignedLong";
                                st.add("isUnsignedLong", true);
                            }
                            if(unsignedInt.getText().equals("unsignedlonglong")){
                                variableType = "UnsignedLongLong";
                                st.add("isUnsignedLongLong", true);
                            }
                        }
                        type = "integer";
                    } else if(tc_instance.base_type_spec().floating_pt_type() != null){
                        switch (tc_instance.base_type_spec().floating_pt_type().getText()){
                            case "float":
                                variableType = "Float";
                                st.add("isFloat", true);
                                break;
                            case "double":
                                variableType = "Double";
                                st.add("isDouble", true);
                                break;
                            case "long double":
                                variableType = "LongDouble";
                                st.add("isLongDouble", true);
                                break;
                        }
                        type = "float";
                    } else {
                        type = tc_instance.base_type_spec().getText();
                        switch (type){
                            case "char":
                                variableType = "Char";
                                st.add("isChar", true);
                                break;
                            case "string":
                                variableType = "String";
                                st.add("isString", true);
                                break;
                            case "boolean":
                                variableType = "Boolean";
                                st.add("isBoolean", true);
                                break;
                        }
                    }
                }
                if(tc_instance.scoped_name() != null){
                    type = tc_instance.scoped_name().ID().toString();
                }

                //获取当前变量类型：简单变量或数组变量，及其赋值
                List<ParserRulesParser.DeclaratorContext> list = dc_instance.declarator();
                int count = 0;
                for(ParserRulesParser.DeclaratorContext d : list){
                    if(d.array_declarator() != null){
                        Stack<String> stack;
                        if(typeTable.containsKey(d.array_declarator().ID().getText())){
                            stack = typeTable.get(d.array_declarator().ID().getText());
                            if(!stack.isEmpty()){
                                if(stack.peek().equals("array_decl")){
                                    String exception = "重复命名! 出错变量定义: array_declarator, 出错变量名: "+d.array_declarator().start.getText();
                                    builder.append(table).append(exception).append("\n");
//                                    throw new Exception(exception);
                                } else {
                                    stack.push("array_decl");
                                    typeTable.put(d.array_declarator().ID().getText(), stack);
                                }
                            } else {
                                stack.push("array_decl");
                                typeTable.put(d.array_declarator().ID().getText(), stack);
                            }
                        } else {
                            stack = new Stack<>();
                            stack.push("array_decl");
                            typeTable.put(d.array_declarator().ID().getText(), stack);
                        }
                        st.add("isArray", true);
                        st.add("decl",d.array_declarator().ID().getText());

                        List<ParserRulesParser.Or_exprContext> orList = d.array_declarator().exp_list().or_expr();
                        st.add("length",orList.size());
                        boolean incompatible = false;
                        int index = 0;
                        for (;index<orList.size(); ++index) {
                            ParserRulesParser.Or_exprContext or = orList.get(index);
                            if(or == null){
                                continue;
                            }
                            ParserRulesParser.LiteralContext value = or.xor_expr(0).and_expr(0).shift_expr(0).add_expr(0).mult_expr(0).unary_expr(0).literal();
                            if (value.FLOATING_PT() != null) {
                                if (!type.equals("float")) {
                                    incompatible = true;
                                    break;
                                }
                            }
                            if (value.INTEGER() != null) {
                                if (!type.equals("integer")) {
                                    incompatible = true;
                                    break;
                                }
                            }
                            if (value.BOOLEAN() != null) {
                                if (!type.equals("boolean")) {
                                    incompatible = true;
                                    break;
                                }
                            }
                            if (value.CHAR() != null) {
                                if (!type.equals("char")) {
                                    incompatible = true;
                                    break;
                                }
                            }
                            if (value.STRING() != null) {
                                if (!type.equals("string")) {
                                    incompatible = true;
                                    break;
                                }
                            }
                            if (index < orList.size()-1 ) {
                                arrayValues.add(value.getText());
                            } else {
                                lastVal = value.getText();
                            }
                        }
                        if(incompatible){
                            String exception = "变量类型与赋值类型不兼容! 出错变量定义: array_declarator, 出错变量名: " + d.array_declarator().start.getText();
                            builder.append(table).append(exception).append("\n");
//                            throw new Exception(exception);
                        }
                        st.add("vals", arrayValues);
                        st.add("lastVal", lastVal);
                    } else if (d.simple_declarator() != null){
                        translatorDeclarators.add(d.simple_declarator().ID().getText());
                        Stack<String> stack;
                        if(typeTable.containsKey(d.simple_declarator().ID().getText())){
                            stack = typeTable.get(d.simple_declarator().ID().getText());
                            if(!stack.isEmpty()){
                                if(stack.peek().equals("simple_decl")){
                                    String exception = "重复命名! 出错变量定义: simple_declarator, 出错变量名: "+d.simple_declarator().start.getText();
                                    builder.append(table).append(exception).append("\n");
//                                    throw new Exception(exception);
                                } else {
                                    stack.push("simple_decl");
                                    typeTable.put(d.simple_declarator().ID().getText(), stack);
                                }
                            } else {
                                stack.push("simple_decl");
                                typeTable.put(d.simple_declarator().ID().getText(), stack);
                            }
                        } else {
                            stack = new Stack<>();
                            stack.push("simple_decl");
                            typeTable.put(d.simple_declarator().ID().getText(), stack);
                        }

                        ParserRulesParser.Or_exprContext or_expr = d.simple_declarator().or_expr();
                        if(or_expr == null){
                            continue;
                        }
                        boolean incompatible = false;
                        ParserRulesParser.LiteralContext value = or_expr.xor_expr(0).and_expr(0).shift_expr(0).add_expr(0).mult_expr(0).unary_expr(0).literal();
                        if (value.FLOATING_PT() != null) {
                            if (!type.equals("float")) {
                                incompatible = true;
                            }
                        }
                        if (value.INTEGER() != null) {
                            if (!type.equals("integer")) {
                                incompatible = true;
                            }
                        }
                        if (value.BOOLEAN() != null) {
                            if (!type.equals("boolean")) {
                                incompatible = true;
                            }
                        }
                        if (value.CHAR() != null) {
                            if (!type.equals("char")) {
                                incompatible = true;
                            }
                        }
                        if (value.STRING() != null) {
                            if (!type.equals("string")) {
                                incompatible = true;
                            }
                        }
                        if(incompatible){
                            String exception = "变量类型与赋值类型不兼容! 出错变量定义: simple_declarator, 出错变量名: " + d.simple_declarator().start.getText();
                            builder.append(table).append(exception).append("\n");
//                            throw new Exception(exception);
                        }
                        translatorValues.add(value.getText());

                        //根据变量类型拼接获得传入ST模板参数的名称
                        String multi = "multi"+variableType;
                        String decls = variableType+"Decls";
                        String vals = variableType+"Vals";
                        String lastDecl = "last"+variableType+"Decl";
                        String lastV = "last"+variableType+"Val";

                        //存在多个值时
                        if (list.size() > 1){
                            st.add(multi, true);
                            if (count == list.size()-1) {
                                translatorDeclarators.remove(translatorDeclarators.size()-1);
                                translatorValues.remove(translatorValues.size()-1);
                                st.add(decls, translatorDeclarators);
                                st.add(vals, translatorValues);
                                st.add(lastDecl, d.simple_declarator().ID().getText());
                                st.add(lastV, value.getText());
                            }
                        } else {
                            st.add(decls, translatorDeclarators);
                            st.add(vals, translatorValues);
                        }
                    }
                    count++;
                }
            }
        }
    }

    @Override
    public void enterType_spec(ParserRulesParser.Type_specContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"type_spec");
        builder.append(table).append("type_spec\n");

        //检测是否有引用声明，若有，查看符号表，不存在时报错。
        if(ctx.scoped_name() != null){
            TerminalNode id = ctx.scoped_name().ID(ctx.scoped_name().ID().size()-1);
            if(!structTable.containsKey(id.getText())){
                String exception = "struct变量未定义即使用! 出错变量定义: scoped_name, 出错变量名: "+id.getText();
                builder.append(table).append(exception).append("\n");
                System.out.println(table+exception);
            } else {
                //非公共声明空间下的引用需要添加正确引用，否则报错
                if(!structTable.get(id.getText()).equals("public")){
                    boolean flag = false;
                    for(TerminalNode prefix : ctx.scoped_name().ID()){
                        if(prefix.getText().equals(structTable.get(id.getText()))){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        String exception = "struct变量引用错误! 出错变量定义: scoped_name, 出错变量名: "+id.getText();
                        builder.append(table).append(exception).append("\n");
                        System.out.println(table+exception);
                    }
                }
            }
        }
    }

    @Override
    public void enterScoped_name(ParserRulesParser.Scoped_nameContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"scoped_name");
        builder.append(table).append("scoped_name").append("\n");
        table.append("\t");

        System.out.println(table+"::"+ctx.ID(ctx.ID().size()-1));
        builder.append(table).append("::").append(ctx.ID(ctx.ID().size()-1)).append("\n");
    }

    @Override
    public void enterBase_type_spec(ParserRulesParser.Base_type_specContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"base_type_spec");
        builder.append(table).append("base_type_spec").append("\n");
        ////已到达终止符
        if(ctx.floating_pt_type() == null && ctx.integer_type() == null){
            System.out.println(table+"\t"+ctx.getText());
            builder.append(table).append("\t").append(ctx.getText()).append("\n");
        }
    }

    @Override
    public void enterFloating_pt_type(ParserRulesParser.Floating_pt_typeContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"floating_pt_type");
        builder.append(table).append("floating_pt_type").append("\n");
    }

    @Override
    public void enterInteger_type(ParserRulesParser.Integer_typeContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"integer_type");
        builder.append(table).append("integer_type").append("\n");
    }

    @Override
    public void enterSigned_int(ParserRulesParser.Signed_intContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }

        System.out.println(table+"signed_int");
        builder.append(table).append("signed_int").append("\n");

        table.append("\t");

        if(ctx.getText().equals("short") || ctx.getText().equals("int16")){
            System.out.println(table+"\t"+"short | int16");
            builder.append(table).append("short | int16").append("\n");
        }

        if(ctx.getText().equals("long") || ctx.getText().equals("int32")){
            System.out.println(table+"\t"+"long | int32");
            builder.append(table).append("long | int32").append("\n");
        }

        if(ctx.getText().equals("long long") || ctx.getText().equals("int64")){
            System.out.println(table+"\t"+"long long | int64");
            builder.append(table).append("long long | int64").append("\n");
        }

        if(ctx.getText().equals("int8")){
            System.out.println(table+"\t"+"int8");
            builder.append(table).append("int8").append("\n");
        }

    }

    @Override
    public void enterUnsigned_int(ParserRulesParser.Unsigned_intContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }

        System.out.println(table+"signed_int");
        builder.append(table).append("signed_int").append("\n");

        table.append("\t");

        if(ctx.getText().equals("unsigned short") || ctx.getText().equals("uint16")){
            System.out.println(table+"\t"+"unsigned short | uint16");
            builder.append(table).append("unsigned short | uint16").append("\n");
        }

        if(ctx.getText().equals("unsigned long") || ctx.getText().equals("uint32")){
            System.out.println(table+"\t"+"unsigned long | uint32");
            builder.append(table).append("unsigned long | uint32").append("\n");
        }

        if(ctx.getText().equals("unsigned long long") || ctx.getText().equals("uint64")){
            System.out.println(table+"\t"+"unsigned long long | uint64");
            builder.append(table).append("unsigned long long | uint64").append("\n");
        }

        if(ctx.getText().equals("uint8")){
            System.out.println(table+"\t"+"uint8");
            builder.append(table).append("uint8").append("\n");
        }

    }

    @Override
    public void enterDeclarators(ParserRulesParser.DeclaratorsContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"declarators");
        builder.append(table).append("declarators").append("\n");
    }

    @Override
    public void enterDeclarator(ParserRulesParser.DeclaratorContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"declarator");
        builder.append(table).append("declarator").append("\n");
    }

    @Override
    public void enterSimple_declarator(ParserRulesParser.Simple_declaratorContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        builder.append(table).append("simple_declarator\n");
        System.out.println(table+"simple_declarator");
        builder.append(table).append("\t").append(ctx.ID()).append("\n");
        System.out.println(table+"\t"+ctx.ID());
        //子节点数超过1存在赋值语句
        if(ctx.getChildCount() > 1){
            builder.append(table).append("\t").append("=\n");
            System.out.println(table+"\t=");
        }
    }

    @Override
    public void enterArray_declarator(ParserRulesParser.Array_declaratorContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        builder.append(table).append("array_declarator\n");
        System.out.println(table+"array_declarator");
        table.append("\t");
        builder.append(table).append(ctx.ID()).append("\n");
        builder.append(table).append("[").append("\n");
        System.out.print(builder);
    }



    @Override
    public void enterExp_list(ParserRulesParser.Exp_listContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        //赋值前加入等号
        builder.append(table).append("=\n");
        builder.append(table).append("exp_list\n");
        System.out.println(table+"=");
        System.out.println(table+"exp_list");
        table.append("\t");
        builder.append(table).append("[\n");

        System.out.print(builder);
    }


    @Override
    public void enterOr_expr(ParserRulesParser.Or_exprContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"or_expr");
        builder.append(table).append("or_expr\n");
    }

    @Override
    public void exitOr_expr(ParserRulesParser.Or_exprContext ctx) {
        //找出当前结点在所有子节点中的位置
        ParserRuleContext parent = ctx.getParent();
        int index = 0;
        List<ParseTree> children =  parent.children;
        for(ParseTree child : children){
            if(child != ctx){
                index++;
            } else {
                break;
            }
        }
        //存在后继节点，则需要添加 *
        if(index + 1 < parent.getChildCount()){
            StringBuilder table = new StringBuilder();
            int depth = ctx.depth();
            for(int i=0; i<depth; ++i){
                table.append("\t");
            }
            table.append(parent.getChild(index+1));
            System.out.println(table);
            builder.append(table).append("\n");
        }
    }

    @Override
    public void enterXor_expr(ParserRulesParser.Xor_exprContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }

        System.out.println(table+"xor_expr");
        builder.append(table).append("xor_expr\n");
    }

    @Override
    //处理运算符
    public void exitXor_expr(ParserRulesParser.Xor_exprContext ctx) {
        //找出当前结点在所有子节点中的位置
        ParserRuleContext parent = ctx.getParent();
        int index = 0;
        List<ParseTree> children =  parent.children;
        for(ParseTree child : children){
            if(child != ctx){
                index++;
            } else {
                break;
            }
        }
        //存在后继节点，则需要添加 *
        if(index + 1 < parent.getChildCount()){
            StringBuilder table = new StringBuilder();
            int depth = ctx.depth();
            for(int i=0; i<depth; ++i){
                table.append("\t");
            }
            table.append(parent.getChild(index+1));
            System.out.println(table);
            builder.append(table).append("\n");
        }
    }

    @Override
    public void enterAnd_expr(ParserRulesParser.And_exprContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"and_expr");
        builder.append(table).append("and_expr\n");
    }

    @Override
    //处理运算符
    public void exitAnd_expr(ParserRulesParser.And_exprContext ctx) {
        //找出当前结点在所有子节点中的位置
        ParserRuleContext parent = ctx.getParent();
        int index = 0;
        List<ParseTree> children =  parent.children;
        for(ParseTree child : children){
            if(child != ctx){
                index++;
            } else {
                break;
            }
        }
        //存在后继节点，则需要添加 *
        if(index + 1 < parent.getChildCount()){
            StringBuilder table = new StringBuilder();
            int depth = ctx.depth();
            for(int i=0; i<depth; ++i){
                table.append("\t");
            }
            table.append(parent.getChild(index+1));
            System.out.println(table);
            builder.append(table).append("\n");
        }
    }

    @Override
    public void enterShift_expr(ParserRulesParser.Shift_exprContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"shift_expr");
        builder.append(table).append("shift_expr\n");
    }

    @Override
    //处理运算符
    public void exitShift_expr(ParserRulesParser.Shift_exprContext ctx) {
        //找出当前结点在所有子节点中的位置
        ParserRuleContext parent = ctx.getParent();
        int index = 0;
        List<ParseTree> children =  parent.children;
        for(ParseTree child : children){
            if(child != ctx){
                index++;
            } else {
                break;
            }
        }
        //存在后继节点，则需要添加 *
        if(index + 1 < parent.getChildCount()){
            StringBuilder table = new StringBuilder();
            int depth = ctx.depth();
            for(int i=0; i<depth; ++i){
                table.append("\t");
            }
            table.append(parent.getChild(index+1));
            System.out.println(table);
            builder.append(table).append("\n");
        }
    }

    @Override
    public void enterAdd_expr(ParserRulesParser.Add_exprContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"add_expr");
        builder.append(table).append("add_expr\n");    }

    @Override
    //处理运算符
    public void exitAdd_expr(ParserRulesParser.Add_exprContext ctx) {
        //找出当前结点在所有子节点中的位置
        ParserRuleContext parent = ctx.getParent();
        int index = 0;
        List<ParseTree> children =  parent.children;
        for(ParseTree child : children){
            if(child != ctx){
                index++;
            } else {
                break;
            }
        }
        //存在后继节点，则需要添加 *
        if(index + 1 < parent.getChildCount()){
            StringBuilder table = new StringBuilder();
            int depth = ctx.depth();
            for(int i=0; i<depth; ++i){
                table.append("\t");
            }
            table.append(parent.getChild(index+1));
            System.out.println(table);
            builder.append(table).append("\n");
        }
    }

    @Override
    public void enterMult_expr(ParserRulesParser.Mult_exprContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"mult_expr");
        builder.append(table).append("mult_expr\n");
    }

    @Override
    //处理运算符
    public void exitMult_expr(ParserRulesParser.Mult_exprContext ctx) {
        //找出当前结点在所有子节点中的位置
        ParserRuleContext parent = ctx.getParent();
        int index = 0;
        List<ParseTree> children =  parent.children;
        for(ParseTree child : children){
            if(child != ctx){
                index++;
            } else {
                break;
            }
        }
        //存在后继节点，则需要添加 *
        if(index + 1 < parent.getChildCount()){
            StringBuilder table = new StringBuilder();
            int depth = ctx.depth();
            for(int i=0; i<depth; ++i){
                table.append("\t");
            }
            table.append(parent.getChild(index+1));
            System.out.println(table);
            builder.append(table).append("\n");
        }
    }

    @Override
    public void enterUnary_expr(ParserRulesParser.Unary_exprContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"unary_expr");
        builder.append(table).append("unary_expr\n");

        //存在符号 + - ~
        if(ctx.getChildCount() > 1){
            table = new StringBuilder();
            for(int i=0; i<depth; ++i){
                table.append("\t");
            }
            table.append("\t").append(ctx.getChild(0).getText());
            builder.append(table).append("\n");
            System.out.println(table);
        }
    }

    @Override
    //处理运算符 *
    public void exitUnary_expr(ParserRulesParser.Unary_exprContext ctx) {
        //找出当前结点在所有子节点中的位置
        ParserRuleContext parent = ctx.getParent();
        int index = 0;
        List<ParseTree> children =  parent.children;
        for(ParseTree child : children){
            if(child != ctx){
                index++;
            } else {
                break;
            }
        }
        //存在后继节点，则需要添加 *
        if(index + 1 < parent.getChildCount()){
            StringBuilder table = new StringBuilder();
            int depth = ctx.depth();
            for(int i=0; i<depth; ++i){
                table.append("\t");
            }
            table.append(parent.getChild(index+1));
            System.out.println(table);
            builder.append(table).append("\n");
        }
    }

    @Override
    public void enterLiteral(ParserRulesParser.LiteralContext ctx) {
        StringBuilder table = new StringBuilder();
        int depth = ctx.depth();
        for(int i=0; i<depth; ++i){
            table.append("\t");
        }
        System.out.println(table+"literal");
        builder.append(table).append("literal\n");

        if(ctx.INTEGER()!=null){
            builder.append(table).append("\t").append("INTEGER:").append(ctx.INTEGER().getText()).append("\n");
            System.out.print(builder);
        }

        if(ctx.FLOATING_PT()!=null){
            builder.append(table).append("\t").append("FLOATING_PT:").append(ctx.FLOATING_PT().getText()).append("\n");
            System.out.print(builder);
        }

        if(ctx.CHAR()!=null){
            builder.append(table).append("\t").append("CHAR:").append(ctx.CHAR().getText()).append("\n");
            System.out.print(builder);
        }

        if(ctx.STRING()!=null){
            builder.append(table).append("\t").append("STRING:").append(ctx.STRING().getText()).append("\n");
            System.out.print(builder);
        }

        if(ctx.BOOLEAN()!=null){
            builder.append(table).append("\t").append("BOOLEAN:").append(ctx.BOOLEAN().getText()).append("\n");
            System.out.print(builder);
        }
    }

}
