package com.notenoughmail.configjs.hacks;

import com.notenoughmail.configjs.ConfigJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.GeneratedClassLoader;
import dev.latvian.mods.rhino.classfile.ByteCode;
import dev.latvian.mods.rhino.classfile.ClassFileWriter;
import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * This class is a specially crafted wrapper around Rhino's {@link ClassFileWriter} that
 * creates an enum class with constants whose names are defined by the provided list of strings.<br><br>
 *
 * The generated enum classes are put into the same package as this class and contain only the
 * minimum amount of methods required to convince others that the class is, in fact, an enum.<br><br>
 *
 * Of note for these classes, they do <b><i>not</i></b> have a {@code this} reference.<br><br>
 *
 * This is, quite frankly, an abuse of Rhino, and should never have been done. Oh well!
 */
public class EnumWriter {

    private static int classNum = 0;
    private static final short valuesAccess = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC;
    private static final short classAccess = Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_ENUM;
    private static final short fieldAccess = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_ENUM;
    private static final String baseName = EnumWriter.class.getPackageName() + ".GeneratedConfigEnum";
    private static GeneratedClassLoader loader;

    public static <T extends Enum<T>> Class<T> getNewEnum(List<String> values) {
        // Setup basic values
        final String className = baseName + classNum++;
        final String classNameOp = className.replace('.', '/');
        final String classType = "L" + classNameOp + ";";
        final String arrayClassType = "[" + classType;
        final int numberOfValues = values.size();

        // Create class, add enum constants
        final ClassFileWriter cfw = new ClassFileWriter(className, Enum.class.getName(), null);
        cfw.setFlags(classAccess);
        for (String value : values) {
            cfw.addField(value, classType, fieldAccess);
        }

        // Create #values(), required for the JVM/Enum#valueOf to recognize it as an enum
        cfw.startMethod("values", "()" + arrayClassType, valuesAccess);
        if (numberOfValues < 6) {
            if (numberOfValues == 5) {
                cfw.add(ByteCode.ICONST_5);
            } else if (numberOfValues == 4) {
                cfw.add(ByteCode.ICONST_4);
            } else if (numberOfValues == 3) {
                cfw.add(ByteCode.ICONST_3);
            } else if (numberOfValues == 2) {
                cfw.add(ByteCode.ICONST_2);
            } else {
                throw new IllegalArgumentException("Enum should have 2 or more values!");
            }
        } else {
            cfw.add(ByteCode.BIPUSH, numberOfValues);
        }
        cfw.add(ByteCode.ANEWARRAY, classNameOp);
        for (int i = 0 ; i < numberOfValues ; i++) {
            cfw.add(ByteCode.DUP);
            addConstOp(cfw, i);
            cfw.add(Opcodes.GETSTATIC, classNameOp, values.get(i), classType);
            cfw.add(ByteCode.AASTORE);
        }
        cfw.add(ByteCode.ARETURN);
        cfw.stopMethod((short) 0);

        // Normal init
        cfw.startMethod("<init>", "(Ljava/lang/String;I)V", (short) Opcodes.ACC_PRIVATE);
        cfw.addALoad(0);
        cfw.addALoad(1);
        cfw.addILoad(2);
        cfw.addInvoke(ByteCode.INVOKESPECIAL, "java/lang/Enum", "<init>", "(Ljava/lang/String;I)V");
        cfw.add(ByteCode.RETURN);
        cfw.stopMethod((short) 3);

        // Class init
        cfw.startMethod("<clinit>", "()V", (short) Opcodes.ACC_STATIC);
        for (int i = 0 ; i < numberOfValues ; i++) {
            cfw.add(ByteCode.NEW, classNameOp);
            cfw.add(ByteCode.DUP);
            cfw.addLoadConstant(values.get(i));
            addConstOp(cfw, i);
            cfw.addInvoke(ByteCode.INVOKESPECIAL, classNameOp, "<init>", "(Ljava/lang/String;I)V");
            cfw.add(Opcodes.PUTSTATIC, classNameOp, values.get(i), classType);
        }
        cfw.add(ByteCode.RETURN);
        cfw.stopMethod((short) 0);

        // Add class to the class loader
        if (loader == null) {
            // Defer creation because there's no reason to make it if no ever makes an enum
            loader = Context.enter().createClassLoader(ConfigJS.class.getClassLoader());
        }
        final Class<?> clazz = loader.defineClass(className, cfw.toByteArray());
        loader.linkClass(clazz);

        return (Class<T>) clazz;
    }

    private static void addConstOp(ClassFileWriter cfw, int i) {
        switch (i) {
            case 0 -> cfw.add(ByteCode.ICONST_0);
            case 1 -> cfw.add(ByteCode.ICONST_1);
            case 2 -> cfw.add(ByteCode.ICONST_2);
            case 3 -> cfw.add(ByteCode.ICONST_3);
            case 4 -> cfw.add(ByteCode.ICONST_4);
            case 5 -> cfw.add(ByteCode.ICONST_5);
            default -> cfw.add(Opcodes.BIPUSH, i);
        }
    }
}
