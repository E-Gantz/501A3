import java.util.ArrayList;

public class ObjectCreator {
    TextUI tex;
    ArrayList<Object> objects;
    public static void main(String[] args) {
        ObjectCreator objC = new ObjectCreator();
        objC.createObjects();
    }

    public void createObjects(){
        this.tex = new TextUI();
        this.objects = new ArrayList<Object>();
        whichObject();
    }

    public void whichObject(){
        while(true){
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
                //
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

            tex.goAgain();
            int again = tex.chooseIntOption(1, 2);
            if (again == 2){
                break;
            }
        }
    }

    public void createPrimOb(boolean custom){
        if (custom){
            while(true){
                tex.showPrimObVars();
                String input = tex.chooseStringOption().trim();
                System.out.println("input is " + input);
                String[] obVars = input.split(" ");
                System.out.println("length is " + obVars.length);
                if(obVars.length != 3){
                    continue;
                }
                try {
                    int x = Integer.parseInt(obVars[0]);
                    double y = Double.parseDouble(obVars[1]);
                    boolean z = parseBool(obVars[2]);
                    objects.add(new primOb(x, y, z));
                    break;
                } catch (Exception e) {
                    System.out.println("There was an issue parsing your input.");
                    continue;
                }
            }
        }
        else{
            objects.add(new primOb());
        }
    }

    /*public void create//(boolean custom){
        if (custom){
            //
        }
        else{
            //objects.add(new //);
        }
    }*/

    /*public void create//(boolean custom){
        if (custom){
            //
        }
        else{
            //objects.add(new //);
        }
    }*/

    /*public void create//(boolean custom){
        if (custom){
            //
        }
        else{
            //objects.add(new //);
        }
    }*/

    /*public void create//(boolean custom){
        if (custom){
            //
        }
        else{
            //objects.add(new //);
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