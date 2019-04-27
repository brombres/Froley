package froley.demo.json;

public class CmdFactory
{
  static public Cmd create( int index, Token t, CmdInitArgs args )
  {
    switch (index)
    {
      case 0:  return new Cmd.Binary().init( t, args );
      default: throw new Error( "[INTERNAL] CmdFactory.create() index out of bounds: " + index );
    }
  }
}

