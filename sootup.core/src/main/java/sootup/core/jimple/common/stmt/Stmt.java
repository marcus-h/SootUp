package sootup.core.jimple.common.stmt;
/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 2018-2023 Linghui Luo, Christian Brüggemann, Markus Schmidt
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
import java.util.List;
import javax.annotation.Nonnull;
import sootup.core.jimple.basic.EquivTo;
import sootup.core.jimple.basic.LValue;
import sootup.core.jimple.basic.StmtPositionInfo;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.ref.JArrayRef;
import sootup.core.jimple.common.ref.JFieldRef;
import sootup.core.jimple.visitor.Acceptor;
import sootup.core.jimple.visitor.StmtVisitor;
import sootup.core.util.Copyable;
import sootup.core.util.printer.StmtPrinter;

public interface Stmt extends EquivTo, Acceptor<StmtVisitor>, Copyable {
  @Nonnull
  List<Value> getUses();

  @Nonnull
  List<LValue> getDefs();

  @Nonnull
  List<Value> getUsesAndDefs();

  /**
   * Returns true if execution after this statement may continue at the following statement. (e.g.
   * GotoStmt will return false and e.g. IfStmt will return true).
   */
  boolean fallsThrough();

  /**
   * Returns true if execution after this statement does not necessarily continue at the following
   * statement. The {@link BranchingStmt}'s GotoStmt, JSwitchStmt and IfStmt will return true.
   */
  boolean branches();

  int getExpectedSuccessorCount();

  void toString(@Nonnull StmtPrinter up);

  boolean containsInvokeExpr();

  AbstractInvokeExpr getInvokeExpr();

  boolean containsArrayRef();

  JArrayRef getArrayRef();

  boolean containsFieldRef();

  JFieldRef getFieldRef();

  StmtPositionInfo getPositionInfo();

  Stmt withNewUse(@Nonnull Value oldUse, @Nonnull Value newUse);
}
