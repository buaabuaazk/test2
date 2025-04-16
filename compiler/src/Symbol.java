import java.util.ArrayList;
public class Symbol {
    public int id;		// 当前单词对应的poi。
    public int tableId; 	// 当前单词所在的符号表编号。
    public String token; 	// 当前单词所对应的字符串。
    public int type; 		// 0 -> a, 1 -> a[], 2 -> a[][], -1 -> func
    public int con;		// 1 -> const, 0 -> var
    // 数组的维数：a[dim1][dim2]   dim1 dim2
    // 变量的值：val，寄存器：reg
    // func：
    public int funType;// 0 -> int, 1 -> void
    public int paramNum;	// 参数数量
    public ArrayList<Integer> paramTypeList;	//参数类型,0 -> a, 1 -> a[], 2 -> a[][],
    public ArrayList<String> paramNameList; //  这个list和上一个的长度必须一致
    public Symbol(int tableId, String token, int type, int con) {
        this.tableId = tableId;
        this.token = token;
        this.type = type;
        this.con = con;
    }
    public Symbol(int tableId, String token, int type, int funType,int paramNum,ArrayList paramTypeList){
        this.tableId = tableId;
        this.token = token;
        this.type = type;
        this.funType = funType;
        this.paramNum = paramNum;
        this.paramTypeList = paramTypeList;
    }
    public Symbol(int tableId, String token, int type, int funType,int paramNum,ArrayList paramTypeList, ArrayList paramNameList){
        this.tableId = tableId;
        this.token = token;
        this.type = type;
        this.funType = funType;
        this.paramNum = paramNum;
        this.paramTypeList = paramTypeList;
        this.paramNameList = paramNameList;
    }
    @Override
    public String toString(){
        if(this.type==-1){
            return "tableId=" + tableId + ", token=" + token + ", funType=" + funType + ", paramNum=" +paramNum;
        }
        else {
            return "tableId=" + tableId + ", token=" + token + ", type=" + type + ", con=" +con;
        }
    }
}