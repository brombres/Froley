package froley.demo.json;

import java.util.ArrayList;

public class Cmd
{
  // PROPERTIES
  public Token t;

  // METHODS
  public Cmd init( Token t, CmdInitArgs args )
  {
    this.t = t;
    args.require_count( 0 );
  }

  public String toString()
  {
    StringBuilder result = StringBuilder();
    write( result );
    return result.toString();
  }

  public void write( StringBuilder builder )
  {
    String typeName = getClass().name();
    int i = typeName.indexOf( "Cmd" );
    builder.print( (i==-1) ? typeName : typeName.substring(index+3) );
  }
}

public class CmdList extends Cmd
{
  //PROPERTIES
  public ArrayList<Cmd> list;

  //METHODS
  public CmdList( Token t, CmdInitArgs args )
  {
    this.t = t;
    list = new ArrayList<Cmd>( args.size() );
    for (int i=0; i<args.size(); ++i)
    {
      list.add( args.get(i) );
    }
  }

  public int count()
  {
    return list.size();
  }

  public Cmd first()
  {
    return list.get( 0 );
  }

  public Cmd get( int index )
  {
    return list.get( index );
  }

  public Cmd last()
  {
    return list.get( list.size()-1 );
  }
}

public class CmdStatements extends CmdList
{
  static public void write( StringBuilder builder )
  {
    for (int i=0; i<list.size(); ++i)
    {
      Cmd statement = list.get( i );
      statement.write( builder );
      builder.print( '\n' );
    }
  }
}

class CmdArgs : CmdList
  METHODS
    method write( builder:StringBuilder )
      forEach (arg at index in list)
        if (index > 0) builder.print( ',' )
        builder.print( arg )
      endForEach
endClass

class CmdUnary( t, operand:Cmd ) : Cmd
  METHODS
    method init( t, args:CmdInitArgs )
      args.require_count( 1 )
      operand = args.first

    method op->String
      throw UnsupportedOperationError()

    method write( builder:StringBuilder )
      builder.print( '(' ).print( op ).print( operand ).print( ')' )
endClass

class CmdPostUnary : CmdUnary
  METHODS
    method write( builder:StringBuilder )
      builder.print( '(' ).print( operand ).print( op ).print( ')' )
endClass

class CmdBinary( t, left:Cmd, right:Cmd ) : Cmd
  METHODS
    method init( t, args:CmdInitArgs )
      args.require_count( 2 )
      left = args.first
      right = args.last

    method op->String
      throw UnsupportedOperationError()

    method write( builder:StringBuilder )
      builder.print( '(' ).print( left ).print( ' ' ).print( op ).print( ' ' ).print( right ).print( ')' )
endClass


class CmdObject( t, definitions:Cmd ) : Cmd
  METHODS
    method init( t, args:CmdInitArgs )      args.require_count( 1 )
      definitions = args[ 0 ]

    method write( builder:StringBuilder )
      builder.print "Object($)"(definitions)
endClass

class CmdArray( t, array:Cmd ) : Cmd
  METHODS
    method init( t, args:CmdInitArgs )      args.require_count( 1 )
      array = args[ 0 ]

    method write( builder:StringBuilder )
      builder.print "Array($)"(array)
endClass

class CmdString( t, value:String ) : Cmd
  METHODS
    method init( t, args:CmdInitArgs )      args.require_count( 0 )
      value = t.content

    method write( builder:StringBuilder )
      builder.print value
endClass

class CmdNumber( t, value:Real64 ) : Cmd
  METHODS
    method init( t, args:CmdInitArgs )      args.require_count( 0 )
      value = t.content->Real64

    method write( builder:StringBuilder )
      builder.print value->String
endClass

class CmdTrue : Cmd
  METHODS
    method write( builder:StringBuilder )
      builder.print "true"
endClass

class CmdFalse : Cmd
  METHODS
    method write( builder:StringBuilder )
      builder.print "false"
endClass

class CmdNull : Cmd
  METHODS
    method write( builder:StringBuilder )
      builder.print "null"
endClass

class CmdMapping( t, name:String, value:Cmd ) : Cmd
  METHODS
    method init( t, args:CmdInitArgs )      args.require_count( 1 )
      name = t.content
      value = args[ 0 ]

    method write( builder:StringBuilder )
      builder.print "Mapping($,$)"(name,value)
endClass
