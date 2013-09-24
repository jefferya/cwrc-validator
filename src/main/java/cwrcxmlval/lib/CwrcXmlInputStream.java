package cwrcxmlval.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mpm1
 */
public class CwrcXmlInputStream extends Reader {

    private Reader data;
    private File temp;
    private Writer tmpWriter;

    public CwrcXmlInputStream(Reader data) {
        this.data = data;
        try {
            temp = File.createTempFile("tempfile" + System.currentTimeMillis(), ".tmp");
            tmpWriter = new FileWriter(temp);
        } catch (IOException ex) {
            Logger.getLogger(CwrcXmlInputStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getLine(int line) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(temp));
        
        String lineString = "";
        for(int i = 0; i < line; ++i){
            lineString = reader.readLine();
        }
        
        reader.close();
        return lineString;
    }

    @Override
    public void close() throws IOException {
        data.close();
    }

    @Override
    public synchronized void mark(int i) throws IOException {
        data.mark(i);
    }

    @Override
    public boolean markSupported() {
        return data.markSupported();
    }

    @Override
    public int read(char[] chars) throws IOException {
        return read(chars, 0, chars.length);
    }

    @Override
    public int read(char[] chars, int i, int i1) throws IOException {
        int result = data.read(chars, i, i1);
        
        tmpWriter.write(chars, i, i1);
        tmpWriter.flush();
        
        return result;
    }

    @Override
    public int read(CharBuffer cb) throws IOException {
        return data.read(cb);
    }

    @Override
    public int read() throws IOException {
        char chars[] = new char[1];
        int result = read(chars);

        if (result > 0) {
            return (int) chars[0];
        }

        return result;
    }

    @Override
    public boolean ready() throws IOException {
        return data.ready();
    }

    @Override
    public synchronized void reset() throws IOException {
        data.reset();
    }

    @Override
    public long skip(long l) throws IOException {
        return data.skip(l);
    }
}
