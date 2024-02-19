package sootup.java.bytecode.frontend;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

import categories.Java8Test;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.model.SourceType;
import sootup.core.signatures.MethodSignature;
import sootup.java.bytecode.inputlocation.DefaultRTJarAnalysisInputLocation;
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.JavaSootMethod;
import sootup.java.core.types.JavaClassType;
import sootup.java.core.views.JavaView;

@Category(Java8Test.class)
public class AsmMethodSourceTest {

  @Test
  public void testFix_StackUnderrun_convertPutFieldInsn_init() {

    double version = Double.parseDouble(System.getProperty("java.specification.version"));
    if (version > 1.8) {
      fail("The rt.jar is not available after Java 8. You are using version " + version);
    }

    JavaView view = new JavaView(new DefaultRTJarAnalysisInputLocation());

    final JavaIdentifierFactory idf = JavaIdentifierFactory.getInstance();
    JavaClassType mainClassSignature =
        idf.getClassType("javax.management.NotificationBroadcasterSupport");
    MethodSignature mainMethodSignature =
        idf.getMethodSignature(
            mainClassSignature,
            "<init>",
            "void",
            Arrays.asList(
                "java.util.concurrent.Executor", "javax.management.MBeanNotificationInfo[]"));

    assertTrue(idf.isConstructorSignature(mainMethodSignature));
    assertTrue(idf.isConstructorSubSignature(mainMethodSignature.getSubSignature()));

    final SootClass abstractClass = view.getClass(mainClassSignature).get();

    final SootMethod method = abstractClass.getMethod(mainMethodSignature.getSubSignature()).get();
    method.getBody().getStmts();
  }

  @Test
  public void testNestedMethodCalls() {
    JavaClassPathAnalysisInputLocation inputLocation =
        new JavaClassPathAnalysisInputLocation(
            "../shared-test-resources/bugfixes/", SourceType.Application, Collections.emptyList());
    JavaView view = new JavaView(Collections.singletonList(inputLocation));

    JavaSootMethod method =
        view.getMethod(
                JavaIdentifierFactory.getInstance()
                    .parseMethodSignature("<NestedMethodCall: void nestedMethodCall()>"))
            .get();
    assertEquals(
        "this := @this: NestedMethodCall;\n"
            + "i = 0;\n"
            + "s = \"abc\";\n"
            + "i = i + 1;\n"
            + "$stack5 = virtualinvoke s.<java.lang.String: char charAt(int)>(i);\n"
            + "$stack3 = i;\n"
            + "i = i + 1;\n"
            + "$stack4 = virtualinvoke s.<java.lang.String: char charAt(int)>($stack3);\n"
            + "virtualinvoke this.<NestedMethodCall: void decode(char,char)>($stack5, $stack4);\n"
            + "\n"
            + "return;",
        method.getBody().getStmtGraph().toString().trim());
  }
}
