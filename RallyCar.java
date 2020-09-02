import com.ibm.rally.*;

import com.ibm.rally.ICar;


public class RallyCar extends Car {
	
	IObject checkPoints[];//チェックポイント配列
	IObject gasSimport com.ibm.rally.*;
	import com.ibm.rally.ICar;

	public class RallyCar extends Car {
		//a
		IObject checkPoints[];
		IObject gasStations[];
		IObject enemys[];
		ICar opponents[];
		
		int nextNo,prevNo;
		int waitBack=0;
		boolean gasFlag=false;
		boolean startFlag=false;
		
		public String getName() {
			return "Banana";
		}
		
		public String getSchoolName() {
			return "D-02*";
		}

		public byte getColor() {
			return CAR_YELLOW;
		}
		
		public void initialize() {
			checkPoints=getCheckpoints();
			gasStations=getFuelDepots();
			enemys=getOpponents();
			startFlag=true;
			prevNo=getPreviousCheckpoint();
			nextNo=getNearIObject(checkPoints);
			if(getDistanceTo(checkPoints[nextNo])<=80){
				nextNo++;
			}
		}

		public void move(int lastMoveTime, boolean hitWall, ICar collidedWithCar, ICar hitBySpareTire) {
			
			//至近距離・前方の他者を攻撃
			opponents=getOpponents();
			int oppoNo=getNearIObject(opponents);
			if(getDistanceTo(opponents[oppoNo])<80&&teamCheck(opponents[oppoNo])==true){
				attack(opponents[oppoNo]);
			}
			
			//他者と衝突
			if(collidedWithCar!=null&&waitBack==0&&gasFlag==false){
				waitBack=8;

			}
			
			if(getDistanceTo(opponents[oppoNo])<20){
				waitBack=8;
			}
			
			//壁と衝突
			if(hitWall==true&&waitBack==0&&gasFlag==false){
				
			}
			
			//ガソリン補給
			if(getFuel()<30&&waitBack==0&&gasFlag==false&&getClockTicks()<400){
				prevNo=getPreviousCheckpoint();
				gasFlag=true;
			}
			
			//前進・後退
			if(waitBack>0){
				//後退処理
				waitBack--;
				setSteeringSetting(-5);
				setThrottle(MIN_THROTTLE);
				
			}else if(gasFlag==true){
				//給油処理
				gotoGas(getNearIObject(gasStations));
				
			}else{
				//前進処理
				int targetNo;
				if(startFlag==true){
					//最寄りのチェックポイントに向かう
					targetNo=nextNo;
					if(prevNo!=getPreviousCheckpoint()){
						startFlag=false;
					}
				}else{
					//次のチェックポイントに向かう
					targetNo=getPreviousCheckpoint()+1;
				}
				
				//最後のチェックポイントを通過したら、最初に戻る
				if(targetNo>=checkPoints.length){
					targetNo=0;
				}
				drive(checkPoints[targetNo]);
			}
			
			//プロテクトモード
			int enemyNo=getNearIObject(enemys);
			double distance=getDistanceTo(enemys[enemyNo]);
			if(distance<60){
				if(isInProtectMode()==false){
					enterProtectMode();
				}
			}
			
			
		}
		
		
		//driveメソッド
		
		private void drive(IObject target){
			
			double rate1=0.8,rate2=0.9;
			int targetAngle=getHeadingTo(target)-getHeading();
			targetAngle=changeAngle(targetAngle);
			
			//方向に応じて、ステアリングを調整する
			//距離に応じて、スロットルを調整する
			if(targetAngle>10){
				setSteeringSetting(MAX_STEER_RIGHT);
				setThrottle((int)(MAX_THROTTLE*rate2));
			}else if (targetAngle<-10){
				setSteeringSetting(MAX_STEER_LEFT);
				setThrottle((int)(MAX_THROTTLE*rate2));
			}else if (getDistanceTo(target)>30){
				setSteeringSetting(targetAngle/2);
				setThrottle(MAX_THROTTLE);
			}else{
				setSteeringSetting(targetAngle);
				setThrottle((int)(MAX_THROTTLE*rate1));
			}
		}
		
		
		//ガソリンを給油する
		
		private void gotoGas(int number){
			
			int targetAngle=getHeadingTo(gasStations[number])-getHeading();
			targetAngle=changeAngle(targetAngle);
			double distance=getDistanceTo(gasStations[number]);
			
			if(getFuel()>70){
				gasFlag=false;
				startFlag=true;
				nextNo=getNearIObject(checkPoints);
				if(getDistanceTo(checkPoints[nextNo])<=80){
					nextNo++;
				}
			}else if(distance<25){
				if(isInProtectMode()==false){
					enterProtectMode();
				}
				setSteeringSetting(0);
				setThrottle(0);
			}
			else if(distance<80){
				setSteeringSetting(targetAngle);
				setThrottle(40);
			}
			else if(distance<200){
				setSteeringSetting(targetAngle);
				setThrottle(60);
			}
			else{
				drive(gasStations[number]);
			}
		}
		
		//他者を攻撃
		//方向を調べて、±20°以内ならタイヤを発射
		
		private void attack(ICar icar){
			int enemyAngle=getHeadingTo(icar)-getHeading();
			if(enemyAngle>-20&&enemyAngle<20&&isReadyToThrowSpareTire()){
				throwSpareTire();
			}
		}
		
		//角度を標準化する
		
		private int changeAngle(int angle){
			if(angle>180){
				angle=angle-360;
			}
			if(angle<-180){
				angle=angle+360;
			}
			return angle;
		}
		
		//最寄りのIObjectを検索する
		
		private int getNearIObject(IObject io[]){
			int j=0;
			double distance=getDistanceTo(io[j]);
			for(int i=1;i<io.length;i++){
				double distance2=getDistanceTo(io[i]);
				if(distance>distance2){
					distance=distance2;
					j=i;
				}
			}
			return j;
		}
		
		//敵・味方の判別
		private boolean teamCheck(ICar target){
			
			if(getSchoolName().equals(target.getSchoolName()))
				return true;
			else
				return false;
			
		}
		
		
		
	}


tations[];//ガス給油所配列
	IObject spareTires[];//タイヤ補給所配列
	ICar[] enemys;
	
	int nextNo,prevNo;

	boolean gasFlag=false;
	boolean startFlag=false;
	
	public String getName() {
		return "Banana";
	}
	
	public String getSchoolName() {
		return "D-02";
	}

	public byte getColor() {
		return CAR_BLUE;
	}
	
	public void initialize() {
		checkPoints=getCheckpoints();
		gasStations=getFuelDepots();
		spareTires=getSpareTireDepot();
		enemys=getOpponents();
		startFlag=true;
		prevNo=getPreviousCheckpoint();
		nextNo=getNearIObject(checkPoints);
//		if(getDistanceTo(checkPoints[nextNo])<=80){
//			nextNo++;
//		}
//	}

	public void move(int lastMoveTime, boolean hitWall, ICar collidedWithCar, ICar hitBySpareTire) {
		
		//タイヤ攻撃
		int enemyNo=getNearIObject(enemys);
		if(getDistanceTo(enemyNo)<70){
			attack(enemys[enemyNo]);
		}
		
		//プロテクトモード
		double distance=getDistanceTo(enemys[enemyNo]);
		if(distance<70){
			if(isInProtectMode()==false){
				enterProtectMode();
			}
		}
		
		
		//味方の場合後退
		if(collidedWithCar!=null&&gasFlag==false&&teamCheck(enemys[enemyNo])==true){
			setSteeringSetting(-5);
			setThrottle(MIN_THROTTLE);
			
		}
		
		
		//ガソリン補給
		if(getFuel()<30&&gasFlag==false){
			prevNo=getPreviousCheckpoint();
			gasFlag=true;
		}
		
		//前進
		
		if(gasFlag==true){
			//給油処理
			gotoGas(getNearIObject(gasStations));
			
		}else{
			//前進処理
			int targetNo;
			if(startFlag==true){
				//最寄りのチェックポイントに向かう
				targetNo=nextNo;
				if(prevNo!=getPreviousCheckpoint()){
					startFlag=false;
				}
			}else{
				//次のチェックポイントに向かう
				targetNo=getPreviousCheckpoint()+1;
			}
			
			//最後のチェックポイントを通過したら、最初に戻る
			if(targetNo>=checkPoints.length){
				targetNo=0;
			}
			drive(checkPoints[targetNo]);
		}
	}
	
	
	//driveメソッド
	
	private void drive(IObject target){
		
		double rate1=0.8,rate2=0.9;
		int targetAngle=getHeadingTo(target)-getHeading();
		targetAngle=changeAngle(targetAngle);
		
		//方向に応じて、ステアリングを調整する
		//距離に応じて、スロットルを調整する
		if(targetAngle>10){
			setSteeringSetting(MAX_STEER_RIGHT);
			setThrottle((int)(MAX_THROTTLE*rate2));
		}else if (targetAngle<-10){
			setSteeringSetting(MAX_STEER_LEFT);
			setThrottle((int)(MAX_THROTTLE*rate2));
		}else if (getDistanceTo(target)>30){
			setSteeringSetting(targetAngle/2);
			setThrottle(MAX_THROTTLE);
		}else{
			setSteeringSetting(targetAngle);
			setThrottle((int)(MAX_THROTTLE*rate1));
		}
	}
	
	//ガソリンを給油する
	
	private void gotoGas(int number){
		
		int targetAngle=getHeadingTo(gasStations[number])-getHeading();
		targetAngle=changeAngle(targetAngle);
		double distance=getDistanceTo(gasStations[number]);
		
		if(getFuel()>70){
			gasFlag=false;
			startFlag=true;
			nextNo=getNearIObject(checkPoints);
			if(getDistanceTo(checkPoints[nextNo])<=80){
				nextNo++;
			}
		}else if(distance<35){
			if(isInProtectMode()==false){
				enterProtectMode();
			}
			setSteeringSetting(0);
			setThrottle(0);
		}
		else if(distance<80){
			setSteeringSetting(targetAngle);
			setThrottle(40);
		}
		else if(distance<200){
			setSteeringSetting(targetAngle);
			setThrottle(60);
		}
		else{
			drive(gasStations[number]);
		}
	}
	
	//他者を攻撃
	//方向を調べて、±25°以内ならタイヤを発射
	
	private void attack(ICar icar){
		int enemyAngle=getHeadingTo(icar)-getHeading();
		if(enemyAngle>-25&&enemyAngle<25&&isReadyToThrowSpareTire()&&isReadyToThrowSpareTire()){
			throwSpareTire();
		}
	}
	
	//角度を標準化する
	
	private int changeAngle(int angle){
		if(angle>180){
			angle=angle-360;
		}
		if(angle<-180){
			angle=angle+360;
		}
		return angle;
	}
	
	//最寄りのIObjectを検索する
	
	private int getNearIObject(IObject io[]){
		int j=0;
		double distance=getDistanceTo(io[j]);
		for(int i=1;i<io.length;i++){
			double distance2=getDistanceTo(io[i]);
			if(distance>distance2){
				distance=distance2;
				j=i;
			}
		}
		return j;
	}
	
	//敵・味方の判定(true:味方、false:敵)
	
	private boolean teamCheck(ICar target){
		if(getSchoolName().equals(target.getSchoolName())){
			return true;
		}else{
			return false;
		}
	}
	
	
}


