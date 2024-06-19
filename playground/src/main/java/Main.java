import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.views.JavaView;

public class Main {

  public static void main(String[] args) {
    JavaClassPathAnalysisInputLocation ail = new JavaClassPathAnalysisInputLocation(args[0]);
    JavaView view = new JavaView(ail);
    view.getClasses().stream().forEach(sc -> {});
  }
}
