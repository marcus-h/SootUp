/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999 Patrick Lam
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.  
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */






package de.upb.soot.jimple.internal;

import de.upb.soot.jimple.AbstractUnit;
import de.upb.soot.jimple.FieldRef;
import de.upb.soot.jimple.ValueBox;
import de.upb.soot.jimple.expr.ArrayRef;
import de.upb.soot.jimple.expr.InvokeExpr;
import de.upb.soot.jimple.stmt.Stmt;

public abstract class AbstractStmt extends AbstractUnit implements Stmt
{

    @Override
    public boolean containsInvokeExpr()
    {
        return false;
    }

    @Override
    public InvokeExpr getInvokeExpr()
    {
        throw new RuntimeException("getInvokeExpr() called with no invokeExpr present!");
    }

    @Override
    public ValueBox getInvokeExprBox()
    {
        throw new RuntimeException("getInvokeExprBox() called with no invokeExpr present!");
    }

    @Override
    public boolean containsArrayRef()
    {
	return false;
    }

    @Override
    public ArrayRef getArrayRef()
    {
	throw new RuntimeException("getArrayRef() called with no ArrayRef present!");
    }

    @Override
    public ValueBox getArrayRefBox()
    {
	throw new RuntimeException("getArrayRefBox() called with no ArrayRef present!");
    }

    @Override
    public boolean containsFieldRef()
    {
	return false;
    }

    @Override
    public FieldRef getFieldRef()
    {
	throw new RuntimeException("getFieldRef() called with no FieldRef present!");
    }

    @Override
    public ValueBox getFieldRefBox()
    {
	throw new RuntimeException("getFieldRefBox() called with no FieldRef present!");
    }

}









