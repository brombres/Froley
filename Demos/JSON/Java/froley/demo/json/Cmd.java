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
    args.requireCount( 0 );
    return this;
  }

  public String toString()
  {
    StringBuilder result = new StringBuilder();
    write( result );
    return result.toString();
  }

  public void write( StringBuilder builder )
  {
    String typeName = getClass().getName();
    int i = typeName.lastIndexOf( '$' );
    if (i == -1) i = typeName.lastIndexOf( "Cmd" );
    if (i != -1 && i == typeName.length()-1) i = -1;
    builder.print( (i==-1) ? typeName : typeName.substring(i+3) );
  }

  static public class List extends Cmd
  {
    //PROPERTIES
    public ArrayList<Cmd> list;

    //METHODS
    public List( Token t, CmdInitArgs args )
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

  static public class Statements extends List
  {
    public Statements( Token t, CmdInitArgs args )
    {
      super( t, args );
    }

    public void write( StringBuilder builder )
    {
      for (int i=0; i<list.size(); ++i)
      {
        Cmd statement = list.get( i );
        statement.write( builder );
        builder.print( '\n' );
      }
    }
  }

  static public class Unary extends Cmd
  {
    // PROPERTIES
    public Cmd operand;

    // METHODS
    public Cmd init( Token t, CmdInitArgs args )
    {
      this.t = t;
      args.requireCount( 1 );
      return this;
    }

    public String op()
    {
      throw new UnsupportedOperationException();
    }

    public void write( StringBuilder builder )
    {
      builder.print( '(' ).print( op() );
      operand.write( builder );
      builder.print( ')' );
    }
  }

  static public class PostUnary extends Unary
  {
    //METHODS
    public void write( StringBuilder builder )
    {
      builder.print( '(' );
      operand.write( builder );
      builder.print( op() ).print( ')' );
    }
  }

  static public class Binary extends Cmd
  {
    // PROPERTIES
    public Cmd left;
    public Cmd right;

    // METHODS
    public Cmd init( Token t, CmdInitArgs args )
    {
      this.t = t;
      args.requireCount( 2 );
      left = args.get( 0 );
      right = args.get( 1 );
      return this;
    }

    public String op()
    {
      throw new UnsupportedOperationException();
    }

    public void write( StringBuilder builder )
    {
      builder.print( '(' );
      left.write( builder );
      builder.print( ' ' ).print( op() ).print( ' ' );
      right.write( builder );
      builder.print( ')' );
    }
  }
}

