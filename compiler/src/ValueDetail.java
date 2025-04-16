/**
 * @author zwzk8
 */
public class ValueDetail {
    public int dimension =0;
    public int d1=0;
    public int d2=0;
    public String addressType ="i32";
    public String intVal="";
    public String [] d1Value = null;
    public String [][] d2Value = null;
    public void setD1(int d1){
        this.d1=d1;
        if(d1==0){d1Value = new String[1024];}
        else{d1Value = new String[d1];}
    }

    public void setD2(int d2){
        this.d2=d2;
        if(this.d1==0){d2Value = new String[1024][d2];}
        else{d2Value = new String[this.d1][d2];}
    }

    public void setD1Value(String[] d1Value){
        this.d1Value=d1Value;
    }

    public void setD2Value(String[][] d2Value){
        this.d2Value=d2Value;
    }

    public String[] getD1Value(){
        return d1Value;
    }
    public String[][] getD2Value(){
        return d2Value;
    }
    public void setIntVal(String intVal){
        this.intVal=intVal;
    }
    public String getIntVal(){
        return intVal;
    }
    public void setAddressType(String addType){
        addressType =addType;
    }

    public void setDimension(int dimension){
        this.dimension = dimension;
    }

    public void setDim(int i) {
        this.dimension = i;
    }

}
