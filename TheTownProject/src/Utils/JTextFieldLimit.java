package Utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <code>JTextField</code> with a character limit
 * <p>used for forms, and extention of {@code PlainDocument} with its
 *  default methods implemented</p>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @see GUI.FormGame
 * @see javax.swing.text.PlainDocument
 * @since 1.0
 */
public class JTextFieldLimit extends PlainDocument {
  private int limit;
  
  /**
   * Creates a text field with a character limit
   * <p>The only different method 
   * @param limit maximum number of characters 
   */
  public JTextFieldLimit(int limit) {
    super();
    this.limit = limit;
  }

  /**
   * Default constructor override ({@code PlainDocuemnt})
   * @param limit maximum number of characters
   * @param upper default constructor parameter
   * @see javax.swing.text.PlainDocument
   */
  public JTextFieldLimit(int limit, boolean upper) {
    super();
    this.limit = limit;
  }

  /**
   * Default method override ({@code PlainDocuemnt})
   * @param offset default method parameter
   * @param str default method parameter
   * @param attr default method parameter
   * @see javax.swing.text.PlainDocument
   * @throws BadLocationException 
   */
  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
    if (str == null)
      return;

    if ((getLength() + str.length()) <= limit) {
      super.insertString(offset, str, attr);
    }
  }
}