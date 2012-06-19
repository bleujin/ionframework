package net.ion.framework.template.parse;



/**
 * �ױ׿��� �Ӽ��� �����Ѵ�.
 *
 * <pre>
 *      [[--�ױ��̸�,�Ӽ�1,�Ӽ�2,�Ӽ�3--]]
 *      --> �Ӽ�1
 *          �Ӽ�2
 *          �Ӽ�3
 *
 *      ������ �����Ѵ�.
 *      ���� �Ӽ� ������ ','(�Ӽ��и���)�� �����Ѵٸ�
 *      '\,'�� �Է��ϵ����Ѵ�.
 *
 *      ��,
 *      [[--�ױ��̸�,�Ӽ�1:����\,������,�Ӽ�2:�Ӽ�2��,�Ӽ�3:�Ӽ�3��--]]
 *      --> �Ӽ�1:����,������
 *          �Ӽ�2:�Ӽ�2��
 *          �Ӽ�3:�Ӽ�3��
 *
 *      parsing�� ��� mark �� ���ڿ� �糡�� ���鹮�ڴ� trimming�ȴ�.
 *      ��, �ε������� ������ ��ġ�� �ʴ���.
 *
 *      ����:
 *          [[--TAG,A1,A2,A3,,--]]
 *          --> ���ʴ�� : A1,A2,A3,���ڿ�,���ڿ� �� �Ľ̵ȴ�.
 * </pre>
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class AttributeParser extends Parser
{
    private static final String TAG_OPEN=TagParser.TAG_OPEN;
    private static final String TAG_CLOSE=TagParser.TAG_CLOSE;

    private static final char NAME_SEPARATOR=TagNameParser.NAME_SEPARATOR; // ','
    public static final char ATTR_SEPARATOR=':';

    public Marker parseNext() throws ParserException
    {
        String text=getText();

        if(!(text.startsWith(TAG_OPEN) && text.endsWith(TAG_CLOSE))) {
            throw new ParserException("incorrect tag.");
        }

        int parsingPoint=getParsingPoint();

        int start=parsingPoint;
        int end=0;
        Marker mark=null;

        StringBuffer buff=new StringBuffer();
        boolean isStarted=false;

        PARSE: {
            for(int length=text.length() - TAG_CLOSE.length(),i=parsingPoint;i < length;++i) {
                char c=text.charAt(i);
                switch(c) {
                    case NAME_SEPARATOR: //','
                        if(isStarted) {
                            end=i;
                            mark=new Marker(start,end,buff.toString().trim());
                            break PARSE;
                        }
                        else {
                            start=i + 1;
                            isStarted=true;
                        }
                        break;

                        // , �� �Ӽ� ������ ����ϸ� �Ӽ� �����ڰ� ,�̱� ������ ������ �����.
                        // �̷� ��� ,�� \, ���� ����Ѵ�.
                    case '\\':
                        if(i + 1 < length &&
                           text.charAt(i + 1) == ',') {
                            ++i;
                            c=',';
                        }
                        // no break,do default!
                    default:
                        if(isStarted) {
                            buff.append(c);
                        }
                        break;
                }
            }

            if(isStarted) {
                mark=new Marker(start,text.length() - TAG_CLOSE.length(),buff.toString().trim());
            }
        }

        setParsingPoint(mark);
        return mark;
    }

//    public static void main( String[] args ) throws ParserException
//    {
//
//        String s = "[[--name,\\\\  attr1\\=fdgdshds\\,\\,\\,\\s   ,, attr2 ,     attr3 ,attr4 ,sss,--]]";
//        Parser p = new AttributeParser();
//        p.initialize( s );
//
//        Mark m;
//
//        while ( ( m = p.parseNext() ) != null )
//        {
//            System.out.println( m.getParsedString() );
//        }
//    }
}
