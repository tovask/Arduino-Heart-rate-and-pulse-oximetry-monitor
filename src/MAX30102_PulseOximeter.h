#ifndef MAX30102_PulseOximeter_h
#define MAX30102_PulseOximeter_h

// https://github.com/sparkfun/SparkFun_MAX3010x_Sensor_Library
#include "MAX30105.h"

class MAX30102_PulseOximeter {
  private:

    MAX30105 particleSensor;
    
    bool initialized = false;

    // static const byte RATE_SIZE = 4; //Increase this for more averaging. 4 is good.
    // byte rates[RATE_SIZE]; //Array of heart rates
    // byte rateSpot = 0;
    // long lastBeat = 0; //Time at which the last beat occurred
    // float beatsPerMinute;
    // int beatAvg;
    // 
    // int i;

  public:
    bool setup();
    uint32_t getIRValue();
    uint32_t getRedValue();
};

#endif
