# Arduino heart rate and pulse oximetry monitor
Monitoring Heart Rate with AD8232 ECG and Pulse Oximetry with MAX30102, from an Arduino Uno WiFi Rev2.
Sending the data to a PC through Wifi.

### For the Arduino
Edit [config.h](src/config.h) to your Wifi name/password.  
Upload the compiled program to the device.  
Connect the specific pins to the sensors.  
Reset.


### For the PC
Compile & Run: `javac *.java && java -cp hsqldb-2.3.6.jar:. PC`


