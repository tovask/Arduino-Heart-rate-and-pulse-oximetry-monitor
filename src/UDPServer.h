#ifndef UDPServer_h
#define UDPServer_h

#include <WiFiUdp.h>
#include "WifiManager.h"

#define PACKET_BUFFER_LENGTH 256

class UDPServer {
private:
	
	WiFiUDP Udp;
	
	unsigned int localPort = 2390;      // local port to listen on
	IPAddress lastRemoteIP;      // last received packet's adress
	unsigned int lastRemotePort;      // last received packet's port
	
	char packetBuffer[PACKET_BUFFER_LENGTH]; //buffer to hold incoming packet
	int packetSize = 0;
	int packetLength = 0;
	
	WifiManager wifi;
	
public:
	UDPServer(WifiManager& wifi):wifi(wifi){};
	bool start();
	int check();
};

#endif