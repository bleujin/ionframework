/*  Copyright (c) 2006-2007, Vladimir Nikic
    All rights reserved.

    Redistribution and use of this software in source and binary forms,
    with or without modification, are permitted provided that the following
    conditions are met:

    * Redistributions of source code must retain the above
      copyright notice, this list of conditions and the
      following disclaimer.

    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the
      following disclaimer in the documentation and/or other
      materials provided with the distribution.

    * The name of HtmlCleaner may not be used to endorse or promote
      products derived from this software without specific prior
      written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.

    You can contact Vladimir Nikic by sending e-mail to
    nikic_vladimir@yahoo.com. Please include the word "HtmlCleaner" in the
    subject line.
*/

package net.ion.framework.convert.html;

import java.io.IOException;
import java.io.Writer;

import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.CharUtils;

/**
 * <p>HTML text token.</p>
 */
public class ContentNode implements BaseToken, HtmlNode {

    private StringBuilder content;
    private boolean blank ;

    public ContentNode(String content) {
        this(content.toCharArray(), content.length()) ;
    }

    ContentNode(char[] content, int len) {
        this.content = new StringBuilder(len + 16);
        this.content.append(content, 0, len);
        this.blank = isBlank(content) ;
    }

    private boolean isBlank(char[] content){
        if(content == null || content.length == 0) return true;
        for(char c : content)
            if(!Character.isWhitespace(c)) return false;

        return true;
    }
    
    public String toString() {
        return content.toString();
    }

    public StringBuilder getContent() {
        return content;
    }
    
    public boolean isBlank(){
    	return this.blank ;
    }

    public void serialize(Serializer serializer, Writer writer) throws IOException {
    	writer.write( content.toString() );
    }

}