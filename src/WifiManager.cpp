
/*
https://github.com/arduino-libraries/WiFiNINA
*/

#include "WifiManager.h"

bool WifiManager::connect() {

	// check for the WiFi module:
	if (WiFi.status() == WL_NO_MODULE) {
		LOG("Communication with WiFi module failed!\n");
		return false;
	}

	String fv = WiFi.firmwareVersion();
	if (fv < WIFI_FIRMWARE_LATEST_VERSION) {
		LOG("Please upgrade the WiFi firmware!\n");
	}

	// attempt to connect to Wifi network:
	while (status != WL_CONNECTED) {
		LOG("Attempting to connect to SSID: ");
		LOG(ssid);
		LOG(" ...\n");
		// Connect to WPA/WPA2 network. Change this line if using open or WEP network:
		status = WiFi.begin(ssid, pass);

		// wait 5 seconds for connection:
		delay(5000);
	}
	LOG("Connected to wifi");
	// printStatus();
	return true;
}


void WifiManager::printStatus() {
	// print the SSID of the network you're attached to:
	Serial.print("\nSSID: ");
	Serial.println(WiFi.SSID());

	// print your board's IP address:
	IPAddress ip = WiFi.localIP();
	Serial.print("IP Address: ");
	Serial.println(ip);

	// print the received signal strength:
	long rssi = WiFi.RSSI();
	Serial.print("signal strength (RSSI):");
	Serial.print(rssi);
	Serial.println(" dBm\n");
}

long WifiManager::getSignalStrength() {
	return WiFi.RSSI();
}

IPAddress WifiManager::getLocalIpAddress() {
	return WiFi.localIP();
}

String WifiManager::getLocalIpAddressString() {
	const IPAddress& address = getLocalIpAddress();
	return String() + address[0] + "." + address[1] + "." + address[2] + "." + address[3];
}