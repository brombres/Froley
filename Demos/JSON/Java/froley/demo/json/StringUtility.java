package froley.demo.json;

public class StringUtility
{
  public String toEscapedASCII( String st )
  {
    boolean needsEscaping = false;
    int count = st.length();
    for (int i=count; --i>=0; )
    {
      char ch = st.charAt( i );
      if (ch < 32 || ch >= 127)
      {
        needsEscaping = true;
        break;
      }
    }
    if ( !needsEscaping ) return st;

    StringBuilder builder = new StringBuilder( (int)(st.length()*1.5) );
    for (int i=0; i<count; ++i)
    {
      char ch = st.charAt( i );
      switch (ch)
      {
        case '\0': builder.print( "\\0" ); break;
        case '\b': builder.print( "\\b" ); break;
        case '\f': builder.print( "\\f" ); break;
        case '\n': builder.print( "\\n" ); break;
        case '\r': builder.print( "\\t" ); break;
        default:
          if (ch <= 127)
          {
            // Octal escape
            builder.print( '\\' );
            builder.print( (char)('0'+(ch>>6)) );
            builder.print( (char)('0'+((ch>>3) & 7)) );
            builder.print( (char)('0'+(ch&7)) );
          }
          else
          {
            // Unicode escape
            builder.print( "\\u" );
            builder.print( toHexCharacter(ch>>12) );
            builder.print( toHexCharacter(ch>>8) );
            builder.print( toHexCharacter(ch>>4) );
            builder.print( toHexCharacter(ch) );
          }
      }
    }
    return builder.toString();
  }

  public char toHexCharacter( int value )
  {
    value &= 15;
    if (value <= 9) return (char)('0'+value);
    else            return (char)('A'+(value-10));
  }

}

