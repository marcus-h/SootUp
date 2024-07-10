package sootup.java.codepropertygraph.ast;

import javax.annotation.Nonnull;
import sootup.core.jimple.basic.Immediate;
import sootup.core.jimple.common.expr.Expr;
import sootup.core.jimple.common.ref.Ref;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.javabytecode.stmt.JEnterMonitorStmt;
import sootup.core.jimple.javabytecode.stmt.JExitMonitorStmt;
import sootup.core.jimple.javabytecode.stmt.JSwitchStmt;
import sootup.core.jimple.visitor.AbstractStmtVisitor;
import sootup.core.model.Body;
import sootup.java.codepropertygraph.propertygraph.*;
import sootup.java.codepropertygraph.propertygraph.nodes.*;

public class AstStmtVisitor extends AbstractStmtVisitor<Void> {
  private final PropertyGraph graph;
  private final PropertyGraphNode parentNode;
  private final Body body;

  public AstStmtVisitor(PropertyGraph graph, PropertyGraphNode parentNode, Body body) {
    this.graph = graph;
    this.parentNode = parentNode;
    this.body = body;
  }

  private void addExprNode(PropertyGraphNode parent, Expr expr) {
    ExprGraphNode exprNode = new ExprGraphNode(expr);
    graph.addEdge(new PropertyGraphEdge(parent, exprNode, EdgeType.AST));
    expr.accept(new AstExprVisitor(graph, exprNode));
  }

  private void addImmediateNode(PropertyGraphNode parent, Immediate immediate) {
    ImmediateGraphNode immediateNode = new ImmediateGraphNode(immediate);
    graph.addEdge(new PropertyGraphEdge(parent, immediateNode, EdgeType.AST));
  }

  private void addRefNode(PropertyGraphNode parent, Ref ref) {
    RefGraphNode refNode = new RefGraphNode(ref);
    graph.addEdge(new PropertyGraphEdge(parent, refNode, EdgeType.AST));
  }

  @Override
  public void caseAssignStmt(@Nonnull JAssignStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    if (stmt.getLeftOp() instanceof Immediate) {
      addImmediateNode(stmtNode, (Immediate) stmt.getLeftOp());
    } else if (stmt.getLeftOp() instanceof Ref) {
      addRefNode(stmtNode, (Ref) stmt.getLeftOp());
    } else {
      addExprNode(stmtNode, (Expr) stmt.getLeftOp());
    }
    if (stmt.getRightOp() instanceof Immediate) {
      addImmediateNode(stmtNode, (Immediate) stmt.getRightOp());
    } else if (stmt.getRightOp() instanceof Ref) {
      addRefNode(stmtNode, (Ref) stmt.getRightOp());
    } else {
      addExprNode(stmtNode, (Expr) stmt.getRightOp());
    }
  }

  @Override
  public void caseInvokeStmt(@Nonnull JInvokeStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    addExprNode(stmtNode, stmt.getInvokeExpr());
  }

  @Override
  public void caseReturnStmt(@Nonnull JReturnStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    addImmediateNode(stmtNode, stmt.getOp());
  }

  @Override
  public void caseIfStmt(@Nonnull JIfStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    addExprNode(stmtNode, stmt.getCondition());
  }

  @Override
  public void caseNopStmt(@Nonnull JNopStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
  }

  @Override
  public void caseThrowStmt(@Nonnull JThrowStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    addImmediateNode(stmtNode, stmt.getOp());
  }

  @Override
  public void caseIdentityStmt(@Nonnull JIdentityStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    addImmediateNode(stmtNode, stmt.getLeftOp());
    addRefNode(stmtNode, stmt.getRightOp());
  }

  @Override
  public void caseGotoStmt(@Nonnull JGotoStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
  }

  @Override
  public void caseEnterMonitorStmt(@Nonnull JEnterMonitorStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    addImmediateNode(stmtNode, stmt.getOp());
  }

  @Override
  public void caseExitMonitorStmt(@Nonnull JExitMonitorStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    addImmediateNode(stmtNode, stmt.getOp());
  }

  @Override
  public void caseSwitchStmt(@Nonnull JSwitchStmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
    addImmediateNode(stmtNode, stmt.getKey());
    stmt.getTargetStmts(body)
        .forEach(
            target ->
                graph.addEdge(
                    new PropertyGraphEdge(stmtNode, new StmtGraphNode(target), EdgeType.AST)));
  }

  @Override
  public void defaultCaseStmt(@Nonnull Stmt stmt) {
    StmtGraphNode stmtNode = new StmtGraphNode(stmt);
    graph.addEdge(new PropertyGraphEdge(parentNode, stmtNode, EdgeType.AST));
  }
}
