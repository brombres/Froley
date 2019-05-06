package froley.demo.json;

public class IntList
{
  public int[] data;
  public int   count;

  public IntList()
  {
  }

  public IntList( int initialCapacity )
  {
    data = new int[ initialCapacity ];
  }

  public IntList add( int value )
  {
    reserve( 1 );
    data[ count++ ] = value;
    return this;
  }

  public int capacity()
  {
    return (data == null) ? 0 : data.length;
  }

  public IntList clear()
  {
    count = 0;
    return this;
  }

  public boolean contains( int value )
  {
    return -1 != locate( value );
  }

  public IntList ensureCapacity( int minimumCapacity )
  {
    return reserve( minimumCapacity - count );
  }

  public IntList expand( int additionalElementCount )
  {
    return expandToCount( count + additionalElementCount );
  }

  public IntList expandToCount( int minimumCount )
  {
    if (minimumCount > count)
    {
      ensureCapacity( minimumCount );
      int count = this.count;
      int[] data = this.data;
      for (int i=minimumCount; --i>=count; )
      {
        data[ i ] = 0;
      }
      this.count = minimumCount;
    }
    return this;
  }

  public int first()
  {
    return data[ 0 ];
  }

  public int get( int index )
  {
    return data[ index ];
  }

  public IntList insert( int value, int atIndex )
  {
    reserve( 1 );
    int[] data = this.data;
    for (int i=count; --i>=atIndex; )
    {
      data[i+1] = data[i];
    }
    data[ atIndex ] = value;
    ++count;
    return this;
  }

  public int last()
  {
    return data[ count-1 ];
  }

  public int locate( int value )
  {
    int n = count;
    for (int i=0; i<count; ++i)
    {
      if (data[i] == value) return i;
    }
    return -1;
  }

  public boolean remove( int value )
  {
    int n = count;
    for (int i=0; i<n; ++i)
    {
      if (data[i] == value)
      {
        removeAt( i );
        return true;
      }
    }
    return false;
  }

  public int removeAt( int index )
  {
    int result = data[index];
    int n = --count;
    for (int i=index; i<n; ++i)
    {
      data[i] = data[i+1];
    }
    return result;
  }

  public int removeLast()
  {
    return removeAt( count-1 );
  }

  public IntList reserve( int additionalElementCount )
  {
    if (additionalElementCount <= 0) return this;
    if (data == null)
    {
      if (additionalElementCount < 10) additionalElementCount = 10;
      data = new int[ additionalElementCount ];
    }
    else
    {
      int requiredCapacity = count + additionalElementCount;
      int currentCapacity = data.length;
      if (requiredCapacity > currentCapacity)
      {
        int doubleCapacity = currentCapacity * 2;
        if (doubleCapacity > requiredCapacity) requiredCapacity = doubleCapacity;
        int[] newData = new int[ requiredCapacity ];
        int[] data = this.data;
        for (int i=count; --i>=0; )
        {
          newData[i] = data[i];
        }
        this.data = newData;
      }
    }
    return this;
  }

  public IntList set( int index, int value )
  {
    data[ index ] = value;
    return this;
  }

  public int[] toArray()
  {
    int[] result = new int[ count ];
    for (int i=count; --i>=0;)
    {
      result[i] = get(i);
    }
    return result;
  }

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.print( '[' );
    for (int i=0; i<count; ++i)
    {
      if (i > 0) builder.print( ',' );
      builder.print( get(i) );
    }
    builder.print( ']' );
    return builder.toString();
  }
}
