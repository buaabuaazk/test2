import java.util.ArrayList;
public class FuncFParam {
    //两用类，可以表示函数某个参数的信息，也可以表示函数形参表的信息
    public int p;
    public int type;// 0 -> a, 1 -> a[], 2 -> a[][], -1 -> func
    public String name;
    public ArrayList<Integer> FuncFParams = new ArrayList<>();
    public ArrayList<String> FuncFParamName = new ArrayList<>();
}
