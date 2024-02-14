package sootup.jimple.parser.javatestsuite.java6;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.jimple.parser.javatestsuite.JimpleTestSuiteBase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** @author Kaustubh Kelkar */
@Tag("Java8")
public class AbstractClassTest extends JimpleTestSuiteBase {

  @Test
  public void test() {
    SootClass clazz = loadClass(getDeclaredClassSignature());
    // The SuperClass is the abstract one
    SootClass superClazz = loadClass(clazz.getSuperclass().get());
    assertTrue(superClazz.isAbstract());
    SootMethod method = loadMethod(getMethodSignature());
    assertJimpleStmts(method, expectedBodyStmts());
  }

  public MethodSignature getMethodSignature() {
    return identifierFactory.getMethodSignature(
        getDeclaredClassSignature(), "abstractClass", "void", Collections.emptyList());
  }

  public List<String> expectedBodyStmts() {
    return Stream.of(
            "l0 := @this: AbstractClass",
            "$stack2 = new AbstractClass",
            "specialinvoke $stack2.<AbstractClass: void <init>()>()",
            "l1 = $stack2",
            "virtualinvoke l1.<A: void a()>()",
            "return")
        .collect(Collectors.toList());
  }
}
