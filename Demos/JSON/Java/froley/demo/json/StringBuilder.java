package froley.demo.json;

class StringBuilder implements CharSequence, Comparable<String>
{
  // Unlike java.lang.StringBuilder, this class implements Comparable<String> and
  // can be used to index into HashMap<String,OtherType> etc.
  public char[]  characters;
  public int     count;
  public int     hash;

  public int     unicode;
  public int     continuationCount;

  public boolean isValid;

  public StringBuilder()
  {
    this( 128 );
  }

  public StringBuilder( int minimumCapacity )
  {
    characters = new char[ minimumCapacity ];
  }

  public StringBuilder( String content )
  {
    this( content.length() );
    print( content );
  }

  public StringBuilder clear()
  {
    count = 0;
    hash = 0;
    isValid = true;
    return this;
  }

  public char charAt( int index )
  {
    return characters[ index ];
  }

  public int compareTo( String other )
  {
    return compareTo( (CharSequence) other );
  }

  public int compareTo( CharSequence other )
  {
    int thisLength  = count;
    int otherLength = other.length();
    int minLength = Math.min( thisLength, otherLength );

    for (int i=0; i<minLength; ++i)
    {
      char thisCh = characters[ i ];
      char otherCh = other.charAt( i );
      if (thisCh != otherCh)
      {
        if (thisCh < otherCh) return -1;
        else                  return  1;
      }
    }

    if (thisLength == otherLength) return  0;  // equal
    if (thisLength < otherLength)  return -1;
    return 1;
  }

  public boolean equals( Object other )
  {
    if (other instanceof CharSequence) return equals( (CharSequence) other );
    return this == other;
  }

  public boolean equals( CharSequence other )
  {
    if (count != other.length()) return false;
    if (hash != other.hashCode()) return false;
    for (int i=count; --i>=0; )
    {
      if (characters[i] != other.charAt(i)) return false;
    }
    return true;
  }

  public int hashCode()
  {
    return hash;
  }

  public int length()
  {
    return count;
  }

  public StringBuilder print( String value )
  {
    int this_count = count;
    int other_count = value.length();
    reserve( other_count );
    for (int i=0; i<other_count; ++i)
    {
      char ch = value.charAt( i );
      characters[ this_count+i ] = ch;
      hash = ((hash << 3) - hash) + ch;
    }
    count += other_count;
    return this;
  }

  public StringBuilder print( char value )
  {
    reserve( 1 );
    characters[ count++ ] = value;
    hash = ((hash << 3) - hash) + value;
    return this;
  }

  public StringBuilder print( int value )
  {
    return print( ""+value );
  }

  public StringBuilder reserve( int additional )
  {
    int requiredCapacity = count + additional;
    if (requiredCapacity >= characters.length)
    {
      int newCapacity = characters.length * 2;
      if (requiredCapacity > newCapacity) newCapacity = requiredCapacity;
      char[] newCharacters = new char[ newCapacity ];
      for (int i=count; --i>=0; )
      {
        newCharacters[i] = characters[i];
      }
      characters = newCharacters;
    }
    return this;
  }

  public StringBuilder subSequence( int i1, int limit )
  {
    StringBuilder result = new StringBuilder( limit - i1 );
    int code = 0;
    for (int i=limit; --i>=i1; )
    {
      char ch = characters[i];
      result.characters[i-i1] = ch;
      code = ((code << 3) - code) + ch;
    }
    result.hash = code;
    return result;
  }

  public char[] toArray()
  {
    char[] result = new char[ count ];
    for (int i=count; --i>=0;)
    {
      result[i] = charAt(i);
    }
    return result;
  }

  public String toString()
  {
    return new String( characters, 0, count );
  }

  public void writeUTF8Byte( int value )
  {
    value &= 255;
    if (continuationCount == 0)
    {
      if ((value & 0xC0) == 0x80)  // 10xxxxxx - UTF-8 continuation byte
      {
        isValid = false;
      }
      else
      {
        if (value < 0x80)  // 0xxxxxxx
        {
          writeUnicode( value );
        }
        else if ((value & 0xE0) == 0xC0)  // 110xxxxx
        {
          unicode = value & 0x1F;
          continuationCount = 1;
        }
        else if ((value & 0xF0) == 0xE0)  // 1110xxxx
        {
          unicode = value & 0xF;
          continuationCount = 2;
        }
        else if ((value & 0xF8) == 0xF0)  // 11110xxx
        {
          unicode = value & 7;
          continuationCount = 3;
        }
        else
        {
          isValid = false;
        }
      }
    }
    else
    {
      if ((value & 0xC0) != 0x80)
      {
        isValid = false;
      }
      else
      {
        unicode = (unicode << 6) | (value & 0x3F);
      }
      if (--continuationCount == 0) writeUnicode( unicode );
    }
  }

  public void writeUnicode( int code )
  {
    if (code >= 0 && code <= 0xFFFF)
    {
      // Standard UTF-16 encoding
      reserve( 1 );
      characters[ count++ ] = (char) code;
      hash = ((hash << 3) - hash) + code;
    }
    else if (code < 0 || code > 0x10FFFF)
    {
      // Invalid code point - write as character 0
      reserve( 1 );
      characters[ count++ ] = (char) 0;
      hash = ((hash << 3) - hash);
    }
    else
    {
      // Write 10000..0x10FFFF as surrogate pair
      reserve( 2 );
      code -= 0x10000;
      characters[ count++ ] = (char)(0xD800 + (code >> 10));   // high surrogate comes first
      characters[ count++ ] = (char)(0xDC00 + (code & 0x3FF)); // low surrogate
      hash = ((hash << 3) - hash) + characters[count-2];
      hash = ((hash << 3) - hash) + characters[count-1];
    }
  }

}
