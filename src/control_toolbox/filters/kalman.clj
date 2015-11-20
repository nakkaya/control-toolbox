(ns control-toolbox.filters.kalman
  (:require [clojure.core.matrix :as mat])
  (:import (com.sun.jna Function)
           (com.sun.jna.ptr PointerByReference)
           (com.nakkaya.filter.kalman CvMat CvKalman)))

;;cvkalman.cpp - http://sourcecodebrowser.com/opencv/1.0.0/cvkalman_8cpp.html

(let [fn (Function/getFunction "opencv_core" "cvReleaseMat")]
  (defn cvReleaseMat [mat]
    (.invoke fn (to-array [(PointerByReference. (.getPointer mat))]))))

(let [CV_32FC1 5
      create-mat-fn (Function/getFunction "opencv_core" "cvCreateMat")
      create-mat (fn [row col]
                   (.invoke create-mat-fn
                            com.nakkaya.filter.kalman.CvMat$ByReference
                            (to-array [row col CV_32FC1])))
      
      cvSetReal2D-fn (Function/getFunction "opencv_core" "cvSetReal2D")
      set-idx (fn [mat r c v]
                (.invoke cvSetReal2D-fn (to-array [mat r c (double v)])))

      cvGetReal2D-fn (Function/getFunction "opencv_core" "cvGetReal2D")
      get-idx (fn [mat r c]
                (.invoke cvGetReal2D-fn Double (to-array [mat r c])))]
  (defn update-CvMat-float [mat data]
    (let [_rows (-> mat .field1 .rows)
          _cols (-> mat .field2 .cols)
          [rows cols] (mat/shape data)]
      (if (or (not= _rows rows)
              (not= _cols cols))
        (throw (Exception. (str "Dimension Mismatch "
                                "CvMat: " _rows "X" _cols " "
                                "Matrix: " rows "X" cols))))
      (dotimes [r rows]
        (dotimes [c cols]
          (set-idx mat r c (mat/mget data r c))))
      mat))
  (defn to-CvMat-float [data]
    (let [[rows cols] (mat/shape data)
          mat (create-mat rows cols)]
      (update-CvMat-float mat data)
      mat))
  (defn from-CvMat-float [mat]
    (let [rows (-> mat .field1 .rows)
          cols (-> mat .field2 .cols)
          data (reduce (fn[rows idx]
                         (conj rows (map #(get-idx mat idx %) (range cols))))
                       [] (range rows))
          matrix (mat/matrix data)]
      matrix)))

(let [cvCreateKalman (Function/getFunction "opencv_video" "cvCreateKalman")]
  (defn kalman-create [nDynamParams nMeasureParams nControlParams]
    (.invoke cvCreateKalman com.nakkaya.filter.kalman.CvKalman$ByReference
             (to-array [nDynamParams nMeasureParams nControlParams]))))

(let [cvReleaseKalman (Function/getFunction "opencv_video" "cvReleaseKalman")]
  (defn kalman-release [filter]
    (.invoke cvReleaseKalman (to-array [(PointerByReference. (.getPointer filter))]))))

(let [cvKalmanPredict (Function/getFunction "opencv_video" "cvKalmanPredict")]
  (defn kalman-predict [filter]
    (.invoke cvKalmanPredict (to-array [filter 0]))))

(let [cvKalmanCorrect (Function/getFunction "opencv_video" "cvKalmanCorrect")]
  (defn kalman-correct [filter mat]
    (.invoke cvKalmanCorrect com.nakkaya.filter.kalman.CvMat$ByReference (to-array [filter mat]))))

(defprotocol KalmanProtocol
  (get-filter [f])
  (get-measurement [this])
  (update-measurement [this data]))

(deftype KalmanFilter [filter measurement]
  KalmanProtocol
  (get-filter [this]
    filter)
  (get-measurement [this]
    measurement)
  (update-measurement [this data]
    (update-CvMat-float measurement data))
  (finalize [this]
    (cvReleaseMat measurement)
    (kalman-release filter)))

(defn kalman-filter [nDynamParams nMeasureParams
                     state-pre transition-matrix measurement-matrix
                     process-noise-cov measurement-noise-cov error-cov-post]
  (let [filter (kalman-create nDynamParams nMeasureParams 0)
        measurement (to-CvMat-float
                     (mat/matrix
                      (reduce
                       (fn[rows idx]
                         (conj rows (reduce (fn[h v] (conj h 0)) [] (range 1))))
                       [] (range nMeasureParams))))]
    (update-CvMat-float (.state_pre filter) state-pre)
    (update-CvMat-float (.transition_matrix filter) transition-matrix)
    (update-CvMat-float (.measurement_matrix filter) measurement-matrix)
    (update-CvMat-float (.process_noise_cov filter) process-noise-cov)
    (update-CvMat-float (.measurement_noise_cov filter) measurement-noise-cov)
    (update-CvMat-float (.error_cov_post filter) error-cov-post)
    (KalmanFilter. filter measurement)))

(defn kalman-cycle [filter measurement]
  (kalman-predict (get-filter filter))
  (update-measurement filter measurement)
  (from-CvMat-float (kalman-correct (get-filter filter) (get-measurement filter))))
