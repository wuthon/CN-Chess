package XiangQi;

import java.awt.Image;
import java.io.Serializable;
/**
 * @author wuwang
 */
public class Piece {
	Image image,SImage;//image是未选中的样式 SImage是选中后的样式
	boolean color=true;//标记颜色 true红 false黑
	String attr;//棋子角色属性	
	public Piece(Image i,Image s) {
		image=i;
		SImage=s;
	}
	public Piece(Image i,Image s,String a) {
		this(i,s);
		attr=a;
	}
	public Piece(Image i,Image s,String a,boolean c) {
		this(i,s,a);
		color=c;
	}
}
//用于记录每一次移动
class Step implements Serializable{
	private static final long serialVersionUID = 1L;
	
	Pair first,second;//由first位置移动到second位置
	boolean isCovered=false;//是否发生覆盖 说白了就是吃子
	public Step(Pair arg0,Pair arg1) {
		first=arg0;
		second=arg1;
	}
	public Step(Pair arg0,Pair arg1,boolean arg2) {
		this(arg0,arg1);
		isCovered=arg2;
	}
	public Pair getFirst() {
		return first;
	}
	public Pair getSecond() {
		return second;
	}
	public boolean isCovered() {
		return isCovered;
	}
}
 
class Pair implements Serializable{
	private static final long serialVersionUID = 1L;
	int x,y;
	public Pair(int argx,int argy) {
		x=argx;
		y=argy;
	}
	public boolean equals(int argx,int argy) {
		return x==argx&&y==argy;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null||obj.getClass()!=Pair.class) 
			return false;
		Pair p=(Pair)obj;
		if(p.x==x&&p.y==y)
			return true;
		else 
			return false;
	}
}
