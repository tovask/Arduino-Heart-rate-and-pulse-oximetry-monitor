
/*
https://www.arduino.cc/en/Reference/WiFiNINAUDPBeginMulticast

*/

#include "UDPServer.h"
#include "config.h"
#include "WifiManager.h"

bool UDPServer::start() {
	LOG("Starting UDP Multicast server...\n");
	return Udp.beginMulticast(IPAddress(224, 0, 0, 123), localPort) ? true : false;
}

int UDPServer::check() {
	LOG("Checking for incoming UDP packets...\n");
	// if there's data available, read a packet
	// packetSize = Udp.parsePacket();
	packetSize = 0;
	int lastPacketSize;
	while ((lastPacketSize  = Udp.parsePacket())) {
		packetSize = lastPacketSize;
		this->lastRemoteIP = Udp.remoteIP();
		this->lastRemotePort = Udp.remotePort();
		LOG("Received UDP packet of size ");
		LOG(packetSize);
		LOG(" from ");
		LOG(this->lastRemoteIP);
		LOG(":");
		LOG(this->lastRemotePort);
		LOG("\n");
		// read the packet into packetBufffer
		packetLength = Udp.read(packetBuffer, PACKET_BUFFER_LENGTH-1);
		// Udp.flush();
	}
	if (!packetSize) {
		LOG("No UDP packet found.\n");
		return -1;	// TODO: return 0, not?
	}
	if (packetLength > 0) {
		packetBuffer[packetLength] = 0;
		LOG(String("\tcontent: ") + packetBuffer + "\n");
		
		LOG( String("Sending Arduino's IP ") + wifi.getLocalIpAddressString() + " to " );
		LOG(this->lastRemoteIP);
		LOG(":");
		LOG(this->lastRemotePort);
		Udp.beginPacket(this->lastRemoteIP, this->lastRemotePort);
		String reply = "Arduino's IP: " + wifi.getLocalIpAddressString();
		const char* replyBuf = reply.c_str();
		Udp.write(replyBuf, strlen(replyBuf));
		Udp.endPacket();
	}
	LOG("\n");
	return packetLength;
}
