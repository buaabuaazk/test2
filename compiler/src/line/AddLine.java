package line;

//<result> = add <ty> <op1>, <op2>
//               type
public class AddLine extends Line {
    String result;
    String type;//i32  i1
    //op可能是数字，也可能是寄存器
    String op1;
    String op2;

    public AddLine(int level, String content) {
        super(level, content);
    }

    @Override
    public String toString() {
        return result + " = add "+ type +" "+ op1 + ", "+ op2;
    }
}
