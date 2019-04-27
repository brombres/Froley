package froley.demo.json;

public class ParserOpcode
{
  static public int SYNTAX_ERROR           =  0;
  static public int RETURN                 =  1;
  static public int CALL                   =  2;
  static public int JUMP                   =  3;
  static public int JUMP_IF_TRUE           =  4;
  static public int JUMP_IF_FALSE          =  5;
  static public int ON_TOKEN_TYPE          =  6;
  static public int HAS_ANOTHER            =  7;
  static public int NEXT_HAS_ATTRIBUTE     =  8;
  static public int NEXT_IS_TYPE           =  9;
  static public int BEGIN_LIST             = 10;
  static public int CREATE_CMD             = 11;
  static public int CREATE_NULL_CMD        = 12;
  static public int CREATE_LIST            = 13;
  static public int CREATE_STATEMENTS      = 14;
  static public int CONSUME_EOLS           = 15;
  static public int CONSUME_TYPE           = 16;
  static public int MUST_CONSUME_TYPE      = 17;
  static public int SAVE_POSITION          = 18;
  static public int RESTORE_POSITION       = 19;
  static public int DISCARD_SAVED_POSITION = 20;
  static public int TRACE                  = 21;
  static public int PRINTLN_STRING         = 22;
  static public int PRINTLN_NUMBER         = 23;
  static public int POP_DISCARD            = 24;
  static public int PUSH_INT32             = 25;
  static public int DECLARE_VAR            = 26;
  static public int WRITE_VAR              = 27;
  static public int READ_VAR               = 28;
  static public int LOGICAL_NOT            = 29;
  static public int COMPARE_EQ             = 30;
  static public int COMPARE_NE             = 31;
  static public int COMPARE_LT             = 32;
  static public int COMPARE_LE             = 33;
  static public int COMPARE_GT             = 34;
  static public int COMPARE_GE             = 35;
}

