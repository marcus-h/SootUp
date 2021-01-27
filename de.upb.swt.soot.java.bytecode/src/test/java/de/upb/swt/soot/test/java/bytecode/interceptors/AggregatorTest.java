package de.upb.swt.soot.test.java.bytecode.interceptors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import de.upb.swt.soot.core.jimple.basic.Local;
import de.upb.swt.soot.core.jimple.basic.NoPositionInformation;
import de.upb.swt.soot.core.jimple.basic.StmtPositionInfo;
import de.upb.swt.soot.core.jimple.basic.Trap;
import de.upb.swt.soot.core.jimple.common.constant.IntConstant;
import de.upb.swt.soot.core.jimple.common.stmt.Stmt;
import de.upb.swt.soot.core.model.Body;
import de.upb.swt.soot.core.types.PrimitiveType;
import de.upb.swt.soot.core.util.ImmutableUtils;
import de.upb.swt.soot.java.bytecode.interceptors.Aggregator;
import de.upb.swt.soot.java.core.JavaIdentifierFactory;
import de.upb.swt.soot.java.core.language.JavaJimple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class AggregatorTest {

  @Test
  public void testAggregation() {
    Body.BodyBuilder testBuilder = createBody(true);
    Body testBody = testBuilder.build();
    new Aggregator().interceptBody(testBuilder);
    Body processedBody = testBuilder.build();
    List<Stmt> originalStmts = testBody.getStmts();
    List<Stmt> processedStmts = processedBody.getStmts();

    assertEquals(originalStmts.size(), processedStmts.size());
    assertNotEquals(originalStmts.get(0), processedStmts.get(0));
    assertEquals("b = a", originalStmts.get(1).toString());
    assertEquals("b = 7", processedStmts.get(1).toString());
    assertEquals(originalStmts.get(2), processedStmts.get(2));
  }

  @Test
  public void testNoAggregation() {
    Body.BodyBuilder testBuilder = createBody(false);
    Body testBody = testBuilder.build();
    new Aggregator().interceptBody(testBuilder);
    Body processedBody = testBuilder.build();
    List<Stmt> originalStmts = testBody.getStmts();
    List<Stmt> processedStmts = processedBody.getStmts();

    assertEquals(originalStmts.size(), processedStmts.size());
    for (int i = 0; i < processedStmts.size(); i++) {
      assertEquals(originalStmts.get(i).toString(), processedStmts.get(i).toString());
    }
  }

  private static Body.BodyBuilder createBody(boolean withAggregation) {
    StmtPositionInfo noPositionInfo = StmtPositionInfo.createNoStmtPositionInfo();

    Local a = JavaJimple.newLocal("a", PrimitiveType.getInt());
    Local b = JavaJimple.newLocal("b", PrimitiveType.getInt());

    Stmt intToA = JavaJimple.newAssignStmt(a, IntConstant.getInstance(7), noPositionInfo);
    Stmt intToB;
    if (withAggregation) {
      intToB = JavaJimple.newAssignStmt(b, a, noPositionInfo);
    } else {
      intToB = JavaJimple.newAssignStmt(b, IntConstant.getInstance(42), noPositionInfo);
    }
    Stmt ret = JavaJimple.newReturnVoidStmt(noPositionInfo);

    Set<Local> locals = ImmutableUtils.immutableSet(a, b);

    List<Trap> traps = new ArrayList<>();

    Body.BodyBuilder builder = Body.builder();
    builder.setStartingStmt(intToA);
    builder.setMethodSignature(
        JavaIdentifierFactory.getInstance()
            .getMethodSignature("test", "ab.c", "void", Collections.emptyList()));

    builder.addFlow(intToA, intToB);
    builder.addFlow(intToB, ret);
    builder.setLocals(locals);
    builder.setTraps(traps);
    builder.setPosition(NoPositionInformation.getInstance());

    return builder;
  }
}
