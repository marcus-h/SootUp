package de.upb.swt.soot.test;

import static org.junit.Assert.*;

import categories.Java8Test;
import de.upb.swt.soot.core.jimple.Jimple;
import de.upb.swt.soot.core.jimple.basic.Immediate;
import de.upb.swt.soot.core.jimple.basic.Local;
import de.upb.swt.soot.core.jimple.basic.Value;
import de.upb.swt.soot.core.jimple.common.expr.Expr;
import de.upb.swt.soot.core.jimple.common.expr.JSpecialInvokeExpr;
import de.upb.swt.soot.core.jimple.common.expr.JStaticInvokeExpr;
import de.upb.swt.soot.core.jimple.visitor.ReplaceUseExprVisitor;
import de.upb.swt.soot.core.signatures.MethodSignature;
import de.upb.swt.soot.core.types.Type;
import de.upb.swt.soot.java.core.JavaIdentifierFactory;
import de.upb.swt.soot.java.core.language.JavaJimple;
import de.upb.swt.soot.java.core.types.JavaClassType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/** @author Zun Wang */
@Category(Java8Test.class)
public class ReplaceUseExprVisitorTest {

  JavaIdentifierFactory factory = JavaIdentifierFactory.getInstance();
  JavaClassType intType = factory.getClassType("int");
  JavaClassType testClass = factory.getClassType("TestClass");
  JavaClassType voidType = factory.getClassType("void");

  Local op1 = JavaJimple.newLocal("op1", intType);
  Local op2 = JavaJimple.newLocal("op2", intType);
  Local newOp = JavaJimple.newLocal("op#", intType);

  Local base = JavaJimple.newLocal("base", testClass);
  Local arg1 = JavaJimple.newLocal("arg1", intType);
  Local arg2 = JavaJimple.newLocal("arg2", intType);
  Local arg3 = JavaJimple.newLocal("arg3", intType);
  Local newArg = JavaJimple.newLocal("argn", intType);

  MethodSignature methodeWithOutParas =
      new MethodSignature(testClass, "invokeExpr", Collections.emptyList(), voidType);

  /** Test use replacing in case BinopExpr. JaddExpr is as an example. */
  @Test
  public void testCaseBinopExpr() {

    ReplaceUseExprVisitor visitor = new ReplaceUseExprVisitor(op1, newOp);

    // replace op1 with newOp1
    Expr addExpr = JavaJimple.newAddExpr(op1, op2);
    addExpr.accept(visitor);
    Expr newExpr = visitor.getNewExpr();

    List<Value> expectedUses = new ArrayList<>();
    expectedUses.add(newOp);
    expectedUses.add(op2);
    assertTrue(newExpr.getUses().equals(expectedUses));

    // replace op1 and op1 with newOp1
    addExpr = JavaJimple.newAddExpr(op1, op1);
    addExpr.accept(visitor);
    newExpr = visitor.getNewExpr();

    expectedUses.set(1, newOp);
    assertTrue(newExpr.getUses().equals(expectedUses));

    // there's no matched op in Expr
    addExpr = JavaJimple.newAddExpr(op2, op2);
    addExpr.accept(visitor);
    newExpr = visitor.getNewExpr();
    assertTrue(newExpr.equivTo(addExpr));
  }

  /**
   * Test use replacing in case JStaticInvokeExpr JDynamicInvoke JNewMultiArrayExpr.
   * JStaticInvokeExpr is as an example.
   */
  @Test
  public void testCaseInvokeExpr() {

    List<Immediate> args = new ArrayList<Immediate>();
    args.add(arg1);
    args.add(arg2);
    args.add(arg3);
    List<Type> parameters = new ArrayList<>();
    parameters.add(intType);
    parameters.add(intType);
    parameters.add(intType);

    MethodSignature method = new MethodSignature(testClass, "invokeExpr", parameters, voidType);

    ReplaceUseExprVisitor visitor = new ReplaceUseExprVisitor(arg1, newArg);

    // replace arg1 in args with newArg
    Expr invokeExpr = new JStaticInvokeExpr(method, args);
    invokeExpr.accept(visitor);
    Expr newInvokeExpr = visitor.getNewExpr();

    List<Value> expectedUses = new ArrayList<>();
    expectedUses.add(newArg);
    expectedUses.add(arg2);
    expectedUses.add(arg3);
    assertTrue(newInvokeExpr.getUses().equals(expectedUses));

    // repalce arg1 2 times in args with newArg
    args.set(2, arg1);
    invokeExpr = new JStaticInvokeExpr(method, args);
    invokeExpr.accept(visitor);
    newInvokeExpr = visitor.getNewExpr();

    expectedUses.set(2, newArg);
    assertTrue(newInvokeExpr.getUses().equals(expectedUses));

    // There's no matched arg in args
    invokeExpr = new JStaticInvokeExpr(methodeWithOutParas, Collections.emptyList());
    invokeExpr.accept(visitor);
    assertTrue(visitor.getNewExpr().equivTo(invokeExpr));
  }

  /** Test use replacing in case InstanceInvokeExpr. JSpecialInvokeExpr is as an example. */
  @Test
  public void testCaseInstanceInvokeExpr() {

    List<Immediate> args = new ArrayList<Immediate>();
    args.add(arg1);
    args.add(arg2);
    args.add(arg3);
    List<Type> parameters = new ArrayList<>();
    parameters.add(intType);
    parameters.add(intType);
    parameters.add(intType);

    MethodSignature method = new MethodSignature(testClass, "invokeExpr", parameters, voidType);

    ReplaceUseExprVisitor visitor = new ReplaceUseExprVisitor(arg1, newArg);

    // replace arg1 in case and args with newArg
    Expr invokeExpr = new JSpecialInvokeExpr(arg1, method, args);
    invokeExpr.accept(visitor);
    Expr newInvokeExpr = visitor.getNewExpr();

    List<Value> expectedUses = new ArrayList<>();
    expectedUses.add(newArg);
    expectedUses.add(arg2);
    expectedUses.add(arg3);
    expectedUses.add(newArg);
    assertTrue(newInvokeExpr.getUses().equals(expectedUses));

    // replace arg1 in args with newArg
    args.set(2, arg1);
    invokeExpr = new JSpecialInvokeExpr(base, method, args);
    invokeExpr.accept(visitor);
    newInvokeExpr = visitor.getNewExpr();

    expectedUses.set(2, newArg);
    expectedUses.set(3, base);

    assertTrue(newInvokeExpr.getUses().equals(expectedUses));

    // replace arg1=base with newArg
    invokeExpr = new JSpecialInvokeExpr(arg1, methodeWithOutParas, Collections.emptyList());
    invokeExpr.accept(visitor);
    newInvokeExpr = visitor.getNewExpr();
    expectedUses.clear();
    expectedUses.add(newArg);
    assertTrue(newInvokeExpr.getUses().equals(expectedUses));

    // There's no matched arg in args, no matched base
    invokeExpr = new JSpecialInvokeExpr(base, methodeWithOutParas, Collections.emptyList());
    invokeExpr.accept(visitor);
    newInvokeExpr = visitor.getNewExpr();
    assertTrue(newInvokeExpr.equivTo(invokeExpr));
  }

  /**
   * Test use replacing in case UnopExpr, JCastExpr, JInstanceOfExpr, JNewArrayExpr. JLengthExpr is
   * as an example.
   */
  @Test
  public void testUnopExpr() {

    ReplaceUseExprVisitor visitor = new ReplaceUseExprVisitor(op1, newOp);

    // replace op1 with newOp1
    Expr lengthExpr = Jimple.newLengthExpr(op1);
    lengthExpr.accept(visitor);
    Expr newExpr = visitor.getNewExpr();

    List<Value> expectedUses = new ArrayList<>();
    expectedUses.add(newOp);
    assertTrue(newExpr.getUses().equals(expectedUses));

    // There's no matched op
    lengthExpr = Jimple.newLengthExpr(op2);
    lengthExpr.accept(visitor);
    assertTrue(visitor.getNewExpr().equivTo(lengthExpr));
  }
}