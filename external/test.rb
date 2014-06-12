#!/usr/bin/ruby
$stdout.sync = true
require 'yaml'

while true
  c = YAML.load(gets.chomp)
  c["event"]["x"] = c["event"]["x"] + 10 
  puts YAML.dump(c)
end
