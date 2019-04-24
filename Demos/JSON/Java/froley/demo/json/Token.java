package froley.demo.json;

import java.io.*;
import java.util.*;

public class Token
{
  // GLOBAL PROPERTIES
  static public String nextFilepath;
  static public String nextSource;
  static public int    nextLine;
  static public int    nextColumn;

  // PROPERTIES
  public String filepath;
  public String source;
  public String content;
  public int    type;
  public int    line;
  public int    column;

  // METHODS
  public Token( int type )
  {
    this( type, null );
  }

  public Token( int type, String content )
  {
    this( type, content, nextFilepath, nextSource, nextLine, nextColumn );
  }

  public Token( int type, String content, String filepath, String source, int line, int column )
  {
    this.type     = type;
    this.content  = content;
    this.filepath = filepath;
    this.source   = source;
    this.line     = line;
    this.column   = column;
  }

  public Token cloned()
  {
    return new Token(
    method cloned->Token
      return Token( type, filepath, source, line, column, content )

    method error( message:String )->Error
      return Error( filepath, source, line, column, message )

    method to->String
      if (content) return content
      return type.symbol.to_escaped_ascii
}

