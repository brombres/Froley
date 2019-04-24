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
    wordWrapMessage( 79, builder, null );

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

  public void wordWrapMessage( int width, StringBuilder builder, String allowBreakAfter )
  {
    // Prints a word-wrapped version of 'message' to the given
    // StringBuilder.  Existing newlines characters will cause a new line to
    // begin immediately.  Spaces immediately following existing newline
    // characters are preserved. If 'allowBreakAfter' is non-null then a word-wrap
    // line break may be inserted after any of the characters in that string if
    // no space has been found to break at.
    int i1=0, i2=0;
    int len = message.length();

    if (len == 0) return;

    int w = width;
    int initialIndent = 0;
    for (int i=0; i<len; ++i)
    {
      char ch = message.charAt( i );
      if (ch != ' ') break;
      ++initialIndent;
      --w;
      ++i1;
    }

    if (w <= 0)
    {
      w = width;
      initialIndent = 0;
      builder.print( '\n' );
    }
    else
    {
      for (int i=width-w; --i>=0; )
      {
        builder.print( ' ' );
      }
    }

    boolean needsNewline = false;
    while (i2 < len)
    {
      // find first \n, last space, or last break-after character
      while ((i2-i1) < w && i2 < len && message.charAt(i2) != '\n') i2++;

      if ((i2-i1) == w)
      {
        // No \n in allowed width
        if (i2 >= len)
        {
          // The rest of the text will fit as-is.
          i2 = len;
        }
        else if (message.charAt(i2) != '\n')
        {
          // Look for the last space within the given width
          while (message.charAt(i2)!=' ' && i2>i1) i2--;

          if (i2 == i1)
          {
            // Not found - move cursor to end and look for the last break-after
            // character.
            i2 = i1 + w;
            if (allowBreakAfter != null)
            {
              while (i2 > i1 && -1 != allowBreakAfter.indexOf(message.charAt(i2-1)) && i2>i1) i2--;
              if (i2 == i1)
              {
                // Could not find a break - just use the first 'w' characters.
                i2 = i1 + w;
              }
            }
          }
        }
      }

      if (needsNewline)
      {
        builder.print( '\n' );
        if (initialIndent > 0)
        {
          for (int i=initialIndent; --i>=0; ) builder.print( ' ' );
        }
      }

      for (int i=i1; i<i2; ++i) builder.print( message.charAt(i) );
      needsNewline = true;

      if (i2 == len)
      {
        return;
      }
      else
      {
        switch (message.charAt(i2))
        {
          case ' ':
            // Discard trailing spaces
            while (i2<len && message.charAt(i2)==' ') i2++;

            if (i2<len && message.charAt(i2)=='\n') i2++;

            i1 = i2;
            break;

          case '\n':
            ++i2;

            w = width;
            initialIndent = 0;
            for (int i=i2; i<len; ++i)
            {
              if (message.charAt(i) != ' ') break;
              ++initialIndent;
              --w;
              ++i2;
            }

            if (w <= 0)
            {
              w = width;
              initialIndent = 0;
            }
            else
            {
              for (int i=width-w; --i>=0; ) builder.print( ' ' );
            }
        }

        i1 = i2;
      }
    }
  }
}

