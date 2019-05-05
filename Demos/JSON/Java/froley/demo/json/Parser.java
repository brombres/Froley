package froley.demo.json;

public class Parser
{
  // DEFINITIONS
  final static public int VERSION     = 1;
  final static public int MIN_VERSION = 1;

  // PROPERTIES
  public String   filepath;
  public int[]    code;
  public String[] strings;
  public Token[]  tokens;

  public HashMap<String,Integer> methods          = new HashMap<String,Integer>();
  public HashMap<Integer,String> methodsByAddress = new HashMap<Integer,String>();

  public IntList callStack           = new IntList();
  public IntList methodStack         = new IntList();
  public IntList numberStack         = new IntList();
  public ArrayList<Token> tokenStack = new ArrayList<Token>();

  public ArrayList<String> varNames  = new ArrayList<String>();
  public IntList           varValues = new IntList();
  public IntList           varFrames = new IntList().add( 0 );

  public ArrayList<ParsePosition> savedPositions = new ArrayList<ParsePosition>();

  public ArrayList<Cmd>   cmdQueue     = new ArrayList<Cmd>();
  public CmdInitArgs      cmdArgs      = new CmdInitArgs();
  public ArrayList<Token> listStartT   = new ArrayList<Token>();
  public IntList          listStartPos = new IntList();

  public int   position;
  public int   nextTokenType;
  public Token curToken;

  public Tokenizer tokenizer = new Tokenizer();

  // METHODS
  public Parser()
  {
    load( Code.parserCode );
  }

  public void reset()
  {
    callStack.clear();
    methodStack.clear();
    tokenStack.clear();
    numberStack.clear();
    varNames.clear();
    varValues.clear();
    varFrames.clear().add(0);
    savedPositions.clear();
    listStartT.clear();
    listStartPos.clear();
    position = 0;
    curToken = null;
    nextTokenType = -1;
    tokenizer.reset();
  }

  public void execute( int ip )
  {
    curToken = peek();
    methodStack.add( ip );
    for (;;)
    {
      int opcode = code[ ip++ ];
      // System.out.println( StringUtility.format(ip,'0',4) + " " + opcode );
      switch (opcode)
      {
        case ParserOpcode.SYNTAX_ERROR:
          if (position == tokens.count)
          {
            throw peek().error( "Syntax error - unexpected end of input." );
          }
          else
          {
            Token t = tokens[ position ];
            String sym = StringUtility.quoted( TokenType.symbols[t.type] );
            throw t.error( "Syntax error - unexpected " + sym + "." );
          }
        case ParserOpcode.RETURN:
          {
            if (callStack.count == 0) return;
            ip = callStack.removeLast();
            methodStack.removeLast();
            curToken = tokenStack.removeLast();
            int varCount = varFrames.removeLast();
            while (varNames.size() > varCount) varNames.remove( varNames.size()-1 );
            varValues.count = varCount;
            continue;
          }
        case ParserOpcode.CALL:
          callStack.add( ip+1 );
          tokenStack.add( curToken );
          curToken = peek();
          ip = code[ ip ];
          methodStack.add( ip );
          varFrames.add( varValues.count );
          continue;
        case ParserOpcode.JUMP:
          ip = code[ ip ];
          continue;
        case ParserOpcode.JUMP_IF_TRUE:
          if (numberStack.count>0 && 0!=numberStack.removeLast()) ip = code[ ip ];
          else                                                    ++ip;
          continue;
        case ParserOpcode.JUMP_IF_FALSE:
          if (numberStack.count>0 && 0==numberStack.removeLast()) ip = code[ ip ];
          else                                                    ++ip;
          continue;
        case ParserOpcode.ON_TOKEN_TYPE:
          if (code[ip] == nextTokenType)
          {
            ip += 2;
            curToken = read();
          }
          else
          {
            ip = code[ ip+1 ];
          }
          continue;
        case ParserOpcode.HAS_ANOTHER:
          numberStack.add( (nextTokenType!=-1)?1:0 )
          continue;
        case ParserOpcode.NEXT_HAS_ATTRIBUTE:
          numberStack.add( (nextTokenType != -1 && (TokenType.attributes[nextTokenType] & code[ip]) != 0) ? 1 : 0 );
          ++ip;
          continue;
        case ParserOpcode.NEXT_IS_TYPE:
          numberStack.add( (nextTokenType == code[ip++]) ? 1 : 0 );
          continue;
        case ParserOpcode.BEGIN_LIST:
          listStartT.add( peek() );
          listStartPos.add( cmdQueue.size() );
          continue;
        case ParserOpcode.CREATE_CMD:
          int cmdTypeIndex = code[ ip++ ];
          int argCount = code[ ip+++ ];
          cmdArgs.clear();
          if (argCount > 0)
          {
            int i1 = cmdQueue.size() - argCount;
            if (i1 < 0) throw curToken.error( "[INTERNAL] Command queue too small." );
            for (int i=i1; i<cmdQueue.size(); ++i)
            {
              cmdArgs.add( cmdQueue.get(i) );
            }
            while (cmdQueue.size() > i1) cmdQueue.remove( cmdQueue.size() - 1 );
          }
          cmdQueue.add( CmdFactory.createCmd(cmdTypeIndex,curToken,cmdArgs) );
          continue;

        case ParserOpcode.CREATE_NULL_CMD:
          cmdQueue.add( null );
          continue;

        case ParserOpcode.CREATE_LIST:
          if (listStartT.size() == 0) throw peek().error( "[INTERNAL] No prior beginList before calling createList/produceList." )
          Token t  = listStartT.removeLast();
          int   i1 = listStartPos.removeLast();
          cmdArgs.clear();
          for (int i=i1; i<cmdQueue.size(); ++i)
          {
            cmdArgs.add( cmdQueue.get(i) );
          }
          while (cmdQueue.size() > i1) cmdQueue.remove( cmdQueue.size() - 1 );
          cmdQueue.add( new CmdList(curToken,cmdArgs) );
          continue;

        case ParserOpcode.CREATE_STATEMENTS:
          if (listStartT.size() == 0) throw peek().error( "[INTERNAL] No prior beginList before calling createStatements/produceStatements." )
          Token t  = listStartT.removeLast();
          int   i1 = listStartPos.removeLast();
          cmdArgs.clear();
          for (int i=i1; i<cmdQueue.size(); ++i)
          {
            cmdArgs.add( cmdQueue.get(i) );
          }
          while (cmdQueue.size() > i1) cmdQueue.remove( cmdQueue.size() - 1 );
          cmdQueue.add( new CmdStatements(curToken,cmdArgs) );
          continue;

        case ParserOpcode.CONSUME_EOLS:
          // Called to automatically consume EOL tokens that occur in the
          // midst of parsing a unary or binary operator, like "a+\nb".
          while (nextTokenType == TokenType.EOL) read();
          continue;

        case ParserOpcode.CONSUME_TYPE:
          if (nextTokenType == code[ip++])
          {
            read();
            numberStack.add( 1 );
          }
          else
          {
            numberStack.add( 0 );
          }
          continue;

        case ParserOpcode.MUST_CONSUME_TYPE:
          if (nextTokenType == code[ip++])
          {
            read();
            continue;
          }
          else
          {
            throw peek().error(
                "Expected " + StringUtility.quoted(TokenType.symbols[code[ip]]) +
                ", found " + ((nextTokenType!=-1) ? StringUtility.quoted(peek().toString()) : "end of input") + "." );
          }

        case ParserOpcode.SAVE_POSITION:
          savedPositions.add( new ParsePosition(position,cmdQueue.size(),curToken) );
          continue;

        case ParserOpcode.RESTORE_POSITION:
        {
          int count = savedPositions.size();
          if (count == 0) throw peek().error( "[INTERNAL] No savePosition to restore." );
          int savedPosition = savedPositions.remove( count - 1 );
          position = savedPosition.position;
          int discardFrom = savedPosition.cmdCount;
          while (cmdQueue.size() > discardFrom) cmdQueue.remove( cmdQueue.size() - 1 );
          curToken = savedPosition.curToken;
          if (position < tokens.count) nextTokenType = tokens[position].type;
          else                         nextTokenType = -1;
          continue;
        }

        case ParserOpcode.DISCARD_SAVED_POSITION:
        {
          if (savedPositions.count) savedPositions.remove();
          int count = savedPositions.size();
          if (count > 0) savedPositions.remove( count - 1 );
          continue;
        }

        case ParserOpcode.TRACE:
          print( "Line " ).print( code[ip++] ).print( " next:" )
          if (nextTokenType != -1) print( peek() )
          else                       print( "end of input" )
          print( " opcode:" ).print( ParserOpcode(code[ip]) )
          println
          print "  "
          forEach (mIp at index in methodStack)
            if (index > 0) print " > "
            print methodsByAddress[ mIp ]
          endForEach
          println
          print "  ["
          forEach (cmd at index in cmdQueue)
            if (index > 0) print ","
            print select{ cmd:cmd.typeName || "null" }
          endForEach
          println "]"
          continue;

        case ParserOpcode.PRINTLN_STRING:
          println strings[ code[ip++] ]
          continue;

        case ParserOpcode.PRINTLN_NUMBER:
          System.out.println( numberStack.removeLast() );
          if (value == value.floor) println value->Int32
          else                      println value.format(".4")
          continue;

        case ParserOpcode.POP_DISCARD:
          numberStack.removeLast();
          continue;

        case ParserOpcode.PUSH_INT32:
          numberStack.add( code[ip++] )
          continue;

        case ParserOpcode.DECLARE_VAR:
          local name = strings[ code[ip++] ]
          local index = locateVar( name, varFrames.last )
          if (index.exists)
            throw Error( "A variable named '$' has already been declared in the current method."(name) )
          else
            vars.add( Variable(name,numberStack.removeLast()) )
          endIf
          continue;

        case ParserOpcode.WRITE_VAR:
          local name = strings[ code[ip+] ]
          local index = locateVar( name, 0, &mustLocate ).value
          vars[ index ] = Variable( name, numberStack.removeLast() )
          continue;

        case ParserOpcode.READ_VAR:
          local name = strings[ code[ip++] ]
          local index = locateVar( name, 0, &mustLocate ).value
          numberStack.add( vars[index].value )
          continue;

        case ParserOpcode.LOGICAL_NOT:
          numberStack.last = not numberStack.last
          continue;

        case ParserOpcode.COMPARE_EQ:
          local b = numberStack.removeLast();
          numberStack.add( numberStack.removeLast() == b )
          continue;

        case ParserOpcode.COMPARE_NE:
          local b = numberStack.removeLast();
          numberStack.add( numberStack.removeLast() != b )
          continue;

        case ParserOpcode.COMPARE_LT:
          local b = numberStack.removeLast();
          numberStack.add( numberStack.removeLast() < b )
          continue;

        case ParserOpcode.COMPARE_LE:
          local b = numberStack.removeLast();
          numberStack.add( numberStack.removeLast() <= b )
          continue;

        case ParserOpcode.COMPARE_GT:
          local b = numberStack.removeLast();
          numberStack.add( numberStack.removeLast() > b )
          continue;

        case ParserOpcode.COMPARE_GE:
          local b = numberStack.removeLast();
          numberStack.add( numberStack.removeLast() >= b )
          continue;

        others
          throw Error( "[INTERNAL]", "Unhandled parser opcode: " + ParserOpcode(opcode) )
      }
    }
  }

  public boolean hasAnother()
  {
    return (position < tokens.count)
  }

  public void load( data:Byte[] )
  {
    load( DataReader(data) )
  }

  public void load( reader:DataReader )
  {
    local version = reader.readInt32x
    require version >= MIN_VERSION
    local n = reader.readInt32x

    strings.reserve( n )
    loop (n) strings.add( reader.readString )

    // Method names & addresses
    n = reader.readInt32x
    methods.reserve( n )
    loop (n)
      local name = strings[ reader.readInt32x ]
      methods[ name ] = reader.readInt32x
      methodsByAddress[ methods[name] ] = name
    endLoop

    n = reader.readInt32x
    code.reserve( n )
    loop (n) code.add( reader.readInt32x )
  }

  public int locateVar( name:String, lowestIndex:Int32, &mustLocate )
  {
    forEach (index in vars.count-1 downTo lowestIndex)
      if (vars[index].name == name) return index
    endForEach
    if (mustLocate) throw Error( "No variable named '$' has been declared."(name) )
    return null
  }

  public Cmd parse( ruleName:String )
  {
    local index = methods.locate( ruleName )
    if (not index.exists) throw Error( "[INTERNAL]", "No parse rule '$' exists."(ruleName) )
    return parse( methods[index.value] )
  }

  public Cmd parse( address:Int32 )
  {
    callStack.clear
    cmdQueue.clear
    execute( address )
    if (cmdQueue.size()) return cmdQueue.removeLast();
    else                 return null
  }

  public Token peek()
  {
    if (position == tokens.count)
      if (tokens.count == 0) return Token( filepath, "", 0, 0, TokenType(0) )
      local t = tokens.last.cloned( TokenType.EOI )
      ++t.column
      return t
    else
      return tokens[ position ]
    endIf
  }

  public Token read()
  {
    ++position
    if (position < tokens.count) nextTokenType = tokens[position].type->Int32
    else                         nextTokenType = -1
    return tokens[ position-1 ]
  }

  public void open( filepath, content:String, startLine=1:Int32, startColumn=1:Int32 )
  {
    require tokenizer
    open( filepath, tokenizer.tokenize(filepath,content,startLine,startColumn) )
  }

  public void open( file:File )
  {
    require tokenizer
    open( file.filepath, tokenizer.tokenize(file) )
  }

  public void open( filepath, tokens:Token[] )
  {
    @tokens.clear
    @tokens.add( tokens )
    if (tokens.count)
      nextTokenType = tokens.first.type->Int32
    else
      nextTokenType = -1
    endIf
    position = 0
  }

  static public class ParsePosition
  {
    public int   position;
    public int   cmdCount;
    public Token curToken;

    public ParsePosition( int position, int cmdCount, Token curToken )
    {
      this.position = position;
      this.cmdCount = cmdCount;
      this.curToken = curToken;
    }
  }
}
