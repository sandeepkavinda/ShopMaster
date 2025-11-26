package utils;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;


public class ClipboardUtils {

    public static void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(selection, null);
    }
}
