package ee.veikokaap.debugproxy.testframework.utils;

import static org.objectweb.asm.Opcodes.ASM6;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class MarkerFinder {

  private final static Class MARKER_CLASS = BreakpointUtil.class;
  private final static String MARKER_METHOD_NAME = "mark";
  private final static String MARKER_METHOD_DESC = "(I)V";

  private final HashMap<Integer, BreakpointLocation> markerLocationMap = new HashMap<>();
  private final TestClassVisitor classVisitor = new TestClassVisitor();

  public static HashMap<Integer, BreakpointLocation> findLocations(Class clazz) {
    MarkerFinder markerFinder = new MarkerFinder();

    String resourceName = clazz.getSimpleName() + ".class";
    try (InputStream stream = clazz.getResourceAsStream(resourceName)) {
      if (stream == null) {
        throw new IllegalStateException("No class resource found with name " + resourceName);
      }
      ClassReader classReader = new ClassReader(stream);
      classReader.accept(markerFinder.classVisitor, 0);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

    return markerFinder.markerLocationMap;
  }

  private class TestMethodVisitor extends MethodVisitor {
    private int lineNr;
    private int id = -1;

    private TestMethodVisitor() {
      super(ASM6);
    }

    public void visitCode() {
    }

    public void visitLineNumber(int line, Label start) {
      this.lineNr = line;
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      if (matchesMarkerMethod(owner, name, desc)) {
        markerLocationMap.put(id, new BreakpointLocation(classVisitor.className.replace('/','.'), lineNr));
      }
      super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    public void visitInsn(int opcode) {
      switch (opcode) {
        case ICONST_0:
          id = 0;
          break;
        case ICONST_1:
          id = 1;
          break;
        case ICONST_2:
          id = 2;
          break;
        case ICONST_3:
          id = 3;
          break;
        case ICONST_4:
          id = 4;
          break;
        case ICONST_5:
          id = 5;
          break;
      }

      super.visitInsn(opcode);
    }
  }

  private boolean matchesMarkerMethod(String owner, String name, String desc) {
    return owner.equals(getMarkerClassName()) && name.equals(MARKER_METHOD_NAME) && desc.equals(MARKER_METHOD_DESC);
  }

  private class TestClassVisitor extends ClassVisitor {
    private TestMethodVisitor methodVisitor = new TestMethodVisitor();

    private @NonNull String className = "";

    private TestClassVisitor() {
      super(ASM6);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      className = name;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      return methodVisitor;
    }
  }

  private static String getMarkerClassName() {
    return MARKER_CLASS.getName().replace('.', '/');
  }

}
