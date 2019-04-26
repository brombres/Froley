package froley.demo.json;

import java.util.ArrayList;

public class CmdInitArgs extends ArrayList<Cmd>
{
  // PROPERTIES
  public Token t;

  //METHODS
  public void requireCount( int requiredCount )
  {
    if (size() != requiredCount)
    {
      String message = "[INTERNAL] init(t,args) got " + size() + " arg";
      if (size() != 1) message += "s";
      message += ", expected " + requiredCount + ".";
      throw t.error( message );
    }
  }
}

