package froley.demo.json;

import java.util.ArrayList;

public class Tokenizer
{
  // DEFINITIONS
  final static public int VERSION     = 1;
  final static public int MIN_VERSION = 1;

  // PROPERTIES
  public int[]        code;
  public IntList      stack = new IntList();
  public String[]     strings;
  public EntryPoint[] entryPoints;

  public int ch;
  public int count;
  public int result;

  public StringBuilder buffer    = new StringBuilder();
  public ArrayList<Token> tokens = new ArrayList<Token>( 1024 );

  public String filepath;
  public String scanner;
  public int    line;
  public int    column;

  public int    startIP;

  public String idStart    = "<id_start>";
  public String idContinue = "<id_continue>";
  public int[]  idCharacters = new int[ 128 ];

  // METHODS
  public Tokenizer()
  {
    for (int i=idContinue.length(); --i>=0; )
    {
      idCharacters[ idContinue.charAt(i) & 127 ] = 2;  // A '2' can only be a continuation character
    }
    for (int i=idStart.length(); --i>=0; )
    {
      idCharacters[ idStart.charAt(i) & 127 ] = 1;  // A '1' can be a start or continuation character
    }
    load( Code.tokenizerCode );
  }

  public void add( int tokenType )
  {
    Token t = new Token( tokenType, filepath, scanner.source, line, column );
    if (0 != (TokenType._attributes[tokenType] & TokenTypeAttribute.CONTENT)) t.content = buffer.toString();
    tokens.add( t );
  }

  public void execute()
  {
    int ip = startIP;
    for (;;)
    {
      int opcode = code[ ip++ ];
      // System.out.println( StringUtility.format(ip,4,'0') + " " + opcode );
      switch (opcode)
      {
        case TokenizerOpcode.HALT:
          return;
        case TokenizerOpcode.RESTART:
          buffer.clear();
          ip = startIP;
          continue;
        case TokenizerOpcode.MODE_INT32:
          startIP = code[ ip++ ];
          continue;
        case TokenizerOpcode.ERROR:
          throw new Error( filepath, scanner.source, scanner.line, scanner.column, buffer.toString() );
        case TokenizerOpcode.MARK_SOURCE_POS:
          line   = scanner.line;
          column = scanner.column;
          continue;
        case TokenizerOpcode.CREATE_INT32:
          int tokenType = code[ ip++ ];
          add( tokenType );
          buffer.clear();
          continue;
        case TokenizerOpcode.JUMP:
          ip = code[ ip ];
          continue;
        case TokenizerOpcode.JUMP_EQ:
          if (result == 0) ip = code[ ip ];
          else             ++ip;
          continue;
        case TokenizerOpcode.JUMP_NE:
          if (result != 0) ip = code[ ip ];
          else             ++ip;
          continue;
        case TokenizerOpcode.JUMP_LT:
          if (result < 0) ip = code[ ip ];
          else            ++ip;
          continue;
        case TokenizerOpcode.JUMP_LE:
          if (result <= 0) ip = code[ ip ];
          else             ++ip;
          continue;
        case TokenizerOpcode.JUMP_GT:
          if (result > 0) ip = code[ ip ];
          else            ++ip;
          continue;
        case TokenizerOpcode.JUMP_GE:
          if (result >= 0) ip = code[ ip ];
          else             ++ip;
          continue;
        case TokenizerOpcode.CALL:
          stack.add( ip+1 );
          ip = code[ ip ];
          continue;
        case TokenizerOpcode.RETURN:
          if (stack.count == 0) throw Error( "[Compiled Code]", "'return' on empty stack." );
          ip = stack.removeLast();
          continue;
        case TokenizerOpcode.HAS_ANOTHER:
          result = (scanner.hasAnother() ? 1 : 0);
          continue;
        case TokenizerOpcode.PEEK_CH_INT32:
          ch = scanner.peek( code[ip++] );
          continue;
        case TokenizerOpcode.PEEK_CH_COUNT:
          ch = scanner.peek( count );
          continue;
        case TokenizerOpcode.READ_CH:
          ch = scanner.read();
          continue;
        case TokenizerOpcode.CONSUME_CHARACTER:
          int value = code[ip++];
          result = (scanner.consume((char)value) ? 1 : 0);
          continue;
        case TokenizerOpcode.NEXT_IS_CHARACTER:
          int value = code[ip++];
          result = (scanner.hasAnother() && scanner.peek(0) == value);
          continue;
        case TokenizerOpcode.SCAN_DIGITS:
          int minDigits = code[ ip ];
          int maxDigits = code[ ip+1 ];
          int base = code[ ip+2 ];
          int n = 0;
          ip += 3;
          ch = 0;
          for (int i=maxDigits; --i>=0; )
          {
            if ( !scanner.hasAnother() ) break;

            int value = StringUtility.characterToNumber( scanner.peek(0), base );
            if (value == -1) break;
            scanner.read();
            ch = ch * base + value;
            ++n;
          }
          result = n - minDigits;
          continue;
        case TokenizerOpcode.SCAN_IDENTIFIER:
          if (scanner.hasAnother() && id_characters[scanner.peek&0x7F] == 1)
          {
            buffer.clear();
            buffer.print( scanner.read );
            while (scanner.hasAnother() && id_characters[scanner.peek&0x7f])
            {
              buffer.print( scanner.read );
            }
            result = 1;
          }
          else
          {
            result = 0;
          }
          continue;
        case TokenizerOpcode.CLEAR_BUFFER:
          buffer.clear();
          continue;
        case TokenizerOpcode.COLLECT_CH:
          buffer.writeUnicode( ch );
          continue;
        case TokenizerOpcode.COLLECT_CHARACTER:
          buffer.writeUnicode( code[ip++] );
          continue;
        case TokenizerOpcode.COLLECT_STRING:
          buffer.print( strings[code[ip++]] );
          continue;
        case TokenizerOpcode.BUFFER_TO_LOWERCASE:
          for (int i=buffer.count; --i>=0; )
          {
            char ch = buffer.characters[ i ];
            if (ch >= 'A' && ch <= 'Z') buffer.characters[i] += ('a' - 'A');
          }
          continue;
        case TokenizerOpcode.PRINT_BUFFER:
          for (int i=0; i<buffer.count; ++i)
          {
            System.out.print( buffer.characters[i] );
          }
          continue;
        case TokenizerOpcode.PRINT_CH:
          System.out.print( (char) ch );
          continue;
        case TokenizerOpcode.PRINT_COUNT:
          System.out.print( count );
          continue;
        case TokenizerOpcode.PRINT_CHARACTER:
          System.out.print( (char)code[ip++] );
          continue;
        case TokenizerOpcode.PRINT_STRING:
          System.out.print( strings[code[ip++]] );
          continue;
        case TokenizerOpcode.COMPARE_CH_INT32:
          result = ch - code[ip++];
          continue;
        case TokenizerOpcode.COMPARE_COUNT_INT32:
          result = count - code[ip++];
          continue;
        case TokenizerOpcode.CH_IS_DIGIT_INT32:
          int base = code[ip++];
          int n = StringUtility.characterToNumber( ch, base );
          result = (n != -1) ? 1 : 0;
          continue;
        case TokenizerOpcode.CH_IS_DIGIT_COUNT:
          int base = code[ip++];
          int n = StringUtility.characterToNumber( ch, count );
          result = (n != -1) ? 1 : 0;
          continue;
        case TokenizerOpcode.CH_IS_LETTER:
          result = ((ch>='a' && ch<='z') || (ch>='A' && ch<='Z')) ? 1 : 0;
          continue;
        case TokenizerOpcode.PUSH_CH:
          stack.add( ch );
          continue;
        case TokenizerOpcode.PUSH_COUNT:
          stack.add( count );
          continue;
        case TokenizerOpcode.POP_CH:
          if (stack.count == 0) throw Error( "[Compiled Code]", "'pop ch' on empty stack." );
          ch = stack.removeLast();
          continue;
        case TokenizerOpcode.POP_COUNT:
          if (stack.count == 0) throw Error( "[Compiled Code]", "'pop count' on empty stack." );
          count = stack.removeLast();
          continue;
        case TokenizerOpcode.SET_CH_TO_INT32:
          ch = code[ ip++ ];
          result = ch;
          continue;
        case TokenizerOpcode.SET_CH_TO_COUNT:
          ch = count;
          result = ch;
          continue;
        case TokenizerOpcode.SET_COUNT_TO_INT32:
          count = code[ ip++ ];
          result = count;
          continue;
        case TokenizerOpcode.SET_COUNT_TO_CH:
          count = ch;
          result = count;
          continue;
        case TokenizerOpcode.SET_RESULT_TO_CH:
          result = ch;
          continue;
        case TokenizerOpcode.SET_RESULT_TO_COUNT:
          result = count;
          continue;
        case TokenizerOpcode.SET_RESULT_TO_INT32:
          result = code[ ip++ ];
          continue;
        case TokenizerOpcode.ADD_CH_COUNT:
          ch += count;
          continue;
        case TokenizerOpcode.ADD_CH_INT32:
          ch += code[ ip++ ];
          continue;
        case TokenizerOpcode.ADD_COUNT_INT32:
          count += code[ ip++ ];
          continue;
        case TokenizerOpcode.SUBTRACT_CH_COUNT:
          ch -= count;
          continue;
        case TokenizerOpcode.WHICH_INPUT:
          int curNode = ip;
          int lookahead = 0;
          int lastAcceptableNode      = 0;
          int lastAcceptableLinkCount = 0;
          int lastAcceptableLookahead = 0;
          for (;;)
          {
            int linkCount = code[curNode+1];
            if (code[curNode] != 0)
            {
              lastAcceptableNode = curNode;
              lastAcceptableLinkCount = linkCount;
              lastAcceptableLookahead = lookahead;
            }
            if ( !scanner.hasAnother(lookahead+1) ) break;
            int c = scanner.peek( lookahead );
            ip = curNode + 2;
            boolean foundLink = false;
            for (int i=linkCount; --i>=0; )
            {
              if (c == code[ip])
              {
                curNode = code[ ip+1 ];
                foundLink = true;
                break;
              }
              ip += 2;
            }
            if (foundLink) ++lookahead;
          }
          // Either no links match or EOI - jump to code of last acceptable node.
          // The start node is always acceptable and either contains the
          // 'others' case or jumps to the end of the scan table.
          ip = lastAcceptableNode + lastAcceptableLinkCount * 2 + 2;
          for (int i=lastAcceptableLookahead; --i>=0; ) buffer.print( scanner.read() );
          continue;

        case TokenizerOpcode.WHICH_BUFFER:
          int curNode   = ip;
          int startNode = curNode;
          int lookahead = 0;
          for (;;)
          {
            int linkCount = code[curNode+1];
            if (lookahead == buffer.count) break;
            int c = buffer[ lookahead ];
            ip = curNode + 2;
            boolean foundLink = false;
            for (int i=linkCount; --i>=0; )
            {
              if (c == code[ip])
              {
                curNode = code[ ip+1 ];
                foundLink = true;
                break;
              }
              ip += 2;
            }
            if (foundLink) ++lookahead;
          }

          // Either no links match or EOI
          if (code[curNode] == 0 || lookahead < buffer.count)
          {
            // Not a complete match; use the 'others' code in the start node
            curNode = startNode;
          }
          ip = curNode + code[curNode+1] * 2 + 2;
          continue;
        default:
          throw new Error( "[INTERNAL]", "Unhandled tokenizer opcode: " + opcode );
      }
    }
  }

  public void load( Byte[] data )
  {
    load( new Base64IntXReader(data) );
  }

  public void load( Base64IntXReader reader )
  {
    int version = reader.readInt32X();
    if (version >= MIN_VERSION) throw new FroleyError( "[INTERNAL]", "Unsupported tokenizer version: " + version );
    int n = reader.readInt32X();
    strings = new String[ n ];
    for (int i=0; i<n; ++i) strings[i] = reader.readString();

    n = reader.readInt32X();
    entryPoints = new EntryPoint[ n ];
    for (int i=0; i<n; ++i)
    {
      String name = strings[ reader.readInt32X() ];
      entryPoints[ i ] = new EntryPoint( name, reader.readInt32X() );
    }

    n = reader.readInt32X();
    code = new int[ n ];
    for (int i=0; i<n; ++i)
    {
      code[i] = reader.readInt32X();
    }
  }

  public Token[] tokenize( File file )
  {
    if (!file.exists) throw new FroleyError( "RogueFroley", "File not found: " + file );
    tokenize( file.toString(), StringUtility.load(file) );
    return tokens;
  }

  public Token[] tokenize( String filepath, String source )
  {
    return tokenize( filepath, source, 1, 1 );
  }

  public Token[] tokenize( String filepath, String source, int start_line, int start_column )
  {
    this.filepath = filepath;
    tokens.clear();
    scanner = new Scanner( source );
    scanner.line = start_line;
    scanner.column = start_column;
    execute();
    return tokens.toArray( new Token[ tokens.size() ] );
  }

  static public class EntryPoint
  {
    // PROPERTIES
    public String name;
    public int    ip;

    // METHODS
    public EntryPoint( String name, String ip )
    {
      this.name = name;
      this.ip = ip;
    }

    public String toString()
    {
      return StringUtility.format( ip, 4, '0' ) + " " + name;
    }
  }
}

