#!/usr/bin/env python3

import sys
import socket
import datetime
import json

MSG_SIZE = 1500
TIMEOUT = 10

packets = {}
seq = []

def log(string):
  sys.stderr.write(datetime.datetime.now().strftime("%H:%M:%S.%f") + " " + string + "\n")
  sys.stderr.flush()

#determines if previous packet to the given one has been received
def has_prev(packet):
  currseq = packet['sequence']

  for sqn in packets:
    if sqn + len(packets[sqn]['data']) == currseq:
      return True

  return False

#writes all data in order
def writemessage():
  seq.sort()
  for num in seq:
    log('writing num: ' + str(num))
    if num == 0 or has_prev(packets[num]):
      sys.stdout.write(packets[num]['data'])

def main():
  # Bind to localhost and an ephemeral port
  udp_ip = "127.0.0.1"
  udp_port = 0

  # Set up the socket
  sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
  sock.bind((udp_ip, udp_port))
  sock.settimeout(TIMEOUT)

  # Get port we bound to
  udp_port = sock.getsockname()[1]
  log(f"[bound] {udp_port}")

  # Now listen for packets
  while True:
    result = sock.recvfrom(MSG_SIZE)

    # If nothing is ready, we hit the timeout
    if result:
      (data, addr) = result
      data = data.decode('ascii')

      try:
        decoded = json.loads(data)

        ackseq = decoded['sequence']
        # If the EOF flag is set, write out all data and exit
        if decoded['eof']:
          writemessage()
          msg = json.dumps({"ack": ackseq})
          log("ABOUT TO SEND " + msg)
          if sock.sendto(msg.encode('ascii'), addr) < len(msg):
            log("[error] unable to fully send packet")
          log("[completed]")
          sys.exit(0)

        #duplicate packets, ack but don't add to data to be printed
        if decoded['sequence'] in packets:
          ackseq = decoded['sequence'] + len(decoded['data'])
          msg = json.dumps({"ack": ackseq})
          if sock.sendto(msg.encode('ascii'), addr) < len(msg):
            log("[error] unable to fully send packet")
          log("[duplicate]: " + str(decoded["sequence"]))
          log("ABOUT TO SEND " + msg)

        # If there is new data, we accept it and hold on to packet
        else:
          if decoded['data']:
            packets[decoded['sequence']] = decoded
            seq.append(decoded['sequence'])
            log(f"[recv data] {decoded['sequence']} ({len(decoded['data'])}) ACCEPTED")
            #calculate ack
            ackseq = decoded['sequence'] + len(decoded['data'])

          # Send back an ack to the sender
          msg = json.dumps({"ack": ackseq})
          log("ABOUT TO SEND " + msg)
          if sock.sendto(msg.encode('ascii'), addr) < len(msg):
            log("[error] unable to fully send packet")
      except (ValueError, KeyError, TypeError) as exc:
        log("[recv corrupt packet]")
        raise exc
    else:
      log("[error] timeout")
      sys.exit(-1)

if __name__ == '__main__':
  main()
