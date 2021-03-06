package com.nakkaya.filter.kalman;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.FloatByReference;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : /usr/local/Cellar/opencv/2.4.9/include/opencv2/video/tracking_bak.hpp:41</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CvKalman extends Structure {
	/** number of measurement vector dimensions */
	public int MP;
	/** number of state vector dimensions */
	public int DP;
	/** number of control vector dimensions */
	public int CP;
	/**
	 * =state_pre->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference PosterState;
	/**
	 * =state_post->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference PriorState;
	/**
	 * =transition_matrix->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference DynamMatr;
	/**
	 * =measurement_matrix->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference MeasurementMatr;
	/**
	 * =measurement_noise_cov->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference MNCovariance;
	/**
	 * =process_noise_cov->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference PNCovariance;
	/**
	 * =gain->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference KalmGainMatr;
	/**
	 * =error_cov_pre->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference PriorErrorCovariance;
	/**
	 * =error_cov_post->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference PosterErrorCovariance;
	/**
	 * temp1->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference Temp1;
	/**
	 * temp2->data.fl<br>
	 * C type : float*
	 */
	public FloatByReference Temp2;
	/**
	 * predicted state (x'(k)):<br>
	 * x(k)=A*x(k-1)+B*u(k)<br>
	 * C type : CvMat*
	 */
	public CvMat.ByReference state_pre;
	/**
	 * corrected state (x(k)):<br>
	 * x(k)=x'(k)+K(k)*(z(k)-H*x'(k))<br>
	 * C type : CvMat*
	 */
	public Pointer state_post;
	/**
	 * state transition matrix (A)<br>
	 * C type : CvMat*
	 */
	public CvMat.ByReference transition_matrix;
	/**
	 * control matrix (B)<br>
	 * (it is not used if there is no control)<br>
	 * C type : CvMat*
	 */
	public Pointer control_matrix;
	/**
	 * measurement matrix (H)<br>
	 * C type : CvMat*
	 */
	public CvMat.ByReference measurement_matrix;
	/**
	 * process noise covariance matrix (Q)<br>
	 * C type : CvMat*
	 */
	public CvMat.ByReference process_noise_cov;
	/**
	 * measurement noise covariance matrix (R)<br>
	 * C type : CvMat*
	 */
	public CvMat.ByReference measurement_noise_cov;
	/**
	 * priori error estimate covariance matrix (P'(k)):<br>
	 * P'(k)=A*P(k-1)*At + Q)<br>
	 * C type : CvMat*
	 */
	public Pointer error_cov_pre;
	/**
	 * Kalman gain matrix (K(k)):<br>
	 * K(k)=P'(k)*Ht*inv(H*P'(k)*Ht+R)<br>
	 * C type : CvMat*
	 */
	public Pointer gain;
	/**
	 * posteriori error estimate covariance matrix (P(k)):<br>
	 * P(k)=(I-K(k)*H)*P'(k)<br>
	 * C type : CvMat*
	 */
	public CvMat.ByReference error_cov_post;
	/**
	 * temporary matrices<br>
	 * C type : CvMat*
	 */
	public Pointer temp1;
	/** C type : CvMat* */
	public Pointer temp2;
	/** C type : CvMat* */
	public Pointer temp3;
	/** C type : CvMat* */
	public Pointer temp4;
	/** C type : CvMat* */
	public Pointer temp5;
	public CvKalman() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("MP", "DP", "CP", "PosterState", "PriorState", "DynamMatr", "MeasurementMatr", "MNCovariance", "PNCovariance", "KalmGainMatr", "PriorErrorCovariance", "PosterErrorCovariance", "Temp1", "Temp2", "state_pre", "state_post", "transition_matrix", "control_matrix", "measurement_matrix", "process_noise_cov", "measurement_noise_cov", "error_cov_pre", "gain", "error_cov_post", "temp1", "temp2", "temp3", "temp4", "temp5");
	}
	public CvKalman(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends CvKalman implements Structure.ByReference {
		
	};
	public static class ByValue extends CvKalman implements Structure.ByValue {
		
	};
}
