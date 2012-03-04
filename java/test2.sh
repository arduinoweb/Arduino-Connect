#!/bin/bash

x=1

while [ 1 ]
do

 let x=x+1


 if [ $x -eq 256 ]
 then
   let x=0
 fi

 echo "W V 2 "$x" E" | nc 192.168.141.131 10003 
 
done
