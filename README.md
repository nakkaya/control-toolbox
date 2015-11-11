# control-toolbox

A Clojure library designed to ... well, that part is up to you.

## Usage

### Motion

#### Holomonic Motion

Initialize the model,

```
(def motion (holomonic-motion-init {:diameter 0.057
                                    :angles [60 135 225 300]}))
```

Get motor speeds to direction,

```
(holomonic-motion-speeds motion (vector-2d 10 10) 0)
```

### Control

#### PID

   From Wikipedia,

> A PID controller calculates an 'error' value as the difference between
> a measured [Input] and a desired setpoint. The controller attempts
> to minimize the error by adjusting [an Output].

   We tell the PID controller what to measure (input) Where we want
   that measurement to be (setpoint) and the variable to adjust
   that will make that happen (output). Contoller then adjusts the
   output trying to make the input equal the setpoint.
 
   For reference, in a car, the Input, Setpoint, and Output would be the
   speed, desired speed, and gas pedal angle respectively.
 
   Some notes on tuning PID controllers,
 
   - Use KP to decrease the rise time.
   - Use KD to reduce the overshoot and settling time.
   - Use KI to eliminate the steady-state error.
 
   The effects of increasing each of the controller parameters KP , KI
   and KD can be summarized as,

     |----------+-----------+-----------+---------------+-----------|
     | Response | Rise Time | Overshoot | Settling Time | S-S Error |
     |----------+-----------+-----------+---------------+-----------|
     | Kp       | Decrease  | Increase  |               | Decrease  |
     |----------+-----------+-----------+---------------+-----------|
     | Ki       | Decrease  | Increase  | Increase      | Eliminate |
     |----------+-----------+-----------+---------------+-----------|
     | Kd       |           | Decrease  | Decrease      |           |
     |----------+-----------+-----------+---------------+-----------|
   
## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
