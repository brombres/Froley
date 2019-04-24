package froley.demo.json;

public class Int32List
{
  public int[] data;
  public int   count;

  public Int32List()
  {
  }

  public Int32List( int initialCapacity )
  {
    data = new int[ initialCapacity ];
  }

  public void add( int value )
  {
    reserve( 1 );
    data[ count++ ] = value;
  }

  public int capacity()
  {
    return (data == null) ? 0 : data.length;
  }

  public void clear()
  {
    count = 0;
  }

  public boolean contains( int value )
  {
    return -1 != locate( value );
  }

  public void ensureCapacity( int minimumCapacity )
  {
    reserve( minimumCapacity - count );
  }

  public void expand( int additionalElementCount )
  {
    expandToCount( count + additionalElementCount );
  }

  public void expandToCount( int minimumCount )
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
  }

  public int first()
  {
    return data[ 0 ];
  }

  public int get( int index )
  {
    return data[ index ];
  }

  public void insert( int value, int atIndex )
  {
    reserve( 1 );
    int[] data = this.data;
    for (int i=count; --i>=atIndex; )
    {
      data[i+1] = data[i];
    }
    data[ atIndex ] = value;
    ++count;
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

  public void reserve( int additionalElementCount )
  {
    if (additionalElementCount <= 0) return;
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
  }

  public void set( int index, int value )
  {
    data[ index ] = value;
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
    builder.append( '[' );
    for (int i=0; i<count; ++i)
    {
      if (i > 0) builder.append( ',' );
      builder.append( get(i) );
    }
    builder.append( ']' );
    return builder.toString();
  }
}

