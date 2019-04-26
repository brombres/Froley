package froley.demo.json;

public class StringUtility
{
  static public void wordWrap( String text, int width, StringBuilder builder, String allowBreakAfter )
  {
    // Prints a word-wrapped version of 'text' to the given
    // StringBuilder.  Existing newlines characters will cause a new line to
    // begin immediately.  Spaces immediately following existing newline
    // characters are preserved. If 'allowBreakAfter' is non-null then a word-wrap
    // line break may be inserted after any of the characters in that string if
    // no space has been found to break at.
    int i1=0, i2=0;
    int len = text.length();

    if (len == 0) return;

    int w = width;
    int initialIndent = 0;
    for (int i=0; i<len; ++i)
    {
      char ch = text.charAt( i );
      if (ch != ' ') break;
      ++initialIndent;
      --w;
      ++i1;
    }

    if (w <= 0)
    {
      w = width;
      initialIndent = 0;
      builder.print( '\n' );
    }
    else
    {
      for (int i=width-w; --i>=0; )
      {
        builder.print( ' ' );
      }
    }

    boolean needsNewline = false;
    while (i2 < len)
    {
      // find first \n, last space, or last break-after character
      while ((i2-i1) < w && i2 < len && text.charAt(i2) != '\n') i2++;

      if ((i2-i1) == w)
      {
        // No \n in allowed width
        if (i2 >= len)
        {
          // The rest of the text will fit as-is.
          i2 = len;
        }
        else if (text.charAt(i2) != '\n')
        {
          // Look for the last space within the given width
          while (text.charAt(i2)!=' ' && i2>i1) i2--;

          if (i2 == i1)
          {
            // Not found - move cursor to end and look for the last break-after
            // character.
            i2 = i1 + w;
            if (allowBreakAfter != null)
            {
              while (i2 > i1 && -1 != allowBreakAfter.indexOf(text.charAt(i2-1)) && i2>i1) i2--;
              if (i2 == i1)
              {
                // Could not find a break - just use the first 'w' characters.
                i2 = i1 + w;
              }
            }
          }
        }
      }

      if (needsNewline)
      {
        builder.print( '\n' );
        if (initialIndent > 0)
        {
          for (int i=initialIndent; --i>=0; ) builder.print( ' ' );
        }
      }

      for (int i=i1; i<i2; ++i) builder.print( text.charAt(i) );
      needsNewline = true;

      if (i2 == len)
      {
        return;
      }
      else
      {
        switch (text.charAt(i2))
        {
          case ' ':
            // Discard trailing spaces
            while (i2<len && text.charAt(i2)==' ') i2++;

            if (i2<len && text.charAt(i2)=='\n') i2++;

            i1 = i2;
            break;

          case '\n':
            ++i2;

            w = width;
            initialIndent = 0;
            for (int i=i2; i<len; ++i)
            {
              if (text.charAt(i) != ' ') break;
              ++initialIndent;
              --w;
              ++i2;
            }

            if (w <= 0)
            {
              w = width;
              initialIndent = 0;
            }
            else
            {
              for (int i=width-w; --i>=0; ) builder.print( ' ' );
            }
        }

        i1 = i2;
      }
    }
  }
}

