package XiangQi;

import java.awt.Image;
/**
 * 
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
class Pair{
	int x,y;
	public Pair(int argx,int argy) {
		x=argx;
		y=argy;
	}
	@Override
	public boolean equals(Object obj) {
		return false;
		
	}
}