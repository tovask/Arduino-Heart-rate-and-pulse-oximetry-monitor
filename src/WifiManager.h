#ifndef WifiManager_h
#define WifiManager_h

#include <WiFiNINA.h>

#include "config.h"

class WifiManager {
private:
	
	const char * ssid = SECRET_SSID;        // your network SSID (name)
	const char * pass = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)
	int keyIndex = 0;            // your network key Index number (needed only for WEP)
	
	int status = WL_IDLE_STATUS;
	
public:
	bool connect();
	void printStatus();
	long getSignalStrength();
	IPAddress getLocalIpAddress();
	String getLocalIpAddressString();
};

#endif