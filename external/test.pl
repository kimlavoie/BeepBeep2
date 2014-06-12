#!/usr/bin/perl

use YAML;

$| = 1;

while(1){
  $input = <>;
  $content = Load($input);
  print Dump($content);
}
