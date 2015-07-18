package me.zhihan.asm_util

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes

import org.apache.commons.cli.Options
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.HelpFormatter

import java.io.InputStream
import java.io.FileInputStream
import java.util.jar.JarFile
import java.util.zip.ZipEntry

import groovy.transform.CompileStatic

class AVisitor extends AnnotationVisitor {

    public AVisitor() {
        super(Opcodes.ASM5)
    }

    @Override
    public void visit(String name, Object value) {
        println(name + ": " + value)
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
        Options options = new Options()
        options.addOption("j", "jar", true, "Path to the jar file")

        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse(options, args);
        String className = cmd.getArgs()[0]
        String fileName = className.replace(".", "/") + ".class"

        InputStream inS = null
        if (cmd.hasOption("j")) {
            JarFile jarFile = new JarFile(cmd.getOptionValue("j"))
            ZipEntry entry = jarFile.getEntry(fileName)
            inS = jarFile.getInputStream(entry)
        } else {
            inS = new FileInputStream(fileName)
        }

        ClassReader reader = new ClassReader(inS)
        ClassVisitor visitor = new CVisitor()
        reader.accept(visitor, ClassReader.SKIP_FRAMES)
    }
}