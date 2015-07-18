package me.zhihan.asm_util

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes

import java.io.InputStream
import java.io.FileInputStream

import groovy.transform.CompileStatic

class AVisitor extends AnnotationVisitor {

    public AVisitor() {
        super(Opcodes.ASM5)
    }

    @Override
    public void visit(String name, Object value) {
        println("Name is " + name)
        println(":" + value)
    }
}

class MVisitor extends MethodVisitor {
    public MVisitor() {
        super(Opcodes.ASM5)
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        println("Visiting annotation " + desc)
        return new AVisitor()
    }
}

class CVisitor extends ClassVisitor {
    public CVisitor() {
        super(Opcodes.ASM5)
    }

    @Override
    public MethodVisitor visitMethod(int access,
                                 String name,
                                 String desc,
                                 String signature,
                                 String[] exceptions) {
        return new MVisitor()
    }
}

class Scanner {
    static void main(String[] args) {
        String fileName = args[0]

        println(fileName)

        InputStream inS = new FileInputStream(fileName)
        ClassReader reader = new ClassReader(inS)
        ClassVisitor visitor = new CVisitor()
        reader.accept(visitor, ClassReader.SKIP_FRAMES)
    }
}