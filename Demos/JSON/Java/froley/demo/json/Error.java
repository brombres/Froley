package froley.demo.json;

public class Error extends Exception
{
  static public void main( String[] args )
  {
    String filepath = "froley/demo/json/Error.java";
    String source = new Scanner( new java.io.File(filepath) ).source;
    new Error( filepath, source, 35, 21, "Testing" ).print();
  }

  public String filepath;
  public String source;
  public String message;
  public int    line;
  public int    column;

  public Error( String filepath, String source, int line, int column, String message )
  {
    this.filepath = filepath;
    this.source   = source;
    this.message  = message;
    this.line     = line;
    this.column   = column;
  }

  public Error( String filepath, String message )
  {
    this.filepath = filepath;
    this.message  = message;
  }

  public Error( String message )
  {
    this.message = message;
  }

  public void print()
  {
    System.err.println( toString() );
  }

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    for (int i=0; i<79; ++i) builder.print( '=' );
    builder.print( '\n' );

    builder.print( "ERROR" );
    if (filepath != null)
    {
      builder.print( " in \"" ).print( filepath ).print( "\"" );
      if (line > 0)
      {
        builder.print( " line " ).print( line ).print( ", column " ).print( column );
      }
    }
    builder.print( "\n\n" );
    StringUtility.wordWrap( message, 79, builder, null );

    if (line>0 && column>0  && source!=null)
    {
      builder.print( "\n\n" );
      int skip = (column<=70) ? 0 : column-70;
      Scanner scanner = new Scanner( (filepath!=null)?filepath:"", source );
      while (scanner.hasAnother())
      {
        scanner.read();
        if (scanner.line == this.line)
        {
          if (skip > 0)
          {
            for (int i=skip; --i>=0; ) scanner.read();
          }
          for (int i=0; i<80; ++i)
          {
            char ch = scanner.read();
            builder.print( ch );
            if (ch == '\n') break;
          }
          break;
        }
      }
      for (int i=column-(skip+1); --i>=0; ) builder.print( ' ' );
      builder.print( '^' ).print( '\n' );
    }

    for (int i=0; i<79; ++i) builder.print( '=' );
    return builder.toString();
  }
}

