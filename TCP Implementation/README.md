These two files represent a sender and receiver utilizing a simple implementation of TCP in Python. The sender will send a file of X size
in packets of 1500 bytes. The receiver then sends acknowledgements to the sender if it has received a sent packet. This implementation was
built to work on unreliable networks that may drop, reorder, or corrupt packets, and have high latency.
