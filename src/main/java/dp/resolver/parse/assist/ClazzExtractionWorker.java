package dp.resolver.parse.assist;

import dp.resolver.parse.entity.MessagingClazz;
import dp.resolver.parse.entity.MessagingJar;
import dp.resolver.parse.entity.MessagingMethod;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClazzExtractionWorker {

    private final MessagingJar messagingJar;

    public ClazzExtractionWorker(MessagingJar messagingJar) {
        this.messagingJar = messagingJar;
    }

    public void analyzeJarWithBCEL(File jarFile) throws IOException {
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
                MessagingClazz messagingClazz = new MessagingClazz();
                ClassParser parser = new ClassParser(jarFile.getAbsolutePath(), entry.getName());
                JavaClass javaClass = null;
                try {
                    javaClass = parser.parse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                messagingClazz.setClazzName(javaClass.getClassName().split("\\.")[javaClass.getClassName().split("\\.").length - 1]);
                messagingClazz.setFullQualifiedName(javaClass.getClassName());
                messagingClazz.setPackageName(javaClass.getPackageName());
                messagingClazz.setDeprecated(false);
                if (javaClass.getAnnotationEntries().length > 0) {
                    for (AnnotationEntry annotationEntry : javaClass.getAnnotationEntries()) {
                        if (annotationEntry.getAnnotationType().equals("L" + Deprecated.class.getTypeName().replace(".", "/") + ";")) {
                            messagingClazz.setDeprecated(true);
                        }
                    }
                }

                Method[] methods = javaClass.getMethods();
                for (Method method : methods) {
                    MessagingMethod messagingMethod = new MessagingMethod();
                    messagingMethod.setDeprecated(false);
                    if (method.getAnnotationEntries().length > 0) {
                        for (AnnotationEntry annotationEntry : method.getAnnotationEntries()) {
                            if (annotationEntry.getAnnotationType().equals("L" + Deprecated.class.getTypeName().replace(".", "/") + ";")) {
                                messagingMethod.setDeprecated(true);
                            }
                        }
                    }
                    if (method.getName().equals("<init>")) {
                        messagingMethod.setMethodName(javaClass.getSourceFileName().replace(".java",""));
                    } else {
                        messagingMethod.setMethodName(method.getName());
                    }
                    messagingMethod.setAbstract(method.isAbstract());
                    messagingMethod.setFinal(method.isFinal());
                    messagingMethod.setStatic(method.isStatic());
                    messagingMethod.setVisibility(getVisibility(method));
                    messagingMethod.setSynchronized(method.isSynchronized());
                    messagingMethod.setRetType(method.getReturnType().toString());
                    messagingMethod.setHasPrimitiveReturnType(method.getReturnType() instanceof org.apache.bcel.generic.BasicType);
                    messagingMethod.setHasVoidReturnType(method.getReturnType() instanceof org.apache.bcel.generic.BasicType && method.getReturnType().toString().equals("void"));
                    messagingMethod.setNumberOfParams(1l * method.getArgumentTypes().length);
                    messagingMethod.setMethodHeader(constructHeader(method));

                    messagingClazz.getMethods().add(messagingMethod);
                }

                messagingJar.getClazzes().add(messagingClazz);
            }
        }
    }

    private String constructHeader(Method method) {
        String[] paramsAsStrings = Arrays.stream(method.getArgumentTypes()).parallel().map(x -> x.toString()).collect(Collectors.toList()).toArray(new String[]{});
        String paramString = "";
        for (int i = 0; i < paramsAsStrings.length; i++) {
            String p = paramsAsStrings[i];
            paramString += p;
            if (i + 1 < paramsAsStrings.length) {
                paramString += ",";
            }
        }

        return getVisibility(method) + " " +
                method.getReturnType() + " " +
                method.getName() + "(" +
                paramString
                + ")"
                .trim().replaceAll(" +", " "); //replace multiple spaces with one in case this happens
    }

    private String getVisibility(Method method) {
        if (method.isPublic()) {
            return "public";
        }
        if (method.isProtected()) {
            return "protected";
        }
        if (method.isPrivate()) {
            return "private";
        }
        return ""; //default it is default ;)
    }

}
