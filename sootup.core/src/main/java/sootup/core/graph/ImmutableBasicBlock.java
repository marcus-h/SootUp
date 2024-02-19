package sootup.core.graph;

/*-
 * #%L
 * SootUp
 * %%
 * Copyright (C) 1997 - 2024 Raja Vallée-Rai and others
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
import java.util.Map;
import javax.annotation.Nonnull;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.types.ClassType;

// FIXME: [ms] IMPLEMENT!
public class ImmutableBasicBlock implements BasicBlock<ImmutableBasicBlock> {

  @Nonnull
  @Override
  public List<ImmutableBasicBlock> getPredecessors() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Nonnull
  @Override
  public List<ImmutableBasicBlock> getSuccessors() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  @Nonnull
  public Map<ClassType, ImmutableBasicBlock> getExceptionalPredecessors() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Nonnull
  @Override
  public Map<? extends ClassType, ImmutableBasicBlock> getExceptionalSuccessors() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Nonnull
  @Override
  public List<Stmt> getStmts() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public int getStmtCount() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Nonnull
  @Override
  public Stmt getHead() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Nonnull
  @Override
  public Stmt getTail() {
    throw new UnsupportedOperationException("not implemented");
  }
}
