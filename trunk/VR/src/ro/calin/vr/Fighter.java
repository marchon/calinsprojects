package ro.calin.vr;

import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;

public class Fighter extends Model {
	private static final float MOVE_ACCEL = 2f;
	private static final float AUTO_DECEL = 0.5f;
	private static final float MAX_UP = 10f;
	private static final float MAX_DOWN = -2f;
	private static final float MAX_LEFT = -8f;
	private static final float MAX_RIGHT = 8f;
	private static final float MIN_VEL = 1f;
	private static final float MAX_VEL = 3f;
	private static final float MIN_SPEED = 1f;
	private static final float MAX_SPEED = 5f;
	private static final float SPEED_FACTOR = 1.0f;
	private static final float MAX_ROT_ANGLE = FastMath.PI / 16;
	private static final float ROT_ANGLE_INC = FastMath.PI / 8;

	private Vector3f pos = new Vector3f(0, 0, 0);
	
	private float rotXAngle = 0;
	private float rotYAngle = 0;
	private Matrix3f rotX= new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1);
	private Matrix3f rotY = new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1);
	
	private boolean moveIssued = false;
	private Camera cam;

	private float velocityX;
	private float velocityY;
	
	private float speed = MIN_SPEED;
	
	private Vector3f camLeft;
	private Vector3f camUp;

	public Fighter(String id, Spatial model, Camera cam) {
		super(id, model);
		this.cam = cam;
		camLeft = cam.getLeft();
		camUp = cam.getUp();
	}
	
	public void increaseSpeed(float time) {
		speed += SPEED_FACTOR * time;
		if(speed > MAX_SPEED)
			speed = MAX_SPEED;
	}
	
	public void decreaseSpeed(float time) {
		speed -= SPEED_FACTOR * time;
		if(speed < MIN_SPEED)
			speed = MIN_SPEED;
	}

	public void moveUp(float time) {
		if(velocityY < MIN_VEL) {
			velocityY = MIN_VEL;
		} else {
			velocityY += time * MOVE_ACCEL;
			if(velocityY > MAX_VEL)
				velocityY = MAX_VEL;
		}
		
		if(rotXAngle < MAX_ROT_ANGLE)
			rotXAngle += time * ROT_ANGLE_INC; 
		
		moveIssued = true;
	}

	public void moveDown(float time) {
		if(velocityY > -MIN_VEL) {
			velocityY = -MIN_VEL;
		} else {
			velocityY -= time * MOVE_ACCEL;
			if(velocityY < -MAX_VEL)
				velocityY = -MAX_VEL;
		}
		
		if(rotXAngle > -MAX_ROT_ANGLE)
			rotXAngle -= time * ROT_ANGLE_INC;
		
		moveIssued = true;
	}

	public void moveLeft(float time) {
		if(velocityX > -MIN_VEL) {
			velocityX = -MIN_VEL;
		} else {
			velocityX -= time * MOVE_ACCEL;
			if(velocityX < -MAX_VEL)
				velocityX = -MAX_VEL;
		}
		
		if(rotYAngle < MAX_ROT_ANGLE)
			rotYAngle += time * ROT_ANGLE_INC; 
		
		moveIssued = true;
	}

	public void moveRight(float time) {
		if(velocityX < MIN_VEL) {
			velocityX = MIN_VEL;
		} else {
			velocityX += time * MOVE_ACCEL;
			if(velocityX > MAX_VEL)
				velocityX = MAX_VEL;
		}
		
		if(rotYAngle > -MAX_ROT_ANGLE)
			rotYAngle -= time * ROT_ANGLE_INC;
		
		moveIssued = true;
	}

	public void update(float time) {
		if (!moveIssued) {
			if (FastMath.abs(pos.x) > .1f) {
				// the deceleration is proportional with distance from origin
				velocityX += -Math.signum(pos.x) * (AUTO_DECEL) * time;
			} else {
				velocityX = 0;
			}
			if (FastMath.abs(pos.y) > .1f) {
				velocityY += -Math.signum(pos.y) * (AUTO_DECEL) * time;
			} else {
				velocityY = 0;
			}
			
			if(FastMath.abs(rotYAngle) > .01f)
				rotYAngle -= FastMath.sign(rotYAngle) * time * ROT_ANGLE_INC * 2;
			else
				rotYAngle = 0;
			
			if(FastMath.abs(rotXAngle) > .01f)
				rotXAngle -= FastMath.sign(rotXAngle) * time * ROT_ANGLE_INC * 2;
			else
				rotXAngle = 0;
		} 
		//calc position
		pos.x += (time * velocityX);
		pos.y += (time * velocityY);
		pos.z = (MIN_SPEED - speed) * 3;
		
		//calc rotation on y
		rotY.m00 = FastMath.cos(rotYAngle);
		rotY.m02 = -FastMath.sin(rotYAngle);
		rotY.m20 = FastMath.sin(rotYAngle);
		rotY.m22 = FastMath.cos(rotYAngle);

		//calc rotation on x
		rotX.m11 = FastMath.cos(rotXAngle);
		rotX.m12 = FastMath.sin(rotXAngle);
		rotX.m21 = -FastMath.sin(rotXAngle);
		rotX.m22 = FastMath.cos(rotXAngle);
		
		if (pos.y > MAX_UP)
			pos.y = MAX_UP;
		if (pos.y < MAX_DOWN)
			pos.y = MAX_DOWN;
		if (pos.x < MAX_LEFT)
			pos.x = MAX_LEFT;
		if (pos.x > MAX_RIGHT)
			pos.x = MAX_RIGHT;

		
		//update location/rotation
		this.setLocalTranslation(pos);
		this.setLocalRotation(rotY.mult(rotX));
		
		
		//camera special effect :d
		float alfpha = (FastMath.PI * pos.x) / (30 * MAX_RIGHT);
		camLeft.x = -FastMath.cos(alfpha);
		camLeft.z = -FastMath.sin(alfpha);
		
		float beta = (FastMath.PI * pos.y) / (30 * MAX_UP);
		camUp.y = FastMath.cos(beta);
		camUp.z = FastMath.sin(beta);

		cam.setLeft(camLeft);
		cam.setUp(camUp);
		
		moveIssued = false;
	}
}
