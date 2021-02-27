package dp.resolver.parse.assist;

import javassist.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*********************************
 Created by Fabian Oraze on 29.01.21
 *********************************/

public class AssistParser {

    /**
     * parses a jar file to {@link ClazzWithMethodsDto} objects
     *
     * @param path the full path to the Jar
     * @return a list with all classes from jar
     */
    public static List<ClazzWithMethodsDto> getJarClassList(String path) {
        System.out.println("Parsing jar: " + path);
        List<ClazzWithMethodsDto> list = new ArrayList<ClazzWithMethodsDto>();
        JarFile jarFile;
        try {
            jarFile = new JarFile(path);
            Enumeration<?> allEntries = jarFile.entries();
            while (allEntries.hasMoreElements()) {
                JarEntry entry = (JarEntry) allEntries.nextElement();
                String name = entry.getName();
                List<MethodInformation> methodInformations = new ArrayList<>();
                if (name.contains(".class") && !name.contains("$")) {
//                    list.add(name);

                    ClassPool pool = new ClassPool();
                    pool.insertClassPath(new File(jarFile.getName()).getAbsolutePath());

                    CtClass cc = pool.get(name.replace("/", ".").replace(".class", ""));

                    methodInformations = extractMethods(pool, name.replace("/", "."));

                    ClazzWithMethodsDto clazzWithMethodsDto = new ClazzWithMethodsDto();

                    /*if (cc.getAnnotation(Deprecated.class) != null) {
//                        System.out.println("DEPRECATED CLASS: "+cc.getName());
                        clazzWithMethodsDto.setDeprecated(true);
                        for (MethodInformation methodInformation : methodInformations) {
                            methodInformation.setDeprecated(true);
                        }
                    }*/

                    clazzWithMethodsDto.setClazzName(name);
                    clazzWithMethodsDto.setMethods(methodInformations);
//                    System.out.println(name);
//                    System.out.println(methodInformations);
                    list.add(clazzWithMethodsDto);
                } else {
//                    if (name.contains(".class") && name.contains("$")) { //also add inner clazzes
//                        list.add(name);
//                        methodInformations=extractMethods(jarFile.getName(), name.replace("/", "."));
//                        System.out.println(name);
//                        System.out.println(methodInformations);
//                    } else {
//                        //System.out.println("Ignored: " + name);
//                    }
                }
            }

            jarFile.close();
        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(list);
        return list;
    }

    private static List<MethodInformation> extractMethods(ClassPool pool, String clazzName) throws Exception {
        List<MethodInformation> methodInformations = new ArrayList<>();

        try {
//            ClassPool pool = ClassPool.getDefault();
//            ClassPool pool = new ClassPool();
//
//            pool.insertClassPath(new File(jarFile).getAbsolutePath());

            CtClass cc = pool.get(clazzName.replace(".class", ""));
            //System.out.println("Parsing class: " + clazzName);
            CtMethod[] methods = cc.getMethods();
            /*if (cc.getAnnotation(Deprecated.class) != null) {
                System.out.println("DEPRECATED CLASS: " + cc.getName());
            }*/
            for (CtMethod method : methods) {
                try {
                    if (!isMethodFromObjectClazz(method)) {
                        MethodInformation mi = extractMethodInformation(method);
//                        System.out.println("    " + mi);
                        methodInformations.add(mi);
                    }
                } catch (Exception e) {
                    //ignore and use next method
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodInformations;
    }

    private static MethodInformation extractMethodInformation(CtMethod method) {
        MethodInformation mi = new MethodInformation();
        boolean errorFlag = false;
        try {
            Object depcrecated = method.getAnnotation(Deprecated.class);
//            System.out.println(method.getName()+" DEPRECATED: "+depcrecated);
            mi.setDeprecated(depcrecated != null);
        } catch (Exception e) {
            errorFlag = true;
        }
        String retType = "";
        try {
            retType = method.getReturnType().getName();
            mi.setRetType(retType);
        } catch (Exception e) {
            errorFlag = true;
        }
        String visibility = "";
        try {
            /*visibility = getVisibility(method);
            mi.setVisibility(visibility);*/
        } catch (Exception e) {
            errorFlag = true;
        }
        String abstractString = "";
        try {
            abstractString = Modifier.isAbstract(method.getModifiers()) ? "abstract" : "";
            mi.setAbstract(Modifier.isAbstract(method.getModifiers()));
        } catch (Exception e) {
            errorFlag = true;
        }
        String finalString = "";
        try {
            finalString = Modifier.isFinal(method.getModifiers()) ? "final" : "";
            mi.setFinal(Modifier.isFinal(method.getModifiers()));
        } catch (Exception e) {
            errorFlag = true;
        }
        String synchronizedString = "";
        try {
            synchronizedString = Modifier.isSynchronized(method.getModifiers()) ? "synchronized" : "";
            mi.setSynchronized(Modifier.isSynchronized(method.getModifiers()));
        } catch (Exception e) {
            errorFlag = true;
        }
        String staticString = "";
        try {
            staticString = Modifier.isStatic(method.getModifiers()) ? "static" : "";
            mi.setStatic(Modifier.isStatic(method.getModifiers()));
        } catch (Exception e) {
            errorFlag = true;
        }
        try {
            mi.setNumberOfParams(Long.valueOf(method.getParameterTypes().length));
            if (mi.getNumberOfParams() == null) mi.setNumberOfParams(Long.valueOf(method.getMethodInfo().getAttributes().size()));
        } catch (Exception e) {
            errorFlag = true;
        }
        String paramString = "UNKNOWN";
        try {
            CtClass[] parameterTypes = method.getParameterTypes();
            List<String> params = new ArrayList<>();
            for (CtClass parameterType : parameterTypes) {
                params.add(parameterType.getName());
            }
            paramString = String.join(",", params);
        } catch (Exception e) {
            errorFlag = true;
        }
        String methodString = "";
        methodString += visibility + " " + staticString + " " + abstractString + " " + finalString + " " + synchronizedString + " " + retType + " " + method.getName();
        methodString += "(";
        methodString += paramString;
        methodString += ")";
        methodString = methodString.replaceAll("\\s{2,}", " ");
        mi.setMethodHeader(methodString);
        mi.setMethodName(method.getName());
        try {
            mi.setHasPrimitiveReturnType(method.getReturnType() instanceof CtPrimitiveType && !"void".equals(method.getReturnType().getName()));
        } catch (Exception e) {
            errorFlag = true;
        }
        try {
            mi.setHasVoidReturnType(method.getReturnType() instanceof CtPrimitiveType && "void".equals(method.getReturnType().getName()));
        } catch (Exception e) {
            errorFlag = true;
        }
        mi.setInformationComplete(!errorFlag);
        return mi;
    }

    private static boolean isMethodFromObjectClazz(CtMethod method) throws Exception {
        List<String> objectMethods = Arrays.asList("public boolean equals(java.lang.Object)",
                "protected void finalize()",
                "public java.lang.String toString()",
                "public final java.lang.Class getClass()",
                "public final void notifyAll()",
                "public int hashCode()",
                "public final void wait()",
                "public final void notify()",
                "public final void wait(long)",
                "public final void wait(long,int)",
                "protected java.lang.Object clone()");
        MethodInformation methodInformation = extractMethodInformation(method);
        return objectMethods.contains(methodInformation.getMethodHeader());
    }


}
