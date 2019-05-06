package froley.demo.json;

public class JSON
{
  // GLOBAL METHODS
  static public void main( String[] args )
  {
    new JSON( args );
  }

  // METHODS
  public JSON( String[] args )
  {
    // Parse input or file by repeatedly calling first method defined in parser.
    Parser parser = Parser();
    if (args.length > 0)
    {
      try
      {
        String commandLine = args[0];
        for (int i=1; i<args.length; ++i) commandLine += " "+ args[i];
        if (File.exists(commandLine))
        {
          parser.open( new File(commandLine) );
        }
        else
        {
          parser.open( "[Command Line]", commandLine );
        }

        if ( !parser.methodAddresses.isEmpty() )
        {
          while (parser.hasAnother())
          {
            String cmd = parser.parse( parser.firstMethodAddress );
            System.out.println( cmd );
          }
        }
      }
      catch (Error error)
      {
        System.err.println( err );
      }
    }
    else
    {
      // Interactive mode
      for (;;)
      {
        try
        {
          local input = Console.input( "> " )
          parser.open( "[Command Line]", input )
          if ( !parser.methods.isEmpty() )
          {
            while (parser.has_another)
            {
              Cmd cmd = parser.parse( parser.firstMethodAddress );
              System.out.println( cmd );
            }
          }
        }
        catch (err:Error)
        {
          System.err.println( err );
        }
      }
    }
  }
}


