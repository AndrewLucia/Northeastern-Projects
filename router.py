#!/usr/bin/env python3
""" Skeleton Implementation of Project 2 for NEU CS3700 """

import argparse
import json
import select
import socket
import ipaddress

##########################################################################################

# Message Fields
TYPE = "type"
SRCE = "src"
DEST = "dst"
MESG = "msg"
TABL = "table"

# Message Types
DATA = "data"
DUMP = "dump"
UPDT = "update"
RVKE = "revoke"
NRTE = "no route"

# Update Message Fields
NTWK = "network"
NMSK = "netmask"
ORIG = "origin"
LPRF = "localpref"
APTH = "ASPath"
SORG = "selfOrigin"

# internal route info
CUST = "cust"
PEER = "peer"
PROV = "prov"


##########################################################################################

class Router:
  """ Your Router """
  def __init__(self, networks):
    self.asn = networks.pop(0)
    self.routes = {} #forwarding table [ip: list of content of update packets]
    self.updates = {} #store announcements [ip: list of update packets]
    self.relations = {} #cust, peer, etc. [ip: relationship to us]
    self.sockets = {}
    for relationship in networks:
      network, relation = relationship.split("-")
      self.sockets[network] = socket.socket(socket.AF_UNIX, socket.SOCK_SEQPACKET)
      self.sockets[network].setblocking(0)
      self.sockets[network].connect(network)
      self.relations[network] = relation

  def convert_bin_to_ip(self, bin): #converts a binary string to a valid ip address
    oct1 = bin[0: 8]
    oct2 = bin[8:16]
    oct3 = bin[16:24]
    oct4 = bin[24:32]

    str1 = str(int(oct1, 2))
    str2 = str(int(oct2, 2))
    str3 = str(int(oct3, 2))
    str4 = str(int(oct4, 2))

    return str1 + '.' + str2 + '.' + str3 + '.' + str4

  def convert_ip_to_bin(self, ip): #converts a valid ip address to a binary string
    octets = ip.split('.')
    ipbin = ''
    for bit in octets:
      ipbin += format(int(bit), '08b')

    return ipbin

  #determines if two routes have the same attributes (local pref, origin, self origin, APAth, etc.)
  def sameAttributes(self, route1, route2):
    if route1[ORIG] == route2[ORIG] and route1[SORG] == route2[SORG] and route1[APTH] == route2[APTH] and route1[LPRF] == route2[LPRF]:
      return True

    return False

  #determines if two network routes are the same up to the last masked bit
  def sameToOneBit(self, net1, mask1, net2, mask2):
    netobj1 = ipaddress.ip_network(net1+ "/" + mask1)
    netobj2 = ipaddress.ip_network(net2 + "/" + mask2)
    prelen1 = netobj1.prefixlen
    prelen2 = netobj2.prefixlen
    netbin1 = self.convert_ip_to_bin(net1)
    netbin2 = self.convert_ip_to_bin(net2)

    matching_bits = 0

    for bit in range(0, max(prelen1, prelen2)):
      if netbin1[bit] == netbin2[bit]:
         matching_bits += 1
      else:
        break

    return matching_bits == (max(prelen1, prelen2) - 1)

  #determines if a given ip address is a valid route for a given network
  def is_possible_route(self, ip, network, netmask):

    netobj = ipaddress.ip_network(network + "/" + netmask)
    prefixlength = netobj.prefixlen

    netbin = self.convert_ip_to_bin(network)
    ipbin = self.convert_ip_to_bin(ip)

    matching_bits = 0
    for bit in range(0, prefixlength):
      if netbin[bit] == ipbin[bit]:
        matching_bits += 1
      else:
        break

    return matching_bits == prefixlength

  def get_shortest_as_path(self, pos_routes):
    """ select the route with the shortest AS Path """
    # Max size of route below
    size = 999;
    outroutes = []

    for route in pos_routes:
      for route_info in self.routes[route[0]]:
        if route[1] == route_info[NTWK] and route[2] == route_info[NMSK] and len(route_info[APTH]) < size:
          size = len(route_info[APTH])

    for route in pos_routes:
      for route_info in self.routes[route[0]]:
        if route[1] == route_info[NTWK] and route[2] == route_info[NMSK] and len(route_info[APTH]) == size:
          outroutes.append(route)

    return outroutes

  def get_highest_preference(self, pos_routes):
    """ select the route with highest preference """
    #pos_routes is list of tuples so [(router ip, network, netmask), (router ip, network, netmask),...]
    #tuple structure is (router ip, network, netmask)
    # routes is all possible routes to daddr
    outroutes = []
    highest = 0

    for route in pos_routes:
      routerip = route[0]
      network = route[1]
      netmask = route[2]
      for route_info in self.routes[routerip]:
        if network == route_info[NTWK] and netmask == route_info[NMSK] and route_info[LPRF] > highest:
          highest = route_info[LPRF]

    for route in pos_routes:
      routerip = route[0]
      network = route[1]
      netmask = route[2]
      for route_info in self.routes[routerip]:
        if network == route_info[NTWK] and netmask == route_info[NMSK] and route_info[LPRF] == highest:
          outroutes.append(route)

    return outroutes

  def get_self_origin(self, pos_routes):
    """ select self originating routes """
    # TODO
    outroutes = []

    for route in pos_routes:
      for route_info in self.routes[route[0]]:
        if route[1] == route_info[NTWK] and route[2] == route_info[NMSK] and route_info[SORG]:
          outroutes.append(route)

    if not outroutes:  #self origin was false for everyone
      return pos_routes

    return outroutes

  def get_origin_routes(self, pos_routes):
    """ select origin routes:  IGP > EGP > UNK """
    # TODO
    outroutes = []

    for route in pos_routes:
      for route_info in self.routes[route[0]]:
        if route[1] == route_info[NTWK] and route[2] == route_info[NMSK] and route_info[ORIG] == "IGP":
          outroutes.append(route)

    if len(outroutes) == 0:
      for route in pos_routes:
        for route_info in self.routes[route[0]]:
          if route[1] == route_info[NTWK] and route[2] == route_info[NMSK] and route_info[ORIG] == "EGP":
            outroutes.append(route)

    # If no IGP nor EGP were found return rest of routes bc UNK
    if len(outroutes) == 0:
      return pos_routes

    return outroutes

  def filter_relationships(self, srcip, routes):
    """ Don't allow Peer->Peer, Peer->Prov, or Prov->Peer forwards """
    outroutes = []

    # if route is from a customer then add all routes
    if self.relations[srcip] == CUST:
      outroutes = routes
    # if route is not from customer but is going to a customer add that route
    else:
      for route in routes:
        if self.relations[route[0]] == CUST:
          outroutes.append(route)

    return outroutes

  #returns a list of routes who have the longest prefix match with a given destination address
  def get_longest_prefix_match(self, daddr):
    longest_matches = []
    longest_matches2 = []

    longest_match = 0
    for router_ip in self.routes: #ips
      for route in self.routes[router_ip]:
        network = route[NTWK]
        netmask = route[NMSK]
        if self.is_possible_route(daddr, network, netmask):
          netobj = ipaddress.ip_network(network + "/" + netmask)
          prefixlength = netobj.prefixlen
          if prefixlength >= longest_match:
            longest_match = prefixlength
            longest_matches.append((router_ip, network, netmask))

    for route in longest_matches: #tuples
      netobj = ipaddress.ip_network(route[1] + "/" + route[2])
      prefixlength = netobj.prefixlen
      if prefixlength == longest_match:
        longest_matches2.append(route)


    return longest_matches2

  #gets the lowest ip address of the given possible routes
  def get_lowest_ip(self, pos_routes):
    outroutes = []

    lowestip = '255.255.255.255'
    for route in pos_routes:
      routerip = route[0]
      routeripbin = self.convert_ip_to_bin(routerip)
      lowestipbin = self.convert_ip_to_bin(lowestip)
      for bit in range(0, len(routeripbin)):
        if routeripbin[bit] == '0' and lowestipbin[bit] == '1':
          lowestip = routerip
          break
        elif routeripbin[bit] == '1' and lowestip[bit] == '0':
          break
        else:
          continue

    for route in pos_routes:
      if route[0] == lowestip:
        outroutes.append(route)

    return outroutes

  #returns the best route for a given packet to travel on to its destination address
  def get_route(self, srcip, daddr): #destination address of this packet
    """ Select the best route for a given address """
    # TODO fill in peer?

    #pos_routes is list of tuples
    #determine if there are any routes
    pos_routes = self.get_longest_prefix_match(daddr)

    if not pos_routes: #no possible routes, return None
      return None

    # Rules go here
    if pos_routes:
      # 1. Highest Preference
      pos_routes = self.get_highest_preference(pos_routes)
      # 2. Self Origin
      pos_routes = self.get_self_origin(pos_routes)
      # 3. Shortest ASPath
      pos_routes = self.get_shortest_as_path(pos_routes)
      # 4. EGP > IGP > UNK
      pos_routes = self.get_origin_routes(pos_routes)
      # 5. Lowest IP Address
      pos_routes = self.get_lowest_ip(pos_routes)

      # Final check: enforce peering relationships
      pos_routes = self.filter_relationships(srcip, pos_routes)

    # peer = pos_routes[0][0]


    if not pos_routes: #no possible routes, return None
      return None

    for key in self.sockets.keys():
      if key == pos_routes[0][0]:
        return self.sockets[key]

    return None;

  #forwards a packet based on the best route to take
  def forward(self, srcip, packet): #gets entire packet and srcip of that packet
    """ Forward a data packet """
    # get route to send packet
    best_route = self.get_route(srcip, packet[DEST]) #is a socket
    sock = best_route

    if sock == None:
      self.send_error(self.sockets[srcip], packet, srcip)
      return;

    jsonpack = json.dumps(packet)
    sock.sendall(jsonpack.encode())
    return True

  #Coalesces a route by removing the last bit and shortening the netmask
  def coalesceRoute(self, route):
    network = route[NTWK]
    netmask = route[NMSK]
    netobj = ipaddress.ip_network(network + "/" + netmask)
    prelen = netobj.prefixlen
    netbin = self.convert_ip_to_bin(network)
    maskbin = self.convert_ip_to_bin(netmask)

    net_list = list(netbin)
    net_list[prelen - 1] = '0'
    netbin = ''.join(net_list)
    route[NTWK] = self.convert_bin_to_ip(netbin)

    mask_list = list(maskbin)
    mask_list[prelen - 1] = '0'
    maskbin = ''.join(mask_list)
    route[NMSK] = self.convert_bin_to_ip(maskbin)

    return route

  #determines which of the two given routes has a longer netmask
  def longerprefix(self,route1, route2):
    netobj1 = ipaddress.ip_network(route1[NTWK] + "/" + route1[NMSK])
    netobj2 = ipaddress.ip_network(route2[NTWK] + "/" + route2[NMSK])
    prelen1 = netobj1.prefixlen
    prelen2 = netobj2.prefixlen

    if prelen1 >= prelen2:
      return route1

    return route2

  #coalesces the routing table by checking if each router has a coalescable pair of routes and calls coalesceRoute to
  #coalesce them
  def coalesce(self):
    """ coalesce any routes that are right next to each other """
    # TODO (this is the most difficult task, save until last)

    for routerip in self.routes:
      for i in range(len(self.routes[routerip])-1):
        route1 = self.routes[routerip][i]
        for j in range(i+1, len(self.routes[routerip])):
          route2 = self.routes[routerip][j]
          if not (route1 == None) and not (route2 == None) and self.sameToOneBit(route1[NTWK], route1[NMSK], route2[NTWK], route2[NMSK]) and self.sameAttributes(route1, route2):
            if self.longerprefix(route1, route2) == route1:
              self.routes[routerip][i] = self.coalesceRoute(route1)
              self.routes[routerip][j] = None
            else:
              self.routes[routerip][j] = self.coalesceRoute(route2)
              self.routes[routerip][i] = None

    #keep all the coalesced non-None routes
    for routerip in self.routes:
      keeplist = []
      for route in self.routes[routerip]:
        if not (route == None):
          keeplist.append(route)
      self.routes[routerip] = keeplist

    return True

  #determine if a given route is in the updates of a given router
  def searchUpdates(self, route, routerip):
    for packet in self.updates[routerip]:
      if packet[TYPE] == UPDT:
        routeinfo = packet[MESG]
        if self.is_possible_route(routeinfo[NTWK], route[NTWK], route[NMSK]):
          return True
    return False

  #determine if a given route is in the forwarding table for a given router
  def searchRoutes(self, route, routerip):

    for routeinfo in self.routes[routerip]:
      if self.is_possible_route(routeinfo[NTWK], route[NTWK], route[NMSK]):
        return True
    return False

  def update(self, srcip, packet):
    """ handle update packets """

    # TODO save copy of announcement
    if not(srcip in self.updates.keys()):
      self.updates[srcip] = []

    self.updates[srcip].append(packet) #entire packet


    # add entry to forwarding table
    path = packet[MESG][APTH]
    path.append(self.asn)
    packet[MESG][APTH] = path


    msg = dict(packet[MESG])

    if not(srcip in self.routes):
      self.routes[srcip] = []

    self.routes[srcip].append(msg)

    self.coalesce()

    # decide if send copies of announcement to neighboring routers
    for ip in self.sockets.keys():
      if not(ip == srcip):
        #get the .1 version of the ip i'm sending update to
        #ip i'm sending to is ip
        dest_ip_port = list(ip)
        dest_ip_port[len(ip) - 1] = '1'
        packet['src'] = ''.join(dest_ip_port)
        packet['dst'] = ip

        jsonpack = json.dumps(packet)
        # if receiving message from peer or provider then only send to customer
        if self.relations[srcip] == PEER or self.relations[srcip] == PROV:
          if self.relations[ip] == CUST:
            self.sockets[ip].sendall(jsonpack.encode())
        # if receiving from customer send to all neighbors
        else:
          self.sockets[ip].sendall(jsonpack.encode())
    # TODO
    return True

  #dissaggregates the routing for a single router and rebuilds it based on updates and revokes
  def rebuild(self, routerip):
    routes = []
    routes_after_revoke =[]

    for packet in self.updates[routerip]:
      if packet[TYPE] == UPDT:
        routes.append(packet[MESG])

    for packet in self.updates[routerip]:
      if packet[TYPE] == RVKE:
        revokes = packet[MESG]
        for revoke_route in revokes: #dictionary
          for i in range(0, len(routes)):
            route = routes[i]
            if not (route == None) and self.is_possible_route(route[NTWK], revoke_route[NTWK], revoke_route[NMSK]):
              routes[i] = None

    for route in routes:
      if not (route == None):
        routes_after_revoke.append(route)

    return routes_after_revoke

  #determines if a given specific network is in a larger network
  def routeInNetwork(self, smallerroute, largerroute):
    netobj1 = ipaddress.ip_network(smallerroute[NTWK] + "/" + smallerroute[NMSK])
    netobj2 = ipaddress.ip_network(largerroute[NTWK] + "/" + largerroute[NMSK])
    prelen2 = netobj2.prefixlen

    netbin1 = self.convert_ip_to_bin(smallerroute[NTWK])
    netbin2 = self.convert_ip_to_bin(largerroute[NTWK])

    matching_bits = 0
    for bit in range(0, prelen2):
      if netbin1[bit] == netbin2[bit]:
        matching_bits += 1
      else:
        break

    return matching_bits == prelen2

  def revoke(self, packet):
    """ handle revoke packets """

    srcip = packet[SRCE]
    routes_to_revoke = packet[MESG]
    # add revoke message to updates
    self.updates[srcip].append(packet)  # entire packet

    # Deaggregate
    self.routes[srcip] = self.rebuild(srcip)

    self.coalesce()

    # check to make sure other targets are available otherwise remove srcip
    if len(self.routes[srcip]) == 0:
      del self.routes[srcip]


    # decide if send copies of announcement to neighboring routers
    for ip in self.sockets.keys():
      if not (ip == srcip):
        # get the .1 version of the ip i'm sending update to
        # ip i'm sending to is ip
        dest_ip_port = list(ip)
        dest_ip_port[len(ip) - 1] = '1'
        packet['src'] = ''.join(dest_ip_port)
        packet['dst'] = ip

        jsonpack = json.dumps(packet)
        # if receiving message from peer or provider then only send to customer
        if self.relations[srcip] == PEER or self.relations[srcip] == PROV:
          if self.relations[ip] == CUST:
            self.sockets[ip].sendall(jsonpack.encode())
        # if receiving from customer send to all neighbors
        else:
          self.sockets[ip].sendall(jsonpack.encode())

    return True

  def dump(self, packet):
    """ handles dump table requests """
    # TODO
    packet['type'] = "table"
    src = packet['src']
    packet['src'] = packet['dst']
    packet['dst'] = src

    table_list = []

    for ip in self.routes:
      for route in self.routes[ip]:
        entry = {'network' : route[NTWK], 'netmask' : route[NMSK], 'peer' : ip}
        table_list.append(entry)

    packet[MESG] = table_list
    msg = json.dumps(packet)

    sock = self.sockets[src]
    sock.sendall(msg.encode())
    return True

  def handle_packet(self, srcip, packet):
    """ dispatches a packet """
    type = packet['type']
    if type == 'update':
      self.update(srcip, packet)
    elif type == 'dump':
      self.dump(packet)
    elif type == 'data':
      # check if a route is in forwarding table
      # if so forward
      self.forward(srcip,packet)
    elif type == 'revoke':
      self.revoke(packet)

    return True

  def send_error(self, conn, packet, neighbor_ip):
    """ Send a no_route error message """
    # dst ip becomes src ip to return the message
    # src ip becomes this ip
    # type becomes "no route"

    orig_ip = packet[SRCE]
    ip = neighbor_ip
    dest_ip_port = list(ip)
    dest_ip_port[len(ip) - 1] = '1'
    packet['src'] = ''.join(dest_ip_port)
    packet['dst'] = orig_ip

    packet['type'] = "no route"
    # msg is empty
    packet[MESG] = {}

    # send from port incoming...current dst ip?
    msg = json.dumps(packet)
    # sock = self.sockets[src]
    conn.sendall(msg.encode())

    return

  def run(self):
    """ main loop for the router """
    while True:
      socks = select.select(self.sockets.values(), [], [], 0.1)[0]
      for conn in socks:
        try:
          k = conn.recv(65535)
        except:
          # either died on a connection reset, or was SIGTERM's by parent
          return
        if k:
          for sock in self.sockets:
            if self.sockets[sock] == conn:
              srcip = sock
          msg = json.loads(k)
          if not self.handle_packet(srcip, msg):
            return False;


        else:
          return

if __name__ == "__main__":
  PARSER = argparse.ArgumentParser(description='route packets')
  PARSER.add_argument('networks', type=str, nargs='+', help="networks")
  args = PARSER.parse_args()
  networks = args.networks
  router = Router(args.networks).run()   #custruct router object and call run method
