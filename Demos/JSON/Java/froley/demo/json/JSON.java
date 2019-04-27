package froley.demo.json;

public class JSON
{
  /*
  METHODS
  method init
    # Parse input or file by repeatedly calling first method defined in parser.
    local parser = Parser()
    if (System.command_line_arguments.count)
      try
        local args = System.command_line_arguments.join( " " )
        if (File.exists(args))
          # Parse contents of file
          parser.open( File(args) )
        else
          parser.open( "[Command Line]", args )
        endIf

        if (not parser.methods.is_empty)
          while (parser.has_another)
            local cmd = parser.parse( parser.methods[0] )
            println cmd
          endWhile
        endIf
      catch (err:Error)
        Console.error.println err
      endTry

    else
      # Interactive mode
      loop
        try
          local input = Console.input( "> " )
          parser.open( "[Command Line]", input )
          if (not parser.methods.is_empty)
            while (parser.has_another)
              local cmd = parser.parse( parser.methods[0] )
              println cmd
            endWhile
          endIf
        catch (err:Error)
          Console.error.println err
        endTry
      endLoop
    endIf
    */
}


