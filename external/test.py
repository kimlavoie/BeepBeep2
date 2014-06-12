#!/usr/bin/python

import yaml

i = 2

while True:
  content = yaml.load(raw_input()) 
  content["event"]["x"] = content["event"]["x"] + i
  i += 1
  print(yaml.dump(content))
