import com.ibm.rally.*;

import com.ibm.rally.ICar;


public class RallyCar extends Car {
	
	IObject checkPoints[];//�`�F�b�N�|�C���g�z��
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
			
			//���ߋ����E�O���̑��҂��U��
			opponents=getOpponents();
			int oppoNo=getNearIObject(opponents);
			if(getDistanceTo(opponents[oppoNo])<80&&teamCheck(opponents[oppoNo])==true){
				attack(opponents[oppoNo]);
			}
			
			//���҂ƏՓ�
			if(collidedWithCar!=null&&waitBack==0&&gasFlag==false){
				waitBack=8;

			}
			
			if(getDistanceTo(opponents[oppoNo])<20){
				waitBack=8;
			}
			
			//�ǂƏՓ�
			if(hitWall==true&&waitBack==0&&gasFlag==false){
				
			}
			
			//�K�\�����⋋
			if(getFuel()<30&&waitBack==0&&gasFlag==false&&getClockTicks()<400){
				prevNo=getPreviousCheckpoint();
				gasFlag=true;
			}
			
			//�O�i�E���
			if(waitBack>0){
				//��ޏ���
				waitBack--;
				setSteeringSetting(-5);
				setThrottle(MIN_THROTTLE);
				
			}else if(gasFlag==true){
				//��������
				gotoGas(getNearIObject(gasStations));
				
			}else{
				//�O�i����
				int targetNo;
				if(startFlag==true){
					//�Ŋ��̃`�F�b�N�|�C���g�Ɍ�����
					targetNo=nextNo;
					if(prevNo!=getPreviousCheckpoint()){
						startFlag=false;
					}
				}else{
					//���̃`�F�b�N�|�C���g�Ɍ�����
					targetNo=getPreviousCheckpoint()+1;
				}
				
				//�Ō�̃`�F�b�N�|�C���g��ʉ߂�����A�ŏ��ɖ߂�
				if(targetNo>=checkPoints.length){
					targetNo=0;
				}
				drive(checkPoints[targetNo]);
			}
			
			//�v���e�N�g���[�h
			int enemyNo=getNearIObject(enemys);
			double distance=getDistanceTo(enemys[enemyNo]);
			if(distance<60){
				if(isInProtectMode()==false){
					enterProtectMode();
				}
			}
			
			
		}
		
		
		//drive���\�b�h
		
		private void drive(IObject target){
			
			double rate1=0.8,rate2=0.9;
			int targetAngle=getHeadingTo(target)-getHeading();
			targetAngle=changeAngle(targetAngle);
			
			//�����ɉ����āA�X�e�A�����O�𒲐�����
			//�����ɉ����āA�X���b�g���𒲐�����
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
		
		
		//�K�\��������������
		
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
		
		//���҂��U��
		//�����𒲂ׂāA�}20���ȓ��Ȃ�^�C���𔭎�
		
		private void attack(ICar icar){
			int enemyAngle=getHeadingTo(icar)-getHeading();
			if(enemyAngle>-20&&enemyAngle<20&&isReadyToThrowSpareTire()){
				throwSpareTire();
			}
		}
		
		//�p�x��W��������
		
		private int changeAngle(int angle){
			if(angle>180){
				angle=angle-360;
			}
			if(angle<-180){
				angle=angle+360;
			}
			return angle;
		}
		
		//�Ŋ���IObject����������
		
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
		
		//�G�E�����̔���
		private boolean teamCheck(ICar target){
			
			if(getSchoolName().equals(target.getSchoolName()))
				return true;
			else
				return false;
			
		}
		
		
		
	}


tations[];//�K�X�������z��
	IObject spareTires[];//�^�C���⋋���z��
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
		
		//�^�C���U��
		int enemyNo=getNearIObject(enemys);
		if(getDistanceTo(enemyNo)<70){
			attack(enemys[enemyNo]);
		}
		
		//�v���e�N�g���[�h
		double distance=getDistanceTo(enemys[enemyNo]);
		if(distance<70){
			if(isInProtectMode()==false){
				enterProtectMode();
			}
		}
		
		
		//�����̏ꍇ���
		if(collidedWithCar!=null&&gasFlag==false&&teamCheck(enemys[enemyNo])==true){
			setSteeringSetting(-5);
			setThrottle(MIN_THROTTLE);
			
		}
		
		
		//�K�\�����⋋
		if(getFuel()<30&&gasFlag==false){
			prevNo=getPreviousCheckpoint();
			gasFlag=true;
		}
		
		//�O�i
		
		if(gasFlag==true){
			//��������
			gotoGas(getNearIObject(gasStations));
			
		}else{
			//�O�i����
			int targetNo;
			if(startFlag==true){
				//�Ŋ��̃`�F�b�N�|�C���g�Ɍ�����
				targetNo=nextNo;
				if(prevNo!=getPreviousCheckpoint()){
					startFlag=false;
				}
			}else{
				//���̃`�F�b�N�|�C���g�Ɍ�����
				targetNo=getPreviousCheckpoint()+1;
			}
			
			//�Ō�̃`�F�b�N�|�C���g��ʉ߂�����A�ŏ��ɖ߂�
			if(targetNo>=checkPoints.length){
				targetNo=0;
			}
			drive(checkPoints[targetNo]);
		}
	}
	
	
	//drive���\�b�h
	
	private void drive(IObject target){
		
		double rate1=0.8,rate2=0.9;
		int targetAngle=getHeadingTo(target)-getHeading();
		targetAngle=changeAngle(targetAngle);
		
		//�����ɉ����āA�X�e�A�����O�𒲐�����
		//�����ɉ����āA�X���b�g���𒲐�����
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
	
	//�K�\��������������
	
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
	
	//���҂��U��
	//�����𒲂ׂāA�}25���ȓ��Ȃ�^�C���𔭎�
	
	private void attack(ICar icar){
		int enemyAngle=getHeadingTo(icar)-getHeading();
		if(enemyAngle>-25&&enemyAngle<25&&isReadyToThrowSpareTire()&&isReadyToThrowSpareTire()){
			throwSpareTire();
		}
	}
	
	//�p�x��W��������
	
	private int changeAngle(int angle){
		if(angle>180){
			angle=angle-360;
		}
		if(angle<-180){
			angle=angle+360;
		}
		return angle;
	}
	
	//�Ŋ���IObject����������
	
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
	
	//�G�E�����̔���(true:�����Afalse:�G)
	
	private boolean teamCheck(ICar target){
		if(getSchoolName().equals(target.getSchoolName())){
			return true;
		}else{
			return false;
		}
	}
	
	
}


