#!/usr/bin/python

import yaml

while True:
  content = yaml.load(raw_input()) 
  content["event"]["x"] = content["event"]["x"] + 2
  print(yaml.dump(content).strip())
