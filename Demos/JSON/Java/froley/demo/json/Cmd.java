package froley.demo.json;

/*
class Cmd
{
  PROPERTIES
    t : Token

  METHODS
    method init( t, args:CmdInitArgs )
      args.require_count( 0 )

    method to->String
      local result = StringBuilder()
      write( result )
      return result->String

    method write( builder:StringBuilder )
      builder.print( type_name.after_any("Cmd") )
}

class CmdList : Cmd
  PROPERTIES
    list : Cmd[]

  METHODS
    method init( t )
      list = Cmd[](5)

    method init( t, args:CmdInitArgs )
      list = Cmd[]( args.count )
      list.add( forEach in args )

    method count->Int32
      return list.count

    method first->Cmd
      return list.first

    method get( index:Int32 )->Cmd
      return list[ index ]

    method last->Cmd
      return list.last
endClass

class CmdStatements : CmdList
  METHODS
    method write( builder:StringBuilder )
      builder.println( forEach in list )
endClass

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
*/
