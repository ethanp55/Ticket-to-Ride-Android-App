package ateam.tickettoride.common;

import java.io.Serializable;
import java.lang.reflect.Method;


import ateam.tickettoride.common.responses.Response;

/**
 * Class representing a command to be executed elsewhere on a singleton object.
 */

public class Command implements Serializable {
    //The fully-qualified name of the class the method will be called on.
    private String callClassName;
    //The name of the method to be called.
    private String callMethodName;
    //The fully-qualified names of the types of the method parameters.
    private String[] paramTypes;
    //The fully-qualified names of the types of the arguments given.
    private String[] argTypes;
    //The arguments, stored in JSON to avoid being messed up by GSON.
    private String[] argumentJSON;

    public Command(String theClassName, String theMethodName, String[] theParamTypes, Object[] theArgs){
        callClassName = theClassName;
        callMethodName = theMethodName;
        paramTypes = theParamTypes;
        argTypes = new String[theArgs.length];
        argumentJSON = new String[theArgs.length];
        fillJSON(theArgs);
    }

    /**
     * Stores the array of objects for the arguments in JSON format,
     * and stores their class names in a separate array.
     * @param args  The array of arguments for the method call.
     */
    private void fillJSON(Object[] args){
        for(int i = 0; i<args.length; i++){
            argTypes[i] = args[i].getClass().getName();
            argumentJSON[i] = Jsonifier.toJson(args[i]);
        }
    }

    /**
     * Retrieves the stored arguments from JSON format to Objects.
     * @return  The array of Objects that are to be used for the method call.
     */
    private Object[] fromJSON(){
        Object[] objs = new Object[argumentJSON.length];
        for(int i = 0; i<argumentJSON.length; i++){
            try {
                Class klass = Class.forName(argTypes[i]);
                objs[i] = Jsonifier.fromJson(argumentJSON[i], klass);
            }catch(ClassNotFoundException e){
                System.out.println("Class not found in fromJSON in Command class.");
                System.out.println(e.getMessage());
                objs[i] = null;
            }
        }
        return objs;
    }

    /**
     * Executes the method on the given class.
     * @return  The object returned by the method executed.
     */
    public Object execute(){
        String getInstance = "getInstance";
        //Response response = null;
        try {
            Class klass = Class.forName(callClassName);

            Class[] paramClasses = new Class[paramTypes.length];

            for(int i = 0; i<paramTypes.length; i++){
                paramClasses[i] = Class.forName(paramTypes[i]);
            }

            Method grabInstance = klass.getDeclaredMethod(getInstance);

            //get the ServerFacade
            Object singletonToCall = grabInstance.invoke(null);

            Method method = klass.getDeclaredMethod(callMethodName, paramClasses);

            return method.invoke(singletonToCall, fromJSON());

        }catch(Exception e){

            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Someone doesn't know how to use reflection.");
        }
        return null;
    }
}
