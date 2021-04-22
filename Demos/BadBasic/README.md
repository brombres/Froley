# Bad Basic
A Rogue-based Froley compiler and executor (AKA interpreter) for [Murphy McCauley's](https://github.com/MurphyMc/) "Bad Basic" language (as of April 20, 2021).

To build, install & build [Rogue](https://github.com/AbePralle/Rogue) and Froley (this repo), then CD to `Demos/BadBasic` and type `rogo ex` to see a list of example programs to run. Here's the output of each example:

```
~/Projects/Froley/Demos/BadBasic> rogo ex
Run 'rogo ex #' where '#' is one of the following:
   1 - Examples/expr2.txt
   2 - Examples/fib.txt
   3 - Examples/hello_name.txt
   4 - Examples/list.txt
   5 - Examples/maze_for.txt

~/Projects/Froley/Demos/BadBasic> rogo ex 1
-----------------------------------------Examples/expr2.txt-----------------------------------------
print 100 + 50 // 2 + " is 125"
print 100 // 2 + 50 + " is 100"
print 10000 // (48+2) // 50 + " is 4"
print 100 // 10 // 2 + " is 5"

-----------------------------------------------Output-----------------------------------------------
> Build/BadBasic-macOS Examples/expr2.txt
125 is 125
100 is 100
4 is 4
5 is 5

~/Projects/Froley/Demos/BadBasic> rogo ex 2
------------------------------------------Examples/fib.txt------------------------------------------
print "Print out some Fibonacci numbers"
let a = 1
let b = 1
while a < 500
  print a
  print b
  let a = a + b
  let b = a + b
endwhile
print "done"

-----------------------------------------------Output-----------------------------------------------
> Build/BadBasic-macOS Examples/fib.txt
Print out some Fibonacci numbers
1
1
2
3
5
8
13
21
34
55
89
144
233
377
done

~/Projects/Froley/Demos/BadBasic> rogo ex 3
--------------------------------------Examples/hello_name.txt---------------------------------------
echo "What's your name? "
let name = input()
print "Hello, " + name + "!"
echo "Do you like dogs (y/N)? "
likedogs = input()
if likedogs == "y"
  echo "How many dogs would you like? "
  num_dogs = int(input())
  dogs_so_far = 0
  while dogs_so_far < num_dogs
    dogs_so_far = dogs_so_far + 1
    print "Dog number " + dogs_so_far
  endwhile
  print "There's your " + num_dogs + " dogs.  Enjoy!"
else
  print "Okay.  No dogs for you!"
endif

-----------------------------------------------Output-----------------------------------------------
> Build/BadBasic-macOS Examples/hello_name.txt
What's your name? Abe
Hello, Abe!
Do you like dogs (y/N)? y
How many dogs would you like? 2
Dog number 1
Dog number 2
There's your 2 dogs.  Enjoy!

~/Projects/Froley/Demos/BadBasic> rogo ex 4
-----------------------------------------Examples/list.txt------------------------------------------
l = list(11,22,33,44,55,66)

for i = 0 to len(l)-1
  l[i] = l[i] * 10
next

print l


-----------------------------------------------Output-----------------------------------------------
> Build/BadBasic-macOS Examples/list.txt
[110,220,330,440,550,660]

~/Projects/Froley/Demos/BadBasic> rogo ex 5
---------------------------------------Examples/maze_for.txt----------------------------------------
print "On a Mac, try changing your Terminal font to Monaco, 13 point,"
print "and then setting the Character Spacing to 0.77 and the Line"
print "Spacing to 0.56."
for h = 1 to 20
  for w = 1 to 78
    # Pick a random 0 or 1
    let r = randint(0, 1)
    if r
      echo "/"
    else
      echo "\"
    endif
  endfor
  print
endfor

-----------------------------------------------Output-----------------------------------------------
> Build/BadBasic-macOS Examples/maze_for.txt
On a Mac, try changing your Terminal font to Monaco, 13 point,
and then setting the Character Spacing to 0.77 and the Line
Spacing to 0.56.
\//\\/\\///\/\//\\/\////\\/\/\/////\///\\\/////\\\\/\//\\\\////\\\\\//\/\\/\\/
\\/\\\\/\/\\\//\/\/\\\/\/\//\//////\\\\\\/\/\\/\/\//\\\\//\/\/\\/\\\//\///////
\/\/\/\\/\\///\////\\\//\/\//\///////\\\\\//\///\////\//////\/\\\/\\\//\/\/\/\
\///\/\//\\\\\/\/\\/\\//\///\//\\\\/\///\/\\/\\\\////\\\/\/\\//\\\/\/\\/\\\\//
//\/\\////\\\\\\\\\\\\/\\///\\\//\\\\/\//////\\\\////\\///\////\\/\/\////\//\/
\/\\\/\\//\\/////\\\////\/////\\\///\///\//\\///\/\/\\////\\//\/\\/\\/\//\\\\/
\\\\\\\\/\\\///\\\\\\/\\/\/\//\/\/\\\/\\\\/\/\///\/\/\//\///\/\//\\\\\//\\//\/
\\\//\//\\///\\\\///\////\/\\\\\/\/\\\\\\///\\\\/\\\\\/\//\/\\//\/\/\/\\\/\\\/
\/////\/\\\\////\\//\////\\/\///\/\\//\\/\/\\////////\\\/\//\//\\\\////\\\/\//
\/\\\\////\/\\/\//\/\//\/\//\\/\/\\///\/\/\\\/\\////\/\/\\///\//\\/\/\\///\/\\
/\/\/\/\//\\\///\///\/\/\/\\\/\//\/\//\\/\//\//\\/\//\/\\//\/\/////\//\/\/\/\\
\/\\///\//\////\\///\\/\/\\\/\\\/////\///\//\/\/\/\/\\//\\/\\/\/\/\/\\\\\\\\\\
\/\///\//\//\/////\///\/\\///////\/\/\/\//\\/\\\/\\\\\////\\///\\\//\/\/////\\
/\\\/\////\\\\////\///\/\\\\\\\\/\//\/\\\\/\//\\////\\\//\//\/\/\\\///\\/\\//\
\\///\/\\\\/\\/\\\\//\/\\/\\\/\/\\//\\/\\//\//\//\\/\\/\\\/\\\/\////\/\/\/\\/\
\/\\/\\/\\//\\/\///\\\\//\//\//\\/\////\//////\\/\//\\////\\\////\\/////\/////
\/\/\/\////\\/\\\\/\\//\//\\\\\/\///\\/\/\\\/\//////\//\//\///\/////\/\\\//\\\
/\/\\\/\\\/////////\\\/////\\\\///\//\//\\\\\/\//\\\/\////\\\\/\/\\///\\\\\/\/
\\//\\///\\//\\/\/\//\\/\\///\\/\//\/\/\\\//\\/\\//\\///\//\\/\\////\\\//\/\\\
\/\/\\///\//\//\\/\/////\/\\\//\//\\//\\/\\//\\/\\\//\\/\/\////\//\\//\\\\/\\\
```

# Development Process
If you run `rogo xclean` it will delete all the Froley-generated `.rogue` files in the `Source/` folder except for `BadBasic.rogue`, which has a bit of custom code versus the originally-generated version. The `Source/` folder will then contain [`BadBasic.froley`](Source/BadBasic.froley), [`BadBasic.rogue`](Source/BadBasic.rogue), and [`Execute.rogue`](Source/Execute.rogue) - the latter contains the execution logic for the BadBasic runtime. You can then re-run `rogo ex` to have Froley regenerate all the necessary source files to tokenize and parse Bad Basic programs. 

Here's a guide to how this Froley-based Bad Basic interpreter was developed from scratch that can assist in developing custom compilers.

## 1. Create a Rogo-based project
```
mkdir BadBasic
cd BadBasic
rogo --create --project=BadBasic
rm Source/BadBasic.rogue  # delete this so Froley will generate a BadBasic.rogue of its own
```

## 2. Add a Froley compile step to `Build.rogue`.
Edit `Build.rogue` and adjust the code around the `rogo_build` routine to be as follows:

```
routine rogo_build
  rogo_froley # NEW ADDITION
  ...
endRoutine

# NEW CODE BELOW
routine rogo_froley( rebuild=false:Logical )
  # Recompiles the .froley language definition if necessary
  local def = File( "Source/BadBasic.froley" )
  if (def.is_newer_than("Source/TokenizerVM.rogue") or rebuild)
    println "Compiling Bad Basic language parser"
    execute "froley $ --target=rogue --main --output=Source" (def.esc)
  endIf
endRoutine
```

## 3. Implement Tokenizer Logic in `Source/BadBasic.froley`
Edit `Source/BadBasic.froley` and add the token definitions and tokenizer logic. This is everything in [`BadBasic.froley`](Source/BadBasic.froley) up to and not including the `[parser]` section.

## 4. Test the Parser
Run `rogo build && Build/BadBasic-macOS Examples/fib.txt` (replace the macOS executable with the one on your system). The compiler (tokenzer) will print out:

```
print
Print out some Fibonacci numbers
EOL
let
a
=
1
EOL
let
b
=
1
EOL
while
a
<
500
EOL
print
a
EOL
print
b
EOL
let
a
=
a
+
b
EOL
let
b
=
a
+
b
EOL
endwhile
EOL
print
done
EOL
```

## 5. Add Parser Logic to `Source/BadBasic.froley`
Edit `Source/BadBasic.froley` and add the parser section of [`BadBasic.froley`](Source/BadBasic.froley) (your version should now contain the entire content of the file).

## 6. Test the Parser
Start by deleting your `Source/BadBasic.rogue`. In Step 4 Froley created it to only handle tokenization and Froley will not overwrite it. When you delete it, Froley will recreate it to handle both tokenization and parsing.

```
rm Source/BadBasic.rogue
rogo build && Build/BadBasic-macOS Examples/fib.txt
```

The parser will print out:

```
[Print(Print out some Fibonacci numbers),Assign(a,1),Assign(b,1),While((a < 500),[Print(a),Print(b),Assign(a,(a + b)),Assign(b,(a + b))]),Print(done)]
```

## 7. Add Execution Logical
Add [`Execute.rogue`](Source/Execute.rogue) to your `Source/` folder to provide execution logic, then modify `Source/BadBasic.rogue` to incorporate it:

```
module BadBasic

$include "Execute.rogue"  # ADD THIS LINE
$include "Parser.rogue"

...
            while (parser.has_another)
              local cmd = parser.parse( parser.methods[0] )
              #println cmd    # REMOVE THIS LINE
              if (cmd) cmd()  # ADD THIS LINE
            endWhile
...
              while (parser.has_another)
                local cmd = parser.parse( parser.methods[0] )
                #println cmd    # REMOVE THIS LINE
                if (cmd) cmd()  # ADD THIS LINE
              endWhile
...
```

That's it!

