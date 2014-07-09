Manual for the XPath like notation for YAML
===========================================
Reaching a dictionary entry
---------------------------

  When expecting a dictionary, a word or a letter return the dictionary entry to the corresponding key, whatever the value is (another dictionary, a list, a single value, ...).
  
    {a: {b: {c: 3}}}
    a/b/c = 3
  
Reaching a list entry
---------------------

  A number return the value at the zero-indexed position of the list, whatever the value is.

    [[0, [1,2,3], 4], 5] 
    0/1/2 = 3

Ranges
------

  When expecting a list, it is possible to select a range, which return a new list with included elements. The notation is as follow:
  
  included_start_number..excluded_end_number

  If start_number is left empty, it is implied to be 0. If the last is empty, it is implied to be the length of the list.

    {a: 1, b: [a,b,c,d,e,f]}
    b/1..3 = [b,c]
    b/..3 = [a,b,c]
    b/3.. = [d,e,f]
    b/.. = [a,b,c,d,e,f] -> useless, since b return the entire list anyway

Dictionary selection
--------------------

  When expecting a dictionary, it is possible to select a list of values from keys. It returns, as expected, a list. The notation use a ',' between each selected values.

    g: {a: 1, b: 3, c: 4}
    g/a,c = [1,4]

List picking
------------

  When expecting a list, it is possible to return a list from a selection on all element of the list. There is two case:
   
* You want to access a dictionary entry:
    
Just use the key or dictionary selection.
        
    g: [{a: 2, b: 3}, {a: 4, b: 5}]
    g/a = [2, 4]
    g/a,b = [[2,3], [4,5]]
    
* You want to access a list with range or index:
    
Prefix with '!'.
        
    g: [[0,1,2,3,4], [5,6,7,8,9]] 
    g/!2 = [2, 7]
    g/!1..4 = [[1,2,3], [6,7,8]]
