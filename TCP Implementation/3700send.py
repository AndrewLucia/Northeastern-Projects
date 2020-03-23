#!/usr/bin/env python3

import sys
import socket
import datetime
import json

MSG_SIZE = 1500
DATA_SIZE = 1472
TIMEOUT = 1
s_wnd = 1

def log(string):
  sys.stderr.write(datetime.datetime.now().strftime("%H:%M:%S.%f") + " " + string + "\n")
  sys.stderr.flush()

#resend packets until they are acked
def resend(sock, dest, packets):
  global s_wnd
  log("RESENDING")
  loop = False
  resendcount = 1
  for seqn in packets:
    if packets[seqn]['ack'] == False:
      loop = True

  received_acks = {}

  while loop:
    sent = 0
    #resend all unacked packets from last sent window
    for seqn in packets:
      if packets[seqn]['ack'] == False:
        sent += 1
        msg = packets[seqn]
        log('resending: ' + str(resendcount))
        for i in range(resendcount):
          sock.sendto(json.dumps(msg).encode('ascii'), dest)
          log("[resend data] " + str(seqn) + " (" + str(len(msg["data"])) + ")")

    #receive acks until timeout or all are received
    for i in range(resendcount * sent):
      try:
        result = sock.recvfrom(MSG_SIZE)
        if result:
          (data, _addr) = result
          data = data.decode('ascii')

          decoded = json.loads(data)
          received_acks[decoded['ack']] = decoded
      except (socket.timeout):
        break

    #mark which packets were acked
    for sqn in packets:
      if sqn + len(packets[sqn]['data']) in received_acks:
        packets[sqn]['ack'] = True
        s_wnd += 1

    #break out of resend if all packets have been acked
    loop = False
    for sqn in packets:
      if packets[sqn]['ack'] == False:
        loop = True
    resendcount *= 3

#send next window of packets
def send_next_packets(start_seqn, sock, dest, packets):

  seqn = start_seqn
  for i in range(s_wnd):
    msg = {"sequence": seqn, "data": "", "ack": False, "eof": False}
    overhead = len(json.dumps(msg))
    msg["data"] = sys.stdin.read(DATA_SIZE - overhead)
    if len(msg["data"]) > 0:
      assert (len(msg) <= DATA_SIZE), f"ERROR: Datagram is longer ({len(msg)}) than {DATA_SIZE} bytes!!!"

      if sock.sendto(json.dumps(msg).encode('ascii'), dest) < len(msg):
        log("[error] unable to fully send packet")
      else:
        log("[send data] " + str(seqn) + " (" + str(len(msg["data"])) + ")")
        packets[seqn] = msg
        seqn += len(msg["data"])

  return seqn

#handle sending the EOF packet
#special case as the ack could be dropped and the receiver closed, creating infinite loop of sending
def finish(sock, dest, seqn):
  finmsg = {"eof": True, "data": "", "sequence": seqn, "ack": False}
  loop = True
  resendcount = 1

  while loop:
    log('finish')

    #limit sending to 13 tries
    if resendcount > 9:
      log('assume eof was received')
      break
    for i in range(resendcount):
      sock.sendto(json.dumps(finmsg).encode('ascii'), dest)

    for i in range(resendcount):
      try:
        result = sock.recvfrom(MSG_SIZE)
        if result:
          (data, _addr) = result
          data = data.decode('ascii')

          decoded = json.loads(data)

          if decoded['ack'] == seqn:
            loop = False
            break
      except (socket.timeout):
        break
    resendcount *= 3



def main():
  # Bind to localhost and an ephemeral port
  ip_port = sys.argv[1]
  udp_ip = ip_port[0:ip_port.find(":")]
  udp_port = int(ip_port[ip_port.find(":")+1:])
  dest = (udp_ip, udp_port)
  seqn = 0
  global s_wnd

  # Set up the socket
  sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
  sock.settimeout(TIMEOUT)

  # Send first packet

  loop = True

  # Now read in data, send packets
  while loop:
    packets = {}
    seqn = send_next_packets(seqn, sock, dest, packets)
    log("ABOUT TO SLEEP")

    packets_sent = len(packets.keys())
    received_acks = {}

    #prepare to receive acks for the number of packets sent
    for i in range(packets_sent):

      try:
        result = sock.recvfrom(MSG_SIZE)
      except (socket.timeout):
        break
      if result:
        (data, _addr) = result
        data = data.decode('ascii')

        decoded = json.loads(data)
        received_acks[decoded['ack']] = decoded

      else:
        log("[error] timeout")
        sys.exit(-1)


    log(f"(seqn {seqn})")
    for sqn in packets:
      if sqn + len(packets[sqn]['data']) in received_acks:
        packets[sqn]['ack'] = True
        s_wnd += 1

    resend(sock, dest, packets)

    #no more data to send, exit
    if packets_sent == 0:
      break

  finish(sock, dest, seqn)
  sys.exit(0)

if __name__ == '__main__':
  main()