use YAML;

while(1){
  $input = <>;
  $content = Load($input);
  print Dump($content);
}
