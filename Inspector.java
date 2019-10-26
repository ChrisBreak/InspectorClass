//Cristhian Sotelo-Plaza
//CPSC 501 FALL 2019
//Assignment 2


import java.lang.reflect.*;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, "");
    }

    private void inspectClass(Class c, Object obj, boolean recursive, String indent) {

      if (c.isArray()) {
        inspectArray(c, obj, recursive, indent);
      } else {
        String classType = "CLASS";
        if (c.isInterface()) {
          classType = "INTERFACE";
        }

        System.out.println(indent + classType + ": " + c.getName());
        String insideIndent = indent + "\t";

        if (c.getSuperclass() != null) {
          System.out.println(insideIndent + "SUPER" + classType + ": " + c.getSuperclass().getName());
          inspectClass(c.getSuperclass(), obj, recursive, indent + "\t");
        } else {
          System.out.println(insideIndent + "SUPER" + classType + ": NONE" );
        }

        if (!c.isInterface()) {
          printInterfaces(c, obj, recursive, insideIndent);
          printConstructors(c, insideIndent);
        }

        printMethods(c, insideIndent);
        printFields(c, obj, recursive, insideIndent);

        System.out.println(indent + "======END OF " + classType + " " + c.getName() + "======");
      }
    }

    private void inspectArray(Class c, Object obj, boolean recursive, String indent) {
      System.out.println(indent + "CLASS: " + c.getName());
      String insideIndent = indent + "\t";

      for (int i = 0; i < Array.getLength(obj); i++) {
        if (Array.get(obj, i) != null) {
          System.out.println(insideIndent + "\tVALUE: " + Array.get(obj, i));

          if (recursive && !(obj.getClass().getComponentType().isPrimitive())) {
            inspectClass(Array.get(obj, i).getClass(), Array.get(obj, i), recursive, insideIndent + "\t");
          }
        } else {
          System.out.println(insideIndent + "\tVALUE: " + Array.get(obj, i));
        }
      }
    }

    private void printInterfaces(Class c, Object obj, boolean recursive, String indent) {
      System.out.println(indent + "INTERFACES: FROM: " + c.getName());
      Class[] interfaces = c.getInterfaces();
      if (interfaces.length > 0) {
        for (int i = 0; i < interfaces.length; i++) {
          inspectClass(interfaces[i], obj, recursive, indent + "\t");
        }
      } else {
        System.out.println(indent + "\tNONE");
      }
    }

    public void testPrintInterfaces(Object obj) {
      printInterfaces(obj.getClass(), obj, false, "");
    }

    private void printConstructors(Class c, String indent) {
      System.out.println(indent + "CONSTRUCTORS: FROM: " + c.getName());
      Constructor[] constructors = c.getDeclaredConstructors();
      if (constructors.length > 0) {
        for (int i = 0; i < constructors.length; i++) {
          System.out.println(indent + "\tCONSTRUCTOR NAME: " + constructors[i].getName() + " FROM: " + c.getName());
          Class[] parameters = constructors[i].getParameterTypes();
          printClassList(parameters, indent, "PARAMETERS");

          System.out.println(indent + "\tMODIFIER: " + Modifier.toString(constructors[i].getModifiers()));
          System.out.println(indent + "\t-----------------");
        }
      } else {
        System.out.println(indent + "\tNONE");
      }
    }

    public void testPrintConstructors(Class c) {
      printConstructors(c, "");
    }

    private void printMethods(Class c, String indent) {
      System.out.println(indent + "METHODS: FROM: " + c.getName());
      Method[] methods = c.getDeclaredMethods();
      if (methods.length > 0) {
        for (int i = 0; i < methods.length; i++) {
          System.out.println(indent + "\tMETHOD NAME: " + methods[i].getName() + " FROM " + c.getName());
          Class[] exceptions = methods[i].getExceptionTypes();
          printClassList(exceptions, indent, "EXCEPTIONS THROWN");

          Class[] parameters = methods[i].getParameterTypes();
          printClassList(parameters, indent, "PARAMETERS");

          System.out.println(indent + "\tRETURN TYPE: " + methods[i].getReturnType().getName());
          System.out.println(indent + "\tMODIFIER: " + Modifier.toString(methods[i].getModifiers()));
          System.out.println(indent + "\t-----------------");
        }
      } else {
        System.out.println(indent + "\tNONE");
      }
    }

    private void printFields(Class c, Object obj, boolean recursive, String indent) {
      System.out.println(indent + "FIELDS: FROM: " + c.getName());
      Field[] fields = c.getDeclaredFields();
      if (fields.length > 0) {
        System.out.println(indent + "RECURSIVE: " + recursive);

        for (int i = 0; i < fields.length; i++) {
          fields[i].setAccessible(true);
          System.out.println(indent + "\tFIELD NAME: " + fields[i].getName() + " FROM " + c.getName());
          System.out.println(indent + "\tTYPE: " + fields[i].getType().getName());
          System.out.println(indent + "\tMODIFIER: " + Modifier.toString(fields[i].getModifiers()));
          try {
            if (fields[i].get(obj) != null) {
              System.out.print(indent + "\tVALUE: " + fields[i].get(obj));
              if (!(fields[i].getType().isPrimitive())) {
                System.out.println("@" + Integer.toHexString(fields[i].get(obj).hashCode()));
              } else {
                System.out.println("");
              }
            } else {
              System.out.println(indent + "\tVALUE: " + fields[i].get(obj));
            }
            if (recursive && !(fields[i].getType().isPrimitive()) && (fields[i].get(obj) != null)) {
              inspectClass(fields[i].get(obj).getClass(), fields[i].get(obj), recursive, indent + "\t");
            }
          }
          catch (IllegalAccessException e) {
            e.printStackTrace(System.out);
            System.out.println("Error: " + e.getMessage());
          }
          System.out.println(indent + "\t-----------------");
        }
      } else {
        System.out.println(indent + "\tNONE");
      }
    }

    private void printClassList(Class[] parameters, String indent, String listName) {
      if (parameters.length > 0) {
        System.out.print(indent + "\t" + listName + ": (");
        for (int i = 0; i < parameters.length; i++) {
          System.out.print(parameters[i].getName());
          if (i == (parameters.length - 1)) {
            System.out.println(")");
          } else {
            System.out.print(", ");
          }
        }
      } else {
        System.out.println(indent + "\t" + listName + ": NONE");
      }
    }

    public void testPrintClassList(Class c) {
      Method[] methods = c.getDeclaredMethods();
      Class[] parameters = methods[2].getParameterTypes();
      printClassList(parameters, "", "PARAMETERS");
    }

}
