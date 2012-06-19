package net.ion.framework.template.parse;



/**
 * 테그에서 속성을 추출한다.
 *
 * <pre>
 *      [[--테그이름,속성1,속성2,속성3--]]
 *      --> 속성1
 *          속성2
 *          속성3
 *
 *      순서로 추출한다.
 *      만약 속성 값으로 ','(속성분리자)이 들어가야한다면
 *      '\,'을 입력하도록한다.
 *
 *      즉,
 *      [[--테그이름,속성1:나는\,나에요,속성2:속성2값,속성3:속성3값--]]
 *      --> 속성1:나는,나에요
 *          속성2:속성2값
 *          속성3:속성3값
 *
 *      parsing된 결과 mark 내 문자열 양끝의 공백문자는 trimming된다.
 *      단, 인덱스에는 영향을 미치지 않느다.
 *
 *      주의:
 *          [[--TAG,A1,A2,A3,,--]]
 *          --> 차례대로 : A1,A2,A3,빈문자열,빈문자열 로 파싱된다.
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

                        // , 를 속성 값으로 사용하면 속성 구분자가 ,이기 때문에 문제가 생긴다.
                        // 이럴 경우 ,를 \, 으로 사용한다.
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
