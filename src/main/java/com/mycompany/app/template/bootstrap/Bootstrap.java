package com.mycompany.app.template.bootstrap;

import java.io.File;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

public class Bootstrap {

    ClassLoader cl;
    Class<?> mainClass;

    public Bootstrap() {
    }

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start(args);
    }

    public void start(String[] args) throws Exception {
        this.cl = this.loadLib();
        if (cl != null) {
            final String classToExec = "com.mycompany.app.template.AppStart";
            final String methodExec = "main";
            final String[] argsExec = args;
            //-
            final String className = classToExec;
            final Class<?>[] classes = new Class[]{argsExec.getClass()};
            final Object[] methodArgs = new Object[]{argsExec};
            mainClass = cl.loadClass(className);
            final Method method = mainClass.getMethod(methodExec, classes);
            Runnable execer = () -> {
                try {
                    method.invoke(null, methodArgs);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            };
            Thread bootstrapper = new Thread(execer, "main");
            bootstrapper.setContextClassLoader(cl);
            bootstrapper.start();
        }
    }

    public ClassLoader loadLib() {
        File parent = LibLoader.findBootstrapHome();
        if (parent != null) {
            String lib_dir = parent.getPath() + File.pathSeparator
                    + parent.getParentFile().getPath() + File.separator + "bundles";
            try {
                ClassLoader _cl = LibLoader.loadClasses(lib_dir, false);
                return _cl;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
