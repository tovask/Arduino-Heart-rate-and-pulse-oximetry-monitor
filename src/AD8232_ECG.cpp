/*
Wiring:
    Arduino     AD8232
      10    -   LO+
      11    -   LO-
      A0    -   Output
      3.3V  -   3.3V
      GND   -   GND
*/

#include "AD8232_ECG.h"
#include <Arduino.h>

bool AD8232_ECG::setup() {
  pinMode(10, INPUT); // Setup for leads off detection LO +
  pinMode(11, INPUT); // Setup for leads off detection LO -
  return true;
}

int AD8232_ECG::getValue() {
  if ((digitalRead(10) == 1) || (digitalRead(11) == 1)) {
    //Serial.println(analogRead(A0));
    //    Serial.println("!");
    return -1;
  }
  else {
    // send the value of analog input 0:
    // Serial.println(analogRead(A0));
    // Serial.println(" ok");
    return analogRead(A0);
  }
  //Wait for a bit to keep serial data from saturating
  delay(1);
}
