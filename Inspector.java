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
          System.out.println(insideIndent + "INTERFACES: FROM: " + c.getName());
          Class[] interfaces = c.getInterfaces();
          if (interfaces.length > 0) {
            for (int i = 0; i < interfaces.length; i++) {
              inspectClass(interfaces[i], obj, recursive, insideIndent + "\t");
            }
          } else {
            System.out.println(insideIndent + "\tNONE");
          }
        }

        if (!c.isInterface()) {
          System.out.println(insideIndent + "CONSTRUCTORS: FROM: " + c.getName());
          Constructor[] constructors = c.getDeclaredConstructors();
          if (constructors.length > 0) {
            for (int i = 0; i < constructors.length; i++) {
              System.out.println(insideIndent + "\tCONSTRUCTOR NAME: " + constructors[i].getName() + " FROM: " + c.getName());
              Class[] parameters = constructors[i].getParameterTypes();
              if (parameters.length > 0) {
                System.out.print(insideIndent + "\tPARAMETERS: (");
                for (int j = 0; j < parameters.length; j++) {
                  System.out.print(parameters[j].getName());
                  if (j == (parameters.length - 1)) {
                    System.out.println(")");
                  } else {
                    System.out.print(", ");
                  }
                }
              } else {
                System.out.println(insideIndent + "\tPARAMETERS: NONE");
              }
              System.out.println(insideIndent + "\tMODIFIER: " + Modifier.toString(constructors[i].getModifiers()));
              System.out.println(insideIndent + "\t-----------------");

            }

          } else {
            System.out.println(insideIndent + "\tNONE");
          }

        }

        System.out.println(insideIndent + "METHODS: FROM: " + c.getName());
        Method[] methods = c.getDeclaredMethods();
        if (methods.length > 0) {
          for (int i = 0; i < methods.length; i++) {
            System.out.println(insideIndent + "\tMETHOD NAME: " + methods[i].getName() + " FROM " + c.getName());
            Class[] exceptions = methods[i].getExceptionTypes();
            if (exceptions.length > 0) {
              System.out.print(insideIndent + "\tEXCEPTIONS THROWN: ");
              for (int j = 0; j < exceptions.length; j++) {
                System.out.print(exceptions[j].getName());
                if (j == (exceptions.length - 1)) {
                  System.out.println("");
                } else {
                  System.out.print(", ");
                }
              }

            } else {
              System.out.println(insideIndent + "\tEXCEPTIONS THROWN: NONE");
            }

            Class[] parameters = methods[i].getParameterTypes();
            if (parameters.length > 0) {
              System.out.print(insideIndent + "\tPARAMETERS: (");
              for (int k = 0; k < parameters.length; k++) {
                System.out.print(parameters[k].getName());
                if (k == (parameters.length - 1)) {
                  System.out.println(")");
                } else {
                  System.out.print(", ");
                }
              }
            } else {
              System.out.println(insideIndent + "\tPARAMETERS: NONE");
            }

            System.out.println(insideIndent + "\tRETURN TYPE: " + methods[i].getReturnType().getName());
            System.out.println(insideIndent + "\tMODIFIER: " + Modifier.toString(methods[i].getModifiers()));

            System.out.println(insideIndent + "\t-----------------");
          }

        } else {
          System.out.println(insideIndent + "\tNONE");
        }

        System.out.println(insideIndent + "FIELDS: FROM: " + c.getName());
        Field[] fields = c.getDeclaredFields();
        if (fields.length > 0) {
          System.out.println(insideIndent + "RECURSIVE: " + recursive);

          for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            System.out.println(insideIndent + "\tFIELD NAME: " + fields[i].getName() + " FROM " + c.getName());
            System.out.println(insideIndent + "\tTYPE: " + fields[i].getType().getName());
            System.out.println(insideIndent + "\tMODIFIER: " + Modifier.toString(fields[i].getModifiers()));
            try {
              if (fields[i].get(obj) != null) {
                System.out.print(insideIndent + "\tVALUE: " + fields[i].get(obj));
                if (!(fields[i].getType().isPrimitive())) {
                  System.out.println("@" + Integer.toHexString(fields[i].get(obj).hashCode()));
                } else {
                  System.out.println("");
                }
              } else {
                System.out.println(insideIndent + "\tVALUE: " + fields[i].get(obj));
              }
              if (recursive && !(fields[i].getType().isPrimitive()) && (fields[i].get(obj) != null)) {
                inspectClass(fields[i].get(obj).getClass(), fields[i].get(obj), recursive, insideIndent + "\t");
              }
            }
            catch (IllegalAccessException e) {
              e.printStackTrace(System.out);
              System.out.println("Error: " + e.getMessage());
            }

            System.out.println(insideIndent + "\t-----------------");
          }

        } else {
          System.out.println(insideIndent + "\tNONE");
        }

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

}
