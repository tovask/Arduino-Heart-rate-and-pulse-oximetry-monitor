
/*
Wiring:
    5V = 5V ( or 3.3V)
    SDA = SDA
    SCL = SCL
    GND = GND
    INT = Not connected
*/
// dependency: https://github.com/sparkfun/SparkFun_MAX3010x_Sensor_Library
// (see also https://github.com/aromring/MAX30102_by_RF)

#include "MAX30102_PulseOximeter.h"

#include <Wire.h>
#include "heartRate.h"      // Heart rate calculating algorithm
#include "config.h"         // LOG macro

bool MAX30102_PulseOximeter::setup() {
  // Initialize sensor
  if (particleSensor.begin() == false) //Use default I2C port, default speed
  {
    LOG("MAX30105/2 was not found. Please check wiring/power.\n");
    initialized = false;
    return false;
  }
  particleSensor.setup(); //Configure sensor with default settings
  LOG("MAX30105/2 settled up!\n");
  //  particleSensor.setPulseAmplitudeRed(0x0A); //Turn Red LED to low to indicate sensor is running
  initialized = true;
  return true;
}

uint32_t MAX30102_PulseOximeter::getIRValue() {
  if (!initialized) {
    return 0;
  }
  return particleSensor.getIR();
}

uint32_t MAX30102_PulseOximeter::getRedValue() {
  if (!initialized) {
    return 0;
  }
  return particleSensor.getRed();
}

//int MAX30102_PulseOximeter::getValue() {
//  i++;
//  long irValue = particleSensor.getIR();    //Reading the IR value it will permit us to know if there's a finger on the sensor or not
//  //Also detecting a heartbeat
//  if (irValue > 7000) {
//    if (i % 10 == 0) {
//      Serial.print("BPM ");
//      Serial.println(beatAvg);
//    }
//
//    if (checkForBeat(irValue) == true)                        //If a heart beat is detected
//    {
//      //We sensed a beat!
//      Serial.println("Beeeeeeeat!!!");
//      long delta = millis() - lastBeat;                   //Measure duration between two beats
//      lastBeat = millis();
//
//      beatsPerMinute = 60 / (delta / 1000.0);           //Calculating the BPM
//
//      if (beatsPerMinute < 255 && beatsPerMinute > 20)               //To calculate the average we strore some values (4) then do some math to calculate the average
//      {
//        rates[rateSpot++] = (byte)beatsPerMinute; //Store this reading in the array
//        rateSpot %= RATE_SIZE; //Wrap variable
//
//        //Take average of readings
//        beatAvg = 0;
//        for (byte x = 0 ; x < RATE_SIZE ; x++)
//          beatAvg += rates[x];
//        beatAvg /= RATE_SIZE;
//      }
//    }
//  }
//  if (irValue < 7000) {      //If no finger is detected it inform the user and put the average BPM to 0 or it will be stored for the next measure
//    beatAvg = 0;
//    Serial.print(irValue);
//    Serial.println("Please Place your finger ");
//  }
//}

//int check = 0;
//void loop() {
//  //  Serial.println(check);
//  check = particleSensor.check(); //Check the sensor
//  while (particleSensor.available()) {
//    // read stored IR
//    Serial.print("IR: ");
//    Serial.print(particleSensor.getFIFOIR());
//    Serial.print(", ");
//    // read stored red
//    Serial.print("red: ");
//    Serial.println(particleSensor.getFIFORed());
//    // read next set of samples
//    particleSensor.nextSample();
//  }
//}

