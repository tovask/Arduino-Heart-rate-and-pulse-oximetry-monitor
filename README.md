# Arduino heart rate and pulse oximetry monitor
Monitoring Heart Rate with AD8232 ECG and Pulse Oximetry with MAX30102, from an Arduino Uno WiFi Rev2.
Sending the data to a PC through Wifi.

## For the Arduino
Edit [config.h](src/config.h) to your Wifi name/password.  
Upload the compiled program to the device.  
Connect the specific pins to the sensors.  
Reset.


## For the PC
Compile & Run: `javac *.java && java -cp hsqldb-2.3.6.jar:. PC`  
  
##### To build a single jar:  
 - unpack hsqldb.jar next to the own class files (the 'org' directory is in the same level as the 'PC.class')  
 - make sure PC.mf exists with the right content  
 - `jar cvfm PC.jar PC.mf *.class org`  
 - to run it: `java -jar PC.jar`  
 
##### Pack it into an .exe file:  
 - download [Launch4j](http://launch4j.sourceforge.net/)  
 - run it with [the configuration file](pc/launch4j_config.xml)  
  
<br/><br/><br/><br/><br/><br/>
(download jre from https://adoptopenjdk.net/releases.html)
