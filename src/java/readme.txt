Rebuild OpenCV Bindings,

`CvMat`

```
java -jar jnaerator-0.13-SNAPSHOT-shaded.jar /usr/local/Cellar/opencv/2.4.9/include/opencv2/core/types_c.h -gui -mode Directory -runtime JNA -v
```

`CvKalman`

```
java -jar jnaerator-0.13-SNAPSHOT-shaded.jar /usr/local/Cellar/opencv/2.4.9/include/opencv2/video/tracking.hpp -gui -mode Directory -runtime JNA -v
```

Get define for `CV_32FC1` compile `vals.c`
