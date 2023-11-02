
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
            //
        }
        else if (objChoice == 4){
            //
        }
        else if (objChoice == 5){
            //
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
            }
        }
        else{
            object = new refOb2(new primOb(4, 50.5, false));
        }
    }

    /*public void create//(boolean custom){
        if (custom){
            //
        }
        else{
            //object = new //;
        }
    }*/

    /*public void create//(boolean custom){
        if (custom){
            //
        }
        else{
            //object = new //;
        }
    }*/

    /*public void create//(boolean custom){
        if (custom){
            //
        }
        else{
            //object = new //;
        }
    }*/

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