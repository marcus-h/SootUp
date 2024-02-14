package sootup.jimple.parser.javatestsuite.java6;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.jimple.parser.javatestsuite.JimpleTestSuiteBase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** @author Kaustubh Kelkar */
@Tag("Java8")
public class SymbolsAsClassNameTest extends JimpleTestSuiteBase {
  public MethodSignature getMethodSignature() {
    return identifierFactory.getMethodSignature(
        getDeclaredClassSignature(), "αρετηAsClassName", "void", Collections.emptyList());
  }

  @Disabled
  public void test() {
    /**
     * TODO: does only work on a Unicode filesystem
     *
     * <p>Exception in thread "main" java.nio.file.InvalidPathException: Illegal char <?> at index
     * 1: a?et?.java
     */
    SootMethod method = loadMethod(getMethodSignature());
    assertJimpleStmts(method, expectedBodyStmts());
    SootClass sootClass = loadClass(getDeclaredClassSignature());
    System.out.println(sootClass.getClassSource().getClassType().getClassName());
  }

  public List<String> expectedBodyStmts() {
    return Stream.of(
            "r0 := @this: αρετη",
            "r1 = <java.lang.System: java.io.PrintStream out>",
            "virtualinvoke r1.<java.io.PrintStream: void println(java.lang.String)>(\"this is αρετη class\")",
            "return")
        .collect(Collectors.toList());
  }
}
