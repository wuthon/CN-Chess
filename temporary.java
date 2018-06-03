	void initPic() {//初始化一些需要的资源 如image.. 可以调用线程优化
		//init location
		for(int i=0;i<10;++i) {
			for(int j=0;j<9;++j) 
				location[i][j]=new Ellipse2D.Double
				(beginX+j*interval,beginY+i*interval,chessSize,chessSize);
		}
		//init 	Image
		for(int i=0;i<9;i+=2) {//卒
			allPiece[6][i]=new Piece(getImage("/home/wuwang/Chess/DELICATE/RP.GIF"),
					getImage("/home/wuwang/Chess/DELICATE/RPS.GIF"),"zu",true);
			allPiece[3][i]=new Piece(getImage("/home/wuwang/Chess/DELICATE/BP.GIF"),
					getImage("/home/wuwang/Chess/DELICATE/BPS.GIF"),"zu",false);		
		}
		for(int i=1;i<9;i+=6) {//炮
			allPiece[7][i]=new Piece(getImage("/home/wuwang/Chess/DELICATE/RC.GIF"),
					getImage("/home/wuwang/Chess/DELICATE/RCS.GIF"),"pao",true);
			allPiece[2][i]=new Piece(getImage("/home/wuwang/Chess/DELICATE/BC.GIF"),
					getImage("/home/wuwang/Chess/DELICATE/BCS.GIF"),"pao",false);		
		}
		//建立map 保存name-image
		TreeMap<String,Image> redN=new TreeMap<>(),redS=new TreeMap<>(),
							  blackN=new TreeMap<>(),blackS=new TreeMap<>();	
		//根目录下四个子目录 子目录1,2,3,4保存不同类型的Image
		File file=new File("/home/wuwang/Chess/DELICATE/Chess");		
		File[] dir=file.listFiles();
		for(int i=0;i<4;++i) {
			File[] files=dir[i].listFiles();
			String name=dir[i].getName();
			for(int j=0;j<files.length;++j) {
				int p=files[j].getName().lastIndexOf('.');//文件名截取名字为attr
				switch(name){
				case "1":
					redS.put(files[j].getName().substring(0, p),getImage(files[j].getPath()));
					break;
				case "2":				
					blackN.put(files[j].getName().substring(0, p),getImage(files[j].getPath()));
					break;
				case "3":
					blackS.put(files[j].getName().substring(0, p),getImage(files[j].getPath()));
					break;
				case "4":
					redN.put(files[j].getName().substring(0, p),getImage(files[j].getPath()));
					break;					
				}			
			}
		}					
		String[] attrs=new String[] {
			"ju","ma","xiang","shi","jiang"
		};
		for(int i=0;i<5;++i) {
			allPiece[0][i]=new Piece(blackN.get(attrs[i]),blackS.get(attrs[i]),attrs[i],false);
			allPiece[9][i]=new Piece(redN.get(attrs[i]),redS.get(attrs[i]),attrs[i],true);		
		}
		for(int i=0;i<4;++i) {
			allPiece[0][8-i]=allPiece[0][i];
			allPiece[9][8-i]=allPiece[9][i]	;			
		}
	}
