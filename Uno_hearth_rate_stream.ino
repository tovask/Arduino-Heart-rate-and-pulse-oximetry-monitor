
/*
  https://github.com/arduino/ArduinoCore-avr/
*/

#include "src/AD8232_ECG.h"
#include "src/MAX30102_PulseOximeter.h"
#include "src/WifiManager.h"
#include "src/UDPServer.h"
#include "src/TCPServer.h"

AD8232_ECG ecg;
MAX30102_PulseOximeter po;

WifiManager wifi;
UDPServer udp(wifi);
TCPServer tcp;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  // while (!Serial){} // wait for serial port to connect. Needed for native USB port only
  Serial.println("Arduino main setup started");
  wifi.connect();
  wifi.printStatus();
  ecg.setup();
  po.setup();
  udp.start();
  tcp.start();
}

void loop() {
  // put your main code here, to run repeatedly:
  // Serial.print("ecg: ");
  // Serial.println(ecg.getValue());
  // Serial.print("po: ");
  // Serial.println(po.getValue());
  // Serial.println(wifi.getSignalStrength());
  if (udp.check() > 0) {
    // delay(10000);
    String msg;
    do {
      delay(100);
      msg = String("ecg: ") + ecg.getValue() + ", po_IR: " + po.getIRValue() + ", po_Red: " + po.getRedValue() + "\n";
      // msg = String("ecg: ") + random(100000) + ", po_IR: " + random(100000) + ", po_Red: " + random(100000) + "\n";
    } while ( tcp.send(msg) );
  }
  delay(1000);
}
