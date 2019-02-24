BURP lerps a rotation by changing the longest change in rotation first.  i.e., 
Quaternion q0, q1, diff;
q0 = 45,  90, -45 //degs
q1 =  0, 180,  0 //degs
diff = q1.subtract(q0)
diff = -45,  90,  45//
max = abs(diff.x or diff.y, diff.z)
as time (t) goes up, inverse time (1 - t) goes down. 
BURP will starting adjusting the y value as time goes on. 90 = %100 of max so it starts right away.  The 45s will start halfway through the process. 
Press [ for prograde, ] for retrograde, ; for normal, ' for anti-normal, l (L) for Auto Level
Esc key to exit. 
