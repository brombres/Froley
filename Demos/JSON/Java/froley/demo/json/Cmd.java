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

  static public class CmdList extends Cmd
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

  static public class CmdStatements extends CmdList
  {
    public CmdStatements( Token t, CmdInitArgs args )
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

  static public class CmdUnary extends Cmd
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

  static public class CmdPostUnary extends CmdUnary
  {
    //METHODS
    public void write( StringBuilder builder )
    {
      builder.print( '(' );
      operand.write( builder );
      builder.print( op() ).print( ')' );
    }
  }

  static public class CmdBinary extends Cmd
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

  static public class CmdObject extends Cmd
  {
    // PROPERTIES
    public CmdList definitions;

    // METHODS
    public CmdObject()
    {
    }

    public CmdObject( Token t, CmdList definitions )
    {
      this.t = t;
      this.definitions = definitions;
    }

    public CmdObject init( Token t, CmdInitArgs args )
    {
      args.requireCount( 1 );
      this.t = t;
      definitions = (CmdList)args.get( 0 );
      return this;
    }

    public void write( StringBuilder builder )
    {
      definitions.write( builder );
    }
  }

  static public class CmdArray extends Cmd
  {
    // PROPERTIES
    public CmdList array;

    // METHODS
    public CmdArray()
    {
    }

    public CmdArray( Token t, CmdList array )
    {
      this.t = t;
      this.array = array;
    }

    public CmdArray init( Token t, CmdInitArgs args )
    {
      args.requireCount( 1 );
      this.t = t;
      array = (CmdList)args.get( 0 );
      return this;
    }

    public void write( StringBuilder builder )
    {
      array.write( builder );
    }
  }

  static public class CmdString extends Cmd
  {
    // PROPERTIES
    public String value;

    // METHODS
    public CmdString()
    {
    }

    public CmdString( Token t, String value )
    {
      this.t = t;
      this.value = value;
    }

    public CmdString init( Token t, CmdInitArgs args )
    {
      args.requireCount( 0 );
      this.t = t;
      value = t.content;
      return this;
    }

    public void write( StringBuilder builder )
    {
      builder.print( value );
    }
  }

  static public class CmdNumber extends Cmd
  {
    // PROPERTIES
    public double value;

    // METHODS
    public CmdNumber()
    {
    }

    public CmdNumber( Token t, double value )
    {
      this.t = t;
      this.value = value;
    }

    public CmdNumber init( Token t, CmdInitArgs args )
    {
      args.requireCount( 0 );
      this.t = t;
      value = StringUtility.stringToReal64( t.content );
      return this;
    }

    public void write( StringBuilder builder )
    {
      builder.print( ""+value );
    }
  }

  static public class CmdTrue extends Cmd
  {
    // METHODS
    public void write( StringBuilder builder )
    {
      builder.print( "true" );
    }
  }

  static public class CmdFalse extends Cmd
  {
    // METHODS
    public void write( StringBuilder builder )
    {
      builder.print( "false" );
    }
  }

  static public class CmdNull extends Cmd
  {
    // METHODS
    public void write( StringBuilder builder )
    {
      builder.print( "null" );
    }
  }

  static public class CmdMapping extends Cmd
  {
    // PROPERTIES
    public String name;
    public Cmd value;

    // METHODS
    public CmdMapping()
    {
    }

    public CmdMapping( Token t, String name, Cmd value )
    {
      this.t = t;
      this.name = name;
      this.value = value;
    }

    public CmdMapping init( Token t, CmdInitArgs args )
    {
      args.requireCount( 1 );
      this.t = t;
      name = t.content;
      value = args.get( 0 );
      return this;
    }

    public void write( StringBuilder builder )
    {
      builder.print( "Mapping(" );
      builder.print( name );
      builder.print( ',' );
      value.write( builder );
      builder.print( ')' );
    }
  }
}
