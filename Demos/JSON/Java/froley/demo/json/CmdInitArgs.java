package froley.demo.json;

public class CmdInitArgs extends ArrayList<Cmd>
{
  // PROPERTIES
  public Token t;

  //METHODS
  public void requireCount( int requiredCount )
  {
    if (count != requiredCount)
    {
      String message = "init(t,args) got " + count + " arg";
      if (count != 1) message += "s";
      message += ", expected " + requiredCount + ".";
      throw t.error( "[RogueFroley]", message );
    }
  }
}

