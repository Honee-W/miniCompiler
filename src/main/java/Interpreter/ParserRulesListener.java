// Generated from C:/Users/86153/Desktop/MyInterpreter/src/main/java\ParserRules.g4 by ANTLR 4.10.1
package Interpreter;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ParserRulesParser}.
 */
public interface ParserRulesListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#specification}.
	 * @param ctx the parse tree
	 */
	void enterSpecification(ParserRulesParser.SpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#specification}.
	 * @param ctx the parse tree
	 */
	void exitSpecification(ParserRulesParser.SpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(ParserRulesParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(ParserRulesParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(ParserRulesParser.ModuleContext ctx) throws Exception;
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(ParserRulesParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#type_decl}.
	 * @param ctx the parse tree
	 */
	void enterType_decl(ParserRulesParser.Type_declContext ctx) throws Exception;
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#type_decl}.
	 * @param ctx the parse tree
	 */
	void exitType_decl(ParserRulesParser.Type_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#struct_type}.
	 * @param ctx the parse tree
	 */
	void enterStruct_type(ParserRulesParser.Struct_typeContext ctx) throws Exception;
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#struct_type}.
	 * @param ctx the parse tree
	 */
	void exitStruct_type(ParserRulesParser.Struct_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#member_list}.
	 * @param ctx the parse tree
	 */
	void enterMember_list(ParserRulesParser.Member_listContext ctx) throws Exception;
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#member_list}.
	 * @param ctx the parse tree
	 */
	void exitMember_list(ParserRulesParser.Member_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void enterType_spec(ParserRulesParser.Type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#type_spec}.
	 * @param ctx the parse tree
	 */
	void exitType_spec(ParserRulesParser.Type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#scoped_name}.
	 * @param ctx the parse tree
	 */
	void enterScoped_name(ParserRulesParser.Scoped_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#scoped_name}.
	 * @param ctx the parse tree
	 */
	void exitScoped_name(ParserRulesParser.Scoped_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#base_type_spec}.
	 * @param ctx the parse tree
	 */
	void enterBase_type_spec(ParserRulesParser.Base_type_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#base_type_spec}.
	 * @param ctx the parse tree
	 */
	void exitBase_type_spec(ParserRulesParser.Base_type_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#floating_pt_type}.
	 * @param ctx the parse tree
	 */
	void enterFloating_pt_type(ParserRulesParser.Floating_pt_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#floating_pt_type}.
	 * @param ctx the parse tree
	 */
	void exitFloating_pt_type(ParserRulesParser.Floating_pt_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#integer_type}.
	 * @param ctx the parse tree
	 */
	void enterInteger_type(ParserRulesParser.Integer_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#integer_type}.
	 * @param ctx the parse tree
	 */
	void exitInteger_type(ParserRulesParser.Integer_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#signed_int}.
	 * @param ctx the parse tree
	 */
	void enterSigned_int(ParserRulesParser.Signed_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#signed_int}.
	 * @param ctx the parse tree
	 */
	void exitSigned_int(ParserRulesParser.Signed_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#unsigned_int}.
	 * @param ctx the parse tree
	 */
	void enterUnsigned_int(ParserRulesParser.Unsigned_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#unsigned_int}.
	 * @param ctx the parse tree
	 */
	void exitUnsigned_int(ParserRulesParser.Unsigned_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#declarators}.
	 * @param ctx the parse tree
	 */
	void enterDeclarators(ParserRulesParser.DeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#declarators}.
	 * @param ctx the parse tree
	 */
	void exitDeclarators(ParserRulesParser.DeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#declarator}.
	 * @param ctx the parse tree
	 */
	void enterDeclarator(ParserRulesParser.DeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#declarator}.
	 * @param ctx the parse tree
	 */
	void exitDeclarator(ParserRulesParser.DeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#simple_declarator}.
	 * @param ctx the parse tree
	 */
	void enterSimple_declarator(ParserRulesParser.Simple_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#simple_declarator}.
	 * @param ctx the parse tree
	 */
	void exitSimple_declarator(ParserRulesParser.Simple_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#array_declarator}.
	 * @param ctx the parse tree
	 */
	void enterArray_declarator(ParserRulesParser.Array_declaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#array_declarator}.
	 * @param ctx the parse tree
	 */
	void exitArray_declarator(ParserRulesParser.Array_declaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#exp_list}.
	 * @param ctx the parse tree
	 */
	void enterExp_list(ParserRulesParser.Exp_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#exp_list}.
	 * @param ctx the parse tree
	 */
	void exitExp_list(ParserRulesParser.Exp_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#or_expr}.
	 * @param ctx the parse tree
	 */
	void enterOr_expr(ParserRulesParser.Or_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#or_expr}.
	 * @param ctx the parse tree
	 */
	void exitOr_expr(ParserRulesParser.Or_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#xor_expr}.
	 * @param ctx the parse tree
	 */
	void enterXor_expr(ParserRulesParser.Xor_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#xor_expr}.
	 * @param ctx the parse tree
	 */
	void exitXor_expr(ParserRulesParser.Xor_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#and_expr}.
	 * @param ctx the parse tree
	 */
	void enterAnd_expr(ParserRulesParser.And_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#and_expr}.
	 * @param ctx the parse tree
	 */
	void exitAnd_expr(ParserRulesParser.And_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#shift_expr}.
	 * @param ctx the parse tree
	 */
	void enterShift_expr(ParserRulesParser.Shift_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#shift_expr}.
	 * @param ctx the parse tree
	 */
	void exitShift_expr(ParserRulesParser.Shift_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#add_expr}.
	 * @param ctx the parse tree
	 */
	void enterAdd_expr(ParserRulesParser.Add_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#add_expr}.
	 * @param ctx the parse tree
	 */
	void exitAdd_expr(ParserRulesParser.Add_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#mult_expr}.
	 * @param ctx the parse tree
	 */
	void enterMult_expr(ParserRulesParser.Mult_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#mult_expr}.
	 * @param ctx the parse tree
	 */
	void exitMult_expr(ParserRulesParser.Mult_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#unary_expr}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expr(ParserRulesParser.Unary_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#unary_expr}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expr(ParserRulesParser.Unary_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserRulesParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(ParserRulesParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserRulesParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(ParserRulesParser.LiteralContext ctx);
}