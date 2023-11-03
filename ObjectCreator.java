import java.util.Stack;

public class ObjectCreator {
    TextUI tex;
    Object object;

    public Object createObject(){
        this.tex = new TextUI();
        whichObject();
        return object;
    }

    public void whichObject(){
        tex.showObjOptions();
        int objChoice = tex.chooseIntOption(1, 5);
        tex.showCreationOptions();
        int valChoice = tex.chooseIntOption(1, 2);
        boolean custom = false;
        if(valChoice == 2){
            custom = true;
        }

        if (objChoice == 1){
            createPrimOb(custom);
        }
        else if (objChoice == 2){
            createRefOb(custom);
        }
        else if (objChoice == 3){
            createPrimArray(custom);
        }
        else if (objChoice == 4){
            createObArray();
        }
        else if (objChoice == 5){
            createCollection();
        }
    }

    public void createPrimOb(boolean custom){
        if (custom){
            while(true){
                tex.showPrimObVars();
                String input = tex.chooseStringOption().trim();
                String[] obVars = input.split(" ");
                if(obVars.length != 3){
                    continue;
                }
                try {
                    int x = Integer.parseInt(obVars[0]);
                    double y = Double.parseDouble(obVars[1]);
                    boolean z = parseBool(obVars[2]);
                    object = new primOb(x, y, z);
                    break;
                } catch (Exception e) {
                    System.out.println("There was an issue parsing your input.");
                    continue;
                }
            }
        }
        else{
            object = new primOb();
        }
    }

    public primOb createPrimOb(){
        while(true){
            tex.showPrimObVars();
            String input = tex.chooseStringOption().trim();
            String[] obVars = input.split(" ");
            if(obVars.length != 3){
                continue;
            }
            try {
                int x = Integer.parseInt(obVars[0]);
                double y = Double.parseDouble(obVars[1]);
                boolean z = parseBool(obVars[2]);
                return new primOb(x, y, z);
            } catch (Exception e) {
                System.out.println("There was an issue parsing your input.");
                continue;
            }
        }
    }

    public void createRefOb(boolean custom){
        if (custom){
            while(true){
                tex.showRefObVars();
                primOb prim1 = createPrimOb();
                primOb prim2 = createPrimOb();
                object = new refOb2(prim1, prim2);
                break;
            }
        }
        else{
            object = new refOb2(new primOb(4, 50.5, false));
        }
    }

    public void createPrimArray(boolean custom){
        while(true){
            try {
                tex.showPrimArrayOptions();
                int choice = tex.chooseIntOption(1, 5);
                if (custom){
                    tex.primArrayPrompt();
                    String input = tex.chooseStringOption();
                    if(choice == 1){
                        object = new boolArrayOb(input);
                    }
                    else if(choice == 2){
                        object = new IntArrayOb(input);
                    }
                    else if(choice == 3){
                        object = new shortArrayOb(input);
                    }
                    else if(choice == 4){
                        object = new DoubleArrayOb(input);
                    }
                    else if(choice == 5){
                        object = new CharArrayOb(input);
                    }
                }
                else{
                    if(choice == 1){
                        object = new boolArrayOb();
                    }
                    else if(choice == 2){
                        object = new IntArrayOb();
                    }
                    else if(choice == 3){
                        object = new shortArrayOb();
                    }
                    else if(choice == 4){
                        object = new DoubleArrayOb();
                    }
                    else if(choice == 5){
                        object = new CharArrayOb();
                    }
                }
                break;
            } catch (Exception e) {
                System.out.println("There was a problem processing your input, try again");
            }
        }
    }

    public void createObArray(){
        object = new ObjArrayOb();
    }

    public void createCollection(){
        object = new CollectionField(1);
    }

    //funfact: Boolean.parseBoolean does not throw an exception if the input is neither true nor false, it just defaults to false.
    public boolean parseBool(String str) throws Exception{
        if(str.toLowerCase().equals("true")){
            return true;
        }
        else if(str.toLowerCase().equals("false")){
            return false;
        }
        else {
            throw new Exception();
        }
    }
}

class primOb{
    int num;
    double dub;
    boolean bool;

    public primOb(){
        num=1;
        dub=1.0;
        bool=true;
    }

    public primOb(int x, double y, boolean z){
        num=x;
        dub=y;
        bool=z;
    }

    public int getNum(){
        return num;
    }

    public void setNum(int x){
        num = x;
    }

    public double getDub(){
        return dub;
    }

    public void setDub(double x){
        dub = x;
    }

    public boolean getBool(){
        return bool;
    }

    public void setBool(boolean x){
        bool=x;
    }
}

class refOb1{
    refOb2 miniMe;
    primOb prim;

    public refOb1(){
        miniMe = null;
        prim = new primOb();
    }

    public refOb1(refOb2 clone){
        miniMe = clone;
        prim = new primOb(); 
    }

    public refOb1(refOb2 clone, primOb custom){
        miniMe = clone;
        prim = custom;
    }

    public refOb1(primOb custom){
        miniMe = null;
        prim = custom;
    }
}

class refOb2 extends refOb1{
    refOb1 drEvil;
    primOb prim;

    public refOb2(){
        drEvil = new refOb1(this);
        prim = new primOb();
    }

    public refOb2(refOb1 dad){
        drEvil = dad;
        prim = new primOb();
    }

    public refOb2(refOb1 dad, primOb custom){
        drEvil = dad;
        prim = custom;
    }

    public refOb2(primOb custom){
        drEvil = new refOb1(this);
        prim = custom;
    }
    
    public refOb2(primOb prim1, primOb prim2){
        prim = prim2;
        drEvil = new refOb1(this, prim1);
    }
}

class boolArrayOb{
    boolean[] boolarray;

    boolArrayOb(){
        boolarray = new boolean[] {true, true, true, true, false, false, false, true, false, false, false, true};
    }

    boolArrayOb(String str){
        String[] strs = str.trim().split(" ");
        boolarray = new boolean[strs.length];
        for(int i=0; i<strs.length; i++){
            boolarray[i] = Boolean.parseBoolean(strs[i]);
        }
    }
}
class IntArrayOb{
    int[] intarray;

    IntArrayOb(){
        intarray = new int[] {8, 6, 7, 5, 3, 0, 9};
    }

    IntArrayOb(String str){
        String[] strs = str.trim().split(" ");
        intarray = new int[strs.length];
        for(int i=0; i<strs.length; i++){
            intarray[i] = Integer.parseInt(strs[i]);
        }
    }
}
class shortArrayOb{
    Short[] shortarray;

    shortArrayOb(){
        shortarray = new Short[] {8, 6, 7, 5, 3, 0, 9};
    }

    shortArrayOb(String str){
        String[] strs = str.trim().split(" ");
        shortarray = new Short[strs.length];
        for(int i=0; i<strs.length; i++){
            shortarray[i] = Short.parseShort(strs[i]);
        }
    }
}
class DoubleArrayOb{
    Double[] Doublearray;

    DoubleArrayOb(){
        Doublearray = new Double[] {8.1, 6.2, 7.3, 5.4, 3.5, 0.6, 9.7};
    }

    DoubleArrayOb(String str){
        String[] strs = str.trim().split(" ");
        Doublearray = new Double[strs.length];
        for(int i=0; i<strs.length; i++){
            Doublearray[i] = Double.parseDouble(strs[i]);
        }
    }
}
class CharArrayOb{
    char[] Chararray;

    CharArrayOb(){
        Chararray = new char[] {'a', 'e', 'i', 'o', 'u'};
    }

    CharArrayOb(String str){
        str.trim().replaceAll(" ", "");
        Chararray = str.toCharArray();
    }
}
class ObjArrayOb{
    Object[] objArray;

    ObjArrayOb(){
        refOb2 o2 = new refOb2();
        refOb1 o1 = o2.drEvil;
        objArray = new Object[] {o2, o1, new primOb(), new primOb(3, 67.8, false), new CharArrayOb()};
    }
}
class CollectionField{
    Stack<Object> stack;
    CollectionField(int useless){
        stack = new Stack<Object>();
        refOb2 o2 = new refOb2();
        refOb1 o1 = o2.drEvil;
        stack.push(o1);
        stack.push(o2);
        stack.push(new primOb());
        stack.push(new primOb(3, 67.8, false));
        stack.push(new CharArrayOb());
    }
    CollectionField(){
        stack = new Stack<Object>();
        refOb2 o2 = new refOb2();
        refOb1 o1 = o2.drEvil;
        stack.push(o1);
        stack.push(o2);
    }
}