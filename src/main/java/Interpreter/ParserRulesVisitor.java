// Generated from C:/Users/86153/Desktop/MyInterpreter/src/main/java\ParserRules.g4 by ANTLR 4.10.1
package Interpreter;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ParserRulesParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ParserRulesVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecification(ParserRulesParser.SpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(ParserRulesParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(ParserRulesParser.ModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#type_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_decl(ParserRulesParser.Type_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#struct_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_type(ParserRulesParser.Struct_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#member_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMember_list(ParserRulesParser.Member_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#type_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_spec(ParserRulesParser.Type_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#scoped_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScoped_name(ParserRulesParser.Scoped_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#base_type_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBase_type_spec(ParserRulesParser.Base_type_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#floating_pt_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloating_pt_type(ParserRulesParser.Floating_pt_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#integer_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger_type(ParserRulesParser.Integer_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#signed_int}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSigned_int(ParserRulesParser.Signed_intContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#unsigned_int}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsigned_int(ParserRulesParser.Unsigned_intContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#declarators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarators(ParserRulesParser.DeclaratorsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarator(ParserRulesParser.DeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#simple_declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_declarator(ParserRulesParser.Simple_declaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#array_declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_declarator(ParserRulesParser.Array_declaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#exp_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_list(ParserRulesParser.Exp_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#or_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_expr(ParserRulesParser.Or_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#xor_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXor_expr(ParserRulesParser.Xor_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#and_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_expr(ParserRulesParser.And_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#shift_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShift_expr(ParserRulesParser.Shift_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#add_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_expr(ParserRulesParser.Add_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#mult_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMult_expr(ParserRulesParser.Mult_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#unary_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_expr(ParserRulesParser.Unary_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ParserRulesParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(ParserRulesParser.LiteralContext ctx);
}